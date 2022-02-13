package com.gcu.business;

import java.security.KeyPair;
import java.util.ArrayList;
import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.gcu.controller.rainfall;
import com.gcu.data.IDataAccess;
import com.gcu.data.UserDataService;
import com.gcu.model.CredentialsModel;
import com.gcu.model.UserModel;
import com.gcu.model.Wallet;

/**
 * Business Logic for Users
 */
public class UsersBusinessService {
	//Autowire DataAccess interface as 'data'
	@Autowired
	IDataAccess<UserModel> data;
	
	@Autowired
	UserDataService service;
	/**
	 * Gets all the users from the database and returns them in a list
	 * @return list of users
	 */
	public List<UserModel> read() 
	{
		return service.read();
	}

	/**
	 * finds a user in the database by it's id
	 * @param id (user id)
	 * @return user model
	 */
	public UserModel findById(int id) 
	{
		return service.findById(id);
	}

	/**
	 * create a user using a user model
	 * @param user (user model)
	 * @return true/false
	 */
	public boolean create(UserModel user) 
	{
		return service.create(user);
	}
	
	/**
	 * create a wallet using a wallet object
	 * @param wallet (wallet model)
	 * @return true/false
	 */
	public boolean createWallet(Wallet wallet) {
		return service.createWallet(wallet);
	}
	
	/**
	 * updates the user model in the database
	 * @param oldt (original model)
	 * @param newt (updated model)
	 * @return true/false
	 */
	public boolean update(UserModel oldt, UserModel newt) {
		return service.update(oldt, newt);
	}
	
	/**
	 * remove friend connection from database
	 * @param friend (friend connection user)
	 * @param user (logged in user)
	 * @return null
	 */
	public UserModel removeFriend(UserModel friend, UserModel user) 
	{
		return service.removeFriend(friend, user);
	}
	/**
	 * add friend connection in database
	 * @param friend (friend connection user)
	 * @param user (logged in user)
	 * @return null
	 */
	public UserModel addFriend(UserModel friend, UserModel user) 
	{
		return service.addFriend(friend, user);
	}
	/**
	 * Find user by username
	 * @param username (searched username)
	 * @return usermodel found/exception
	 */
	public UserModel findByUsername(String username) 
	{
		return service.findByUsername(username);
	}
	/**
	 * search for multiple users by username
	 * @param username (searched username)
	 * @return users (list of found users)
	 */
	public ArrayList<UserModel> searchUsers(String username) 
	{
		return service.searchUsers(username);
	}
}
