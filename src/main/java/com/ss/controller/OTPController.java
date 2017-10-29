package com.ss.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.ss.security.OTPUtil;

@Controller
public class OTPController {

	@Autowired
	OTPUtil otpUtil;

	@RequestMapping(value = "/OTP", method = RequestMethod.GET)
	public String getRegistration(ModelMap model) {
		model.addAttribute("otp_attempts", 3);
		return "otpPage";
	}

	@RequestMapping(value = "/OTP", method = RequestMethod.POST)
	public String handleRegistration(HttpServletRequest req, Authentication auth, Model model) {
		int userOTP = Integer.parseInt(req.getParameter("userOTP"));
		String username = req.getUserPrincipal().getName();
		if (! otpUtil.validateOTP(username, userOTP)) {
			int count = Integer.parseInt(req.getParameter("otp_attempts")) - 1;
			System.out.println("[TEST] " + count);
			if (count > 0) {
				model.addAttribute("otp_attempts", count);
				return "otpPage";
			} else {
				return "welcome";
			}
		} else {
			return "tier1";
		}
	}
}
