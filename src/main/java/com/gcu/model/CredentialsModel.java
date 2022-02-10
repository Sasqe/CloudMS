package com.gcu.model;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

//login/credentials model.
/**
 * The model for login credentials
 */
public class CredentialsModel 
{
	@NotNull(message="Username is a required field")
	@Size(min=1, max=32, message="Username must be between 1 and 32 characters")
	private String username;
	@NotNull(message="Password is a required field")
	@Size(min=1, max=32, message="Password must be between 1 and 32 characters")
	private String password;
	/**
	 * Login Constructor with parameters
	 * @param username (user's username)
	 * @param password (user's password)
	 */
	public CredentialsModel(String username, String password) {
		this.username = username;
		this.password = password;
	}
	/**
	 * Copy Constructor
	 */
	public CredentialsModel() {
		
	}
	
	/** user name getter
	 * @return username
	 */
	public String getUsername() {
		return username;
	}
	/** username setter
	 * @param username (user's username)
	 */
	public void setUsername(String username) {
		this.username = username;
	}
	/** password getter
	 * @return password 
	 */
	public String getPassword() {
		return password;
	}
	/** password setter
	 * @param password (user's password)
	 */
	public void setPassword(String password) {
		this.password = password;
	}
}
