package com.gcu;

/**
 * Chris King and Kacey Morris
 * CST 323 Milestone 4 Logging / Monitoring
 * April 3, 2022
 * SecurityConfig.java
 * 
 * This class implements spring security to protect user data and pages. 
 */

import java.security.Security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.session.HttpSessionEventPublisher;

import com.gcu.business.SecurityBusinessService;


/**
 * Handles all authentication security
 *
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter 
{
	//Autowired PasswordEncoder 
	@Autowired
	PasswordEncoder passwordEncoder;
	//Autowired Security Service
	@Autowired
	SecurityBusinessService service;
	/** encoder for passwords
	 * @return new BCryptPasswordEncoder
	 */
	//Bean for PasswordEncoder object
	@Bean
	BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	/**
	 * Configures which pages need security. verifies log in and logout
	 * @param http (the security token for user session)
	 */
//	@Override
	//Configuration for security, blocks products pages and service pages while unauth'ed
	protected void configure(HttpSecurity http) throws Exception 
	{
		Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider()); //Setup Bouncey castle as a Security Provider
		http.csrf().disable()
		.httpBasic()
			.and()
			.authorizeRequests()
			.antMatchers("/blocks/**").authenticated()
			.and()
		.authorizeRequests()
			.antMatchers("/", "/images/**/", "/displayQauthCode").permitAll()
			.and()
			.formLogin()
			//Default login page is /login
			.loginPage("/login")
			//Credentials parameters are username and password
			.usernameParameter("username")
			.passwordParameter("password")
			.permitAll()
			.defaultSuccessUrl("/home/", true)
			.and()
		.logout()
			.logoutUrl("/logout")
			.invalidateHttpSession(true)
			.clearAuthentication(true)
			.permitAll()
			.logoutSuccessUrl("/")
			.and()
			.sessionManagement()
			.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
			.and()
			.sessionManagement().maximumSessions(2)
			.expiredUrl("/login")
			.and()
			.invalidSessionUrl("/login")
			.and()
			.sessionManagement().sessionFixation().migrateSession();
	}

    /**
     * authenticates that the password is encoded
     * @param auth - used to manage authentication
     */
    @Autowired
    public void configure(AuthenticationManagerBuilder auth) throws Exception
    {
    
      auth
      .userDetailsService(service)
      .passwordEncoder(passwordEncoder);
    }
    /**
     * establishes an HTTP event
     * @return HttpSessionEventPublisher
     */
    @Bean
    public HttpSessionEventPublisher httpSessionEventPublisher() {
        return new HttpSessionEventPublisher();
    }
 
}

