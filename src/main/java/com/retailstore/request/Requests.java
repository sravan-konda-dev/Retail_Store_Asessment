package com.retailstore.request;

import com.retailstore.models.Bill;
import com.retailstore.models.User;

public class Requests {
	
	private User user;
	private Bill bill;
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public Bill getBill() {
		return bill;
	}
	public void setBill(Bill bill) {
		this.bill = bill;
	}

}
