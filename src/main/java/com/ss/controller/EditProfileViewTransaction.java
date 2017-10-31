package com.ss.controller;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.ss.daoImpl.TransactionDaoImpl;
import com.ss.model.TransactionList;
import com.ss.security.CustomAuthenticationSuccessHandler;
import com.ss.security.OTPUtil;
import com.ss.service.CreatePDF;

@Controller
public class EditProfileViewTransaction {

	@Autowired
	TransactionDaoImpl transactionDaoImpl;
	@Autowired
	OTPUtil otpUtil;

//	@RequestMapping(value = "/editprofile", method = RequestMethod.GET)
//	public String getEditProfile() {
//		return "editprofile";
//	}
	
	
	@RequestMapping(value = "/Merchant/Merchanteditprofile", method = RequestMethod.GET)
	public String MerchantgetEditProfile() {
		
		return "Merchanteditprofile";
	}
	

	@RequestMapping(value = "/viewtransaction", method = RequestMethod.GET)
	public String viewtransactionOTP(HttpServletRequest req, Authentication auth, ModelMap model) {
		String username = auth.getName();
		otpUtil.generateOTP(username);
		model.addAttribute("page", "viewtransaction");
		model.addAttribute("otp_attempts", 3);
		return "otpPage";
	}

	@RequestMapping(value = "/Merchantviewtransaction", method = RequestMethod.GET)
	public String MerchantviewtransactionOTP(HttpServletRequest req, Authentication auth, ModelMap model) {
		String username = auth.getName();
		System.out.println(username+"This is in ");
		otpUtil.generateOTP(username);
		model.addAttribute("page", "Merchantviewtransaction");
		model.addAttribute("otp_attempts", 3);
		return "MerchantotpPage";
	}
	
	@RequestMapping(value = "/viewtransaction", method = RequestMethod.POST)
	public ModelAndView getviewtransaction(HttpServletRequest req, HttpServletResponse resp, Authentication auth, ModelMap model) {
		String username = auth.getName();
		int userOTP = Integer.parseInt(req.getParameter("userOTP"));
		int count = Integer.parseInt(req.getParameter("otp_attempts"));
		if (!otpUtil.validateOTP(username, userOTP, count)) {
			count--;
			if (count > 0) {
				model.addAttribute("page", "viewtransaction");
				model.addAttribute("otp_attempts", count);
				return new ModelAndView("otpPage");
			} else {
				try {
					new CustomAuthenticationSuccessHandler().onAuthenticationSuccess(req, resp, auth);
				} catch (IOException | ServletException e) {
					
				}
			}
		}

		List<TransactionList> data = transactionDaoImpl.viewTransaction(username);
		if (data == null || data.isEmpty()) {
			model.addAttribute("emptyMsg", "No Transaction available to view");
		}
		model.addAttribute("list", data);
		model.addAttribute("column1", "Date");
		model.addAttribute("column2", "Details");
		model.addAttribute("column3", "Amount");
		model.addAttribute("title", "Transaction List");
		// return "transactionpage";
		return new ModelAndView("transactionpage", "list", data);
	}
	
	
	
	@RequestMapping(value = "/Merchantviewtransaction", method = RequestMethod.POST)
	public ModelAndView Merchantgetviewtransaction(HttpServletRequest req, HttpServletResponse resp, Authentication auth, ModelMap model) {
		String username = auth.getName();
		
		int userOTP = Integer.parseInt(req.getParameter("userOTP"));
		int count = Integer.parseInt(req.getParameter("otp_attempts"));
		if (!otpUtil.validateOTP(username, userOTP, count)) {
			count--;
			if (count > 0) {
				model.addAttribute("page", "Merchantviewtransaction");
				model.addAttribute("otp_attempts", count);
				return new ModelAndView("MerchantotpPage");
			} else {
				try {
					new CustomAuthenticationSuccessHandler().onAuthenticationSuccess(req, resp, auth);
				} catch (IOException | ServletException e) {
					
				}
			}
		}

		List<TransactionList> data = transactionDaoImpl.viewTransaction(username);
		if (data == null || data.isEmpty()) {
			model.addAttribute("emptyMsg", "No Transaction available to view");
		}
		model.addAttribute("list", data);
		model.addAttribute("column1", "Date");
		model.addAttribute("column2", "Details");
		model.addAttribute("column3", "Amount");
		model.addAttribute("title", "Transaction List");
		// return "transactionpage";
		return new ModelAndView("Merchanttransactionpage", "list", data);
	}
	
	

	@RequestMapping(value = "/downloadPDF")
	public void downloadPDF(Authentication auth, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		String username = auth.getName();
		List<TransactionList> data = transactionDaoImpl.viewTransaction(username);
		if (data == null) {
			return;
		}
		final ServletContext servletContext = request.getSession().getServletContext();
		final File tempDirectory = (File) servletContext.getAttribute("javax.servlet.context.tempdir");
		final String temperotyFilePath = tempDirectory.getAbsolutePath();

		String fileName = "BankStatement_" + username + ".pdf";
		response.setContentType("application/pdf");
		response.setHeader("Content-disposition", "attachment; filename=" + fileName);

		try {

			CreatePDF.createPDF(temperotyFilePath + "\\" + fileName, data);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			baos = convertPDFToByteArrayOutputStream(temperotyFilePath + "\\" + fileName);
			OutputStream os = response.getOutputStream();
			baos.writeTo(os);
			os.flush();
		} catch (Exception e1) {
			e1.printStackTrace();
		}

	}

	private ByteArrayOutputStream convertPDFToByteArrayOutputStream(String fileName) {

		InputStream inputStream = null;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {

			inputStream = new FileInputStream(fileName);
			byte[] buffer = new byte[1024];
			baos = new ByteArrayOutputStream();

			int bytesRead;
			while ((bytesRead = inputStream.read(buffer)) != -1) {
				baos.write(buffer, 0, bytesRead);
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return baos;
	}

}
