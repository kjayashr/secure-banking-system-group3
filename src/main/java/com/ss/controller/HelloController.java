package com.ss.controller;
import com.ss.daoImpl.UserDaoImpl;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import com.ss.dao.RegistrationDao;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.ss.daoImpl.AccountDaoImpl;
import com.ss.model.Account;
import com.ss.model.TransactionList;
import com.ss.model.User;
import com.ss.security.CustomAuthenticationSuccessHandler;
import com.ss.security.OTPUtil;

import org.apache.log4j.Logger;
@Controller
public class HelloController {
	
	@Autowired
	AccountDaoImpl accountDoaImpl;
	
	@Autowired
	UserDaoImpl userDaoImpl;
	
	@Autowired
	RegistrationDao	registrationImpl;
	
	@Autowired
	OTPUtil otpUtil;
	
	@RequestMapping(value = "/welcome**" , method = RequestMethod.GET)
	public ModelAndView welcomePage(HttpServletRequest req, HttpServletResponse resp,HttpSession se,Authentication auth) {
		ModelAndView model = new ModelAndView();
		if(req.getUserPrincipal() == null) {
			try {
				req.getRequestDispatcher("/login?expired").forward(req, resp);
				model.setViewName("hello");
				return model;
			} catch (ServletException|IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				model.setViewName("login");
				return model;
			}
		}
		
		String uName = "";
		String role = "";
		if (!(auth instanceof AnonymousAuthenticationToken)) {
			UserDetails userDetail = (UserDetails) auth.getPrincipal();
			model.addObject("username", userDetail.getUsername());
			uName = userDetail.getUsername();
			role = registrationImpl.getRole(uName);
			if (role.equals("ROLE_TIER2")) {
				model.addObject("myrole", "tier2");
			} else if (role.equals("ROLE_TIER1") || role.equals("ROLE_TIER1_APPROVED")) {
				model.addObject("myrole", "tier1");
			} else if(role.equals("ROLE_ADMIN")) {
				model.addObject("myrole", "admin");
			}
			
		}
		String name = req.getUserPrincipal().getName();
		HashMap<String,Account> accountinfo=accountDoaImpl.getAccountInfo(name);
		model.addObject("savings", "Spring Security Hello World");
		model.addObject("message", "This is welcome page!");
		model.addObject("accountData", accountinfo);
		List<String> validAccounts = accountDoaImpl.getValidAccounts(name);
		se.setAttribute("validAccounts",validAccounts);
		model.setViewName("hello");
		return model;
		
	}
	
	@RequestMapping(value = "/admin**", method = RequestMethod.GET)
	public ModelAndView adminPage() {

		ModelAndView model = new ModelAndView();
		model.addObject("title", "Spring Security Hello World");
		model.addObject("message", "This is protected page!");
		model.setViewName("admin");

		return model;

	}
	
	//Spring Security see this :
		@RequestMapping(value = { "/", "/login" }, method = RequestMethod.GET)
		public ModelAndView login(
			@RequestParam(value = "error", required = false) String error,
			@RequestParam(value = "logout", required = false) String logout,
			@RequestParam(value = "expired", required = false) String expired
				) {

			ModelAndView model = new ModelAndView();
			if (error != null) {
				model.addObject("error", "Invalid username and password!");
			}

			if (logout != null) {
				model.addObject("msg", "You've been logged out successfully.");
			}
			if (expired != null) {
				model.addObject("expired", "Your session has expired.");
			}
			model.setViewName("login"); 

			return model;

		}
		
		//for 403 access denied page
		@RequestMapping(value = "/403", method = RequestMethod.GET)
		public ModelAndView accesssDenied() {

		  ModelAndView model = new ModelAndView();

		  //check if user is login
		  Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		  if (!(auth instanceof AnonymousAuthenticationToken)) {
			UserDetails userDetail = (UserDetails) auth.getPrincipal();
			model.addObject("username", userDetail.getUsername());
		  }

		  model.setViewName("403");
		  return model;

		}
		
		@RequestMapping(value="/editProfile",method = RequestMethod.GET)
		public ModelAndView modifyPersonalAccount(HttpServletRequest req,Authentication auth) {
			ModelAndView model = new ModelAndView();
			System.out.println("hello from user controller");
			String uName="";
			if (!(auth instanceof AnonymousAuthenticationToken)) {
				UserDetails userDetail = (UserDetails) auth.getPrincipal();
				model.addObject("username", userDetail.getUsername());
				uName = userDetail.getUsername();
			}
			System.out.println(uName);
			List<User> InternaluserInfo = userDaoImpl.getExternalUserInfo(uName);
			System.out.println("user-size" + InternaluserInfo.size());
			
			model.addObject("userInfo",InternaluserInfo.get(0));
			model.addObject("message", "This is welcome page!");
			model.setViewName("editprofile");			
			
			return model;
		}

	    @RequestMapping(value="/user/changedDetails", method=RequestMethod.GET)
	    public ModelAndView changedDetails(HttpServletRequest req,Authentication auth) {
	    	ModelAndView model = new ModelAndView();
			String uName = "";
			String role = "ROLE_USER";
			if (!(auth instanceof AnonymousAuthenticationToken)) {
				UserDetails userDetail = (UserDetails) auth.getPrincipal();
				model.addObject("username", userDetail.getUsername());
				uName = userDetail.getUsername();
				}
			
			model.addObject("savings", "Spring Security Hello World");
			model.addObject("message", "This is welcome page!");
			
			String email=req.getParameter("email");
			String address=req.getParameter("address");
			String city	=req.getParameter("city");
			String state=req.getParameter("state");
			String country=req.getParameter("country");
			String postcode=req.getParameter("postcode");
			// validation left
			registrationImpl.myNewMethod2(uName,email,address, city, state, country, postcode);
			
			model.setViewName("redirect:/welcome");
			return model;
		}
	    
	    
	    @RequestMapping(value = "/resetpassword", method = RequestMethod.GET)
		public String resetPassword(ModelMap model) {
	    	model.addAttribute("reset_state", "start");
			return "resetpassword";
		}
	    
	    @RequestMapping(value = "/resetpassword", method = RequestMethod.POST)
		public String resetPassword(HttpServletRequest req, HttpServletResponse resp, Authentication auth, ModelMap model) {
			
			if(req.getParameter("password_set") != null && req.getParameter("password_set").equals("DONE")) {
				String username = req.getParameter("username_holder");
				String password = req.getParameter("new_password");
				int ret = registrationImpl.resetPassword(username, password);
				if(ret != 0) {
					model.addAttribute("reset_state", "finish");
				} else {
					model.addAttribute("reset_state", "wrong");
				}
				return "resetpassword";
			} else {
				String username = req.getParameter("username_holder");
				if(req.getParameter("otp_attempts") == null) {
					otpUtil.generateOTP(username);
					model.addAttribute("username_holder", username);
					model.addAttribute("page", "resetpassword");
					model.addAttribute("otp_attempts", 3);
					return "otpPage";
				} else {
					int userOTP = 0;
					int count = -1;
					
					try {
						userOTP = Integer.parseInt(req.getParameter("userOTP"));
						count = Integer.parseInt(req.getParameter("otp_attempts"));
					} catch (NumberFormatException e) {
						
					}
					
					if (!otpUtil.validateOTP(username, userOTP, count)) {
						count--;
						if (count > 0) {
							model.addAttribute("username_holder", username);
							model.addAttribute("page", "resetpassword");
							model.addAttribute("otp_attempts", count);
							return "otpPage";
						} else {
							model.addAttribute("reset_state", "wrongOTP");
							return "resetpassword";
						}
					}
					
					model.addAttribute("username_holder", username);
					model.addAttribute("reset_state", "takePass");
					return "resetpassword";
				}
			}
		}
	    
	    @RequestMapping(value="/prescheck",method=RequestMethod.POST)
		public @ResponseBody String checkUserName(@RequestParam("username") String username,
				@RequestParam("contactno") String contactno){
			System.out.print("Username to check " +username+" | "+contactno);
			String ret = registrationImpl.checkValidEntry(username,contactno);
			return ret;
		}
	
}
