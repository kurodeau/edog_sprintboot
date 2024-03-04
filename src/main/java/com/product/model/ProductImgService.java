package com.product.model;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service("productImg")
public class ProductImgService {
	
	@Autowired
	ProductImgRepository repository ;
	
	public void addProductImg(ProductImgVO productImgVO) {
		repository.save(productImgVO);
	}
	
	public void updateProduct(ProductImgVO productImgVO) {
		repository.save(productImgVO);
	}
	
	public void deleteProduct(Integer productId) {
		if(repository.existsById(productId))
		   repository.deleteByProductId(productId);
	}
	
	public ProductImgVO getOneProduct(Integer productId) {
		Optional<ProductImgVO> optional = repository.findById(productId);
		return optional.orElse(null);
		
	}
	
	public List<ProductImgVO> getAll(){
	
		return repository.findAll();
	}
	
}
