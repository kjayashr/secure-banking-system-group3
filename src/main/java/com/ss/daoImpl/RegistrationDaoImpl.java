package com.ss.daoImpl;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import com.ss.dao.RegistrationDao;

public class RegistrationDaoImpl implements RegistrationDao {

	@Autowired
	DataSource dataSource;
	@Autowired
	JdbcTemplate jdbcTemplate;

	@Override
	public int addNewUser(String username, String password, String firstname,String lastname,
			String dateofbirth, String email, String address, Long contactno, Long ssn, String city, String state, String country, int postcode) {

		String sql="Insert into users(username,passwords,firstname,lastname,dob, address,"
				+ "email, contactno, ssn, city, state,country,postcode) values "
				+ "('" +username+"','"+password+"','"+firstname+"','"+lastname+"','"+dateofbirth+"','"+address+"','"+email+"',"+contactno+","+ssn+",'"+city+"','"+state+"','"+country+"',"+postcode+");";

		return jdbcTemplate.update(sql);	

	}

}
