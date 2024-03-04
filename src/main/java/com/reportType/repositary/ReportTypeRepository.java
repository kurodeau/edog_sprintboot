// https://docs.spring.io/spring-data/jpa/docs/current/reference/html/

package com.reportType.repositary;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.reportType.entity.ReportTypeVO;

public interface ReportTypeRepository extends JpaRepository<ReportTypeVO, Integer> {

}