package com.kakaopay.sec.repository;

import java.util.Optional;
import java.util.Set;

import com.kakaopay.sec.model.entity.Transaction;

public interface TransactionRepository {

    void save(Transaction transaction);
    
    Optional<Set<Transaction>> findById(String acctNo);
    
    Set<Transaction> findAll();
    
    void deleteAll();
}
