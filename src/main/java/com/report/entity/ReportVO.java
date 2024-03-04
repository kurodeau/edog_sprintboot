package com.report.entity;

import java.util.Date;
import java.io.IOException;
//import java.sql.Date;

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

import com.article.entity.ArticleVO;
import com.buyer.entity.BuyerVO;
import com.reply.entity.ReplyVO;
import com.reportType.entity.ReportTypeVO;

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

/*
 * 註1: classpath必須有javax.persistence-api-x.x.jar 
 * 註2: Annotation可以添加在屬性上，也可以添加在getXxx()方法之上
 */

@Entity  
@Table(name = "report")
public class ReportVO implements java.io.Serializable {
    private static final long serialVersionUID = 1L;
    
    private Integer reportId;
    private BuyerVO buyerVO;
    private Integer reportTargetType;
    private ArticleVO articleVO;
    private ReplyVO replyVO;
    private ReportTypeVO reportTypeVO;
    private Date reportTime;
    private Integer reportState;
    private Date reportDealTime;
    
    public ReportVO() {
    }

    @Id 
    @Column(name = "reportId")
    @GeneratedValue(strategy = GenerationType.IDENTITY) 
    public Integer getReportId() {
        return this.reportId;
    }

    public void setReportId(Integer reportId) {
        this.reportId = reportId;
    }
    
    @ManyToOne
    @JoinColumn(name = "reportMemberId", referencedColumnName = "memberId")
    public BuyerVO getBuyerVO() {
        return this.buyerVO;
    }

    public void setBuyerVO(BuyerVO buyerVO) {
        this.buyerVO = buyerVO;
    }
    
    @Column(name = "reportTargetType")
    @NotNull(message="舉報目標類型: 請勿空白")
    public Integer getReportTargetType() {
        return reportTargetType;
    }

    public void setReportTargetType(Integer reportTargetType) {
        this.reportTargetType = reportTargetType;
    }
    
    @ManyToOne
    @JoinColumn(name = "articleId", referencedColumnName = "articleId")
    public ArticleVO getArticleVO() {
        return this.articleVO;
    }

    public void setArticleVO(ArticleVO articleVO) {
//    	if(articleVO != null) {
//            this.articleVO = articleVO;
//        }
        this.articleVO = articleVO;
    }
    
    @ManyToOne
    @JoinColumn(name = "replyId", referencedColumnName = "replyId")
    public ReplyVO getReplyVO() {
    	return this.replyVO;
    }
    
    public void setReplyVO(ReplyVO replyVO) {
//    	if(replyVO != null) {
//            this.replyVO = replyVO;
//        }
    	this.replyVO = replyVO;
    }
    @ManyToOne
    @JoinColumn(name = "reportTypeId", referencedColumnName = "reportTypeId")
    public ReportTypeVO getReportTypeVO() {
    	return this.reportTypeVO;
    }
    
    public void setReportTypeVO(ReportTypeVO reportTypeVO) {
    	this.reportTypeVO = reportTypeVO;
    }

   

    @Column(name = "reportTime")
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getReportTime() {
		return this.reportTime;
	}

    public void setReportTime(Date reportTime) {
    	this.reportTime = reportTime;
    }
    
    @Column(name = "reportState")
    @NotNull(message="舉報狀態: 請勿空白")
    public Integer getReportState() {
        return reportState;
    }

    public void setReportState(Integer reportState) {
        this.reportState = reportState;
    }
    
    @Column(name = "reportDealTime")
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getReportDealTime() {
    	return this.reportDealTime;
    }
    
    public void setReportDealTime(Date reportDealTime) {
    	this.reportDealTime = reportDealTime;
    }

    
}
