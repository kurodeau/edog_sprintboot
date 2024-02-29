package com.seller.repositary;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.seller.entity.SellerVO;

public interface SellerRepository extends JpaRepository<SellerVO, Integer>{
	@Transactional
	@Modifying
	@Query(value = "delete from seller where sellerId =?1", nativeQuery = true)
	void deleteByTheId(int Id);

}


