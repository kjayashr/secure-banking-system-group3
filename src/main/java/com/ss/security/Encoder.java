package com.ss.security;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class Encoder {
	
	BCryptPasswordEncoder encoder=new BCryptPasswordEncoder();

	public String encode(String str){
		
		//BCryptPasswordEncoder encoder=new BCryptPasswordEncoder();
		return encoder.encode(str);
	}
		
	public boolean matches(String raw, String encodedString){
		return encoder.matches(raw, encodedString);
	}
	

}
