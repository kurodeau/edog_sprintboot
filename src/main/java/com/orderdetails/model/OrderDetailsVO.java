package com.orderdetails.model;

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

import com.product.model.ProductVO;
import com.productorder.model.ProductOrderVO;


@Entity
@Table(name = "orderDetails")
public class OrderDetailsVO implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	
	
	
	private Integer orderDetailsId;
	private ProductOrderVO productOrderVO; //FK:orderId
	private ProductVO productVO; //FK:productId
	
	private Integer purchaseQuantity;
	private Boolean isCommented;
	private Integer stars;
	private Timestamp commentedTime;
	private String comments;
	private byte[] attachments;
	private Boolean isEnable;
	
	
	public OrderDetailsVO(){
	}

	@Id //@Id代表這個屬性是這個Entity的唯一識別屬性，並且對映到Table的主鍵 
	@Column(name = "orderDetailsId")  //@Column指這個屬性是對應到資料庫Table的哪一個欄位   //【非必要，但當欄位名稱與屬性名稱不同時則一定要用】
	@GeneratedValue(strategy = GenerationType.IDENTITY) //@GeneratedValue的generator屬性指定要用哪個generator //【strategy的GenerationType, 有四種值: AUTO, IDENTITY, SEQUENCE, TABLE】 
	public Integer getOrderDetailsId() {
		return orderDetailsId;
	}


	public void setOrderDetailsId(Integer orderDetailsId) {
		this.orderDetailsId = orderDetailsId;
	}

	
	

	
	

	@Column(name = "purchaseQuantity")
	public Integer getPurchaseQuantity() {
		return purchaseQuantity;
	}


	public void setPurchaseQuantity(Integer purchaseQuantity) {
		this.purchaseQuantity = purchaseQuantity;
	}

	@Column(name = "isCommented")
	public Boolean getIsCommented() {
		return isCommented;
	}


	public void setIsCommented(Boolean isCommented) {
		this.isCommented = isCommented;
	}


	@Column(name = "stars")
	public Integer getStars() {
		return stars;
	}


	public void setStars(Integer stars) {
		this.stars = stars;
	}


	@Column(name = "commentedTime")
	public Timestamp getCommentedTime() {
		return commentedTime;
	}


	public void setCommentedTime(Timestamp commentedTime) {
		this.commentedTime = commentedTime;
	}


	@Column(name = "comments")
	public String getComments() {
		return comments;
	}


	public void setComments(String comments) {
		this.comments = comments;
	}


	@Column(name = "attachments")
	public byte[] getAttachments() {
		return attachments;
	}


	public void setAttachments(byte[] attachments) {
		this.attachments = attachments;
	}


	@Column(name = "isEnable")
	public Boolean getIsEnable() {
		return isEnable;
	}


	public void setIsEnable(Boolean isEnable) {
		this.isEnable = isEnable;
	}
	
/////訂單與訂單明細的關聯/////////////////////////////////////////
	
	
	@ManyToOne
	@JoinColumn(name = "orderId")   // 指定用來join table的column
	public ProductOrderVO getProductOrderVO() {//FK1
		return productOrderVO;
	}
	

	public void setProductOrderVO(ProductOrderVO productOrderVO) {
		this.productOrderVO = productOrderVO;
	}

	
/////////////////////商品與訂單明細的關聯//////////////////////////////////////////	
	@ManyToOne
	@JoinColumn(name = "productId")   // 指定用來join table的column
	public ProductVO getProductVO() {//FK1
		return productVO;
	}
	
	
	public void setProductVO(ProductVO productVO) {
		this.productVO = productVO;
	}
/////////////////////////////////////////////////////////////
	
	
	
	
}
