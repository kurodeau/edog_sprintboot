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
	
	public void updateProductImg(ProductImgVO productImgVO) {
		repository.save(productImgVO);
	}
	
	public void deleteProductImg(Integer productImgId) {
		if(repository.existsById(productImgId))
		   repository.deleteByProductId(productImgId);
	}
	
	public ProductImgVO getOneProductImg(Integer productImgId) {
		Optional<ProductImgVO> optional = repository.findById(productImgId);
		return optional.orElse(null);
		
	}
	
	public List<ProductImgVO> getProductImgs(Integer productId) {
		List<ProductImgVO> productImgVOs = repository.findByProductId(productId);
		
		return productImgVOs;
	}
	
	
	public List<ProductImgVO> getAll(){
	
		return repository.findAll();
	}
	
}
