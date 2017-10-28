package com.ss.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ss.dao.AccountDao;
import com.ss.dao.RegistrationDao;
import com.ss.security.OTPUtil;

@Controller
public class OTPController {

	@Autowired
	OTPUtil otpUtil;

	@RequestMapping(value = "/OTP", method = RequestMethod.GET)
	public String getRegistration() {
		return "otpPage";
	}

	@RequestMapping(value = "/OTP", method = RequestMethod.POST)
	public String handleRegistration(HttpServletRequest req, Authentication auth, Model model) {
		int userOTP = Integer.parseInt(req.getParameter("userOTP"));
		String username = req.getUserPrincipal().getName();
		if (otpUtil.validateOTP(username, userOTP)) {
			return "login";
		}

		return "otpPage";
	}
}
