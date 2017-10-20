package com.ss.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class EditProfileViewTransaction {
	
	@RequestMapping(value="/editprofile",method=RequestMethod.GET)
	public String getEditProfile(){
		return "editprofile";
	}
	
	@RequestMapping(value="/viewtransaction",method=RequestMethod.GET)
	public String getviewtransaction(){
		return "editprofile";
	}
	
	

}
