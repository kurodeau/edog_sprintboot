package com.productImg.model;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.product.model.ProductVO;


	
	@Entity
	@Table(name = "productImage") // Specify the corresponding database table name
	public class ProductImgVO implements java.io.Serializable {

		
		private Integer productImgId;
		private ProductVO productId; // Replace YourProductVO with your actual product entity
		private byte[] productImg;
		private Date productImgTime;		
		private Boolean isCover;
		private Boolean isEnabled;

		
		@Id
		@GeneratedValue(strategy = GenerationType.IDENTITY)
		@Column(name = "productImgId")
		public Integer getProductImgId() {
			return productImgId;
		}

		
		
		public void setProductImgId(Integer productImgId) {
			this.productImgId = productImgId;
		}

		@ManyToOne
		@JoinColumn(name = "productId", referencedColumnName = "productId")
		public ProductVO getProductId() {
			return productId;
		}

		public void setProductId(ProductVO productId) {
			this.productId = productId;
		}
		
		@Column(name = "productImg")
		public byte[] getProductImg() {
			return productImg;
		}

		public void setProductImg(byte[] productImg) {
			this.productImg = productImg;
		}

		@Column(name = "productImgTime")
		public Date getProductImgTime() {
			return productImgTime;
		}

		public void setProductImgTime(Date productImgTime) {
			this.productImgTime = productImgTime;
		}

		
		@Column(name = "isCover")
		public Boolean getIsCover() {
			return isCover;
		}

		public void setIsCover(Boolean isCover) {
			this.isCover = isCover;
		}

		@Column(name = "isEnabled")
		public Boolean getIsEnabled() {
			return isEnabled;
		}

		public void setIsEnabled(Boolean isEnabled) {
			this.isEnabled = isEnabled;
		}

	}


