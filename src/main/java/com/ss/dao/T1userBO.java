package com.ss.dao;

import com.ss.model.T1userDO;
import java.util.List;

public interface T1userBO{
	List<T1userDO> gett1users(String userrole);
	boolean grantApproval(String userName, String userId);
	
}