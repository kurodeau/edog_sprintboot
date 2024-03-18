// https://docs.spring.io/spring-data/jpa/docs/current/reference/html/

package com.articleLike.repositary;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.article.entity.ArticleVO;
import com.articleLike.entity.ArticleLikeVO;
import com.articleType.entity.ArticleTypeVO;
import com.buyer.entity.BuyerVO;
import com.msg.entity.MsgVO;

public interface ArticleLikeRepository extends JpaRepository<ArticleLikeVO, Integer> {

	@Transactional
	@Modifying
	@Query(value = "delete from articleLike where articleLikeId=?1", nativeQuery = true)
	void deleteByArticleLikeId(int articleLikeId);

	ArticleLikeVO findByBuyerVOAndArticleVO(BuyerVO buyerVO, ArticleVO articleVO);
	
	List<ArticleLikeVO> findByBuyerVO(BuyerVO buyerVO);

}