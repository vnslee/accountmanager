package com.kakaopay.sec.model.entity;

import java.io.Serializable;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

/**
 * 계좌 정보 DAO 클래스
 */
@Builder
@Getter
@ToString
public class Account implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 593596169133127488L;

	private String acctNo;
	
	private String name;
	
	private String brCode;
}
