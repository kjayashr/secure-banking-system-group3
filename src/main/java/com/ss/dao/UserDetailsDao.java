package com.ss.dao;

import com.ss.model.UserAttempts;

public interface UserDetailsDao {
	

	public void resetFailAttempts(String username);

	public void updateFailAttempts(String name) ;

	public UserAttempts getUserAttempts(String name);

}
