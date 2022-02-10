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
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gcu.data.UserDataService;
import com.gcu.model.UserModel;

/**
 * simple welcome controller that returns a view name
 *
 */
@Controller
@RequestMapping("/profile")
public class ProfileController 
{
	@Autowired
	UserDataService service;
	/**
	 * A route to home that sets the title and welcome message
	 * @param model (page model)
	 * @return the home page
	 */
	@GetMapping("/")
	public String display(Model model) 
	{
		UserModel user = new UserModel();
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (!(authentication instanceof AnonymousAuthenticationToken)) {
			System.out.println("USER BEING SET!!");
			user = service.findByUsername(authentication.getName());
		}
		//Simply return a Model w/an attribute named 
		//message and return a view named home using a string
		model.addAttribute("title", "Cloud Computing Testing");
		model.addAttribute("welcomeMessage", "Welcome");
		model.addAttribute("user", user);
		System.out.println(user.getCredentials().getUsername());
		System.out.println("BALANCE :" + user.getWallet().getBalance());
		return "profile";
	}
	@GetMapping("/viewProfile")
	public String viewProfile(@RequestParam(name="id") String id, Model model) 
	{	
		UserModel user = new UserModel();
		UserModel friend = new UserModel();
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (!(authentication instanceof AnonymousAuthenticationToken)) {
			System.out.println("USER BEING SET!!");
			user = service.findByUsername(authentication.getName());
		}
		friend = service.findByUsername(id);
		
		//Simply return a Model w/an attribute named 
		//message and return a view named home using a string
		model.addAttribute("title", "Cloud Computing Testing");
		model.addAttribute("welcomeMessage", "Welcome");
		model.addAttribute("user", user);
		model.addAttribute("friend", friend);
		System.out.println("CREDENTIALS ::" + friend.getCredentials().getUsername());
		return "profile2";
	}
	@PostMapping("/removeFriend")
	public String removeFriend(@RequestParam(name="id") String id, Model model) 
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
	    model.addAttribute("title", "Friend Deleted Successfully");
	    model.addAttribute("user", user);
	    model.addAttribute("friend", friend);
	    //return updateProduct view
	    service.removeFriend(friend, user.getId());
	    return "profile";
	}
	@PostMapping("/addFriend")
	public String addFriend(@RequestParam(name="id") String id, Model model) 
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
	    model.addAttribute("title", "Friend Deleted Successfully");
	    model.addAttribute("user", user);
	    model.addAttribute("friend", friend);
	    //return updateProduct view
	    service.addFriend(friend, user.getId());
	    return "profile";
	}
	@PostMapping("/searchUsers")
	public String searchUsers(@RequestParam(name="id") String id, Model model) 
	{
		ArrayList<UserModel> results = new ArrayList<UserModel>();
		UserModel user = new UserModel();
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (!(authentication instanceof AnonymousAuthenticationToken)) {
			System.out.println("USER BEING SET!!");
			user = service.findByUsername(authentication.getName());
		}
		results = service.searchUsers(id);
		if (results.size() < 1) {
			model.addAttribute("resultsempty", true);
		}
		//editView method for landing on the edit page, requestparam ID for finding which user was clicked on
		//Add attributes and set attribute 'productModel' as the object that was clicked on
	    model.addAttribute("title", "Results");
	    model.addAttribute("user", user);
	    if (results.size() > 0)
	    model.addAttribute("users", results);
	    //return updateProduct view
	    System.out.println(id);
	    for (int i = 0; i < results.size(); i++) {
	    	System.out.println("RESULT " + i);
	    	System.out.println(results.get(i).getFirstname() + results.get(i).getLastname() + results.get(i).getCredentials().getUsername() + results.get(i).getEmail() + results.get(i).getNumber());
	    }
	    	
	    return "profiles";
	}

}
