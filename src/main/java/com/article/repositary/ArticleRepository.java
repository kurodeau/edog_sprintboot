// https://docs.spring.io/spring-data/jpa/docs/current/reference/html/

package com.article.repositary;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.article.entity.ArticleVO;
import com.articleType.entity.ArticleTypeVO;
import com.buyer.entity.BuyerVO;

public interface ArticleRepository extends JpaRepository<ArticleVO, Integer> {

	@Transactional
	@Modifying
	@Query(value = "delete from article where articleId =?1", nativeQuery = true)
	void deleteByArticleId(int articleId);
	
	List<ArticleVO> findByArticleTypeVO(ArticleTypeVO articleTypeVO);
	
	List<ArticleVO> findByBuyerVO(BuyerVO buyerVO);
	
	List<ArticleVO> findByArticleTitleContaining(String articleTitle);



}