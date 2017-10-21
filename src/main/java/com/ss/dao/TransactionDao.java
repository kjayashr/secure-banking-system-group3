package com.ss.dao;

import java.util.List;

import com.ss.model.ApprovalList;
import com.ss.model.TransactionList;

public interface TransactionDao {

	List<ApprovalList> getApprovalList(String username);
	int changestatus(String transactionId, String status);
	List<TransactionList> viewTransaction(String username);
}
