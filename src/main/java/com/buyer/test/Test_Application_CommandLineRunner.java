//package com.buyer.test;
//
//import java.math.BigDecimal;
//import java.util.Date;
//import java.util.List;
//import java.util.Optional;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.boot.SpringApplication;
//import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.context.annotation.ComponentScan;
//
//import com.buyer.entity.BuyerVO;
//import com.buyer.model.BuyerRepository;
//
//@SpringBootApplication
//@ComponentScan("com")
//public class Test_Application_CommandLineRunner implements CommandLineRunner {
//
//    @Autowired
//    BuyerRepository repository;
//
//    public static void main(String[] args) {
//        SpringApplication.run(Test_Application_CommandLineRunner.class);
//    }
//
//    @Override
//    public void run(String... args) throws Exception {
//
//        // ● 新增
//    	BuyerVO buyerVO = new BuyerVO();
//       	buyerVO.setMemberEmail("add@example.com");
//    	buyerVO.setThirdFrom(null);
//    	buyerVO.setMemberName("soyo");
//    	buyerVO.setMemberPhone("0229345678");
//    	buyerVO.setMemberMobile("0988123456");
//    	buyerVO.setMemberBirthday( new Date() );
//    	buyerVO.setMemberPassword("passwo123");
//    	buyerVO.setMemberAddress("100台北市中正區重慶南路一段122號");
//    	buyerVO.setIsMemberEmail(false);
//    	buyerVO.setMemberRegistrationTime( new Date() );
//    	buyerVO.setPetName("木木鴞");
//    	buyerVO.setPetImg(null);
//    	buyerVO.setPetImgUploadTime( new Date() );
//    	buyerVO.setPetVaccName1("默德納之類的");
//        buyerVO.setPetVaccTime1( new Date() );
//        buyerVO.setPetVaccName2(null);
//        buyerVO.setPetVaccTime2(null);
//        buyerVO.setIsConfirm(true);
//        
//        // Set other properties accordingly
//        repository.save(buyerVO);
//
//        // ● 修改
//    	BuyerVO updatedBuyerVO = new BuyerVO();
//    	updatedBuyerVO.setMemberId(1);
//    	// 信箱等同帳號, 不許改
////    	updatedBuyerVO.setMemberEmail("add@example.com");
//    	updatedBuyerVO.setThirdFrom("null");
//    	updatedBuyerVO.setMemberName("mutsumi");
//    	updatedBuyerVO.setMemberPhone("079345678");
//    	updatedBuyerVO.setMemberMobile("0966987654");
//    	updatedBuyerVO.setMemberBirthday( new Date() );
//    	updatedBuyerVO.setMemberPassword("999pasw");
//    	updatedBuyerVO.setMemberAddress("220新北市板橋區永豐街42-8號");
//    	updatedBuyerVO.setIsMemberEmail(false);
//    	updatedBuyerVO.setMemberRegistrationTime( new Date() );
//    	updatedBuyerVO.setPetName("木木鴞你不准進化");
//    	updatedBuyerVO.setPetImg(null);
//    	updatedBuyerVO.setPetImgUploadTime( new Date() );
//    	updatedBuyerVO.setPetVaccName1("YEEEEEE苗");
//    	updatedBuyerVO.setPetVaccTime1( new Date() );
//        updatedBuyerVO.setPetVaccName2(null);
//        updatedBuyerVO.setPetVaccTime2(null);
//        updatedBuyerVO.setIsConfirm(true);
//
//        // Set other properties accordingly
//        repository.save(updatedBuyerVO);
//
//        // ● 刪除
//        // repository.deleteById(1); // Assuming the ID of the record to delete
//
//        // ● 查詢-findByPrimaryKey
//        Optional<BuyerVO> optional = repository.findById(1); // Assuming the ID of an existing record
//        BuyerVO retrievedBuyerVO = optional.orElse(null);
//        printBuyerInfo(retrievedBuyerVO);
//
//        // ● 查詢-getAll
//        List<BuyerVO> buyerList = repository.findAll();
//        for (BuyerVO aBuyer : buyerList) {
//        	printBuyerInfo(aBuyer);
//        }
//    }
//
//    private void printBuyerInfo(BuyerVO buyerVO) {
//        System.out.print("memberId: " + buyerVO.getMemberId() + ",");
//        System.out.print("memberEmail: " + buyerVO.getMemberEmail() + ",");
//        System.out.print("thirdFrom: " + buyerVO.getThirdFrom() + ",");
//        System.out.print("memberName: " + buyerVO.getMemberName() + ",");
//        System.out.print("memberPhone: " + buyerVO.getMemberPhone() + ",");
//        System.out.print("memberMobile: " + buyerVO.getMemberMobile() + ",");
//        System.out.print("memberBirthday: " + buyerVO.getMemberBirthday() + ",");
//        System.out.print("memberPassword: " + buyerVO.getMemberPassword() + ",");
//        System.out.print("memberAddress: " + buyerVO.getMemberAddress() + ",");
//        System.out.print("isMemberEmail: " + buyerVO.getIsMemberEmail() + ",");
//        System.out.print("memberRegistrationTime: " + buyerVO.getMemberRegistrationTime() + ",");
//        System.out.print("petName: " + buyerVO.getPetName() + ",");
//        System.out.print("petImgUploadTime: " + buyerVO.getPetImg() + ",");
//        System.out.print("petVaccName1: " + buyerVO.getPetImgUploadTime() + ",");
//        System.out.print("petVaccTime1: " + buyerVO.getPetVaccName1() + ",");
//        System.out.print("petVaccName2: " + buyerVO.getPetVaccTime1() + ",");
//        System.out.print("petVaccTime2: " + buyerVO.getPetVaccName2() + ",");
//        System.out.print("petVaccTime2: " + buyerVO.getPetVaccTime2() + ","); 
//        System.out.print("isConfirm: " + buyerVO.getIsConfirm());        
//        // Print other properties accordingly
//        System.out.println();
//    }
//}
