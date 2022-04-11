package com.gcu.business;

/**
 * Chris King and Kacey Morris
 * CST 323 Milestone 4 Logging / Monitoring
 * April 3, 2022
 * BlockchainBusinessService.java
 * 
 * Business service for the blockchain. 
 */

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gcu.data.BlockchainDataService;
import com.gcu.model.Transaction;
import com.gcu.model.UserModel;

/**
 * Business logic for products
 */
@Service
public class BlockchainBusinessService 
{
	@Autowired
	BlockchainDataService DAO;
	
	/**
	 * Test method for debugging
	 */
	public void test() 
	{
		System.out.println("Hello");
	}
	
	/**
	 * This method gets all transactions by the user who is logged in
	 * @param user
	 * @return ArrayList<Transaction>
	 */
	public ArrayList<Transaction> getTransactionsByUser(UserModel user) {
		ArrayList<Transaction> transactions = DAO.getTransactionsByUser(user);
		return transactions;
	}
	
	/**
	 * allows a user to transfer money from one user to another, returns false if not possible
	 * @param user
	 * @param friend
	 * @param amount
	 * @return boolean
	 */
	public boolean sendMoney(UserModel user, UserModel friend, float amount) {
		// if the user doesnt have enough money, dont even try and return false
		if (user.getWallet().getAmount() < amount) {
			return false;
		}
		// otherwise, return the result of the database send money
		return DAO.sendMoney(user, friend, amount);
	}
}
