// https://docs.spring.io/spring-data/jpa/docs/current/reference/html/

package com.msg.repositary;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.msg.entity.MsgVO;
import com.msgType.entity.MsgTypeVO;
import com.reply.entity.ReplyVO;
import com.article.entity.ArticleVO;
import com.articleLike.entity.ArticleLikeVO;
import com.buyer.entity.BuyerVO;

public interface MsgRepository extends JpaRepository<MsgVO, Integer> {

	@Transactional
	@Modifying
	@Query(value = "delete from msg where msgId =?1", nativeQuery = true)
	void deleteByMsgId(int msgId);
	
	
	List<MsgVO> findByBuyerVO(BuyerVO buyerVO);
	
	MsgVO findByBuyerVOAndArticleVOAndMsgTypeVO(BuyerVO buyerVO, ArticleVO articleVO,MsgTypeVO msgTypeVO);
	
	MsgVO findByBuyerVOAndReplyVOAndMsgTypeVO(BuyerVO buyerVO, ReplyVO replyVO,MsgTypeVO msgTypeVO);
}