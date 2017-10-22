package com.ss.daoImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import com.ss.dao.TransactionBO;
import java.util.List;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.ss.model.TransactionDO;

public class TransactionBOImpl implements TransactionBO {
private static final String USER_ROLE_TIER1 = "TIER1";

	@Autowired
	JdbcTemplate jdbcTemplate;
	
    @Override
    public List<TransactionDO> getUnapprovedNonCriticalTransactions(String userRole) {
    	String sql = "";
    	if (USER_ROLE_TIER1.equals(userRole)) {
    	    sql="select * from transaction where (approverUserName is null and critical=false and transacterusername is not null) and ((status ='accepted' and transferto is not null) or (status='pending' and transferto='') or (status='pending and transferto is null'))";
    	}
		List<TransactionDO> transactions =jdbcTemplate.query(sql, new TransactionInfoMapper()) ;
		return transactions;	
    }
    
    class TransactionInfoMapper implements RowMapper<TransactionDO> {
  	  public TransactionDO mapRow(ResultSet rs, int arg1) throws SQLException {
  		TransactionDO transactionData = new TransactionDO();
  	    transactionData.setAmount(rs.getDouble("amount"));
  	    transactionData.setTransactorUserName(rs.getString("transacterusername"));
  	    transactionData.setTargetUserName(rs.getString("transferto"));
  	    transactionData.setTransactionId(Integer.parseInt(rs.getString("id")));
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