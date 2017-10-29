package com.ss.dao;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.ss.model.User;

public interface UserDao {
	
	 List<User> getUserInfo();
	 List<User> getInternalUserInfo(String username);
	 int ProcessInternalUserProfileUpdate(HttpServletRequest req, String username);
	 int ProcessInternalUserProfileDelete(String username);
}
