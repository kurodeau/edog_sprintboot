package com.cart.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

import com.product.model.ProductVO;
import com.productorder.model.ProductOrderVO;
import com.seller.entity.SellerVO;

public class CartVO {
	
	private String sellerCompany;
	private ProductVO productVO;	
	private SellerVO sellerVO;
	private String productNum;
	
	public CartVO() {
		super();
	}

	public ProductVO getProductVO() {
		return productVO;
	}

	public void setProductVO(ProductVO productVO) {
		this.productVO = productVO;
	}

	public String getSellerCompany() {
		return sellerCompany;
	}

	public void setSellerCompany(String sellerCompany) {
		this.sellerCompany = sellerCompany;
	}

	public String getProductNum() {
		return productNum;
	}

	public void setProductNum(String productNum) {
		this.productNum = productNum;
	}

	public SellerVO getSellerVO() {
		return sellerVO;
	}

	public void setSellerVO(SellerVO sellerVO) {
		this.sellerVO = sellerVO;
	}

	@Override
	public String toString() {
		return "CartVO [sellerCompany=" + sellerCompany + ", productVO=" + productVO + ", sellerVO=" + sellerVO
				+ ", productNum=" + productNum + "]";
	}


}
