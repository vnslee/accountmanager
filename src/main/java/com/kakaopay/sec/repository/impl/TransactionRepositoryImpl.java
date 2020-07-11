package com.kakaopay.sec.repository.impl;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.stereotype.Repository;

import com.google.common.annotations.VisibleForTesting;
import com.kakaopay.sec.model.entity.Transaction;
import com.kakaopay.sec.repository.TransactionRepository;

@Repository
public class TransactionRepositoryImpl implements TransactionRepository {

	private final RedisTemplate<String, Transaction> transactionRedisTemplate;

	private SetOperations<String, Transaction> listOperations;

	public TransactionRepositoryImpl(RedisTemplate<String, Transaction> transactionRedisTemplate) {
		this.transactionRedisTemplate = transactionRedisTemplate;
		this.listOperations = this.transactionRedisTemplate.opsForSet();
	}

	@Override
	public Set<Transaction> findAll() {
		
		Set<String> keys = this.getAllKeys();
		Set<Transaction> allTransactions= new HashSet<>();
		
		for(String key : keys) {
			this.findById(key)
				.ifPresent(allTransactions::addAll);
		}
		
		return allTransactions;
	}

	@VisibleForTesting
	Set<String> getAllKeys() {
		return this.transactionRedisTemplate.keys("*");
	}

	@Override
	public void save(Transaction transaction) {

		this.listOperations.add(transaction.getAcctNo(), transaction);
	}

	@Override
	public Optional<Set<Transaction>> findById(String acctNo) {

		return Optional.ofNullable(this.listOperations.members(acctNo));
	}

	@Override
	public void deleteAll() {

		this.transactionRedisTemplate.delete(this.getAllKeys());
	}


}
