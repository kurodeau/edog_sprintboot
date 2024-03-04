// https://docs.spring.io/spring-data/jpa/docs/current/reference/html/

package com.article.repositary;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.article.entity.ArticleVO;

public interface ArticleRepository extends JpaRepository<ArticleVO, Integer> {

	@Transactional
	@Modifying
	@Query(value = "delete from article where articleId =?1", nativeQuery = true)
	void deleteByArticleId(int articleId);

}