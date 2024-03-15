package com.productorder.model;

import java.util.List;

public class ShoppingCartDTO {

	  private List<CartDTO> carts;

	public List<CartDTO> getCarts() {
		return carts;
	}

	public void setCarts(List<CartDTO> carts) {
		this.carts = carts;
	}
	
}
