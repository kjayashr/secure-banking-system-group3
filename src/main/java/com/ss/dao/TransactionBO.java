package com.ss.dao;

import com.ss.model.TransactionDO;
import java.util.List;

public interface TransactionBO {
	
    public List<TransactionDO> getUnapprovedNonCriticalTransactions(String userRole);
	public boolean approveTransaction(int transactionId, String approverUserId);
	public boolean declineTransaction(int transactionId, String approverUserId);
	    
}
