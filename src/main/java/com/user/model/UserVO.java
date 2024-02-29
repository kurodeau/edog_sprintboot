package com.user.model;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Email;
//import org.hibernate.validator.constraints.NotEmpty;
import javax.validation.constraints.NotEmpty;

//import com.dept.model.DeptVO;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;


	@Entity // 要加上@Entity才能成為JPA的一個Entity類別
	@Table(name = "userInformation") // 代表這個class是對應到資料庫的實體table，目前對應的table是userInformation
	public class UserVO implements java.io.Serializable {
		private static final long serialVersionUID = 1L; 
		private Integer userId;
		private String comName;
		private String comAccount;
		private String comPassword;
		private String comUniNumber;
		private String comAddress;
		private String comMail;
		private String comPhone;
		private String comBank;
		private Date comRegDate;
		private String comContactPerson;
		private String comContactPhone;
		private Integer comStat;
		private byte[] comImage1;
		private byte[] comImage2;
		private byte[] comImage3;
		private byte[] comImage4;
		private byte[] comAboutImage;
		private String comAboutContent;
		private Double comRatStars;
		private Integer comRatCount;
		private Integer comIndustry1;
		private Integer comIndustry2;
		private Integer comIndustry3;
//		private Integer comIsValid;
		public UserVO() { // 必需有一個不傳參數建構子(JavaBean基本知識)
		}
		
		@Id //@Id代表這個屬性是這個Entity的唯一識別屬性，並且對映到Table的主鍵 
		@Column(name = "userid")  //@Column指這個屬性是對應到資料庫Table的哪一個欄位   //【非必要，但當欄位名稱與屬性名稱不同時則一定要用】
		@GeneratedValue(strategy = GenerationType.IDENTITY) //@GeneratedValue的generator屬性指定要用哪個generator //【strategy的GenerationType, 有四種值: AUTO, IDENTITY, SEQUENCE, TABLE】
		public Integer getUserId() {
			return this.userId;
		}
		public void setUserId(Integer userId) {
			this.userId = userId;
		}
		@Column(name = "comName")
		@NotEmpty(message="公司名稱: 請勿空白")
		@Pattern(regexp = "^[(\u4e00-\u9fa5)(a-zA-Z0-9_)]{2,10}$", message = "公司名稱: 只能是中、英文字母、數字和_ , 且長度必需在2到10之間")
		public String getComName() {
			return this.comName;
		}
		public void setComName(String comName) {
			this.comName = comName;
		}		
		@Column(name = "comAccount")
		@NotEmpty(message="公司帳號: 請勿空白")
		@Size(min=2,max=50,message="公司帳號: 長度必需在{min}到{max}之間")
		public String getComAccount() {
			return this.comAccount;
		}
		public void setComAccount(String comAccount) {
			this.comAccount = comAccount;
		}
		@Column(name = "comPassword")
		@NotEmpty(message="公司密碼: 請勿空白")
		@Size(min=2,max=50,message="公司密碼: 長度必需在{min}到{max}之間")
		public String getComPassword() {
			return this.comPassword;
		}
		public void setComPassword(String comPassword) {
			this.comPassword = comPassword;
		}
		
		
		@Column(name = "comUniNumber")
		@NotEmpty(message="公司統編: 請勿空白")
		@Size(min=2,max=8,message="公司統編: 長度必需在{min}到{max}之間")
		public String getComUniNumber() {
			return this.comUniNumber;
		}
		public void setComUniNumber(String comUniNumber) {
			this.comUniNumber = comUniNumber;
		}
		@Column(name = "comAddress")
		@NotEmpty(message="公司地址: 請勿空白")
		@Size(min=2,max=255,message="公司地址: 長度必需在{min}到{max}之間")
		public String getComAddress() {
			return this.comAddress;
		}
		public void setComAddress(String comAddress) {
			this.comAddress = comAddress;
		}
		@Column(name = "comMail")
		@NotEmpty(message="公司信箱: 請勿空白")
//		@Email
//		@Length
		public String getComMail() {
			return this.comMail;
		}
		public void setComMail(String comMail) {
			this.comMail = comMail;
		}
		@Column(name = "comPhone")
		@NotEmpty(message="公司電話: 請勿空白")
		@Size(min=2,max=20,message="公司密碼: 長度必需在{min}到{max}之間")
		public String getComPhone() {
			return this.comPhone;
		}
		public void setComPhone(String comPhone) {
			this.comPhone = comPhone;
		}
		@Column(name = "comBank")
		@NotEmpty(message="銀行帳號: 請勿空白")
		@Size(min=2,max=50,message="銀行帳號: 長度必需在{min}到{max}之間")
		public String getComBank() {
			return this.comBank;
		}
		public void setComBank(String comBank) {
			this.comBank = comBank;
		}
		@Column(name = "comRegdate")
//		@NotNull(message="雇用日期: 請勿空白")	
//		@Future(message="日期必須是在今日(不含)之後")
//		@Past(message="日期必須是在今日(含)之前")
//		@DateTimeFormat(pattern="yyyy-MM-dd") 
//		@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss") 
		public Date getComRegDate() {
			return this.comRegDate;
		}
		public void setComRegDate(Date comRegDate) {
			this.comRegDate = comRegDate;
		}
		@Column(name = "comContactperson")
		@NotEmpty(message="公司聯絡人: 請勿空白")
		@Size(min=2,max=10,message="員工職位: 長度必需在{min}到{max}之間")
		public String getComContactPerson() {
			return this.comContactPerson;
		}
		public void setComContactPerson(String comContactPerson) {
			this.comContactPerson = comContactPerson;
		}
		@Column(name = "comContactphone")
		@NotEmpty(message="公司聯絡人電話: 請勿空白")
		@Size(min=10,max=50,message="員工職位: 長度必需在{min}到{max}之間")
		public String getComContactPhone() {
			return this.comContactPhone;
		}
		public void setComContactPhone(String comContactPhone) {
			this.comContactPhone = comContactPhone;
		}
		@Column(name = "comStat")
//		@NotEmpty(message="公司狀態: 請勿空白")
//		@Size(min=1,max=3,message="公司狀態: 長度必需在{min}到{max}之間")
		public Integer getComStat() {
			return this.comStat;
		}
		public void setComStat(Integer comStat) {
			this.comStat = comStat;
		}
		
		@Column(name = "comImage1")
//		@NotEmpty(message="特賣商品圖片: 請上傳圖片") --> 由UserController.java 第60行處理錯誤信息
		public byte[] getComImage1() {
			return comImage1;
		}
		public void setComImage1(byte[] comImage1) {
			this.comImage1 = comImage1;
		}
		@Column(name = "comImage2")
//		@NotEmpty(message="特賣商品圖片: 請上傳圖片") --> 由UserController.java 第60行處理錯誤信息
		public byte[] getComImage2() {
			return comImage2;
		}
		public void setComImage2(byte[] comImage2) {
			this.comImage2 = comImage2;
		}
		@Column(name = "comImage3")
//		@NotEmpty(message="特賣商品圖片: 請上傳圖片") --> 由UserController.java 第60行處理錯誤信息
		public byte[] getComImage3() {
			return comImage3;
		}
		public void setComImage3(byte[] comImage3) {
			this.comImage3 = comImage3;
		}
		@Column(name = "comImage4")
//		@NotEmpty(message="特賣商品圖片: 請上傳圖片") --> 由UserController.java 第60行處理錯誤信息
		public byte[] getComImage4() {
			return comImage4;
		}
		public void setComImage4(byte[] comImage4) {
			this.comImage4 = comImage4;
		}
		@Column(name = "comAboutimage")
//		@NotEmpty(message="關於我們圖片: 請上傳圖片") --> 由UserController.java 第60行處理錯誤信息
		public byte[] getComAboutImage() {
			return comAboutImage;
		}
		public void setComAboutImage(byte[] comAboutImage) {
			this.comAboutImage = comAboutImage;
		}
		@Column(name = "comAboutcontent")
//		@NotEmpty(message="關於我們敘述: 請勿空白")
//		@Size(min=2,max=255,message="關於我們敘述: 長度必需在{min}到{max}之間")
		public String getComAboutContent() {
			return this.comAboutContent;
		}
		public void setComAboutContent(String comAboutContent) {
			this.comAboutContent = comAboutContent;
		}
		@Column(name = "comRatstars")
//		@NotNull(message="評價總星數: 請勿空白")
//		@DecimalMin(value = "1.00", message = "評價總星數: 不能小於{value}")
//		@DecimalMax(value = "5.00", message = "評價總星數: 不能超過{value}")
		public Double getComRatStars() {
			return this.comRatStars;
		}
		public void setComRatStars(Double comRatStars) {
			this.comRatStars = comRatStars;
		}
		@Column(name = "comRatcount")
//		@NotEmpty(message="評價總筆數: 請勿空白")
//		@Size(min=2,max=255,message="關於我們敘述: 長度必需在{min}到{max}之間")
		public Integer getComRatCount() {
			return this.comRatCount;
		}
		public void setComRatCount(Integer comRatCount) {
			this.comRatCount = comRatCount;
		}
		@Column(name = "comIndustry1")
//		@NotEmpty(message="產業類別1: 請勿空白")
		public Integer getComIndustry1() {
			return this.comIndustry1;
		}
		public void setComIndustry1(Integer comIndustry1) {
			this.comIndustry1 = comIndustry1;
		}
		@Column(name = "comIndustry2")
//		@NotEmpty(message="產業類別2: 請勿空白")
		public Integer getComIndustry2() {
			return this.comIndustry2;
		}
		public void setComIndustry2(Integer comIndustry2) {
			this.comIndustry2 = comIndustry2;
		}
		@Column(name = "comIndustry3")
//		@NotEmpty(message="產業類別3: 請勿空白")
		public Integer getComIndustry3() {
			return this.comIndustry3;
		}
		public void setComIndustry3(Integer comIndustry3) {
			this.comIndustry3 = comIndustry3;
		}
//		@Column(name = "comIsValid")
//		@NotEmpty(message="公司帳號狀態: 請勿空白")
//		@Size(min=1,max=3,message="公司帳號狀態: 長度必需在{min}到{max}之間")
//		public Integer getComIsValid() {
//			return comIsValid;
//		}
//		
//		public void setComIsValid(Integer comIsValid) {
//			this.comIsValid = comIsValid;
//		}
		
		
	}
