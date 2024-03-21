package com.petdraw.model;

import com.buyer.entity.BuyerVO;

public class PairPassiveResponse {

	private PetDrawVO petDrawVO;
	
	private BuyerVO memberVO;
	
	private BuyerVO memberPairVO;

	public PetDrawVO getPetDrawVO() {
		return petDrawVO;
	}

	public void setPetDraw(PetDrawVO petDrawVO) {
		this.petDrawVO = petDrawVO;
	}

	public BuyerVO getMemberVO() {
		return memberVO;
	}

	public void setMemberVO(BuyerVO memberVO) {
		this.memberVO = memberVO;
	}

	public BuyerVO getMemberPairVO() {
		return memberPairVO;
	}

	public void setMemberPairVO(BuyerVO memberPairVO) {
		this.memberPairVO = memberPairVO;
	}


	
}
