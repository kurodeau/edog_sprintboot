// https://docs.spring.io/spring-data/jpa/docs/current/reference/html/

package com.orderdetails.model;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;


public interface OrderDetailsRepository extends JpaRepository<OrderDetailsVO, Integer>  {
	
	@Transactional
	@Modifying
	@Query(value = "delete from orderDetails where orderDetailsId =?1", nativeQuery = true)
	void deleteByOrderDetailsId(int orderDetailsId);
	
	
	@Transactional
	@Query(value = "SELECT * FROM orderDetails WHERE orderId = ?1", nativeQuery = true)
	List<OrderDetailsVO> findByOrderId(Integer orderId);

}
