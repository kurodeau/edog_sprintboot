package com.productSort.model;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("productSortService")
public class ProductSortService {

	@Autowired
	ProductSortRepository repository;

	public void addProductSortVO(ProductSortVO productSortVO) {
		repository.save(productSortVO);
	}

	public void updateProductSortVO(ProductSortVO productSortVO) {
		repository.save(productSortVO);
	}

	public void deleteProductSortVO(Integer productSortId) {
		if (repository.existsById(productSortId))
			repository.deleteById(productSortId);
	}

	public ProductSortVO getOneProduct(Integer productSortNo) {
		Optional<ProductSortVO> optional = repository.findById(productSortNo);
		return optional.orElse(null);

	}
	
	
	public ProductSortVO getOneProductSortNo(Integer productSortNo) {
		Optional<ProductSortVO> optional = repository.findByProductSortNo(productSortNo);
		return optional.orElse(null);
	}
	

	public List<ProductSortVO> getAll() {
		return 	repository.findAll();
	}


}
