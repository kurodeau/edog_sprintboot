//package com.seller.test;
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
//import com.seller.entity.SellerVO;
//import com.seller.repositary.SellerRepository;
//
//@SpringBootApplication
//@ComponentScan("com")
//public class Test_Application_CommandLineRunner implements CommandLineRunner {
//
//    @Autowired
//    SellerRepository repository;
//
//    public static void main(String[] args) {
//        SpringApplication.run(Test_Application_CommandLineRunner.class);
//    }
//
//    @Override
//    public void run(String... args) throws Exception {
//
//        // ● 新增
//        SellerVO sellerVO = new SellerVO();
//        sellerVO.setSellerEmail("test@example.com");
//        sellerVO.setSellerCompany("Test Company");
//        sellerVO.setSellerTaxId("12345678");
//        sellerVO.setSellerCapital(100000);
//        sellerVO.setSellerContact("John Doe");
//        sellerVO.setSellerCompanyPhone("1321321");
//        sellerVO.setSellerCompanyExtension("123");
//        sellerVO.setSellerMobile("123456");
//        sellerVO.setSellerAddress("123 Main Street");
//        sellerVO.setSellerPassword("password123");
//        sellerVO.setSellerBankAccount("987654321");
//        sellerVO.setSellerBankCode("456");
//        sellerVO.setSellerBankAccountNumber("7890123456");
//        sellerVO.setSellerCreateTime(new Date());
//        sellerVO.setIsConfirm(false);
//
//        // Set other properties accordingly
//        repository.save(sellerVO);
//
//        // ● 修改
//        SellerVO updatedSellerVO = new SellerVO();
//        updatedSellerVO.setSellerId(1); // Assuming the ID of the existing record
//        updatedSellerVO.setSellerEmail("updated@example.com");
//        updatedSellerVO.setSellerCompany("Updated Company");
//        updatedSellerVO.setSellerTaxId("87654321");
//        updatedSellerVO.setSellerCapital(150000);
//        updatedSellerVO.setSellerContact("Jane Doe");
//        updatedSellerVO.setSellerCompanyPhone("1321321");
//        updatedSellerVO.setSellerCompanyExtension("456");
//        updatedSellerVO.setSellerMobile("1321321");
//        updatedSellerVO.setSellerAddress("456 Oak Street");
//        updatedSellerVO.setSellerPassword("updatedpassword");
//        updatedSellerVO.setSellerBankAccount("123456789");
//        updatedSellerVO.setSellerBankCode("789");
//        updatedSellerVO.setSellerBankAccountNumber("6543210987");
//        updatedSellerVO.setSellerCreateTime(new Date());
//        updatedSellerVO.setIsConfirm(true);
//
//        // Set other properties accordingly
//        repository.save(updatedSellerVO);
//
//        // ● 刪除
//        // repository.deleteById(1); // Assuming the ID of the record to delete
//
//        // ● 查詢-findByPrimaryKey
//        Optional<SellerVO> optional = repository.findById(1); // Assuming the ID of an existing record
//        SellerVO retrievedSellerVO = optional.orElse(null);
//        printSellerInfo(retrievedSellerVO);
//
//        // ● 查詢-getAll
//        List<SellerVO> sellerList = repository.findAll();
//        for (SellerVO aSeller : sellerList) {
//            printSellerInfo(aSeller);
//        }
//    }
//
//    private void printSellerInfo(SellerVO sellerVO) {
//        System.out.print("SellerId: " + sellerVO.getSellerId() + ",");
//        System.out.print("SellerEmail: " + sellerVO.getSellerEmail() + ",");
//        System.out.print("SellerCompany: " + sellerVO.getSellerCompany() + ",");
//        System.out.print("SellerTaxId: " + sellerVO.getSellerTaxId() + ",");
//        System.out.print("SellerCapital: " + sellerVO.getSellerCapital() + ",");
//        System.out.print("SellerContact: " + sellerVO.getSellerContact() + ",");
//        System.out.print("SellerCompanyPhone: " + sellerVO.getSellerCompanyPhone() + ",");
//        System.out.print("SellerCompanyExtension: " + sellerVO.getSellerCompanyExtension() + ",");
//        System.out.print("SellerMobile: " + sellerVO.getSellerMobile() + ",");
//        System.out.print("SellerAddress: " + sellerVO.getSellerAddress() + ",");
//        System.out.print("SellerPassword: " + sellerVO.getSellerPassword() + ",");
//        System.out.print("SellerBankAccount: " + sellerVO.getSellerBankAccount() + ",");
//        System.out.print("SellerBankCode: " + sellerVO.getSellerBankCode() + ",");
//        System.out.print("SellerBankAccountNumber: " + sellerVO.getSellerBankAccountNumber() + ",");
//        System.out.print("SellerCreateTime: " + sellerVO.getSellerCreateTime() + ",");
//        System.out.print("IsConfirm: " + sellerVO.getIsConfirm());
//        // Print other properties accordingly
//        System.out.println();
//    }
//}
