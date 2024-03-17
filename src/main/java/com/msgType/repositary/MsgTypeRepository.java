// https://docs.spring.io/spring-data/jpa/docs/current/reference/html/

package com.msgType.repositary;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.msgType.entity.MsgTypeVO;

public interface MsgTypeRepository extends JpaRepository<MsgTypeVO, Integer> {

}