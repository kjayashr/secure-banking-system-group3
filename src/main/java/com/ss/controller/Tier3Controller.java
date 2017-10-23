package com.ss.controller;

import java.util.HashMap;
import java.util.List;

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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.ss.dao.TransactionBO;
import com.ss.daoImpl.AccountDaoImpl;
import com.ss.model.Account;
import com.ss.model.TransactionDO;
import java.util.List;

import javax.servlet.http.HttpServletRequest;


@Controller
public class Tier3Controller {
	@Autowired
	AccountDaoImpl accountDaoImpl;
	
	@Autowired
	TransactionBO transactionBO;
	
    @RequestMapping(value="/tier3", method=RequestMethod.GET)
	public ModelAndView hellotier3Page() {
		ModelAndView model = new ModelAndView();
		model.addObject("message", "This is welcome page!");
		model.setViewName("Admin_Homepage");
		return model;
	}
    
    @RequestMapping(value="/tier3/createaccount", method=RequestMethod.GET)
	public ModelAndView hellotier3CreateAccount() {
		ModelAndView model = new ModelAndView();
		model.addObject("message", "This is welcome page!");
		model.setViewName("createaccount");
		return model;  
	}

    @RequestMapping(value="/tier3/editprofile", method=RequestMethod.GET)
	public ModelAndView hellotier3ModifyAccount() {
		ModelAndView model = new ModelAndView();
		model.addObject("message", "This is welcome page!");
		model.setViewName("editprofile");
		return model;  
	}
 
    @RequestMapping(value="/tier3/deleteprofile", method=RequestMethod.GET)
	public ModelAndView hellotier3DeleteAccount() {
		ModelAndView model = new ModelAndView();
		model.addObject("message", "This is welcome page!");
		model.setViewName("deleteprofile");
		return model;  
	}
}
