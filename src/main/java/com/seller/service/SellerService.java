package com.seller.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import com.seller.entity.SellerVO;
import com.seller.repositary.SellerRepository;

@Service
public class SellerService {

	@Autowired
	SellerRepository repo;
	
	public void addSeller(@NonNull SellerVO sellerVO) {
		repo.save(sellerVO);
	}

	public void updateSeller(@NonNull SellerVO sellerVO) {
		repo.save(sellerVO);
	}

	public void deleteSeller(@NonNull Integer id) {
			repo.deleteById(id);
	}
	
	public SellerVO getById(Integer id) {
		Optional<SellerVO> optional = repo.findById(id);
		return optional.orElse(null);  
	}
	
	public List<SellerVO> getAll() {
		return repo.findAll();
	}
	
	public Integer getCount() {
		return (int) repo.count();
	}
	
	
}


