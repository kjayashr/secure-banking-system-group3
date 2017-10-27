package com.ss.daoImpl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

//import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.ss.dao.UserDao;
import com.ss.model.User;

@Repository
public class UserDaoImpl implements UserDao {

	@Autowired
	DataSource dataSource;
	@Autowired
	JdbcTemplate jdbcTemplate;

	private List<User> getUserbyString(String sql, Object param) {

		List<User> user = (List<User>) jdbcTemplate.query(sql, new Object[] { param }, new RowMapper<User>() {

			@Override
			public User mapRow(ResultSet rs, int rowNum) throws SQLException {
				System.out.println("User found : " + param);
				User rsUser = new User();
				rsUser.setUsername(rs.getString(1));
				rsUser.setFirstname(rs.getString(2));
				rsUser.setLastname(rs.getString(3));
				rsUser.setDateofbirth(rs.getString(4));
				rsUser.setAddress(rs.getString(5));
				rsUser.setEmail(rs.getString(6));
				rsUser.setContactno(rs.getLong(7));
				rsUser.setSsn(rs.getLong(8));
				rsUser.setCity(rs.getString(9));
				rsUser.setState(rs.getString(10));
				rsUser.setCountry(rs.getString(11));
				rsUser.setPostcode(rs.getInt(12));
				return rsUser;
			}
		});

		return user;
	}

	@Override
	public User getUserbyUsername(String username) {
		String sql = "select username,firstname,lastname,dob, " + "address, email, contactno, ssn, city, state, "
				+ "country,postcode from users where username = ?";

		List<User> user = getUserbyString(sql, username);

		if (user.size() != 0) {
			return user.get(0);
		} else {
			return null;
		}

	}

	@Override
	public User getUserbyEmail(String email) {
		String sql = "select username,firstname,lastname,dob, " + "address, email, contactno, ssn, city, state, "
				+ "country,postcode from users where email= ?";
		
		List<User> user = getUserbyString(sql, email);
		
		if(user.size() != 0) {
			return user.get(0);
		} else {
			return null;
		}
	}
	
	@Override
	public User getUserbyPhone(Long phone) {
		String sql = "select username,firstname,lastname,dob, " + "address, email, contactno, ssn, city, state, "
				+ "country,postcode from users where contactno= ?";
		
		List<User> user = getUserbyString(sql, phone);
		
		if(user.size() != 0) {
			return user.get(0);
		} else {
			return null;
		}
		
	}
	
	

}
