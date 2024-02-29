package com.sellerLv.__dao;



import java.util.List;

import com.sellerLv.entity.SellerLvVO;

public interface SellerLvDAO {
          public Integer insert(SellerLvVO sellerLv);
          public Integer update(SellerLvVO sellerLv);
          public Integer delete(Integer sellerLvId);
          public SellerLvVO findByPrimaryKey(Integer sellerLvId);
          public List<SellerLvVO> getAll();
          public Integer getTotal();
          
          //萬用複合查詢(傳入參數型態Map)(回傳 List)
//        public List<EmpVO> getAll(Map<String, String[]> map); 

}