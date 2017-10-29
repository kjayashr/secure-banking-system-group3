package com.ss.daoImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import com.ss.dao.TransactionBO;

import java.util.Date;
import java.util.List;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.ss.model.TransactionDO;

public class TransactionBOImpl implements TransactionBO {
private static final String USER_ROLE_TIER1 = "ROLE_TIER1";
private static final String USER_ROLE_TIER2 = "ROLE_TIER2";
    @Autowired
    JdbcTemplate jdbcTemplate;
	
    @Override
    public List<TransactionDO> getUnapprovedTransactionInfo(String userRole) {
    	String sql = "";
    	if (USER_ROLE_TIER1.equals(userRole) || USER_ROLE_TIER2.equals(userRole)) {
    	    sql="select * from transaction where approverUserName is null";
    	}
		List<TransactionDO> transactions =jdbcTemplate.query(sql, new TransactionInfoMapper()) ;
		return transactions;	
    }
    
    @Override
    public boolean approveTransaction(int transactionId, String approverUserId, String userLevel) {
    	String sql = "update transaction set approverUserName=? where id=?";
    	boolean success = false;
    	try {
    		jdbcTemplate.update(sql, new Object[] {approverUserId, transactionId});
    	} catch (DataAccessException e) {
    		
    	}
		success = true;
    	return success;
    }
    
    @Override
    public void declineTransaction(int transactionId) {
    	
    }
    
    @Override
    public void insertTransaction(double amount, String detail, String status, String username, Date date, String to, boolean critical, String fromAccountType, String toAccountType) {
    	// TODO Auto-generated method stub
    	String sql="Insert into transaction(amount,detail,status,transacterusername,transactiondate, transferto,critical, fromAccountType, toAccountType) values "
    			+ "(" +amount+",'"+detail+"','"+status+"','"+username+"','"+date+"','"+to+"',"+critical +","+"'"+ fromAccountType + "','" + toAccountType+"'" +");";
    	
    	jdbcTemplate.execute(sql);
    	
    }
    
    @Override
    public List<TransactionDO> getUnapprovedNonCriticalTransactions(String userRole) {
    	String sql = "";
    	sql="select * from transaction where ((approverUserName is null or approverUserName='') and critical=false and transacterusername is not null) and (status ='pending')";
    	List<TransactionDO> transactions =jdbcTemplate.query(sql, new TransactionInfoMapper()) ;
		return transactions;	
    }
    public List<TransactionDO> getUnapprovedCriticalTransactions(String userRole) {
    	String sql = "";
    	sql="select * from transaction where ((approverUserName is null or approverUserName='') and critical=true and transacterusername is not null) and ((status ='accepted' and transferto is not null) or (status='pending' and transferto='') or (status='pending and transferto is null'))";
    	List<TransactionDO> transactions =jdbcTemplate.query(sql, new TransactionInfoMapper()) ;
		return transactions;	
    }
    
    @Override
    public TransactionDO getTransactionFromId(int transactionId) {
    	String sql = "";
    	sql="select * from transaction where id=?";
    	List<TransactionDO> transactions = jdbcTemplate.query(sql, new Object[] {transactionId}, new TransactionInfoMapper());
    	if (transactions.size()>0) {
    		return transactions.get(0);
    	} else {
    		return null;
    	}
    }
    
    class TransactionInfoMapper implements RowMapper<TransactionDO> {
  	  public TransactionDO mapRow(ResultSet rs, int arg1) throws SQLException {
  		TransactionDO transactionData = new TransactionDO();
  	    transactionData.setAmount(rs.getDouble("amount"));
  	    transactionData.setTransactorUserName(rs.getString("transacterusername"));
  	    transactionData.setTargetUserName(rs.getString("transferto"));
  	    transactionData.setTransactionId(Integer.parseInt(rs.getString("id")));
  	    transactionData.setFromAccountType(rs.getString("fromAccountType"));
  	    transactionData.setToAccountType(rs.getString("toAccountType"));
  	    return transactionData;
  	  }
  	  
  	  
  }
    
    @Override
    public boolean approveTransaction(int transactionId, String approverUserName) {
    	String sql = "update transaction set approverUserName=?, status='approved' where id=?";
    	boolean success = false;
    	try {
    		jdbcTemplate.update(sql, new Object[] {approverUserName, transactionId});
    		success = true;
    	} catch (DataAccessException e) {
    		System.out.println("encountered Data Access Exception");
    		throw e;
    	}
    	return success;
    }
    
    @Override
    public boolean declineTransaction(int transactionId, String rejecterUserName) {
    	String sql = "update transaction set approverUserName=?, status='rejected' where id=?";
    	boolean success = false;
    	try {
    		jdbcTemplate.update(sql, new Object[] {rejecterUserName, transactionId});
        	success = true;
    	} catch(DataAccessException e) {
    		System.out.println("encountered Data Access Exception");
    		throw e;
    	}
    	return success;
    }
    
}