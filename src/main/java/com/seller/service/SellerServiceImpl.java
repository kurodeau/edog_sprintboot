package com.seller.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.lang.NonNull;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.config.DBUserDetailsManagerSeller;
import com.seller.entity.SellerVO;
import com.seller.repositary.SellerRepository;

@Service
public class SellerServiceImpl implements SellerService {

	private DBUserDetailsManagerSeller dbUserDetailsManagerSeller;
	private SellerRepository repo;
	
	@Autowired
	public void setDbUserDetailsManagerSeller(DBUserDetailsManagerSeller dbUserDetailsManagerSeller) {
		this.dbUserDetailsManagerSeller = dbUserDetailsManagerSeller;
	}

	@Autowired
	public void setSellerRepository(SellerRepository repo) {
		this.repo = repo;
	}


	public void addSeller(@NonNull SellerVO sellerVO) {
		repo.save(sellerVO);
	}

	public void updateSeller(@NonNull SellerVO sellerVO) {
		repo.save(sellerVO);
	}

	public void deleteSeller(@NonNull Integer id) {
		repo.deleteByTheId(id);
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

	public SellerVO findUserEmail(String email) {
		return repo.findByEmail(email);
	}

	 public void saveUserDetails(SellerVO sellerVO) {
	 UserDetails userdetails =
//	 User.withDefaultPasswordEncoder().username(sellerVO.getSellerEmail()).password(sellerVO.getSellerPassword()).roles("USER")
//	 .build();

	 User.withDefaultPasswordEncoder().username(sellerVO.getSellerEmail()).password(sellerVO.getSellerPassword()).roles("USER")
	 .build();
	 

	 dbUserDetailsManagerSeller.createUser(userdetails,sellerVO);
	 }

}
