package com.kakaopay.sec.config.writer;

import java.util.List;

import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;

import com.kakaopay.sec.model.entity.Account;
import com.kakaopay.sec.repository.AccountRepository;

public class AccountWriter implements ItemWriter<Account> {

	@Autowired
	private AccountRepository accountRepository;
	
	@Override
	public void write(List<? extends Account> items) throws Exception {

		this.accountRepository.saveAll(items);
	}

}
