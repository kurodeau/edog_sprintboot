//package com.petdraw.test;
//
//
//
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
//import com.petdraw.model.PetDrawRepository;
//import com.petdraw.model.PetDrawVO;
//
//@SpringBootApplication
//@ComponentScan("com")
//public class Test_Application_CommandLineRunner implements CommandLineRunner {
//
//    @Autowired
//    PetDrawRepository model;
//
//    public static void main(String[] args) {
//        SpringApplication.run(Test_Application_CommandLineRunner.class);
//    }
//
//    @Override
//    public void run(String... args) throws Exception {
//
//        // ● 新增
//    	 PetDrawVO petDrawVO = new PetDrawVO();
//         petDrawVO.setMemberMain(1);
//         petDrawVO.setMemberPair(2);
//         petDrawVO.setIsMemberLike(true);
//         petDrawVO.setMemberResTime(new Date());
//         petDrawVO.setMemberPairResTime(new Date());
//         petDrawVO.setIsMemberPairLike(false);
//         petDrawVO.setPetDrawTime(new Date());
//         petDrawVO.setPetDrawLog(123.456);
//         petDrawVO.setPetDrawLat(78.90);
//
//
//
//         // ● 修改
//         PetDrawVO updatedPetDrawVO = new PetDrawVO();
//         updatedPetDrawVO.setpetDrawId(1); // 假設現有記錄的ID
//         updatedPetDrawVO.setMemberMain(3);
//         updatedPetDrawVO.setMemberPair(4);
//         updatedPetDrawVO.setIsMemberLike(false);
//         updatedPetDrawVO.setMemberResTime(new Date());
//         updatedPetDrawVO.setMemberPairResTime(new Date());
//         updatedPetDrawVO.setIsMemberPairLike(true);
//         updatedPetDrawVO.setPetDrawTime(new Date());
//         updatedPetDrawVO.setPetDrawLog(789.012);
//         updatedPetDrawVO.setPetDrawLat(34.56);
//
//
//         // ● 刪除
//         model.deleteById(1); // 假設要刪除的記錄ID
//
//         // ● 查詢-findByPrimaryKey
//         Optional<PetDrawVO> optional = model.findById(1); // 假設存在的記錄ID
//         PetDrawVO retrievedPetDrawVO = optional.orElse(null);
//         printPetDrawInfo(retrievedPetDrawVO);
//
//         // ● 查詢-getAll
//         List<PetDrawVO> petDrawList = model.findAll();
//         for (PetDrawVO aPetDraw : petDrawList) {
//             printPetDrawInfo(aPetDraw);
//         }
//     }
//
//     private void printPetDrawInfo(PetDrawVO petDrawVO) {
//         System.out.print("PetDrawId: " + petDrawVO.getpetDrawId() + ",");
//         System.out.print("MemberMain: " + petDrawVO.getMemberMain() + ",");
//         System.out.print("MemberPair: " + petDrawVO.getMemberPair() + ",");
//         System.out.print("IsMemberLike: " + petDrawVO.getIsMemberLike() + ",");
//         System.out.print("MemberResTime: " + petDrawVO.getMemberResTime() + ",");
//         System.out.print("MemberPairResTime: " + petDrawVO.getMemberPairResTime() + ",");
//         System.out.print("IsMemberPairLike: " + petDrawVO.getIsMemberPairLike() + ",");
//         System.out.print("PetDrawTime: " + petDrawVO.getPetDrawTime() + ",");
//         System.out.print("PetDrawLog: " + petDrawVO.getPetDrawLog() + ",");
//         System.out.print("PetDrawLat: " + petDrawVO.getPetDrawLat());
//         // 根據需要列印其他屬性
//         System.out.println();
//     }
// }