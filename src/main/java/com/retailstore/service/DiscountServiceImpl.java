package com.retailstore.service;

import java.math.BigDecimal;


import org.springframework.stereotype.Service;

import com.retailstore.utility.DiscountLogic;


import com.retailstore.models.Bill;
import com.retailstore.models.ProductType;
import com.retailstore.models.User;

@Service
public class DiscountServiceImpl implements DiscountService{
	
	@Override
	public BigDecimal discountCalculation(User user, Bill bill) {
		DiscountLogic discountLogic = new DiscountLogic();
		
		BigDecimal totalAmount = discountLogic.calculateTotal(bill.getProducts());
		BigDecimal groceryAmount = discountLogic.calculateTotalPerType(bill.getProducts(), ProductType.GROCERY);
		BigDecimal nonGroceryAmount = totalAmount.subtract(groceryAmount);
		BigDecimal UserDiscount = discountLogic.getUserDiscount(user);
		BigDecimal billsDiscount = discountLogic.calculateBillDiscount(totalAmount, new BigDecimal(100), new BigDecimal(5));
		if(nonGroceryAmount.compareTo(BigDecimal.ZERO)>0) {
			nonGroceryAmount = discountLogic.calculateDiscount(nonGroceryAmount,UserDiscount);
		}
		BigDecimal netPayableAmount = nonGroceryAmount.add(groceryAmount).subtract(billsDiscount);
		return netPayableAmount;
	}


}
