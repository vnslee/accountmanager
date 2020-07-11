package com.kakaopay.sec.repository.impl;


import java.util.Map;
import java.util.Optional;

import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import com.kakaopay.sec.model.entity.Account;
import com.kakaopay.sec.repository.AccountRepository;

@Repository
public class AccountRepositoryImpl implements AccountRepository {

	private static final String KEY = "account";
	
    private HashOperations<String, String, Account> hashOperations;

    private final RedisTemplate<String, Object> redisTemplate;
    
    public AccountRepositoryImpl(RedisTemplate<String, Object> redisTemplate) {
		this.redisTemplate = redisTemplate;
		this.hashOperations = this.redisTemplate.opsForHash();
	}
	
	@Override
	public void delete(String acctNo) {

		this.hashOperations.delete(KEY, acctNo);
	}

	@Override
	public void deleteAll() {

		for(String acctNo : this.findAll().keySet()) {
			this.delete(acctNo);
		}
	}

	@Override
	public Map<String, Account> findAll() {
		
		return hashOperations.entries(KEY);
	}

	@Override
	public Optional<Account> findById(String acctNo) {
		
		return Optional.ofNullable(
				this.hashOperations.get(KEY, acctNo));
	}

	@Override
	public void save(Account account) {
		
		this.hashOperations.put(KEY, account.getAcctNo(), account);
	}

	@Override
	public void update(Account account) {
		
		this.save(account);
	}
	
}
