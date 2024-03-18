package com.manager;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ad.model.AdVO;
import com.buyer.entity.BuyerVO;

@Service("ManagerService")
public class ManagerService {

	@Autowired
	ManagerRepository repository;

	public Integer addManager(ManagerVO managerVO) {
	    ManagerVO savedManager = repository.save(managerVO);
	    return savedManager.getManagerId();
	}

	public void updateManager(ManagerVO managerVO) {
		repository.save(managerVO);
	}

	public void deleteManager(Integer managerUserId) {
		if (repository.existsById(managerUserId))
			repository.deleteById(managerUserId);
	}

	public ManagerVO getOneManager(Integer managerUserId) {
		Optional<ManagerVO> optional = repository.findById(managerUserId);
		return optional.orElse(null);

	}

	public List<ManagerVO> getAll() {

		return repository.findAll();

	}
	
	public ManagerVO findByOnlyOneEmail(String managerEmail) {
		return repository.findByOnlyOneEmail(managerEmail);
	}

}
