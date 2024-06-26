package com.product.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
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
@RequestMapping("/front/seller/product")

public class ProductController {

	@Autowired
	ProductService productSvc;

	@Autowired
	ProductImgService productImgSvc;

	@Autowired
	ProductSortService pdstSvc;

	@Autowired
	SellerService srSvc;

	@GetMapping("add")
	public String sellerProductAdd(Model model) {

		ProductVO productVO = new ProductVO();
		productVO.setProductName("假商品名稱");
		productVO.setPrice(BigDecimal.valueOf(99.99));
		productVO.setProductStockQuantity(100);
		productVO.setProductDetails("這是假商品的描述");
		productVO.setProductStatus("在售");
		productVO.setProductCreationTime(new Timestamp(System.currentTimeMillis()));
		productVO.setRatings(4);
		productVO.setTotalReviews(50);
		productVO.setIsEnabled(true);
		
		
		model.addAttribute("productVO", productVO);

		return "front-end/seller/seller-product-add";
	}

	@PostMapping("insert")
	public String insert(@Valid ProductVO productVO, BindingResult result, Model model,
			@RequestParam("mainImage") MultipartFile[] parts, 
			@RequestParam("subImages") MultipartFile[] partsSec,
			@RequestParam("productSortNo") String productSortNo) throws IOException {

		/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
		
		result = removeFieldError(productVO, result, "mainImage");
		result = removeFieldError(productVO, result, "subImages");


		if (parts[0].isEmpty()) {
			model.addAttribute("errorMessage", "商品照片:請上傳照片");
		} else {
			for (MultipartFile multipartFile : parts) {
				byte[] buf = multipartFile.getBytes();
				productVO.setProductCoverImg(buf);
			}
		}
		
		
		if (partsSec[0].isEmpty()) {
			model.addAttribute("errorMessage", "商品照片:請上傳其他照片");
		}
		

		if (result.hasErrors() || parts[0].isEmpty() || partsSec[0].isEmpty()) {
			System.out.println("XXXXXXXX"+result);
			return "front-end/seller/seller-product-add";
		}

		/*************************** 2.開始新增資料 *****************************************/

		SecurityContext secCtx = SecurityContextHolder.getContext();
        Authentication authentication = secCtx.getAuthentication();
        SellerVO sellerVO = (SellerVO) authentication.getPrincipal();		
		
		productVO.setSellerVO(sellerVO);

		ProductSortVO productSortVO = pdstSvc.getOneProductSortNo(Integer.valueOf(productSortNo));
		productVO.setProductSortVO(productSortVO);

		long currentTime = System.currentTimeMillis();
		Timestamp timestamp = new Timestamp(currentTime);
		productVO.setProductCreationTime(timestamp);
		productVO.setProductSoldQuantity(0);
		productVO.setProductStatus(ProductStatus.DISABLED.getStatus());
		productVO.setRatings(0);
		productVO.setTotalReviews(0);
		productVO.setIsEnabled(true);

		productSvc.addProduct(productVO); // 新增一個product 產生PK

		for (MultipartFile multipartFile : partsSec) {

			ProductImgVO productImg = new ProductImgVO();

			byte[] buf1 = multipartFile.getBytes();

			productImg.setProductImg(buf1);
			productImg.setProductVO(productVO); // FK
			productImg.setProductImgTime(timestamp);
			productImg.setIsCover(false);
			productImg.setIsEnabled(true);
			productImgSvc.addProductImg(productImg);

		}

		/*************************** 3.新增完成,準備轉交(Send the Success view) **************/
		List<ProductVO> list = productSvc.getAll();
		model.addAttribute("ProductListData", list);
		model.addAttribute("success", "-(新增成功");
		return "redirect:/front/seller/product/productlist";
	}

	@PostMapping("getOne_For_Update")
	public String getOne_For_Update(@RequestParam("productId") String productId, ModelMap model) {
		/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
		/*************************** 2.開始查詢資料 *****************************************/
		ProductVO productVO = productSvc.getOneProduct(Integer.valueOf(productId));

		Integer productSortVO = productVO.getProductSortVO().getProductSortNo();
		
		List<ProductImgVO> productImgVOs = productImgSvc.getProductImgs(Integer.valueOf(productId));

		/*************************** 3.查詢完成,準備轉交(Send the Success view) **************/
		model.addAttribute("productVO", productVO);
		model.addAttribute("productSortVO", productSortVO);
		model.addAttribute("productImageList", productImgVOs);
		return "front-end/seller/seller-product-update_product";
	}

	@PostMapping("update")
	public String update(@Valid ProductVO productVO,BindingResult result, Model model,
			@RequestParam("mainImage") MultipartFile[] parts, 
			@RequestParam("subImages") MultipartFile[] partsSec,
			@RequestParam("productSortNo") String productSortNo, 
			@RequestParam("productId") String productId)
			throws IOException {
		
		/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
		
		System.out.println(result);

		
		if (parts[0].isEmpty()) {

			byte[] upFiles = productSvc.getOneProduct(productVO.getProductId()).getProductCoverImg();

			productVO.setProductCoverImg(upFiles);

		} else {
			for (MultipartFile multipartFile : parts) {

				byte[] upFiles = multipartFile.getBytes();

				productVO.setProductCoverImg(upFiles);
			}
		}
		
		
		if(result.hasErrors()) {
			
			
			ProductVO productVO1 = productSvc.getOneProduct(Integer.valueOf(productId));

			Integer productSortVO = productVO1.getProductSortVO().getProductSortNo();
			
			model.addAttribute("productSortVO", productSortVO);
			
			return "front-end/seller/seller-product-update_product";
		}
		
		
		

		/*************************** 2.開始新增資料 *****************************************/

		SecurityContext secCtx = SecurityContextHolder.getContext();
        Authentication authentication = secCtx.getAuthentication();
        SellerVO sellerVO = (SellerVO) authentication.getPrincipal();	
		productVO.setSellerVO(sellerVO);

	
		
		long currentTime = System.currentTimeMillis();
		Timestamp timestamp = new Timestamp(currentTime);

		// 取出原本的售出數量，在更新時存入數字
		ProductVO productInfo = productSvc.getOneProduct(Integer.valueOf(productId));
		productVO.setProductSoldQuantity(productInfo.getProductSoldQuantity());
		productVO.setRatings(productInfo.getRatings());
		productVO.setTotalReviews(productInfo.getTotalReviews());
		

		productVO.setProductStatus(ProductStatus.DISABLED.getStatus());
		productVO.setIsEnabled(true);

		ProductSortVO productSortVO = pdstSvc.getOneProductSortNo(Integer.valueOf(productSortNo));
		productVO.setProductSortVO(productSortVO);
		
		
		productSvc.updateProduct(productVO);
		
		

		if(partsSec.length ==1 && partsSec[0].getBytes().length==0) {
			
			List<ProductImgVO> originalImgs = productImgSvc.getProductImgs(Integer.valueOf(productId));
				
			ProductImgVO productImgVO = new ProductImgVO();
			
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
		return "redirect:/front/seller/product/productlist";
	}

	@PostMapping("delete")
	public String delete(@RequestParam("productId") String productId, ModelMap model) {
		/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
		/*************************** 2.開始刪除資料 *****************************************/
		productSvc.deleteProduct(Integer.valueOf(productId));
		/*************************** 3.刪除完成,準備轉交(Send the Success view) **************/
		List<ProductVO> list = productSvc.getAll();
		model.addAttribute("productListData", list);
		model.addAttribute("success", "-(刪除成功)");
		return null;
	}

	@PostMapping("launch")
	public String launch(@RequestParam("productId") String productId, ModelMap model) {
		ProductVO productVO = productSvc.getOneProduct(Integer.valueOf(productId));
		productVO.setProductStatus(ProductStatus.ENABLED.getStatus());
		productSvc.updateProduct(productVO);

		return "redirect:/front/seller/product/productlist";
	}

	@PostMapping("unLaunch")
	public String UnLaunch(@RequestParam("productId") String productId, ModelMap model) {
		ProductVO productVO = productSvc.getOneProduct(Integer.valueOf(productId));
		productVO.setProductStatus(ProductStatus.DISABLED.getStatus());
		productSvc.updateProduct(productVO);
		return "redirect:/front/seller/product/productlist";

	}

	@PostMapping("deleteStatus")
	public String deleteStatus(@RequestParam("productId") String productId, ModelMap model) {

		ProductVO productVO = productSvc.getOneProduct(Integer.valueOf(productId));
		productVO.setIsEnabled(false);
		productSvc.updateProduct(productVO);

		return "redirect:/front/seller/product/productlist";
	}

//	 去除BindingResult中某個欄位的FieldError紀錄
	public BindingResult removeFieldError(ProductVO productVO, BindingResult result, String removedFieldname) {
		List<FieldError> errorsListToKeep = result.getFieldErrors().stream()
				.filter(fieldname -> !fieldname.getField().equals(removedFieldname)).collect(Collectors.toList());
		result = new BeanPropertyBindingResult(productVO, "productVO");
		for (FieldError fieldError : errorsListToKeep) {
			result.addError(fieldError);
		}
		return result;
	}

	public BindingResult removeFieldError1(ProductImgVO productImgVO, BindingResult result, String removedFieldname) {
		List<FieldError> errorsListToKeep = result.getFieldErrors().stream()
				.filter(fieldname -> !fieldname.getField().equals(removedFieldname)).collect(Collectors.toList());
		result = new BeanPropertyBindingResult(productImgVO, "productImgVO");
		for (FieldError fieldError : errorsListToKeep) {
			result.addError(fieldError);
		}
		return result;
	}

}
