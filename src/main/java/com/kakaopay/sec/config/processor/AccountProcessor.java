package com.kakaopay.sec.config.processor;

import org.springframework.batch.item.ItemProcessor;

import com.kakaopay.sec.model.entity.Account;

public class AccountProcessor implements ItemProcessor<Account, Account>{

	@Override
	public Account process(Account item) throws Exception {
        
        return Account.builder()
        		.acctNo(item.getAcctNo())
        		.name(item.getName())
        		.brCode(item.getBrCode())
        		.build();
	}

}
