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


	 public Integer addPetDraw(PetDrawVO petDrawVO) {
	        PetDrawVO savedPetDraw = petDrawRepository.save(petDrawVO);
	        return savedPetDraw.getpetDrawId();
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

	public List<PetDrawVO> GetfindByMemberId(Integer memberId) {
		// 使用注入的 petDrawRepository 獲取 BuyerVO
		List<PetDrawVO> list = petDrawRepository.findByMemberId(memberId);
		PetDrawVO petDraw = list.get(0);
		Integer member = petDraw.getMemberId();
		BuyerVO user = buyerRepository.findById(memberId).orElse(null);
		user.getPetName();
		user.getMemberName();
		return list;

	}
	@PostConstruct
	public void init() {
	    // 進行 petDrawRepository 和 buyerRepository 的初始化
	}
	
	 public int getRandomMemberIdNotEqualTo(int memberId) {
	        List<BuyerVO> buyers = buyerRepository.findAll();
	        Random random = new Random();
	        int randomIndex;
	        do {
	            randomIndex = random.nextInt(buyers.size());
	        } while (buyers.get(randomIndex).getMemberId() == memberId);
	        return buyers.get(randomIndex).getMemberId();
	    }
}
