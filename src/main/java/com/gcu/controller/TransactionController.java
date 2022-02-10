package com.gcu.controller;

import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;





import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.gcu.data.BlockchainDataService;
import com.gcu.data.IDataAccess;
import com.gcu.data.UserDataService;
import com.gcu.model.Block;
import com.gcu.model.UserModel;
import com.gcu.model.Wallet;
/**
 * The Controller that handles all routing for product pages
 *
 */
@Controller
@RequestMapping("/blockchain")
public class TransactionController 
{
	@Autowired
	private UserDataService service;
	@Autowired
	private BlockchainDataService blockservice;
	/**
	 * displays the product page that shows all the products in the database
	 * @param model (page model)
	 * @return products page
	 */
	@GetMapping("/")
	public String display(Model model) 
	{
		//display method for landing on the all products page, passes services get all products in as 'products' attribute
		model.addAttribute("products", service.read());
		//return products view
		return "products";
	}
	@PostMapping("/transactionView")
	public String transactionView(@RequestParam(name="id") String id, Model model) 
	{
		UserModel user = new UserModel();
		UserModel friend = new UserModel();
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (!(authentication instanceof AnonymousAuthenticationToken)) {
			System.out.println("USER BEING SET!!");
			user = service.findByUsername(authentication.getName());
		}
		friend = service.findByUsername(id);
		//editView method for landing on the edit page, requestparam ID for finding which user was clicked on
		//Add attributes and set attribute 'productModel' as the object that was clicked on
	    model.addAttribute("title", "Send Money");
	    model.addAttribute("user", user);
	    model.addAttribute("friend", friend);
	    //return updateProduct view
	    return "transactionView";
	}
	@PostMapping("/doTransaction")
	public String transact(@RequestParam(name="id") String id, @RequestParam(value = "amount") String amount, Model model) throws SQLException 
	{	
		NumberFormat nf = NumberFormat.getInstance();
		nf.setMinimumFractionDigits(2);
		nf.setMaximumFractionDigits(2);
		UserModel user = new UserModel();
		UserModel friend = new UserModel();
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (!(authentication instanceof AnonymousAuthenticationToken)) {
			System.out.println("USER BEING SET!!");
			user = service.findByUsername(authentication.getName());
		}
		friend = service.findByUsername(id);
		//editView method for landing on the edit page, requestparam ID for finding which user was clicked on
		//Add attributes and set attribute 'productModel' as the object that was clicked on
	    model.addAttribute("title", "Send Money");
	    model.addAttribute("user", user);
	    model.addAttribute("friend", friend);
	    // LOGIC
	    float amountf = Float.parseFloat(amount);
//	    String amountformat = nf.format(amountf);
//	    double newAmount = Double.parseDouble(amountformat);
//	    System.out.println(newAmount);
	    // Logic for (1) creating new block (2) block.addtransaction using both wallets (3) add Block
	    System.out.println(amountf);
	    return null;
	    
	}
	
}
