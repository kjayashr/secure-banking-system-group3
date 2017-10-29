package com.ss.daoImpl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import com.ss.dao.AccountDao;
import com.ss.model.Account;
import com.ss.service.CreditCardNumberGenerator;
import com.ss.service.MailService;
import com.sun.istack.internal.logging.Logger;;

public class AccountDaoImpl implements AccountDao {

	@Autowired
	DataSource dataSource;
	@Autowired
	JdbcTemplate jdbcTemplate;

	@Override
	public int createAccount(int balance,String username, String type,int interest) {
		String sql="Insert into account values("+balance+",'"+type+"','"+username+"',"+interest+");";
		int ret=jdbcTemplate.update(sql);
		return ret;
	}
	
	@Override
	public HashMap<String, Account> getAccountInfo(String username) {
		String sql="select accountType,SUM(balance) as balance from account where username= ? group by accountType";
		List<Account> data =jdbcTemplate.query(sql, new Object[]{username}, new accountinfoMapper()) ;
		HashMap<String,Account> retMap=new HashMap<String,Account>();
		for(Account a:data){
			if(a.getAccountType().equalsIgnoreCase("Saving")){
				retMap.put("Saving", a);
			}else if(a.getAccountType().equalsIgnoreCase("Checking")){
				retMap.put("Checking", a);
			}else{
				retMap.put("Credit", a);
			}
		}
		System.out.println("::::::::----------------"+retMap);
		for (Map.Entry entry : retMap.entrySet()) {
		    System.out.println(entry.getKey() + ", " + entry.getValue());
		}
		return retMap;

	}
	
	@Override
	public String createCreditCard(String username,String email)
	{
		CreditCardNumberGenerator cGen = new CreditCardNumberGenerator();
		String bin = "5422";
		int length = 16;
		String cardNumber = cGen.generate(bin, length);
		System.out.println(cardNumber);
		String cvv = cGen.generate("9",3);
		double creditlimit = 10000;
		double currentBalance = 10000;
		double currentdue = 0;
		
		String sql="Insert into creditcard values("+cardNumber+","+cvv+",'"+username+"',"+currentBalance+","+currentdue+","+creditlimit+");";
		//String sql="Insert into account values("+balance+",'"+type+"','"+username+"',"+interest+");";
		int ret=jdbcTemplate.update(sql);
		System.out.println("After creating card"+ret);
		String carddetails = cardNumber+" and your CVV is : "+cvv;
		MailService.carddetails(email, username, carddetails);
		
		return carddetails;
	}

	public void doCreditDebit(String accountType, double amount, String type, String username) {
		
		// check for threshold and user
		
		if(type.equalsIgnoreCase("credit")){
			
			String sql="Update account set balance= balance + "+ amount + " where accountType= '"+ accountType + "';";
			jdbcTemplate.execute(sql);
		}
		else{
			String sql="Update account set balance= balance - "+ amount + " where accountType= '"+ accountType+ "';";
			jdbcTemplate.execute(sql);

		}
	}
	
public void doTransfer(String from, String to, double amount) {
		
		// check for threshold and user
	if(from.equalsIgnoreCase(to)!= true){
		String sql="Update account set balance= balance + "+ amount + " where accountType= '"+ to + "';";
		jdbcTemplate.execute(sql);
		String sql1="Update account set balance= balance - "+ amount + " where accountType= '"+ from + "';";
		jdbcTemplate.execute(sql1);
	}
	
		
	}
    public void doTransfer(String fromUserName, String fromAccountType, String toUserName, String toAccountType, double amount) {
    	String sql = "update account set balance = balance - " + amount + " where username='" + fromUserName + "' and accountType ='"+fromAccountType+"';";
    	jdbcTemplate.execute(sql);
    	String sql1 = "update account set balance = balance + " + amount + " where username='" + toUserName + "' and accountType ='"+toAccountType+"';";
    	jdbcTemplate.execute(sql1);
    }
public void doPayment(String accountTypeFrom, double amount) {
	// TODO Auto-generated method stub
	
		String sql1="Update account set balance= balance - "+ amount + " where accountType= '"+ accountTypeFrom + "';";
		jdbcTemplate.execute(sql1);
}


public void MPayment(String cardno,String cvv,double amount,String usernameofuser, String username,String accountTypeTo) {
	
	String sqlmc="Update creditcard set current_balance = current_balance- "+amount+"where cardnumber='"+cardno+"'AND cvv='"+cvv+"';";
	String sqlcurrdue="Update creditcard set current_due=creditlimit-current_balance where cardnumber='"+cardno+"';";
	String sqlacc="Update account set balance=balance-"+amount+"where username='"+usernameofuser+"'AND accountType='Credit Card';";
	String sqlMacc="Update account set balance=balance-"+amount+"where username='"+username+"'AND accountType='"+accountTypeTo+"';";
	jdbcTemplate.execute(sqlmc);
	jdbcTemplate.execute(sqlacc);
	jdbcTemplate.execute(sqlcurrdue);
	jdbcTemplate.execute(sqlMacc);
}

public boolean checkAmount(String accountType,double amount, String username){
	String sql="Select balance from account where accountType='"+ accountType+"' AND username='"+ username+"';";
	System.out.println(sql);
	Integer ret= jdbcTemplate.queryForObject(sql, Integer.class);
	System.out.println(ret);
	return ret - amount > 0;
}


public boolean checkCAmount(String cardno,String cvv,double amount){
	String sql="Select current_balance from creditcard where cardnumber='"+ cardno+"' AND cvv='"+ cvv+"';";
	Integer ret= jdbcTemplate.queryForObject(sql, Integer.class);
	String limit="Select creditlimit from creditcard where cardnumber='"+cardno+"';";
	
	Double credit = jdbcTemplate.queryForObject(limit, Double.class);
	System.out.println(ret);
	System.out.println(credit);
	if (((ret-amount)>0)&&((credit-amount)>0))
		return true;
	else
		return false;
}

public boolean checkDet(String accountFrom,String cardno) {
	String a="Select count(*) from creditcard where username='"+accountFrom+"' AND cardnumber='" + cardno +"';";
	Integer count=jdbcTemplate.queryForObject(a, Integer.class);
	if(count > 0)
		return true;
	else
		return false;
}
	
	
	
	


public String getusername(String email){
	
	String sql1="select username from users where email='"+email+"';";
	String ret= jdbcTemplate.queryForObject(sql1,String.class);
	
	return ret;
}

public void addToTransaction(double amount, String detail, String status, String username, Date date, String to, boolean critical) {
	// TODO Auto-generated method stub
	String sql="Insert into transaction(amount,detail,status,transacterusername,transactiondate, transferto,critical) values "
			+ "(" +amount+",'"+detail+"','"+status+"','"+username+"','"+date+"','"+to+"',"+critical+");";
	
	jdbcTemplate.execute(sql);
	
}

@Override
public List<String> getValidAccounts(String name) {
	String sql="select distinct accountType from account where username= ?";
	List<Account> data =jdbcTemplate.query(sql, new Object[] {name}, new accountMapper()) ;
	List<String> ret=new ArrayList<String>();
	for(Account a:data) {
		ret.add(a.getAccountType());
	}
	return ret;
}

public String getusernameMerchant(String cardno) {
	
	String sql1="select username from creditcard where cardnumber='"+cardno+"';";
	String ret= jdbcTemplate.queryForObject(sql1,String.class);
	
	return null;
}


}




class accountMapper implements RowMapper<Account> {
	public Account mapRow(ResultSet rs,int arg1) throws SQLException {
		Account accountdata = new Account();
	    accountdata.setAccountType(rs.getString("accountType"));
	    return accountdata;
	}
}

class accountinfoMapper implements RowMapper<Account> {
	  public Account mapRow(ResultSet rs, int arg1) throws SQLException {
	    Account accountdata = new Account();
	    accountdata.setBalance(rs.getInt("balance"));
	    accountdata.setAccountType(rs.getString("accountType"));
	    //accountdata.setInterestRate(rs.getString("interestRate"));
	    return accountdata;
	  }
	  
	  
}

class BalanceMapper implements RowMapper<Double>{

	@Override
	public Double mapRow(ResultSet rs, int arg1) throws SQLException {
		System.out.println(rs.getDouble("balance"));
		return (Double)rs.getDouble("balance");
		// TODO Auto-generated method stub
		
	}
	
}