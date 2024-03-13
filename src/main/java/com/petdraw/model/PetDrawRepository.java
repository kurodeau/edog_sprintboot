package com.petdraw.model;

import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.buyer.entity.BuyerVO;

public interface PetDrawRepository extends JpaRepository<PetDrawVO, Integer> {
	@Transactional
	@Modifying
	@Query(value = "delete from petDrawInformation where petDrawId =?1", nativeQuery = true)
	void deleteByPetDrawId(int PetDrawId);

////	 根據 memberMain 和 petDrawTime 查詢
//	List<PetDrawVO> findByMemberIdAndPetDrawTime(BuyerVO memberId, LocalDate petDrawTime);
//
//	// 根據 memberPair 和 memberPairResTime 查詢
//	List<PetDrawVO> findByMemberPairAndMemberPairIdResTime(BuyerVO memberPairId, LocalDate memberPairResTime);
//	
//	@Query(value = "SELECT COUNT(*) FROM PetDrawVO", nativeQuery = true)
//	Integer findMemberCount();
//	
//	@Query(value = "SELECT * FROM PetDrawVO WHERE memberId =: memberId", nativeQuery = true)
//	List<PetDrawVO> findByMemberId(Integer memberId);
}
