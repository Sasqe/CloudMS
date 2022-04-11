package com.gcu.controller;

import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.gcu.business.BlockchainBusinessService;
import com.gcu.business.UsersBusinessService;
import com.gcu.model.Transaction;
import com.gcu.model.UserModel;

/**
 * Chris King and Kacey Morris
 * CST 323 Milestone 5 Final
 * April 10, 2022
 * TransactionController.java
 * 
 * The Controller that handles all routing for product pages
 */
@Controller
@RequestMapping("/blockchain")
public class TransactionController 
{	
	@Autowired
	private UsersBusinessService bservice;
	
	@Autowired
	private BlockchainBusinessService blockservice;
	/**
	 * displays the product page that shows all the products in the database
	 * @param model (page model)
	 * @return products page
	 */
	
	// displays the form used to send money from one user to another CONFIRMED
	@PostMapping("/transactionView")
	public String transactionView(@RequestParam(name="id") String id, Model model) 
	{
		UserModel user = new UserModel();
		UserModel friend = new UserModel();
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (!(authentication instanceof AnonymousAuthenticationToken)) {
			System.out.println("USER BEING SET!!");
			user = bservice.findByUsername(authentication.getName());
		}
		friend = bservice.findByUsername(id);
		// editView method for landing on the edit page, requestparam ID for finding which user was clicked on
		// Add attributes and set attribute 'friend' as the usermodel that was clicked on
		String message = "Send Money: " + user.getCredentials().getUsername() + " to " + friend.getCredentials().getUsername();
	    model.addAttribute("title", message);
	    model.addAttribute("user", user);
	    model.addAttribute("friend", friend);
	    //return form for sending money 
	    return "transactionView";
	}
	
	/**
	 * sends amount of money to another user
	 * add money to one persons account, remove money from another, only track transaction and commit these changes if both occurred
	 * @param id
	 * @param amount
	 * @param model
	 * @return individual profile page
	 * @throws SQLException
	 */
	@PostMapping("/doTransaction")
	public String transact(@RequestParam(name="id") String id, @RequestParam(value = "amount") String amount, Model model) throws SQLException 
	{	
		// number formatting
		NumberFormat nf = NumberFormat.getInstance();
		nf.setMinimumFractionDigits(2);
		nf.setMaximumFractionDigits(2);
		// user and friend to be defined
		UserModel user = new UserModel();
		UserModel friend = new UserModel();
		// grab the current user
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (!(authentication instanceof AnonymousAuthenticationToken)) {
			System.out.println("USER BEING SET!!");
			// establish the current user by their username 
			user = bservice.findByUsername(authentication.getName());
		}
		// establish friend by id passed
		friend = bservice.findByUsername(id);
		// check to see if we got the right users
		System.out.println("Sending Money Original User: " + user.getCredentials().getUsername());
		System.out.println("Sending Money Friend: " + friend.getCredentials().getUsername());
		
	    // LOGIC
	    float amountf = Float.parseFloat(amount);
	    System.out.println("Sending Money amount float: " + amountf);
	    
	    // call the service and send the money
	    boolean result = blockservice.sendMoney(user, friend, amountf);
	    
	    // get the updated user with the updated balance
	    // add attributes for the user and friend involved in the transaction
	    model.addAttribute("user", bservice.findByUsername(user.getCredentials().getUsername()));
	    model.addAttribute("friend", bservice.findByUsername(friend.getCredentials().getUsername()));
	    
	    if (result) {
	    	model.addAttribute("title", "Transaction Successful");
	    }
	    else {
	    	model.addAttribute("title", "Unable to Complete Transaction");
	    }
	    return "profile";
	}
	
	// displays a list of past transactions from a user in a table CONFIRMED
	@GetMapping("/history")
	public String transactionHistory(Model model) 
	{
		// Create empty user model
		UserModel user = new UserModel();
		// Use auth security context to find currently logged in user
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (!(authentication instanceof AnonymousAuthenticationToken)) {
			System.out.println("USER BEING SET!!");
			user = bservice.findByUsername(authentication.getName());
		}
		// where all results will be saved
		ArrayList<Transaction> results = new ArrayList<Transaction>();
		
		// get all transactions for this user
		results = blockservice.getTransactionsByUser(user);
		if (results == null || results.size() < 1) {
			model.addAttribute("resultsempty", true);
		}
		else {
			model.addAttribute("transactions", results);
		}
		// add title and user attributes
	    model.addAttribute("title", "Transaction History");
	    model.addAttribute("user", user);
	    
	    // return transactions page
	    return "transactions";
	}
	
}
