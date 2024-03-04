package com.reportType.entity;

import java.util.*;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.OrderBy;

import com.report.entity.ReportVO;

@Entity
@Table(name = "reportType")
public class ReportTypeVO implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	
	private Integer reportTypeId;
	private String reportTypeSort;
	private Set<ReportVO> reports = new HashSet<ReportVO>();

	public ReportTypeVO() {
	}

	@Id
	@Column(name = "reportTypeId")
	@GeneratedValue(strategy = GenerationType.IDENTITY) //@GeneratedValue的generator屬性指定要用哪個generator //【strategy的GenerationType, 有四種值: AUTO, IDENTITY, SEQUENCE, TABLE】 
	public Integer getReportTypeId() {
		return this.reportTypeId;
	}

	public void setReportTypeId(Integer reportTypeId) {
		this.reportTypeId = reportTypeId;
	}

	@Column(name = "reportTypeSort")
	public String getReportTypeSort() {
		return this.reportTypeSort;
	}

	public void setReportTypeSort(String reportTypeSort) {
		this.reportTypeSort = reportTypeSort;
	}
	
	@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.EAGER, mappedBy="reportTypeVO")
	@OrderBy("reportId asc")
	public Set<ReportVO> getReports() {
		return this.reports;
	}

	public void setReports(Set<ReportVO> reports) {
		this.reports = reports;
	}

}
