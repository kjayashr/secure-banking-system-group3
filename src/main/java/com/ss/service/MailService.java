package com.ss.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;;

public class MailService {
	private static final String username = "ssgrp3f17@gmail.com";
	private static final String pass = "SS@Group3";

	private static void sendMail(String toMail, String toName, String subject, String body, String absoluteFileName, String fileName) {
		Properties props = new Properties();
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.socketFactory.port", "465");
		props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.port", "465");

		Session session = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, pass);
			}
		});

		try {
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(username, "Secure-Banking-System"));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toMail));
			message.setSubject(subject);
			String text = "Dear " + toName + ", \n\n\n" + body + "\n\n\nThanks,\nSS Group3\nCSE 545 Fall'17";
			if (absoluteFileName == null || absoluteFileName.isEmpty()) {
				message.setText(text);
			} else {
				BodyPart bodyPart = new MimeBodyPart();
				bodyPart.setText(text);

				Multipart multipart = new MimeMultipart();
				multipart.addBodyPart(bodyPart);

				bodyPart = new MimeBodyPart();
				DataSource source = new FileDataSource(absoluteFileName);
				bodyPart.setDataHandler(new DataHandler(source));
				if (fileName == null || fileName.isEmpty()) {
					bodyPart.setFileName(absoluteFileName);
				} else {
					bodyPart.setFileName(fileName);
				}
				multipart.addBodyPart(bodyPart);

				message.setContent(multipart);
			}
			Transport.send(message);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void sendKey(String toMail, String toName, String key) {
		String body = "Your Key for future use is generated."+
						"\n\nYour Key is :\n\n" + key + 
						"\n\nPlease mark this mail as important. and DON'T delete this. " +
						"This key is needed to complete some Banking activities. " +
						"For safety, you can keep this key stored in some secure place.";
		
		sendMail(toMail, toName, "Bank Key [IMPORTANT]", body, null, null);
	}
	
	
	
	public static void sendOTPMail(String toMail, String toName, int otp, boolean isResend) {
		String body = "Your OTP is ";
		if (isResend) {
			body = body + "Regenerated. ";
		} else {
			body = body + "Generated. ";
		}
		body = body + "The OTP is : [ " + otp + 
				" ]\n\nPlease use this OTP to validate.";
		
		sendMail(toMail, toName, "OTP [IMPORTANT]", body, null, null);
	}
	
	public static void sendStatement(String toMail, String toName, String absoluteFileName) {
		String body = "Your Statement is generated and attached with this mail.";
		String fileName = "BankStatement_"+(new SimpleDateFormat("yyyy-MM-dd_HH-mm").format(new Date()))+".pdf"; 
		
		sendMail(toMail, toName, "Bank Statement", body, absoluteFileName, fileName);
	}
	
	public static void sendErrorMail(String toMail, String toName, ArrayList<String> errors) {
		
		String body = "We couldn't process your request for some reasons.\n\n";
		for(int i=1; i<=errors.size(); i++) {
			body = body + "		" + i + ". " + errors.get(i-1) + "\n"; 
		}
		body = body + "\n\nSome of the issues maybe possible to self-resolve. Please contact the Bank admin to resolve these issues." +
						"\n\nWe sincerely regret the inconvenience caused";
		
		sendMail(toMail, toName, "Service Error [IMPORTANT]", body, null, null);
	}
	
	public static void sendCompleteMail(String toMail, String toName, String subject, String body, String absoluteFileName, String fileName) {
		sendMail(toMail, toName, subject, body, absoluteFileName, fileName);
	}

	public static void carddetails(String toMail, String toName, String carddetails) {
		String body = "Your Credit card details are here. Physical mail is outdated!"+
				" "+ carddetails;

sendMail(toMail, toName, "Credit Card Details [IMPORTANT]", body, null, null);
		
	}
	

}
