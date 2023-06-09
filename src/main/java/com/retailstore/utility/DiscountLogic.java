package com.retailstore.utility;

import java.math.BigDecimal;


import java.math.RoundingMode;
import java.time.LocalDate;

import java.util.List;

import com.retailstore.models.Product;
import com.retailstore.models.ProductType;
import com.retailstore.models.User;
import com.retailstore.models.UserType;

public class DiscountLogic {
	private static final double YEARS_FOR_DISCOUNT = 2;

	private static final double EMPLOYEE_DISCOUNT = 0.30;
	private static final double AFFILIATE_DISCOUNT = 0.10;
	private static final double CUSTOMER__DISCOUNT = 0.05;

	public BigDecimal calculateTotal(List<Product> products) {

		return products.stream().map(p -> p.getPrice()).reduce(BigDecimal.ZERO, BigDecimal::add);

	}

	public BigDecimal calculateTotalPerType(List<Product> products, ProductType type) {
		BigDecimal sum = new BigDecimal(0);

		if (type != null) {

			sum = products.stream().filter(t -> type.equals(t.getType())).map(t -> t.getPrice()).reduce(sum,
					BigDecimal::add);
		}
		return sum;

	}

	public BigDecimal getUserDiscount(User user) {

		if (user == null) {
			throw new NullPointerException("User cannot be null");
		}

		BigDecimal discount = new BigDecimal(0);

		UserType type = user.getType();

		switch (type) {

		case EMPLOYEE:
			discount = new BigDecimal(EMPLOYEE_DISCOUNT).setScale(3, RoundingMode.HALF_EVEN);
			break;

		case AFFILIATE:
			discount = new BigDecimal(AFFILIATE_DISCOUNT).setScale(3, RoundingMode.HALF_EVEN);
			break;

		case CUSTOMER:
			if (isCustomerOver(user.getJoinedDate(), YEARS_FOR_DISCOUNT)) {
				discount = new BigDecimal(CUSTOMER__DISCOUNT).setScale(2, RoundingMode.HALF_EVEN);
			}
			break;

		default:

			break;
		}
		return discount;
	}

	public boolean isCustomerOver(LocalDate joinedDate, double years) {

		LocalDate currentDate = LocalDate.now();
		return joinedDate.plusYears((long) years).isBefore(currentDate);
				
	}

	public BigDecimal calculateBillDiscount(BigDecimal totalAmount, BigDecimal amount, BigDecimal discountAmount) {
		int discAmount = totalAmount.divide(amount).intValue();
		return discountAmount.multiply(new BigDecimal(discAmount));
	}

	public BigDecimal calculateDiscount(BigDecimal amount, BigDecimal discount) {
		if (discount.doubleValue() > 1.0) {
			throw new IllegalArgumentException("Discount cannot exceed 100%");
		}

		BigDecimal x = amount.multiply(discount);
		return amount.subtract(x);

	}

}
