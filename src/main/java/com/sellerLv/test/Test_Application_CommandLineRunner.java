//package com.sellerLv.test;
//
//import java.math.BigDecimal;
//import java.util.List;
//import java.util.Optional;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.boot.SpringApplication;
//import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.context.annotation.ComponentScan;
//
//import com.sellerLv.entity.SellerLvVO;
//import com.sellerLv.repositary.SellerLvRepository;
//
//@SpringBootApplication
//@ComponentScan("com")
//public class Test_Application_CommandLineRunner implements CommandLineRunner {
//
//	@Autowired
//	SellerLvRepository repository;
//
//	public static void main(String[] args) {
//		SpringApplication.run(Test_Application_CommandLineRunner.class);
//	}
//
//	@Override
//	public void run(String... args) throws Exception {
//
//		// ● 新增
//		SellerLvVO sellerLvVO = new SellerLvVO();
//		sellerLvVO.setLvName("TEST");
//		sellerLvVO.setPlatformCommission(new BigDecimal("0.05"));
//		sellerLvVO.setAdAllowType(1);
//		sellerLvVO.setIsExportGoldflow(true);
//		sellerLvVO.setFreightSub(2);
//		sellerLvVO.setReturnSubPerMonth(5);
//		sellerLvVO.setIsShowPriority(true);
//		sellerLvVO.setShelvesNumber(10);
//		repository.save(sellerLvVO);
//
//		// ● 修改
//		SellerLvVO updatedSellerLvVO = new SellerLvVO();
//		updatedSellerLvVO.setSellerLvId(7); // Assuming the ID of the existing record
//		updatedSellerLvVO.setLvName("TEST22");
//		updatedSellerLvVO.setPlatformCommission(new BigDecimal("0.03"));
//		updatedSellerLvVO.setAdAllowType(2);
//		updatedSellerLvVO.setIsExportGoldflow(false);
//		updatedSellerLvVO.setFreightSub(3);
//		updatedSellerLvVO.setReturnSubPerMonth(7);
//		updatedSellerLvVO.setIsShowPriority(false);
//		updatedSellerLvVO.setShelvesNumber(15);
//		repository.save(updatedSellerLvVO);
//
//		// ● 刪除
////		repository.deleteByTheId(7); // Assuming the ID of the record to delete
//
//		// ● 查詢-findByPrimaryKey
//		Optional<SellerLvVO> optional = repository.findById(2); // Assuming the ID of an existing record
//		SellerLvVO retrievedSellerLvVO = optional.get();
//		printSellerLvInfo(retrievedSellerLvVO);
//
//		// ● 查詢-getAll
//		List<SellerLvVO> sellerLvList = repository.findAll();
//		for (SellerLvVO aSellerLv : sellerLvList) {
//			printSellerLvInfo(aSellerLv);
//		}
//	}
//
//	private void printSellerLvInfo(SellerLvVO sellerLvVO) {
//		System.out.print("SellerLvId: " + sellerLvVO.getSellerLvId() + ",");
//		System.out.print("LvName: " + sellerLvVO.getLvName() + ",");
//		System.out.print("PlatformCommission: " + sellerLvVO.getPlatformCommission() + ",");
//		System.out.print("AdAllowType: " + sellerLvVO.getAdAllowType() + ",");
//		System.out.print("IsExportGoldflow: " + sellerLvVO.getIsExportGoldflow() + ",");
//		System.out.print("FreightSub: " + sellerLvVO.getFreightSub() + ",");
//		System.out.print("ReturnSubPerMonth: " + sellerLvVO.getReturnSubPerMonth() + ",");
//		System.out.print("IsShowPriority: " + sellerLvVO.getIsShowPriority() + ",");
//		System.out.print("ShelvesNumber: " + sellerLvVO.getShelvesNumber());
//		System.out.println();
//	}
//}
