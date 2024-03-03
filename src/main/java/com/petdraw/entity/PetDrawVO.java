package com.petdraw.entity;


import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.buyer.entity.BuyerVO;



@Entity
@Table(name = "petdraw") // 指定對應的資料庫表格名稱
public class PetDrawVO implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	
	private Integer petDrawId;
	private BuyerVO memberMain;
	private BuyerVO memberPair;
	private Boolean isMemberLike;
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
	private Integer getpetDrawId() {
		return this.petDrawId;
	}
	public void setpetDrawId(Integer petDrawId) {
		this.petDrawId = petDrawId;
	}
	
	

    @ManyToOne
    @JoinColumn(name = "memberId")
    public BuyerVO getMemberMain() {
        return memberMain;
    }

    public void setMemberMain(BuyerVO memberMain) {
        this.memberMain = memberMain;
    }

    @ManyToOne
    @JoinColumn(name = "memberpairId")
    public BuyerVO getMemberPair() {
        return memberPair;
    }

    public void setMemberPair(BuyerVO memberPair) {
        this.memberPair = memberPair;
    }


	  @Column(name = "isMemberLike")
	    private Boolean getIsMemberLike() {
	        return isMemberLike;
	    }

	    public void setIsMemberLike(Boolean isMemberLike) {
	        this.isMemberLike = isMemberLike;
	    }

	@Column(name = "memberResTime")
	public Date getMemberResTime() {
        return memberResTime;
    }

    public void setMemberResTime(Date memberResTime) {
        this.memberResTime = memberResTime;
    }

	@Column(name = "memberPairResTime")
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
