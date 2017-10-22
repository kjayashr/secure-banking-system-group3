package com.ss.daoImpl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import com.ss.dao.TransactionDao;
import com.ss.model.TransactionList;
import com.ss.model.ApprovalList;;;


public class TransactionDaoImpl implements TransactionDao{

	@Autowired
	DataSource dataSource;
	@Autowired
	JdbcTemplate jdbcTemplate;

	public List<ApprovalList> getApprovalList(String username) {
		String sql="select id, transactiondate, transacterusername, amount from transaction where status='pending' and"
				+ " transferto=?";
		List<ApprovalList> data =jdbcTemplate.query(sql, new Object[]{username}, new approvalListMapper()) ;
		System.out.println(data.size());

		return data.size()>0? data:null;
	}

	public int changestatus(String transactionId, String status) {
		String sql="Update transaction set status='"+status+"' where id="+transactionId;
		int i=jdbcTemplate.update(sql);
		return i;
	}

	public List<TransactionList> viewTransaction(String username) {
		String sql="Select transactiondate, amount, detail from transaction where (transferto='"+username+"' or transacterusername='"+username+"') and (status='approve');";
		List<TransactionList> data =jdbcTemplate.query(sql,new viewTransactionMapper());
		System.out.println(data.size());
		return data.size()>0? data:null;


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


