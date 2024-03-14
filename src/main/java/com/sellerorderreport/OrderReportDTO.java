package com.sellerorderreport;

import java.math.BigDecimal;
import java.time.LocalDateTime;


public class OrderReportDTO {


	  private Integer orderId;
	    private LocalDateTime orderDate;
	    private String productName;
	    private Integer quantity;
	    private Integer status;
	    private BigDecimal unitPrice;
	    
	    
		public Integer getOrderId() {
			return orderId;
		}
		public LocalDateTime getOrderDate() {
			return orderDate;
		}
		public void setOrderDate(LocalDateTime orderDate) {
			this.orderDate = orderDate;
		}
		public void setOrderId(Integer orderId) {
			this.orderId = orderId;
		}
	
		public String getProductName() {
			return productName;
		}
		public void setProductName(String productName) {
			this.productName = productName;
		}
		public Integer getQuantity() {
			return quantity;
		}
		public void setQuantity(Integer quantity) {
			this.quantity = quantity;
		}
		public Integer getStatus() {
			return status;
		}
		public void setStatus(Integer status) {
			this.status = status;
		}
		public BigDecimal getUnitPrice() {
			return unitPrice;
		}
		public void setUnitPrice(BigDecimal unitPrice) {
			this.unitPrice = unitPrice;
		}
		
	    
	    
}
