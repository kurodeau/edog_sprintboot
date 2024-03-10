package com.orderdetails.model;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service("orderDetailsService")
public class OrderDetailsService {

	@Autowired
	OrderDetailsRepository repository;
	
	public void addOrderDetails(OrderDetailsVO orderDetailsVO) {
		repository.save(orderDetailsVO);
	}
	
	
	public void updateOrderDetails(OrderDetailsVO orderDetailsVO) {
		repository.save(orderDetailsVO);
	}
	
	public void deleteOrderDetails(Integer orderDetailsId) {
		if (repository.existsById(orderDetailsId))
			repository.deleteByOrderDetailsId(orderDetailsId);
//		    repository.deleteById(orderId);
	}
	
	public OrderDetailsVO getOneOrderDetails(Integer orderDetailsId) {
		Optional<OrderDetailsVO> optional = repository.findById(orderDetailsId);
//		return optional.get();
		return optional.orElse(null);  // public T orElse(T other) : 如果值存在就回傳其值，否則回傳other的值
	}
	
	public List<OrderDetailsVO> getAll() {
		return repository.findAll();
	}
	
}
