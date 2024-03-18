package com.replyLike.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.reply.entity.ReplyVO;
import com.replyLike.entity.ReplyLikeVO;
import com.replyLike.repositary.ReplyLikeRepository;
import com.buyer.entity.BuyerVO;

@Service("replyLikeService")
public class ReplyLikeService {

	@Autowired
	ReplyLikeRepository repository;

	public void addReplyLike(ReplyLikeVO replyLikeVO) {
		repository.save(replyLikeVO);
	}

	public void updateReplyLike(ReplyLikeVO replyLikeVO) {
		repository.save(replyLikeVO);
	}

	public void deleteReplyLike(Integer replyLikeId) {
		if (repository.existsById(replyLikeId))
			repository.deleteByReplyLikeId(replyLikeId);
	}


	public ReplyLikeVO getOneReplyLike(Integer replyLikeId) {
		Optional<ReplyLikeVO> optional = repository.findById(replyLikeId);
//		return optional.get();
		return optional.orElse(null);  // public T orElse(T other) : 如果值存在就回傳其值，否則回傳other的值
	}

	public List<ReplyLikeVO> getAll() {
		return repository.findAll();
	}
	public Integer findReplyLikeIdByMemberIdAndArticleId(BuyerVO buyerVO, ReplyVO replyVO) {
        Integer memberId = buyerVO.getMemberId();
        Integer replyId = replyVO.getReplyId();
        System.out.println(memberId);
        System.out.println(replyId);
        ReplyLikeVO replyLike = repository.findByBuyerVOAndReplyVO(buyerVO, replyVO);
        if (replyLike != null) {
            return replyLike.getReplyLikeId();
        } else {
            return null; // 如果未找到匹配的记录，则返回 null
        }
    }

	}