package com.ss.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.ss.daoImpl.TransactionDaoImpl;
import com.ss.model.TransactionList;

@Controller
public class MerchantEditProfileViewTransaction {

	@Autowired
	TransactionDaoImpl transactionDaoImpl;

	@RequestMapping(value="Merchanteditprofile",method=RequestMethod.GET)
	public String getEditProfile(){
		return "Merchanteditprofile";
	}

	@RequestMapping(value="Merchantviewtransaction",method=RequestMethod.GET)
	public String getviewtransaction(Authentication auth,ModelMap model){
		String username=auth.getName();
		List<TransactionList> data=transactionDaoImpl.viewTransaction(username);
		model.addAttribute("list",data);
		model.addAttribute("column1", "Date");
		model.addAttribute("column2", "Details");
		model.addAttribute("column3", "Amount");
		model.addAttribute("title", "Transaction List");
		return "Merchanttransactionpage";

	}



}
