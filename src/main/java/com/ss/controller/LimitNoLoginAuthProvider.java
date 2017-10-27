package com.ss.controller;


import java.util.Date;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import com.ss.dao.UserDetailsDao;
import com.ss.model.UserAttempts;

public class LimitNoLoginAuthProvider extends DaoAuthenticationProvider{

	UserDetailsDao userDetailsDao;
	
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		try{
			Authentication auth = super.authenticate(authentication);
			userDetailsDao.resetFailAttempts(authentication.getName());

			return auth;

		}catch(BadCredentialsException e) {
			userDetailsDao.updateFailAttempts(authentication.getName());
			throw e;
		} catch (LockedException e) {

			String error = "";
			UserAttempts userAttempts = userDetailsDao.getUserAttempts(authentication.getName());
			if (userAttempts != null) {
				Date lastAttempts = userAttempts.getLastModified();
				error = "User account is locked! Username : " + authentication.getName()
						+ " Last Attempts : " + lastAttempts + " Please contact the administrator ";
			} else {
				error = e.getMessage();
			}

			throw new LockedException(error);
		}

	}
	
	public UserDetailsDao getUserDetailsDao() {
		return userDetailsDao;
	}

	public void setUserDetailsDao(UserDetailsDao userDetailsDao) {
		this.userDetailsDao = userDetailsDao;
	}
	
}
