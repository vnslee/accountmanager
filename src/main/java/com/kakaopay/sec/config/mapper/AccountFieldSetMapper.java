package com.kakaopay.sec.config.mapper;

import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindException;

import com.kakaopay.sec.model.entity.Account;

@Component
public class AccountFieldSetMapper implements FieldSetMapper<Account> {

	public static final String ACCT_NO = "계좌번호";
	
	public static final String BR_CODE = BranchFieldSetterMapper.BR_CODE;
	
	public static final String NAME = "계좌명";
	
	@Override
	public Account mapFieldSet(FieldSet fieldSet) throws BindException {

		return Account.builder()
				.acctNo(fieldSet.readString(ACCT_NO))
				.name(fieldSet.readString(NAME))
				.brCode(fieldSet.readString(BR_CODE))
				.build();
	}


}
