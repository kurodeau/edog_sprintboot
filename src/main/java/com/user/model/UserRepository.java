package com.user.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;


public interface UserRepository extends JpaRepository<UserVO, Integer>{
	@Transactional
	@Modifying
	@Query(value = "delete from userInformation where userid =?1", nativeQuery = true)
	void deleteByUserId(int UserId);
}
