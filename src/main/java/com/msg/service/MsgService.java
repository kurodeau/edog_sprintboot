package com.msg.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.msg.entity.MsgVO;
import com.msg.repositary.MsgRepository;
import com.buyer.entity.BuyerVO;

@Service("msgService")
public class MsgService {

	@Autowired
	MsgRepository repository;

	public void addMsg(MsgVO msgVO) {
		repository.save(msgVO);
	}

	public void updateMsg(MsgVO msgVO) {
		repository.save(msgVO);
	}

	public void deleteMsg(Integer msgId) {
		if (repository.existsById(msgId))
			repository.deleteByMsgId(msgId);
//		    repository.deleteById(msgId);
	}

	public MsgVO getOneMsg(Integer msgId) {
		Optional<MsgVO> optional = repository.findById(msgId);
//		return optional.get();
		return optional.orElse(null);  // public T orElse(T other) : 如果值存在就回傳其值，否則回傳other的值
	}

	public List<MsgVO> getAll() {
		return repository.findAll();
	}
	public List<MsgVO> getByMemberId(BuyerVO buyerVO){
		List<MsgVO> myMsg=repository.findByBuyerVO(buyerVO);
		System.out.println("MemberId="+buyerVO.getMemberId());
		return myMsg;
	}
	
	}