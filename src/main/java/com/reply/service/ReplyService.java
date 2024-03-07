package com.reply.service;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.article.repositary.ArticleRepository;
import com.article.entity.ArticleVO;
import com.reply.entity.ReplyVO;
import com.reply.repositary.ReplyRepository;


@Service("replyService")
public class ReplyService {

	@Autowired
	ReplyRepository repository;
	
	@Autowired
    private SessionFactory sessionFactory;

	public void addReply(ReplyVO replyVO) {
		repository.save(replyVO);
	}

	public void updateReply(ReplyVO replyVO) {
		repository.save(replyVO);
	}

	public void deleteReply(Integer replyId) {
		if (repository.existsById(replyId))
			repository.deleteByReplyId(replyId);
//		    repository.deleteById(articleId);
	}

	public ReplyVO getOneReply(Integer replyId) {
		Optional<ReplyVO> optional = repository.findById(replyId);
//		return optional.get();
		return optional.orElse(null);  // public T orElse(T other) : 如果值存在就回傳其值，否則回傳other的值
	}

	public List<ReplyVO> getAll() {
		return repository.findAll();
	}
	
	public List<ReplyVO> getAllRepliesForArticle(Integer articleId) {
		 List<Integer> ids = new ArrayList<>();
	        ids.add(articleId);
	        List<ReplyVO> replyVOList = repository.findAllById(ids);
        return replyVOList;
    }
	
	public List<ReplyVO> getByArticleId(ArticleVO articleVO){
		List<ReplyVO> articleReply=repository.findByArticleVO(articleVO);
		return articleReply;
	}

}