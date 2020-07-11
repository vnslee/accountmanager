package com.kakaopay.sec.service.impl;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.kakaopay.sec.model.entity.Transaction;
import com.kakaopay.sec.repository.TransactionRepository;
import com.kakaopay.sec.service.TransactionService;

@Service
public class TransactionServiceImpl implements TransactionService {

	private final TransactionRepository transactionRepository;
	
	
	public TransactionServiceImpl(TransactionRepository transactionRepository) {
		this.transactionRepository = transactionRepository;
	}

	@Override
	public void save(Transaction transaction) {
		
		this.transactionRepository.save(transaction);
	}

	@Override
	public Optional<Set<Transaction>> findByAcctNo(String acctNo) {

		return this.transactionRepository.findById(acctNo);
	}

	@Override
	public Set<Transaction> findAll() {

		return this.transactionRepository.findAll();
	}

	@Override
	public void update(Transaction transaction) {

		this.transactionRepository.save(transaction);
	}

	@Override
	public Set<Integer> getAllTransacionYears() {

		Set<Transaction> transactions = this.findAll();
		Set<Integer> years = new HashSet<>();
		
		for(Transaction transaction : transactions) {

			int year = transaction.getDate().getYear();
			if(years.contains(year) == false) {
				years.add(year);
			}
		}
		
		return years;
	}

}
