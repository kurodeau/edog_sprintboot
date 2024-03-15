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

	public void addManager(ManagerVO managerVO) {
		repository.save(managerVO);
	}

	public void updateManager(ManagerVO managerVO) {
		repository.save(managerVO);
	}

	public void deleteManager(Integer managerUserId) {
		if (repository.existsById(managerUserId))
			repository.deleteByAdId(managerUserId);
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
