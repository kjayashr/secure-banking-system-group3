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
    public List<TransactionDO> getUnapprovedTransactionInfo(String userRole) {
    	String sql = "";
    	if (USER_ROLE_TIER1.equals(userRole)) {
    	    sql="select * from transaction where approverUserName is null";
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
    
}