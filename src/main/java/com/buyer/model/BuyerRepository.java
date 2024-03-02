// https://docs.spring.io/spring-data/jpa/docs/current/reference/html/

package com.buyer.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.buyer.entity.BuyerVO;

public interface BuyerRepository extends JpaRepository<BuyerVO, Integer> {

	@Transactional
	@Modifying
	@Query(value = "delete from buyer where memberId =?1", nativeQuery = true)
	void deleteByMemberId(int memberId);

}