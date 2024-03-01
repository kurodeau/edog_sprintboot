package com.seller.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;

import com.sellerLv.entity.SellerLvVO;
import com.validator.MyZeroValidator;

@Entity
@Table(name = "seller")
public class SellerVO implements java.io.Serializable {
	public SellerVO() {
		super();
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "sellerId", updatable = false)
	private Integer sellerId;

	@ManyToOne
	@JoinColumn(name = "sellerLvId", referencedColumnName = "sellerLvId")
	private SellerLvVO sellerLvId;

	public SellerLvVO getSellerLvId() {
		return sellerLvId;
	}

	public void setSellerLvId(SellerLvVO sellerLvId) {
		this.sellerLvId = sellerLvId;
	}

	@Column(name = "sellerEmail")
	private String sellerEmail;

	@Column(name = "sellerCompany")
	private String sellerCompany;

	@Column(name = "sellerTaxId", columnDefinition = "char(8)")
	private String sellerTaxId;

	@Column(name = "sellerCapital")
	private Integer sellerCapital;

	@Column(name = "sellerContact")
	private String sellerContact;

	@Column(name = "sellerCompanyPhone")
	private String sellerCompanyPhone;

	@Column(name = "sellerCompanyExtension")
	private String sellerCompanyExtension;

	@Column(name = "sellerMobile")
	private String sellerMobile;

	@Column(name = "sellerAddress")
	private String sellerAddress;

	@Column(name = "sellerPassword")
	private String sellerPassword;

	@Column(name = "sellerBankAccount")
	private String sellerBankAccount;

	@Column(name = "sellerBankCode")
	private String sellerBankCode;

	@Column(name = "sellerBankAccountNumber")
	private String sellerBankAccountNumber;

	@Column(name = "sellerCreateTime")
	@Temporal(TemporalType.TIMESTAMP)
	private Date sellerCreateTime = new Date();

	@Column(name = "isConfirm")
	private Boolean isConfirm = false;

	public Integer getSellerId() {
		return sellerId;
	}

	public void setSellerId(Integer sellerId) {
		this.sellerId = sellerId;
	}

	@NotEmpty(message = "信箱不能留白")
	@Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}", message = "信箱格式錯誤")
	public String getSellerEmail() {
		return sellerEmail;
	}

	public void setSellerEmail(String sellerEmail) {
		this.sellerEmail = sellerEmail;
	}

	@NotEmpty(message = "公司名稱: 請勿空白")
	@Size(min = 2, max = 50, message = "公司名稱: 長度必需在{min}到{max}之間")
	public String getSellerCompany() {
		return sellerCompany;
	}

	public void setSellerCompany(String sellerCompany) {
		this.sellerCompany = sellerCompany;
	}

	@NotEmpty(message = "公司統編請勿空白")
	@Size(max = 5, message = "不能超過{max}碼")
	public String getSellerTaxId() {
		return sellerTaxId;
	}

	public void setSellerTaxId(String sellerTaxId) {
		this.sellerTaxId = sellerTaxId;
	}

	@MyZeroValidator(message = "資本額必須為正數")
	public Integer getSellerCapital() {
		return sellerCapital;
	}

	public void setSellerCapital(Integer sellerCapital) {
		this.sellerCapital = sellerCapital;
	}

	@NotEmpty(message = "公司負責人請勿空白")
	public String getSellerContact() {
		return sellerContact;
	}

	public void setSellerContact(String sellerContact) {
		this.sellerContact = sellerContact;
	}

	@NotEmpty(message = "公司電話請勿空白")
	@Size(max = 10, message = "公司電話請勿超過十碼")
	public String getSellerCompanyPhone() {
		return sellerCompanyPhone;
	}

	public void setSellerCompanyPhone(String sellerCompanyPhone) {
		this.sellerCompanyPhone = sellerCompanyPhone;
	}

	@NotEmpty(message = "公司分機請勿空白")
	@Size(max = 10, message = "公司分機請勿超過十碼")
	public String getSellerCompanyExtension() {
		return sellerCompanyExtension;
	}

	public void setSellerCompanyExtension(String sellerCompanyExtension) {
		this.sellerCompanyExtension = sellerCompanyExtension;
	}

	@NotEmpty(message = "手機請勿空白")
	@Pattern(regexp = "^09\\d\\d[0-9]{6}$", message = "手機格式錯誤")
	public String getSellerMobile() {
		return sellerMobile;
	}

	public void setSellerMobile(String sellerMobile) {
		this.sellerMobile = sellerMobile;
	}

	public String getSellerAddress() {
		return sellerAddress;
	}

	public void setSellerAddress(String sellerAddress) {
		this.sellerAddress = sellerAddress;
	}

	@NotEmpty(message = "密碼請勿空白")
	@Size(min = 8, message = "密碼長度需至少為8位")
	@Pattern(regexp = ".*[A-Z].*", message = "密碼需包含至少一個大寫字母")
	@Pattern(regexp = ".*[a-z].*", message = "密碼需包含至少一個小寫字母")
	@Pattern(regexp = ".*\\d.*", message = "密碼需包含至少一個數字")
	public String getSellerPassword() {
		return sellerPassword;
	}

	public void setSellerPassword(String sellerPassword) {
		this.sellerPassword = sellerPassword;
	}

	@NotEmpty(message = "銀行帳號請勿留白")
	@Pattern(regexp = "\\d{5,14}", message = "銀行帳號應為數字")
	public String getSellerBankAccount() {
		return sellerBankAccount;
	}

	public void setSellerBankAccount(String sellerBankAccount) {
		this.sellerBankAccount = sellerBankAccount;
	}

	@Pattern(regexp = "\\d{3}", message = "銀行代碼應為數字")
	public String getSellerBankCode() {
		return sellerBankCode;
	}

	public void setSellerBankCode(String sellerBankCode) {
		this.sellerBankCode = sellerBankCode;
	}

	@Pattern(regexp = "\\d{5,14}", message = "銀行帳戶應為數字")
	public String getSellerBankAccountNumber() {
		return sellerBankAccountNumber;
	}

	public void setSellerBankAccountNumber(String sellerBankAccountNumber) {
		this.sellerBankAccountNumber = sellerBankAccountNumber;
	}

	// @NotNull(message="雇用日期: 請勿空白")
	// @Future(message="日期必須是在今日(不含)之後")
	// @Past(message="日期必須是在今日(含)之前")
	// @DateTimeFormat(pattern="yyyy-MM-dd")
	// @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@NotNull(message = "創建日期: 請勿空白")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	public Date getSellerCreateTime() {
		return sellerCreateTime;
	}

	public void setSellerCreateTime(Date sellerCreateTime) {
		this.sellerCreateTime = sellerCreateTime;
	}

	@NotNull(message = "是否確認: 請勿空白")
	public Boolean getIsConfirm() {
		return isConfirm;
	}

	public void setIsConfirm(Boolean isConfirm) {
		this.isConfirm = isConfirm;
	}

	@Override
	public String toString() {
		return "SellerVO [sellerId=" + sellerId + ", sellerLvId=" + sellerLvId + ", sellerEmail=" + sellerEmail
				+ ", sellerCompany=" + sellerCompany + ", sellerTaxId=" + sellerTaxId + ", sellerCapital="
				+ sellerCapital + ", sellerContact=" + sellerContact + ", sellerCompanyPhone=" + sellerCompanyPhone
				+ ", sellerCompanyExtension=" + sellerCompanyExtension + ", sellerMobile=" + sellerMobile
				+ ", sellerAddress=" + sellerAddress + ", sellerPassword=" + sellerPassword + ", sellerBankAccount="
				+ sellerBankAccount + ", sellerBankCode=" + sellerBankCode + ", sellerBankAccountNumber="
				+ sellerBankAccountNumber + ", sellerCreateTime=" + sellerCreateTime + ", isConfirm=" + isConfirm + "]";
	}

}
