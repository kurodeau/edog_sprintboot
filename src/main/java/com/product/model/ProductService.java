package com.product.model;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service("adService")
public class ProductService {
	
	@Autowired
	ProductRepository repository ;
	
	public void addProduct(ProductVO productVO) {
		repository.save(productVO);
	}
	
	public void updateProduct(ProductVO productVO) {
		repository.save(productVO);
	}
	
	public void deleteProduct(Integer productId) {
		if(repository.existsById(productId))
		   repository.deleteByProductId(productId);
	}
	
	public ProductVO getOneProduct(Integer productId) {
		Optional<ProductVO> optional = repository.findById(productId);
		return optional.orElse(null);
		
	}
	
	public List<ProductVO> getAll(){
	
		return repository.findAll();
	}
	
}
