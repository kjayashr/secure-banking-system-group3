package com.ss.daoImpl;

import java.util.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.sql.DataSource;

//import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;

import com.ss.dao.RegistrationDao;
import com.ss.security.GenerateKeyPair;
import com.ss.service.EncryptDecryptUtil;
import com.ss.service.MailService;

@Repository
public class RegistrationDaoImpl implements RegistrationDao {

	@Autowired
	DataSource dataSource;
	@Autowired
	JdbcTemplate jdbcTemplate;

	@Override
	public void myNewMethod(String username, String firstname, String lastname, String address, String city,
			String state, String country, String postcode, String contactno) {

		String sql = "insert into requests (requesterusername,status) values (?,?);";
		GeneratedKeyHolder holder = new GeneratedKeyHolder();
		jdbcTemplate.update(new PreparedStatementCreator() {

			@Override
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				// TODO Auto-generated method stub
				PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
				ps.setString(1, username);
				ps.setString(2, "pending");
				return ps;
			}
		}, holder);
		long pid = (long)holder.getKey();
		System.out.println("[TEST : GENERATED_KEY NEWTEST] " + holder.getKey());
		
		
		if (firstname.length() != 0) {
			String sql1 = "insert into request_changes (requestid, fieldname, fieldvalue) values (?,?,?);";
			GeneratedKeyHolder holder1 = new GeneratedKeyHolder();
			jdbcTemplate.update(new PreparedStatementCreator() {

				@Override
				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
					// TODO Auto-generated method stub
					PreparedStatement ps = con.prepareStatement(sql1, Statement.RETURN_GENERATED_KEYS);
					ps.setString(1, Long.toString(pid));
					ps.setString(2, "firstname");
					ps.setString(3, firstname);
					return ps;
				}
			}, holder1);
	
		}

		if (lastname.length() != 0) {
			String sql2 = "insert into request_changes (requestid, fieldname, fieldvalue) values (?,?,?);";
			GeneratedKeyHolder holder2 = new GeneratedKeyHolder();
			jdbcTemplate.update(new PreparedStatementCreator() {

				@Override
				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
					// TODO Auto-generated method stub
					PreparedStatement ps = con.prepareStatement(sql2, Statement.RETURN_GENERATED_KEYS);
					ps.setString(1, Long.toString(pid));
					ps.setString(2, "lastname");
					ps.setString(3, lastname);
					return ps;
				}
			}, holder2);
			
		}

		if (address.length() != 0) {
			String sql3 = "insert into request_changes (requestid, fieldname, fieldvalue) values (?,?,?);";
			GeneratedKeyHolder holder3 = new GeneratedKeyHolder();
			jdbcTemplate.update(new PreparedStatementCreator() {

				@Override
				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
					// TODO Auto-generated method stub
					PreparedStatement ps = con.prepareStatement(sql3, Statement.RETURN_GENERATED_KEYS);
					ps.setString(1, Long.toString(pid));
					ps.setString(2, "address");
					ps.setString(3, address);
					return ps;
				}
			}, holder3);
			
		}
		
		if (city.length() != 0) {
			String sql4 = "insert into request_changes (requestid, fieldname, fieldvalue) values (?,?,?);";
			GeneratedKeyHolder holder4 = new GeneratedKeyHolder();
			jdbcTemplate.update(new PreparedStatementCreator() {

				@Override
				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
					// TODO Auto-generated method stub
					PreparedStatement ps = con.prepareStatement(sql4, Statement.RETURN_GENERATED_KEYS);
					ps.setString(1, Long.toString(pid));
					ps.setString(2, "city");
					ps.setString(3, city);
					return ps;
				}
			}, holder4);

		}
		
		if (state.length() != 0) {
			String sql5 = "insert into request_changes (requestid, fieldname, fieldvalue) values (?,?,?);";
			GeneratedKeyHolder holder5 = new GeneratedKeyHolder();
			jdbcTemplate.update(new PreparedStatementCreator() {

				@Override
				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
					// TODO Auto-generated method stub
					PreparedStatement ps = con.prepareStatement(sql5, Statement.RETURN_GENERATED_KEYS);
					ps.setString(1, Long.toString(pid));
					ps.setString(2, "state");
					ps.setString(3, state);
					return ps;
				}
			}, holder5);

		}
		
		if (country.length() != 0) {
			String sql6 = "insert into request_changes (requestid, fieldname, fieldvalue) values (?,?,?);";
			GeneratedKeyHolder holder6 = new GeneratedKeyHolder();
			jdbcTemplate.update(new PreparedStatementCreator() {

				@Override
				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
					// TODO Auto-generated method stub
					PreparedStatement ps = con.prepareStatement(sql6, Statement.RETURN_GENERATED_KEYS);
					ps.setString(1, Long.toString(pid));
					ps.setString(2, "country");
					ps.setString(3, country);
					return ps;
				}
			}, holder6);

		}
		
		if (postcode.length() != 0) {
			String sql7 = "insert into request_changes (requestid, fieldname, fieldvalue) values (?,?,?);";
			GeneratedKeyHolder holder7 = new GeneratedKeyHolder();
			jdbcTemplate.update(new PreparedStatementCreator() {

				@Override
				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
					// TODO Auto-generated method stub
					PreparedStatement ps = con.prepareStatement(sql7, Statement.RETURN_GENERATED_KEYS);
					ps.setString(1, Long.toString(pid));
					ps.setString(2, "postcode");
					ps.setString(3, postcode);
					return ps;
				}
			}, holder7);

		}
		
		if (contactno.length() != 0) {
			String sql8 = "insert into request_changes (requestid, fieldname, fieldvalue) values (?,?,?);";
			GeneratedKeyHolder holder8 = new GeneratedKeyHolder();
			jdbcTemplate.update(new PreparedStatementCreator() {

				@Override
				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
					// TODO Auto-generated method stub
					PreparedStatement ps = con.prepareStatement(sql8, Statement.RETURN_GENERATED_KEYS);
					ps.setString(1, Long.toString(pid));
					ps.setString(2, "contactno");
					ps.setString(3, contactno);
					return ps;
				}
			}, holder8);
		
		}

	}

	@Override
	public int addNewUser(String username, String password, String firstname, String lastname, String dateofbirth,
			String email, String address, Long contactno, Long ssn, String city, String state, String country,
			int postcode) {

		String privateKey = "";
		String publicKey = "";
		GenerateKeyPair kp;
		ArrayList<String> errors = new ArrayList<String>();
		try {
			kp = new GenerateKeyPair();
			
			// Encoding the private key before storing in DB 
			privateKey = EncryptDecryptUtil.singleEncryption(kp.getPrivateKey());
			publicKey = kp.getPublicKey();
			MailService.sendKey(email, firstname + " " + lastname, publicKey);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			errors.add("There is some issue with your key generation.");
			errors.add("Your account could not be generated due to key generation issue.");
		}

		if (errors.size() > 0) {
			MailService.sendErrorMail(email, firstname + " " + lastname, errors);
			return 0;
		}

		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
	    password = encoder.encode(password);
		
		String sql = "Insert into users(username,password,firstname,lastname,dob, address,"
				+ "email, contactno, ssn, city, state,country,postcode,key_private) values "
				+ "('" +username+"','"+password+"','"+firstname+"','"+lastname+"','"+dateofbirth+"','"+address+"','"+email+"',"+contactno+","+ssn+",'"+city+"','"+state+"','"+country+"',"+postcode+",'"+privateKey+"');";
		
		int userQ=jdbcTemplate.update(sql);
		
		String role="ROLE_USER";
		String sql2="Insert into user_roles(username,role) values"
					+ "('" +username+"','"+role+"');";
		
		int roleQ=jdbcTemplate.update(sql2);

		MailService.sendKey(email, firstname+" "+lastname, publicKey);
		

		
		return roleQ;
	}

	public String check(String username) {
		String sql = "SELECT username from users where username = '" + username + "';";
		try {
			String ret = jdbcTemplate.queryForObject(sql, String.class);
			return "false";
		} catch (EmptyResultDataAccessException e) {
			return "true";
		}

	}

	public String getRole(String un) {
		String sql = "SELECT role from user_roles where username = '" + un + "';";
		try {
			String ret = jdbcTemplate.queryForObject(sql, String.class);
			return ret;

		} catch (EmptyResultDataAccessException e) {
			return "error";
		}
	}

	public String checkEmail(String email) {
		String sql = "SELECT email from users where email = '" + email + "';";
		try {
			String ret = jdbcTemplate.queryForObject(sql, String.class);
			return "false";
		} catch (EmptyResultDataAccessException e) {
			return "true";
		}
	}

	@Override
	public void rollback(String username) {
		String delete_user="DELETE FROM users where username = '"+username+"'";
		jdbcTemplate.update(delete_user);
		String delete_user_role="DELETE FROM user_roles where username = '"+username+"'";
		jdbcTemplate.update(delete_user_role);
		String delete_accounts="DELETE FROM account where username = '"+username+"'";
		jdbcTemplate.update(delete_accounts);
		String delete_attempts="DELETE FROM attempts where username = '"+username+"'";
		jdbcTemplate.update(delete_attempts);
		
	}

}
