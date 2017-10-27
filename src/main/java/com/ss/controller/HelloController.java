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
public class HelloController {
	
	@Autowired
	AccountDaoImpl accountDoaImpl;
	
	@RequestMapping(value = "/welcome**" , method = RequestMethod.GET)
	public ModelAndView welcomePage(HttpServletRequest req, HttpServletResponse resp,HttpSession se) {

		ModelAndView model = new ModelAndView();
		if(req.getUserPrincipal() == null) {
			try {
				req.getRequestDispatcher("/login?expired").forward(req, resp);
				model.setViewName("hello");
				return model;
			} catch (ServletException|IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				model.setViewName("login");
				return model;
			}
		}
		String name = req.getUserPrincipal().getName();
		HashMap<String,Account> accountinfo=accountDoaImpl.getAccountInfo(name);
		model.addObject("savings", "Spring Security Hello World");
		model.addObject("message", "This is welcome page!");
		model.addObject("accountData", accountinfo);
		List<String> validAccounts = accountDoaImpl.getValidAccounts(name);
		se.setAttribute("validAccounts",validAccounts);
		model.setViewName("hello");
		return model;

	}


	@RequestMapping(value = "/admin**", method = RequestMethod.GET)
	public ModelAndView adminPage() {

		ModelAndView model = new ModelAndView();
		model.addObject("title", "Spring Security Hello World");
		model.addObject("message", "This is protected page!");
		model.setViewName("admin");

		return model;

	}
	
	//Spring Security see this :
		@RequestMapping(value = { "/", "/login" }, method = RequestMethod.GET)
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
			model.setViewName("login"); 

			return model;

		}
		
		//for 403 access denied page
		@RequestMapping(value = "/403", method = RequestMethod.GET)
		public ModelAndView accesssDenied() {

		  ModelAndView model = new ModelAndView();

		  //check if user is login
		  Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		  if (!(auth instanceof AnonymousAuthenticationToken)) {
			UserDetails userDetail = (UserDetails) auth.getPrincipal();
			model.addObject("username", userDetail.getUsername());
		  }

		  model.setViewName("403");
		  return model;

		}

	

}
