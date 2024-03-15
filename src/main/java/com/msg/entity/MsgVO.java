package com.msg.entity;

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

import com.msgType.entity.MsgTypeVO;
import com.reply.entity.ReplyVO;
import com.report.entity.ReportVO;
import com.article.entity.ArticleVO;
import com.buyer.entity.BuyerVO;

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
@Table(name = "msg")
public class MsgVO implements java.io.Serializable {
    private static final long serialVersionUID = 1L;
    
    private Integer msgId;
    private BuyerVO buyerVO;
    private ArticleVO articleVO;
    private ReplyVO replyVO;
    private ReportVO reportVO;
    private MsgTypeVO msgTypeVO;
    private Date msgTime; 
    private Boolean isRead;
    private Boolean isEnabled;

    public MsgVO() {
    }

    @Id 
    @Column(name = "msgId")
    @GeneratedValue(strategy = GenerationType.IDENTITY) 
    public Integer getMsgId() {
        return this.msgId;
    }

    public void setMsgId(Integer msgId) {
        this.msgId = msgId;
    }

    @ManyToOne
    @JoinColumn(name = "msgTypeId", referencedColumnName = "msgTypeId")
    public MsgTypeVO getMsgTypeVO() {
        return this.msgTypeVO;
    }

    public void setMsgTypeVO(MsgTypeVO msgTypeVO) {
        this.msgTypeVO = msgTypeVO;
    }
    
    @Column(name = "msgTime")
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getMsgTime() {
    	return this.msgTime;
    }
    
    public void setMsgTime(Date msgTime) {
    	this.msgTime = msgTime;
    }
    

    @ManyToOne
    @JoinColumn(name = "reportId", referencedColumnName = "reportId")
    public ReportVO getReportVO() {
        return this.reportVO;
    }

    public void setReportVO(ReportVO reportVO) {
        this.reportVO = reportVO;
    }
    
    @ManyToOne
    @JoinColumn(name = "articleId", referencedColumnName = "articleId")
    public ArticleVO getArticleVO() {
    	return this.articleVO;
    }
    
    public void setArticleVO(ArticleVO articleVO) {
    	this.articleVO = articleVO;
    }
    @ManyToOne
    @JoinColumn(name = "replyId", referencedColumnName = "replyId")
    public ReplyVO getReplyVO() {
    	return this.replyVO;
    }
    
    public void setReplyVO(ReplyVO replyVO) {
    	this.replyVO = replyVO;
    }
    @ManyToOne
    @JoinColumn(name = "memberId", referencedColumnName = "memberId")
    public BuyerVO getBuyerVO() {
    	return this.buyerVO;
    }
    
    public void setBuyerVO(BuyerVO buyerVO) {
    	this.buyerVO = buyerVO;
    }

    
    public Boolean getIsRead() {
        return this.isRead;
    }
    
    public void setIsRead(Boolean isRead) {
        this.isRead = isRead;
    }  
    public Boolean getIsEnabled() {
    	return this.isEnabled;
    }
    
    public void setIsEnabled(Boolean isEnabled) {
    	this.isEnabled = isEnabled;
    }  
    
}
