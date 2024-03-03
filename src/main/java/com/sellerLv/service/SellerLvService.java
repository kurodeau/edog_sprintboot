package com.sellerLv.service;

import java.util.List;

import org.springframework.lang.NonNull;

import com.sellerLv.entity.SellerLvVO;

public interface SellerLvService {

    void addSellerLv(@NonNull SellerLvVO sellerLvVO);

    void updateSellerLv(@NonNull SellerLvVO sellerLvVO);

    void deleteSellerLv(@NonNull Integer id);

    SellerLvVO getById(@NonNull Integer id);

    List<SellerLvVO> getAll();

    Integer getCount();
}
