package com.ss.daoImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import com.ss.dao.T1userBO;
import com.ss.daoImpl.TransactionBOImpl.TransactionInfoMapper;

import java.util.List;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.ss.model.T1userDO;
import com.ss.model.TransactionDO;


public class T1userBOImpl implements T1userBO{
	private static final String USER_ROLE_TIER2 = "ROLE_TIER2";
    @Autowired
    JdbcTemplate jdbcTemplate;
    
    @Override
    public List<T1userDO> gett1users(String userrole){
    	String sql = "";
    	System.out.println("Gett1 users function step1");
    	if (USER_ROLE_TIER2.equals(userrole))
    		sql = "select username,user_role_id from user_roles where role='ROLE_TIER1'";
    	System.out.println("Gett1 users function step1");
    	List<T1userDO> t1users = jdbcTemplate.query(sql, new T1userInfoMapper()) ;
    	System.out.println("Gett1 users function step2");
    	
    	return t1users;
    }


class T1userInfoMapper implements RowMapper<T1userDO>{
	  public T1userDO mapRow(ResultSet rs, int arg1) throws SQLException {
		T1userDO t1userData = new T1userDO();
	    t1userData.setUserName(rs.getString("userName"));
	    t1userData.setUserRoleId(rs.getString("userRoleId"));
	    return t1userData;
	  }
}

@Override
public boolean grantApproval(String userName, String userId) {
	String sql = "update user_roles set role='ROLE_TIER1_APPROVED' where username=?";
	boolean success = false;
	try {
		jdbcTemplate.update(sql, new Object[] {userName});
		success = true;
	} catch (DataAccessException e) {
		System.out.println("encountered Data Access Exception");
		throw e;
	}
	return success;
}

}