package com.ss.model;

public class TransactionDetails {
	
	int id;
	String date;
	Double amount;
	String detail;
	String status;
	String transferto;
	String transacterUsername;
	boolean critical;
	String approverUsername;
	String fromAccountType;
	String toAccounttype;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public Double getAmount() {
		return amount;
	}
	public void setAmount(Double amount) {
		this.amount = amount;
	}
	public String getDetail() {
		return detail;
	}
	public void setDetail(String detail) {
		this.detail = detail;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getTransferto() {
		return transferto;
	}
	public void setTransferto(String transferto) {
		this.transferto = transferto;
	}
	public String getTransacterUsername() {
		return transacterUsername;
	}
	public void setTransacterUsername(String transacterUsername) {
		this.transacterUsername = transacterUsername;
	}
	public boolean isCritical() {
		return critical;
	}
	public void setCritical(boolean critical) {
		this.critical = critical;
	}
	public String getApproverUsername() {
		return approverUsername;
	}
	public void setApproverUsername(String approverUsername) {
		this.approverUsername = approverUsername;
	}
	public String getFromAccountType() {
		return fromAccountType;
	}
	public void setFromAccountType(String fromAccountType) {
		this.fromAccountType = fromAccountType;
	}
	public String getToAccounttype() {
		return toAccounttype;
	}
	public void setToAccounttype(String toAccounttype) {
		this.toAccounttype = toAccounttype;
	}

}
