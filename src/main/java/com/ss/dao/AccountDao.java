package com.ss.dao;

import java.util.HashMap;
import java.util.List;

import com.ss.model.Account;


public interface AccountDao {
	
	//void register(User user);
	//  User validateUser(String username, String password);
	//  void registerUser(String username, String password, String firstname, String lastname, String dateofbirth,String email, String address, String postcode);
	//  List<LoggedData> getLogData(String username);
	
	 HashMap<String, Account> getAccountInfo(int userid);
	 void doCreditDebit(String accountType, double amount, String type);
	
	

}