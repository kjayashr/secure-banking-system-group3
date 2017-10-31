package com.ss.controller;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.ss.dao.AccountDao;
import com.ss.dao.AdminDao;
import com.ss.dao.RegistrationDao;
import com.ss.daoImpl.AccountDaoImpl;
import com.ss.daoImpl.UserAttemptDaoImpl;
import com.ss.daoImpl.UserDaoImpl;
import com.ss.model.Account;
import com.ss.dao.TransactionBO;
import com.ss.dao.UserAttemptDao;
import com.ss.daoImpl.AccountDaoImpl;
import com.ss.model.Account;
import com.ss.model.TransactionDO;
import com.ss.model.User;
import com.ss.model.UserRequest;
import com.ss.model.UserRequestDetails;

import java.util.List;

import javax.servlet.http.HttpServletRequest;


@Controller
public class Tier3Controller {
	@Autowired
	RegistrationDao registrationImpl;

	@Autowired
	AccountDaoImpl accountDaoImpl;
	
	@Autowired
	AccountDao accountDao;

	@Autowired
	AdminDao adminDaoImpl;
	
	@Autowired
	TransactionBO transactionBO;
	
    @Autowired
	UserDaoImpl userDaoImpl;
    
    @Autowired
    UserAttemptDao userAttemptDao;
    
    final static Logger logger = Logger.getLogger(Tier3Controller.class);

    @RequestMapping(value="/admin/Welcome", method=RequestMethod.GET)
	public ModelAndView hellotier3Page() {
		ModelAndView model = new ModelAndView();
		model.addObject("message", "This is welcome page!");
		model.setViewName("Admin_Homepage");
		return model;
	}

    
    @RequestMapping(value="/admin/registration", method=RequestMethod.GET)
	public ModelAndView hellotier3CreateAccount() {
		ModelAndView model = new ModelAndView();
		model.addObject("message", "This is welcome page!");
		model.setViewName("internalUserRegistration");
		return model;  
	}
    
	@RequestMapping(value="/admin/addInternalUser",method=RequestMethod.POST)
	public String handleRegistration(HttpServletRequest req,Authentication auth){
		String username=req.getParameter("username");
		String password=req.getParameter("password");
		BCryptPasswordEncoder encoder=new BCryptPasswordEncoder();
		password=encoder.encode(password);
		String firstname=req.getParameter("firstname");
		String lastname=req.getParameter("lastname");
		String dateofbirth=req.getParameter("dateofbirth");
		String email=req.getParameter("email");
		String address=req.getParameter("address");
		Long contactno = 1L;
		try {
		contactno = Long.parseLong(req.getParameter("contactno"));
		} catch (NumberFormatException e) {
			logger.info("faced number format exception due to faulty contactno input");
			return "Admin_Homepage";
		}
		Long ssn=1L;
		try {
			ssn=Long.parseLong(req.getParameter("ssn"));
		} catch (NumberFormatException e) {
			logger.info("faced number format exception due to faulty ssn input");
			return "Admin_Homepage";
		}
		String city=req.getParameter("city");
		String state=req.getParameter("state");
		String country=req.getParameter("country");
		int postcode = 0;
		try {
			postcode=Integer.parseInt(req.getParameter("postcode"));
		} catch (NumberFormatException e) {
			logger.info("faced number format exception due to faulty postcode input");
			return "Admin_Homepage";
		}
		String type=req.getParameter("accountType");
		double balance=0;
		try {
		    balance = Double.parseDouble(req.getParameter("balance"));
		} catch (NumberFormatException e) {
			logger.info("faced number format exception due to faulty balance input");
			return "Admin_Homepage";
		}
		double interest = 0;
		try {
			interest=Integer.parseInt(req.getParameter("interest"));
		} catch (NumberFormatException e) {
			logger.info("faced number format exception due to faulty interest input");
			return "Admin_Homepage";
		}
		String role = req.getParameter("UserType");
		// validation left
		try{
			int i=registrationImpl.addNewUser(username,password,firstname,lastname, dateofbirth, email, address,contactno, ssn, city, state, country, postcode, role);
			int accountCrRet=accountDaoImpl.createAccount(balance,username,type,interest);
			if(type.equalsIgnoreCase("Credit Card"))
			{
				String createCreditCard=accountDao.createCreditCard(username, email);
			}
			int noofAttemps=0;
			int userAttempt=userAttemptDao.insertUser(username, password, 1, noofAttemps, new Date(), true, true, true);
		}catch(Exception e){
			registrationImpl.rollback(username);
			
		}
		
		return "Admin_Homepage";
	}
  
    
    @RequestMapping(value="/admin/searchprofile", method=RequestMethod.GET)
	public ModelAndView SearchInternalUserAccount() {
		ModelAndView model = new ModelAndView();
		model.addObject("pagename","Search Internal-Users profile");
		model.setViewName("searchprofile");
		return model;  
	}

	@RequestMapping(value="/admin/checkinternalusername*",method=RequestMethod.POST)
	public @ResponseBody String CheckInternalUsername(@RequestParam("username") String username){
		System.out.println("checking interal USer");
    	List<User> InternaluserInfo = userDaoImpl.getInternalUserInfo(username);
		if(InternaluserInfo.size() > 0 ) {
			return "true";
		} else {
			return "false";
		}		
	}	
    
    @RequestMapping(value=	"/admin/modifyprofile", method=RequestMethod.POST)
    public ModelAndView ModifyInternalUserAccount(HttpServletRequest req,Authentication auth) {
    	ModelAndView model = new ModelAndView();
		String username=req.getParameter("username");
    	List<User> InternaluserInfo = userDaoImpl.getInternalUserInfo(username);
		System.out.println("user-size" + InternaluserInfo.size());
		if(InternaluserInfo.size() > 0 ) {
			System.out.println("userrr" + InternaluserInfo.get(0).getFirstname());
			model.addObject("userInfo",InternaluserInfo.get(0));
			model.addObject("message", "This is welcome page!");
			model.setViewName("modifyprofile");			
		} else {
			model.setViewName("searchprofile");			
		}
		return model;  
	}

    @RequestMapping(value=	"/admin/updateordeleteprofile", method=RequestMethod.POST)
    public ModelAndView UpdateOrDeleteInternalUserAccount(HttpServletRequest req,Authentication auth) {
    	ModelAndView model = new ModelAndView();
    	String operation=req.getParameter("operation");
		String username=req.getParameter("username");
    	if(operation.equals("Update")) {
    		int s = userDaoImpl.ProcessInternalUserProfileUpdate(req,username);
    	}

    	if(operation.equals("Delete")) {
    		int s = userDaoImpl.ProcessInternalUserProfileDelete(username);
    	}

    	model.setViewName("searchprofile");
    	return model;   	    
	}
    
    @RequestMapping(value="/admin/viewrequests", method=RequestMethod.GET)
	public ModelAndView ShowUserChangeRequests() {
		ModelAndView model = new ModelAndView();
		List<UserRequest> userrequests=userDaoImpl.getUserRequestsInfo();
		model.addObject("userrequests",userrequests);
		model.addObject("pagename","Current User Change Requests");
		model.setViewName("viewrequests");
 		return model;  
	}

    @RequestMapping(value="/admin/showrequestdetails/{id}/{requesterusername}", method=RequestMethod.GET)
    public ModelAndView ShowRequestDetails(@PathVariable("id")int requestid, @PathVariable("requesterusername")String requesterusername) throws IOException {
    	ModelAndView model = new ModelAndView();
		List<UserRequestDetails> userrequestdetails=userDaoImpl.getUserRequestsDetailsInfo(requestid);
    	List<User> existinguserInfo = userDaoImpl.getInternalUserInfo(requesterusername);
    	if(existinguserInfo.size() > 0 ) {
	    	model.addObject("existinguserdetails",existinguserInfo.get(0));
			model.addObject("requestid", requestid);
			model.addObject("userrequestdetails",userrequestdetails);
	    	model.setViewName("showrequestdetails");
		} else {
	    	model.setViewName("viewrequests");			
		}
	    return model;  
    }    

    @RequestMapping(value=	"/admin/processapproveorrejectrequest", method=RequestMethod.POST)
    public ModelAndView ApproveOrRejectUserRequest(HttpServletRequest req,Authentication auth) {
    	ModelAndView model = new ModelAndView();
    	String operation=req.getParameter("operation");
    	String requesterusername=req.getParameter("requesterusername");
    	int requestid=Integer.parseInt(req.getParameter("requestid"));
    	if(operation.equals("Approve")) {
    		System.out.println(auth.getName());
    		int s = userDaoImpl.ProcessApproveUserRequest(req,requestid,requesterusername,auth.getName());
    	}
    	if(operation.equals("Reject")) {
    		int s = userDaoImpl.ProcessRejectUserRequest(requestid, auth.getName());
    	}

    	model.setViewName("Admin_Homepage");
    	return model;   	    
	}
    
    @RequestMapping(value="/admin/viewlogs", method=RequestMethod.GET)
	public ModelAndView ViewExistingLogs() {
		ModelAndView model = new ModelAndView();
		model.addObject("message", "Existing Logs");		
		List<String> logFiles=adminDaoImpl.getExistingLogFilesPaths();
		System.out.println(logFiles);
		model.addObject("existinglogfiles",logFiles);
		model.setViewName("viewlogs");
		return model;  
	}

    @RequestMapping(value="/admin/downloadlog/{filename}", method=RequestMethod.GET)
    public void download(@PathVariable("filename")String filename, HttpServletResponse response) throws IOException {
 //TO-DO Check the user is admin before downloading
    	
    	File file = new File(System.getProperty("catalina.base") + "\\logs\\" + "my-application.log." + filename );
        InputStream is = new FileInputStream(file);
 
        // MIME type of the file
        response.setContentType("application/octet-stream");
        // Response header
        response.setHeader("Content-Disposition", "attachment; filename=\""
                + file.getName() + "\"");
        // Read from the file and write into the response
        OutputStream os = response.getOutputStream();
        byte[] buffer = new byte[1024];
        int len;
        while ((len = is.read(buffer)) != -1) {
            os.write(buffer, 0, len);
        }
        os.flush();
        os.close();
        is.close();
    }    
   
	@RequestMapping(value="/admin/pii", method=RequestMethod.GET)
	public ModelAndView acessPii(HttpServletRequest req,Authentication auth) {
		ModelAndView model = new ModelAndView();
		List<User> userInfo = userDaoImpl.getUserInfo();
		model.addObject("userInfo", userInfo);
		model.setViewName("pii");
		return model;
	}
    
	@RequestMapping(value="/admin/unblockUser", method=RequestMethod.GET)
	public ModelAndView unblockUser(HttpServletRequest req,Authentication auth) {
		System.out.println("In view unblockUser method");
		List<String> blockedUsers = userAttemptDao.getBlockedUser();

		ModelAndView model = new ModelAndView();
		model.addObject("blockedUsers", blockedUsers);
		model.setViewName("unblockUser");
		return model;
	}
	
	@RequestMapping(value= "/userUnblockApproved", method = RequestMethod.POST)
	public @ResponseBody String userUnblockApproved(@RequestParam("username") String username) {
		System.out.println("In Unblok aproval method " + username);
		
		if(username!=null && username.trim().length()!=0) {
			boolean status = userAttemptDao.unblockedUser(username);
			System.out.println("In Unblok aproval method --- " + status);
			if(status) {
				return "Unblocked successfully!";
			} else {
				return "Unblocked failed!";
			}
		} else {
			return "Unblocked failed! No username passed!";
		}
	}
    
}
