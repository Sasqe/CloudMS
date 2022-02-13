package com.gcu.data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Service;

import com.gcu.business.UsersBusinessService;
import com.gcu.model.Transaction;
import com.gcu.model.UserModel;
/**
 * All the Data layer logic for users
 */
@Service
public class BlockchainDataService 
{
	//Autowired datasource injections
	@Autowired
	private DataSource dataSource;
	private JdbcTemplate jdbcTemplateObject;
	
	@Autowired
	private UsersBusinessService service;
	//Constructor
	/**
	 * Adds the data source to the data service when its instantiated
	 * @param dataSource (source of data)
	 */
	public BlockchainDataService(DataSource dataSource) 
	{
		//set data source and template object
		this.dataSource = dataSource;
		this.jdbcTemplateObject = new JdbcTemplate(dataSource);
	}

	// send money from one user to another
	// transaction for adding and removing money, then logging in transactions table
	public boolean sendMoney(UserModel user, UserModel friend, float amount) {
		float userNewBalance = user.getWallet().getAmount() - amount;
		float friendNewBalance = friend.getWallet().getAmount() + amount;
		System.out.println("user new balance " + userNewBalance  + " friend new balance " + friendNewBalance);
		LocalDate time = LocalDate.now();
		// query string same for add and remove money
		String sql = "UPDATE wallets SET balance = ? WHERE user_id = ?";
		
		// transaction sql
		String transactionSql = "INSERT INTO transactions(userID, friendID, amount, dateDone) VALUES(?, ?, ?, ?)";
		
		try {
			//execute SQL update
			int rows = jdbcTemplateObject.update(sql,
								userNewBalance,
								user.getId());
			// if the first one worked, do the second
			if (rows == 1) {
				System.out.println("user balance was updated successfully ");
				//execute SQL update
				int rows2 = jdbcTemplateObject.update(sql,
									friendNewBalance,
									friend.getId());
				
				// if both worked, log transaction
				if (rows2 == 1) {
					//execute SQL insert
					int transaction = jdbcTemplateObject.update(transactionSql,
										user.getId(),
										friend.getId(),
										amount,
										time);
					
					// if all worked, return true
					return true;
				}
			}
			// if one of them didnt work, return false
			return false;
		}
		//catch exception and print stack trace
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return false;
	}
	
	public ArrayList<Transaction> getTransactionsByUser(UserModel user) {
		// query string same for add and remove money
		String sql = "SELECT * FROM transactions WHERE userID = ? or friendID = ?";
		
		try {
			//create structure to hold users and transactions
			UserModel friend;
			Transaction transaction  = new Transaction();
			ArrayList<Transaction> results = new ArrayList<Transaction>();
			//Execute SQL query and loop over result set
			SqlRowSet srs = jdbcTemplateObject.queryForRowSet(sql,
												user.getId(),
												user.getId());	
			while(srs.next()){
				// get the sending user in the transaction
				user = service.findById(srs.getInt("userID"));
				// get the receiving user in the transaction
				friend = service.findById(srs.getInt("friendID"));
				// create a new transaction
				transaction = new Transaction(srs.getInt("id"),
										user,
										friend,
										srs.getFloat("amount"),
										srs.getDate("dateDone").toLocalDate());
				// add the transaction to the list
				results.add(transaction);
			}
			// if there are results, return the results
			if (results.size() > 0) {
				return results;
			}
			// else return false
			System.out.println("No transactions for this user :: " + user.getCredentials().getUsername());
			return results;
		}
		//catch exception and print stack trace
		catch (Exception e) 
		{
			e.printStackTrace();
			return null;
		}
	}
}
