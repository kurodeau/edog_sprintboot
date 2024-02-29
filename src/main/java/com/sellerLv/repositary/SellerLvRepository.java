package com.sellerLv.repositary;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.sellerLv.entity.SellerLvVO;

public interface SellerLvRepository extends JpaRepository<SellerLvVO, Integer>{
	@Transactional
	@Modifying
	@Query(value = "delete from sellerlv where sellerLvId =?1", nativeQuery = true)
	void deleteByTheId(int Id);

}


