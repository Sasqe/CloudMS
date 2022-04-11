package com.gcu.controller;

import java.util.ArrayList;

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

import com.gcu.business.UsersBusinessService;
import com.gcu.model.UserModel;

/**
 * Chris King and Kacey Morris
 * CST 323 Milestone 5 Final
 * April 10, 2022
 * ProfileController.java
 * 
 * simple welcome controller that returns a view name
 */

@Controller
@RequestMapping("/profile")
public class ProfileController 
{	
	@Autowired
	UsersBusinessService bservice;
	/**
	 * A route to home that sets the title and welcome message
	 * @param model (page model)
	 * @return the home page
	 */
	// the current users profile
	// displays an editable table CONFIRMED
	@GetMapping("/")
	public String display(Model model) 
	{
		// Create empty user model
		UserModel user = new UserModel();
		// Use auth security context to find currently logged in user
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (!(authentication instanceof AnonymousAuthenticationToken)) {
			System.out.println("USER BEING SET!!");
			user = bservice.findByUsername(authentication.getName());
		}
		// Add user to attribute
		model.addAttribute("title", "Cloud Computing Testing");
		model.addAttribute("welcomeMessage", "Welcome");
		model.addAttribute("user", user);
		// Test logging -- print user's username and wallet balance
		System.out.println(user.getCredentials().getUsername());
		System.out.println("BALANCE :" + user.getWallet().getAmount());
		// return profile 1, currently logged in user's profile
		return "profile";
	}
	
	/**
	 * displays another users profile in a table
	 * @param id
	 * @param model
	 * @return String other profiles page
	 */
	@GetMapping("/viewProfile")
	public String viewProfile(@RequestParam(name="id") String id, Model model) 
	{	
		// create new EMPTY user and friend model
		UserModel user = new UserModel();
		UserModel friend = new UserModel();
		// Use authentication context to grab currently logged in user
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (!(authentication instanceof AnonymousAuthenticationToken)) {
			System.out.println("USER BEING SET!!");
			user = bservice.findByUsername(authentication.getName());
		}
		// Find friend in database via param ID
		friend = bservice.findByUsername(id);
		// add attributes containing user, friend, and page messages
		model.addAttribute("title", "Cloud Computing Testing");
		model.addAttribute("welcomeMessage", "Welcome");
		// Add user and friend attributes to profile view
		model.addAttribute("user", user);
		model.addAttribute("friend", friend);
		// Test logging -- print the credentials of the users' friends
		System.out.println("CREDENTIALS ::" + friend.getCredentials().getUsername());
		// return profile 2, friend's profile page.
		return "profile2";
	}
	
	/**
	 * removes a friend connection from the database and redirects to profile
	 * @param id
	 * @param model
	 * @return individual profile page
	 */
	@PostMapping("/removeFriend")
	public String removeFriend(@RequestParam(name="id") String id, Model model) 
	{
		UserModel user = new UserModel();
		UserModel friend = new UserModel();
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (!(authentication instanceof AnonymousAuthenticationToken)) {
			System.out.println("USER BEING SET!!");
			user = bservice.findByUsername(authentication.getName());
		}
		// find friend in database from param id
		friend = bservice.findByUsername(id);
		//Add attributes and set attribute 'title' as friend deleted succesfully n
	    model.addAttribute("title", "Friend Removed Successfully");
	    //remove friend return profile view
	    bservice.removeFriend(friend, user);
	    // update friends list
	    user = bservice.findByUsername(user.getCredentials().getUsername());
	    model.addAttribute("user", user);
	    
	    return "profile";
	}
	
	/**
	 * adds a friend connection to the database and redirects to profile page
	 * @param id
	 * @param model
	 * @return individual profile page
	 */
	@PostMapping("/addFriend")
	public String addFriend(@RequestParam(name="id") String id, Model model) 
	{
		UserModel user = new UserModel();
		UserModel friend = new UserModel();
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (!(authentication instanceof AnonymousAuthenticationToken)) {
			System.out.println("USER BEING SET!!");
			user = bservice.findByUsername(authentication.getName());
		}
		friend = bservice.findByUsername(id);
		//Add friend method from user and friend retrieved from database
		bservice.addFriend(friend, user);
		// update friends list
	    user = bservice.findByUsername(user.getCredentials().getUsername());
		model.addAttribute("title", "Friend Added Successfully");
	    model.addAttribute("user", user);
	    model.addAttribute("friend", friend);
	    //return profile view
	    return "profile";
	}
	
	/**
	 * displays a list of users in a table
	 * @param id
	 * @param model
	 * @return user results page
	 */
	@PostMapping("/searchUsers")
	public String searchUsers(@RequestParam(name="id") String id, Model model) 
	{
		ArrayList<UserModel> results = new ArrayList<UserModel>();
		UserModel user = new UserModel();
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (!(authentication instanceof AnonymousAuthenticationToken)) {
			System.out.println("USER BEING SET!!");
			user = bservice.findByUsername(authentication.getName());
		}
		// search all users containing search query in their username
		results = bservice.searchUsers(id);
		if (results.size() < 1) {
			model.addAttribute("resultsempty", true);
		}
		// add title and user attributes
	    model.addAttribute("title", "Results");
	    model.addAttribute("user", user);
	    // ONLY add results attribute if result set is not empty
	    if (results.size() > 0)
	    model.addAttribute("users", results);
	    //Test logging -- print users in the result set
	    System.out.println(id);
	    for (int i = 0; i < results.size(); i++) {
	    	System.out.println("RESULT " + i);
	    	System.out.println(results.get(i).getFirstname() + results.get(i).getLastname() + results.get(i).getCredentials().getUsername() + results.get(i).getEmail() + results.get(i).getNumber());
	    }	
	    // return profile page
	    return "profiles";
	}
	
	/**
	 * updates a profile and redirects back to the profile with updated information
	 * @param userModel
	 * @param bindingResult
	 * @param model
	 * @return individual profile page
	 */
	@PostMapping("/updateProfile")
	public String updateProfile(@Valid UserModel userModel, BindingResult bindingResult, Model model) 
	{
		// get the current information for the user in the database
		UserModel user = bservice.findById(userModel.getId());
		// use service to update that user with the new user (old, new)
		boolean result = bservice.update(user, userModel);
		// get the updated user
		user = bservice.findById(userModel.getId());
		// Add user to attribute
		if (result) {
			model.addAttribute("title", "Profile Updated");
		}
		else {
			model.addAttribute("title", "Unable to Update Profile");
		}
		model.addAttribute("welcomeMessage", "Welcome");
		model.addAttribute("user", user);
		// redirect the user to their updated profile
		return "profile";	
	}
}
