package com.kakaopay.sec.config.processor;

import org.springframework.batch.item.ItemProcessor;

import com.kakaopay.sec.model.entity.Transaction;

public class TransactionProcessor implements ItemProcessor<Transaction, Transaction>{

	@Override
	public Transaction process(Transaction item) throws Exception {

		return new Transaction(item.getAcctNo(),
				item.getAmt(),
				item.getDate(),
				item.getFee(),
				item.isCanceled(),
				item.getTransactionNo());
	}


}
