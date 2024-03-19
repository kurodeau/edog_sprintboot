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
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.buyer.entity.BuyerVO;
import com.orderdetails.model.OrderDetailsVO;
import com.seller.entity.SellerVO;

@Entity  //要加上@Entity才能成為JPA的一個Entity類別
@Table(name = "productOrder") //代表這個class是對應到資料庫的實體table，目前對應的table是EMP2 
public class ProductOrderVO implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	

	
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

	

	

	@Column(name = "couponId")	
	public Integer getCouponId() {
		return couponId;
	}


	public void setCouponId(Integer couponId) {
		this.couponId = couponId;
	}


	
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
	
	@Size(max = 10, message = "收貨者姓名不能超過10個字")
	@Pattern(regexp = "^[a-zA-Z\\u4e00-\\u9fa5]+$", message = "收貨者姓名僅能是中文或英文")
	@NotEmpty(message = "收貨者姓名不能為空")
	@Column(name = "receiver")
	public String getReceiver() {
		return receiver;
	}

	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}
	@Column(name = "receiveTime")
//	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Timestamp getReceiveTime() {
		return receiveTime;
	}

	public void setReceiveTime(Timestamp receiveTime) {
		this.receiveTime = receiveTime;
	}
	
	
	@Pattern(regexp="^09\\d{8}$", message="手機號碼格式不正確，請輸入09開頭的8位數字")
	@Column(name = "mobile")
	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	
	 @Pattern(regexp = "^[\\u4e00-\\u9fa5a-zA-Z0-9\\s,.-]{10,100}$", message = "收貨地址格式不正確，請以中文、英文字母、数字、空格、逗號（,）、點（.）以及短橫線（-）组成，並且長度為10-100個字之間。")
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
//	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Timestamp getShippingTime() {
		return shippingTime;
	}

	public void setShippingTime(Timestamp shippingTime) {
		this.shippingTime = shippingTime;
	}
	
/////訂單明細關聯/////////////////////////////////////////
	private Set<OrderDetailsVO> orderDetailss = new HashSet<OrderDetailsVO>();
	
	@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.EAGER, mappedBy="productOrderVO")
	@OrderBy("orderId asc")
	public Set<OrderDetailsVO> getOrderDetailss() {
		return this.orderDetailss;
	}

	public void setOrderDetailss(Set<OrderDetailsVO> orderDetailss) {
		this.orderDetailss = orderDetailss;
	}
	

////////買家（會員）關聯/////////////////////////////////

	@ManyToOne
	@JoinColumn(name = "memberId")   // 指定用來join table的column
	public BuyerVO getBuyerVO() {
		return buyerVO;
	}

	public void setBuyerVO(BuyerVO buyerVO) {
		this.buyerVO = buyerVO;
	}
	
////////賣家關聯/////////////////////////////////
	@ManyToOne
	@JoinColumn(name = "sellerId") 
	public SellerVO getSellerVO() {
		return sellerVO;
	}

	public void setSellerVO(SellerVO sellerVO) {
		this.sellerVO = sellerVO;
	}


	@Override
	public String toString() {
		return "ProductOrderVO [orderId=" + orderId + ", sellerVO=" + sellerVO + ", buyerVO=" + buyerVO + ", couponId="
				+ couponId + ", memberPaysShipping=" + memberPaysShipping + ", sellerPaysShipping=" + sellerPaysShipping
				+ ", orderOrigPrice=" + orderOrigPrice + ", actualPay=" + actualPay + ", orderTime=" + orderTime
				+ ", orderStatus=" + orderStatus + ", invoiceNumber=" + invoiceNumber + ", receiver=" + receiver
				+ ", receiveTime=" + receiveTime + ", mobile=" + mobile + ", contactAddress=" + contactAddress
				+ ", isDelivered=" + isDelivered + ", shippingTime=" + shippingTime + ", orderDetailss=" + orderDetailss
				+ "]";
	}
	
	
	
}
