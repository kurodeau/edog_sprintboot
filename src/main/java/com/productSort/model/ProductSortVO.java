package com.productSort.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.product.model.ProductVO;

@Entity
@Table(name="productSort")
public class ProductSortVO implements Serializable{
	
	private Integer productSortId;
	private Integer productSortNo;
	private String productSortName;
	private String productCategory;
	private Boolean isEnabled;
	
	private Set<ProductVO> productVO = new HashSet<ProductVO>();
	
	
	
	@Column(name = "productCategory")
	public String getProductCategory() {
		return productCategory;
	}


	public void setProductCategory(String productCategory) {
		this.productCategory = productCategory;
	}


	@OneToMany(cascade = CascadeType.ALL , mappedBy="productSortVO")
	public Set<ProductVO> getProductVO() {
		return productVO;
	}


	public void setProductVO(Set<ProductVO> productVO) {
		this.productVO = productVO;
	}


	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "productSortId")
	public Integer getProductSortId() {
		return productSortId;
	}
	
	
	public void setProductSortId(Integer productSortId) {
		this.productSortId = productSortId;
	}
	
	@Column(name = "productSortNo")
	public Integer getProductSortNo() {
		return productSortNo;
	}
	
	
	public void setProductSortNo(Integer productSortNo) {
		this.productSortNo = productSortNo;
	}
	
	@Column(name = "productSortName")
	public String getProductSortName() {
		return productSortName;
	}
	
	
	public void setProductSortName(String productSortName) {
		this.productSortName = productSortName;
	}
	
	@Column(name = "isEnabled")
	public Boolean getIsEnable() {
		return isEnabled;
	}
	
	
	public void setIsEnable(Boolean isEnable) {
		this.isEnabled = isEnable;
	}
	
	
	
	

}
