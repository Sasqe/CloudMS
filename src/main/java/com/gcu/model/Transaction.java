package com.gcu.model;

import java.time.LocalDate;


/**
 * Model for User info
 */
public class Transaction 
{
	// transaction properties
	private int id;
	private UserModel user;
	private UserModel friend;
	private float amount;
	private LocalDate dateDone;
	
	// data constructor
	public Transaction(int id, UserModel user, UserModel friend, float amount, LocalDate dateDone) {
		super();
		this.id = id;
		this.user = user;
		this.friend = friend;
		this.amount = amount;
		this.dateDone = dateDone;
	}
	
	// default constructor 
	public Transaction() {
		
	}
	
	// getters and setters 
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public UserModel getUser() {
		return user;
	}
	public void setUser(UserModel user) {
		this.user = user;
	}
	public UserModel getFriend() {
		return friend;
	}
	public void setFriend(UserModel friend) {
		this.friend = friend;
	}
	public float getAmount() {
		return amount;
	}
	public void setAmount(float amount) {
		this.amount = amount;
	}
	public LocalDate getDateDone() {
		return dateDone;
	}
	public void setDateDone(LocalDate dateDone) {
		this.dateDone = dateDone;
	}
}
