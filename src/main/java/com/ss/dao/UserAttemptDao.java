package com.ss.dao;

import java.util.Date;
import java.util.List;

public interface UserAttemptDao {

	int insertUser(String username, String password, int i, int numOfAttempt, Date date, boolean b, boolean c,
			boolean d);
	public List<String> getBlockedUser();
	public boolean unblockedUser(String username);
	
}
