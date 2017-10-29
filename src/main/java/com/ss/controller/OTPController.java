package com.ss.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
	/*
	@Autowired
	OTPUtil otpUtil;

	@RequestMapping(value = "/OTP", method = RequestMethod.GET)
	public String getRegistration(ModelMap model) {
		model.addAttribute("otp_attempts", 3);
		return "otpPage";
	}

	@RequestMapping(value = "/OTP", method = RequestMethod.POST)
	public String handleRegistration(HttpServletRequest req, HttpServletResponse resp, Authentication auth, Model model) {
		int userOTP = Integer.parseInt(req.getParameter("userOTP"));
		String username = req.getUserPrincipal().getName();
		int count = Integer.parseInt(req.getParameter("otp_attempts"));
		if (! otpUtil.validateOTP(username, userOTP,count)) {
			count = Integer.parseInt(req.getParameter("otp_attempts")) - 1;
			System.out.println("[TEST] " + count);
			if (count > 0) {
				model.addAttribute("otp_attempts", count);
				try {
					System.out.println("[TEST -- ]"+req.getMethod()+" | "+req.getRequestURI());
					req.getRequestDispatcher("/welcome").forward(req, resp);
				} catch (ServletException | IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				};
				return "hello";
			} else {
				try {
					req.getRequestDispatcher("/welcome").forward(req, resp);
				} catch (ServletException | IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				};
				return "hello";
			}
		} else {
			try {
				req.getRequestDispatcher("/welcome").forward(req, resp);
			} catch (ServletException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			};
			return "tier1";
		}
	}
	*/
}
