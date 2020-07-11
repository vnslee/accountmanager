package com.kakaopay.sec.model.response;

import lombok.Builder;
import lombok.Data;

/**
 * 에러 정보 반환 클래스
 */
@Builder
@Data
public class ResponseError {

	/** 에러 코드 */
	private int errCode;
	
	/** 에러 메시지 */
	private String message;

}
