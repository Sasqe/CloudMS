package com.gcu.data;


import java.util.ArrayList;

import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Service;

import com.gcu.model.CredentialsModel;
import com.gcu.model.UserModel;
import com.gcu.model.Wallet;

/**
 * Chris King and Kacey Morris
 * CST 323 Milestone 5 Final
 * April 10, 2022
 * UserDataService.java
 * 
 * All the Data layer logic for users
 */

@Service
public class UserDataService implements IDataAccess<UserModel> 
{
	//Autowired datasource injections
	@Autowired
	private DataSource dataSource;
	private JdbcTemplate jdbcTemplateObject;
	//Constructor
	/**
	 * Adds the data source to the data service when its instantiated
	 * @param dataSource (source of data)
	 */
	public UserDataService(DataSource dataSource) 
	{
		//set data source and template object
		this.dataSource = dataSource;
		this.jdbcTemplateObject = new JdbcTemplate(dataSource);
	}
	/**
	 * Gets all the users from the database and returns them in a list
	 * @return list of users
	 */
	@Override
	public List<UserModel> read() 
	{
		//sql statement to select all from users
		String sql = "SELECT * FROM USERS LEFT JOIN wallets ON wallets.user_id = users.id";
		//create structure to hold users
		List<UserModel> users = new ArrayList<UserModel>();
		try 
		{
			//Execute SQL query and loop over result set
			SqlRowSet srs = jdbcTemplateObject.queryForRowSet(sql);	
			while(srs.next())
			{
				Wallet wallet = new Wallet();
				wallet.setAmount(srs.getFloat("balance"));
				users.add(new UserModel(srs.getInt("id"),
										srs.getString("firstname"),
										srs.getString("lastname"),
										srs.getString("email"),
										srs.getString("number"),
										new CredentialsModel(srs.getString("username"),
													   srs.getString("password")),
										wallet,
										null));
			}
		}
		//catch exception and print stack trace
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		//return users data set 
		return users;
	}

	/**
	 * finds a user in the database by it's id
	 * @param id (user id)
	 * @return user model
	 */
	public UserModel findById(int id) 
	{
		//SQL to select all from users where passed credentials match user's credentials
		String sql = "SELECT A.*, W.*, B.*,C.id AS friendid, C.firstname AS friendfirstname, C.lastname AS friendlastname, C.username AS friendusername, C.email AS friendemail, C.number AS friendnumber\r\n"
				+ "    FROM users A\r\n"
				+ "    LEFT JOIN friends B ON A.id=B.users_id\r\n"
				+ "    LEFT JOIN users C on B.friend_id=C.id\r\n"
				+ "	LEFT JOIN wallets W ON w.user_id = A.id\r\n"
				+ " WHERE A.id = '" + id + "'";
				//create structure to hold users
				UserModel user = new UserModel();
				ArrayList<UserModel> friends = new ArrayList<UserModel>();
				try {
					//Execute SQL query and loop over result set
					SqlRowSet srs = jdbcTemplateObject.queryForRowSet(sql);	
					while(srs.next())
					{
						Wallet wallet = new Wallet();
						wallet.setAmount(srs.getFloat("balance"));
						user = new UserModel(srs.getInt("id"),
												srs.getString("firstname"),
												srs.getString("lastname"),
												srs.getString("email"),
												srs.getString("number"),
												new CredentialsModel(srs.getString("username"),
															   srs.getString("password")),
												wallet,
												friends);
						UserModel friend = new UserModel(srs.getInt("friendid"),
								srs.getString("friendfirstname"),
								srs.getString("friendlastname"),
								srs.getString("friendemail"),
								srs.getString("friendnumber"),
								new CredentialsModel(srs.getString("friendusername"),
											   null),
								null,
								null);
						if (friend.getCredentials().getUsername() != null)
						user.getFriends().add(friend);
					}
					//there should only be one user with unique credentials
					if (user.getFirstname() != null) {
						return user;
					}
					// else return false
					return null;
				}
				//catch exception and print stack trace
				catch (Exception e) {
					e.printStackTrace();
					return null;
				}
	}

	/**
	 * create a user using a user model
	 * @param user (user model)
	 * @return true/false
	 */
	@Override
	public boolean create(UserModel user) 
	{
		//sql statement for inserting values
		String sql = "INSERT INTO users(id, firstname, lastname, username, password, email, number) VALUES(?,?,?,?,?,?,?)";
		try 
		{
			//execute SQL insert
			int rows = jdbcTemplateObject.update(sql,
												user.getId(),
												user.getFirstname(),
												user.getLastname(),
												user.getCredentials().getUsername(),
												user.getCredentials().getPassword(),
												user.getEmail(),
												user.getNumber());
			//return result of insertion
			return rows == 1 ? true : false;
					
		}
		//catch exception and print stack trace
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		
		return false;
	}
	
	/**
	 * create a wallet using a wallet object
	 * @param wallet (wallet model)
	 * @return true/false
	 */
	public boolean createWallet(Wallet wallet) 
	{
		String sql =
				 "INSERT INTO wallets(user_id, balance) VALUES((SELECT users.id FROM users ORDER BY `id` DESC LIMIT 1), ?)";
			try { 
				  int rows = jdbcTemplateObject.update(sql, 
						  									wallet.getAmount()
						  									); 
				  
				  if (rows > 0) { System.out.println("A row has been inserted successfully.");	return true; }
				  
				}
			catch (Exception ex) {
				ex.printStackTrace();
			}
			return false;
	}

	/**
	 * updates the user model in the database
	 * @param oldt (original model)
	 * @param newt (updated model)
	 * @return true/false
	 */
	@Override
	public boolean update(UserModel oldt, UserModel newt) 
	{
		//sql statement for updating values
		String sql = "UPDATE users SET firstname = ?, lastname = ?, username = ?, email = ?, number = ? WHERE id = ?";
		try 
		{
			//execute SQL insert
			int rows = jdbcTemplateObject.update(sql,
								newt.getFirstname(),
								newt.getLastname(),
								newt.getCredentials().getUsername(),
								newt.getEmail(),
								newt.getNumber(),
								oldt.getId());
			//return result of update
			return rows == 1 ? true : false;
		}
		//catch exception and print stack trace
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * deletes a user from the database
	 * @param t (user model)
	 * @return true/false
	 */
	@Override
	public boolean delete(UserModel t) 
	{
	return false;
	}
	/**
	 * remove friend connection from database
	 * @param friend (friend connection user)
	 * @param user (logged in user)
	 * @return null
	 */
	public UserModel removeFriend(UserModel friend, UserModel user) 
	{
		String sql =
				 "DELETE FROM friends WHERE users_id = ? AND friend_id = ?";
			try {

				  int rows = jdbcTemplateObject.update(sql, user.getId(), friend.getId()
						  									); 
				  
				  if (rows > 0) { System.out.println("A row has been removed successfully."); user = findByUsername(user.getCredentials().getUsername()); return user; }
				  
				}
			catch (Exception ex) {
				ex.printStackTrace();
			}
			return null;
	}
	/**
	 * add friend connection in database
	 * @param friend (friend connection user)
	 * @param user (logged in user)
	 * @return null
	 */
	public UserModel addFriend(UserModel friend, UserModel user) 
	{
		String sql =
				 "INSERT INTO `friends`(`users_id`, `friend_id`) VALUES (?,?)";
			try {

				  int rows = jdbcTemplateObject.update(sql, user.getId(), friend.getId()
						  									); 
				  
				  if (rows > 0) { System.out.println("A row has been inserted successfully."); 
				  user = findByUsername(user.getCredentials().getUsername()); 
				  return user; 
				  }
				  
				}
			catch (Exception ex) {
				ex.printStackTrace();
			}
			return null;
	}
	/**
	 * Find user by username
	 * @param username (searched username)
	 * @return usermodel found/exception
	 */
	public UserModel findByUsername(String username) 
	{
		//SQL to select all from users where passed credentials match user's credentials
		String sql = "SELECT A.*, W.*, B.*,C.id AS friendid, C.firstname AS friendfirstname, C.lastname AS friendlastname, C.username AS friendusername, C.email AS friendemail, C.number AS friendnumber\r\n"
				+ "    FROM users A\r\n"
				+ "    LEFT JOIN friends B ON A.id=B.users_id\r\n"
				+ "    LEFT JOIN users C on B.friend_id=C.id\r\n"
				+ "	LEFT JOIN wallets W ON w.user_id = A.id\r\n"
				+ " WHERE A.username = '" + username + "'";
		//create structure to hold users
		UserModel user = new UserModel();
		ArrayList<UserModel> friends = new ArrayList<UserModel>();
		try {
			//Execute SQL query and loop over result set
			SqlRowSet srs = jdbcTemplateObject.queryForRowSet(sql);	
			while(srs.next())
			{
				Wallet wallet = new Wallet();
				wallet.setAmount(srs.getFloat("balance"));
				user = new UserModel(srs.getInt("id"),
										srs.getString("firstname"),
										srs.getString("lastname"),
										srs.getString("email"),
										srs.getString("number"),
										new CredentialsModel(srs.getString("username"),
													   srs.getString("password")),
										wallet,
										friends);
				UserModel friend = new UserModel(srs.getInt("friendid"),
						srs.getString("friendfirstname"),
						srs.getString("friendlastname"),
						srs.getString("friendemail"),
						srs.getString("friendnumber"),
						new CredentialsModel(srs.getString("friendusername"),
									   null),
						null,
						null);
				if (friend.getCredentials().getUsername() != null)
				user.getFriends().add(friend);
			}
			//there should only be one user with unique credentials
			if (user.getFirstname() != null) {
				return user;
			}
			// else return false
			return null;
		}
		//catch exception and print stack trace
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	/**
	 * search for multiple users by username
	 * @param username (searched username)
	 * @return users (list of found users)
	 */
	public ArrayList<UserModel> searchUsers(String username) 
	{
		//SQL to select all from users where passed credentials match user's credentials
		String sql = "SELECT * "
				+ "    FROM users \r\n"
				+ " WHERE username LIKE '%" + username + "%'";
		//create structure to hold users
		UserModel user = new UserModel();
		ArrayList<UserModel> results = new ArrayList<UserModel>();
		try {
			//Execute SQL query and loop over result set
			SqlRowSet srs = jdbcTemplateObject.queryForRowSet(sql);	
			while(srs.next())
			{
				user = new UserModel(srs.getInt("id"),
										srs.getString("firstname"),
										srs.getString("lastname"),
										srs.getString("email"),
										srs.getString("number"),
										new CredentialsModel(srs.getString("username"),
													   null),
										null,
										null);
				results.add(user);
				System.out.println(user.getCredentials().getUsername() + "------------------");
			}
			//there should only be one user with unique credentials
			if (results.size() > 0) {
				return results;
			}
			// else return false
			System.out.println("No results found for search string :: " + username);
			return results;
		}
		//catch exception and print stack trace
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
