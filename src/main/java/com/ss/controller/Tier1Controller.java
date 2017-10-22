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
public class Tier1Controller {
	private static final String USER_ROLE_TIER1 = "TIER1";
	
	@Autowired
	AccountDaoImpl accountDaoImpl;
	
	@Autowired
	TransactionBO transactionBO;
	
    @RequestMapping(value="/tier1", method=RequestMethod.GET)
	public ModelAndView hellotier1Page() {

		ModelAndView model = new ModelAndView();
		model.addObject("savings", "Spring Security Hello World");
		model.addObject("message", "This is welcome page!");
		//System.out
		model.setViewName("hellotier1");
		return model;
     
	}
    
	@RequestMapping(value = "/tier1/transactions", method = RequestMethod.GET)
	public ModelAndView viewTransactions() {
		System.out.println("In view Transactions method");
		List<TransactionDO> transactions = transactionBO.getUnapprovedTransactionInfo(USER_ROLE_TIER1);

		ModelAndView model = new ModelAndView();
		model.addObject("transactions", transactions);
		model.setViewName("transactions");
		return model;

	}    
    
	@RequestMapping(value= "/tier1/transaction/approve", method = RequestMethod.POST)
	public @ResponseBody String approveTransaction(@RequestParam("transactionId") int transactionId) {
		System.out.println("In approve transactions method");
		boolean transactionSuccess = transactionBO
				.approveTransaction(transactionId, "someUser", USER_ROLE_TIER1);
		String transactionMessage = "";
		if (transactionSuccess) {
			transactionMessage = "Transaction approval processed successfully!";
		} else {
			transactionMessage = "Transaction approval failed!";
		}
		return transactionMessage;
	}

}
