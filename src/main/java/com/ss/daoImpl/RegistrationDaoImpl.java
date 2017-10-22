package com.ss.daoImpl;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;

import com.ss.dao.RegistrationDao;

@Repository
public class RegistrationDaoImpl implements RegistrationDao {

	@Autowired
	DataSource dataSource;
	@Autowired
	JdbcTemplate jdbcTemplate;
	
	@Override
	public int addNewUser(String username, String password, String firstname,String lastname,
			String dateofbirth, String email, String address, Long contactno, Long ssn, String city, String state, String country, int postcode) {
		BCryptPasswordEncoder encoder=new BCryptPasswordEncoder();
		password=encoder.encode(password);
		String sql="Insert into users(username,password,firstname,lastname,dob, address,"
				+ "email, contactno, ssn, city, state,country,postcode) values "
				+ "('" +username+"','"+password+"','"+firstname+"','"+lastname+"','"+dateofbirth+"','"+address+"','"+email+"',"+contactno+","+ssn+",'"+city+"','"+state+"','"+country+"',"+postcode+");";
		
		int userQ=jdbcTemplate.update(sql);
		
		String role="ROLE_USER";
		String sql2="Insert into user_roles(username,role) values"
					+ "('" +username+"','"+role+"');";
		
		int roleQ=jdbcTemplate.update(sql2);
		
		return roleQ;	

	}

	public String check(String username) {
		String sql="SELECT username from users where username = '"+username+"';";
		try {
			String ret = jdbcTemplate.queryForObject(sql, String.class);
			return "false";
		}catch(EmptyResultDataAccessException e) {
			return "true";
		}
		
	}

	public String checkEmail(String email) {
		String sql="SELECT email from users where email = '"+email+"';";
		try {
			String ret = jdbcTemplate.queryForObject(sql, String.class);
			return "false";
		}catch(EmptyResultDataAccessException e) {
			return "true";
		}
	}

}
