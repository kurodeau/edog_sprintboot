package com.productorder.model;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

import com.buyer.entity.BuyerVO;
import com.orderdetails.model.OrderDetailsVO;
import com.seller.entity.SellerVO;


public class ProductOrderDTO  {

	

	
	private Integer orderId;//PK

	private SellerVO sellerVO;//FK:sellerId
	private BuyerVO buyerVO;//FK:memberId

	private Integer couponId;//FK3
	private Integer memberPaysShipping;
	private Integer sellerPaysShipping;
	private Integer orderOrigPrice;
	private Integer actualPay;
	private Timestamp orderTime;
	private Integer orderStatus;
	private Integer invoiceNumber;
	private String receiver;
	private String receiveTime;
	private String mobile;
	private String contactAddress;
	private Integer isDelivered;
	private String shippingTime;

	public ProductOrderDTO() { 
	}
	
	
	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

	

	

	public Integer getCouponId() {
		return couponId;
	}


	public void setCouponId(Integer couponId) {
		this.couponId = couponId;
	}


	
	public Integer getMemberPaysShipping() {
		return memberPaysShipping;
	}

	public void setMemberPaysShipping(Integer memberPaysShipping) {
		this.memberPaysShipping = memberPaysShipping;
	}

	public Integer getSellerPaysShipping() {
		return sellerPaysShipping;
	}

	public void setSellerPaysShipping(Integer sellerPaysShipping) {
		this.sellerPaysShipping = sellerPaysShipping;
	}

	public Integer getOrderOrigPrice() {
		return orderOrigPrice;
	}

	public void setOrderOrigPrice(Integer orderOrigPrice) {
		this.orderOrigPrice = orderOrigPrice;
	}

	
	public Integer getActualPay() {
		return actualPay;
	}

	public void setActualPay(Integer actualPay) {
		this.actualPay = actualPay;
	}

	public Timestamp getOrderTime() {
		return orderTime;
	}

	public void setOrderTime(Timestamp orderTime) {
		this.orderTime = orderTime;
	}
	public Integer getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(Integer orderStatus) {
		this.orderStatus = orderStatus;
	}
	public Integer getInvoiceNumber() {
		return invoiceNumber;
	}

	public void setInvoiceNumber(Integer invoiceNumber) {
		this.invoiceNumber = invoiceNumber;
	}
	public String getReceiver() {
		return receiver;
	}

	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}
//	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public String getReceiveTime() {
		return receiveTime;
	}

	public void setReceiveTime(String receiveTime) {
		this.receiveTime = receiveTime;
	}
	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	
	public String getContactAddress() {
		return contactAddress;
	}

	public void setContactAddress(String contactAddress) {
		this.contactAddress = contactAddress;
	}
	public Integer getIsDelivered() {
		return isDelivered;
	}

	public void setIsDelivered(Integer isDelivered) {
		this.isDelivered = isDelivered;
	}
//	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public String getShippingTime() {
		return shippingTime;
	}

	public void setShippingTime(String shippingTime) {
		this.shippingTime = shippingTime;
	}
	
/////訂單明細關聯/////////////////////////////////////////
	private Set<OrderDetailsVO> orderDetailss = new HashSet<OrderDetailsVO>();
	
	public Set<OrderDetailsVO> getOrderDetailss() {
		return this.orderDetailss;
	}

	public void setOrderDetailss(Set<OrderDetailsVO> orderDetailss) {
		this.orderDetailss = orderDetailss;
	}
	

////////買家（會員）關聯/////////////////////////////////

	public BuyerVO getBuyerVO() {
		return buyerVO;
	}

	public void setBuyerVO(BuyerVO buyerVO) {
		this.buyerVO = buyerVO;
	}
	
////////賣家關聯/////////////////////////////////
	public SellerVO getSellerVO() {
		return sellerVO;
	}

	public void setSellerVO(SellerVO sellerVO) {
		this.sellerVO = sellerVO;
	}
	
}
