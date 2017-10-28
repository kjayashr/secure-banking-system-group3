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

@Controller
public class RequestController {

	@Autowired
	AccountDaoImpl accountDaoImpl;
	int threshold=1000;
	
	
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
