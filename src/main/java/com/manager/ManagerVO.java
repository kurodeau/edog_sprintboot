package com.manager;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;



@Entity
@Table(name="manager")
public class ManagerVO implements Serializable{
	
	
	
	
	private Integer managerId;
	
	private String managerEmail;
	
	private String managerPassword;
	
	private Integer managerPer;
	
	private Timestamp managerCreatetime;

	


	@Column(name = "managerEmail")
	public String getManagerEmail() {
		return managerEmail;
	}

	public void setManagerEmail(String managerEmail) {
		this.managerEmail = managerEmail;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="managerId" , updatable = false)
	public Integer getManagerId() {
		return managerId;
	}

	public void setManagerId(Integer managerId) {
		this.managerId = managerId;
	}
	


	@Column(name="managerPassword")
	public String getManagerPassword() {
		return managerPassword;
	}

	public void setManagerPassword(String managerPassword) {
		this.managerPassword = managerPassword;
	}
	
	@Column(name = "managerPer")
	public Integer getManagerPer() {
		return managerPer;
	}

	public void setManagerPer(Integer managerPer) {
		this.managerPer = managerPer;
	}

	@Column(name ="managerCreatetime")
	public Timestamp getManagerCreatetime() {
		return managerCreatetime;
	}

	public void setManagerCreatetime(Timestamp managerCreatetime) {
		this.managerCreatetime = managerCreatetime;
	}
	
	

}
