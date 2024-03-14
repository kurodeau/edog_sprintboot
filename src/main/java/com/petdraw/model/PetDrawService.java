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

	public InsertResponse GetfindByMemberId(Integer memberId) {
		// 使用 buyerRepository 查找特定的會員
		Optional<BuyerVO> buyerOptional = buyerRepository.findById(memberId);
		// 檢查是否找到會員
		if (buyerOptional.isPresent()) {
			BuyerVO buyer = buyerOptional.get();

			// 獲取會員相關資料，例如會員名稱和寵物名稱
			Integer member = buyer.getMemberId();
			String petName = buyer.getPetName();
			String memberName = buyer.getMemberName();
			 // 構造 PetDrawVO 對象
	        InsertResponse insertResponse = new InsertResponse();
	        insertResponse.setMemberPairId(buyer);

	       // 返回抽卡結果
	        return insertResponse;
	    } else {
	        // 如果找不到會員，返回錯誤或空的對象
	        return new InsertResponse(); // 或其他表示錯誤的處理方式
	    }
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
