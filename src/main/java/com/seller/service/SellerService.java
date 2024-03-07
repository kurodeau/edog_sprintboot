package com.seller.service;

import java.util.List;

import org.springframework.lang.NonNull;

import com.seller.entity.SellerVO;

public interface SellerService {

    void addSeller(@NonNull SellerVO sellerVO);

    void updateSeller(@NonNull SellerVO sellerVO);

    void deleteSeller(@NonNull Integer id);

    SellerVO getById(Integer id);

    List<SellerVO> getAll();

    Integer getCount();

    SellerVO findUserEmail(String email);

    SellerVO findByOnlyOneEmail(String email);
    
    
     void saveUserDetails(SellerVO sellerVO);
}
