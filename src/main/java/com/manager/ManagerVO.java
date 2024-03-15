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
	
	
	
	
	private Integer managerUserId;
	
	private String managerEmail;
	
	private String managerPassword;
	
	private Integer managerPer;
	
	private Timestamp createtime;

	private Boolean isConfirm;
	
	
	@Column(name = "isConfrim")
	public Boolean getIsConfirm() {
		return isConfirm;
	}

	public void setIsConfirm(Boolean isConfirm) {
		this.isConfirm = isConfirm;
	}

	@Column(name = "managerEmail")
	public String getManagerEmail() {
		return managerEmail;
	}

	public void setManagerEmail(String managerEmail) {
		this.managerEmail = managerEmail;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="managerUserId" , updatable = false)
	public Integer getManagerUserId() {
		return managerUserId;
	}

	public void setManagerUserId(Integer managerUserId) {
		this.managerUserId = managerUserId;
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

	@Column(name ="createtime")
	public Timestamp getCreatetime() {
		return createtime;
	}

	public void setCreatetime(Timestamp createtime) {
		this.createtime = createtime;
	}
	
	

}
