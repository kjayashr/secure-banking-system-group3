package com.ss.controller;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
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



public class Tier1Controller {
	@Autowired

	AccountDaoImpl accountDaoImpl;
    @RequestMapping(value="/tier1", method=RequestMethod.POST)
	public ModelAndView hellotier1Page() {

		ModelAndView model = new ModelAndView();
		model.addObject("savings", "Spring Security Hello World");
		model.addObject("message", "This is welcome page!");
		model.setViewName("hellotier1");
		return model;
     
	}

}
