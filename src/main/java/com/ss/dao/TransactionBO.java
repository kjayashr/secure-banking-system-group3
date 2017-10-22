package com.ss.dao;

import com.ss.model.TransactionDO;
import java.util.List;

public interface TransactionBO {
	
    public List<TransactionDO> getUnapprovedTransactionInfo(String userRole);
	public boolean approveTransaction(int transactionId, String approverUserId, String userLevel);
	public void declineTransaction(int transactionId);
	    
}
