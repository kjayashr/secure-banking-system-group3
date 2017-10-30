package com.ss.daoImpl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.ss.dao.UserDao;
import com.ss.model.User;
import com.ss.model.UserRequest;
import com.ss.model.UserRequestDetails;

@Repository
public class UserDaoImpl implements UserDao {

	@Autowired
	DataSource dataSource;
	@Autowired
	JdbcTemplate jdbcTemplate;

	private List<User> getUserbyString(String sql, Object param) {
		try {
			return (List<User>) jdbcTemplate.query(sql, new Object[] { param }, new userinfoMapper());
		} catch (DataAccessException e) {
			// TODO Log message
			return null;
		}
	}

	@Override
	public User getUserbyUsername(String username) {
		String sql = "select username,firstname,lastname,dob, address, email, contactno, "
				+ "ssn, city, state, country, postcode from users where username = ?";

		List<User> user = getUserbyString(sql, username);

		if (user.size() != 0) {
			return user.get(0);
		} else {
			return null;
		}

	}

	@Override
	public User getUserbyEmail(String email) {
		String sql = "select username, firstname, lastname, dob, address, email, contactno, "
				+ "ssn, city, state, country, postcode from users where email= ?";

		List<User> user = getUserbyString(sql, email);

		if (user.size() != 0) {
			return user.get(0);
		} else {
			return null;
		}
	}

	@Override
	public User getUserbyPhone(Long phone) {
		String sql = "select username, firstname, lastname, dob, address, email, contactno, "
				+ "ssn, city, state, country, postcode from users where contactno= ?";

		List<User> user = getUserbyString(sql, phone);

		if (user.size() != 0) {
			return user.get(0);
		} else {
			return null;
		}
	}
	
	@Override
	public String getPrivateKey(String username) {
		String sql = "select key_private from users where username = ?";
		try {
			List<String> keyList = jdbcTemplate.query(sql, new Object[] {username}, new RowMapper<String> () {

				@Override
				public String mapRow(ResultSet rs, int rowNum) throws SQLException {
					return rs.getString("key_private");
				}
			});
			if(keyList.size() == 0) {
				return null;
			} else {
				return keyList.get(0);
			}
		} catch (Exception e) {
			//TODO log statement
			return null;
		}
	}

	@Override
	public List<User> getUserInfo() {
		String sql = "select username, firstname, lastname, dob, address, email, contactno, "
				+ "ssn, city, state, country, postcode from users";
		try {
			return jdbcTemplate.query(sql, new userinfoMapper());
		} catch (DataAccessException e) {
			// TODO Log message
			return null;
		}
	}

	@Override
	public List<User> getInternalUserInfo(String username) {
		String sql = "select users.username as username, firstname, lastname, dob, address, email, contactno, "
				+ "ssn, city, state, country, postcode from users inner join user_roles on users.username = user_roles.username where "
				+ "users.username=? and user_roles.role like 'ROLE_TIER%'";
		try {
			return jdbcTemplate.query(sql, new Object[] { username }, new userinfoMapper());
		} catch (DataAccessException e) {
			// TODO Log message
			return null;
		}

	}
	
	
	public List<User> getExternalUserInfo(String username) {
		System.out.println("Inside getExternaluserinfo");
		String sql = "select users.username as username, firstname, lastname, dob, address, email, contactno, "
				+ "ssn, city, state, country, postcode from users inner join user_roles on users.username = user_roles.username where "
				+ "users.username=? and (user_roles.role like 'ROLE_USER%' or user_roles.role like 'ROLE_MERCHANT%')";
				
		try {
			System.out.println("This");
			return jdbcTemplate.query(sql, new Object[] { username }, new userinfoMapper());
		} catch (DataAccessException e) {
			// TODO Log message
			System.out.println("Or This");
			return null;
		}

	}
	

	

	@Override
	public int ProcessInternalUserProfileUpdate(HttpServletRequest req, String username) {
		System.out.println("ProcessInternalUserProfileUpdate"+   username);
		String sql = "update users set firstname = ?, lastname = ?, dob = ?, address = ?, city = ?, state = ?, "
				+ "country = ?, postcode = ?, contactno = ? where username = ?";
		try {
			return jdbcTemplate.update(sql, new Object[] { req.getParameter("firstname"), req.getParameter("lastname"),
					req.getParameter("dob"), req.getParameter("address"), req.getParameter("city"),
					req.getParameter("state"), req.getParameter("country"), Integer.parseInt(req.getParameter("postcode")),
					Long.parseLong(req.getParameter("contactno")),username });
		} catch (DataAccessException e) {
			// TODO Log message
			System.out.println("God damn it");
			return 0;
		}
	}

	@Override
	public int ProcessInternalUserProfileDelete(String username) {
		String user_sql = "delete from users where username = ?";
		String user_role_sql = "delete from user_roles where username = ?";

		try {
			int user_profile_delete = jdbcTemplate.update(user_sql, new Object[] { username });
			int user_role_delete = jdbcTemplate.update(user_role_sql, new Object[] { username });
			if (user_profile_delete != 0 && user_role_delete != 0) {
				return 1;
			}
		} catch (DataAccessException e) {
			// TODO Log message
		}
		return 0;
	}

	class userinfoMapper implements RowMapper<User> {
		public User mapRow(ResultSet rs, int rowNum) throws SQLException {
			User userdata = new User();
			userdata.setUsername(rs.getString("username"));
			userdata.setFirstname(rs.getString("firstname"));
			userdata.setLastname(rs.getString("lastname"));
			userdata.setDateofbirth(rs.getString("dob"));
			userdata.setSsn(rs.getLong("ssn"));
			userdata.setAddress(rs.getString("address"));
			userdata.setCity(rs.getString("city"));
			userdata.setState(rs.getString("state"));
			userdata.setCountry(rs.getString("country"));
			userdata.setContactno(rs.getLong("contactno"));
			userdata.setEmail(rs.getString("email"));
			userdata.setPostcode(rs.getInt("postcode"));
			return userdata;
		}
	}
	
	@Override
	public List<UserRequest> getUserRequestsInfo() {
		String sql="select requests.id as id,requesterusername,approverusername,status from requests inner join user_roles on "
				+ "requests.requesterusername = user_roles.username where user_roles.role like 'ROLE_TIER%' and requests.status = 'pending' ";
		List<UserRequest> data =jdbcTemplate.query(sql, new userRequestsInfoMapper()) ;
		return data;
	}
	
	class userRequestsInfoMapper implements RowMapper<UserRequest> {
		  public UserRequest mapRow(ResultSet rs, int arg1) throws SQLException {
			UserRequest userrequestdata = new UserRequest();
			userrequestdata.setId(rs.getInt("id"));
			userrequestdata.setRequesterusername(rs.getString("requesterusername"));
			userrequestdata.setApproverusername(rs.getString("approverusername"));
			userrequestdata.setStatus(rs.getString("status"));
		    return userrequestdata;
		  }
	}
	
	
	@Override
	public List<UserRequestDetails> getUserRequestsDetailsInfo(int requestid) {
		String sql="select requestid,fieldname,fieldvalue from request_changes where requestid =" + requestid ;
		List<UserRequestDetails> userrequestdetails =jdbcTemplate.query(sql, new userRequestsIDetailsnfoMapper()) ;
		System.out.println(userrequestdetails.get(0).getFieldname());
		return userrequestdetails;
	}
	
	class userRequestsIDetailsnfoMapper implements RowMapper<UserRequestDetails> {
		  public UserRequestDetails mapRow(ResultSet rs, int arg1) throws SQLException {
			UserRequestDetails userrequestdetails = new UserRequestDetails();
			userrequestdetails.setRequestid(rs.getInt("requestid"));
			userrequestdetails.setFieldname(rs.getString("fieldname"));
			userrequestdetails.setFieldvalue(rs.getString("fieldvalue"));
		    return userrequestdetails;
		  }
	}

	
	public int ProcessApproveUserRequest(HttpServletRequest req,int requestid, String requesterusername, String approverusername ) {
		int i =0;
		List<Object> paramList = new ArrayList<Object>();
		String sql="update users set ";
		if(req.getParameter("firstname") != null) {
			i=1;
			sql = sql + "firstname=? ";
			paramList.add(req.getParameter("firstname"));
		}
		if(req.getParameter("lastname") != null) {
			if(i==1) {
				sql = sql + ",";								
			}
			sql = sql + "lastname=?";
			paramList.add(req.getParameter("lastname"));
			i=1;
		}
		if(req.getParameter("dob") != null) {
			if(i==1) {
				sql = sql + ",";								
			}
			sql = sql + "dob='?";
			paramList.add(req.getParameter("dob"));
			i=1;
		}
		if(req.getParameter("address") != null) {
			if(i==1) {
				sql = sql + ",";								
			}
			sql = sql + "address=?";
			paramList.add(req.getParameter("address"));
			i=1;
		}
		if(req.getParameter("city") != null) {
			if(i==1) {
				sql = sql + ",";								
			}
			sql = sql + "city=?";
			paramList.add(req.getParameter("city"));
			i=1;
		}
		if(req.getParameter("state") != null) {
			if(i==1) {
				sql = sql + ",";								
			}
			sql = sql + "state=?";
			paramList.add(req.getParameter("state"));
			i=1;
		}
		if(req.getParameter("country") != null) {
			if(i==1) {
				sql = sql + ",";								
			}
			sql = sql + "country=?";
			paramList.add(req.getParameter("country"));
			i=1;
		}
		if(req.getParameter("postcode") != null) {
			if(i==1) {
				sql = sql + ",";								
			}
			sql = sql + "postcode=?";
			paramList.add(Integer.parseInt(req.getParameter("postcode")));
			i=1;
		}
		if(req.getParameter("contactno") != null) {
			if(i==1) {
				sql = sql + ",";								
			}
			sql = sql + "contactno=?";
			paramList.add(Long.parseLong(req.getParameter("contactno")));
			i=1;
		}
		
		sql = sql + " where username = ?" ;
		paramList.add(requesterusername);
		
		int userQ=jdbcTemplate.update(sql, paramList.toArray(new Object[0]));
		if(userQ != 0) {
			UpdateUserRequestStatus(requestid, approverusername, "approved");
		}
		return userQ;
	}

	@Override
	public int ProcessRejectUserRequest(int requestid, String approverusername) {
		int d = UpdateUserRequestStatus(requestid,approverusername, "rejected");
		return d;
	}
	
	public int UpdateUserRequestStatus(int requestid, String approverusername, String status) {
		String sql="update requests set approverusername =?, status=? where id =?";
		int update=jdbcTemplate.update(sql, new Object[] {approverusername, status, requestid});
		return update;	
	}

	

}
