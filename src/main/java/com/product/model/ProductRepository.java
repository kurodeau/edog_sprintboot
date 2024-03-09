// https://docs.spring.io/spring-data/jpa/docs/current/reference/html/

package com.product.model;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface ProductRepository extends JpaRepository<ProductVO, Integer>, JpaSpecificationExecutor<ProductVO> {

	@Transactional
	@Modifying
	@Query(value = "delete from ad where productId =?1", nativeQuery = true)
	void deleteByProductId(int productId);
	
	
	@Query("SELECT p FROM ProductVO p WHERE p.productName LIKE %:keyword%")
	List<ProductVO> findByKeyword(@Param("keyword") String keyword);


//	@FindWithOptionalParams
//	void findProductSortVOProductSortNameInAndTotalStarsInAndProductPriceGreaterThanAndProductPriceLessThanOrKeywordBy(
////			List<String> animalType, 
//			List<String> productCategory, 
//			List<Integer> ratings, 
//			String priceFrom,
//			String priceTo, 
//			String keyword);
	


}