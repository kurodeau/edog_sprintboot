// https://docs.spring.io/spring-data/jpa/docs/current/reference/html/

package com.product.model;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface ProductImgRepository extends JpaRepository<ProductImgVO, Integer> {

	@Transactional
	@Modifying
	@Query(value = "delete from ad where productId =?1", nativeQuery = true)
	void deleteByProductId(int productId);
	

	@Query(value = "SELECT * FROM productImage WHERE productId = ?1", nativeQuery = true)
	List<ProductImgVO> findProductImgVOListByProductId(Integer productId);
	
	@Query(value = "SELECT productImgId FROM productImage WHERE productId = ?1", nativeQuery = true)
	List<ProductImgVO> findProductImgsByProductId(ProductVO productVO);


}