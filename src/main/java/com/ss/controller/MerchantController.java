package com.ss.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.ss.daoImpl.AccountDaoImpl;
import com.ss.model.Account;


@Controller
public class MerchantController {
	
	@Autowired
	AccountDaoImpl accountDoaImpl;
	
	@RequestMapping(value = "/Merchanthello" , method = RequestMethod.GET)
	public ModelAndView welcomePage(HttpServletRequest req, HttpServletResponse resp) {

		ModelAndView model = new ModelAndView();
		req.getUserPrincipal();
		if(req.getUserPrincipal() == null) {
			try {
				req.getRequestDispatcher("/login?expired").forward(req, resp);
				model.setViewName("Merchanthello");
				return model;
			} catch (ServletException|IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				model.setViewName("Merchantlogin");
				return model;
			}
		}
		String name = req.getUserPrincipal().getName();
		HashMap<String,Account> accountinfo=accountDoaImpl.getAccountInfo(name);
		model.addObject("savings", "Spring Security Hello World");
		model.addObject("message", "This is welcome page!");
		model.addObject("accountData", accountinfo);
		model.setViewName("403");
		return model;

	}


	
	
	//Spring Security see this :
		@RequestMapping(value = { "/Merchantlogin" }, method = RequestMethod.GET)
		public ModelAndView login(
			@RequestParam(value = "error", required = false) String error,
			@RequestParam(value = "logout", required = false) String logout,
			@RequestParam(value = "expired", required = false) String expired
				) {

			ModelAndView model = new ModelAndView();
			if (error != null) {
				model.addObject("error", "Invalid username and password!");
			}

			if (logout != null) {
				model.addObject("msg", "You've been logged out successfully.");
			}
			if (expired != null) {
				model.addObject("expired", "Your session has expired.");
			}
			model.setViewName("Merchantlogin"); 

			return model;

		}
		
		

	

}
