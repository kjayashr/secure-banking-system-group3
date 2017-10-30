package com.ss.controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
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
import org.springframework.web.servlet.ModelAndView;

import com.ss.daoImpl.AccountDaoImpl;
import com.ss.model.Account;
import com.ss.security.PKICertificate;
import com.ss.service.EncryptDecryptUtil;
import com.ss.dao.TransactionBO;
import com.ss.dao.UserDao;

@Controller
public class RequestController {
	private static String SAVINGS_ACCOUNT_TYPE = "Saving";

	@Autowired
	AccountDaoImpl accountDaoImpl;
	int threshold=1000;
	
	@Autowired
	UserDao userDao;
	
	@Autowired
	TransactionBO transactionBO;
	
	
	@RequestMapping(value="/request", method=RequestMethod.POST)
	public ModelAndView creditDebitRequest(HttpServletRequest req,Authentication auth){
		String username=auth.getName();
		boolean critical=true;
		ModelAndView notifyPage=new ModelAndView("notify");
		String accountType=req.getParameter("accountType");
		String type=req.getParameter("type");
		String approverusername=null;
		double amount=Double.parseDouble(req.getParameter("amount"));
		String pubKey = req.getParameter("pubKey");
		System.out.println(accountType + " " + type + " " +amount+ " " +pubKey);
		boolean toProcess = false;
		
		if(pubKey!=null && pubKey.trim().length()!=0) {
			try {
				System.out.println("[TEST] 1");
				String lockedData = PKICertificate.lock(Double.toString(amount), pubKey);
				String prvKey = userDao.getPrivateKey(username);
				if (prvKey != null) {
					System.out.println("[TEST] 2 : Private found");
					prvKey = EncryptDecryptUtil.singleDecryption(prvKey);
					String unlockedData = PKICertificate.unlock(lockedData, prvKey);
					if (unlockedData.equals(Double.toString(amount))) {
						System.out.println("[TEST] 3 FINE");
						toProcess = true;
					} else {
						//TODO log statement
						System.out.println("[TEST] 4 WRONG");
					}
				} else {
					System.out.println("[TEST] 5 ; Private not found");
				}
			} catch(Exception e) {
				//TODO log statement
				System.out.println("[TEST] 6 EXCEPTION");
			}
		}
		
		if(!toProcess) {
			notifyPage.addObject("notification","Wrong key passed. Payment failed.");
			return notifyPage;
		}
		
		// add validation over credit and debit
		if(type.equalsIgnoreCase("Debit")){
			if(accountDaoImpl.checkAmount(accountType, amount,username)){
				System.out.println("inside debit");

				String detail="Debit to "+accountType;
				String status="pending";
				DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
				Date date = new Date();
				if(amount<threshold){
					critical=false;
					accountDaoImpl.doCreditDebit(accountType, amount, type, username);
					status="approved";

				}
				amount=amount*(-1);
				accountDaoImpl.addToTransaction(amount, detail, status, username, date, null, critical,approverusername,accountType,accountType);
				notifyPage.addObject("notification","Payment Processed sucessfully");
			}else{
				notifyPage.addObject("notification","Insufficient Funds");
			}	
		}else{
			System.out.println("inside credit");
			DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			Date date = new Date();
			String detail="Credit to "+accountType;
			String status="pending";
			if(amount<threshold){
				critical=false;
				accountDaoImpl.doCreditDebit(accountType, amount, type, username);
				status="approved";
				}
			accountDaoImpl.addToTransaction(amount, detail, status, username, date, null, critical,approverusername,accountType,accountType);
			notifyPage.addObject("notification","Payment Processed sucessfully");
		}
		return notifyPage;
	}
	
	@RequestMapping(value="/Merchantrequest", method=RequestMethod.POST)
	public ModelAndView merchantcreditDebitRequest(HttpServletRequest req,Authentication auth){
		String username=auth.getName();
		boolean critical=false;
		//ModelAndView notifyPage=new ModelAndView("notify");
		ModelAndView notifyPageM=new ModelAndView("notifyMer");
		String accountType=req.getParameter("accountType");
		String type=req.getParameter("type");
		double amount=Double.parseDouble(req.getParameter("amount"));
		String pubKey = req.getParameter("pubKey");
		System.out.println(accountType + " " + type + " " +amount+ " " +pubKey);
		boolean toProcess = false;
		
		if(pubKey!=null && pubKey.trim().length()!=0) {
			try {
				System.out.println("[TEST] 1");
				String lockedData = PKICertificate.lock(Double.toString(amount), pubKey);
				String prvKey = userDao.getPrivateKey(username);
				if (prvKey != null) {
					System.out.println("[TEST] 2 : Private found");
					prvKey = EncryptDecryptUtil.singleDecryption(prvKey);
					String unlockedData = PKICertificate.unlock(lockedData, prvKey);
					if (unlockedData.equals(Double.toString(amount))) {
						System.out.println("[TEST] 3 FINE");
						toProcess = true;
					} else {
						//TODO log statement
						System.out.println("[TEST] 4 WRONG");
					}
				} else {
					System.out.println("[TEST] 5 ; Private not found");
				}
			} catch(Exception e) {
				//TODO log statement
				System.out.println("[TEST] 6 EXCEPTION");
			}
		}
		
		if(!toProcess) {
			notifyPageM.addObject("notification","Wrong key passed. Payment failed.");
			return notifyPageM;
		}
		
		// add validation over credit and debit
		if(type.equalsIgnoreCase("Debit")){
			if(accountDaoImpl.checkAmount(accountType, amount,username)){
				String detail="Debit to "+accountType;
				//System.out.println("           &&&\n\n***This is the detail:::                :::"+detail);
				String status="pending";
				DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
				Date date = new Date();
				if(amount>=threshold)
					critical=true;


				//accountDaoImpl.addToTransaction(amount, detail, status, username, date, "", critical, null);


				accountDaoImpl.doCreditDebit(accountType, amount, type, username);
				notifyPageM.addObject("notification","Payment Processed sucessfully");
			}else{
				notifyPageM.addObject("notification","Insufficient Funds");
			}	
		}else{
			accountDaoImpl.doCreditDebit(accountType, amount, type, username);
			notifyPageM.addObject("notification","Payment Processed sucessfully");
		}
		return notifyPageM;
	}

	@RequestMapping(value="/tier1/requestForUser", method=RequestMethod.POST)
	public ModelAndView creditDebitRequestForUser(HttpServletRequest req,Authentication auth){
		String accountType=req.getParameter("accountType");
		String type=req.getParameter("type");
		double amount=Double.parseDouble(req.getParameter("amount"));
		String forUser = req.getParameter("forUser");
		String byUser = auth.getName();
		System.out.println("New request function");
		String pubKey = req.getParameter("pubKey");
		System.out.println(accountType + " " + type + " " +amount+ " " +pubKey);
		boolean toProcess = false;
		
		if(pubKey!=null && pubKey.trim().length()!=0) {
			try {
				System.out.println("[TEST] 1");
				String lockedData = PKICertificate.lock(Double.toString(amount), pubKey);
				String prvKey = userDao.getPrivateKey(byUser);
				if (prvKey != null) {
					System.out.println("[TEST] 2 : Private found");
					prvKey = EncryptDecryptUtil.singleDecryption(prvKey);
					String unlockedData = PKICertificate.unlock(lockedData, prvKey);
					if (unlockedData.equals(Double.toString(amount))) {
						System.out.println("[TEST] 3 FINE");
						toProcess = true;
					} else {
						//TODO log statement
						System.out.println("[TEST] 4 WRONG");
					}
				} else {
					System.out.println("[TEST] 5 ; Private not found");
				}
			} catch(Exception e) {
				//TODO log statement
				System.out.println("[TEST] 6 EXCEPTION");
			}
		}
		
		if(!toProcess) {
			ModelAndView notifyPage=new ModelAndView("notify");
			notifyPage.addObject("notification","Wrong key passed. Payment failed.");
			return notifyPage;
		}
		
		ModelAndView resultPage = checkBalanceAndCreditDebit(forUser, accountType, amount, type, byUser);
		return resultPage;
	}
	
	@RequestMapping(value="/tier2/requestForUser", method=RequestMethod.POST)
	public ModelAndView t2creditDebitRequestForUser(HttpServletRequest req,Authentication auth){
		String accountType=req.getParameter("accountType");
		String type=req.getParameter("type");
		double amount=Double.parseDouble(req.getParameter("amount"));
		String forUser = req.getParameter("forUser");
		String byUser = auth.getName();
		System.out.println("New request function");
		ModelAndView resultPage = checkBalanceAndCreditDebit(forUser, accountType, amount, type, byUser);
		return resultPage;
	}

	public ModelAndView checkBalanceAndCreditDebit(String transacterUserName, 
			String accountType, double amount, String type, String byUser) {
		boolean critical=false;
		ModelAndView notifyPage=new ModelAndView("notify");
		System.out.println(accountType + " " + type + " " +amount);
		if(amount>=threshold) {
			critical=true;
		}
		String status="pending";
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date = new Date();
		if(type.equalsIgnoreCase("Debit")){
			if(accountDaoImpl.checkAmount(accountType, amount,transacterUserName)){
				String detail="Debit from "+transacterUserName+ " accountType " + accountType + " by " + byUser + " for amount " + amount;
				System.out.println(detail);
				transactionBO.insertTransaction(-amount, detail, status, transacterUserName, date, null, critical, accountType, accountType);
				//accountDaoImpl.doCreditDebit(accountType, amount, type);
				notifyPage.addObject("notification","Payment Processed sucessfully");
			}else {
				
				notifyPage.addObject("notification","Insufficient Funds");
			}	
		}else if (type.equalsIgnoreCase("Credit")) {
			String detail="Credit from "+transacterUserName+ " accountType " + accountType + " by " + byUser + " for amount " + amount;
			System.out.println(detail);
			transactionBO.insertTransaction(amount, detail, status, transacterUserName, date, null, critical, accountType, accountType);
			
			//TODO: what if it is a critical credit? also, don't we want to add a transaction in this case?
			//accountDaoImpl.doCreditDebit(accountType, amount, type);
			notifyPage.addObject("notification","Payment Processed sucessfully");
		} else {
			notifyPage.addObject("notification", "Incorrect Banking Function accessed");
		}
		return notifyPage;
	}
	
	
	@RequestMapping(value="/tier1/transferForUser", method=RequestMethod.POST)
	public ModelAndView transferRequestForUser(HttpServletRequest req,Authentication auth){
		String byUser=auth.getName();
		System.out.println("New transfer function");
		String fromUserName=req.getParameter("forUser");
		String accountTypeTo=req.getParameter("to");
		String accountTypeFrom=req.getParameter("from");
		String typeOfTransfer=req.getParameter("typeoftransfer");
		String toUserEmail=req.getParameter("recipient");
		String toUserName = null;
		if (!typeOfTransfer.equalsIgnoreCase("internal")) {
			toUserName = userDao.getUserbyEmail(toUserEmail).getUsername();
		}
		double amount=Double.parseDouble(req.getParameter("amount"));
		
		String pubKey = req.getParameter("pubKey");
		System.out.println(amount+ " " +pubKey);
		boolean toProcess = false;
		
		if(pubKey!=null && pubKey.trim().length()!=0) {
			try {
				System.out.println("[TEST] 1");
				String lockedData = PKICertificate.lock(Double.toString(amount), pubKey);
				String prvKey = userDao.getPrivateKey(byUser);
				if (prvKey != null) {
					System.out.println("[TEST] 2 : Private found");
					prvKey = EncryptDecryptUtil.singleDecryption(prvKey);
					String unlockedData = PKICertificate.unlock(lockedData, prvKey);
					if (unlockedData.equals(Double.toString(amount))) {
						System.out.println("[TEST] 3 FINE");
						toProcess = true;
					} else {
						//TODO log statement
						System.out.println("[TEST] 4 WRONG");
					}
				} else {
					System.out.println("[TEST] 5 ; Private not found");
				}
			} catch(Exception e) {
				//TODO log statement
				System.out.println("[TEST] 6 EXCEPTION");
			}
		}
		
		if(!toProcess) {
			ModelAndView notifyPage=new ModelAndView("notify");
			notifyPage.addObject("notification","Wrong key passed. Payment failed.");
			return notifyPage;
		}
		
		ModelAndView notifyPage =
				checkBalanceAndPerformTransfer(fromUserName, accountTypeFrom, amount, toUserName, accountTypeTo, typeOfTransfer, byUser);
		
		return notifyPage;
	}
	
	@RequestMapping(value="/tier2/transferForUser", method=RequestMethod.POST)
	public ModelAndView t2transferRequestForUser(HttpServletRequest req,Authentication auth){
		System.out.println("1");
		String byUser=auth.getName();
		System.out.println("2");
		
		System.out.println("New transfer function");
		System.out.println("3");
		
		String fromUserName=req.getParameter("forUser");
		System.out.println("4");
		
		String accountTypeTo=req.getParameter("to");
		System.out.println("5");
		
		String accountTypeFrom=req.getParameter("from");
		System.out.println("6");
		
		String typeOfTransfer=req.getParameter("typeoftransfer");
		System.out.println("7");
		
		String toUserEmail=req.getParameter("recipient");
		System.out.println("8");
		String toUserName = null;
		if(!typeOfTransfer.equalsIgnoreCase("internal")) {
			toUserName = userDao.getUserbyEmail(toUserEmail).getUsername();
		}
		System.out.println("9");
		
		double amount=Double.parseDouble(req.getParameter("amount"));
		System.out.println("10");
		
		ModelAndView notifyPage =
				checkBalanceAndPerformTransfer(fromUserName, accountTypeFrom, amount, toUserName, accountTypeTo, typeOfTransfer, byUser);
		System.out.println("11");
		
		return notifyPage;
	}

	public ModelAndView checkBalanceAndPerformTransfer(String fromUserName,
        String accountTypeFrom, double amount, String toUserName, String accountTypeTo, String typeOfTransfer, String byUser) {
		
		boolean check=accountDaoImpl.checkAmount(accountTypeFrom, amount,fromUserName);
		boolean critical = false;
		ModelAndView notifyPage=new ModelAndView("notify");
		if(check){
			String tousername="";
			String status="pending";
			DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			Date date = new Date();
			if(amount>=threshold) {
				critical=true;
			}
			String detail="";
			if(typeOfTransfer.equalsIgnoreCase("internal")){
				 detail="Transfer to "+ accountTypeTo + " from "+ accountTypeFrom + " for user " + fromUserName + " by user" + byUser;
				 tousername=accountTypeFrom;
				 System.out.println("transfer1:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::");
					
				 transactionBO.insertTransaction(amount, detail, status, fromUserName, date, null, critical, accountTypeFrom, accountTypeTo);
				 System.out.println("transfer2:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::");
					
			}
			else{
				if (accountTypeTo == null) {
					accountTypeTo = "Saving";
				}
				 detail="Transfer to "+ toUserName + " from "+ fromUserName + " for amount " + amount + " by user " + byUser;
				 transactionBO.insertTransaction(amount, detail, status, fromUserName, date, toUserName, critical, accountTypeFrom, accountTypeTo);
			}
			notifyPage.addObject("notification","Transfer Processed Sucessfully");
	    } else {
	    	notifyPage.addObject("notification","Insufficient funds to transfer");
	    }
		System.out.println("transfer:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::");
		
		return notifyPage;
	}
	
	@RequestMapping(value="/transfer", method=RequestMethod.POST)
	public ModelAndView transferRequest(HttpServletRequest req,Authentication auth){
		String username=auth.getName();
		ModelAndView notifyPage=new ModelAndView("notify");
		String accountTypeFrom=req.getParameter("from");
		String accountTypeTo=req.getParameter("to");
		String typeOfTransfer=req.getParameter("typeoftransfer");
		String recipient=req.getParameter("recipient");
		double amount=Double.parseDouble(req.getParameter("amount"));
		String pubKey = req.getParameter("pubKey");
		
		System.out.println(accountTypeFrom + " " + accountTypeTo + " " +amount+ " " +pubKey);
		boolean toProcess = false;
		
		if(pubKey!=null && pubKey.trim().length()!=0) {
			try {
				System.out.println("[TEST] 1");
				String lockedData = PKICertificate.lock(Double.toString(amount), pubKey);
				String prvKey = userDao.getPrivateKey(username);
				if (prvKey != null) {
					System.out.println("[TEST] 2 : Private found");
					prvKey = EncryptDecryptUtil.singleDecryption(prvKey);
					String unlockedData = PKICertificate.unlock(lockedData, prvKey);
					if (unlockedData.equals(Double.toString(amount))) {
						System.out.println("[TEST] 3 FINE");
						toProcess = true;
					} else {
						//TODO log statement
						System.out.println("[TEST] 4 WRONG");
					}
				} else {
					System.out.println("[TEST] 5 ; Private not found");
				}
			} catch(Exception e) {
				//TODO log statement
				System.out.println("[TEST] 6 EXCEPTION");
			}
		}
		
		if(!toProcess) {
			notifyPage.addObject("notification","Wrong key passed. Payment failed.");
			return notifyPage;
		}
		
		boolean critical=false;
		
		// add validation over credit and debit
		boolean check=accountDaoImpl.checkAmount(accountTypeFrom, amount,username);
		if(check){
			String tousername="";
			String status="pending";
			DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			Date date = new Date();
			if(amount>=threshold) critical=true;
			String detail="";
			if(typeOfTransfer.equalsIgnoreCase("internal")){
				 detail="Transfer to "+ accountTypeTo + " from "+ accountTypeFrom;
				 tousername=accountTypeTo;
				 if(critical==false){
					 accountDaoImpl.doTransferInternal(username,amount, accountTypeFrom,accountTypeTo);
					 status="approved";

				 }
				 accountDaoImpl.addToTransaction(amount, detail, status, username, date, tousername, critical,null,accountTypeFrom,accountTypeTo); 

			}
			else{    // external
				  String to=accountDaoImpl.getusername(recipient);
				  tousername=to;
				 detail="Transfer to "+ recipient + " from "+ accountTypeFrom;
				 System.out.println("inside external");
				accountDaoImpl.addToTransaction(amount, detail, status, username, date, tousername, critical,null,accountTypeFrom,"Saving"); 
				//accountDaoImpl.doTransferExternal(username, amount, accountTypeFrom, tousername);
			}
			
			//accountDaoImpl.doTransfer(accountTypeFrom,tousername,amount, username);
			notifyPage.addObject("notification","Payment Processed sucessfully");
		}else{
			notifyPage.addObject("notification","Insufficient Funds");
		}
		return notifyPage;
	}
	
	
	@RequestMapping(value="/Merchanttransfer", method=RequestMethod.POST)
	public ModelAndView MerchanttransferRequest(HttpServletRequest req,Authentication auth){
		String username=auth.getName();
		ModelAndView notifyPageM=new ModelAndView("notifyMer");
		String accountTypeFrom=req.getParameter("from");
		String accountTypeTo=req.getParameter("to");
		String typeOfTransfer=req.getParameter("typeoftransfer");
		String recipient=req.getParameter("recipient");
		double amount=Double.parseDouble(req.getParameter("amount"));
		System.out.println(accountTypeFrom + " " + accountTypeTo + " " +amount);
		boolean critical=false;
		
		// add validation over credit and debit
		boolean check=accountDaoImpl.checkAmount(accountTypeFrom, amount,username);
		if(check){
			String tousername="";
			String status="pending";
			DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			Date date = new Date();
			if(amount>=threshold) critical=true;
			String detail="";
			if(typeOfTransfer.equalsIgnoreCase("internal")){
				 detail="Transfer to "+ accountTypeTo + " from "+ accountTypeFrom;
				 tousername=accountTypeTo;
				 accountDaoImpl.addToTransaction(amount, detail, status, username, date, tousername, critical,null,accountTypeFrom,accountTypeTo); 
				 accountDaoImpl.doTransferInternal(username,amount, accountTypeFrom,accountTypeTo);

			}
			else{    // external
				  String to=accountDaoImpl.getusername(recipient);
				  tousername=to;
				 detail="Transfer to "+ recipient + " from "+ accountTypeFrom;
				 System.out.println("inside external");
				accountDaoImpl.addToTransaction(amount, detail, status, username, date, tousername, critical,null,accountTypeFrom,"Saving"); 
				accountDaoImpl.doTransferExternal(username, amount, accountTypeFrom, tousername);
			}
			
			//accountDaoImpl.doTransfer(accountTypeFrom,tousername,amount, username);
			notifyPageM.addObject("notification","Payment Processed sucessfully");
		}else{
			notifyPageM.addObject("notification","Insufficient Funds");
		}
		return notifyPageM;
	}
	
	
	@RequestMapping(value="/payment", method=RequestMethod.POST)
	public ModelAndView paymentRequest(HttpServletRequest req,Authentication auth){
		String username=auth.getName();
		boolean critical=false;
		ModelAndView notifyPage=new ModelAndView("notify");
		String accountTypeFrom=req.getParameter("from");
		String accountTypeTo=req.getParameter("to");
		double amount=Double.parseDouble(req.getParameter("amount"));
		String pubKey = req.getParameter("pubKey");
		System.out.println(accountTypeFrom + " " + accountTypeTo + " " +amount+ " " +pubKey);
		boolean toProcess = false;
		
		if(pubKey!=null && pubKey.trim().length()!=0) {
			try {
				System.out.println("[TEST] 1");
				String lockedData = PKICertificate.lock(Double.toString(amount), pubKey);
				String prvKey = userDao.getPrivateKey(username);
				if (prvKey != null) {
					System.out.println("[TEST] 2 : Private found");
					prvKey = EncryptDecryptUtil.singleDecryption(prvKey);
					String unlockedData = PKICertificate.unlock(lockedData, prvKey);
					if (unlockedData.equals(Double.toString(amount))) {
						System.out.println("[TEST] 3 FINE");
						toProcess = true;
					} else {
						//TODO log statement
						System.out.println("[TEST] 4 WRONG");
					}
				} else {
					System.out.println("[TEST] 5 ; Private not found");
				}
			} catch(Exception e) {
				//TODO log statement
				System.out.println("[TEST] 6 EXCEPTION");
			}
		}
		
		if(!toProcess) {
			notifyPage.addObject("notification","Wrong key passed. Payment failed.");
			return notifyPage;
		}
		
		// add validation over cedit and debit
		// check if both to and from are same
		boolean check=accountDaoImpl.checkAmount(accountTypeFrom, amount, username);
		if(check){
			//ADDING TO TRANSACTION TABLE
			String detail="Paid to "+ accountTypeTo;
			String status="pending";
			DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			Date date = new Date();
			if(amount>=threshold) critical=true;

			accountDaoImpl.addToTransaction(amount, detail, status, username, date, null, critical,null, accountTypeFrom,"payment"); 
			//accountDaoImpl.doPayment(accountTypeFrom,amount,username);
			notifyPage.addObject("notification","Payment Processed sucessfully");
		}else{
			notifyPage.addObject("notification","Insufficient Funds");
		}
		return notifyPage;
	}
	

	
	
	
	
	
	@RequestMapping(value="/tier1/paymentForUser", method=RequestMethod.POST)
	public ModelAndView createPaymentRequestForUser(HttpServletRequest req, Authentication auth) {
		String byusername=auth.getName();
		String accountTypeTo = SAVINGS_ACCOUNT_TYPE;
		boolean critical=false;
		ModelAndView notifyPage=new ModelAndView("notify");
		String fromUser = req.getParameter("forUser");
		String accountTypeFrom=req.getParameter("from");
		String recipientEmail=req.getParameter("to");
		String toUserName = userDao.getUserbyEmail(recipientEmail).getUsername();
		double amount=Double.parseDouble(req.getParameter("amount"));
		String pubKey = req.getParameter("pubKey");
		System.out.println(accountTypeFrom + " " + fromUser + " " +amount+ " " +pubKey);
		boolean toProcess = false;
		
		if(pubKey!=null && pubKey.trim().length()!=0) {
			try {
				System.out.println("[TEST] 1");
				String lockedData = PKICertificate.lock(Double.toString(amount), pubKey);
				String prvKey = userDao.getPrivateKey(byusername);
				if (prvKey != null) {
					System.out.println("[TEST] 2 : Private found");
					prvKey = EncryptDecryptUtil.singleDecryption(prvKey);
					String unlockedData = PKICertificate.unlock(lockedData, prvKey);
					if (unlockedData.equals(Double.toString(amount))) {
						System.out.println("[TEST] 3 FINE");
						toProcess = true;
					} else {
						//TODO log statement
						System.out.println("[TEST] 4 WRONG");
					}
				} else {
					System.out.println("[TEST] 5 ; Private not found");
				}
			} catch(Exception e) {
				//TODO log statement
				System.out.println("[TEST] 6 EXCEPTION");
			}
		}
		
		if(!toProcess) {
			notifyPage.addObject("notification","Wrong key passed. Payment failed.");
			return notifyPage;
		}
		
		// add validation over cedit and debit
		// check if both to and from are same
		boolean check=accountDaoImpl.checkAmount(accountTypeFrom, amount, byusername);
		if(check){
			//ADDING TO TRANSACTION TABLE
			String detail="Paid to "+ accountTypeTo;
			String status="pending";
			DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			Date date = new Date();
			if(amount>threshold) critical=true;
			transactionBO.insertTransaction(amount, detail, status, fromUser, date, toUserName, critical, accountTypeFrom, accountTypeTo);
			notifyPage.addObject("notification","Payment Processed sucessfully");
		}else{
			notifyPage.addObject("notification","Insufficient Funds");
		}
		return notifyPage;
	}
	@RequestMapping(value="/tier2/paymentForUser", method=RequestMethod.POST)
	public ModelAndView t2createPaymentRequestForUser(HttpServletRequest req, Authentication auth) {
		String byusername=auth.getName();
		String accountTypeTo = SAVINGS_ACCOUNT_TYPE;
		boolean critical=false;
		ModelAndView notifyPage=new ModelAndView("notify");
		String fromUser = req.getParameter("forUser");
		String accountTypeFrom=req.getParameter("from");
		String recipientEmail=req.getParameter("to");
		String toUserName = userDao.getUserbyEmail(recipientEmail).getUsername();
		double amount=Double.parseDouble(req.getParameter("amount"));
		System.out.println(accountTypeFrom + " " + accountTypeTo + " " +amount);
		
		// add validation over cedit and debit
		// check if both to and from are same
		boolean check=accountDaoImpl.checkAmount(accountTypeFrom, amount, byusername);
		if(check){
			//ADDING TO TRANSACTION TABLE
			String detail="Paid to "+ accountTypeTo;
			String status="pending";
			DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			Date date = new Date();
			if(amount>threshold) critical=true;
			transactionBO.insertTransaction(amount, detail, status, fromUser, date, toUserName, critical, accountTypeFrom, accountTypeTo);
			notifyPage.addObject("notification","Payment Processed sucessfully");
		}else{
			notifyPage.addObject("notification","Insufficient Funds");
		}
		return notifyPage;
	}
	
	
	
	
	
	
	@RequestMapping(value="/GetCustomerPayment", method=RequestMethod.POST)
	public ModelAndView CustomerpaymentRequest(HttpServletRequest req,Authentication auth){
		String username=auth.getName();
		boolean critical=false;
		ModelAndView notifyPageM=new ModelAndView("notifyMer");
		String accountFrom=req.getParameter("fromName");
		System.out.println(accountFrom);
		String accountTypeTo=req.getParameter("to");
		String cvv=req.getParameter("fromCVV");
		String cardno=req.getParameter("fromCard");
		double amount=Double.parseDouble(req.getParameter("amount"));
		String pubKey = req.getParameter("pubKey");
		System.out.println(amount+ " " +pubKey);
		boolean toProcess = false;
		
		if(pubKey!=null && pubKey.trim().length()!=0) {
			try {
				System.out.println("[TEST] 1");
				String lockedData = PKICertificate.lock(Double.toString(amount), pubKey);
				String prvKey = userDao.getPrivateKey(username);
				if (prvKey != null) {
					System.out.println("[TEST] 2 : Private found");
					prvKey = EncryptDecryptUtil.singleDecryption(prvKey);
					String unlockedData = PKICertificate.unlock(lockedData, prvKey);
					if (unlockedData.equals(Double.toString(amount))) {
						System.out.println("[TEST] 3 FINE");
						toProcess = true;
					} else {
						//TODO log statement
						System.out.println("[TEST] 4 WRONG");
					}
				} else {
					System.out.println("[TEST] 5 ; Private not found");
				}
			} catch(Exception e) {
				//TODO log statement
				System.out.println("[TEST] 6 EXCEPTION");
			}
		}
		
		if(!toProcess) {
			notifyPageM.addObject("notification","Wrong key passed. Payment failed.");
			return notifyPageM;
		}
		
		// add validation over credit and debit
		// check if both to and from are same
		boolean verify=accountDaoImpl.checkDet(accountFrom,cardno);
		boolean checkCustomerAmount=false;
		String usernameofuser="";
		if(verify) {
			 checkCustomerAmount=accountDaoImpl.checkCAmount(cardno,cvv,amount);
			 usernameofuser=accountDaoImpl.getusernameMerchant(cardno);

		}
		
		System.out.println("one" + checkCustomerAmount);
		System.out.println("second" + verify);
		if(verify&&checkCustomerAmount){
			//ADDING TO TRANSACTION TABLE
			String detail="From user"+ accountFrom;
			String status="pending";
			DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			Date date = new Date();
			if(amount>threshold) critical=true;
			accountDaoImpl.addToTransaction(amount, detail, status, username, date, null, critical,"",accountFrom, accountTypeTo); 
			accountDaoImpl.MPayment(cardno,cvv,amount,usernameofuser,username,accountTypeTo);
			//accountDaoImpl.doPayment(accountTypeTo,-amount);
			notifyPageM.addObject("notification","Payment Processed sucessfully");
		}else{
			notifyPageM.addObject("notification","Insufficient Funds");
		}
		return notifyPageM;
	}
	
	@RequestMapping(value="/notifyMer", method=RequestMethod.GET)
	public String Merrequest(HttpServletRequest req){
		return "notifyMer";
	}
	
	@RequestMapping(value="/notify", method=RequestMethod.GET)
	public String request(HttpServletRequest req){
		return "notify";
	}
	
}
