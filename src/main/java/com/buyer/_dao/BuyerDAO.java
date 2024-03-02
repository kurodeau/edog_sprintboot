package com.buyer._dao;

import java.util.List;
import java.util.Map;

import com.buyer.entity.BuyerVO;

public interface BuyerDAO {
	public Integer insert(BuyerVO buyer);

	public Integer update(BuyerVO buyer);

	public Integer delete(Integer memberId);

	public BuyerVO findByPrimaryKey(Integer memberId);

	public List<BuyerVO> getAll();

	public Integer getTotal();

	// 萬用複合查詢(傳入參數型態Map)(回傳 List)
	public List<BuyerVO> getByCompositeQuery(Map<String, String> map);

}
