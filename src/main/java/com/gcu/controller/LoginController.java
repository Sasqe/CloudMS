package com.gcu.controller;
import org.springframework.stereotype.Controller;

import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.GetMapping;

/**
 * Chris King and Kacey Morris
 * CST 323 Milestone 5 Final
 * April 10, 2022
 * LoginController.java
 * 
 * The controller that handles all the routing for login
 */

@Controller
public class LoginController 
{
	/**
	 * the route to display the login screen with form
	 * @param model (page model)
	 * @return login page
	 */
	// displays the login form, can be accessed at any time CONFIRMED
	@GetMapping("/login")
	public String display(Model model) 
	{
		//display method for landing on the login page
	    //return login view
	    return "login";
	}
}