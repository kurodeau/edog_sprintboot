// https://docs.spring.io/spring-data/jpa/docs/current/reference/html/

package com.reply.repositary;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.article.entity.ArticleVO;
import com.reply.entity.ReplyVO;

public interface ReplyRepository extends JpaRepository<ReplyVO, Integer> {

	@Transactional
	@Modifying
	@Query(value = "delete from reply where replyId =?1", nativeQuery = true)
	void deleteByReplyId(int replyId);
	
	List<ReplyVO> findByArticleVO(ArticleVO articleVO);

}