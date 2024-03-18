// https://docs.spring.io/spring-data/jpa/docs/current/reference/html/

package com.replyLike.repositary;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.reply.entity.ReplyVO;
import com.replyLike.entity.ReplyLikeVO;
import com.buyer.entity.BuyerVO;

public interface ReplyLikeRepository extends JpaRepository<ReplyLikeVO, Integer> {

	@Transactional
	@Modifying
	@Query(value = "delete from replyLike where replyLikeId=?1", nativeQuery = true)
	void deleteByReplyLikeId(int replyLikeId);

	ReplyLikeVO findByBuyerVOAndReplyVO(BuyerVO buyerVO, ReplyVO replyVO);
	

}