package com.product.model;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Arrays;
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

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.orderdetails.model.OrderDetailsVO;
import com.productSort.model.ProductSortVO;
import com.seller.entity.SellerVO;



@Entity
@Table(name = "product")  // Replace "your_table_name" with the actual table name
public class ProductVO implements Serializable{
	public static final Integer MAX_IMAGE_SIZE =  10 * 1024 * 1024;
//	public static final Integer MAX_PRODUCT_SORT =  ProductSortEnum.values().length;


	
	private Integer productId;   
    private byte[] productCoverImg;
    private String productName;    
    private BigDecimal price;
    private Integer productStockQuantity;   
    private Integer productSoldQuantity;
	private String productDetails;    
    private String productStatus;
    private Timestamp productCreationTime; 
    private Integer ratings;    
    private Integer totalReviews;
    private ProductSortVO productSortVO;
    private Boolean isEnabled;   
    private SellerVO sellerVO;
    private String animalType;	
    
    
    
    @Column(name = "animalType")
    public String getAnimalType() {
		return animalType;
	}

	public void setAnimalType(String animalType) {
		this.animalType = animalType;
	}

	//OnetoMany需要加上
    private Set<ProductImgVO> productImgVO = new HashSet<ProductImgVO>();
    
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "productVO")
	public Set<ProductImgVO> getProductImgVO() {
	    return productImgVO;
	}
	
	public void setProductImgVO(Set<ProductImgVO> productImgVO) {
		this.productImgVO = productImgVO;
	}
   
	   
    
	 @Column(name = "productSoldQuantity")
    public Integer getProductSoldQuantity() {
		return productSoldQuantity;
	}
    

	public void setProductSoldQuantity(Integer productSoldQuantity) {
		this.productSoldQuantity = productSoldQuantity;
	}
	
	
	
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
	
	@Column(name = "price")
	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
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

	@Column(name = "productCreationTime" , updatable = false)
	public Timestamp getProductCreationTime() {
		return productCreationTime;
	}

	public void setProductCreationTime(Timestamp productCreationTime) {
		this.productCreationTime = productCreationTime;
	}

	@Column(name = "Ratings")
	public Integer getRatings() {
		return ratings;
	}

	public void setRatings(Integer ratings) {
		this.ratings = ratings;
	}

	@Column(name = "totalReviews")
	public Integer getTotalReviews() {
		return totalReviews;
	}

	public void setTotalReviews(Integer totalReviews) {
		this.totalReviews = totalReviews;
	}

	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "productsortNo", referencedColumnName = "productsortNo")	
	public ProductSortVO getProductSortVO() {
		return productSortVO;
	}

	public void setProductSortVO(ProductSortVO productSortVO) {
		this.productSortVO = productSortVO;
	}

    @Column(name = "isEnabled")
	public Boolean getIsEnabled() {
		return isEnabled;
	}

	public void setIsEnabled(Boolean isEnabled) {
		this.isEnabled = isEnabled;
	}

	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "sellerId", referencedColumnName = "sellerId")
	public SellerVO getSellerVO() {
	    return sellerVO;
	}

	public void setSellerVO(SellerVO sellerVO) {
	    this.sellerVO = sellerVO;
	}

	
//////////////商品與訂單明細的關聯///////////////////////
private Set<OrderDetailsVO> orderDetailss = new HashSet<OrderDetailsVO>();

@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.EAGER, mappedBy="productVO")
@OrderBy("orderId asc")
public Set<OrderDetailsVO> getOrderDetailss() {
return this.orderDetailss;
}

public void setOrderDetailss(Set<OrderDetailsVO> orderDetailss) {
this.orderDetailss = orderDetailss;
}


    // Add getters and setters

    // You can also add additional methods or annotations as needed

	@Override
	public String toString() {
		return "ProductVO [productId=" + productId + ", productCoverImg=" + Arrays.toString(productCoverImg)
				+ ", productName=" + productName + ", price=" + price + ", productStockQuantity=" + productStockQuantity
				+ ", productSoldQuantity=" + productSoldQuantity + ", productDetails=" + productDetails
				+ ", productStatus=" + productStatus + ", productCreationTime=" + productCreationTime + ", ratings="
				+ ratings + ", totalReviews=" + totalReviews + ", productSortVO=" + productSortVO + ", isEnabled="
				+ isEnabled + ", sellerVO=" + sellerVO + ", animalType=" + animalType + ", productImgVO=" + productImgVO
				+ "]";
	}
    
    
}
