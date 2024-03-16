package com.petdraw.model;

import com.buyer.entity.BuyerVO;

public class PairResponse {

	private PetDrawVO petDraw;
	
	private BuyerVO memberVO;
	
	private BuyerVO memberPairVO;

	public PetDrawVO getPetDraw() {
		return petDraw;
	}

	public void setPetDraw(PetDrawVO petDraw) {
		this.petDraw = petDraw;
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
