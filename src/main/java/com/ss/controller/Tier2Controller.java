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
import com.ss.model.User;

import jdk.nashorn.internal.ir.RuntimeNode.Request;

import com.ss.dao.RegistrationDao;
import java.util.List;
import com.ss.daoImpl.UserDaoImpl;
import javax.servlet.http.HttpServletRequest;


@Controller
public class Tier2Controller {
	private static final String USER_ROLE_TIER2 = "ROLE_TIER2";
	private static String PAYMENT_ACCOUNT_TYPE = "payment";
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
	
    @Autowired
	UserDaoImpl userDaoImpl;
	
    @RequestMapping(value="/tier2", method=RequestMethod.GET)
	public ModelAndView hellotier2Page() {

		ModelAndView model = new ModelAndView();
		model.addObject("savings", "Spring Security Hello World");
		model.addObject("message", "This is welcome page!");
		//System.out
		model.setViewName("hellotier2");
		return model;
     
	}
    
    @RequestMapping(value="/tier2/nctransactions",method=RequestMethod.GET)
    public ModelAndView nctransaction(HttpServletRequest req, Authentication auth) {
		List<TransactionDO> transactions = transactionBO.getUnapprovedNonCriticalTransactions(USER_ROLE_TIER2);

		ModelAndView model = new ModelAndView();
		model.addObject("transactions", transactions);
		model.setViewName("t2Transaction");
		return model;

    }
    @RequestMapping(value="/tier2/changedDetails", method=RequestMethod.POST)
    public ModelAndView changedDetails(HttpServletRequest req,Authentication auth) {
    	ModelAndView model = new ModelAndView();
		String uName = "";
		String role = "ROLE_TIER2";
		if (!(auth instanceof AnonymousAuthenticationToken)) {
			UserDetails userDetail = (UserDetails) auth.getPrincipal();
			model.addObject("username", userDetail.getUsername());
			uName = userDetail.getUsername();
			}
		
		model.addObject("savings", "Spring Security Hello World");
		model.addObject("message", "This is welcome page!");
		String firstname=req.getParameter("firstname");
		String lastname=req.getParameter("lastname");
		String contactno=req.getParameter("contactno");
		String address=req.getParameter("address");
		String city	=req.getParameter("city");
		String state=req.getParameter("state");
		String country=req.getParameter("country");
		String postcode=req.getParameter("postcode");
		// validation left
		registrationImpl.myNewMethod(uName,firstname,lastname,address,
			 city, state, country, postcode,contactno);
		
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
    @RequestMapping(value="/tier2/searchExternalUser",method=RequestMethod.GET)
    public ModelAndView searchExternaluser() {
    	ModelAndView model = new ModelAndView();
    	model.addObject("pagename","Search External Users Profile");
    	model.setViewName("t2searchExternalUser");
    	return model;
    }
    @RequestMapping(value="/tier2/modifyExternalUser",method=RequestMethod.POST)
    public ModelAndView ModifyInternalUserAccount(HttpServletRequest req,Authentication auth) {
    	ModelAndView model = new ModelAndView();
		String username=req.getParameter("username");
		
		/// named internal user below but works for external users as well
    	List<User> InternaluserInfo = userDaoImpl.getExternalUserInfo(username);
    	System.out.println(username);
		System.out.println("user-size" + InternaluserInfo.size());
		if(InternaluserInfo.size() > 0 ) {
			System.out.println("userrr" + InternaluserInfo.get(0).getFirstname());
			model.addObject("userInfo",InternaluserInfo.get(0));
			model.addObject("message", "This is welcome page!");
			model.setViewName("t2modifyExternalUser");			
		} else {
			model.setViewName("t2searchExternalUser");			
		}
		return model;  
	}
    
	@RequestMapping(value="/tier2/checkinternalusername*",method=RequestMethod.POST)
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

    @RequestMapping(value=	"/tier2/updateOrDeleteExternalUser", method=RequestMethod.POST)
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

    	model.setViewName("hellotier2");
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
    
	@RequestMapping(value="/tier2/modifyPersonalAccount", method = RequestMethod.GET)
	public ModelAndView modifyPersonalAccount(HttpServletRequest req,Authentication auth) {
		ModelAndView model = new ModelAndView();
		String uName="";
		if (!(auth instanceof AnonymousAuthenticationToken)) {
			UserDetails userDetail = (UserDetails) auth.getPrincipal();
			model.addObject("username", userDetail.getUsername());
			uName = userDetail.getUsername();
		}
		
		List<User> InternaluserInfo = userDaoImpl.getInternalUserInfo(uName);
		System.out.println("user-size" + InternaluserInfo.size());
		
		model.addObject("userInfo",InternaluserInfo.get(0));
		model.addObject("message", "This is welcome page!");
		model.setViewName("t2modifyPersonalAccount");			
		
		return model;
	
	}
	
	
	@RequestMapping(value="/tier2/t1user/grantApproval", method=RequestMethod.POST)
	public @ResponseBody String grantApproval(@RequestParam("userName") String userName) {
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
			
			TransactionDO transaction = transactionBO.getTransactionFromId(transactionId);
			//handling payments
			if (PAYMENT_ACCOUNT_TYPE.equals(transaction.getToAccountType())) {
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
					accountDaoImpl.doCreditDebit(transaction.getFromAccountType(), transaction.getAmount(), "debit", transaction.getTransactorUserName());
				    return "Transaction approval processed successfully!";
				} else {
					return "Transaction failed";
				}
			}
			
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
	@RequestMapping(value= "/tier2/fillTransactionRequest", method = RequestMethod.POST)
    public ModelAndView fillTransactionRequest(HttpServletRequest req) {
		String fromUserName=req.getParameter("fromUser");
		ModelAndView mav = new ModelAndView();
		mav.addObject("customerUser", fromUserName);
		mav.setViewName("tier2paymentRequest");
		return mav;
	}

	
	////////////////////////////////////
	
	
	
    
}
