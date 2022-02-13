package com.gcu.business;

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
	
	// get all transactions by user
	public ArrayList<Transaction> getTransactionsByUser(UserModel user) {
		ArrayList<Transaction> transactions = DAO.getTransactionsByUser(user);
		return transactions;
	}
	
	// send money from one user to another
	// return false if not enough money
	public boolean sendMoney(UserModel user, UserModel friend, float amount) {
		// if the user doesnt have enough money, dont even try and return false
		if (user.getWallet().getAmount() < amount) {
			return false;
		}
		// otherwise, return the result of the database send money
		return DAO.sendMoney(user, friend, amount);
	}
}
