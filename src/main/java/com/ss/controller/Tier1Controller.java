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
		model.setViewName("tier1/hellotier1");
		return model;
     
	}
    
	@RequestMapping(value = "/tier1/transactions", method = RequestMethod.GET)
	public ModelAndView viewTransactions() {
		System.out.println("In view Transactions method");
		List<TransactionDO> transactions = transactionBO.getUnapprovedNonCriticalTransactions(USER_ROLE_TIER1);

		ModelAndView model = new ModelAndView();
		model.addObject("transactions", transactions);
		model.setViewName("tier1/transactions");
		return model;

	}    
    
	@RequestMapping(value= "/tier1/transaction/approve", method = RequestMethod.POST)
	public @ResponseBody String approveTransaction(@RequestParam("transactionId") int transactionId) {
		System.out.println("In approve transactions method");
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String userInSession = "someUser";
		if (!(auth instanceof AnonymousAuthenticationToken)) {
		    UserDetails userDetail = (UserDetails) auth.getPrincipal();
		    userInSession = userDetail.getUsername();
		}
		
		TransactionDO transaction = transactionBO.getTransactionFromId(transactionId);
		
		boolean amountCheck = accountDaoImpl.checkAmount(transaction.getFromAccountType(), transaction.getAmount(), transaction.getTransactorUserName());
		boolean transactionSuccess = false; 
		String transactionMessage = "";
		
		//Checking if balance is available
		if (amountCheck) {
		    transactionSuccess = transactionBO
				.approveTransaction(transactionId, userInSession);
		} else {
			transactionMessage = "Not enough balance in transfer initiator";
		}
		
		//setting message depending on transaction.
		if (transactionSuccess) {
			accountDaoImpl.doTransfer(transaction.getTransactorUserName(), transaction.getFromAccountType(), transaction.getTargetUserName(), transaction.getToAccountType(), transaction.getAmount());
			transactionMessage = "Transaction approval processed successfully!";
		} else {
			transactionMessage = "Transaction approval failed!";
		}
		System.out.println(transactionMessage);
		return transactionMessage;
	}

	@RequestMapping(value= "/tier1/transaction/decline", method = RequestMethod.POST)
	public @ResponseBody String declineTransaction(@RequestParam("transactionId") int transactionId) {
		System.out.println("In approve transactions method");
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String userInSession = "someUser";
		if (!(auth instanceof AnonymousAuthenticationToken)) {
		    UserDetails userDetail = (UserDetails) auth.getPrincipal();
		    userInSession = userDetail.getUsername();
		}
		boolean transactionSuccess = transactionBO
				.declineTransaction(transactionId, userInSession);
		String transactionMessage = "";
		if (transactionSuccess) {
			transactionMessage = "Transaction decline processed successfully!";
		} else {
			transactionMessage = "Transaction decline failed!";
		}
		System.out.println(transactionMessage);
		return transactionMessage;
	}
	
	@RequestMapping(value= "/tier1/tier1TransactionUser", method = RequestMethod.GET)
    public ModelAndView viewTransactionUser() {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("tier1/tier1TransactionUser");
		return mav;
	}
	
	@RequestMapping(value= "/tier1/fillTransactionRequest", method = RequestMethod.POST)
    public ModelAndView fillTransactionRequest(HttpServletRequest req) {
		String fromUserName=req.getParameter("fromUser");
		ModelAndView mav = new ModelAndView();
		mav.addObject("customerUser", fromUserName);
		mav.setViewName("tier1/paymentRequest");
		return mav;
	}
}
