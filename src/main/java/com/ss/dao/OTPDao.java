package com.ss.dao;

public interface OTPDao {

	boolean create(String username, String OTP);

	boolean delete(String username);

	boolean update(String username, String OTP);

	String getOTP(String username);
}
