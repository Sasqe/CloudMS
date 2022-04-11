package com.gcu.model;

import java.util.ArrayList;

import javax.validation.constraints.NotNull;


import javax.validation.constraints.Size;

/**
 * Chris King and Kacey Morris
 * CST 323 Milestone 5 Final
 * April 10, 2022
 * UserModel.java
 * 
 * Model for User info
 */

public class UserModel 
{
	//credentials used for logging in
	private int id;
	private CredentialsModel credentials;
	@NotNull(message="First Name is a required field")
	@Size(min=1, max=32, message="First Name must be between 1 and 32 characters")
	private String firstname;
	@NotNull(message="Last Name is a required field")
	@Size(min=1, max=32, message="Last Name must be between 1 and 32 characters")
	private String lastname;
	@NotNull(message="Email Address is a required field")
	@Size(min=10, max=32, message="Please enter a valid email address")
	private String email;
	private String number;
	private Wallet wallet;
	private ArrayList<UserModel> friends;
	
	/**
	 * User Model Constructor with all params
	 * @param id (user id)
	 * @param firstname (users' firstname)
	 * @param lastName (users' lastname)
	 * @param email (users' email)
	 * @param number (users' phone number)
	 * @param loginModel (users' username and password via login credentials)
	 */
	public UserModel(int id, String firstname,String lastName,String email,String number,CredentialsModel loginModel, Wallet wallet, ArrayList<UserModel> friends) 
	{
		this.id = id;
		this.firstname = firstname;
		this.lastname = lastName;
		this.email = email;
		this.number = number;
		this.credentials = loginModel;
		this.wallet = wallet;
		this.friends = friends;
	}
	/**
	 * Empty copy constructor
	 */
	public UserModel() {
		Wallet wallet = new Wallet();
		this.wallet = wallet;
	}
	/**
	 * gets the wallet of the user
	 * @return wallet
	 */
	public Wallet getWallet() {
		return wallet;
	}
	/**
	 * sets the wallet of the user
	 * @param wallet
	 */
	public void setWallet(Wallet wallet) {
		this.wallet = wallet;
	}
	/** id getter
	 * @return user's id
	 */
	public int getId() {
		return id;
	}
	/** id setter
	 * @param id (user's id)
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * gets the friends list of the user
	 * @return ArrayList<UserModel>
	 */
	public ArrayList<UserModel> getFriends() {
		return friends;
	}
	/**
	 * sets the friends list of the user
	 * @param friends
	 */
	public void setFriends(ArrayList<UserModel> friends) {
		this.friends = friends;
	}
	/** firstname getter
	 * @return firstname
	 */
	public String getFirstname() {
		return firstname;
	}
	/** firstname setter
	 * @param firstname (user's firstname)
	 */
	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}
	/** last name getter
	 * @return lastname
	 */
	public String getLastname() {
		return lastname;
	}
	/** last name setter
	 * @param lastname (user's last name)
	 */
	public void setLastname(String lastname) {
		this.lastname = lastname;
	}
	/** email getter
	 * @return email
	 */
	public String getEmail() {
		return email;
	}
	/** email setter
	 * @param email (user's email)
	 */
	public void setEmail(String email) {
		this.email = email;
	}
	/** phone number getter
	 * @return phone number
	 */
	public String getNumber() {
		return number;
	}
	/** phone number setter
	 * @param number (users phone number)
	 */
	public void setNumber(String number) {
		this.number = number;
	}
	//get and set credentials
	/** gets the associated login model
	 * @return login credentials
	 */
	public CredentialsModel getCredentials() {
		return this.credentials;
	}
	/** sets the associated login model
	 * @param credentials (user's login credentials)
	 */
	public void setCredentials(CredentialsModel credentials) {
		this.credentials = credentials; 
	}
}
