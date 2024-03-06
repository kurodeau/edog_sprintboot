package com.config;


import java.util.ArrayList;
import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.buyer.entity.BuyerVO;
import com.buyer.model.BuyerRepository;
import com.seller.entity.SellerVO;


@Component("buyerDetailsService")
public class BuyerDetailsService  implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(BuyerDetailsService .class);

    
    @Autowired
	@Qualifier("buyerPasswordEncoder")
    private  PasswordEncoder buyerPasswordEncoder;
  
	
	@Autowired
	BuyerRepository buyerRepo;

	public void createUser(UserDetails user,BuyerVO buyerVO) {

		buyerVO.setMemberEmail(buyerVO.getMemberEmail());
		buyerVO.setMemberPassword( buyerPasswordEncoder.encode(buyerVO.getMemberPassword()));
		buyerVO.setIsConfirm(true);

		buyerRepo.save(buyerVO);
	}

	@Override
	// UserDetailsService 用於獲取用戶名，用來從DB中獲取User對象，並驗證用戶
	// 在UsernamePasswordAutheniticationFilter過濾器 AttemptAutheniciation方法中
	// 把用戶輸入的密碼和SQL中或的密名進行驗證
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		logger.error(username);
		
		
//		SellerVO targetUser =  sellerRepo.findByEmail(username);
		SellerVO targetUser =  buyerRepo.findByOnlyOneEmail(username);
		System.out.println(targetUser);
		// 創建權限列表
		Collection<GrantedAuthority> authorities = new ArrayList<>();


		if (targetUser == null) {
			throw new UsernameNotFoundException(username);
		} else {
			return new org.springframework.security.core.userdetails.User(
				    targetUser.getSellerEmail(),
				    targetUser.getSellerPassword(),
				    true,  // 用户启用状态
				    true,  // 用戶帳號是否過期
				    true,  // 用戶憑證是否過期
				    true,  // 用戶是否未被鎖定
				    authorities // 授權權限列表
				);
		}

	}

}
