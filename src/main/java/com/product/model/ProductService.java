package com.product.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.IndexControllerMain.FormData;
import com.productSort.model.ProductSortVO;

@Service("productService")
public class ProductService {

	@Autowired
	ProductRepository repository;
	
	
	public void addProduct(ProductVO productVO) {
		repository.save(productVO);
	}

	public void updateProduct(ProductVO productVO) {
		repository.save(productVO);
	}

	public void deleteProduct(Integer productId) {
		if (repository.existsById(productId))
			repository.deleteByProductId(productId);
	}

	public ProductVO getOneProduct(Integer productId) {
		Optional<ProductVO> optional = repository.findById(productId);
		return optional.orElse(null);

	}

	public List<ProductVO> searchProducts(String keyword) {
		List<ProductVO> searchResults = repository.findByKeyword(keyword);
		return searchResults;
	}

	public List<ProductVO> getAll() {

		List<ProductVO> allProducts = repository.findAll();

		List<ProductVO> list = allProducts.stream().filter(pt -> Boolean.TRUE.equals(pt.getIsEnabled()))
				.collect(Collectors.toList());

		return list;
	}

	public List<ProductVO> getSellOutProduct() {

		List<ProductVO> allProducts = repository.findAll();
		List<ProductVO> list = allProducts.stream()
				.filter(pt -> "已售完".equals(pt.getProductStatus()) && Boolean.TRUE.equals(pt.getIsEnabled()))
				.collect(Collectors.toList());
		return list;
	}

	public List<ProductVO> getProductLaunch() {

		List<ProductVO> allProducts = repository.findAll();
		List<ProductVO> list = allProducts.stream()
				.filter(pt -> "已上架".equals(pt.getProductStatus()) && Boolean.TRUE.equals(pt.getIsEnabled()))
				.collect(Collectors.toList());
		return list;
	}

	public List<ProductVO> getProductUnLaunch() {

		List<ProductVO> allProducts = repository.findAll();
		List<ProductVO> list = allProducts.stream()
				.filter(pt -> "未上架".equals(pt.getProductStatus()) && Boolean.TRUE.equals(pt.getIsEnabled()))
				.collect(Collectors.toList());
		return list;
	}

	public void getBy(List<String> animalType, List<String> productCategory, List<Integer> ratings, String priceFrom,
			String priceTo, String keyword) {
//		repository.findProductSortVOProductSortNameInAndTotalStarsInAndProductPriceGreaterThanAndProductPriceLessThanOrKeywordBy(productCategory, ratings, priceFrom, priceTo, keyword);
	}

	public List<ProductVO> compositeQuery(FormData formData) {
		Specification<ProductVO> sp = new Specification<ProductVO>() {

			@Override
			public Predicate toPredicate(Root<ProductVO> root, CriteriaQuery<?> query,
					CriteriaBuilder criteriaBuilder) {

				List<Predicate> predicateList = new ArrayList<Predicate>();

				List<String> typeList = formData.getAnimalType();
				if (typeList != null && typeList.size() > 0) {

					predicateList.add(root.get("animalType").in(typeList));
					System.out.println(predicateList);
//					for (String type : typeList) {				
//						predicateList.add(criteriaBuilder.or(criteriaBuilder.equal(root.get("animalType"), type)));

//					}

				}

				List<Integer> ratingList = formData.getRatings();
				if (ratingList != null && ratingList.size() > 0) {
					if (ratingList.size() == 1) {
						predicateList.add(
								criteriaBuilder.and(criteriaBuilder.equal(root.get("ratings"), ratingList.get(0))));
					} else {
						Collections.sort(ratingList);
						predicateList.add(criteriaBuilder
								.and(criteriaBuilder.greaterThanOrEqualTo(root.get("ratings"), ratingList.get(0))));
					}
				}

				String priceFromStr = formData.getPriceFrom();
				String priceToStr = formData.getPriceTo();
				BigDecimal priceFrom = null, priceTo = null;
				if (priceFromStr != null && priceFromStr.matches("^\\d+$")) {
					priceFrom = new BigDecimal(priceFromStr);
				}

				if (priceToStr != null && priceToStr.matches("^\\d+$")) {
					priceTo = new BigDecimal(priceToStr);
				}

				if (priceFrom != null && priceTo != null) {
					predicateList
							.add(criteriaBuilder.and(criteriaBuilder.between(root.get("price"), priceFrom, priceTo)));
				}

				if (priceFrom != null && priceTo == null) {
					predicateList.add(
							criteriaBuilder.and(criteriaBuilder.greaterThanOrEqualTo(root.get("price"), priceFrom)));
				}

				if (priceFrom == null && priceTo != null) {
					predicateList
							.add(criteriaBuilder.and(criteriaBuilder.lessThanOrEqualTo(root.get("price"), priceTo)));
				}

				Join<ProductVO, ProductSortVO> productSortJoin = root.join("productSortVO");
				List<String> categoryList = formData.getProductCategory();
				if (categoryList != null && !categoryList.isEmpty()) {
					predicateList.add(productSortJoin.get("productCategory").in(categoryList));
				}

				query.orderBy(criteriaBuilder.asc(root.get("productId")));
				
				return criteriaBuilder.and(predicateList.toArray(new Predicate[predicateList.size()]));
			}
		};

		return repository.findAll(sp);

	}

}
