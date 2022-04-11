package com.gcu;

/**
 * Chris King and Kacey Morris
 * CST 323 Milestone 5 Final
 * April 10, 2022
 * MilestoneApplication.java
 * 
 * This class initializes the application and allows it to run
 */

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Application Building Class
 *
 */
@SpringBootApplication
public class MilestoneApplication {

	/** main function to run and build the webapp
	 * @param args arguments needed to run the app
	 */
	public static void main(String[] args) {
		SpringApplication.run(MilestoneApplication.class, args);
	}

}
