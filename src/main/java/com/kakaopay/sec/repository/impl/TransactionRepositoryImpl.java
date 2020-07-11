package com.kakaopay.sec.repository.impl;

import java.util.ArrayList;
import java.util.List;
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

	/** Redis 등록 키 접두어 */
	private static final String KEY_PREFIX = "transaction";

	/** 거래 내역 등록 SetOperations */
	private SetOperations<String, Object> listOperations;

	private final RedisTemplate<String, Object> redisTemplate;
	
	public TransactionRepositoryImpl(RedisTemplate<String, Object> redisTemplate) {
		this.redisTemplate = redisTemplate;
		this.listOperations = this.redisTemplate.opsForSet();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void deleteAll() {

		this.redisTemplate.delete(this.getAllKeys());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Transaction> findAll() {
		
		Set<String> keys = this.getAllKeys();
		
		List<Transaction> transactions = new ArrayList<>();
		
		for(String key : keys) {
			transactions.addAll(this.findByTemplateKey(key));
		}
		
		return transactions;
		
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Transaction> findById(String acctNo) {

		String key = KEY_PREFIX + acctNo;
		
		return this.findByTemplateKey(key);
		
	}

	/**
	 * 입력된 키에 포함된 거래 내역 목록을 반환한다.
	 * 
	 * @param key 등록 키
	 * @return 거래 내역 목록
	 */
	private List<Transaction> findByTemplateKey(String key) {
		
		List<Transaction> transactions = new ArrayList<>();
		Optional.ofNullable(this.listOperations.members(key))	
			.ifPresent((Set<Object> trans) -> {
				trans.stream()
					.filter((Object obj) -> obj instanceof Transaction)
					.map(Transaction.class::cast)
					.forEach(transactions::add);
			});
		
		return transactions;
	}

	@VisibleForTesting
	Set<String> getAllKeys() {
		
		return this.redisTemplate.keys(KEY_PREFIX+"[^:]*");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void save(Transaction transaction) {

		String key = KEY_PREFIX + transaction.getAcctNo();
		
		this.listOperations.add(key, transaction);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void saveAll(List<? extends Transaction> transactions) {
	
		for(Transaction transaction : transactions) {
			this.save(transaction);
		}
	}


}
