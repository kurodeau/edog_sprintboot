package com.msg.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.msg.entity.MsgVO;
import com.msg.repositary.MsgRepository;
import com.msgType.entity.MsgTypeVO;
import com.reply.entity.ReplyVO;
import com.article.entity.ArticleVO;
import com.articleLike.entity.ArticleLikeVO;
import com.buyer.entity.BuyerVO;

@Service("msgService")
public class MsgService {

	@Autowired
	MsgRepository repository;

	public void addMsg(MsgVO msgVO) {
		repository.save(msgVO);
	}

	public void updateMsg(MsgVO msgVO) {
		repository.save(msgVO);
	}

	public void deleteMsg(Integer msgId) {
		if (repository.existsById(msgId))
			repository.deleteByMsgId(msgId);
//		    repository.deleteById(msgId);
	}

	public MsgVO getOneMsg(Integer msgId) {
		Optional<MsgVO> optional = repository.findById(msgId);
//		return optional.get();
		return optional.orElse(null);  // public T orElse(T other) : 如果值存在就回傳其值，否則回傳other的值
	}

	public List<MsgVO> getAll() {
		return repository.findAll();
	}
	public List<MsgVO> getByMemberId(BuyerVO buyerVO){
		List<MsgVO> myMsg=repository.findByReceiverMember(buyerVO);
		System.out.println("MemberId="+buyerVO.getMemberId());
		return myMsg;
	}
	
	public Integer findArticleLikeIdByMemberIdAndArticleIdAndMsgTypeId(BuyerVO buyerVO, ArticleVO articleVO,MsgTypeVO msgTypeVO) {
        Integer memberId = buyerVO.getMemberId();
        Integer articleId = articleVO.getArticleId();
        Integer MsgTypeId = msgTypeVO.getMsgTypeId();
        System.out.println(memberId);
        System.out.println(articleId);
        System.out.println(MsgTypeId);
        MsgVO Msg = repository.findBySenderMemberAndArticleVOAndMsgTypeVO(buyerVO, articleVO,msgTypeVO);
        if (Msg != null) {
            return Msg.getMsgId();
        } else {
            return null; // 如果未找到匹配的记录，则返回 null
        }
    }
	public Integer findArticleLikeIdByMemberIdAndReplyIdAndMsgTypeId(BuyerVO buyerVO, ReplyVO replyVO,MsgTypeVO msgTypeVO) {
		Integer memberId = buyerVO.getMemberId();
		Integer replyId = replyVO.getReplyId();
		Integer MsgTypeId = msgTypeVO.getMsgTypeId();
		System.out.println(memberId);
		System.out.println(replyId);
		System.out.println(MsgTypeId);
		MsgVO Msg = repository.findBySenderMemberAndReplyVOAndMsgTypeVO(buyerVO, replyVO,msgTypeVO);
		if (Msg != null) {
			return Msg.getMsgId();
		} else {
			return null; // 如果未找到匹配的记录，则返回 null
		}
	}
	
	}