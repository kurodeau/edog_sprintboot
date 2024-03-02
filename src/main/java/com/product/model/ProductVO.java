package com.product.model;
import java.math.BigDecimal;
import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.seller.entity.SellerVO;



@Entity
@Table(name = "product")  // Replace "your_table_name" with the actual table name
public class ProductVO {
	public static final Integer MAX_IMAGE_SIZE =  10 * 1024 * 1024;
//	public static final Integer MAX_PRODUCT_SORT =  ProductSortEnum.values().length;


    private Integer productId;   
    private byte[] productCoverImg;
    private String productName;    
    private BigDecimal productPrice;
    private Integer productStockQuantity;    
    private String productDetails;    
    private String productStatus;
    private Date productCreationTime; 
    private Integer totalStars;    
    private Integer totalReviews;
    private Integer productSort;
    private Boolean isEnabled;

   
    private SellerVO sellerVO;
    
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "productId")
	public Integer getProductId() {
		return productId;
	}

	public void setProductId(Integer productId) {
		this.productId = productId;
	}
	

	@Column(name = "productCoverImg" ,columnDefinition = "longblob")
	public byte[] getProductCoverImg() {
		return productCoverImg;
	}

	public void setProductCoverImg(byte[] productCoverImg) {
		this.productCoverImg = productCoverImg;
	}
	
    @Column(name = "productName")
	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}
	
	@Column(name = "productPrice")
	public BigDecimal getProductPrice() {
		return productPrice;
	}

	public void setProductPrice(BigDecimal productPrice) {
		this.productPrice = productPrice;
	}

	@Column(name = "productStockQuantity")
	public Integer getProductStockQuantity() {
		return productStockQuantity;
	}

	public void setProductStockQuantity(Integer productStockQuantity) {
		this.productStockQuantity = productStockQuantity;
	}

	@Column(name = "productDetails")
	public String getProductDetails() {
		return productDetails;
	}

	public void setProductDetails(String productDetails) {
		this.productDetails = productDetails;
	}

	@Column(name = "productStatus")
	public String getProductStatus() {
		return productStatus;
	}

	public void setProductStatus(String productStatus) {
		this.productStatus = productStatus;
	}

	@Column(name = "productCreationTime")
	public Date getProductCreationTime() {
		return productCreationTime;
	}

	public void setProductCreationTime(Date productCreationTime) {
		this.productCreationTime = productCreationTime;
	}

	@Column(name = "totalStars")
	public Integer getTotalStars() {
		return totalStars;
	}

	public void setTotalStars(Integer totalStars) {
		this.totalStars = totalStars;
	}

	@Column(name = "totalReviews")
	public Integer getTotalReviews() {
		return totalReviews;
	}

	public void setTotalReviews(Integer totalReviews) {
		this.totalReviews = totalReviews;
	}

	@Column(name = "productSort")
	public Integer getProductSort() {
		return productSort;
	}

	public void setProductSort(Integer productSort) {
		this.productSort = productSort;
	}

    @Column(name = "isEnabled")
	public Boolean getIsEnabled() {
		return isEnabled;
	}

	public void setIsEnabled(Boolean isEnabled) {
		this.isEnabled = isEnabled;
	}

	@ManyToOne
	@JoinColumn(name = "sellerId", referencedColumnName = "sellerId")
	public SellerVO getSellerVO() {
	    return sellerVO;
	}

	public void setSellerVO(SellerVO sellerVO) {
	    this.sellerVO = sellerVO;
	}

	@Override
	public String toString() {
		return "ProductVO [productId=" + productId + ", productCoverImg="
				+ productCoverImg != null && productCoverImg.length > 0 ? "Has Pic" : "No Pic"+ ", productName=" + productName + ", productPrice=" + productPrice
				+ ", productStockQuantity=" + productStockQuantity + ", productDetails=" + productDetails
				+ ", productStatus=" + productStatus + ", productCreationTime=" + productCreationTime + ", totalStars="
				+ totalStars + ", totalReviews=" + totalReviews + ", productSort=" + productSort + ", isEnabled="
				+ isEnabled + ", seller=" + sellerVO + "]";
	}

    // Add getters and setters

    // You can also add additional methods or annotations as needed

}
