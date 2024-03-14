package com.petdraw.model;

import com.buyer.entity.BuyerVO;

public class InsertResponse {

	private PetDrawVO petDraw;
	
	private BuyerVO memberId;
	
	private BuyerVO memberPairId;

	public PetDrawVO getPetDraw() {
		return petDraw;
	}

	public void setPetDraw(PetDrawVO petDraw) {
		this.petDraw = petDraw;
	}

	public BuyerVO getMemberId() {
		return memberId;
	}

	public void setMemberId(BuyerVO memberId) {
		this.memberId = memberId;
	}

	public BuyerVO getMemberPairId() {
		return memberPairId;
	}

	public void setMemberPairId(BuyerVO memberPairId) {
		this.memberPairId = memberPairId;
	}
	
	
}
