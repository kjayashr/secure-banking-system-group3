package com.ss.daoImpl;

import java.util.ArrayList;

import javax.sql.DataSource;

import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;

import com.ss.dao.RegistrationDao;
import com.ss.security.GenerateKeyPair;
import com.ss.security.PKICertificate;
import com.ss.service.MailService;

@Repository
public class RegistrationDaoImpl implements RegistrationDao {

	@Autowired
	DataSource dataSource;
	@Autowired
	JdbcTemplate jdbcTemplate;
	
	@Override
	public int addNewUser(String username, String password, String firstname,String lastname,
			String dateofbirth, String email, String address, Long contactno, Long ssn, 
			String city, String state, String country, int postcode) {
		
		String privateKey = "";
		String publicKey = "";
		GenerateKeyPair kp;
		ArrayList<String> errors = new ArrayList<String>();		
		try {
			kp = new GenerateKeyPair();
			
			// Encoding the private key before storing in DB 
			privateKey = Base64.encodeBase64String(kp.getPrivateKey().getBytes());
			publicKey = kp.getPublicKey();
			MailService.sendKey(email, firstname+" "+lastname, publicKey);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			errors.add("There is some issue with your key generation.");
		}
		
		if(errors.size()>0) {
			MailService.sendErrorMail(email, firstname+" "+lastname, errors);
			return 0;
		}
		
		
		BCryptPasswordEncoder encoder=new BCryptPasswordEncoder();
		password=encoder.encode(password);
		String sql="Insert into users(username,password,firstname,lastname,dob, address,"
				+ "email, contactno, ssn, city, state,country,postcode,key_private) values "
				+ "('" +username+"','"+password+"','"+firstname+"','"+lastname+"','"+dateofbirth+"','"+address+"','"+email+"',"+contactno+","+ssn+",'"+city+"','"+state+"','"+country+"',"+postcode+",'"+privateKey+"');";
		
		int userQ=jdbcTemplate.update(sql);
		
		String role="ROLE_USER";
		String sql2="Insert into user_roles(username,role) values"
					+ "('" +username+"','"+role+"');";
		
		int roleQ=jdbcTemplate.update(sql2);
		
		// TODO Remove afterward
		/**
		 * 	TEST CODE 
		 *  DON'T REMOVE NOW. We can use this to implement the PKI
		 *  in functionality like Transaction 
		 */
		
		String testData = "Hello Testing Line";
		try {
			testData = PKICertificate.lock(testData, publicKey);
		} catch (Exception e1) {
			e1.printStackTrace();
			errors.add("Your key is not valid.");
		}
		
		String pk = "";
		String sqlTest = "Select key_private from users where username = '"+username+"';";
		try {
			pk = jdbcTemplate.queryForObject(sqlTest, String.class);
			pk = new String(Base64.decodeBase64(pk));
		}catch(EmptyResultDataAccessException e) {
			return 0;
		}
		
		try {
			testData = PKICertificate.unlock(testData, pk);
		} catch (Exception e) {
			errors.add("Your Prkey is not valid.");
		}
		
		System.out.println(testData);
		if(errors.size()>0) {
			MailService.sendErrorMail(email, firstname+" "+lastname, errors);
			return 0;
		}
		
		/*****************/
		
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
