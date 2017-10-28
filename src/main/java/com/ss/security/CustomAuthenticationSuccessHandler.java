package com.ss.security;

import java.io.IOException;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication auth)
			throws IOException, ServletException {
		// TODO Auto-generated method stub
		String userTargetURL="/SS/welcome";
		String adminTargetURl="";
		String tier1TargetURL="";
		String tier2TargetURL="";
		Set<String> roles = AuthorityUtils.authorityListToSet(auth.getAuthorities());
		if(roles.contains("ROLE_ADMIN")){
			response.sendRedirect(adminTargetURl);
		}else if(roles.contains("ROLE_USER")){
			response.sendRedirect(userTargetURL);

		}else if(roles.contains("ROLE_TIER1")){
			response.sendRedirect(tier1TargetURL);

	      }
		else{
			response.sendRedirect(tier2TargetURL);

		}
		
		
	}

}
