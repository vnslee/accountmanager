package com.kakaopay.sec.config.processor;

import org.springframework.batch.item.ItemProcessor;

import com.kakaopay.sec.model.entity.Branch;

public class BranchProcessor implements ItemProcessor<Branch, Branch>{

	@Override
	public Branch process(Branch item) throws Exception {

		return Branch.builder()
				.brName(item.getBrName())
				.brCode(item.getBrCode())
				.build();
	}


}
