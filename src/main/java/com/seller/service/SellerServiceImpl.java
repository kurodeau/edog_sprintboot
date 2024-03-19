package com.seller.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
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

	public Boolean isUniqueEmail(String email) {
		
		return this.findByOnlyOneEmail(email) == null;
	}

	public void saveUserDetails(SellerVO sellerVO) {

		if (sellerVO == null) {
			throw new UsernameNotFoundException("User not found with username ");
		}

		System.out.println("saveUserDetails" + sellerVO);

		UserDetails userdetails = User.builder().username(sellerVO.getSellerEmail())
				.password(sellerVO.getSellerPassword()).authorities("ROLE_SELLER").build();

		sellerDetailsService.createUser(userdetails, sellerVO);

	}

	public void updateUserDetails(SellerVO sellerVO) {

		if (sellerVO == null) {
			throw new UsernameNotFoundException("User not found with username ");
		}

		UserDetails userdetails = User.builder().username(sellerVO.getSellerEmail())
				.password(sellerVO.getSellerPassword()).authorities("ROLE_SELLER").build();

		sellerDetailsService.changePassword(userdetails, sellerVO);

	}

	public void updateSecureContext(SellerVO sellerVO) {

		if (sellerVO == null) {
			return;
		}

		SecurityContext secCtx = SecurityContextHolder.getContext();

		Authentication authentication = secCtx.getAuthentication();
		Integer sellerLvId = sellerVO.getSellerLvId().getSellerLvId();
		List<GrantedAuthority> authorities = new ArrayList<>();

		switch (sellerLvId) {
		case 1:
			authorities.add(new SimpleGrantedAuthority("ROLE_SELLER"));
			authorities.add(new SimpleGrantedAuthority("ROLE_SELLERLV1"));
			break;
		case 2:
			authorities.add(new SimpleGrantedAuthority("ROLE_SELLER"));
			authorities.add(new SimpleGrantedAuthority("ROLE_SELLERLV2"));
			break;
		case 3:
			authorities.add(new SimpleGrantedAuthority("ROLE_SELLER"));
			authorities.add(new SimpleGrantedAuthority("ROLE_SELLERLV3"));
			break;

		}
		
		SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(sellerVO, null, authorities));

	}

}
