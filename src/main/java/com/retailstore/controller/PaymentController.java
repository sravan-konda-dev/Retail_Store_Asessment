package com.retailstore.controller;

import java.math.BigDecimal;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.retailstore.request.Requests;
import com.retailstore.service.DiscountService;

@RestController
public class PaymentController {
	
	
		@Autowired
		private DiscountService discountService ;
			
		
		@PostMapping("/store/payment")
		public BigDecimal netPayment(@RequestBody Requests paymentRequests) {
			
			return discountService.discountCalculation(paymentRequests.getUser(),paymentRequests.getBill());
			


	}
}
