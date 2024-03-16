package com.petdraw.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "petdraw") // 指定對應的資料庫表格名稱
public class PetDrawVO implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	
	private Integer petDrawId;
	private Integer memberId;
    private Integer memberPairId;
	private Boolean isMemberLike;
	// 命名問題 除開發者外 其餘人不知此為何時間
	private Date memberResTime;
	private Date memberPairResTime;
	private Boolean isMemberPairLike;
	private Date petDrawTime;
	private Double petDrawLog;
	private Double petDrawLat;

	public PetDrawVO() { // 必需有一個不傳參數建構子(JavaBean基本知識)
	}

	@Id
	@Column(name = "petDrawId")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer getpetDrawId() {
		return this.petDrawId;
	}

	public void setpetDrawId(Integer petDrawId) {
		this.petDrawId = petDrawId;
	}

	//@ManyToOne
    //@JoinColumn(name = "memberId", referencedColumnName = "memberId")
    public Integer getMemberId() {
        return memberId;
    }

    public void setMemberId(Integer memberId) {
        this.memberId = memberId;
    }

    

    public Integer getMemberPairId() {
        return memberPairId;
    }

    public void setMemberPairId(Integer memberPairId) {
        this.memberPairId = memberPairId;
    }

	@Column(name = "isMemberLike")
	public Boolean getIsMemberLike() {
		return isMemberLike;
	}

	public void setIsMemberLike(Boolean isMemberLike) {
		this.isMemberLike = isMemberLike;
	}

	@Column(name = "memberResTime")
	 @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getMemberResTime() {
		return memberResTime;
	}

	public void setMemberResTime(Date memberResTime) {
		this.memberResTime = memberResTime;
	}

	@Column(name = "memberPairResTime")
	 @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getMemberPairResTime() {
		return memberPairResTime;
	}

	public void setMemberPairResTime(Date memberPairResTime) {
		this.memberPairResTime = memberPairResTime;
	}

	@Column(name = "isMemberPairLike")
	public Boolean getIsMemberPairLike() {
		return isMemberPairLike;
	}

	public void setIsMemberPairLike(Boolean isMemberPairLike) {
		this.isMemberPairLike = isMemberPairLike;
	}

	@Column(name = "petDrawTime")
	 @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getPetDrawTime() {
		return petDrawTime;
	}

	public void setPetDrawTime(Date petDrawTime) {
		this.petDrawTime = petDrawTime;
	}

	@Column(name = "petDrawLog")
	public Double getPetDrawLog() {
		return petDrawLog;
	}

	public void setPetDrawLog(Double petDrawLog) {
		this.petDrawLog = petDrawLog;
	}

	@Column(name = "petDrawLat")
	public Double getPetDrawLat() {
		return petDrawLat;
	}

	public void setPetDrawLat(Double petDrawLat) {
		this.petDrawLat = petDrawLat;
	}
}
