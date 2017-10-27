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

import com.ss.dao.T1userBO;
import com.ss.dao.TransactionBO;
import com.ss.daoImpl.AccountDaoImpl;
import com.ss.dao.AccountDao;
import com.ss.model.Account;

import com.ss.model.T1userDO;
import com.ss.model.TransactionDO;

import jdk.nashorn.internal.ir.RuntimeNode.Request;

import com.ss.dao.RegistrationDao;
import java.util.List;

import javax.servlet.http.HttpServletRequest;


@Controller
public class Tier2Controller {
	private static final String USER_ROLE_TIER2 = "TIER2";
	@Autowired
	RegistrationDao registrationImpl;
	
	@Autowired
	AccountDaoImpl accountDaoImpl;
	
	@Autowired
	AccountDao accountDao;
	@Autowired
	TransactionBO transactionBO;
	
	@Autowired
	T1userBO t1userBO;
	
    @RequestMapping(value="/tier2", method=RequestMethod.GET)
	public ModelAndView hellotier2Page() {

		ModelAndView model = new ModelAndView();
		model.addObject("savings", "Spring Security Hello World");
		model.addObject("message", "This is welcome page!");
		//System.out
		model.setViewName("hellotier2");
		return model;
     
	}
    
    @RequestMapping(value="/tier2/createExternalUser", method=RequestMethod.GET)
    public ModelAndView createuser() {
    	ModelAndView model = new ModelAndView();
    	model.addObject("savings","Hello");
    	model.addObject("message","this");
    	model.setViewName("registration");
    	return model;
    }
    
    @RequestMapping(value="/tier2/tier2TransactionUser", method=RequestMethod.GET)
    public ModelAndView createTransaction() {
    	ModelAndView model = new ModelAndView();
    	model.addObject("savings","Hello");
    	model.addObject("message","this");
    	model.setViewName("tier2TransactionUser");
    	return model;
    }
    
	@RequestMapping(value="/tier2/t1users", method = RequestMethod.GET)
	public ModelAndView viewT1Users() {
		List<T1userDO> t1users = t1userBO.gett1users(USER_ROLE_TIER2);
		System.out.print(t1users.size());
		ModelAndView model = new ModelAndView();
		model.addObject("t1users",t1users);
		model.setViewName("autht1byt2");
		return model;
	}
    
	@RequestMapping(value="/tier2/t1user/grantApproval", method=RequestMethod.POST)
	public @ResponseBody String grantApproval(@RequestParam("userName") String userName) {
		System.out.print("hit ");
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String userInSession = "someUser";
		if(!(auth instanceof AnonymousAuthenticationToken)){
			UserDetails userDetail = (UserDetails) auth.getPrincipal();
			userInSession = userDetail.getUsername();
		}
		boolean approvalSuccess = t1userBO.grantApproval(userName,userInSession);
		String grantApprovalMessage = "";
		if (approvalSuccess) {
			grantApprovalMessage = "Approval granted successfully!";
			
		}
		else {
			grantApprovalMessage = "Approval failed!";
		}
		System.out.println(grantApprovalMessage);
		return grantApprovalMessage;
	}
    
    @RequestMapping(value = "/tier2/transactions", method = RequestMethod.GET)
	public ModelAndView viewTransactions() {
		System.out.println("In view Transactions method vvvvS");
		List<TransactionDO> transactions = transactionBO.getUnapprovedCriticalTransactions(USER_ROLE_TIER2);

		ModelAndView model = new ModelAndView();
		model.addObject("transactions", transactions);
		model.setViewName("t2Transaction");
		return model;

	}
	
    
	@RequestMapping(value= "/tier2/transaction/approve", method = RequestMethod.POST)
	public @ResponseBody String approveTransaction(@RequestParam("transactionId") int transactionId) {
		System.out.println("In approve transactions method");
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String userInSession = "someUser";
		if (!(auth instanceof AnonymousAuthenticationToken)) {
		    UserDetails userDetail = (UserDetails) auth.getPrincipal();
		    userInSession = userDetail.getUsername();
		}
		boolean transactionSuccess = transactionBO
				.approveTransaction(transactionId, userInSession);
		String transactionMessage = "";
		if (transactionSuccess) {
			transactionMessage = "Transaction approval processed successfully!";
		} else {
			transactionMessage = "Transaction approval failed!";
		}
		System.out.println(transactionMessage);
		return transactionMessage;
	}

	@RequestMapping(value= "/tier2/transaction/decline", method = RequestMethod.POST)
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

	////////////////////////////////////
	
	
	
    
}
