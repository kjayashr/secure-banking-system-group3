package com.ss.controller;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.ss.dao.AdminDao;
import com.ss.dao.RegistrationDao;
import com.ss.daoImpl.AccountDaoImpl;
import com.ss.daoImpl.UserDaoImpl;
import com.ss.model.Account;
import com.ss.dao.TransactionBO;
import com.ss.daoImpl.AccountDaoImpl;
import com.ss.model.Account;
import com.ss.model.TransactionDO;
import com.ss.model.User;

import java.util.List;

import javax.servlet.http.HttpServletRequest;


@Controller
public class Tier3Controller {
	@Autowired
	RegistrationDao registrationImpl;

	@Autowired
	AccountDaoImpl accountDaoImpl;

	@Autowired
	AdminDao adminDaoImpl;
	
	@Autowired
	TransactionBO transactionBO;
	
    @Autowired
	UserDaoImpl userDaoImpl;

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
		String firstname=req.getParameter("firstname");
		String lastname=req.getParameter("lastname");
		String dateofbirth=req.getParameter("dateofbirth");
		String email=req.getParameter("email");
		String address=req.getParameter("address");
		Long contactno=Long.parseLong(req.getParameter("contactno"));
		Long ssn=Long.parseLong(req.getParameter("ssn"));
		String city=req.getParameter("city");
		String state=req.getParameter("state");
		String country=req.getParameter("country");
		int postcode=Integer.parseInt(req.getParameter("postcode"));
		String type=req.getParameter("accountType");
		String role = req.getParameter("UserType");
		int balance=Integer.parseInt(req.getParameter("balance"));
		int interest=Integer.parseInt(req.getParameter("balance"));
		// validation left
		int i=registrationImpl.addNewUser(username,password,firstname,lastname, dateofbirth, email, address,contactno, ssn, city, state, country, postcode, role);
		int accountCrRet=accountDaoImpl.createAccount(balance,username,type,interest);
		return "Admin_Homepage";
	}
  
    
    @RequestMapping(value="/admin/searchprofile", method=RequestMethod.GET)
	public ModelAndView SearchInternalUserAccount() {
		ModelAndView model = new ModelAndView();
		model.addObject("pagename","Search Internal-Users profile");
		model.setViewName("searchprofile");
		return model;  
	}

	@RequestMapping(value="/checkinternalusername*",method=RequestMethod.POST)
	public @ResponseBody String CheckInternalUsername(@RequestParam("username") String username){
		System.out.print("Username to check " +username);
    	List<User> InternaluserInfo = userDaoImpl.getInternalUserInfo(username);
		System.out.println("user-size" + InternaluserInfo.size());
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
    	if(operation.equals("update")) {
    		System.out.println("Updating");
    		int s = userDaoImpl.ProcessInternalUserProfileUpdate(req,username);
    	}

    	if(operation.equals("delete")) {
    		System.out.println("Deleting");
    		int s = userDaoImpl.ProcessInternalUserProfileDelete(username);
    	}

    	model.setViewName("searchprofile");
    	return model;   
	    
	}
    
    @RequestMapping(value="/admin/viewlogs", method=RequestMethod.GET)
	public ModelAndView ViewExistingLogs() {
		ModelAndView model = new ModelAndView();
		model.addObject("message", "Existing Logs");
		
		List<String> logFiles=adminDaoImpl.getExistingLogFilesPaths();
		
		model.addObject("existinglogfiles",logFiles);

		model.setViewName("viewlogs");
		return model;  
	}

    @RequestMapping(value="*/downloadlog/{filename}", method=RequestMethod.GET)
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
    
    
}