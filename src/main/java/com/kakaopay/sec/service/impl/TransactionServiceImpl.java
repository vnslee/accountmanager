package com.kakaopay.sec.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
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
	public List<Transaction> findAll() {

		return this.transactionRepository.findAll();
	}

	@Override
	public List<Transaction> findByAcctNo(String acctNo) {

		return this.transactionRepository.findById(acctNo);
	}

	@Override
	public Set<Integer> getAllTransacionYears() {

		List<Transaction> transactions = this.findAll();
		List<Integer> years = new ArrayList<>();
		
		for(Transaction transaction : transactions) {

			int year = transaction.getDate().getYear();
			if(years.contains(year) == false) {
				years.add(year);
			}
		}
		
		// 내림차순 정렬
		Collections.sort(years);
		
		return new HashSet<>(years);
	}

}
