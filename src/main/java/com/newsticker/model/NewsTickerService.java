package com.newsticker.model;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;


@Service("newsTickerService")
public class NewsTickerService {

	@Autowired
	NewsTickerRepository repository;

	public void addNewsTicker(@NonNull NewsTickerVO newsTickerVO) {
		repository.save(newsTickerVO);
	}

	public void updateNewsTicker(@NonNull NewsTickerVO newsTickerVO) {
		repository.save(newsTickerVO);
	}

	// 基本上不要用到delete
	public void deleteNewsTicker(@NonNull Integer newsTickerId) {
		if (repository.existsById(newsTickerId))
			repository.deleteByNewsTickerId(newsTickerId); 
//		    repository.deleteById(empno); // 沒有關聯表格應該不需要這個
	}

	public NewsTickerVO getOneNewsTicker(Integer newsTickerId) {
		Optional<NewsTickerVO> optional = repository.findById(newsTickerId);
//		return optional.get();
		return optional.orElse(null);  // public T orElse(T other) : 如果值存在就回傳其值，否則回傳other的值
	}

	public List<NewsTickerVO> getAll() {
		return repository.findAll();
	}
	
	public Integer getCount() {
		return (int) repository.count();
	}
	

}
