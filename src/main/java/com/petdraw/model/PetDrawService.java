package com.petdraw.model;

import java.util.List;
import java.util.Optional;
import java.util.Random;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.buyer.entity.BuyerVO;
import com.buyer.model.BuyerRepository;

@Service("petDrawService")
public class PetDrawService {

	@Autowired
	PetDrawRepository petDrawRepository;
	
	@Autowired
	BuyerRepository buyerRepository;


    public void addPetDraw(PetDrawVO petDrawVO) {
    	// 登入者帳號
    	Integer memberId = petDrawVO.getMemberMain();
    	
    	List<BuyerVO> bueryVOList = buyerRepository.findAll();
//    	// 目前會員人數 假設總數一百
    	Integer memberCount = bueryVOList.size();
//    	// 使用 Random 類獲取一個隨機的索引
        Random random = new Random();
        int randomIndex = random.nextInt(memberCount);
//    	根據索引獲取被配對的人
    	BuyerVO pair = bueryVOList.get(randomIndex);
    	Integer pairMemberId = pair.getMemberId();
    	
    	// 賦值給 PetDrawVO 的 memberPair 屬性
    	petDrawVO.setMemberPair(pair.getMemberId());
    	
    	petDrawRepository.save(petDrawVO);
    }
    
	public void updatePetDraw(PetDrawVO petDrawVO) {
		petDrawRepository.save(petDrawVO);
	}

	 public void deletePetDraw(Integer petDrawId) {
	        if (petDrawRepository.existsById(petDrawId)) {
	            petDrawRepository.deleteById(petDrawId);
	        }
	    }

	public PetDrawVO getOnePetDraw(Integer petDrawId) {
		Optional<PetDrawVO> optional = petDrawRepository.findById(petDrawId);
		return optional.orElse(null);
	}

	public List<PetDrawVO> getAll() {
		return petDrawRepository.findAll();
	}

	public List<PetDrawVO> getBuyerById(Integer memberId) {
		// 使用注入的 petDrawRepository 獲取 BuyerVO
		return petDrawRepository.findByMemberId(memberId);
	}
	@PostConstruct
	public void init() {
	    // 進行 petDrawRepository 和 buyerRepository 的初始化
	}
}
