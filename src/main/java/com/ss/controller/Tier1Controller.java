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
import com.ss.daoImpl.UserDaoImpl;
import com.ss.model.Account;
import com.ss.model.TransactionDO;
import com.ss.model.User;

import java.util.List;

import javax.servlet.http.HttpServletRequest;


@Controller
public class Tier1Controller {
	private static final String USER_ROLE_TIER1 = "TIER1";
	
	@Autowired
	AccountDaoImpl accountDaoImpl;
	
	@Autowired
	TransactionBO transactionBO;
	
	@Autowired
	UserDaoImpl userDaoImpl;
	
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
	
	@RequestMapping(value = "/tier1/createExternalUser", method = RequestMethod.GET)
	public ModelAndView createExternalUser() {
		ModelAndView model = new ModelAndView();
		model.setViewName("registration");
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
		System.out.println("checking values :" + transaction.getTargetUserName() + " " + transaction.getFromAccountType() + transaction.getToAccountType());
		// handles either internal transfer or debit and credit
		if (transaction.getTargetUserName() == null) {
			if (transaction.getFromAccountType().equals(transaction.getToAccountType())) {
				//credit
				if( transaction.getAmount()>0) {
					boolean transactionSuccess = transactionBO.approveTransaction(transactionId, userInSession);
					if (transactionSuccess) {
						accountDaoImpl.doCreditDebit(transaction.getFromAccountType(), transaction.getAmount(), "credit", transaction.getTransactorUserName());
					    return "Transaction approval processed successfully!";
					} else {
						return "Transaction failed";
					}
					
				} else {
					//debit
					boolean amountCheck = accountDaoImpl.checkAmount(transaction.getFromAccountType(), transaction.getAmount(), transaction.getTransactorUserName());
					boolean transactionSuccess = false;
					String transactionMessage = "";
					if (amountCheck) {
						transactionSuccess = transactionBO
								.approveTransaction(transactionId, userInSession);
					} else {
						transactionMessage = "Not enough balance to perform debit";
					}
					
					if (transactionSuccess) {
						accountDaoImpl.doCreditDebit(transaction.getFromAccountType(), -transaction.getAmount(), "debit", transaction.getTransactorUserName());
						transactionMessage = "Transaction approval processed successfully!";
					} else {
						transactionMessage = "Transaction approval failed!";
					}
					System.out.println(transactionMessage);
					return transactionMessage;
				}
				
			} else {
				//internal transfer
				boolean amountCheck = accountDaoImpl.checkAmount(transaction.getFromAccountType(), transaction.getAmount(), transaction.getTransactorUserName());
				boolean transactionSuccess = false;
				String transactionMessage = "";
				if (amountCheck) {
					transactionSuccess = transactionBO
							.approveTransaction(transactionId, userInSession);
				} else {
					transactionMessage = "Not enough balance in transfer initiator account";
				}
				
				if (transactionSuccess) {
					accountDaoImpl.doTransfer(transaction.getTransactorUserName(), transaction.getFromAccountType(), transaction.getTransactorUserName(), transaction.getToAccountType(), transaction.getAmount());
					transactionMessage = "Transaction approval processed successfully!";
				} else {
					transactionMessage = "Transaction approval failed!";
				}
				System.out.println(transactionMessage);
				return transactionMessage;
				
			}
		}
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
	
    @RequestMapping(value="/tier1/searchExternalUser",method=RequestMethod.GET)
    public ModelAndView searchExternaluser() {
    	ModelAndView model = new ModelAndView();
    	model.addObject("pagename","Search External Users Profile");
    	model.setViewName("tier1/t1searchExternalUser");
    	return model;
    }
	
	@RequestMapping(value="/tier1/modifyExternalUser",method=RequestMethod.POST)
    public ModelAndView ModifyInternalUserAccount(HttpServletRequest req,Authentication auth) {
    	ModelAndView model = new ModelAndView();
		String username=req.getParameter("username");
		/// named internal user below but works for external users as well
    	List<User> InternaluserInfo = userDaoImpl.getExternalUserInfo(username);
    	System.out.println(username);
		System.out.println("user-size" + InternaluserInfo.size());
		if(InternaluserInfo.size() > 0 ) {
			System.out.println("user" + InternaluserInfo.get(0).getFirstname());
			model.addObject("userInfo",InternaluserInfo.get(0));
			model.addObject("message", "This is welcome page!");
			model.setViewName("tier1/t1modifyExternalUser");			
		} else {
			model.setViewName("tier1/t1searchExternalUser");			
		}
		return model;  
	}
	
	@RequestMapping(value="/tier1/checkinternalusername*",method=RequestMethod.POST)
	public @ResponseBody String CheckInternalUsername(@RequestParam("username") String username){
		System.out.print("Username to check " +username);
    	List<User> InternaluserInfo = userDaoImpl.getExternalUserInfo(username);
		System.out.println("user-size" + InternaluserInfo.size());
		if(InternaluserInfo.size() > 0 ) {
			return "true";
		} else {
			return "false";
		}
		
	}
	
    @RequestMapping(value=	"/tier1/updateOrDeleteExternalUser", method=RequestMethod.POST)
    public ModelAndView UpdateOrDeleteInternalUserAccount(HttpServletRequest req,Authentication auth) {
    	ModelAndView model = new ModelAndView();
    	String operation=req.getParameter("operation");
		String username=req.getParameter("username");
    	if(operation.equals("update")) {
    		System.out.println("Updating");
    		int s = userDaoImpl.ProcessInternalUserProfileUpdate(req,username);
    	}

    	if(operation.equals("delete")) {
    		System.out.println("Deleting");
    		int s = userDaoImpl.ProcessInternalUserProfileDelete(username);
    	}

    	model.setViewName("tier1/hellotier1");
    	return model;   
	    
	}
}
