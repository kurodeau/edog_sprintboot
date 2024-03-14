package com.buyer.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.buyer.entity.BuyerVO;
import com.seller.entity.SellerVO;

public interface BuyerRepository extends JpaRepository<BuyerVO, Integer> {
	@Transactional
	@Modifying
	@Query(value = "delete from buyer where memberId =?1", nativeQuery = true)
	void deleteByMemberId(int memberId);
	
	@Transactional
	@Modifying
	@Query(value = "UPDATE buyer SET isConfirm = false WHERE memberId = ?1", nativeQuery = true)
	void disableBuyerById(int memberId);
	
	
	@Query(value = "SELECT * FROM buyer WHERE memberEmail = ?1 LIMIT 1", nativeQuery = true)
	BuyerVO findByOnlyOneEmail(String memberEmail);

	
	@Query(value = "FROM BuyerVO WHERE memberEmail = :email")
    BuyerVO findByEmail(String email);
	 
	 
    @Query(value = "FROM BuyerVO WHERE memberMobile = :phone")
	BuyerVO findByOnlyPhone(String phone);
	
}