package com.config;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Component;

import com.seller.entity.SellerVO;
import com.seller.repositary.SellerRepository;
import com.seller.service.SellerService;
import com.seller.service.SellerServiceImpl;

@Component
public class DBUserDetailsManagerSeller implements UserDetailsService, UserDetailsManager {

	@Autowired
	SellerRepository sellerRepo;

	
//	private SellerService sellerSvc =new SellerServiceImpl();;
//	private SellerService sellerSvc;
//
//	@Autowired
//	public void setSellerService(SellerService sellervice) {
//		this.sellerSvc = sellervice;
//	}
//	

//	public DBUserDetailsManagerSeller(SellerService sellerSvc) {
//		super();
//		this.sellerSvc = sellerSvc;
//	}
	
//	private final SellerService sellerSvc = new SellerService() ;


	@Override
	public void createUser(UserDetails user) {
		
	 	
	}
	
	public void createUser(UserDetails user,SellerVO sellerVO) {
		System.out.println("SSSSsdsasYYYasdasdasXXXXXXXXXXXXXXXXXXXXXXXXX");

		sellerVO.setSellerEmail(user.getUsername());
		sellerVO.setSellerPassword(user.getPassword());
		sellerVO.setIsConfirm(true);
	
				
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
		// TODO Auto-generated method stub

	}

	@Override
	public void changePassword(String oldPassword, String newPassword) {
		// TODO Auto-generated method stub

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

		System.out.println("=======loadUserByUsername============");
		System.out.println(username);
		System.out.println("=======loadUserByUsername============");
		
		
		SellerVO targetUser =  sellerRepo.findByEmail(username);
		
		// 創建權限列表
		Collection<GrantedAuthority> authorities = new ArrayList<>();
		
		System.out.println("=======loadUserByUsername============");
		System.out.println(targetUser);
		System.out.println("=======loadUserByUsername============");

		
		
		// 參考InMemorySecurity的做法
		if (targetUser == null) {
			throw new UsernameNotFoundException(username);
		} else {
			return new org.springframework.security.core.userdetails.User(targetUser.getSellerEmail(),
					targetUser.getSellerPassword(), targetUser.getIsConfirm(), true, // 用戶帳號是否過期
					true, // 用戶憑證是否過期
					true, // 用戶是否未被鎖定
					authorities // 授權權限列表
			);
		}

	}

}
