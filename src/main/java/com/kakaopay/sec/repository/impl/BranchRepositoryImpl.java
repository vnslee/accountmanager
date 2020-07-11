package com.kakaopay.sec.repository.impl;

import java.util.Map;
import java.util.Optional;

import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import com.kakaopay.sec.model.entity.Branch;
import com.kakaopay.sec.repository.BranchRepository;

@Repository
public class BranchRepositoryImpl implements BranchRepository {

	private final RedisTemplate<String, Object> redisTemplate;
	
    private HashOperations<String, String, Branch> hashOperations;

    private static final String KEY = "branch";
    
    public BranchRepositoryImpl(RedisTemplate<String, Object> redisTemplate) {
		this.redisTemplate = redisTemplate;
		this.hashOperations = this.redisTemplate.opsForHash();
	}
	
	@Override
	public void save(Branch branch) {
	
		this.hashOperations.put(KEY, branch.getBrName(), branch);
	}

	@Override
	public Map<String, Branch> findAll() {
		
		return this.hashOperations.entries(KEY);
	}

	@Override
	public Optional<Branch> findById(String brName) {

		return Optional.ofNullable(
				(Branch)this.hashOperations.get(KEY, brName));
	}

	@Override
	public void update(Branch branch) {
		
		this.save(branch);
	}

	@Override
	public void delete(String brName) {
		
		this.hashOperations.delete(KEY, brName);
	}

	@Override
	public void deleteAll() {
		
		for(String brName : this.findAll().keySet()) {
			this.delete(brName);
		}
	}


}
