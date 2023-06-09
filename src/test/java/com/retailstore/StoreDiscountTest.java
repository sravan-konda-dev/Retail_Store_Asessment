package com.retailstore;

import org.junit.jupiter.api.Assertions;

import org.junit.jupiter.api.Test;

import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;

import com.retailstore.models.Product;
import com.retailstore.models.ProductType;
import com.retailstore.models.User;
import com.retailstore.models.UserType;
import com.retailstore.utility.DiscountLogic;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class StoreDiscountTest {

	@Test
	public void testCalculateTotal() {
		List<Product> items = new ArrayList<Product>();
		items.add(new Product(ProductType.CLOTHES, new BigDecimal(50.0)));
		items.add(new Product(ProductType.PLASTICS, new BigDecimal(150.0)));
		items.add(new Product(ProductType.CLOTHES, new BigDecimal(200.0)));

		DiscountLogic helper = new DiscountLogic();
		BigDecimal total = helper.calculateTotal(items);
		assertEquals(400.00, total.doubleValue(), 0);
	}

	@Test
	public void testCalculateTotalPerType() {
		List<Product> items = new ArrayList<Product>();
		items.add(new Product(ProductType.CLOTHES, new BigDecimal(100.0)));
		items.add(new Product(ProductType.PLASTICS, new BigDecimal(100.0)));
		items.add(new Product(ProductType.CLOTHES, new BigDecimal(100.0)));
		items.add(new Product(ProductType.GROCERY, new BigDecimal(100.0)));
		items.add(new Product(ProductType.GROCERY, new BigDecimal(100.0)));

		DiscountLogic helper = new DiscountLogic();
		BigDecimal total = helper.calculateTotalPerType(items, ProductType.GROCERY);
		assertEquals(200.00, total.doubleValue(), 0);
	}

	@Test
	public void testCalculateTotal_GroceriesOnly() {
		List<Product> products = new ArrayList<Product>();
		products.add(new Product(ProductType.GROCERY, new BigDecimal(100.0)));
		products.add(new Product(ProductType.CLOTHES, new BigDecimal(100.0)));
		products.add(new Product(ProductType.GROCERY, new BigDecimal(100.0)));

		DiscountLogic helper = new DiscountLogic();
		BigDecimal total = helper.calculateTotalPerType(products, ProductType.GROCERY);
		assertEquals(200.00, total.doubleValue(), 0);
	}

	@Test
	public void testCalculateDiscount_10pct() {
		DiscountLogic discountLogic = new DiscountLogic();
		BigDecimal total = discountLogic.calculateDiscount(new BigDecimal(600), new BigDecimal(0.1));
		assertEquals(540.00, total.doubleValue(), 0);
	}

	@Test
	public void testCalculateDiscount_30pct() {
		DiscountLogic discountLogic = new DiscountLogic();
		BigDecimal total = discountLogic.calculateDiscount(new BigDecimal(1000), new BigDecimal(0.3));
		assertEquals(700.00, total.doubleValue(), 0);
	}

	@Test
	public void testCalculateDiscount_0pct() {
		DiscountLogic discountLogic = new DiscountLogic();
		BigDecimal total = discountLogic.calculateDiscount(new BigDecimal(1500), new BigDecimal(0.0));
		assertEquals(1500.00, total.doubleValue(), 0);
	}

	@Test
	public void testCalculateDiscount_100pct() {
		DiscountLogic discountLogic = new DiscountLogic();
		BigDecimal total = discountLogic.calculateDiscount(new BigDecimal(1000), new BigDecimal(1.0));
		assertEquals(0.0, total.doubleValue(), 0);
	}

	@Test
	public void testCalculateDiscount_error() {
		DiscountLogic discountLogic = new DiscountLogic();
		Assertions.assertThrows(IllegalArgumentException.class,
				() -> discountLogic.calculateDiscount(new BigDecimal(1000), new BigDecimal(2.0)));
	}

	@Test
	public void testGetUserSpecificDiscount_affiliate() {
		User user = new User(UserType.AFFILIATE, LocalDate.now());
		DiscountLogic discountLogic = new DiscountLogic();
		BigDecimal discount = discountLogic.getUserDiscount(user);
		assertEquals(0.1, discount.doubleValue(), 0);
	}

	@Test
	public void testUserDiscount_employee() {
		User user = new User(UserType.EMPLOYEE, LocalDate.now());
		DiscountLogic discountLogic = new DiscountLogic();
		BigDecimal discount = discountLogic.getUserDiscount(user);
		assertEquals(0.3, discount.doubleValue(), 0);
	}
	
	@Test
	public void testUserDiscount_affiliate() {
		User user = new User(UserType.AFFILIATE, LocalDate.now());
		DiscountLogic discountLogic = new DiscountLogic();
		BigDecimal discount = discountLogic.getUserDiscount(user);
		assertEquals(0.1, discount.doubleValue(), 0);
	}

	@Test
	public void testUserDiscount_customer_old() {
		LocalDate joinedDate = LocalDate.of(2016, 2, 23);
		User user = new User(UserType.CUSTOMER, joinedDate);
		DiscountLogic discountLogic = new DiscountLogic();
		BigDecimal discount = discountLogic.getUserDiscount(user);
		assertEquals(0.05, discount.doubleValue(), 0);
	}

	@Test
	public void testUserDiscount_customer_new() {
		User user = new User(UserType.CUSTOMER, LocalDate.now());
		DiscountLogic discountLogic = new DiscountLogic();
		BigDecimal discount = discountLogic.getUserDiscount(user);
		assertEquals(0.0, discount.doubleValue(), 0);
	}

	@Test
	public void testUserDiscount_customer_null_user() {
		DiscountLogic discountLogic = new DiscountLogic();
		Assertions.assertThrows(NullPointerException.class, () -> discountLogic.getUserDiscount(null));
	}

	@Test
	public void testIsCustomerOver_a_year() {
		DiscountLogic discountLogic = new DiscountLogic();
		LocalDate joinedDate = LocalDate.now().minusYears(1);
		boolean isEligible = discountLogic.isCustomerOver(joinedDate, 1.0);
		assertFalse(isEligible);
	}

	@Test
	public void testIfCustomerOver_2_year() {
		DiscountLogic discountLogic = new DiscountLogic();
		LocalDate joinedDate = LocalDate.now().minusYears(3);
		boolean isEligible = discountLogic.isCustomerOver(joinedDate, 2.1);
		assertTrue(isEligible);
	}

	@Test
	public void testIsCustomerOver_3_years() {
		DiscountLogic discountLogic = new DiscountLogic();
		LocalDate joinedDate = LocalDate.now().minusYears(3);
		boolean isEligible = discountLogic.isCustomerOver(joinedDate, 2);
		assertTrue(isEligible);
	}

	@Test
	public void testCalculateBillDiscount() {
		DiscountLogic discountLogic = new DiscountLogic();
		BigDecimal amount = discountLogic.calculateBillDiscount(new BigDecimal(987), new BigDecimal(100),
				new BigDecimal(5));
		assertEquals(45, amount.doubleValue(), 0);
	}

	@Test
	public void testCalculateBillDiscount_2() {
		DiscountLogic discountLogic = new DiscountLogic();
		BigDecimal amount = discountLogic.calculateBillDiscount(new BigDecimal(2500), new BigDecimal(100),
				new BigDecimal(5));
		assertEquals(125, amount.doubleValue(), 0);
	}

	@Test
	public void testCalculateBillDiscount_3() {
		DiscountLogic discountLogic = new DiscountLogic();
		BigDecimal amount = discountLogic.calculateBillDiscount(new BigDecimal(532), new BigDecimal(100),
				new BigDecimal(5));
		assertEquals(25, amount.doubleValue(), 0);
	}

}
