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
	public int createAccount(int balance, String username, String type, int interest) {
		String sql = "Insert into account values(?,?,?,?)"; // +balance+",'"+type+"','"+username+"',"+interest+");";
		return jdbcTemplate.update(sql, new Object[] { balance, type, username, interest });
	}

	@Override
	public HashMap<String, Account> getAccountInfo(String username) {
		String sql = "select accountType,SUM(balance) as balance from account where username= ? group by accountType";
		List<Account> data = jdbcTemplate.query(sql, new Object[] { username }, new accountinfoMapper());
		HashMap<String, Account> retMap = new HashMap<String, Account>();
		for (Account a : data) {
			if (a.getAccountType().equalsIgnoreCase("Saving")) {
				retMap.put("Saving", a);
			} else if (a.getAccountType().equalsIgnoreCase("Checking")) {
				retMap.put("Checking", a);
			} else {
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
		String cvv = cGen.generate("9", 3);
		double creditlimit = 10000;
		double currentBalance = 10000;
		double currentdue = 0;

		// String sql = "Insert into creditcard values(" + cardNumber + "," + cvv + ",'"
		// + username + "'," + currentBalance
		// + "," + currentdue + "," + creditlimit + ");";
		// String sql="Insert into account
		// values("+balance+",'"+type+"','"+username+"',"+interest+");";
		String sql = "Insert into creditcard values(?,?,?,?,?,?);";
		int ret = jdbcTemplate.update(sql,
				new Object[] { cardNumber, cvv, username, currentBalance, currentdue, creditlimit });
		System.out.println("After creating card" + ret);
		String carddetails = cardNumber + " and your CVV is : " + cvv;
		MailService.carddetails(email, username, carddetails);

		return carddetails;
	}

	public void doCreditDebit(String accountType, double amount, String type, String username) {

		// check for threshold and user

		if (type.equalsIgnoreCase("credit")) {

			String sql = "Update account set balance = balance + ? where accountType = ? and username=?";
			jdbcTemplate.update(sql, new Object[] { amount, accountType, username});
		} else {
			String sql = "Update account set balance = balance - ? where accountType = ? and username=?";
			jdbcTemplate.update(sql, new Object[] { amount, accountType, username });
		}
	}

	/*public void doTransfer(String from, String to, double amount, String username) {

		// check for threshold and user
		if (from.equalsIgnoreCase(to) != true) {
			String sql1 = "Update account set balance = balance + ? where accountType = ?";
			String sql2 = "Update account set balance = balance - ? where accountType = ? and username=?";
			jdbcTemplate.update(sql1, new Object[] { amount, to , tousername});
			jdbcTemplate.update(sql2, new Object[] { amount, from, username });
		}

	}  */

	public void doTransfer(String fromUserName, String fromAccountType, String toUserName, String toAccountType,
			double amount) {
		// String sql1 = "update account set balance = balance - " + amount + " where
		// username='" + fromUserName + "' and accountType ='"+fromAccountType+"';";
		// String sql2 = "update account set balance = balance + " + amount + " where
		// username='" + toUserName + "' and accountType ='"+toAccountType+"';";
		String sql1 = "update account set balance = balance - ? where username=? and accountType=?";
		String sql2 = "update account set balance = balance + ? where username=? and accountType =?";
		jdbcTemplate.update(sql1, new Object[] { amount, fromUserName, fromAccountType });
		jdbcTemplate.update(sql2, new Object[] { amount, toUserName, toAccountType });
	}

	public void doPayment(String accountTypeFrom, double amount, String username) {
		// TODO Auto-generated method stub

		String sql = "Update account set balance = balance - ? where accountType = ? and username=?";
		jdbcTemplate.update(sql, new Object[] { amount, accountTypeFrom , username});
	}

	public void MPayment(String cardno, String cvv, double amount, String usernameofuser, String username,
			String accountTypeTo) {

		// String sqlmc = "Update creditcard set current_balance = current_balance- " +
		// amount + "where cardnumber='"
		// + cardno + "'AND cvv='" + cvv + "';";
		// String sqlcurrdue = "Update creditcard set
		// current_due=creditlimit-current_balance where cardnumber='" + cardno
		// + "';";
		// String sqlacc = "Update account set balance=balance-" + amount + "where
		// username='" + usernameofuser
		// + "'AND accountType='Credit Card';";
		// String sqlMacc = "Update account set balance=balance-" + amount + "where
		// username='" + username
		// + "'AND accountType='" + accountTypeTo + "';";

		String sqlmc = "Update creditcard set current_balance = current_balance- ? where cardnumber=? AND cvv=?";
		String sqlcurrdue = "Update creditcard set current_due=creditlimit-current_balance where cardnumber=?";
		String sqlacc = "Update account set balance=balance-? where username=? AND accountType='Credit Card';";
		String sqlMacc = "Update account set balance=balance+? where username=? AND accountType=?";
		jdbcTemplate.update(sqlmc, new Object[] { amount, cardno, cvv });
		jdbcTemplate.update(sqlacc, new Object[] { amount, usernameofuser });
		jdbcTemplate.update(sqlcurrdue, new Object[] { cardno });
		jdbcTemplate.update(sqlMacc, new Object[] { amount, username, accountTypeTo });
	}

	public boolean checkAmount(String accountType, double amount, String username) {
		// String sql="Select balance from account where accountType='"+ accountType+"'
		// AND username='"+ username+"';";
		String sql = "Select balance from account where accountType = ? AND username = ?";
		List<Integer> ret = jdbcTemplate.query(sql, new Object[] { accountType, username }, new RowMapper<Integer>() {

			@Override
			public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rs.getInt("balance");
			}

		});

		if (ret.size() == 0) {
			return false;
		} else {
			System.out.println(ret.get(0));
			return ret.get(0) - amount > 0;
		}
	}

	public boolean checkCAmount(String cardno, String cvv, double amount) {
		String sql = "Select current_balance from creditcard where cardnumber=? AND cvv=?";
		List<Integer> retList = jdbcTemplate.query(sql, new Object[] { cardno, cvv }, new RowMapper<Integer>() {

			@Override
			public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rs.getInt("current_balance");
			}

		});
		
		Integer ret = 0;
		if(retList.size() != 0) {
			ret = retList.get(0);
		}

		String limit = "Select creditlimit from creditcard where cardnumber=?";

		List<Double> creditList = jdbcTemplate.query(limit, new Object[] { cardno }, new RowMapper<Double>() {

			@Override
			public Double mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rs.getDouble("creditlimit");
			}

		});

		Double credit = new Double(0);
		if(creditList.size() != 0) {
			credit = creditList.get(0);
		}
		
		System.out.println("[TEST NEW 1] " + ret);
		System.out.println("[TEST NEW 2] " + credit);
		
		if (((ret - amount) > 0) && ((credit - amount) > 0))
			return true;
		else
			return false;
	}

	public boolean checkDet(String accountFrom, String cardno) {
		String a = "Select count(*) from creditcard where username=? AND cardnumber=?";
		List<Integer> countList = jdbcTemplate.query(a, new Object[] { accountFrom, cardno }, new RowMapper<Integer>() {


public void MPayment(String cardno,String cvv,double amount,String usernameofuser, String username,String accountTypeTo) {
	
	String sqlmc="Update creditcard set current_balance = current_balance- "+amount+"where cardnumber='"+cardno+"'AND cvv='"+cvv+"';";
	String sqlcurrdue="Update creditcard set current_due=creditlimit-current_balance where cardnumber='"+cardno+"';";
	String sqlacc="Update account set balance=balance-"+amount+"where username='"+usernameofuser+"'AND accountType='Credit Card';";
	String sqlMacc="Update account set balance=balance+"+amount+"where username='"+username+"'AND accountType='"+accountTypeTo+"';";
	jdbcTemplate.execute(sqlmc);
	jdbcTemplate.execute(sqlacc);
	jdbcTemplate.execute(sqlcurrdue);
	jdbcTemplate.execute(sqlMacc);
}

			@Override
			public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rs.getInt(1);
			}


		});
		Integer count = 0;
		if(countList.size() != 0) {
			count = countList.get(0);
		}
		if (count > 0)
			return true;
		else
			return false;
	}

	public String getusername(String email) {

		String sql = "select username from users where email = ?";
		List<String> ret = jdbcTemplate.query(sql, new Object[] { email }, new RowMapper<String>() {

			@Override
			public String mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rs.getString("username");
			}

		});

		if (ret.size() != 0) {
			return ret.get(0);
		} else {
			return null;
		}
	}

	@Override
	public void addToTransaction(double amount, String detail, String status, String username, Date date, String to,
			boolean critical,String approvalUsername, String accountTypeFrom, String accountTypeTo) {
		// TODO Auto-generated method stub
		System.out.println("dsbkdsdhksvbdjvs");
		String sql = "Insert into transaction(amount, detail, status, transacterusername, "
				+ "transactiondate, transferto, critical, approverUserName, fromAccountType, toAccountType) "
				+ "values (?,?,?,?,?,?,?,?,?,?)";
		Object[] arguments = new Object[] { amount, detail, status, username, date, to, critical, approvalUsername, accountTypeFrom,accountTypeTo};
		jdbcTemplate.update(sql, arguments);

	}

	@Override
	public List<String> getValidAccounts(String name) {
		String sql = "select distinct accountType from account where username= ?";
		List<Account> data = jdbcTemplate.query(sql, new Object[] { name }, new accountMapper());
		List<String> ret = new ArrayList<String>();
		for (Account a : data) {
			ret.add(a.getAccountType());
		}
		return ret;
	}

	public String getusernameMerchant(String cardno) {

		String sql1 = "select username from creditcard where cardnumber=?";
		List<String> ret = jdbcTemplate.query(sql1, new Object[] { cardno }, new RowMapper<String>() {

			@Override
			public String mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rs.getString("username");
			}

		});

		if (ret.size() == 0) {
			return null;
		} else {
			return ret.get(0);
		}
	}

	public void doTransferInternal(String username, double amount, String accountTypeFrom, String accountTypeTo) {
		// TODO Auto-generated method stub
		String sql1 = "update account set balance = balance - ? where username=? and accountType=?";
		String sql2 = "update account set balance = balance + ? where username=? and accountType =?";
		jdbcTemplate.update(sql1, new Object[] { amount, username, accountTypeFrom });
		jdbcTemplate.update(sql2, new Object[] { amount, username, accountTypeTo });
		
	}

	public void doTransferExternal(String username, double amount, String accountTypeFrom, String tousername) {
		// TODO Auto-generated method stub
		System.out.println("amount:"+ amount);
		String sql1 = "update account set balance = balance - ? where username=? and accountType=?";
		String sql2 = "update account set balance = balance + ? where username=? and accountType ='Saving'";
		jdbcTemplate.update(sql1, new Object[] { amount, username, accountTypeFrom });
		jdbcTemplate.update(sql2, new Object[] { amount, tousername});
	}

}

class accountMapper implements RowMapper<Account> {
	public Account mapRow(ResultSet rs, int arg1) throws SQLException {
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
		// accountdata.setInterestRate(rs.getString("interestRate"));
		return accountdata;
	}

}

class BalanceMapper implements RowMapper<Double> {

	@Override
	public Double mapRow(ResultSet rs, int arg1) throws SQLException {
		System.out.println(rs.getDouble("balance"));
		return (Double) rs.getDouble("balance");
		// TODO Auto-generated method stub

	}

}
