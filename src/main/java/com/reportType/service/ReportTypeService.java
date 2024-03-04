package com.reportType.service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.report.entity.ReportVO;
import com.reportType.entity.ReportTypeVO;
import com.reportType.repositary.ReportTypeRepository;

@Service("reportTypeService")
public class ReportTypeService {

	@Autowired
	ReportTypeRepository repository;

	public void addReportType(ReportTypeVO reportTypeVO) {
		repository.save(reportTypeVO);
	}

	public void updateReportType(ReportTypeVO reportTypeVO) {
		repository.save(reportTypeVO);
	}

	public void deleteReportType(Integer reportTypeId) {
		if (repository.existsById(reportTypeId))
			repository.deleteById(reportTypeId);
	}


	public ReportTypeVO getOneReportType(Integer reportTypeId) {
		Optional<ReportTypeVO> optional = repository.findById(reportTypeId);
//		return optional.get();
		return optional.orElse(null);  // public T orElse(T other) : 如果值存在就回傳其值，否則回傳other的值
	}

	public List<ReportTypeVO> getAll() {
		return repository.findAll();
	}

	public Set<ReportVO> getReportsByReportTypeId(Integer reportTypeId) {
		return getOneReportType(reportTypeId).getReports();
	}

}
