package com.retailstore.service;

import java.math.BigDecimal;


import com.retailstore.models.Bill;
import com.retailstore.models.User;

public interface DiscountService {
	
	public BigDecimal discountCalculation(User user, Bill bill) ;


}
