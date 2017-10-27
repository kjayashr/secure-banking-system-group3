package com.ss.daoImpl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import com.ss.dao.OTPDao;

public class OTPDaoImpl implements OTPDao {

	@Autowired
	DataSource dataSource;
	@Autowired
	JdbcTemplate jdbcTemplate;

	private static final String INSERT = "insert into otp (username, OTP) values (?, ?)";
	private static final String DELETE = "delete from otp where username = ?";
	private static final String UPDATE = "update otp set OTP = ? where username = ?";
	private static final String SELECT = "select OTP from otp where username = ?";

	@Override
	public boolean create(String username, String OTP) {

		boolean success = false;
		try {
			jdbcTemplate.update(INSERT, new Object[] { username, OTP });
			success = true;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return success;

	}

	@Override
	public boolean delete(String username) {

		boolean success = false;
		try {
			int rs = jdbcTemplate.update(DELETE, new Object[] { username });
			if (rs > 0) {
				success = true;
			}
		} catch (DataAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return success;
	}

	@Override
	public boolean update(String username, String OTP) {

		boolean success = false;

		try {
			int rs = jdbcTemplate.update(UPDATE, new Object[] { OTP, username });
			if (rs > 0) {
				success = true;
			}
		} catch (DataAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return success;
	}

	@Override
	public String getOTP(String username) {
		String sql = SELECT;
		List<String> OTP = (List<String>) jdbcTemplate.query(sql, new Object[] { username }, new RowMapper<String>() {

			@Override
			public String mapRow(ResultSet rs, int rowNum) throws SQLException {
				// TODO Auto-generated method stub
				return rs.getString("OTP");
			}

		});

		if (OTP.size() == 0) {
			return "-1";
		} else {
			return OTP.get(0);
		}
	}
}
