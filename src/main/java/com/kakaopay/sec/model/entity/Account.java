package com.kakaopay.sec.model.entity;

import java.io.Serializable;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

/**
 * 계좌 정보 DAO 클래스
 */
@Builder
@Getter
@ToString
@RedisHash("account")
public class Account implements Serializable {

	/** 렬화를 위한 ID  */
	private static final long serialVersionUID = 593596169133127488L;

	/** 계좌 번호 */
	@Id
	private String acctNo;
	
	/** 관리점 코드 */
	private String brCode;
	
	/** 계좌명 */
	private String name;
	
}
