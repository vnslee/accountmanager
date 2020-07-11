package com.kakaopay.sec.config.mapper;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindException;

import com.kakaopay.sec.model.entity.Transaction;

@Component
public class TransactionFieldSetterMapper implements FieldSetMapper<Transaction> {

	public static final String ACCT_NO = AccountFieldSetMapper.ACCT_NO;
	
	public static final String AMOUNT = "금액";
	
	public static final String CANCELD = "취소여부";
	
	public static final String DATE = "거래일자";
	
	public static final String FEE = "수수료";
	
	public static final String TRANSACTION_NO = "거래번호";
	
	@Override
	public Transaction mapFieldSet(FieldSet fieldSet) throws BindException {
		
		String acctNo = fieldSet.readString(ACCT_NO);
		long transactionNo = Long.parseLong(fieldSet.readString(TRANSACTION_NO));
		
		LocalDate date = LocalDate.parse(fieldSet.readString(DATE),
				DateTimeFormatter.BASIC_ISO_DATE);
		
		long amt = Long.parseLong(fieldSet.readString(AMOUNT));
		long fee = Long.parseLong(fieldSet.readString(FEE));
		
		boolean isCanceled = false;
		if(fieldSet.readString(CANCELD).equals("Y")) {
			isCanceled = true;
		}
		
		return new Transaction(acctNo, amt, date, fee, isCanceled, transactionNo);
	}

	

}
