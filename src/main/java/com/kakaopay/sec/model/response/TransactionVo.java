package com.kakaopay.sec.model.response;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@AllArgsConstructor
@Getter
public class TransactionVo {

	private LocalDate date;
	
	private String acctNo;
	
	private Long transactionNo;
	
	private Long amt;
	
	private Long fee;
	
	private boolean isSuccess;
	
}
