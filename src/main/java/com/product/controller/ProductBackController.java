package com.product.controller;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.allenum.ProductStatus;
import com.product.model.ProductImgService;
import com.product.model.ProductImgVO;
import com.product.model.ProductService;
import com.product.model.ProductVO;
import com.productSort.model.ProductSortService;
import com.productSort.model.ProductSortVO;
import com.seller.entity.SellerVO;
import com.seller.service.SellerService;

@Controller
@RequestMapping("/back/product")

public class ProductBackController {
	
	@Autowired
	ProductService productSvc;

	@Autowired
	ProductImgService productImgSvc;

	@Autowired
	ProductSortService pdstSvc;

	@Autowired
	SellerService srSvc;

	@GetMapping("list")
	public String listAllProduct(ModelMap model) {
		return "back-end/back-product-list";
	}
	
	@ModelAttribute("productListData")
	protected List<ProductVO> referenceListData(){
		
		List<ProductVO> list = productSvc.getAll();
		return list;
		
	}
	
	
	@PostMapping("getOne_For_Update")
	public String getOne_For_Update(@RequestParam("productId") String productId , Model model) {
	
		ProductVO productVO = productSvc.getOneProduct(Integer.valueOf(productId));
		
		List<ProductImgVO> productImgVOs = productImgSvc.getProductImgs(Integer.valueOf(productId));
		
		model.addAttribute("productVO" , productVO);
		model.addAttribute("productImageList" , productImgVOs);
		return "back-end/back-product-update_product";	
		
	}
	
	
	@PostMapping("update")
	public String update(@Valid ProductVO productVO, ProductImgVO productImgVO, BindingResult result, Model model,
			@RequestParam("mainImage") MultipartFile[] parts, @RequestParam("subImages") MultipartFile[] partsSec,
			@RequestParam("productSortNo") String productSortNo, @RequestParam("productId") String productId)
			throws IOException {
		/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/

		if (parts[0].isEmpty()) {

			byte[] upFiles = productSvc.getOneProduct(productVO.getProductId()).getProductCoverImg();

			productVO.setProductCoverImg(upFiles);

		} else {
			for (MultipartFile multipartFile : parts) {

				byte[] upFiles = multipartFile.getBytes();

				productVO.setProductCoverImg(upFiles);
			}
		}

		/*************************** 2.開始新增資料 *****************************************/

		SellerVO sellerVO = srSvc.getById(5);
		productVO.setSellerVO(sellerVO);

		ProductSortVO productSortVO = pdstSvc.getOneProductSortNo(Integer.valueOf(productSortNo));
		productVO.setProductSortVO(productSortVO);

		long currentTime = System.currentTimeMillis();
		Timestamp timestamp = new Timestamp(currentTime);

		// 取出原本的售出數量，在更新時存入數字
		ProductVO productInfo = productSvc.getOneProduct(Integer.valueOf(productId));
		productVO.setProductSoldQuantity(productInfo.getProductSoldQuantity());
		productVO.setRatings(productInfo.getRatings());
		productVO.setTotalReviews(productInfo.getTotalReviews());
		

		productVO.setProductStatus(ProductStatus.DISABLED.getStatus());
		productVO.setIsEnabled(true);

		productSvc.updateProduct(productVO);

		if(partsSec.length ==1 && partsSec[0].getBytes().length==0) {
			
			List<ProductImgVO> originalImgs = productImgSvc.getProductImgs(Integer.valueOf(productId));

			for(ProductImgVO previousImgs : originalImgs) {
				
			
				byte[] upFiles = productImgSvc.getOneProductImg(previousImgs.getProductImgId()).getProductImg();
				productImgVO.setProductImg(upFiles);
				productImgSvc.updateProductImg(previousImgs);					
			}
			
			
		}else {
			
			productImgSvc.deleteProductImgs(Integer.valueOf(productId));

			for (MultipartFile multipartFile : partsSec) {
				
				ProductImgVO productImg = new ProductImgVO();
				byte[] buf1 = multipartFile.getBytes();

				productImg.setProductImg(buf1);
				productImg.setProductVO(productVO); // FK
				productImg.setProductImgTime(timestamp);
				productImg.setIsCover(false);
				productImg.setIsEnabled(true);

				productImgSvc.updateProductImg(productImg);

			}
		}
		

		// 3.修改完成,準備轉交(Send the Success view)
		model.addAttribute("success", "-(修改成功");
		productVO = productSvc.getOneProduct(Integer.valueOf(productVO.getProductId()));
		model.addAttribute("productVO", productVO);
		return "redirect:/back/product/list";
	}

	
	
	

}
