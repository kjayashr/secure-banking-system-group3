package com.ss.security;

import java.io.IOException;
import java.util.Set;
import java.io.*;

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
		String adminTargetURl="/SS/admin/Welcome";
		String tier1TargetURL="/SS/tier1";
		String tier2TargetURL="/SS/tier2";
		String merchantTargetURL="/SS/Merchant/Welcome";
		Set<String> roles = AuthorityUtils.authorityListToSet(auth.getAuthorities());
		if(roles.contains("ROLE_ADMIN")){
			response.sendRedirect(adminTargetURl);
		}else if(roles.contains("ROLE_USER")){
			response.sendRedirect(userTargetURL);

		}else if(roles.contains("ROLE_TIER1")){
			response.sendRedirect(tier1TargetURL);

	      }else if (roles.contains("ROLE_MERCHANT")) {
	    	   System.out.println("This is crazy..");
				response.sendRedirect(merchantTargetURL);

	      }
	      else {
			response.sendRedirect(tier2TargetURL);

		}
		
		
	}

}
