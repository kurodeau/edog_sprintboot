package com.buyer.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.buyer.entity.BuyerVO;
import com.buyer.model.BuyerRepository;
import com.config.BuyerDetailsService;
import com.config.SellerDetailsService;
import com.seller.entity.SellerVO;

@Service("buyerService")
@ComponentScan("com.config")
public class BuyerService {

	@Autowired
	BuyerRepository repository;

	private BuyerDetailsService buyerDetailsService;

	@Autowired
	public void setbuyerDetailsService(BuyerDetailsService buyerDetailsService) {
		this.buyerDetailsService = buyerDetailsService;
	}

	public void addBuyer(BuyerVO buyerVO) {
		repository.save(buyerVO);
	}

	public void updateBuyer(BuyerVO buyerVO) {
		repository.save(buyerVO);
	}

	public void deleteBuyer(Integer memberId) {
		if (repository.existsById(memberId))
			repository.deleteByMemberId(memberId);
		// repository.deleteById(memberId);
	}

	public BuyerVO getOneBuyer(Integer memberId) {
		Optional<BuyerVO> optional = repository.findById(memberId);
		// return optional.get();
		return optional.orElse(null); // public T orElse(T other) : 如果值存在就回傳其值，否則回傳other的值
	}

	public List<BuyerVO> getAll() {
		return repository.findAll();
	}

	public void saveUserDetails(BuyerVO buyerVO) {

		UserDetails userdetails = User.builder().username(buyerVO.getMemberEmail())
				.password(buyerVO.getMemberPassword()).roles("BUYER")
				.build();

		buyerDetailsService.createUser(userdetails, buyerVO);

	}

}
