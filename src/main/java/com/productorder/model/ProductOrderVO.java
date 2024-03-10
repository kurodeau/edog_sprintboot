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
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import com.allenum.ProductOrderStatus;
import com.orderdetails.model.OrderDetailsVO;

@Entity  //要加上@Entity才能成為JPA的一個Entity類別
@Table(name = "productOrder") //代表這個class是對應到資料庫的實體table，目前對應的table是EMP2 
public class ProductOrderVO implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	
	
/////關聯大禮包/////////////////////////////////////////
	private Set<OrderDetailsVO> orderDetailss = new HashSet<OrderDetailsVO>();
	
	@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.EAGER, mappedBy="productOrderVO")
	@OrderBy("orderId asc")
	public Set<OrderDetailsVO> getOrderDetailss() {
		return this.orderDetailss;
	}

	public void setOrderDetailss(Set<OrderDetailsVO> orderDetailss) {
		this.orderDetailss = orderDetailss;
	}
	
/////////////////////////////////////////
	
	
	private Integer orderId;//PK
	
	private Integer sellerId;//FK1
	
	private Integer memberId;//FK2
	private Integer couponId;//FK3
	
	private Integer memberPaysShipping;
	private Integer sellerPaysShipping;
	private Integer orderOrigPrice;
	private Integer actualPay;
	private Timestamp orderTime;
	private Integer orderStatus;
	private Integer invoiceNumber;
	private String receiver;
	private Timestamp receiveTime;
	private String mobile;
	private String contactAddress;
	private Integer isDelivered;
	private Timestamp shippingTime;

	public ProductOrderVO() { //必需有一個不傳參數建構子(JavaBean基本知識)
	}
	
	
	@Id //@Id代表這個屬性是這個Entity的唯一識別屬性，並且對映到Table的主鍵 
	@Column(name = "orderId")  //@Column指這個屬性是對應到資料庫Table的哪一個欄位   //【非必要，但當欄位名稱與屬性名稱不同時則一定要用】
	@GeneratedValue(strategy = GenerationType.IDENTITY) //@GeneratedValue的generator屬性指定要用哪個generator //【strategy的GenerationType, 有四種值: AUTO, IDENTITY, SEQUENCE, TABLE】
	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

	@Column(name = "sellerId")	
	public Integer getSellerId() {
		return sellerId;
	}


	public void setSellerId(Integer sellerId) {
		this.sellerId = sellerId;
	}

	@Column(name = "memberId")	
	public Integer getMemberId() {
		return memberId;
	}


	public void setMemberId(Integer memberId) {
		this.memberId = memberId;
	}

	@Column(name = "couponId")	
	public Integer getCouponId() {
		return couponId;
	}


	public void setCouponId(Integer couponId) {
		this.couponId = couponId;
	}


	//	public SellerVO getSellerVO() {
//		return sellerVO;
//	}
//
//	public void setSellerVO(sellerVO sellerVO) {
//		this.sellerVO = sellerVO;
//	}
//
//	public BuyerVO getBuyerVO() {
//		return buyerVO;
//	}
//
//	public void setBuyerVO(BuyerVO buyerVO) {
//		this.buyerVO = buyerVO;
//	}
//
//	public CouponVO getCouponVO() {
//		return couponVO;
//	}
//
//	public void setCouponVO(CouponVO couponVO) {
//		this.couponVO = couponVO;
//	}
	@Column(name = "memberPaysShipping")
	public Integer getMemberPaysShipping() {
		return memberPaysShipping;
	}

	public void setMemberPaysShipping(Integer memberPaysShipping) {
		this.memberPaysShipping = memberPaysShipping;
	}

	@Column(name = "sellerPaysShipping")
	public Integer getSellerPaysShipping() {
		return sellerPaysShipping;
	}

	public void setSellerPaysShipping(Integer sellerPaysShipping) {
		this.sellerPaysShipping = sellerPaysShipping;
	}

	@Column(name = "orderOrigPrice")
	public Integer getOrderOrigPrice() {
		return orderOrigPrice;
	}

	public void setOrderOrigPrice(Integer orderOrigPrice) {
		this.orderOrigPrice = orderOrigPrice;
	}

	@Column(name = "actualPay")
	
	public Integer getActualPay() {
		return actualPay;
	}

	public void setActualPay(Integer actualPay) {
		this.actualPay = actualPay;
	}

	@Column(name = "orderTime")
	public Timestamp getOrderTime() {
		return orderTime;
	}

	public void setOrderTime(Timestamp orderTime) {
		this.orderTime = orderTime;
	}
	@Column(name = "orderStatus")
	public Integer getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(Integer orderStatus) {
		this.orderStatus = orderStatus;
	}
	@Column(name = "invoiceNumber")
	public Integer getInvoiceNumber() {
		return invoiceNumber;
	}

	public void setInvoiceNumber(Integer invoiceNumber) {
		this.invoiceNumber = invoiceNumber;
	}
	@Column(name = "receiver")
	public String getReceiver() {
		return receiver;
	}

	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}
	@Column(name = "receiveTime")
	public Timestamp getReceiveTime() {
		return receiveTime;
	}

	public void setReceiveTime(Timestamp receiveTime) {
		this.receiveTime = receiveTime;
	}
	@Column(name = "mobile")
	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	
	@Column(name = "contactAddress")
	public String getContactAddress() {
		return contactAddress;
	}

	public void setContactAddress(String contactAddress) {
		this.contactAddress = contactAddress;
	}
	@Column(name = "isDelivered")
	public Integer getIsDelivered() {
		return isDelivered;
	}

	public void setIsDelivered(Integer isDelivered) {
		this.isDelivered = isDelivered;
	}
	@Column(name = "shippingTime")
	public Timestamp getShippingTime() {
		return shippingTime;
	}

	public void setShippingTime(Timestamp shippingTime) {
		this.shippingTime = shippingTime;
	}
	
	
	
	
	
}
