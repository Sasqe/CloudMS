package com.gcu.controller;

import java.nio.charset.StandardCharsets;

import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;
import java.security.spec.EncodedKeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.sql.BatchUpdateException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.Iterator;

import com.gcu.model.Block;
import com.gcu.model.Wallet;
import com.gcu.transaction.Transaction;
import com.gcu.transaction.TransactionInput;
import com.gcu.transaction.TransactionOutput;
import com.google.gson.GsonBuilder;

public class rainfall {
	

	public static ArrayList<Block> blockchain = new ArrayList<Block>();
	public static HashMap<String,TransactionOutput> UTXOs = new HashMap<String,TransactionOutput>();
	
	public static int difficulty = 3;
	public static float minimumTransaction = 0.1f;
	public static Wallet walletA;
	public static Wallet walletB;
	public static Transaction genesisTransaction;
	static String databaseURL = "jdbc:mysql://localhost:8889/crypto";
	static String user = "root";
	static String password = "root";
	public static void main(String[] args) throws SQLException {	
		test();
		
		
	}
	public static KeyPair createKeyPair(byte[] encodedPrivateKey, byte[] encodedPublicKey)
	{
		 try {
		        EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(encodedPrivateKey);
		        KeyFactory generator = KeyFactory.getInstance("ECDSA","BC");
		        PrivateKey privateKey = generator.generatePrivate(privateKeySpec);

		        EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(encodedPublicKey);
		        PublicKey publicKey = generator.generatePublic(publicKeySpec);
		        return new KeyPair(publicKey, privateKey);
		    } catch (Exception e) {
		        throw new IllegalArgumentException("Failed to create KeyPair from provided encoded keys", e);
		    }
	}
	public static PublicKey createPublicKey(byte[] encodedKey) {
		 try {
			 	if (encodedKey == null) {
			 		return null;
			 	}
			    KeyFactory generator = KeyFactory.getInstance("ECDSA","BC");
		        EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(encodedKey);
		        PublicKey publicKey = generator.generatePublic(publicKeySpec);
		        return publicKey;
		    } catch (Exception e) {
		        throw new IllegalArgumentException("Failed to create KeyPair from provided encoded keys", e);
		    }
	}
	public static Boolean isChainValid() {
		Block currentBlock; 
		Block previousBlock;
		String hashTarget = new String(new char[difficulty]).replace('\0', '0');
		HashMap<String,TransactionOutput> tempUTXOs = new HashMap<String,TransactionOutput>(); //a temporary working list of unspent transactions at a given block state.
		tempUTXOs.put(genesisTransaction.outputs.get(0).id, genesisTransaction.outputs.get(0));
		
		//loop through blockchain to check hashes:
		for(int i=1; i < blockchain.size(); i++) {
			
			currentBlock = blockchain.get(i);
			previousBlock = blockchain.get(i-1);
			//compare registered hash and calculated hash:
			if(!currentBlock.hash.equals(currentBlock.calculateHash()) ){
				System.out.println("#Current Hashes not equal");
				return false;
			}
			//compare previous hash and registered previous hash
			if(!previousBlock.hash.equals(currentBlock.previousHash) ) {
				System.out.println("#Previous Hashes not equal");
				return false;
			}
			//check if hash is solved
			if(!currentBlock.hash.substring( 0, difficulty).equals(hashTarget)) {
				System.out.println("#This block hasn't been mined");
				return false;
			}
			
			//loop thru blockchains transactions:
			TransactionOutput tempOutput;
			for(int t=0; t <currentBlock.transactions.size(); t++) {
				Transaction currentTransaction = currentBlock.transactions.get(t);
				
				if(!currentTransaction.verifiySignature()) {
					System.out.println("#Signature on Transaction(" + t + ") is Invalid");
					return false; 
				}
				if(currentTransaction.getInputsValue() != currentTransaction.getOutputsValue()) {
					System.out.println("#Inputs are note equal to outputs on Transaction(" + t + ")");
					return false; 
				}
				
				for(TransactionInput input: currentTransaction.inputs) {	
					tempOutput = tempUTXOs.get(input.transactionOutputId);
					
					if(tempOutput == null) {
						System.out.println("#Referenced input on Transaction(" + t + ") is Missing");
						return false;
					}
					
					if(input.UTXO.value != tempOutput.value) {
						System.out.println("#Referenced input Transaction(" + t + ") value is Invalid");
						return false;
					}
					
					tempUTXOs.remove(input.transactionOutputId);
				}
				
				for(TransactionOutput output: currentTransaction.outputs) {
					tempUTXOs.put(output.id, output);
				}
				
				if( currentTransaction.outputs.get(0).reciepient != currentTransaction.reciepient) {
					System.out.println("#Transaction(" + t + ") output reciepient is not who it should be");
					return false;
				}
				if( currentTransaction.outputs.get(1).reciepient != currentTransaction.sender) {
					System.out.println("#Transaction(" + t + ") output 'change' is not sender.");
					return false;
				}
				
			}
			
		}
		System.out.println("Blockchain is valid");
		return true;
	}
	public static void addNew(Block newBlock) throws SQLException {
		newBlock.mineBlock(difficulty);
		blockchain.add(newBlock);
	}
	public static void addBlock(Block newBlock) throws SQLException {
		newBlock.mineBlock(difficulty);
		blockchain.add(newBlock);
	}
	public static void test() throws SQLException {
		//add our blocks to the blockchain ArrayList:
				Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider()); //Setup Bouncey castle as a Security Provider
				//Create wallets:
				walletA = new Wallet();
				walletB = new Wallet();
				PublicKey ogPK = walletA.publicKey;
				PrivateKey ogPRK = walletA.privateKey;
				Wallet coinbase = new Wallet();
				
				//create genesis transaction, which sends 100 NoobCoin to walletA: 
				genesisTransaction = new Transaction(coinbase.publicKey, walletA.publicKey, 100f, null);
				genesisTransaction.generateSignature(coinbase.privateKey);	 //manually sign the genesis transaction	
				genesisTransaction.transactionId = "0"; //manually set the transaction id
				genesisTransaction.outputs.add(new TransactionOutput(genesisTransaction.reciepient, genesisTransaction.value, genesisTransaction.transactionId)); //manually add the Transactions Output
				UTXOs.put(genesisTransaction.outputs.get(0).id, genesisTransaction.outputs.get(0)); //its important to store our first transaction in the UTXOs list.
	}
	
//				
//				System.out.println("Creating and Mining Genesis block... ");
//				Block genesis = new Block("0");
//				genesis.addTransaction(genesisTransaction);
//				// MINE BEFORE INSERTION, ADD TO DATABASE INSTEAD OF LOCAL ARRAYLIST
//				addBlock(genesis);
//				//testing
//				Block block1 = new Block(genesis.hash);
//				System.out.println("\nWalletA's balance is: " + walletA.getBalance());
//				System.out.println("\nWalletA is Attempting to send funds (40) to WalletB...");
//				block1.addTransaction(walletA.sendFunds(walletB.publicKey, 40f));
//				//addBlock(block1);
//				addNew(block1);
//				System.out.println("\nWalletA's balance is: " + walletA.getBalance());
//				System.out.println("WalletB's balance is: " + walletB.getBalance());
//				
//				Block block2 = new Block(block1.hash);
//				System.out.println("\nWalletA Attempting to send more funds (1000) than it has...");
//				block2.addTransaction(walletA.sendFunds(walletB.publicKey, 1000f));
//				//addBlock(block2);
//				addNew(block2);
//				System.out.println("\nWalletA's balance is: " + walletA.getBalance());
//				System.out.println("WalletB's balance is: " + walletB.getBalance());
//				
//				Block block3 = new Block(block2.hash);
//				System.out.println("\nWalletB is Attempting to send funds (20) to WalletA...");
//				block3.addTransaction(walletB.sendFunds( walletA.publicKey, 20));
//				//addBlock(block3);
//				addNew(block3);
//				System.out.println("\nWalletA's balance is: " + walletA.getBalance());
//				System.out.println("WalletB's balance is: " + walletB.getBalance());
//				isChainValid();
				
}

