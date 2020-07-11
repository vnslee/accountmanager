package com.kakaopay.sec.repository;

import java.util.Map;
import java.util.Optional;

import com.kakaopay.sec.model.entity.Account;

public interface AccountRepository {
	
    void save(Account account);
    
    Map<String, Account> findAll();
    
    Optional<Account> findById(String acctNo);
    
    void update(Account account);
    
    void delete(String acctNo);
    
    void deleteAll();
}
