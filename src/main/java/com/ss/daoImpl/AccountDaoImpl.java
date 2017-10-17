package com.ss.daoImpl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import com.ss.dao.AccountDao;
import com.ss.model.Account;

public class AccountDaoImpl implements AccountDao {

	@Autowired
	DataSource dataSource;
	@Autowired
	JdbcTemplate jdbcTemplate;

	@Override
	public HashMap<String, Account> getAccountInfo(int userid) {
		String sql="select accountType,SUM(balance) as balance from account where userid= ? group by accountType";
		List<Account> data =jdbcTemplate.query(sql, new Object[]{userid}, new accountinfoMapper()) ;
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
		return retMap;

	}

	public void doCreditDebit(String accountType, double amount, String type) {
		
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

public void doPayment(String accountTypeFrom, double amount) {
	// TODO Auto-generated method stub
	
		String sql1="Update account set balance= balance - "+ amount + " where accountType= '"+ accountTypeFrom + "';";
		jdbcTemplate.execute(sql1);
}

public boolean checkAmount(String accountType,double amount, int userid){
	String sql="Select balance from account where accountType='"+ accountType+"' AND userid=1;";
	Integer ret= jdbcTemplate.queryForObject(sql, Integer.class);
	System.out.println(ret);
	return ret - amount > 0;
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