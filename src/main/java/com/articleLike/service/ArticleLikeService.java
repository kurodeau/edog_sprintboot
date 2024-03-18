package com.articleLike.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.article.entity.ArticleVO;
import com.articleLike.entity.ArticleLikeVO;
import com.articleLike.repositary.ArticleLikeRepository;
import com.buyer.entity.BuyerVO;
import com.msg.entity.MsgVO;

@Service("articleLikeService")
public class ArticleLikeService {

	@Autowired
	ArticleLikeRepository repository;

	public void addArticleLike(ArticleLikeVO articleLikeVO) {
		repository.save(articleLikeVO);
	}

	public void updateArticleLike(ArticleLikeVO articleLikeVO) {
		repository.save(articleLikeVO);
	}

	public void deleteArticleLike(Integer articleLikeId) {
		if (repository.existsById(articleLikeId))
			repository.deleteByArticleLikeId(articleLikeId);
	}


	public ArticleLikeVO getOneArticleLike(Integer articleLikeId) {
		Optional<ArticleLikeVO> optional = repository.findById(articleLikeId);
//		return optional.get();
		return optional.orElse(null);  // public T orElse(T other) : 如果值存在就回傳其值，否則回傳other的值
	}

	public List<ArticleLikeVO> getAll() {
		return repository.findAll();
	}
	public Integer findArticleLikeIdByMemberIdAndArticleId(BuyerVO buyerVO, ArticleVO articleVO) {
        Integer memberId = buyerVO.getMemberId();
        Integer articleId = articleVO.getArticleId();
        System.out.println(memberId);
        System.out.println(articleId);
        ArticleLikeVO articleLike = repository.findByBuyerVOAndArticleVO(buyerVO, articleVO);
        if (articleLike != null) {
            return articleLike.getArticleLikeId();
        } else {
            return null; // 如果未找到匹配的记录，则返回 null
        }
    }
	public List<ArticleLikeVO> getByMemberId(BuyerVO buyerVO){
		List<ArticleLikeVO> myArticleLike=repository.findByBuyerVO(buyerVO);
		System.out.println("MemberId="+buyerVO.getMemberId());
		return myArticleLike;
	}
	}