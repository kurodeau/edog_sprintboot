//package com;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import com.seller.entity.SellerVO;
//import com.seller.service.SellerService;
//
//@RestController
//@RequestMapping("/seller")
//public class IndexControllerSeller {
//
//	
//    private SellerService sellerSvc;
//
//    public SellerService getSellerSvc() {
//		return sellerSvc;
//	}
//    
//    @Autowired
//	public void setSellerSvc(SellerService sellerSvc) {
//		this.sellerSvc = sellerSvc;
//	}
//
//	@GetMapping("/sellerMain")
//    public void sellerMain(Model model) {
////        return "/seller/seller-main";
//    }
//
//    @GetMapping("/sellerSendtext")
//    public void loginError(Model model) {
////        return "/seller/seller-sendtext";
//    }
//    // /seller/sellerRegister
//    @PostMapping("/sellerRegister")
//    public String register(@RequestBody SellerVO sellerVO, Model model) {
////        sellerSvc.saveUserDetails(sellerVO);
//        return "/seller/seller-register";
//    }
//}
