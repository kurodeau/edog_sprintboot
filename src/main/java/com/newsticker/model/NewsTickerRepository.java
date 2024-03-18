// https://docs.spring.io/spring-data/jpa/docs/current/reference/html/

package com.newsticker.model;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface NewsTickerRepository extends JpaRepository<NewsTickerVO, Integer> {

	@Transactional
	@Modifying
	@Query(value = "delete from newsTicker where newsTickerId =?1", nativeQuery = true)
	void deleteByNewsTickerId(int newsTickerId);

	@Query("SELECT n FROM NewsTickerVO n WHERE n.isDisplay = true")
	List<NewsTickerVO> findAllByIsDisplayTrue();
}