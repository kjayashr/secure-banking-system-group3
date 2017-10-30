package com.ss.controller;

import java.util.Date;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.ss.dao.AccountDao;
import com.ss.dao.RegistrationDao;
import com.ss.dao.UserAttemptDao;

import org.apache.log4j.Logger;

@Controller
public class Registration {
	final static Logger logger = Logger.getLogger(Registration.class);
	
	@Autowired
	RegistrationDao registrationImpl;

	@Autowired
	AccountDao accountDao;
	
	@Autowired
	UserAttemptDao userAttemptDao;

	@RequestMapping(value="/registration",method=RequestMethod.GET)
	public String getRegistration(){
		return "registration";
	}
	
	@RequestMapping(value="/registration",method=RequestMethod.POST)
	public RedirectView handleRegistration(HttpServletRequest req,Authentication auth){
		RedirectView model = new RedirectView();
		model.setUrl(redirectToHome(auth));
		String uName = "";
		String role = "";
	/*	if (!(auth instanceof AnonymousAuthenticationToken)) {
			UserDetails userDetail = (UserDetails) auth.getPrincipal();
			model.addObject("username", userDetail.getUsername());
			uName = userDetail.getUsername();
			role = registrationImpl.getRole(uName);
			System.out.println("ROLE ::::: " + role);
		} */
		
		//String url = (String)req.getHeader("referer");
		//System.out.println(" aldnfldnaodnf"  + url);
		String username=req.getParameter("username");
		String password=req.getParameter("password");
		BCryptPasswordEncoder encoder=new BCryptPasswordEncoder();
		password=encoder.encode(password);
		String firstname=req.getParameter("firstname");
		String lastname=req.getParameter("lastname");
		String dateofbirth=req.getParameter("dateofbirth");
		String email=req.getParameter("email");
		String address=req.getParameter("address");
		Long contactno = 1L;
		try {
		contactno = Long.parseLong(req.getParameter("contactno"));
		} catch (NumberFormatException e) {
			logger.info("faced number format exception due to faulty contactno input");
			return model;
		}
		Long ssn=1L;
		try {
			ssn=Long.parseLong(req.getParameter("ssn"));
		} catch (NumberFormatException e) {
			logger.info("faced number format exception due to faulty ssn input");
			return model;
		}
		String city=req.getParameter("city");
		String state=req.getParameter("state");
		String country=req.getParameter("country");
		int postcode = 0;
		try {
			postcode=Integer.parseInt(req.getParameter("postcode"));
		} catch (NumberFormatException e) {
			logger.info("faced number format exception due to faulty postcode input");
			return model;
		}
		String type=req.getParameter("accountType");
		double balance=0;
		try {
		    balance = Double.parseDouble(req.getParameter("balance"));
		} catch (NumberFormatException e) {
			logger.info("faced number format exception due to faulty balance input");
			return model;
		}
		double interest = 0;
		try {
			interest=Integer.parseInt(req.getParameter("interest"));
		} catch (NumberFormatException e) {
			logger.info("faced number format exception due to faulty interest input");
			return model;
		}
		String roleUser=req.getParameter("userType");
		if(roleUser.equalsIgnoreCase("user")){
			roleUser="ROLE_USER";
		}else{
			roleUser="ROLE_MERCHANT";
		}
		// validation left
		try {
			int i=registrationImpl.addNewUser(username,password,firstname,lastname, dateofbirth, email, address,
				contactno, ssn, city, state, country, postcode,roleUser);
			int accountCrRet=accountDao.createAccount(balance,username,type,interest);
			System.out.println(":::type::"+type);
			if(type.equalsIgnoreCase("Credit Card"))
			{
				String createCreditCardRet=accountDao.createCreditCard(username,email);
			}
			int numOfAttempt=0;
			int userAttempt=userAttemptDao.insertUser(username,password,1,numOfAttempt,new Date(),true,true,true);
		}catch(Exception e) {
			registrationImpl.rollback(username);
			System.out.println(e.getMessage());
		}
		return model;
	/*	if(role.equals("ROLE_TIER2"))
			model.setViewName("hellotier2");
		else if(role.equals("ROLE_TIER1"))
			model.setViewName("hellotier1");
		else if(role.equals("ROLE_TIER3"))
			model.setViewName("Admin_Homepage");
		return model; */
	}
	
	private String redirectToHome(Authentication auth) {
		String adminTargetURl="/SS/admin/Welcome";
		String tier1TargetURL="/SS/tier1";
		String tier2TargetURL="/SS/tier2";
		String merchantTargetURL="/SS/Merchant/Welcome";
		String defaultURL = "/SS/login";
		Set<String> roles = AuthorityUtils.authorityListToSet(auth.getAuthorities());
		if(roles.contains("ROLE_ADMIN")){
			return adminTargetURl;
		}else if(roles.contains("ROLE_TIER1") || roles.contains("ROLE_TIER1_APPROVED")){
			return tier1TargetURL;
	      }else if (roles.contains("ROLE_MERCHANT")) {
	  		return merchantTargetURL;
	      } else if (roles.contains("ROLE_TIER2")) {
	    	  return tier2TargetURL;
	      } else {
	    	  return defaultURL;
	      }
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
