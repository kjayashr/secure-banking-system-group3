package com.ss.daoImpl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;
import com.ss.dao.UserAttemptDao;

@Repository
public class UserAttemptDaoImpl implements UserAttemptDao {

	@Autowired
	DataSource dataSource;
	@Autowired
	JdbcTemplate jdbcTemplate;
	
	@Override
	public int insertUser(String username, String password, int i, int numOfAttempt, Date date, boolean accountNonLocked, boolean credentialsNonExpired,
			boolean accountNonExpired) {
		String sql_attempt_insert = "INSERT INTO attempts (username,password,enabled,attempts,lastlogin,accountNonLocked,credentialsNonExpired,accountNonExpired) "
									+ "VALUES(?,?,?,?,now(),?,?,?)";
		return jdbcTemplate.update(sql_attempt_insert, new Object[] {username,password,i,numOfAttempt,accountNonLocked,credentialsNonExpired,accountNonExpired});
	}
	
	@Override
	public List<String> getBlockedUser() {
		String sql = "select username from attempts where accountNonLocked=0 OR credentialsNonExpired=0 OR accountNonExpired=0";
		List<String> blockedUsers = jdbcTemplate.query(sql, new RowMapper<String>() {

			@Override
			public String mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rs.getString("username");
			}
			
		});
		return blockedUsers;
	}
	
	@Override
	public boolean unblockedUser(String username) {
		System.out.println("update query");
		String sql = "update attempts set accountNonLocked=1, credentialsNonExpired=1, accountNonExpired=1, attempts=0 where username=?";
		int ret = jdbcTemplate.update(sql, new Object[] {username});
		if(ret==0) {
			return false;
		} else {
			return true;
		}
	}

}
