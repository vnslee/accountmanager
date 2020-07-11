package com.kakaopay.sec.model.entity;

import java.io.Serializable;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import lombok.Builder;
import lombok.Getter;

/**
 * 관리점 정보 DAO 클래스
 */
@Builder
@Getter
@RedisHash("branch")
public class Branch implements Serializable {

	/** 직렬화를 위한 ID */
	private static final long serialVersionUID = -2086154109133723418L;

	/** 관리점 코드 */
	private String brCode;
	
	/** 관리점 명 */
	@Id
	private String brName;

}
