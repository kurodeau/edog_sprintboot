package com.report.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.report.entity.ReportVO;
import com.report.repositary.ReportRepository;


@Service("reportService")
public class ReportService {

	@Autowired
	ReportRepository repository;
	
	public void addReport(ReportVO reportVO) {
		repository.save(reportVO);
	}

	public void updateReport(ReportVO reportVO) {
		repository.save(reportVO);
	}

	public void deleteReport(Integer reportId) {
		if (repository.existsById(reportId))
			repository.deleteByReportId(reportId);
	}

	public ReportVO getOneReport(Integer reportId) {
		Optional<ReportVO> optional = repository.findById(reportId);
		return optional.orElse(null);
	}

	public List<ReportVO> getAll() {
		return repository.findAll();
	}
	
	public Integer getCount() {
		return (int) repository.count();
	}

}