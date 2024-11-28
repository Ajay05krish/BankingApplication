package com.user.bankUserService.entity;

public class UserResponse {

	private String id; // Okta user ID

	public UserResponse() {
	}

	public UserResponse(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
}
