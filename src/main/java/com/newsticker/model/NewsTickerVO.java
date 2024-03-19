package com.newsticker.model;

import java.io.IOException;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import javax.validation.constraints.DecimalMin;
import javax.validation.Valid;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.Future;
import javax.validation.constraints.Past;

//import org.hibernate.validator.constraints.NotEmpty;
import javax.validation.constraints.NotEmpty;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import com.validator.MyZeroValidator;

/*
 * 註1: classpath必須有javax.persistence-api-x.x.jar 
 * 註2: Annotation可以添加在屬性上，也可以添加在getXxx()方法之上
 */


@Entity  //要加上@Entity才能成為JPA的一個Entity類別
@Table(name = "newsTicker") //代表這個class是對應到資料庫的實體table，目前對應的table是EMP2 
public class NewsTickerVO implements java.io.Serializable {
	
	public NewsTickerVO() { //必需有一個不傳參數建構子(JavaBean基本知識)
	}
	
	private Integer newsTickerId;
	private String newsTickerContent;
	private Integer sort;
	private Date startTime;
	private Date endTime;
	private Boolean isDisplay = true;	

	@Id //@Id代表這個屬性是這個Entity的唯一識別屬性，並且對映到Table的主鍵 
	@GeneratedValue(strategy = GenerationType.IDENTITY) //@GeneratedValue的generator屬性指定要用哪個generator //【strategy的GenerationType, 有四種值: AUTO, IDENTITY, SEQUENCE, TABLE】 
	@Column(name = "newsTickerId")
	public Integer getNewsTickerId() {
		return this.newsTickerId;
	}

	public void setNewsTickerId(Integer newsTickerId) {
		this.newsTickerId = newsTickerId;
	}
	
	@NotEmpty(message = "走馬燈內容不能空白")
	@Column(name = "newsTickerContent")
	public String getNewsTickerContent() {
		return newsTickerContent;
	}

	public void setNewsTickerContent(String newsTickerContent) {
		this.newsTickerContent = newsTickerContent;
	}
	
	@MyZeroValidator(message = "顯示權重必須是正整數")
	@Column(name = "sort")
	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	@NotNull(message = "起始日期, 請勿空白")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@Column(name = "startTime")
	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}	

	@NotNull(message = "結束日期, 請勿空白")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@Column(name = "endTime")
	public Date getEndTime() {
		return endTime;
	}

	@Override
	public String toString() {
		return "NewsTickerVO [newsTickerId=" + newsTickerId + ", newsTickerContent=" + newsTickerContent + ", sort="
				+ sort + ", startTime=" + startTime + ", endTime=" + endTime + ", isDisplay=" + isDisplay + "]";
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	
	@Column(name = "isDisplay")
	public Boolean getIsDisplay() {
		return isDisplay;
	}

	public void setIsDisplay(Boolean isDisplay) {
		this.isDisplay = isDisplay;
	}
	

	













	
}
