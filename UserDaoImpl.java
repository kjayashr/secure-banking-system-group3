package com.ss.daoImpl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.ss.dao.UserDao;
import com.ss.model.User;

@Component
public class UserDaoImpl implements UserDao {

	@Autowired
	DataSource dataSource;
	
	@Autowired
	JdbcTemplate jdbcTemplate;

	@Override
	public List<User> getUserInfo() {
		String sql="select username,firstname,lastname,dob,ssn,address,city,state,country,contactno,email,postcode from users";
		List<User> data =jdbcTemplate.query(sql, new userinfoMapper()) ;
		return data;
	}

	@Override
	public List<User> getInternalUserInfo(String username) {
		String sql="select users.username as username,firstname,lastname,dob,ssn,address,city,state,country,postcode,contactno,email from users inner join user_roles on users.username = user_roles.username where "
		+ "users.username='" + username + "' " 
		+ "and user_roles.role like 'ROLE_TIER%'";
		List<User> data =jdbcTemplate.query(sql, new userinfoMapper()) ;
		return data;
	}
	
	public int ProcessInternalUserProfileUpdate(HttpServletRequest req, String username) {

		String sql="update users set "
			+ "firstname='" +req.getParameter("firstname") + "',"
			+ "lastname='" +req.getParameter("lastname") + "',"
			+ "dob='" +req.getParameter("dob") + "',"
			+ "ssn='" + Long.parseLong(req.getParameter("ssn")) + "',"
			+ "city='" +req.getParameter("city") + "',"
			+ "state='" +req.getParameter("state") + "',"
			+ "country='" +req.getParameter("country") + "',"
			+ "postcode='" + Integer.parseInt(req.getParameter("postcode")) + "',"
			+ "contactno='" + Long.parseLong(req.getParameter("contactno")) + "',"
			+ "country='" + req.getParameter("country")
			+"' where username = '" + username + "' ;" ;
		int userQ=jdbcTemplate.update(sql);
		return userQ;
	}
	
	public int ProcessInternalUserProfileDelete(String username) {
		String user_sql = "delete from users where username = '"+username +"';" ;
		int user_profile_delete = jdbcTemplate.update(user_sql);
		String user_role_sql = "delete from user_roles where username = '"+username +"';" ;
		int user_role_delete = jdbcTemplate.update(user_role_sql);
		if(user_profile_delete!=0 && user_role_delete!=0) {
			return 1; 
		}
		return 0;
	}
	
	class userinfoMapper implements RowMapper<User> {
		  public User mapRow(ResultSet rs, int arg1) throws SQLException {
		    User userdata = new User();
		    userdata.setUsername(rs.getString("username"));
		    userdata.setFirstname(rs.getString("firstname"));
		    userdata.setLastname(rs.getString("lastname"));
		    userdata.setDob(rs.getString("dob"));
		    userdata.setSsn(rs.getInt("ssn"));
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
	
}
