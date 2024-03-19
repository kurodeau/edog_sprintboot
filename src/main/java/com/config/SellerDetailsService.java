package com.config;


import java.util.ArrayList;
import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Component;

import com.seller.entity.SellerVO;
import com.seller.repositary.SellerRepository;


@Component("sellerDetailsService")
public class SellerDetailsService  implements UserDetailsService, UserDetailsManager {

    private static final Logger logger = LoggerFactory.getLogger(SellerDetailsService .class);

	@Autowired
	@Qualifier("sellerPasswordEncoder")
    private  PasswordEncoder sellerPasswordEncoder;

	
	@Autowired
	SellerRepository sellerRepo;


	
	public void createUser(UserDetails user,SellerVO sellerVO) {
	
		sellerVO.setSellerEmail(user.getUsername());
		
		sellerVO.setSellerPassword(sellerPasswordEncoder.encode(user.getPassword()));
//		sellerVO.setIsConfirm(true);

		
		sellerRepo.save(sellerVO);
	}

	@Override
	public void updateUser(UserDetails user) {
		SellerVO sellerVO = new SellerVO();
		sellerVO.setSellerEmail(user.getUsername());
		sellerVO.setSellerPassword(user.getPassword());
		
		sellerRepo.save(sellerVO);
	}

	@Override
	public void deleteUser(String username) {
	}
	
	public void changePassword(UserDetails user,SellerVO sellerVO) {
		sellerVO.setSellerPassword(sellerPasswordEncoder.encode(user.getPassword()));

		sellerRepo.save(sellerVO);
	}


	@Override
	public void changePassword(String oldPassword, String newPassword) {
		
		
	}

	@Override
	public boolean userExists(String username) {
		SellerVO targetUser = sellerRepo.findByEmail(username);
		return targetUser != null;
	}

	@Override
	// UserDetailsService 用於獲取用戶名，用來從DB中獲取User對象，並驗證用戶
	// 在UsernamePasswordAutheniticationFilter過濾器 AttemptAutheniciation方法中
	// 把用戶輸入的密碼和SQL中或的密名進行驗證
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		logger.error(username);
		
//		SellerVO targetUser =  sellerRepo.findByEmail(username);
		SellerVO targetUser =  sellerRepo.findByOnlyOneEmail(username);
	
		
	

		
//		System.out.println(targetUser);
		// 創建權限列表
		Collection<GrantedAuthority> authorities = new ArrayList<>();



	    if (targetUser == null) {
	        System.out.println("targetUser");
	        
	        throw new UsernameNotFoundException(username);
	    
	    } else {
	        // 創建權限列表
	        Collection<GrantedAuthority> authorities1 = new ArrayList<>();

	        // 根據使用者的角色添加權限
            authorities1.add(new SimpleGrantedAuthority("ROLE_SELLER" ));


	        return new org.springframework.security.core.userdetails.User(
	            targetUser.getSellerEmail(),
	            targetUser.getSellerPassword(),
	            targetUser.getIsConfirm(),  // 用户启用状态
	            true,  // 用戶帳號是否過期
	            true,  // 用戶憑證是否過期
	            true,  // 用戶是否未被鎖定
	            authorities1 // 授權權限列表
	        );
	    }

	}

	@Override
	public void createUser(UserDetails user) {
		// TODO Auto-generated method stub
		
	}

}
