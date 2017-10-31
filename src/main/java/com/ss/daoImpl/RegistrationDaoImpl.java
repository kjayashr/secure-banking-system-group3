package com.ss.daoImpl;

import java.util.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

//import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;

import com.ss.dao.RegistrationDao;
import com.ss.model.User;
import com.ss.security.GenerateKeyPair;
import com.ss.service.EncryptDecryptUtil;
import com.ss.service.MailService;

@Repository
public class RegistrationDaoImpl implements RegistrationDao {

	@Autowired
	DataSource dataSource;
	@Autowired
	JdbcTemplate jdbcTemplate;
	
	@Autowired
	UserDaoImpl userDaoImpl;

	@Override
	public void myNewMethod(String username, String firstname, String lastname, String address, String city,
			String state, String country, String postcode, String contactno) {
		User ExistinguserInfo = userDaoImpl.getInternalUserInfo(username).get(0);
		
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
		System.out.println("Hey there 2");
		long pid = (long)holder.getKey();
		System.out.println("[TEST : GENERATED_KEY NEWTEST] " + holder.getKey());
		
		
		if (!firstname.equals(ExistinguserInfo.getFirstname())) {
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

		if (!lastname.equals(ExistinguserInfo.getLastname())) {
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

		if (!address.equals(ExistinguserInfo.getAddress())) {
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
		
		if (!city.equals(ExistinguserInfo.getCity())) {
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
		
		if (!state.equals(ExistinguserInfo.getState())) {
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
		
		if (!country.equals(ExistinguserInfo.getCountry())) {
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
		
		if (!postcode.equals(Integer.toString(ExistinguserInfo.getPostcode()))) {
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
		
		if (!contactno.equals(Long.toString(ExistinguserInfo.getContactno()))) {
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
	public void myNewMethod2(String uName,String email,String address,String city,String state,String country,String postcode) {
		User ExistinguserInfo = userDaoImpl.getExternalUserInfo(uName).get(0);
		
		String sql = "insert into requests (requesterusername,status) values (?,?);";
			
		GeneratedKeyHolder holder = new GeneratedKeyHolder();
		jdbcTemplate.update(new PreparedStatementCreator() {
       
			@Override
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				// TODO Auto-generated method stub
				PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
				ps.setString(1, uName);
				ps.setString(2, "pending");
				return ps;
			}
		}, holder);
		System.out.println("Hey there 2");
		long pid = (long)holder.getKey();
		System.out.println("[TEST : GENERATED_KEY NEWTEST] " + holder.getKey());
		
		

		if (!address.equals(ExistinguserInfo.getAddress())) {
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
		
		if (!city.equals(ExistinguserInfo.getCity())) {
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
		
		if (!state.equals(ExistinguserInfo.getState())) {
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
		
		if (!country.equals(ExistinguserInfo.getCountry())) {
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
		
		if (!postcode.equals(Integer.toString(ExistinguserInfo.getPostcode()))) {
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
		
		if (!email.equals(ExistinguserInfo.getEmail())) {
			String sql8 = "insert into request_changes (requestid, fieldname, fieldvalue) values (?,?,?);";
			GeneratedKeyHolder holder8 = new GeneratedKeyHolder();
			jdbcTemplate.update(new PreparedStatementCreator() {

				@Override
				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
					// TODO Auto-generated method stub
					PreparedStatement ps = con.prepareStatement(sql8, Statement.RETURN_GENERATED_KEYS);
					ps.setString(1, Long.toString(pid));
					ps.setString(2, "email");
					ps.setString(3, email);
					return ps;
				}
			}, holder8);
		
		}

	}
	
	@Override
	public int addNewUser(String username, String password, String firstname, String lastname, String dateofbirth,
			String email, String address, Long contactno, Long ssn, String city, String state, String country,
			int postcode, String role) {

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
				+ "(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		
		int userQ=jdbcTemplate.update(sql, new Object[] {username, password, firstname, lastname, dateofbirth, address, email, contactno, ssn, city, state, country, postcode, privateKey});
		
		//String role="ROLE_USER";
		String sql2="Insert into user_roles(username,role) values (?,?)";
		
		int roleQ=jdbcTemplate.update(sql2, new Object[] {username, role});

		MailService.sendKey(email, firstname+" "+lastname, publicKey);
		

		
		return roleQ;
	}

	public String check(String username) {
		String sql = "SELECT username from users where username = ?";
		try {
			List<String> ret = jdbcTemplate.query(sql, new Object[] {username}, new RowMapper<String>() {

				@Override
				public String mapRow(ResultSet rs, int rowNum) throws SQLException {
					return rs.getString("username");
				}
				
			});
			
			if(ret.size()==0) {
				return "true";
			} else {
				return "false";
			}
		} catch (EmptyResultDataAccessException e) {
			return "true";
		}

	}

	public String getRole(String un) {
		String sql = "SELECT role from user_roles where username = ?";
		try {
			List<String> ret = jdbcTemplate.query(sql, new Object[] {un}, new RowMapper<String> () {

				@Override
				public String mapRow(ResultSet rs, int rowNum) throws SQLException {
					return rs.getString("role");
				}
				
			});
			if(ret.size() == 0) {
				return "error";
			} else {
				return ret.get(0);
			}
		} catch (EmptyResultDataAccessException e) {
			return "error";
		}
	}

	public String checkEmail(String email) {
		String sql = "SELECT email from users where email = ?";
		try {
			List<String> ret = jdbcTemplate.query(sql, new Object[] {email}, new RowMapper<String>() {

				@Override
				public String mapRow(ResultSet rs, int rowNum) throws SQLException {
					return rs.getString("email");
				}
				
			});
			if (ret.size() == 0) {
				return "true";
			} else {
				return "false";
			}
		} catch (EmptyResultDataAccessException e) {
			return "true";
		}
	}

	@Override
	public void rollback(String username) {
		String delete_user="DELETE FROM users where username = ?";
		String delete_user_role="DELETE FROM user_roles where username = ?";
		String delete_accounts="DELETE FROM account where username = ?";
		String delete_attempts="DELETE FROM attempts where username = ?";
		
		jdbcTemplate.update(delete_user, new Object[] {username});
		jdbcTemplate.update(delete_user_role, new Object[] {username});
		jdbcTemplate.update(delete_accounts, new Object[] {username});
		jdbcTemplate.update(delete_attempts, new Object[] {username});
		
	}
	

	public String checkForExistingAccount(String username,String accounttype) {
		String sql = "SELECT username from account where username = ? and accountType =?";
		try {
			List<String> ret = jdbcTemplate.query(sql, new Object[] {username,accounttype}, new RowMapper<String>() {
				@Override
				public String mapRow(ResultSet rs, int rowNum) throws SQLException {
					return rs.getString("username");
				}
			});			
			if(ret.size()==0) {
				return "false";
			} else {
				return "true";
			}
		} catch (EmptyResultDataAccessException e) {
			return "false";
		}
	}
	
	@Override
	public int resetPassword(String username, String password) {
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
	    String encPassword = encoder.encode(password);
		
	    System.out.println("[TEST] inside resetpass " + username +" | " + password + " | " + encPassword);
		String sql = "update users set password=? where username=?";
		int userQ=jdbcTemplate.update(sql, new Object[] {encPassword, username});
		
		String sql2 = "update attempts set accountNonLocked=1, credentialsNonExpired=1, accountNonExpired=1, attempts=0, password=? where username=?";
		int atmpQ=jdbcTemplate.update(sql2, new Object[] {encPassword, username});
		
		if(userQ != 0 && atmpQ != 0) {
			return 1;
		} else {
			return 0;
		}
	}
	
	@Override
	public String checkValidEntry(String username, String contactno) {
		Long contact;
		try {
			contact = Long.parseLong(contactno);
		} catch (NumberFormatException e) {
			return "false";
		}
		String sql = "select username from users where username=? and  contactno = ?";
		List<String> list = jdbcTemplate.query(sql, new Object[] {username, contact}, new RowMapper<String>() {

			@Override
			public String mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rs.getString("username");
			}
			
		});
		if(list.size()==0) {
			return "false";
		} else {
			return "true";
		}
	}

}
