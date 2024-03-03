package com.sellerLv.entity;

import java.math.BigDecimal;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.seller.entity.SellerVO;

//com.sellerLv.entity.SellerLv
@Entity
@Table(name = "sellerLv")
public class SellerLvVO implements java.io.Serializable {
	private static final long serialVersionUID = 1L; 


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sellerLvId", updatable = false)
    private Integer sellerLvId;
    
	@OneToMany(mappedBy = "sellerLvId", cascade = CascadeType.ALL, orphanRemoval = true)
	@OrderBy("sellerId asc")
	 @JsonIgnore
	private Set<SellerVO> sellers;

    public Set<SellerVO> getSellers() {
		return sellers;
	}

	public void setSellers(Set<SellerVO> sellers) {
		this.sellers = sellers;
	}

	@Column(name = "lvName", length = 255)
    private String lvName;

    @Column(name = "platformCommission", precision = 3, scale = 2)
    private BigDecimal platformCommission;
    

	@Column(name = "adAllowType")
    private Integer adAllowType;

    @Column(name = "isExportGoldflow")
    private Boolean isExportGoldflow;

    @Column(name = "freightSub")
    private Integer freightSub;

    @Column(name = "returnSubPerMonth")
    private Integer returnSubPerMonth;

    @Column(name = "isShowPriority")
    private Boolean isShowPriority;

    @Column(name = "shelvesNumber")
    private Integer shelvesNumber;

    public Integer getSellerLvId() {
		return sellerLvId;
	}

	public void setSellerLvId(Integer sellerLvId) {
		this.sellerLvId = sellerLvId;
	}

	public String getLvName() {
		return lvName;
	}

	public void setLvName(String lvName) {
		this.lvName = lvName;
	}

	public BigDecimal getPlatformCommission() {
		return platformCommission;
	}

	public void setPlatformCommission(BigDecimal platformCommission) {
		this.platformCommission = platformCommission;
	}

	public Integer getAdAllowType() {
		return adAllowType;
	}

	public void setAdAllowType(Integer adAllowType) {
		this.adAllowType = adAllowType;
	}

	public Boolean getIsExportGoldflow() {
		return isExportGoldflow;
	}

	public void setIsExportGoldflow(Boolean isExportGoldflow) {
		this.isExportGoldflow = isExportGoldflow;
	}

	public Integer getFreightSub() {
		return freightSub;
	}

	public void setFreightSub(Integer freightSub) {
		this.freightSub = freightSub;
	}

	public Integer getReturnSubPerMonth() {
		return returnSubPerMonth;
	}

	public void setReturnSubPerMonth(Integer returnSubPerMonth) {
		this.returnSubPerMonth = returnSubPerMonth;
	}

	public Boolean getIsShowPriority() {
		return isShowPriority;
	}

	public void setIsShowPriority(Boolean isShowPriority) {
		this.isShowPriority = isShowPriority;
	}

	public Integer getShelvesNumber() {
		return shelvesNumber;
	}

	public void setShelvesNumber(Integer shelvesNumber) {
		this.shelvesNumber = shelvesNumber;
	}


	public SellerLvVO() {
		super();
	}


}
