package com.gcu.model;

import java.time.LocalDate;

/**
 * Chris King and Kacey Morris
 * CST 323 Milestone 5 Final
 * April 10, 2022
 * Transaction.java
 * 
 * Model for transactional info
 */
public class Transaction 
{
	// transaction properties
	private int id;
	private UserModel user;
	private UserModel friend;
	private float amount;
	private LocalDate dateDone;
	
	/**
	 * transaction model data constructor
	 * @param id
	 * @param user
	 * @param friend
	 * @param amount
	 * @param dateDone
	 */
	public Transaction(int id, UserModel user, UserModel friend, float amount, LocalDate dateDone) {
		super();
		this.id = id;
		this.user = user;
		this.friend = friend;
		this.amount = amount;
		this.dateDone = dateDone;
	}
	
	/**
	 * transaction model default constructor
	 */
	public Transaction() {
		
	}
	
	// getters and setters 
	/**
	 * get the id of the transaction
	 * @return int id
	 */
	public int getId() {
		return id;
	}
	/**
	 * set the id of the transaction
	 * @param id
	 */
	public void setId(int id) {
		this.id = id;
	}
	/**
	 * returns the user associated with the transaction
	 * @return UserModel
	 */
	public UserModel getUser() {
		return user;
	}
	/**
	 * sets the user associated with the transaction
	 * @param user
	 */
	public void setUser(UserModel user) {
		this.user = user;
	}
	/**
	 * gets the receiver of the transaction
	 * @return UserModel
	 */
	public UserModel getFriend() {
		return friend;
	}
	/**
	 * sets the receiver of the transaction
	 * @param friend
	 */
	public void setFriend(UserModel friend) {
		this.friend = friend;
	}
	/**
	 * gets the amount of money sent in the transaction
	 * @return float amount
	 */
	public float getAmount() {
		return amount;
	}
	/**
	 * sets the amount of money sent in the transaction
	 * @param amount
	 */
	public void setAmount(float amount) {
		this.amount = amount;
	}
	/**
	 * gets the date the transaction was completed
	 * @return LocalDate
	 */
	public LocalDate getDateDone() {
		return dateDone;
	}
	/**
	 * sets the date of the transaction
	 * @param dateDone
	 */
	public void setDateDone(LocalDate dateDone) {
		this.dateDone = dateDone;
	}
}
