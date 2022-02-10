package com.gcu.data;

import java.sql.SQLException;
import java.util.List;

/**
 * The user interface with all the methods implemented by the user services
 * using generic models
 * @param <T> generic list users
 */
public interface IDataAccess<T> 
{
	/**
	 * @return list of all users using generics
	 * @throws SQLException 
	 */
	public List<T> read() throws SQLException;
	/**
	 * @param t generic user
	 * @return true/false
	 */
	public boolean create(T t);
	/**
	 * @param oldt original model
	 * @param newt updated model
	 * @return true/false
	 */
	public boolean update(T oldt, T newt);
	/**
	 * @param t generic user
	 * @return true/false
	 */
	public boolean delete(T t);
	
}
