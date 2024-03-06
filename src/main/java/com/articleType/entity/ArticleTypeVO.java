package com.articleType.entity;

import java.util.*;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.OrderBy;

import com.article.entity.ArticleVO;

@Entity
@Table(name = "articleType")
public class ArticleTypeVO implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	
	private Integer articleTypeId;
	private String articleTypeName;
	private Set<ArticleVO> articles = new HashSet<ArticleVO>();

	public ArticleTypeVO() {
	}

	@Id
	@Column(name = "articleTypeId")
	@GeneratedValue(strategy = GenerationType.IDENTITY) //@GeneratedValue的generator屬性指定要用哪個generator //【strategy的GenerationType, 有四種值: AUTO, IDENTITY, SEQUENCE, TABLE】 
	public Integer getArticleTypeId() {
		return this.articleTypeId;
	}

	public void setArticleTypeId(Integer articleTypeId) {
		this.articleTypeId = articleTypeId;
	}

	@Column(name = "articleTypeName")
	public String getArticleTypeName() {
		return this.articleTypeName;
	}

	public void setArticleTypeName(String articleTypeName) {
		this.articleTypeName = articleTypeName;
	}
	
	@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.EAGER, mappedBy="articleTypeVO")
	@OrderBy("articleId asc")
	//註1:【現在是設定成 cascade="all" lazy="false" inverse="true"之意】
	//註2:【mappedBy="多方的關聯屬性名"：用在雙向關聯中，把關係的控制權反轉】【deptVO是EmpVO的屬性】
	//註3:【原預設為@OneToMany(fetch=FetchType.LAZY, mappedBy="deptVO")之意】--> 【是指原為  lazy="true"  inverse="true"之意】
	//FetchType.EAGER : Defines that data must be eagerly fetched
	//FetchType.LAZY  : Defines that data can be lazily fetched
	public Set<ArticleVO> getArticles() {
		return this.articles;
	}

	public void setArticles(Set<ArticleVO> articles) {
		this.articles = articles;
	}

}
