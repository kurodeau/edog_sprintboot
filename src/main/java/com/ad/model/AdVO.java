package com.ad.model;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.Arrays;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Future;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.URL;
import org.springframework.format.annotation.DateTimeFormat;

import com.seller.entity.SellerVO;




@Entity
@Table(name="ad")
public class AdVO implements Serializable {
	
	

	private Integer adId;	
	private SellerVO sellerVO;	
	private byte[] adImg;	
	private Timestamp adImgUploadTime;
	private String adName;
	private String adUrl;
	private Date adStartTime;	
	private Date adEndTime;	
	private Integer adLv;	
	private String adMemo;	
	private Timestamp adCreateTime;
	private String adStatus;
	private Boolean isEnabled;
	
	
	public AdVO() {
		
	}	
	
	public AdVO(Integer adId, byte[] adImg, String adName, Date adStartTime, Date adEndTime, String adStatus) {
		super();
		this.adId = adId;
		this.adImg = adImg;
		this.adName = adName;
		this.adStartTime = adStartTime;
		this.adEndTime = adEndTime;
		this.adStatus = adStatus;
	}




	public AdVO( SellerVO sellerVO, byte[] adImg, Timestamp adImgUploadTime, String adName, String adUrl,
			Date adStartTime, Date adEndTime, Integer adLv, String adMemo, String adStatus,
			Timestamp adCreateTime, Boolean isEnabled) {
		super();
	
		this.sellerVO = sellerVO;
		this.adImg = adImg;
		this.adImgUploadTime = adImgUploadTime;
		this.adName = adName;
		this.adUrl = adUrl;
		this.adStartTime = adStartTime;
		this.adEndTime = adEndTime;
		this.adLv = adLv;
		this.adMemo = adMemo;
		this.adStatus = adStatus;
		this.adCreateTime = adCreateTime;
		this.isEnabled = isEnabled;
	}

	
	
	
	
	
	
	
	
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="adId" , updatable = false)
	public Integer getAdId() {
		return adId;
	}


	@ManyToOne
	@JoinColumn(name = "sellerId", referencedColumnName = "sellerId")
	public SellerVO getSellerVO() {
		return sellerVO;
	}




	public void setSellerVO(SellerVO sellerVO) {
		this.sellerVO = sellerVO;
	}


	
	@Column(name="adImg" ,columnDefinition = "longblob")
	public byte[] getAdImg() {
		return adImg;
	}




	public void setAdImg(byte[] adImg) {
		this.adImg = adImg;
	}



	@Column(name= "adImgUploadTime" , updatable = false)
	public Timestamp getAdImgUploadTime() {
		return adImgUploadTime;
	}




	public void setAdImgUploadTime(Timestamp adImgUploadTime) {
		this.adImgUploadTime = adImgUploadTime;
	}


	
	@Column(name = "adName")
	@NotEmpty(message="請填寫廣告名稱")
	@Pattern(regexp = "^[(\u4e00-\u9fa5)(a-zA-Z0-9_)]{1,30}$", message = "廣告名稱: 只能是中、英文字母、數字和_ , 且長度必需在1到20之間")	
	public String getAdName() {
		return adName;
	}




	public void setAdName(String adName) {
		this.adName = adName;
	}



	@Column(name = "adUrl")
	@NotEmpty(message="請填寫廣告產品的網址")
	@URL
	public String getAdUrl() {
		return adUrl;
	}




	public void setAdUrl(String adUrl) {
		this.adUrl = adUrl;
	}


	
	@Column(name = "adStartTime")
	@NotNull(message = "請填寫廣告開始時間")	
	public Date getAdStartTime() {
		return adStartTime;
	}




	public void setAdStartTime(Date adStartTime) {
		this.adStartTime = adStartTime;
	}



	@Column(name = "adEndTime")
	@NotNull(message = "請填寫廣告結束時間")
	public Date getAdEndTime() {
		return adEndTime;
	}




	public void setAdEndTime(Date adEndTime) {
		this.adEndTime = adEndTime;
	}



	@Column(name ="adLv")
	@NotNull(message = "請選擇廣告類型")
	public Integer getAdLv() {
		return adLv;
	}




	public void setAdLv(Integer adLv) {
		this.adLv = adLv;
	}


	
	@Column(name="adMemo")
	@NotNull(message="請填寫廣告備註")
	public String getAdMemo() {
		return adMemo;
	}




	public void setAdMemo(String adMemo) {
		this.adMemo = adMemo;
	}







	@Column(name = "adStatus")
	public String getAdStatus() {
		return adStatus;
	}




	public void setAdStatus(String adStatus) {
		this.adStatus = adStatus;
	}



	@Column(name = "adCreateTime" , updatable = false)
	public Timestamp getAdCreateTime() {
		return adCreateTime;
	}




	public void setAdCreateTime(Timestamp adCreateTime) {
		this.adCreateTime = adCreateTime;
	}



	@Column(name ="isEnabled")
	public Boolean getIsEnabled() {
		return isEnabled;
	}




	public void setIsEnabled(Boolean isEnabled) {
		this.isEnabled = isEnabled;
	}




	public void setAdId(Integer adId) {
		this.adId = adId;
	}


	
	@Override
	public String toString() {
		return "AdVO [adId=" + adId +" adImg=" + Arrays.toString(adImg)
				+ ", adImgUploadTime=" + adImgUploadTime + ", adName=" + adName + ", adUrl=" + adUrl + ", adStartTime="
				+ adStartTime + ", adEndTime=" + adEndTime + ", adLv=" + adLv + ", adMemo=" + adMemo + ", adStatus="
				+ adStatus + ", adCreateTime=" + adCreateTime + ", isEnabled=" + isEnabled + "]";
	}
	
	

}
