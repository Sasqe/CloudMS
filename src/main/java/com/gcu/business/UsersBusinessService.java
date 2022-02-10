package com.gcu.business;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;

import com.gcu.data.IDataAccess;
import com.gcu.model.UserModel;
import com.gcu.model.Wallet;

/**
 * Business Logic for Users
 */
public class UsersBusinessService {
	//Autowire DataAccess interface as 'data'
	@Autowired
	IDataAccess<UserModel> data;
	/** returns all users from the database using the data service
	 * @return the list from the data service
	 */
	public List<UserModel> findAll() {
		//return data's findall to retrieve all users from the databse :: returns a list
		return null;
	}

	/** 
	 * Finds a user by its id using the data service
	 * @param id (user's id)
	 * @return the user being searched for or no user found
	 */
	public UserModel findById(int id) {
		// TODO Auto-generated method stub
		return null;
	}


	/**
	 * Creates a user and adds them to the database
	 * using the data service
	 * @param user (the user model of the user being created)
	 * @return true/false based on if the user was created successfully
	 */
	public boolean create(UserModel user) {
		return false;
	}


	/**
	 * Updates a user's information in the database
	 * using the data service
	 * @param t (user model containing info being updated)
	 * @return true/false based on if user was updated successfully
	 */
	public boolean update(UserModel t) {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * deletes a user from the database
	 * using the data service
	 * @param t (user model being deleted)
	 * @return true/false based on if the user was deleted successfully
	 */
	public boolean delete(UserModel t) {
		// TODO Auto-generated method stub
		return false;
	}
}
