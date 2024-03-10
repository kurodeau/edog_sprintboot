package com.buyer.entity;

import java.util.Arrays;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


/*
 * 註1: classpath必須有javax.persistence-api-x.x.jar 
 * 註2: Annotation可以添加在屬性上，也可以添加在getXxx()方法之上
 */

@Entity
@Table(name = "buyer")
public class BuyerVO implements java.io.Serializable{
	private static final long serialVersionUID = 1L;

	public BuyerVO() {
		super();
	};
	
	private Integer memberId;
	private String memberEmail; 
	private String thirdFrom;
	private String memberName;
	private String memberPhone;
	private String memberMobile;
	private Date memberBirthday; 
	private String memberPassword; 
	private String memberAddress; 
	private Boolean isMemberEmail; 
	private Date memberRegistrationTime; 
	private String petName; 
	private byte[] petImg; 
	private Date petImgUploadTime; 
	private String petVaccName1; 
	private Date petVaccTime1; 
	private String petVaccName2; 
	private Date petVaccTime2; 
	private Boolean isConfirm;


	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "memberId", updatable = false)
	public Integer getMemberId() {
		return memberId;
	}

	public void setMemberId(Integer memberId) {
		this.memberId = memberId;
	}

	@Column(name = "memberEmail")
	public String getMemberEmail() {
		return memberEmail;
	}

	public void setMemberEmail(String memberEmail) {
		this.memberEmail = memberEmail;
	}
	
	@Column(name = "thirdFrom")
	public String getThirdFrom() {
		return thirdFrom;
	}

	public void setThirdFrom(String thirdFrom) {
		this.thirdFrom = thirdFrom;
	}

	@Column(name = "memberName")
	public String getMemberName() {
		return memberName;
	}

	public void setMemberName(String memberName) {
		this.memberName = memberName;
	}

	@Column(name = "memberPhone")
	public String getMemberPhone() {
		return memberPhone;
	}

	public void setMemberPhone(String memberPhone) {
		this.memberPhone = memberPhone;
	}

	@Column(name = "memberMobile")
	public String getMemberMobile() {
		return memberMobile;
	}

	public void setMemberMobile(String memberMobile) {
		this.memberMobile = memberMobile;
	}

	@Column(name = "memberBirthday")
	@Temporal(TemporalType.TIMESTAMP)
	public Date getMemberBirthday() {
		return memberBirthday;
	}

	public void setMemberBirthday(Date memberBirthday) {
		this.memberBirthday = memberBirthday;
	}

	@Column(name = "memberPassword")
	public String getMemberPassword() {
		return memberPassword;
	}

	public void setMemberPassword(String memberPassword) {
		this.memberPassword = memberPassword;
	}

	@Column(name = "memberAddress")
	public String getMemberAddress() {
		return memberAddress;
	}

	public void setMemberAddress(String memberAddress) {
		this.memberAddress = memberAddress;
	}

	@Column(name = "isMemberEmail")
	public Boolean getIsMemberEmail() {
		return isMemberEmail;
	}

	public void setIsMemberEmail(Boolean isMemberEmail) {
		this.isMemberEmail = isMemberEmail;
	}

	@Column(name = "memberRegistrationTime")
	@Temporal(TemporalType.TIMESTAMP)
	public Date getMemberRegistrationTime() {
		return memberRegistrationTime;
	}

	public void setMemberRegistrationTime(Date memberRegistrationTime) {
		this.memberRegistrationTime = memberRegistrationTime;
	}

	@Column(name = "petName")
	public String getPetName() {
		return petName;
	}

	public void setPetName(String petName) {
		this.petName = petName;
	}

	@Column(name = "petImg", columnDefinition = "LONGBLOB" )
	public byte[] getPetImg() {
		return petImg;
	}

	public void setPetImg(byte[] petImg) {
		this.petImg = petImg;
	}

	@Column(name = "petImgUploadTime")
	@Temporal(TemporalType.TIMESTAMP)
	public Date getPetImgUploadTime() {
		return petImgUploadTime;
	}

	public void setPetImgUploadTime(Date petImgUploadTime) {
		this.petImgUploadTime = petImgUploadTime;
	}

	@Column(name = "petVaccName1")
	public String getPetVaccName1() {
		return petVaccName1;
	}

	public void setPetVaccName1(String petVaccName1) {
		this.petVaccName1 = petVaccName1;
	}

	@Column(name = "petVaccTime1")
	@Temporal(TemporalType.TIMESTAMP)
	public Date getPetVaccTime1() {
		return petVaccTime1;
	}

	public void setPetVaccTime1(Date petVaccTime1) {
		this.petVaccTime1 = petVaccTime1;
	}

	@Column(name = "petVaccName2")
	public String getPetVaccName2() {
		return petVaccName2;
	}

	public void setPetVaccName2(String petVaccName2) {
		this.petVaccName2 = petVaccName2;
	}

	@Column(name = "petVaccTime2")
	@Temporal(TemporalType.TIMESTAMP)
	public Date getPetVaccTime2() {
		return petVaccTime2;
	}

	public void setPetVaccTime2(Date petVaccTime2) {
		this.petVaccTime2 = petVaccTime2;
	}

	@Column(name = "isConfirm")
	public Boolean getIsConfirm() {
		return isConfirm;
	}

	public void setIsConfirm(Boolean isConfirm) {
		this.isConfirm = isConfirm;
	}
		
	@Override
	public String toString() {
		return "BuyerVO [memberId=" + memberId + ", memberEmail=" + memberEmail + ", thirdFrom=" + thirdFrom
				+ ", memberName=" + memberName + ", memberPhone=" + memberPhone + ", memberMobile=" + memberMobile
				+ ", memberBirthday=" + memberBirthday + ", memberPassword=" + memberPassword + ", memberAddress="
				+ memberAddress + ", isMemberEmail=" + isMemberEmail + ", memberRegistrationTime="
				+ memberRegistrationTime + ", petName=" + petName + ", petImg=" + Arrays.toString(petImg)
				+ ", petImgUploadTime=" + petImgUploadTime + ", petVaccName1=" + petVaccName1 + ", petVaccTime1="
				+ petVaccTime1 + ", petVaccName2=" + petVaccName2 + ", petVaccTime2=" + petVaccTime2 + ", isConfirm="
				+ isConfirm + ", getClass()=" + getClass() + ", hashCode()=" + hashCode() + ", toString()="
				+ super.toString() + "]";
	}

}
