package com.productorder.model;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ad.model.AdVO;


@Service("productOrderService")
public class ProductOrderService {
	
	@Autowired
	ProductOrderRepository repository;

	public void addProductOrder(ProductOrderVO productOrderVO) {
		repository.save(productOrderVO);
	}

	public void updateProductOrder(ProductOrderVO productOrderVO) {
		repository.save(productOrderVO);
	}

	public void deleteProductOrder(Integer orderId) {
		if (repository.existsById(orderId))
			repository.deleteByOrderId(orderId);
	}

	public ProductOrderVO getOneProductOrder(Integer orderId) {
		Optional<ProductOrderVO> optional = repository.findById(orderId);
//		return optional.get();
		return optional.orElse(null);  // public T orElse(T other) : 如果值存在就回傳其值，否則回傳other的值
	}

	public List<ProductOrderVO> getAll() {
		return repository.findAll();
	}
	
public List<ProductOrderVO> getProductOrderPendingConfirm() {
		
		List<ProductOrderVO> allProductOrders = repository.findAll();		
		
		 List<ProductOrderVO> list = allProductOrders.stream()
                 .filter(productOrder -> "1".equals(productOrder.getOrderStatus().toString()))
                 .collect(Collectors.toList());									 
				return list;		
	}

public List<ProductOrderVO> getProductOrderNotAcceptedByBuyer() {
	
	List<ProductOrderVO> allProductOrders = repository.findAll();		
	
	 List<ProductOrderVO> list = allProductOrders.stream()
             .filter(productOrder -> "2".equals(productOrder.getOrderStatus().toString()))
             .collect(Collectors.toList());									 
			return list;		
}

public List<ProductOrderVO> getProductOrderNotAcceptedBySeller() {
	
	List<ProductOrderVO> allProductOrders = repository.findAll();		
	
	 List<ProductOrderVO> list = allProductOrders.stream()
             .filter(productOrder -> "3".equals(productOrder.getOrderStatus().toString()))
             .collect(Collectors.toList());									 
			return list;		
}

public List<ProductOrderVO> getProductOrderAccepted() {
	
	List<ProductOrderVO> allProductOrders = repository.findAll();		
	
	 List<ProductOrderVO> list = allProductOrders.stream()
             .filter(productOrder -> "4".equals(productOrder.getOrderStatus().toString()))
             .collect(Collectors.toList());									 
			return list;		
}

public List<ProductOrderVO> getProductOrderSellerProcessing() {
	
	List<ProductOrderVO> allProductOrders = repository.findAll();		
	
	 List<ProductOrderVO> list = allProductOrders.stream()
             .filter(productOrder -> "5".equals(productOrder.getOrderStatus().toString())
            		 				|| "6".equals(productOrder.getOrderStatus().toString()))
             .collect(Collectors.toList());									 
			return list;		
}

public List<ProductOrderVO> getProductOrderItemShipped() {
	
	List<ProductOrderVO> allProductOrders = repository.findAll();		
	
	 List<ProductOrderVO> list = allProductOrders.stream()
             .filter(productOrder -> "6".equals(productOrder.getOrderStatus().toString()))
             .collect(Collectors.toList());									 
			return list;		
}

public List<ProductOrderVO> getProductOrderCompleted() {
	
	List<ProductOrderVO> allProductOrders = repository.findAll();		
	
	 List<ProductOrderVO> list = allProductOrders.stream()
             .filter(productOrder -> "7".equals(productOrder.getOrderStatus().toString()))
             .collect(Collectors.toList());									 
			return list;		
}

public List<ProductOrderVO> getProductOrderCanceled() {
	
	List<ProductOrderVO> allProductOrders = repository.findAll();		
	
	 List<ProductOrderVO> list = allProductOrders.stream()
             .filter(productOrder -> "2".equals(productOrder.getOrderStatus().toString())
            		 			  || "3".equals(productOrder.getOrderStatus().toString()))
             .collect(Collectors.toList());									 
			return list;		
}


}
