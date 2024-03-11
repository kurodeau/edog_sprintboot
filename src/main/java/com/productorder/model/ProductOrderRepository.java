package com.productorder.model;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.orderdetails.model.OrderDetailsVO;


public interface ProductOrderRepository extends JpaRepository<ProductOrderVO, Integer>{
	@Transactional
	@Modifying
	@Query(value = "delete from productOrder where orderId =?1", nativeQuery = true)
	void deleteByOrderId(int orderId);

	@Query(value = "SELECT * FROM productOrder WHERE sellerId = ?1", nativeQuery = true)
	List<ProductOrderVO> findBySellerId(int sellerId);

}
