package com.gcu;

/**
 * Chris King and Kacey Morris
 * CST 323 Milestone 4 Logging / Monitoring
 * April 3, 2022
 * SessionService.java
 * 
 * This file creates sessions. 
 */

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;

public class SessionService {
	public Authentication getSession(HttpServletRequest request) {
		HttpSession session = request.getSession();
		SecurityContext ctx = (SecurityContext) session.getAttribute("SPRING_SECURITY_CONTEXT");
		Authentication auth = ctx.getAuthentication();
	
		return auth;
	}
	
}
