package com.ss.dao;

import java.util.Date;

public interface UserAttemptDao {

	int insertUser(String username, String password, int i, int numOfAttempt, Date date, boolean b, boolean c,
			boolean d);
	
}
