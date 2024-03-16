package com.petdraw.model;

import com.buyer.entity.BuyerVO;

public class PairResponse {

	private PetDrawVO petDrawVO;

	private BuyerVO memberVO;

	private BuyerVO memberPairVO;

	public BuyerVO getMemberVO() {
		return memberVO;
	}

	public PetDrawVO getPetDrawVO() {
		return petDrawVO;
	}

	public void setPetDrawVO(PetDrawVO petDrawVO) {
		this.petDrawVO = petDrawVO;
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
