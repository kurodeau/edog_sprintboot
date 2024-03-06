package com.product.model;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

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
    private BigDecimal productPrice;
    private Integer productStockQuantity;   
    private Integer productSoldQuantity;
	private String productDetails;    
    private String productStatus;
    private Timestamp productCreationTime; 
    private Integer totalStars;    
    private Integer totalReviews;
    private ProductSortVO productSortVO;
    private Boolean isEnabled;   
    private SellerVO sellerVO;
    
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

	@Column(name = "productCreationTime" , updatable = false)
	public Timestamp getProductCreationTime() {
		return productCreationTime;
	}

	public void setProductCreationTime(Timestamp productCreationTime) {
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

	@ManyToOne
	@JoinColumn(name = "sellerId", referencedColumnName = "sellerId")
	public SellerVO getSellerVO() {
	    return sellerVO;
	}

	public void setSellerVO(SellerVO sellerVO) {
	    this.sellerVO = sellerVO;
	}




	
	

    // Add getters and setters

    // You can also add additional methods or annotations as needed

}
