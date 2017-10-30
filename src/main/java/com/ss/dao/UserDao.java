package com.ss.dao;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.ss.model.User;
import com.ss.model.UserRequest;
import com.ss.model.UserRequestDetails;

public interface UserDao {
	public User getUserbyUsername(String username);
	public User getUserbyEmail(String email);
	public User getUserbyPhone(Long phone);
	public List<User> getUserInfo();
	public List<User> getInternalUserInfo(String username);
	public int ProcessInternalUserProfileUpdate(HttpServletRequest req, String username);
	public int ProcessInternalUserProfileDelete(String username);
	List<UserRequest> getUserRequestsInfo();
	List<UserRequestDetails> getUserRequestsDetailsInfo(int requestid);
	int ProcessRejectUserRequest(int requestid, String approverusername);
}
