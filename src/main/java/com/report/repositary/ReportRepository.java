// https://docs.spring.io/spring-data/jpa/docs/current/reference/html/

package com.report.repositary;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.report.entity.ReportVO;

public interface ReportRepository extends JpaRepository<ReportVO, Integer> {

	@Transactional
	@Modifying
	@Query(value = "delete from report where reportId =?1", nativeQuery = true)
	void deleteByReportId(int reportId);

}