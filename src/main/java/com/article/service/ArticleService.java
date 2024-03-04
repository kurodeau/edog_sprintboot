package com.article.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.article.entity.ArticleVO;
import com.article.repositary.ArticleRepository;

@Service("articleService")
public class ArticleService {

	@Autowired
	ArticleRepository repository;

	public void addArticle(ArticleVO articleVO) {
		repository.save(articleVO);
	}

	public void updateArticle(ArticleVO articleVO) {
		repository.save(articleVO);
	}

	public void deleteArticle(Integer articleId) {
		if (repository.existsById(articleId))
			repository.deleteByArticleId(articleId);
//		    repository.deleteById(articleId);
	}

	public ArticleVO getOneArticle(Integer articleId) {
		Optional<ArticleVO> optional = repository.findById(articleId);
//		return optional.get();
		return optional.orElse(null);  // public T orElse(T other) : 如果值存在就回傳其值，否則回傳other的值
	}

	public List<ArticleVO> getAll() {
		return repository.findAll();
	}

}