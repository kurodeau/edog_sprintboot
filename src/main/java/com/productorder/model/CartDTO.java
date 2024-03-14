package com.productorder.model;

import java.util.List;

public class CartDTO {
	private Integer sellerId;
    private ReceiverInfoDTO receiverInfo;
    private List<ProductInfoDTO> products;
	public Integer getSellerId() {
		return sellerId;
	}
	public void setSellerId(Integer sellerId) {
		this.sellerId = sellerId;
	}
	public ReceiverInfoDTO getReceiverInfo() {
		return receiverInfo;
	}
	public void setReceiverInfo(ReceiverInfoDTO receiverInfo) {
		this.receiverInfo = receiverInfo;
	}
	public List<ProductInfoDTO> getProducts() {
		return products;
	}
	public void setProducts(List<ProductInfoDTO> products) {
		this.products = products;
	}
}
