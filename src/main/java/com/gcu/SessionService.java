package com.gcu;

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
