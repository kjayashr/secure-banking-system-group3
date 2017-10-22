package com.ss.model;

public class TransactionDO {
	private int transactionId;
	private long transactionDate;
	private double amount;
	private String status;
	private String approverUserName;
	private String targetUserName;
	private String transactorUserName;
	
	public int getTransactionId() {
		return transactionId;
	}
	public void setTransactionId(int transactionId) {
		this.transactionId = transactionId;
	}
	public long getTransactionDate() {
		return transactionDate;
	}
	public void setTransactionDate(long transactionDate) {
		this.transactionDate = transactionDate;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getApproverUserName() {
		return approverUserName;
	}
	public void setApproverUserName(String approverUserName) {
		this.approverUserName = approverUserName;
	}
	public String getTargetUserName() {
		return targetUserName;
	}
	public void setTargetUserName(String targetUserName) {
		this.targetUserName = targetUserName;
	}

	public String getTransactorUserName() {
		return transactorUserName;
	}
	public void setTransactorUserName(String transactorUserName) {
		this.transactorUserName = transactorUserName;
	}
}
