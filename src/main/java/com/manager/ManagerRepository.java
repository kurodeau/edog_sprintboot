// https://docs.spring.io/spring-data/jpa/docs/current/reference/html/

package com.manager;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.buyer.entity.BuyerVO;

public interface ManagerRepository extends JpaRepository<ManagerVO, Integer> {
	
	@Transactional
	@Modifying
	@Query(value = "DELETE FROM manager WHERE managerId = ?1  LIMIT 1", nativeQuery = true)
	void deleteById(Integer managerId);
	
	
	@Query(value = "SELECT * FROM manager WHERE managerEmail = ?1 LIMIT 1", nativeQuery = true)
	ManagerVO findByOnlyOneEmail(String memberEmail);


}