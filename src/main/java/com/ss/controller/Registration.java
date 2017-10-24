package com.ss.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ss.dao.AccountDao;
import com.ss.dao.RegistrationDao;


@Controller
public class Registration {
	
	@Autowired
	RegistrationDao registrationImpl;

	@Autowired
	AccountDao accountDao;

	@RequestMapping(value="/registration",method=RequestMethod.GET)
	public String getRegistration(){
		return "registration";
	}
	
	@RequestMapping(value="/registration",method=RequestMethod.POST)
	public String handleRegistration(HttpServletRequest req,Authentication auth){
		
		String username=req.getParameter("username");
		String password=req.getParameter("password");
		String firstname=req.getParameter("firstname");
		String lastname=req.getParameter("lastname");
		String dateofbirth=req.getParameter("dateofbirth");
		String email=req.getParameter("email");
		String address=req.getParameter("address");
		Long contactno=Long.parseLong(req.getParameter("contactno"));
		Long ssn=Long.parseLong(req.getParameter("ssn"));
		String city=req.getParameter("city");
		String state=req.getParameter("state");
		String country=req.getParameter("country");
		int postcode=Integer.parseInt(req.getParameter("postcode"));
		String type=req.getParameter("accountType");
		int balance=Integer.parseInt(req.getParameter("balance"));
		int interest=Integer.parseInt(req.getParameter("balance"));
		// validation left
		int i=registrationImpl.addNewUser(username,password,firstname,lastname, dateofbirth, email, address,
				contactno, ssn, city, state, country, postcode);
		int accountCrRet=accountDao.createAccount(balance,username,type,interest);
		return "login";
	}
	
	@RequestMapping(value="/checkusername*",method=RequestMethod.POST)
	public @ResponseBody String checkUserName(@RequestParam("username") String username){
		System.out.print("Username to check " +username);
		String ret = registrationImpl.check(username);
		return ret;
	}
	
	@RequestMapping(value="/checkemail*",method=RequestMethod.POST)
	public @ResponseBody String checkEmail(@RequestParam("email") String email){
		String ret = registrationImpl.checkEmail(email);
		return ret;
	}
	
	
}
