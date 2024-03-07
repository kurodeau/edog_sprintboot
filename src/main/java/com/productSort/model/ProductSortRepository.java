package com.productSort.model;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface ProductSortRepository extends JpaRepository<ProductSortVO , Integer>{
	
	
	@Transactional
	@Modifying
	@Query(value = "delete from ad where productId =?1", nativeQuery = true)
	void deleteByProductSortId(int productSortId);
	
	
	@Query("SELECT ps FROM ProductSortVO ps WHERE ps.productSortNo = :productSortNo")
	Optional<ProductSortVO> findByProductSortNo(Integer productSortNo);

}
