package com.ss.security;

import java.security.SecureRandom;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ss.daoImpl.OTPDaoImpl;
import com.ss.daoImpl.UserDaoImpl;
import com.ss.model.User;
import com.ss.service.EncryptDecryptUtil;
import com.ss.service.MailService;

public class OTPUtil {

	@Autowired
	private OTPDaoImpl otpDao;
	@Autowired
	private UserDaoImpl userDao;

	private int OTP = 0;

	private User user = null;

	private void createOTP(String username, boolean isResend) {

		if (user == null) {
			user = userDao.getUserbyUsername(username);
		}

//		System.out.println("OTP TEST : " + username + " | " + user.getUsername() + " | " + user.getEmail());
		String encryptedOTP = "";

		if (!isResend) {
			SecureRandom random = new SecureRandom();
			OTP = 100000 + random.nextInt(99999);
			encryptedOTP = EncryptDecryptUtil.doubleEncryption(Integer.toString(OTP));
		}

		int otpCheck = getOTP(username);
		if (otpCheck == -1) {
			if (otpDao.create(username, encryptedOTP)) {
				MailService.sendOTPMail(user.getEmail(), user.getFirstname() + " " + user.getLastname(), OTP, isResend);
			} else {
				ArrayList<String> error = new ArrayList<String>();
				error.add("OTP is not generated due to issue");
				MailService.sendErrorMail(user.getEmail(), user.getFirstname() + " " + user.getLastname(), error);
			}
		} else {
			encryptedOTP = EncryptDecryptUtil.doubleEncryption(Integer.toString(OTP));
			if (otpDao.update(username, encryptedOTP)) {
				MailService.sendOTPMail(user.getEmail(), user.getFirstname() + " " + user.getLastname(), OTP, isResend);
			} else {
				ArrayList<String> error = new ArrayList<String>();
				error.add("OTP is not generated due to issue");
				MailService.sendErrorMail(user.getEmail(), user.getFirstname() + " " + user.getLastname(), error);
			}
		}

	}

	public void generateOTP(String username) {
		createOTP(username, false);
	}

	public void resendOTP(String username) {
		createOTP(username, true);
	}

	public boolean validateOTP(String username, int userOTP, int tryLeft) {

		int curOTP = getOTP(username);

		if (userOTP == curOTP) {
			otpDao.delete(username);
			return true;
		} else {
			// in last try, if validation fails, remove the OTP
			if (tryLeft == 1) {
				otpDao.delete(username);
			}
			return false;
		}
	}

	public int getOTP(String username) {
		String encryptedOTP = otpDao.getOTP(username);
		int curOTP = -1;

		try {
			curOTP = Integer.parseInt(EncryptDecryptUtil.doubleDecryption(encryptedOTP));
		} catch (NumberFormatException e) {
			curOTP = -1;
		}

		return curOTP;
	}
}
