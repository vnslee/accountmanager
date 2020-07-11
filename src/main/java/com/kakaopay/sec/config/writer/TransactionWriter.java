package com.kakaopay.sec.config.writer;

import java.util.List;

import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;

import com.kakaopay.sec.model.entity.Transaction;
import com.kakaopay.sec.repository.TransactionRepository;

public class TransactionWriter implements ItemWriter<Transaction>{

	@Autowired
	private TransactionRepository transactionRespository;
	
	@Override
	public void write(List<? extends Transaction> items) throws Exception {

		this.transactionRespository.saveAll(items);
	}

}
