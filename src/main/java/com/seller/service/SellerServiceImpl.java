package com.seller.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.lang.NonNull;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.config.DBSellerDetailsManager;
import com.seller.entity.SellerVO;
import com.seller.repositary.SellerRepository;

@Service
@ComponentScan("com.config")
public class SellerServiceImpl implements SellerService {

//    private static final Logger logger = LoggerFactory.getLogger(SellerServiceImpl.class);

	
	private  PasswordEncoder passwordEncoder;
	@Autowired
	public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
		this.passwordEncoder = passwordEncoder;
	}
	private SellerRepository repo;
	
	private DBSellerDetailsManager dBSellerDetailsManager;
	@Autowired
	public void setDbUserDetailsManagerSeller(DBSellerDetailsManager dBSellerDetailsManager) {
		this.dBSellerDetailsManager = dBSellerDetailsManager;
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
			User.builder().username(sellerVO.getSellerEmail()).password(sellerVO.getSellerPassword()).roles("SELLER")
	        .build();

//  帶{bcypt}
//	 UserDetails userdetails =User.withDefaultPasswordEncoder()
//	         .username("user")
//	         .password("password")
//	         .roles("USER")
//	         .build();
	 dBSellerDetailsManager.createUser(userdetails,sellerVO);
	 
	 }

}
