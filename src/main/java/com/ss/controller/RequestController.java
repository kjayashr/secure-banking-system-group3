package com.ss.controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.apache.log4j.Logger;
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
import com.ss.model.User;
import com.ss.security.PKICertificate;
import com.ss.service.EncryptDecryptUtil;
import com.ss.service.MailService;
import com.ss.dao.TransactionBO;
import com.ss.dao.UserDao;

@Controller
public class RequestController {
	final static Logger logger = Logger.getLogger(RequestController.class);
	private static String SAVINGS_ACCOUNT_TYPE = "Saving";
	private static String PAYMENT_ACCOUNT_TYPE = "payment";

	@Autowired
	AccountDaoImpl accountDaoImpl;
	int threshold=1000;
	
	@Autowired
	UserDao userDao;
	
	@Autowired
	TransactionBO transactionBO;
	
	private boolean isKeyValid(String username, Double amount, String pubKey) {
		boolean retValue = false;
		if(pubKey!=null && pubKey.trim().length()!=0) {
			try {
				String lockedData = PKICertificate.lock(Double.toString(amount), pubKey);
				String prvKey = userDao.getPrivateKey(username);
				if (prvKey != null) {
					prvKey = EncryptDecryptUtil.singleDecryption(prvKey);
					String unlockedData = PKICertificate.unlock(lockedData, prvKey);
					if (unlockedData.equals(Double.toString(amount))) {
						retValue = true;
					} 
				}
			} catch(Exception e) {
				//TODO log statement
			}
		}
		return retValue;
	}
	
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
		
		if(!isKeyValid(username, amount, pubKey)) {
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
	public ModelAndView MerchantcreditDebitRequest(HttpServletRequest req,Authentication auth){
		String username=auth.getName();
		boolean critical=true;
		ModelAndView notifyPageM=new ModelAndView("notifyMer");
		String accountType=req.getParameter("accountType");
		String type=req.getParameter("type");
		String approverusername=null;
		double amount=Double.parseDouble(req.getParameter("amount"));
		String pubKey = req.getParameter("pubKey");
		System.out.println(accountType + " " + type + " " +amount+ " " +pubKey);
		
		if(!isKeyValid(username, amount, pubKey)) {
			notifyPageM.addObject("notification","Wrong key passed. Payment failed.");
			return notifyPageM;
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
//					if(accountType.equalsIgnoreCase("Credit Card")){
//						accountDaoImpl.updateCreditCardTable(username, amount);
//
//					}
					status="approved";

				}
				amount=amount*(-1);
				accountDaoImpl.addToTransaction(amount, detail, status, username, date, null, critical,approverusername,accountType,accountType);
				notifyPageM.addObject("notification","Payment Processed sucessfully");
			}else{
				notifyPageM.addObject("notification","Insufficient Funds");
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
				if(accountType.equalsIgnoreCase("Credit Card")){
					amount=amount*(-1);

				}
				status="approved";
				}
			accountDaoImpl.addToTransaction(amount, detail, status, username, date, null, critical,approverusername,accountType,accountType);
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
		
		if(!isKeyValid(byUser, amount, pubKey)) {
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
		String pubKey = req.getParameter("pubKey");
		System.out.println(accountType + " " + type + " " +amount+ " " +pubKey);
		
		if(!isKeyValid(byUser, amount, pubKey)) {
			ModelAndView notifyPage=new ModelAndView("notify");
			notifyPage.addObject("notification","Wrong key passed. Payment failed.");
			return notifyPage;
		}
		
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
				if (!critical) {
					status = "approved";
					accountDaoImpl.doCreditDebit(accountType, amount, type, transacterUserName);
					notifyPage.addObject("notification","Debit Processed sucessfully");
				} else {
					notifyPage.addObject("notification","Debit Accepted But Pending Employee Approval");
				}
				transactionBO.insertTransaction(-amount, detail, status, transacterUserName, date, null, critical, accountType, accountType);
				
			}else {
				User userDetails = userDao.getUserbyUsername(transacterUserName);
				String userEmail = userDetails.getEmail();
				String toName = userDetails.getFirstname() + " " + userDetails.getLastname();
				System.out.println(toName + " "+ userEmail );
				MailService.mailCrticalTransaction(userEmail, toName, amount, false);
				notifyPage.addObject("notification","Insufficient Funds");
			}
		}else if (type.equalsIgnoreCase("Credit")) {
			String detail="Credit from "+transacterUserName+ " accountType " + accountType + " by " + byUser + " for amount " + amount;
			System.out.println(detail);
			if (!critical) {
				status = "approved";
				accountDaoImpl.doCreditDebit(accountType, amount, type, transacterUserName);
				notifyPage.addObject("notification","Credit Processed sucessfully");
			} else {
				notifyPage.addObject("notification","Credit accepted sucessfully");
			}
			transactionBO.insertTransaction(amount, detail, status, transacterUserName, date, null, critical, accountType, accountType);
			
			
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
		
		if(!isKeyValid(byUser, amount, pubKey)) {
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
		String byUser=auth.getName();
		System.out.println("New transfer function");
		String fromUserName=req.getParameter("forUser");
		String accountTypeTo=req.getParameter("to");
		String accountTypeFrom=req.getParameter("from");
		String typeOfTransfer=req.getParameter("typeoftransfer");
		String toUserEmail=req.getParameter("recipient");
		String toUserName = null;
		if(!typeOfTransfer.equalsIgnoreCase("internal")) {
			toUserName = userDao.getUserbyEmail(toUserEmail).getUsername();
		}
		double amount=Double.parseDouble(req.getParameter("amount"));
		String pubKey = req.getParameter("pubKey");
		System.out.println(amount+ " " +pubKey);
		
		if(!isKeyValid(byUser, amount, pubKey)) {
			ModelAndView notifyPage=new ModelAndView("notify");
			notifyPage.addObject("notification","Wrong key passed. Payment failed.");
			return notifyPage;
		}
		
		ModelAndView notifyPage =
				checkBalanceAndPerformTransfer(fromUserName, accountTypeFrom, amount, toUserName, accountTypeTo, typeOfTransfer, byUser);
		
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
				 
				 if (!critical) {
					 accountDaoImpl.doTransferInternal(fromUserName,amount, accountTypeFrom,accountTypeTo);
				     status="approved";
				     logger.info("internal transaction successfully approved");
						notifyPage.addObject("notification","Transfer Processed Sucessfully");
				 } else {
				     User userDetails = userDao.getUserbyUsername(fromUserName);
					 String userEmail = userDetails.getEmail();
					 String toName = userDetails.getFirstname() + " " + userDetails.getLastname();
					 MailService.mailCrticalTransaction(userEmail, toName, amount, false);
					 logger.info("Email sent for critical transaction");
					 notifyPage.addObject("notification","Transfer Accepted but needs Employee Approval");
				 }
				 transactionBO.insertTransaction(amount, detail, status, fromUserName, date, null, critical, accountTypeFrom, accountTypeTo);
				 logger.info("transaction inserted for user " + fromUserName);
			}
			else{
				if (accountTypeTo == null) {
					accountTypeTo = "Saving";
				}
				 detail="Transfer to "+ toUserName + " from "+ fromUserName + " for amount " + amount + " by user " + byUser;
				 logger.info(detail);
				 if (amount>=threshold) {
					 User userDetails = userDao.getUserbyUsername(fromUserName);
					 String userEmail = userDetails.getEmail();
					 String toName = userDetails.getFirstname() + " " + userDetails.getLastname();
					 MailService.mailCrticalTransaction(userEmail, toName, amount, false);
					 logger.info("Email sent for critical transaction");
					 notifyPage.addObject("notification","Transfer Accepted but needs Employee and User Approval");
				 } else {
					notifyPage.addObject("notification","External Transfer pending User Approval");
				 }
				 transactionBO.insertTransaction(amount, detail, status, fromUserName, date, toUserName, critical, accountTypeFrom, accountTypeTo);
					logger.info("External transfer accepted");
			}
	    } else {
	    	notifyPage.addObject("notification","Insufficient funds to transfer");
	    }
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
		
		if(!isKeyValid(username, amount, pubKey)) {
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
				 accountDaoImpl.addToTransaction(amount, detail, status, username, date, null, critical,null,accountTypeFrom,accountTypeTo); 

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
		String pubKey = req.getParameter("pubKey");
		
		if(!isKeyValid(username, amount, pubKey)) {
			notifyPageM.addObject("notification","Wrong key passed. Payment failed.");
			return notifyPageM;
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
				 detail="Transfer to "+ recipient + " from "+ accountTypeFrom+ recipient;
				 System.out.println("inside external");
				accountDaoImpl.addToTransaction(amount, detail, status, username, date, tousername, critical,null,accountTypeFrom,"Saving"); 
				//accountDaoImpl.doTransferExternal(username, amount, accountTypeFrom, tousername);
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
		boolean critical=true;
		ModelAndView notifyPage=new ModelAndView("notify");
		String accountTypeFrom=req.getParameter("from");
		String accountTypeTo=req.getParameter("to");
		double amount=Double.parseDouble(req.getParameter("amount"));
		String pubKey = req.getParameter("pubKey");
		System.out.println(accountTypeFrom + " " + accountTypeTo + " " +amount+ " " +pubKey);
		
		if(!isKeyValid(username, amount, pubKey)) {
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
			if(amount<threshold) {
				critical=false;
				accountDaoImpl.doPayment(accountTypeFrom,amount,username);
				
				status="approved";

			}

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
		String accountTypeTo = PAYMENT_ACCOUNT_TYPE;
		boolean critical=false;
		ModelAndView notifyPage=new ModelAndView("notify");
		String fromUser = req.getParameter("forUser");
		String accountTypeFrom=req.getParameter("from");
		String toUserName = null;
		double amount=Double.parseDouble(req.getParameter("amount"));
		String pubKey = req.getParameter("pubKey");
		System.out.println(accountTypeFrom + " " + fromUser + " " +amount+ " " +pubKey);
		
		if(!isKeyValid(byusername, amount, pubKey)) {
			notifyPage.addObject("notification","Wrong key passed. Payment failed.");
			return notifyPage;
		}
		
		// add validation over cedit and debit
		// check if both to and from are same
		boolean check=accountDaoImpl.checkAmount(accountTypeFrom, amount, fromUser);
		if(check){
			//ADDING TO TRANSACTION TABLE
			String detail="Paid to "+ accountTypeTo;
			String status="pending";
			DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			Date date = new Date();
			if(amount>=threshold) {
				critical=true;
				User userDetails = userDao.getUserbyUsername(fromUser);
				String userEmail = userDetails.getEmail();
				String toName = userDetails.getFirstname() + " " + userDetails.getLastname();
				MailService.mailCrticalTransaction(userEmail, toName, amount, false);
				logger.info("Email sent for critical transaction");
				notifyPage.addObject("notification","Payment accepted but need Employee approval");
			} else {
				status = "approved";
				accountDaoImpl.doCreditDebit(accountTypeFrom, amount, "debit", fromUser);
				notifyPage.addObject("notification","Payment processed successfully");
				logger.info("Non critical payment successful");
			}
			transactionBO.insertTransaction(amount, detail, status, fromUser, date, toUserName, critical, accountTypeFrom, accountTypeTo);
		}else{
			notifyPage.addObject("notification","Insufficient Funds");
		}
		return notifyPage;
	}
	@RequestMapping(value="/tier2/paymentForUser", method=RequestMethod.POST)
	public ModelAndView t2createPaymentRequestForUser(HttpServletRequest req, Authentication auth) {
		String byusername=auth.getName();
		String accountTypeTo = PAYMENT_ACCOUNT_TYPE;
		boolean critical=false;
		ModelAndView notifyPage=new ModelAndView("notify");
		String fromUser = req.getParameter("forUser");
		String accountTypeFrom=req.getParameter("from");
		String toUserName = null;
		double amount=Double.parseDouble(req.getParameter("amount"));
		String pubKey = req.getParameter("pubKey");
		System.out.println(amount+ " " +pubKey);
		
		if(!isKeyValid(byusername, amount, pubKey)) {
			notifyPage.addObject("notification","Wrong key passed. Payment failed.");
			return notifyPage;
		}
		
		
		// add validation over cedit and debit
		// check if both to and from are same
		System.out.println("accountTypeFrom " + accountTypeFrom + " amount " + amount + " fromUser " + fromUser);
		boolean check=accountDaoImpl.checkAmount(accountTypeFrom, amount, fromUser);
		
		if(check){
			//ADDING TO TRANSACTION TABLE
			String detail="Paid to "+ accountTypeTo;
			String status="pending";
			DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			Date date = new Date();
			if(amount>=threshold) critical=true;
			
			if (!critical) {
				status = "approved";
				accountDaoImpl.doCreditDebit(accountTypeFrom, amount, "debit", fromUser);
				notifyPage.addObject("notification","Payment Processed sucessfully");
				logger.info("Non critical transaction processed sucessfully for user " + fromUser);
			} else {
				User userDetails = userDao.getUserbyUsername(fromUser);
				String userEmail = userDetails.getEmail();
				String toName = userDetails.getFirstname() + " " + userDetails.getLastname();
				MailService.mailCrticalTransaction(userEmail, toName, amount, false);
				logger.info("Email sent for critical transaction");
				notifyPage.addObject("notification","Payment accepted but needs employee approval");
			}
			System.out.println("Inserting payment transaction");
			transactionBO.insertTransaction(amount, detail, status, fromUser, date, toUserName, critical, accountTypeFrom, accountTypeTo);
			
		}else{
			notifyPage.addObject("notification","Insufficient Funds");
		}
		return notifyPage;
	}
	
	
	@RequestMapping(value="/GetCustomerPayment", method=RequestMethod.POST)
	public ModelAndView CustomerpaymentRequest(HttpServletRequest req,Authentication auth){
		String username=auth.getName();
		boolean critical=true;
		String approverusername=null;
		ModelAndView notifyPageM=new ModelAndView("notifyMer");
		String accountFrom="Credit Card";
		System.out.println(accountFrom);
		String accountTypeTo=req.getParameter("to");
		String cvv=req.getParameter("fromCVV");
		String cardno=req.getParameter("fromCard");
		System.out.println(cardno+"This is my card");
		double amount=Double.parseDouble(req.getParameter("amount"));
		String pubKey = req.getParameter("pubKey");
		System.out.println(amount+ " " +pubKey);
		
		if(!isKeyValid(username, amount, pubKey)) {
			notifyPageM.addObject("notification","Wrong key passed. Payment failed.");
			return notifyPageM;
		}
		
		// add validation over credit and debit
		// check if both to and from are same
		boolean verify=accountDaoImpl.checkDet(accountDaoImpl.getusernameMerchant(cardno),cardno);
		System.out.println(verify+"i wrote this");
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
			
			String approverUSerName=null;
			if(amount<threshold) 
			{
				critical=false;
				accountDaoImpl.MPayment(cardno,cvv,amount,usernameofuser,username,accountTypeTo);
				status="approved";
				approverusername=
						 usernameofuser=accountDaoImpl.getusernameMerchant(cardno);;
				
				
				
			}
			accountDaoImpl.addToTransaction(amount, detail, status, usernameofuser, date, username, critical,approverusername,accountFrom, accountTypeTo);
			
			
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
