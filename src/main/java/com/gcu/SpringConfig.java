package com.gcu;

/**
 * Chris King and Kacey Morris
 * CST 323 Milestone 4 Logging / Monitoring
 * April 3, 2022
 * SpringConfig.java
 * 
 * Sets up configurations for beans. 
 */

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.gcu.business.UsersBusinessService;
/**
 * Configuration for Spring Services
 */
@Configuration
public class SpringConfig 
{
/**
 * Sets up Business service
 * @return new business service
 */
	
	@Bean(name="UsersBusinessService")
	public UsersBusinessService getBusinessService()
	{
		return new UsersBusinessService();
	}


}
