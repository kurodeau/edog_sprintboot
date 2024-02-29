//package com.test.Autowired2;
//
//import java.sql.*;
//import java.util.HashSet;
//import java.util.LinkedHashSet;
//import java.util.List;
//import java.util.Optional;
//import java.util.Set;
//
//import javax.sql.DataSource;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.ResponseBody;
//
//import com.dept.model.DeptService;
//import com.dept.model.DeptVO;
//import com.user.model.EmpService;
//import com.user.model.EmpVO;
//
//@Controller
//public class Demo_SpringDataJPA_Controller_testDS5 {
//
//	@Autowired
//	DeptService deptService;
//
//	@RequestMapping("/testDS5")
//	@ResponseBody
//	public String testDS() throws SQLException {

		//● 新增-1(一方dept2.hbm.xml必須有cascade="save-update" 或cascade="all"的設定)(雖然強大,不過實務上並不常用)(但,可用在訂單主檔與明細檔一次新增成功)
//		DeptVO deptVO = new DeptVO(); // 部門POJO
//		Set<EmpVO> set = new LinkedHashSet<EmpVO>();// 準備置入員工數人,以便cascade="save-update"的測試
//
//		EmpVO empXX = new EmpVO();   // 員工POJO1
//		empXX.setEname("吳15");
//		empXX.setJob("MANAGER15");
//		empXX.setHiredate(java.sql.Date.valueOf("2001-01-15"));
//		empXX.setSal(new Double(15000));
//		empXX.setComm(new Double(150));
//		empXX.setDeptVO(deptVO);
//
//		EmpVO empYY = new EmpVO();   // 員工POJO2
//		empYY.setEname("吳16");
//		empYY.setJob("MANAGER16");
//		empYY.setHiredate(java.sql.Date.valueOf("2001-01-16"));
//		empYY.setSal(new Double(16000));
//		empYY.setComm(new Double(160));
//		empYY.setDeptVO(deptVO);
//
//		set.add(empXX);
//		set.add(empYY);
//
//		deptVO.setDname("製造部");
//		deptVO.setLoc("中國江西");
//		deptVO.setEmps(set);
//		deptService.addDept(deptVO);

		//● 修改-1(一方dept2.hbm.xml必須有cascade="save-update" 或 cascade="all"的設定)(雖然強大,不過實務上並不常用)(但,可視情況使用之)
//		DeptVO deptVO = new DeptVO(); // 部門POJO
//		Set<EmpVO> set = new HashSet<EmpVO>(); // 準備置入員工數人,以便cascade="save-update"的測試
//
//		EmpVO empXX = new EmpVO(); // 員工POJO1
//		empXX.setEmpno(7015); // 【如果增加 empXX.setEmpno(7015); 則變成update】
//		empXX.setEname("吳永15");
//		empXX.setJob("MANAGER15");
//		empXX.setHiredate(java.sql.Date.valueOf("2001-01-15"));
//		empXX.setSal(new Double(15555));
//		empXX.setComm(new Double(155));
//		empXX.setDeptVO(deptVO);
//
//		EmpVO empYY = new EmpVO(); // 員工POJO2
//		empYY.setEmpno(7016); // 【如果增加 empXX.setEmpno(7016); 則變成update】
//		empYY.setEname("吳永16");
//		empYY.setJob("MANAGER16");
//		empYY.setHiredate(java.sql.Date.valueOf("2001-01-16"));
//		empYY.setSal(new Double(16666));
//		empYY.setComm(new Double(166));
//		empYY.setDeptVO(deptVO);
//
//		set.add(empXX);
//		set.add(empYY);
//
//		deptVO.setDeptno(50); // 【如果增加deptVO.setDeptno(50); 則變成update】
//		deptVO.setDname("製造部1");
//		deptVO.setLoc("中國江西1");
//		deptVO.setEmps(set);
//		deptService.updateDept(deptVO);

		//● 修改-2(不需設定cascade="save-update" 或 cascade="all")(這是經常要用到的一般修改)
//		DeptVO deptVO2 = new DeptVO(); // 部門POJO
//		deptVO2.setDeptno(50); // 【如果增加deptVO.setDeptno(50); 則變成update】
//		deptVO2.setDname("製造部2");
//		deptVO2.setLoc("中國江西2");
//		deptService.updateDept(deptVO2);

		//●刪除 (超級強大!小心使用!)(一方dept2.hbm.xml必須有cascade="delete" 或 cascade="all"的設定, 再加上inverse="true"設定)
//		deptService.deleteDept(50);

		//● 新增-2(不需要cascade="save-update" 或 cascade="all"的設定)(這是經常要用到的一般新增)
//		DeptVO deptVO = new DeptVO(); // 部門POJO
//		deptVO.setDname("製造部s");
//		deptVO.setLoc("中國江西s");
//		deptService.addDept(deptVO);

		//● 查詢-findByPrimaryKey (優秀!) (一方dept2.hbm.xml必須設為lazy="false")
//		DeptVO deptVO3 = deptService.getOneDept(3);
//		System.out.print(deptVO3.getDeptno() + ",");
//		System.out.print(deptVO3.getDname() + ",");
//		System.out.print(deptVO3.getLoc());
//		System.out.println("\n-----------------");
//		Set<EmpVO> set3 = deptVO3.getEmps();
//		for (EmpVO aEmp : set3) {
//			System.out.print(aEmp.getEmpno() + ",");
//			System.out.print(aEmp.getEname() + ",");
//			System.out.print(aEmp.getJob() + ",");
//			System.out.print(aEmp.getHiredate() + ",");
//			System.out.print(aEmp.getSal() + ",");
//			System.out.print(aEmp.getComm() + ",");
//			System.out.print(aEmp.getDeptVO().getDeptno() + ",");
//			System.out.print(aEmp.getDeptVO().getDname() + ",");
//			System.out.print(aEmp.getDeptVO().getLoc());
//			System.out.println();
//		}

		//● 查詢-getAll-1 (一方dept2.hbm.xml不用設為lazy="false",因為沒用到多方的物件)
//		List<DeptVO> list1 = deptService.getAll();
//		for (DeptVO aDept : list1) {
//			System.out.print(aDept.getDeptno() + ",");
//			System.out.print(aDept.getDname() + ",");
//			System.out.print(aDept.getLoc());
//			System.out.println();
//		}

		//● 查詢-getAll-2 (優秀!!!) (一方dept2.hbm.xml必須設為lazy="false")
//		List<DeptVO> list2 = deptService.getAll();
//		for (DeptVO aDept : list2) {
//			System.out.print(aDept.getDeptno() + ",");
//			System.out.print(aDept.getDname() + ",");
//			System.out.print(aDept.getLoc());
//			System.out.println("\n-----------------");
//			Set<EmpVO> set2 = aDept.getEmps();
//			for (EmpVO aEmp : set2) {
//				System.out.print(aEmp.getEmpno() + ",");
//				System.out.print(aEmp.getEname() + ",");
//				System.out.print(aEmp.getJob() + ",");
//				System.out.print(aEmp.getHiredate() + ",");
//				System.out.print(aEmp.getSal() + ",");
//				System.out.print(aEmp.getComm() + ",");
//				System.out.print(aEmp.getDeptVO().getDeptno() + ",");
//				System.out.print(aEmp.getDeptVO().getDname() + ",");
//				System.out.print(aEmp.getDeptVO().getLoc());
//				System.out.println();
//			}
//			System.out.println();
//		}
//		return "Test testDS5-DeptRepository OK";
//	}
//
//}