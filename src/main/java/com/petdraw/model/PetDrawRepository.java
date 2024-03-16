																														package com.petdraw.model;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.buyer.entity.BuyerVO;

public interface PetDrawRepository extends JpaRepository<PetDrawVO, Integer> {
	@Transactional
	@Modifying
	@Query(value = "delete from petDrawInformation where petDrawId =?1", nativeQuery = true)
	void deleteByPetDrawId(int PetDrawId);

	//	 根據 memberMain 和 petDrawTime 查詢
	@Query(value = "SELECT * FROM petdraw WHERE memberId = :memberId AND petdrawtime >= :petDrawTime", nativeQuery = true)
	List<PetDrawVO> findByMemberIdAndAfterPetDrawTime(@Param("memberId") Integer buyerId, @Param("petDrawTime") LocalDateTime afterDate);

	// 根據 memberPair 和 memberPairResTime 查詢
	@Query(value = "SELECT * FROM petdraw WHERE memberpairId = :memberpairId AND memberrestime >= :memberrestime", nativeQuery = true)
	List<PetDrawVO> findByMemberPairIdAndMemberPairResTime(@Param("memberpairId") Integer buyerId,@Param("memberrestime") LocalDateTime afterDate);
	
	@Query(value = "SELECT COUNT(*) FROM PetDrawVO", nativeQuery = true)
	Integer findMemberCount();
	
	@Query(value = "SELECT * FROM PetDrawVO WHERE memberId =: memberId", nativeQuery = true)
	List<BuyerVO> findByMemberId(Integer memberId);
	
	@Query(value = "SELECT * FROM PetDrawVO WHERE memberPairId =: memberPairId", nativeQuery = true)
	List<BuyerVO> findByMemberPairId(Integer memberPairId);

	
	
}
