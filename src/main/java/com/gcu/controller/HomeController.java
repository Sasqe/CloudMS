package com.gcu.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.gcu.data.UserDataService;
import com.gcu.model.UserModel;

/**
 * simple welcome controller that returns a view name
 *
 */
@Controller
@RequestMapping("/")
public class HomeController 
{
	@Autowired
	UserDataService service;
	/**
	 * A route to home that sets the title and welcome message
	 * @param model (page model)
	 * @return the home page
	 */
	// im adding to this controller 
	// hi
	@GetMapping("/")
	public String home(Model model)
	{
		//Simply return a Model w/an attribute named 
		//message and return a view named home using a string
		model.addAttribute("title", "Cloud Computing Testing");
		model.addAttribute("welcomeMessage", "Welcome");
		return "index";
	}
	/** Handles the home page after the user is logged in
	 * @param model (page model)
	 * @return index page
	 */
	@GetMapping("/home")
	public String homePostLogin(Model model)
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
		System.out.println("USERS FRIENDS: " + user.getFriends().size());
		if (user.getFriends().size() < 1) {
			model.addAttribute("nofriends", 1);
			System.out.println("NO FRIENDS");
		}
		for (int i = 0; i < user.getFriends().size(); i++) {
			System.out.println(user.getFriends().get(i).getCredentials().getUsername());
		}
		return "indexPostLogin";
	}

}
