package com.kakaopay.sec.model.response;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ResponseError {

	private int errCode;
	
	private String message;

}
