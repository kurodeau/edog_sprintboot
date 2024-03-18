package com.articleLike.entity;

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
@Table(name = "articleLike")
public class ArticleLikeVO implements java.io.Serializable {
    private static final long serialVersionUID = 1L;
    
    private Integer articleLikeId;
    private BuyerVO buyerVO;
    private ArticleVO articleVO;
    private Date articleLikeListTime; 

    public ArticleLikeVO() {
    }

    @Id 
    @Column(name = "articleLikeId")
    @GeneratedValue(strategy = GenerationType.IDENTITY) 
    public Integer getArticleLikeId() {
        return this.articleLikeId;
    }

    public void setArticleLikeId(Integer articleLikeId) {
        this.articleLikeId = articleLikeId;
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
    @JoinColumn(name = "memberId", referencedColumnName = "memberId")
    public BuyerVO getBuyerVO() {
    	return this.buyerVO;
    }
    
    public void setBuyerVO(BuyerVO buyerVO) {
    	this.buyerVO = buyerVO;
    }
    
   
    
    @Column(name = "articleLikeListTime")
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getArticleLikeListTime() {
		return this.articleLikeListTime;
	}

    public void setArticleLikeListTime(Date articleLikeListTime) {
    	this.articleLikeListTime = articleLikeListTime;
    }

    
}
