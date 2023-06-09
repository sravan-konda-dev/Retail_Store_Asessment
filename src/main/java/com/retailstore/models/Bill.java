package com.retailstore.models;

import java.util.List;



import com.retailstore.models.Product;

public class Bill {
	private List<Product> products;

	public List<Product> getProducts() {
		return products;
	}

	public void setProducts(List<Product> products) {
		this.products = products;
	}

}
