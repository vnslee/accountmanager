package com.kakaopay.sec.model.entity;

import java.io.Serializable;
import java.time.LocalDate;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 거래 내역 DAO
 */
//@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Transaction implements Serializable{

	/** 직렬화를 위한 serial ID */
	private static final long serialVersionUID = 6788265733109924569L;

	private String acctNo;
	
	private long amt;
	
	@JsonDeserialize(using=LocalDateDeserializer.class)
	@JsonSerialize(using=LocalDateSerializer.class)
	private LocalDate date;
	
	private long fee;
	
	private boolean isCanceled;
	
	private long transactionNo;
	
}
