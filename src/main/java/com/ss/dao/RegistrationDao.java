package com.ss.dao;

public interface RegistrationDao {
	
	int addNewUser(String username, String password, String firstname,String lastname,
			String dateofbirth, String email, String address, Long contactno, Long ssn, String city, String state, String country, int postcode);

}
