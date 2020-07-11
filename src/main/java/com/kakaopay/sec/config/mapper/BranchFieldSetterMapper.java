package com.kakaopay.sec.config.mapper;

import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindException;

import com.kakaopay.sec.model.entity.Branch;

@Component
public class BranchFieldSetterMapper implements FieldSetMapper<Branch>{

	public static final String BR_CODE = "관리점코드";
	
	public static final String BR_NAME = "관리점명";

	@Override
	public Branch mapFieldSet(FieldSet fieldSet) throws BindException {
		
		return Branch.builder()
				.brName(fieldSet.readString(BR_NAME))
				.brCode(fieldSet.readString(BR_CODE))
				.build();
	}

}
