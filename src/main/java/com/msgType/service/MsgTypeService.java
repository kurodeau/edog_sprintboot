package com.msgType.service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.msg.entity.MsgVO;
import com.msgType.entity.MsgTypeVO;
import com.msgType.repositary.MsgTypeRepository;

@Service("msgTypeService")
public class MsgTypeService {

	@Autowired
	MsgTypeRepository repository;

	public void addMsgType(MsgTypeVO msgTypeVO) {
		repository.save(msgTypeVO);
	}

	public void updateMsgType(MsgTypeVO msgTypeVO) {
		repository.save(msgTypeVO);
	}

	public void deleteMsgType(Integer msgTypeId) {
		if (repository.existsById(msgTypeId))
			repository.deleteById(msgTypeId);
	}


	public MsgTypeVO getOneMsgType(Integer msgTypeId) {
		Optional<MsgTypeVO> optional = repository.findById(msgTypeId);
//		return optional.get();
		return optional.orElse(null);  // public T orElse(T other) : 如果值存在就回傳其值，否則回傳other的值
	}

	public List<MsgTypeVO> getAll() {
		return repository.findAll();
	}

//	public Set<MsgVO> getMsgsByMsgTypeId(Integer msgTypeId) {
//		return getOneMsgType(msgTypeId).getMsgs();
//	}

}
