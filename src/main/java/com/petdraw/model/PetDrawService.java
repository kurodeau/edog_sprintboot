package com.petdraw.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
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

	public List<PetDrawVO> getByMemberIdAndAfterPetDrawTime(Integer buyerId, LocalDateTime afterDate) {
		List<PetDrawVO> buyerList = new ArrayList<>();

		buyerList = petDrawRepository.findByMemberIdAndAfterPetDrawTime(buyerId, afterDate);
		return buyerList;
	}

	public List<PairResponse> getPairInvitationsForBuyer(Integer memberId, LocalDateTime afterDate) {
		// 此方法應該從數據庫中獲取所有針對該 memberId 的被動抽卡邀請
		List<PetDrawVO> petDrawInvitations = petDrawRepository.findByMemberPairIdAndMemberPairResTime(memberId,
				afterDate);

		List<PairResponse> pairInvitations = new ArrayList<>();

		for (PetDrawVO petDraw : petDrawInvitations) {
			PairResponse pairInvitation = new PairResponse();
			pairInvitation.setPetDraw(petDraw);
			// 假设 petDraw 中存有发起邀请者的 memberId
			BuyerVO inviter = buyerRepository.findById(petDraw.getMemberId()).orElse(null);
			BuyerVO invitee = buyerRepository.findById(memberId).orElse(null);
			
			pairInvitation.setMemberVO(inviter); // 邀請者
			pairInvitation.setMemberPairVO(invitee); // 被邀請者
			pairInvitations.add(pairInvitation);
		}

		return pairInvitations;
	}

	public PetDrawVO getOnePetDraw(Integer petDrawId) {
		Optional<PetDrawVO> optional = petDrawRepository.findById(petDrawId);
		return optional.orElse(null);
	}

	public List<PetDrawVO> getAll() {
		return petDrawRepository.findAll();
	}

	public BuyerVO getBuyerVoByMemberId(Integer memberId) throws Exception {
		// 使用 buyerRepository 查找特定的會員
		Optional<BuyerVO> buyerOptional = buyerRepository.findById(memberId);

		// 檢查是否找到會員
		if (buyerOptional.isPresent()) {
			BuyerVO buyer = buyerOptional.get();

			return buyer;
		} else {
			// 如果找不到會員，返回錯誤或空的對象
			return null;
		}
	}

//	public PairResponse getPairResponse(BuyerVO currentBuyerVO) {
//		PairResponse pairResponse = new PairResponse();
//		try {
//			BuyerVO pairBuyerVO= getRandomBuyerVOIdNotEqualTo(currentBuyerVO.getMemberId());
//			pairResponse.setMemberPairVO(buyer);
//			pairResponse.setMemberPairVO(pairId);
//		} catch (Exception e) {
//			return new PairResponse();
//		}
//
//			return pairResponse;
//		
//	}

	@PostConstruct
	public void init() {
		// 進行 petDrawRepository 和 buyerRepository 的初始化
	}

	public BuyerVO getRandomBuyerVONotEqualTo(BuyerVO currentBuyerVO) {
		int memberId = currentBuyerVO.getMemberId();
		List<BuyerVO> buyers = buyerRepository.findAll();
		Random random = new Random();
		int randomIndex;
		do {
			randomIndex = random.nextInt(buyers.size());
		} while (buyers.get(randomIndex).getMemberId() == memberId);
		return buyers.get(randomIndex);
	}

}
