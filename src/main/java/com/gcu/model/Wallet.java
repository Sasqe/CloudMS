package com.gcu.model;

/**
 * Chris King and Kacey Morris
 * CST 323 Milestone 5 Final
 * April 10, 2022
 * Wallet.java
 * 
 * wallet that keeps track of a users financial amount
 */

public class Wallet {
	// property
	public float amount;

	/**
	 * get the amount of money in the wallet
	 * @return float amount
	 */
	public float getAmount() {
		return amount;
	}

	/**
	 * set the amount of money in a users wallet
	 * @param amount
	 */
	public void setAmount(float amount) {
		this.amount = amount;
	}
}