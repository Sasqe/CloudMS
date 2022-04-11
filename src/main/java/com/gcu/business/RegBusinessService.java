package com.gcu.business;

/**
 * Chris King and Kacey Morris
 * CST 323 Milestone 5 Final
 * April 10, 2022
 * RegBusinessService.java
 * 
 * This class implements spring security to protect user data and pages. 
 */

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.gcu.data.UserDataService;
import com.gcu.model.CredentialsModel;
import com.gcu.model.UserModel;

/**
 * The Register Business Service that handles all the 
 * Business logic needed for Registration
 */

@Service
public class RegBusinessService 
{
	//Autowire UserDataService as DAO
	@Autowired
	UserDataService DAO;
	
	@Autowired
	UsersBusinessService service;
	/**
	 * Creates a new user and prints the information to the console
	 * @param user 9user model that has all elements that make up a user)
	 */
	public void register(UserModel user) 
	{	
		 //Print registration information to console for debugging
	    System.out.println("User registered with the following details: ");
	    System.out.println("First Name: " + user.getFirstname());
	    System.out.println("Last Name: " + user.getLastname());
	    System.out.println("User Name: " + user.getCredentials().getUsername());
	    System.out.println("Password: " + user.getCredentials().getPassword());
	    System.out.println("Email: " + user.getEmail());
	    System.out.println("Phone Number: " + user.getNumber());
	    System.out.println("USER ID: " + user.getId());
	    
	    //Encode user's credentials before inserting into database
	    user = encodeCredentials(user);
	    //call business service create to insert object in the database
	    service.create(user);
	    service.createWallet(user.getWallet());
	    // DAO.create(user);
	    // DAO.createWallet(user.getWallet());
	}
	
	/** helper method to encode the user's credentials 
	 * @param user (user model to be encoded)
	 * @return updated encoded user model
	 */
	//helper method to encode the user's credentials 
	private UserModel encodeCredentials(UserModel user) 
	{
				//Credentials Model for encoded password
				CredentialsModel encodedCredentials = new CredentialsModel();
				//set credentials' user name to current user name
				encodedCredentials.setUsername(user.getCredentials().getUsername());
				//encode password
				String passwordEncoded = new BCryptPasswordEncoder().encode(user.getCredentials().getPassword());
				//set credentials' password to encoded password
				encodedCredentials.setPassword(passwordEncoded);
				//update user's credentials and return user
				user.setCredentials(encodedCredentials);
				return user;
	}
}
