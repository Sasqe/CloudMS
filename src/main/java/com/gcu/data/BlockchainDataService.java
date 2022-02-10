package com.gcu.data;

import java.security.PublicKey;
import java.security.Security;
import java.sql.BatchUpdateException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.sql.DataSource;
import javax.sql.RowSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Service;

import com.gcu.controller.rainfall;
import com.gcu.model.Block;
import com.gcu.model.CredentialsModel;
import com.gcu.model.UserModel;
import com.gcu.model.Wallet;
import com.gcu.transaction.Transaction;
import com.gcu.transaction.TransactionInput;
import com.gcu.transaction.TransactionOutput;
/**
 * All the Data layer logic for users
 */
@Service
public class BlockchainDataService implements IDataAccess<Block> 
{
	//Autowired datasource injections
	@Autowired
	private DataSource dataSource;
	private JdbcTemplate jdbcTemplateObject;
	//Constructor
	/**
	 * Adds the data source to the data service when its instantiated
	 * @param dataSource (source of data)
	 */
	public BlockchainDataService(DataSource dataSource) 
	{
		//set data source and template object
		this.dataSource = dataSource;
		this.jdbcTemplateObject = new JdbcTemplate(dataSource);
	}
	@Override
	public ArrayList<Block> read() throws SQLException {
		
			// TESTING ADDING GENESIS BLOCK TO BLOCKCHAIN
		String sql = "SELECT * FROM blockchain LEFT JOIN transactions ON transactions.block_id = blockchain.id LEFT JOIN inputs ON inputs.transactions_ID = transactions.id LEFT JOIN inpututxos ON inpututxos.input_ID = inputs.id LEFT JOIN outputs ON outputs.transactions_ID = transactions.id ORDER BY blockchain.id ASC";
		ArrayList<Block> retrievedBlocks = new ArrayList<Block>();
				boolean isset_retrievedTransactions = false;
				SqlRowSet srs = jdbcTemplateObject.queryForRowSet(sql);
				byte[] transactionSender = null;
				byte[] transactionRecipient = null;
				ArrayList<Transaction> transactions = new ArrayList<Transaction>();
				int i = 0;
				while (srs.next()) {
					//Blockchain
					Block block = new Block();
					int id = srs.getInt("id"); System.out.println(id);
				    String hash = srs.getString("hash");
				    String previoushash = srs.getString("previousHash");
				    String merkleRoot = srs.getString("merkleRoot");
				    long timestamp = srs.getLong("timestamp");
				    int nonce = srs.getInt("nonce");
				    block.setHash(hash);
				    block.setPreviousHash(previoushash);
				    block.setMerkleRoot(merkleRoot);
				    block.setTimeStamp(timestamp);
				    block.setNonce(nonce);
				    if (!retrievedBlocks.contains(block))
				    retrievedBlocks.add(block);
				    // Transactions
				    Transaction transaction = new Transaction();
				    int block_id = srs.getInt("block_id");
				    String thistransactionID = srs.getString("transactionID");
				    transactionSender = (byte[]) srs.getObject("sender");
				    transactionRecipient = (byte[]) srs.getObject("recipient");
				    PublicKey retSender = rainfall.createPublicKey(transactionSender);
				    PublicKey retRecipient = rainfall.createPublicKey(transactionRecipient);
				    float transactionsvalue = srs.getFloat("value");
				    byte[] signature = null;
				    signature = (byte[]) srs.getObject("signature");
				    transaction.setTransactionId(thistransactionID);
				    transaction.setSender(retSender);
				    transaction.setReciepient(retRecipient);
				    transaction.setValue(transactionsvalue);
				    transaction.setSignature(signature);
				    if (!retrievedBlocks.get(i).transactions.contains(transaction))
				    {
				    	retrievedBlocks.get(i).transactions.add(transaction);
				    }
//		                
				    // Inputs
				    TransactionInput input = new TransactionInput();
				    int inputs_transaction_id = srs.getInt("transactions_id");
				    String inputs_transactionoutputID = srs.getString("transactionoutputID");
				    // - input's UTXO
				    int utxo_inputID = srs.getInt("input_ID");
				    String utxo_stringID = srs.getString("stringID");
				    byte[] utxoRecipient = null;
				    utxoRecipient = (byte[]) srs.getObject("recipient");
				    float utxoValue = srs.getFloat("value");
				    String utxoparentTransactionID = srs.getString("parentTransactionID");
				    input.setTransactionOutputId(inputs_transactionoutputID);
				    TransactionOutput UTXO = new TransactionOutput();
				    UTXO.setId(utxo_stringID);
				    UTXO.setReciepient(rainfall.createPublicKey(utxoRecipient));
				    UTXO.setValue(utxoValue);
				    UTXO.setParentTransactionId(utxoparentTransactionID);
				    input.UTXO = UTXO;
				    if (block.transactions.size() > 0 && i < block.transactions.size()) {
				    isset_retrievedTransactions = true;
				    if(!retrievedBlocks.get(i).transactions.get(i).inputs.contains(input))
				    	retrievedBlocks.get(i).transactions.get(i).inputs.add(input);
				    } else { isset_retrievedTransactions = false; }
				    
				    TransactionOutput output = new TransactionOutput();
				    int outputs_transactionsID = srs.getInt("transactions_ID");
				    String outputID = srs.getString("outputID");
				    byte[] recipient = null;
				    recipient = (byte[]) srs.getObject("recipient");
				    float value = srs.getFloat("value");
				    String parentTransactionID = srs.getString("parentTransactionID");
				    output.setId(outputID);
				    output.setReciepient(rainfall.createPublicKey(recipient));
				    output.setValue(value);
				    output.setParentTransactionId(parentTransactionID);
				    if (isset_retrievedTransactions) {
				    	if (!retrievedBlocks.get(i).transactions.get(i).outputs.contains(output))
				    block.transactions.get(i).outputs.add(output);
				    } else { }
					i++;
				}
				// TEST CASES SAVE ALL KEYS AS VARIABLES, ENCRYPT & COMPARE
				PublicKey Sender = null;
				PublicKey Recipient = null;
				return retrievedBlocks;
			}
		
	
	@Override
	public boolean create(Block newBlock) {
		newBlock.mineBlock(3);
		System.out.println("TRANSACTIONS SIZE :: " + newBlock.getTransactions().size());
		// TESTING ADDING GENESIS BLOCK TO BLOCKCHAIN
			// SQL STRINGS
			String sql1 = "" // BLOCKCHAIN
			+ "INSERT INTO `blockchain`(`hash`, `previousHash`, `merkleRoot`, `timestamp`, `nonce`) VALUES (?,?,?,?,?);";
			// TRANSACTIONS
			String sql2 = " INSERT INTO `transactions`(`block_id`, `transactionID`, `sender`, `recipient`, `value`, `signature`) VALUES ((SELECT blockchain.id FROM blockchain ORDER BY `id` DESC LIMIT 1),?,?,?,?,?);"; 
			// INPUTS 
  			String sql3 = "INSERT INTO `inputs`(`transactions_ID`, `transactionoutputID`) VALUES ((SELECT transactions.id FROM transactions ORDER BY `id` DESC LIMIT 1),?);";
  			// INPUT UTXOS
  			String sql4 = "INSERT INTO `inpututxos`(`input_ID`, `stringID`, `recipient`, `value`, `parentTransactionID`) VALUES ((SELECT inputs.id FROM inputs ORDER BY `id` DESC LIMIT 1),?,?,?,?);";
			// OUTPUTS
  			String sql5 = "INSERT INTO `outputs`(`transactions_ID`, `outputID`, `recipient`, `value`, `parentTransactionID`) VALUES ((SELECT transactions.id FROM transactions ORDER BY `id` DESC LIMIT 1),?,?,?,?);";
//			// INSERT INTO BLOCKCHAIN
			try 
			{
				  int rows = jdbcTemplateObject.update(sql1, newBlock.getHash(),
						  														newBlock.getPreviousHash(),
						  														newBlock.getMerkleRoot(),
						  														newBlock.getTimeStamp(),
						  														newBlock.getNonce());
				  System.out.println("ADDING BLOCK BATCH");
//				  // log original sender and recipient hashes
				  PublicKey ogSender = null;
				  PublicKey ogRecipient = null;
				  Boolean isset_transactions = false;
				  if (newBlock.getTransactions().size() > 0) {
				  isset_transactions = true;
				  ogSender = newBlock.getTransactions().get(newBlock.getTransactions().size() - 1).sender;
				  ogRecipient = newBlock.getTransactions().get(newBlock.getTransactions().size() - 1).reciepient;
				  }
				  //ITERATE THROUGH TRANSACTIONS
				  int rows2 = 0;
				  if (isset_transactions) {
				  Iterator<Transaction> iterator1 = newBlock.getTransactions().iterator();
					  System.out.println("ITERATING");
			      while (iterator1.hasNext()) {
					  Transaction t = iterator1.next();
					  //ENCODE
					  byte[] senderEncoded = t.sender.getEncoded(); // sender's encoded bytes
					  byte[] recipientEncoded = t.reciepient.getEncoded(); // Recipient's encoded algorithm
					  rows2 = jdbcTemplateObject.update(sql2, t.getTransactionId(),
							 												  senderEncoded,
							 												  recipientEncoded,
							 												  t.getValue(),
							 												  t.getSignature());
			      }
				 } else { System.out.println("TRANSACTIONS ARE EMPTY"); }
				  // ITERATE THROUGH INPUTS FOR EACH TRANSACTION, INDEX = I
				  int rows3 = 0;
				  int rows4 = 0;
				  if (isset_transactions && newBlock.getTransactions().get(0).getInputs() != null) {
				  Iterator<TransactionInput> iterator2 = newBlock.getTransactions().get(0).getInputs().iterator();
				  while (iterator2.hasNext()) {
					  	  TransactionInput ti = iterator2.next();
					  	byte[] utxoEncoded = ti.getUTXO().getReciepient().getEncoded();
						  rows3 = jdbcTemplateObject.update(sql3, ti.getTransactionOutputId());
						  // INSERT INPUT's UTXO
						  // SET STRING ID?
						  rows4 = jdbcTemplateObject.update(sql4, ti.getUTXO().getId(),
								  												   utxoEncoded,
								  												   ti.getUTXO().getValue(),
								  												   ti.getUTXO().getParentTransactionId()); System.out.println("batched index 12");
						  
				  }
				 } else { System.out.println("INPUTS ARE EMPTY"); }
				
				  int rows5 = 0;
				  // ITERATE THROUGH OUTPUTS, INDEX = J
					  if (isset_transactions && newBlock.getTransactions().get(0).getOutputs() != null) {
						  Iterator<TransactionOutput> iterator3 = newBlock.getTransactions().get(0).outputs.iterator();
						  while (iterator3.hasNext()) {
							  TransactionOutput to = iterator3.next();
							  byte[] outputRecipientEncoded = to.getReciepient().getEncoded();
							  rows5 = jdbcTemplateObject.update(sql5, to.getId(),
									  												   outputRecipientEncoded,
									  												   to.getValue(),
									  												   to.getParentTransactionId());
					}
				 } else { System.out.println("OUTPUTS ARE EMPTY"); }
					  if (rows > 0) {
						  if (isset_transactions && rows2 > 0) {
							  if (isset_transactions && rows3 > 0) {
								  if (isset_transactions && rows4 > 0) {
									  if (isset_transactions && rows5 < 0) {
										  return true;
									  }
									  System.out.println("OUTPUTS EMPTY, OR SOMETHING WENT WRONG");
								  }
								  System.out.println("INPUT UTXO EMPTY, OR SOMETHING WENT WRONG");
							  }
							  System.out.println("INPUTS EMPTY, OR SOOMETHING WENT WRONG");
						  }
						  System.out.println("TRANSACTIONS EMPTY, OR SOMETHING WENT WRONG");
						  if (!isset_transactions) {
							  return true;
						  }
						  else {
							  return false;
						  }
					  }
//	       
//	                System.out.println(hash + ", " + previoushash + ", " + merkleRoot + ", " + timestamp + ", " + nonce // blockchain
//	                		          + block_id + ", " + thistransactionID + ", " + transactionSender + ", " + transactionRecipient + ", " + transactionsvalue + ", " + signature  // transactions
//	                		          + ", " + inputs_transaction_id + ", " + inputs_transactionoutputID // inputs
//	                		          + ", " + utxo_inputID + ", " + utxo_stringID + ", " + utxoRecipient + ", " + utxoValue + ", " + utxoparentTransactionID + ", " // input utxos
//	                		          + ", " + outputs_transactionsID + ", " + outputID + ", " + recipient + ", " + value + ", " + parentTransactionID); // outputs

	            
	            
				  
				 
	        
	        } catch (Exception ex) {
	            ex.printStackTrace();
	        }
			return false;
			
		}
	public boolean updateUTXOs(HashMap<String, TransactionOutput> utxos) {
		String delete = "DELETE FROM utxos";
		String sql =
				 "INSERT INTO utxos(id, recipient, value, parentTransactionID) VALUES(?,?,?,?)"; 
				  try {
				  int rows = 0;
				  int deletion = jdbcTemplateObject.update(delete);
				  for (String o: rainfall.UTXOs.keySet()) {
					  TransactionOutput to = new TransactionOutput();
					  String key = o.toString();
					  to = rainfall.UTXOs.get(o);
					  System.out.println(to.getValue());
					  byte[] recipientEncoded = to.getReciepient().getEncoded();
					  rows = jdbcTemplateObject.update(sql,
							  							   to.getId(),
							  							   recipientEncoded,
							  							   to.getValue(),
							  							   to.getParentTransactionId());
				  }
				  
				  
				  if (deletion > 0 ) { 
					  System.out.println("A row has been deleted successfully." + deletion);
					  { 
						  if (rows > 0) System.out.println("A row has been inserted succesfully ::" + rows); 
					  return true; 
					  }
					  }
		  } catch (Exception ex) {
			  ex.printStackTrace();
		}
				  return false;
}
	public HashMap<String, TransactionOutput> readUTXOs() {
		  HashMap<String,TransactionOutput> readutxos = new HashMap<String, TransactionOutput>();
		String sql =
				 "SELECT * FROM utxos"; 
				  try {
				  SqlRowSet srs = jdbcTemplateObject.queryForRowSet(sql);
				  byte[] recipientEncoded = null;
				  while (srs.next()) {
					  TransactionOutput to = new TransactionOutput();
					  recipientEncoded = (byte[]) srs.getObject("recipient");
					  String id = srs.getString("id");
					  float value = srs.getFloat("value");
					  String parentTransactionID = srs.getString("parentTransactionID");
					  PublicKey recipient = rainfall.createPublicKey(recipientEncoded);
					  to.setId(id);
					  to.setValue(value);
					  to.setReciepient(recipient);
					  to.setParentTransactionId(parentTransactionID);
					  readutxos.put(id, to);
				  }
				  
				  return readutxos;
				 
		  } catch (Exception ex) {
			  ex.printStackTrace();
		}
				return readutxos;
				 
}

	@Override
	public boolean update(Block oldt, Block newt) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean delete(Block t) {
		// TODO Auto-generated method stub
		return false;
	}
	public void transact(Block prevBlock, Wallet walletA, Wallet walletB, float amount) {
		Block block1 = new Block(prevBlock.hash);
		System.out.println("\nWalletA's balance is: " + walletA.getBalance());
		System.out.println("\nWalletA is Attempting to send funds (40) to WalletB...");
		block1.addTransaction(walletA.sendFunds(walletB.publicKey, amount));
		this.create(block1);
	}

}
