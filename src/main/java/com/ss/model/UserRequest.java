package com.ss.model;

public class UserRequest {
	
	public int id;
	private String requesterusername;
	private String approverusername;
	private String status;

	public int getId() {
		return id;
	}
	public String getRequesterusername() {
		return requesterusername;
	}
	public String getApproverusername() {
		return approverusername;
	}
	public String getStatus() {
		return status;
	}
	public void setId(int id) {
		this.id = id;
	}
	public void setRequesterusername(String requesterusername) {
		this.requesterusername = requesterusername;
	}
	public void setApproverusername(String approverusername) {
		this.approverusername = approverusername;
	}
	public void setStatus(String status) {
		this.status = status;
	}
}
