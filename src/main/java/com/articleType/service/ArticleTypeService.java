package com.articleType.service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.article.entity.ArticleVO;
import com.articleType.entity.ArticleTypeVO;
import com.articleType.repositary.ArticleTypeRepository;

@Service("articleTypeService")
public class ArticleTypeService {

	@Autowired
	ArticleTypeRepository repository;

	public void addArticleType(ArticleTypeVO articleTypeVO) {
		repository.save(articleTypeVO);
	}

	public void updateArticleType(ArticleTypeVO articleTypeVO) {
		repository.save(articleTypeVO);
	}

	public void deleteArticleType(Integer articleTypeId) {
		if (repository.existsById(articleTypeId))
			repository.deleteById(articleTypeId);
	}


	public ArticleTypeVO getOneArticleType(Integer articleTypeId) {
		Optional<ArticleTypeVO> optional = repository.findById(articleTypeId);
//		return optional.get();
		return optional.orElse(null);  // public T orElse(T other) : 如果值存在就回傳其值，否則回傳other的值
	}

	public List<ArticleTypeVO> getAll() {
		return repository.findAll();
	}

	public Set<ArticleVO> getArticlesByArticleTypeId(Integer articleTypeId) {
		return getOneArticleType(articleTypeId).getArticles();
	}

}
