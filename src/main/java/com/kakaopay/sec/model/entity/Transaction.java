package com.kakaopay.sec.model.entity;

import java.io.Serializable;
import java.time.LocalDate;

import org.springframework.data.annotation.Id;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 거래 내역 DAO
 */
//@Builder
@Data
@NoArgsConstructor
public class Transaction implements Serializable{

	/** 직렬화를 위한 serial ID */
	private static final long serialVersionUID = 6788265733109924569L;

	/** 계좌 번호 */
	@Id
	private String acctNo;
	
	/** 거래 금액 */
	private long amt;
	
	/** 거래일자 */
	@JsonDeserialize(using=LocalDateDeserializer.class)
	@JsonSerialize(using=LocalDateSerializer.class)
	private LocalDate date;
	
	/** 수수료 */
	private long fee;
	
	/** 취소 여부 */
	private boolean isCanceled;
	
	/** 거래 번호 */
	private long transactionNo;

	/**
	 * 생성자
	 */
	@JsonCreator
	public Transaction(@JsonProperty("acctNo") String acctNo,
			@JsonProperty("amt") long amt,
			@JsonProperty("date") LocalDate date,
			@JsonProperty("fee") long fee,
			@JsonProperty("canceled") boolean isCanceled,
			@JsonProperty("transactionNo") long transactionNo) {
		super();
		this.acctNo = acctNo;
		this.amt = amt;
		this.date = date;
		this.fee = fee;
		this.isCanceled = isCanceled;
		this.transactionNo = transactionNo;
	}
	
}
