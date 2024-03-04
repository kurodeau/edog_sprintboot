package com.sellerLv.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import com.sellerLv.entity.SellerLvVO;
import com.sellerLv.repositary.SellerLvRepository;

@Service
public class SellerLvServiceImpl implements SellerLvService {

	@Autowired
	SellerLvRepository repo;

	public void addSellerLv(@NonNull SellerLvVO sellerLvVO) {
		repo.save(sellerLvVO);
	}

	public void updateSellerLv(@NonNull SellerLvVO sellerLvVO) {
		repo.save(sellerLvVO);
	}

	public void deleteSellerLv( @NonNull Integer id) {
		if (repo.existsById(id))
		{
		// 直接刪除
		repo.deleteById(id);
		}
	
		
	}
	
	public SellerLvVO getById(@NonNull Integer id) {
		Optional<SellerLvVO> optional = repo.findById(id);
//		return optional.get();
		return optional.orElse(null);  
	}
	
	public List<SellerLvVO> getAll() {
		return repo.findAll();
	}
	
	public Integer getCount() {
		return (int) repo.count();
	}

	@Override
	public void deleteSecureSellerLv(@NonNull Integer id) {
		if (repo.existsById(id))
		{
			// 預防級聯刪除，導致 單方整表刪除
			repo.deleteByTheId(id);
		}
				
	}

}
