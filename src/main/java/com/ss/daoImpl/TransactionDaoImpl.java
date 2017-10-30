package com.ss.daoImpl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import com.ss.dao.TransactionDao;
import com.ss.model.TransactionList;


import com.ss.model.ApprovalList;
import com.ss.model.TransactionDetails;;;


public class TransactionDaoImpl implements TransactionDao{

	@Autowired
	DataSource dataSource;
	@Autowired
	JdbcTemplate jdbcTemplate;

	public List<ApprovalList> getApprovalList(String username) {
		try {
		String sql="select id, transactiondate, transacterusername, amount from transaction where status='pending' and"
				+ " transferto=?";
		List<ApprovalList> data =jdbcTemplate.query(sql, new Object[]{username}, new approvalListMapper()) ;
		System.out.println(data.size());
		return data.size()!=0?data:null;
		}catch(EmptyResultDataAccessException erda) {
			return null;
		}
	}

	public int changestatus(String transactionId, String status, String amount) {
		Double sum=Double.parseDouble(amount);
		int i=0;
		if(sum>=1000 && status.equalsIgnoreCase("accepted")){
			// critical
			String sql="Update transaction set status='accepted' where id=?";
			i=jdbcTemplate.update(sql, new Object[] {transactionId});
		}else if (sum<1000 && status.equalsIgnoreCase("accepted")){
			//non critical
			System.out.println("inside change status: non critical and approved");
			String sql="Update transaction set status='approved' where id=?";
			i=jdbcTemplate.update(sql, new Object[] {transactionId});
			
		}else{
			//rejection
			String sql="Update transaction set status='rejected' where id=?";
			i=jdbcTemplate.update(sql, new Object[] {transactionId});
		}
		
		return i;
	}
	
	

	public List<TransactionList> viewTransaction(String username) {
		try {
			String sql="Select transactiondate, amount, detail from transaction where (transferto=? or transacterusername=?) and (status='approved');";
			List<TransactionList> data =jdbcTemplate.query(sql, new Object[] {username, username}, new viewTransactionMapper());
			return data.size()!=0?data:null;
		}catch(EmptyResultDataAccessException erda) {
			return null;
		}

	}

	public List<TransactionDetails> getDetailsforInternalTransfer(String transactionId) {
		// TODO Auto-generated method stub
		String sql="Select * from transaction where id=?";
		List<TransactionDetails> list=jdbcTemplate.query(sql,new Object[] {transactionId}, new TransactionDetailMapper() );
		return list;
		
	}

	public void setApproverUserName(String username, String transactionId) {
		// TODO Auto-generated method stub
		int id=Integer.parseInt(transactionId);
		String sql="Update transaction set approverUserName=? where id=?";
		int i=jdbcTemplate.update(sql, new Object[] {username,id});

		
	}

}

class TransactionDetailMapper implements RowMapper<TransactionDetails> {
	public TransactionDetails mapRow(ResultSet rs, int arg1) throws SQLException {
		TransactionDetails list = new TransactionDetails();
		list.setDate(rs.getString("transactiondate"));
		list.setId(rs.getInt("id"));
		list.setAmount(rs.getDouble("amount"));
		list.setDetail(rs.getString("detail"));
		list.setStatus(rs.getString("status"));
		list.setTransacterUsername(rs.getString("transacterusername"));
		list.setTransferto(rs.getString("transferto"));
		list.setCritical(rs.getBoolean("critical"));
		list.setApproverUsername(rs.getString("approverUserName"));
		list.setFromAccountType(rs.getString("fromAccountType"));
		list.setToAccounttype(rs.getString("toAccountType"));
		return list;
	}
}


class approvalListMapper implements RowMapper<ApprovalList> {
	public ApprovalList mapRow(ResultSet rs, int arg1) throws SQLException {
		ApprovalList list = new ApprovalList();
		list.setDate(rs.getString("transactiondate"));
		list.setSender(rs.getString("transacterusername"));
		list.setAmount(rs.getDouble("amount"));
		list.setId(rs.getInt("id"));
		return list;
	}
}


class viewTransactionMapper implements RowMapper<TransactionList> {
	public TransactionList mapRow(ResultSet rs, int arg1) throws SQLException {
		TransactionList list = new TransactionList();
		list.setTransactiondate(rs.getString("transactiondate"));
		list.setDetail(rs.getString("detail"));
		list.setAmount(rs.getDouble("amount"));
		return list;
	}
}



