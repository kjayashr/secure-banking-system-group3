package com.ss.daoImpl;

import java.util.Date;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
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
									+ "VALUES('"+username+"','"+password+"',"+i+","+numOfAttempt+",now(),"+accountNonLocked+","+credentialsNonExpired+","+accountNonExpired+");";
		return jdbcTemplate.update(sql_attempt_insert);
	}

}
