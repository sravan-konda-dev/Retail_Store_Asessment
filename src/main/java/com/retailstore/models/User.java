package com.retailstore.models;

import java.time.LocalDate;


import com.retailstore.models.UserType;

public class User {
	
	private UserType type;
	private LocalDate joinedDate;
	public User(UserType type, LocalDate joinedDate) {
		super();
		this.type = type;
		this.joinedDate = joinedDate;
	}
	public UserType getType() {
		return type;
	}
	public void setType(UserType type) {
		this.type = type;
	}
	public LocalDate getJoinedDate() {
		return joinedDate;
	}
	public void setJoinedDate(LocalDate joinedDate) {
		this.joinedDate = joinedDate;
	}
	
	



}
