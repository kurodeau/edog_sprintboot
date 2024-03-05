package com.product.model;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service("productService")
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
	
	public List<ProductVO> getSellOutProduct(){
		
		
		List<ProductVO> allProducts = repository.findAll();
		List<ProductVO> list = allProducts.stream()
										  .filter(pt -> "已出售".equals(pt.getProductStatus()))
										  .collect(Collectors.toList());
		return list;
	}
	
	
	public List<ProductVO> getProductUnLaunch(){
		
		List<ProductVO> allProducts = repository.findAll();
		List<ProductVO> list = allProducts.stream()
										  .filter(pt -> "已下架".equals(pt.getProductStatus()))
										  .collect(Collectors.toList());
		return list;										  
	}
	
	
}
