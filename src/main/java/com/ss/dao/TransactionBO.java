package com.ss.dao;

import com.ss.model.TransactionDO;
import java.util.List;

public interface TransactionBO {

	List<TransactionDO> getUnapprovedTransactionInfo(String userRole);
	boolean approveTransaction(int transactionId, String approverUserId, String userLevel);
	void declineTransaction(int transactionId);
    List<TransactionDO> getUnapprovedNonCriticalTransactions(String userRole);
	boolean approveTransaction(int transactionId, String approverUserId);
	boolean declineTransaction(int transactionId, String approverUserId);
	    
}
