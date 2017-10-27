package com.ss.dao;

import com.ss.model.User;

public interface UserDao {
	public User getUserbyUsername(String username);
	public User getUserbyEmail(String email);
	public User getUserbyPhone(Long phone);
}
