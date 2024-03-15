package com.msgType.entity;

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

import com.msg.entity.MsgVO;

@Entity
@Table(name = "msgType")
public class MsgTypeVO implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	
	private Integer msgTypeId;
	private String msgTypeContent;
	private Set<MsgVO> msgs = new HashSet<MsgVO>();

	public MsgTypeVO() {
	}

	@Id
	@Column(name = "msgTypeId")
	@GeneratedValue(strategy = GenerationType.IDENTITY) //@GeneratedValue的generator屬性指定要用哪個generator //【strategy的GenerationType, 有四種值: AUTO, IDENTITY, SEQUENCE, TABLE】 
	public Integer getMsgTypeId() {
		return this.msgTypeId;
	}

	public void setMsgTypeId(Integer msgTypeId) {
		this.msgTypeId = msgTypeId;
	}

	@Column(name = "msgTypeContent")
	public String getMsgTypeContent() {
		return this.msgTypeContent;
	}

	public void setMsgTypeContent(String msgTypeContent) {
		this.msgTypeContent = msgTypeContent;
	}
	
//	@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.EAGER, mappedBy="MsgTypeVO")
//	@OrderBy("msgId asc")
//	public Set<MsgVO> getMsgs() {
//		return this.msgs;
//	}
//
//	public void setMsgs(Set<MsgVO> msgs) {
//		this.msgs = msgs;
//	}

}
