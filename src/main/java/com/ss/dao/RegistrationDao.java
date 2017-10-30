package com.ss.dao;

import org.springframework.security.core.Authentication;

public interface RegistrationDao {
	
	int addNewUser(String username, String password, String firstname,String lastname,
			String dateofbirth, String email, String address, Long contactno, Long ssn, String city, String state, String country, int postcode,String role);
	String check(String username);
	String checkEmail(String email);

	void rollback(String username);

	String getRole(String un);
	void myNewMethod(String username, String firstname, String lastname, String address, String city, String state,
			String country, String postcode, String contactno);
	void myNewMethod2(String uName,String email,String address,String city, String state, String country, String postcode);
}
