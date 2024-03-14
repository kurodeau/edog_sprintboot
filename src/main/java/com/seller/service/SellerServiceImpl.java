package com.seller.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.lang.NonNull;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.config.SellerDetailsService;
import com.seller.entity.SellerVO;
import com.seller.repositary.SellerRepository;

@Service
@ComponentScan("com.config")
public class SellerServiceImpl implements SellerService {

	private SellerDetailsService sellerDetailsService;

	@Autowired
	public void setSellerDetailsService(SellerDetailsService sellerDetailsService) {
		this.sellerDetailsService = sellerDetailsService;
	}

	private SellerRepository repo;

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
	
	

	public SellerVO findByOnlyOneEmail(String email) {
		return repo.findByOnlyOneEmail(email);
	}
	

	@Override
	public SellerVO findByOnlyPhone(String phone) {
		 
		return repo.findByOnlyPhone(phone);
	}
	
	
	public Boolean isDuplcateEmail(String email) {
		SellerVO sellerVO = repo.findByOnlyOneEmail(email);
		
		return sellerVO==null || !sellerVO.getSellerEmail().equals(email);
	}


	public void saveUserDetails(SellerVO sellerVO) {

		if (sellerVO == null) {
			throw new UsernameNotFoundException("User not found with username ");
		}
		
		System.out.println("saveUserDetails" +sellerVO);
		
		UserDetails userdetails = User.builder()
				.username(sellerVO.getSellerEmail())
				.password(sellerVO.getSellerPassword())
			    .authorities("ROLE_SELLER")  
				.build();

		sellerDetailsService.createUser(userdetails, sellerVO);

	}
	
	public void updateUserDetails(SellerVO sellerVO) {

		if (sellerVO == null) {
			throw new UsernameNotFoundException("User not found with username ");
		}
		

		UserDetails userdetails = User.builder()
				.username(sellerVO.getSellerEmail())
				.password(sellerVO.getSellerPassword())
			    .authorities("ROLE_SELLER")  
				.build();

		sellerDetailsService.changePassword(userdetails, sellerVO);

	}


}
