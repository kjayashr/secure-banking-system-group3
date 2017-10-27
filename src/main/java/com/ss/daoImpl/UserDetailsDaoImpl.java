package com.ss.daoImpl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.security.authentication.LockedException;
import org.springframework.stereotype.Repository;

import com.ss.dao.UserDetailsDao;
import com.ss.model.UserAttempts;

@Repository
public class UserDetailsDaoImpl extends JdbcDaoSupport implements UserDetailsDao {
	
	@Autowired
	DataSource dataSource;
	@Autowired
	JdbcTemplate jdbcTemplate;
	
	@PostConstruct
	private void initialize() {
		setDataSource(dataSource);
	}

	
	private static final int MAX_ATTEMPTS = 3;
	
	@Override
	public void updateFailAttempts(String name) {
		UserAttempts user = getUserAttempts(name);
		String sql_user_attempt_insert = "INSERT INTO attempts (username, attempts, lastlogin) VALUES(?,?,?)";
		if (user == null) {
			if (isUser(name)) {
				// if no record, insert a new
				jdbcTemplate.update(sql_user_attempt_insert, new Object[] { name, 1, new Date() });
			}
		} else {

			if (isUser(name)) {
				String sql_user_update_attempt = "UPDATE attempts SET attempts = attempts + 1, lastlogin = ? WHERE username = ?";
				// update attempts count, +1
				jdbcTemplate.update(sql_user_update_attempt, new Object[] { new Date(), name });
			}

			if (user.getAttempts() + 1 >= MAX_ATTEMPTS) {
				// locked user
				String sql_account_nonlocked="UPDATE attempts SET accountNonLocked = ? WHERE username = ?";
				jdbcTemplate.update(sql_account_nonlocked, new Object[] { false, name });
				// throw exception
				throw new LockedException("User Account is locked!");
			}

		}
	}

	@Override
	public UserAttempts getUserAttempts(String name) {
		try {	
			
			String sql_user_attempt = "SELECT username,attempts,lastlogin FROM attempts WHERE username=?";
			UserAttempts userAttempts = jdbcTemplate.queryForObject(sql_user_attempt,new Object[] { name }, new UserAttemptMapper());
			return userAttempts;

		} catch (EmptyResultDataAccessException e) {
			return null;
		}

	}

	@Override
	public void resetFailAttempts(String username) {
		String sql_update_reset="UPDATE attempts SET attempts = 0, lastlogin = now() WHERE username = ?";
		jdbcTemplate.update(sql_update_reset,new Object[] { username });
		
	}
	
	boolean isUser(String username) {

		boolean ret = false;
		String sql_user_count="SELECT count(*) FROM attempts WHERE username = ?";
		int count = jdbcTemplate.queryForObject(sql_user_count, new Object[] { username }, Integer.class);
		if (count > 0) {
			ret = true;
		}

		return ret;
	}

}


class UserAttemptMapper implements RowMapper<UserAttempts>{

	@Override
	public UserAttempts mapRow(ResultSet rs, int arg1) throws SQLException {
		UserAttempts user = new UserAttempts();
		user.setUsername(rs.getString("username"));
		user.setAttempts(rs.getInt("attempts"));
		user.setLastModified(rs.getDate("lastlogin"));
		

		return user;
	}
	
}