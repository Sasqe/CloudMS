package com.gcu.business;

/**
 * Chris King and Kacey Morris
 * CST 323 Milestone 5 Final
 * April 10, 2022
 * SecurityBusinessService.java
 * 
 * This class implements security for the application and finds users
 */

import java.util.ArrayList;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.gcu.data.UserDataService;
import com.gcu.model.UserModel;
import org.springframework.security.core.userdetails.User;

/**
 * The Business service for Security and Data Validation
 */
@Service
public class SecurityBusinessService implements UserDetailsService
{
	//Autowired DataAccess interface as DAO
	@Autowired
	UserDataService DAO;
	
	@Autowired
	UsersBusinessService service;
	/**
	 * Validates that the User logging in is a valid user
	 * @param credentials User model containing username and password
	 * @return true/false if the credentials are valid
	 */
	//Authenticate method is used for logging in or authenticating a user, by returning loadbyUsername
	public UserDetails authenticate(UserModel credentials) 
	{
		//SecurityBusiness authenticate, call loadByUsername helper method and pass user 
		String username = credentials.getCredentials().getUsername();
		return this.loadUserByUsername(username);
	}
	/** loads the user by searched username
	 * @param username (searched username)
	 * @return UserDetails of found user/exception
	 */
	//loadByUsername helper method 
	@Override

	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		//Create new user by finding it in the database by username
		UserModel user = service.findByUsername(username);
		//If user is found, use GrantedAuthority to add user to authenticated list
		if (user != null)
		{
			List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
			authorities.add(new SimpleGrantedAuthority("USER"));
			return new User(user.getCredentials().getUsername(), user.getCredentials().getPassword(), authorities);
		}
		//If user does not exist
		else
		{
			throw new UsernameNotFoundException("Username not found");
		}
	}
}
