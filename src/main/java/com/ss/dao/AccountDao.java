package com.ss.dao;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.ss.model.Account;


public interface AccountDao {
	
	//void register(User user);
	//  User validateUser(String username, String password);
	//  void registerUser(String username, String password, String firstname, String lastname, String dateofbirth,String email, String address, String postcode);
	//  List<LoggedData> getLogData(String username);
	
	 HashMap<String, Account> getAccountInfo(String username);
	 void doCreditDebit(String accountType, double amount, String type, String username);
	 void addToTransaction(double amount, String detail, String status, String username, Date date, String object, boolean critical, String approverUSername,String accounttypefrom,String accountTypeto);
	int createAccount(double balance,String username, String type,double interest);
	String createCreditCard(String username,String email);
	List<String> getValidAccounts(String name);
	
	
}
