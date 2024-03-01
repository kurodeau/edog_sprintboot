package com.seller.repositary;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.seller.entity.SellerVO;

public interface SellerRepository extends JpaRepository<SellerVO, Integer>{
	@Transactional
	@Modifying
	@Query(value = "DELETE FROM seller WHERE sellerId = ?1", nativeQuery = true)
	void deleteByTheId(int Id);
	
	@Transactional
	@Modifying
	@Query(value = "UPDATE seller SET isConfirm = false WHERE sellerId = ?1", nativeQuery = true)
	void disableSellerById(int sellerId);
	
	
	 @Query(value = "FROM SellerVO WHERE sellerEmail = :email")
	 SellerVO findByEmail(String email);

}


