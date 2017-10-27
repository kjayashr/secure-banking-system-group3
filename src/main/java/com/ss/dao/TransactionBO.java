package com.ss.dao;

import com.ss.model.TransactionDO;

import java.util.Date;
import java.util.List;

public interface TransactionBO {

	List<TransactionDO> getUnapprovedTransactionInfo(String userRole);
	boolean approveTransaction(int transactionId, String approverUserId, String userLevel);
	void declineTransaction(int transactionId);
    List<TransactionDO> getUnapprovedNonCriticalTransactions(String userRole);
    List<TransactionDO> getUnapprovedCriticalTransactions(String userRole);
	boolean approveTransaction(int transactionId, String approverUserId);
	boolean declineTransaction(int transactionId, String approverUserId);
	void insertTransaction(double amount, String detail, String status, String username, Date date, String to,
			boolean critical, String fromAccountType, String toAccountType);
	    
}
