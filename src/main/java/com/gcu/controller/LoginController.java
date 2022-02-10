package com.gcu.controller;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.GetMapping;



/**
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
	@GetMapping("/login")
	public String display(Model model) 
	{
		//display method for landing on the login page
	    //add new loginmodel object to 'loginModel' attribute
	    //model.addAttribute("loginModel", new CredentialsModel());
	    //return login view
	    return "login";
	}
}