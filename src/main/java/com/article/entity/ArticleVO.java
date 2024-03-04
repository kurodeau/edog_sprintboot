package com.article.entity;

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

import com.articleType.entity.ArticleTypeVO;
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
@Table(name = "article")
public class ArticleVO implements java.io.Serializable {
    private static final long serialVersionUID = 1L;
    
    private Integer articleId;
    private BuyerVO buyerVO;
    private String articleTitle;
    private String articleContent;
    private Integer articleLike;
    private Integer articleComment;
    private Integer articleShare; 
    private Date artUpdateTime; 
    private ArticleTypeVO articleTypeVO;
    private Boolean isEnabled;
    private byte[] upFiles;

    public ArticleVO() {
    }

    @Id 
    @Column(name = "articleId")
    @GeneratedValue(strategy = GenerationType.IDENTITY) 
    public Integer getArticleId() {
        return this.articleId;
    }

    public void setArticleId(Integer articleId) {
        this.articleId = articleId;
    }

    @ManyToOne
    @JoinColumn(name = "memberId", referencedColumnName = "memberId")
    public BuyerVO getBuyerVO() {
        return this.buyerVO;
    }

    public void setBuyerVO(BuyerVO buyerVO) {
        this.buyerVO = buyerVO;
    }
    
    @ManyToOne
    @JoinColumn(name = "articleSort", referencedColumnName = "articleTypeId")
    public ArticleTypeVO getArticleTypeVO() {
    	return this.articleTypeVO;
    }
    
    public void setArticleTypeVO(ArticleTypeVO articleTypeVO) {
    	this.articleTypeVO = articleTypeVO;
    }

    @Column(name = "articleTitle")
    @NotEmpty(message="文章標題: 請勿空白")
    public String getArticleTitle() {
        return articleTitle;
    }

    public void setArticleTitle(String articleTitle) {
        this.articleTitle = articleTitle;
    }

    @Column(name = "articleContent")
    @NotEmpty(message="文章內容: 請勿空白")
    public String getArticleContent() {
        return articleContent;
    }
    
    public void setArticleContent(String articleContent) {
        this.articleContent = articleContent;
    }

    @Column(name = "artUpdateTime")
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getArtUpdateTime() {
		return this.artUpdateTime;
	}

    public void setArtUpdateTime(Date artUpdateTime) {
    	this.artUpdateTime = artUpdateTime;
    }

    @Column(name = "articleLike")
    public Integer getArticleLike() {
        return this.articleLike;
    }

    public void setArticleLike(Integer articleLike) {
        this.articleLike = articleLike;
    }

    @Column(name = "articleComment")
    public Integer getArticleComment() {
        return this.articleComment;
    }

    public void setArticleComment(Integer articleComment) {
        this.articleComment = articleComment;
    }
    
    @Column(name = "articleShare")
    public Integer getArticleShare() {
        return this.articleShare;
    }
    
    public void setArticleShare(Integer articleShare) {
        this.articleShare = articleShare;
    }

    public Boolean getIsEnabled() {
        return this.isEnabled;
    }
    
    public void setIsEnabled(Boolean isEnabled) {
        this.isEnabled = isEnabled;
    }  
    
    @Column(name = "upFiles", columnDefinition = "longblob")
//	@NotEmpty(message="員工照片: 請上傳照片") --> 由EmpController.java 第60行處理錯誤信息
	public byte[] getUpFiles() {
		return upFiles;
	}
	public void setUpFiles(byte[] upFiles) {
		this.upFiles = upFiles;
	}
}
