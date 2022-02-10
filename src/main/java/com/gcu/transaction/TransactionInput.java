package com.gcu.transaction;

public class TransactionInput {
	public String transactionOutputId; //Reference to TransactionOutputs -> transactionId
	public TransactionOutput UTXO; //Contains the Unspent transaction output
	
	public TransactionInput(String transactionOutputId) {
		this.transactionOutputId = transactionOutputId;
	}
	public TransactionInput () {
		
	}

	public String getTransactionOutputId() {
		return transactionOutputId;
	}

	public void setTransactionOutputId(String transactionOutputId) {
		this.transactionOutputId = transactionOutputId;
	}

	public TransactionOutput getUTXO() {
		return UTXO;
	}

	public void setUTXO(TransactionOutput uTXO) {
		UTXO = uTXO;
	}
}