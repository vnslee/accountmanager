package com.kakaopay.sec.service;

import java.util.Optional;
import java.util.Set;

import com.kakaopay.sec.model.entity.Transaction;

public interface TransactionService {

    void save(Transaction transaction);
    
    Optional<Set<Transaction>> findByAcctNo(String acctNo);
    
    Set<Transaction> findAll();
    
    void update(Transaction transaction);
	
    Set<Integer> getAllTransacionYears();
    
}
