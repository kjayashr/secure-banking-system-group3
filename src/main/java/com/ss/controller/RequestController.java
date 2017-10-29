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
import com.ss.dao.TransactionBO;
import com.ss.dao.UserDao;

@Controller
public class RequestController {

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
		boolean critical=false;
		ModelAndView notifyPage=new ModelAndView("notify");
		String accountType=req.getParameter("accountType");
		String type=req.getParameter("type");
		double amount=Double.parseDouble(req.getParameter("amount"));
		System.out.println(accountType + " " + type + " " +amount);
		// add validation over credit and debit
		if(type.equalsIgnoreCase("Debit")){
			if(accountDaoImpl.checkAmount(accountType, amount,username)){
				String detail="Debit to "+accountType;
				String status="pending";
				DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
				Date date = new Date();
				if(amount>=threshold)
					critical=true;
				accountDaoImpl.addToTransaction(amount, detail, status, username, date, null, critical);
				accountDaoImpl.doCreditDebit(accountType, amount, type, username);
				notifyPage.addObject("notification","Payment Processed sucessfully");
			}else{
				notifyPage.addObject("notification","Insufficient Funds");
			}	
		}else{
			accountDaoImpl.doCreditDebit(accountType, amount, type, username);
			notifyPage.addObject("notification","Payment Processed sucessfully");
		}
		return notifyPage;
	}
	

	@RequestMapping(value="/tier1/requestForUser", method=RequestMethod.POST)
	public ModelAndView creditDebitRequestForUser(HttpServletRequest req,Authentication auth){
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
		if(type.equalsIgnoreCase("Debit")){
			if(accountDaoImpl.checkAmount(accountType, amount,transacterUserName)){
				String detail="Debit to "+transacterUserName+ " accountType " + accountType + " by " + byUser + " for amount " + amount;
				System.out.println(detail);
				String status="pending";
				DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
				Date date = new Date();
				if(amount>=threshold)
					critical=true;
				transactionBO.insertTransaction(amount, detail, status, transacterUserName, date, null, critical, accountType, accountType);
				//accountDaoImpl.doCreditDebit(accountType, amount, type);
				notifyPage.addObject("notification","Payment Processed sucessfully");
			}else {
				notifyPage.addObject("notification","Insufficient Funds");
			}	
		}else if (type.equalsIgnoreCase("Credit")) {
			//TODO: what if it is a critical credit? also, don't we want to add a transaction in this case?
			accountDaoImpl.doCreditDebit(accountType, amount, type);
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
		String toUserName = userDao.getUserbyEmail(toUserEmail).getUsername();
		double amount=Double.parseDouble(req.getParameter("amount"));
		
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
				 transactionBO.insertTransaction(amount, detail, status, fromUserName, date, null, critical, accountTypeFrom, accountTypeTo);
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
			}
			else{
				  String to=accountDaoImpl.getusername(recipient);
				  tousername=to;
				 detail="Transfer to "+ recipient + " from "+ accountTypeFrom;
			}
			
			accountDaoImpl.addToTransaction(amount, detail, status, username, date, tousername, critical); 
			accountDaoImpl.doTransfer(accountTypeFrom,tousername,amount);
			notifyPage.addObject("notification","Payment Processed sucessfully");
		}else{
			notifyPage.addObject("notification","Insufficient Funds");
		}
		return notifyPage;
	}
	
	@RequestMapping(value="/payment", method=RequestMethod.POST)
	public ModelAndView paymentRequest(HttpServletRequest req,Authentication auth){
		String username=auth.getName();
		boolean critical=false;
		ModelAndView notifyPage=new ModelAndView("notify");
		String accountTypeFrom=req.getParameter("from");
		String accountTypeTo=req.getParameter("to");
		double amount=Double.parseDouble(req.getParameter("amount"));
		System.out.println(accountTypeFrom + " " + accountTypeTo + " " +amount);
		
		// add validation over cedit and debit
		// check if both to and from are same
		boolean check=accountDaoImpl.checkAmount(accountTypeFrom, amount, username);
		if(check){
			//ADDING TO TRANSACTION TABLE
			String detail="Paid to "+ accountTypeTo;
			String status="pending";
			DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			Date date = new Date();
			if(amount>threshold) critical=true;
			accountDaoImpl.addToTransaction(amount, detail, status, username, date, null, critical); 
			accountDaoImpl.doPayment(accountTypeFrom,amount);
			notifyPage.addObject("notification","Payment Processed sucessfully");
		}else{
			notifyPage.addObject("notification","Insufficient Funds");
		}
		return notifyPage;
	}
	

	
	@RequestMapping(value="/tier1/paymentForUser", method=RequestMethod.POST)
	public ModelAndView createPaymentRequestForUser(HttpServletRequest req, Authentication auth) {
		String byusername=auth.getName();
		boolean critical=false;
		ModelAndView notifyPage=new ModelAndView("notify");
		String fromUser = req.getParameter("forUser");
		String accountTypeFrom=req.getParameter("from");
		String accountTypeTo=req.getParameter("to");
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
			accountDaoImpl.addToTransaction(amount, detail, status, byusername, date, null, critical); 
			accountDaoImpl.doPayment(accountTypeFrom,amount);
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
		//System.out.println(accountTypeFrom + " " + accountTypeTo + " " +amount);
		
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
			accountDaoImpl.addToTransaction(amount, detail, status, username, date, null, critical); 
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
