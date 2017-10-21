package com.ss.dao;

import java.util.List;

import com.ss.model.ApprovalList;

public interface TransactionDao {
	
	List<ApprovalList> getApprovalList(String username);
	int changestatus(String transactionId, String status);

}
