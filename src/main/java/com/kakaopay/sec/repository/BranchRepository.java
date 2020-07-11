package com.kakaopay.sec.repository;

import java.util.Map;
import java.util.Optional;

import com.kakaopay.sec.model.entity.Branch;

public interface BranchRepository {
	
    void save(Branch branch);
    
    Map<String, Branch> findAll();
    
    Optional<Branch> findById(String brName);
    
    void update(Branch branch);
    
    void delete(String brName);
    
    void deleteAll();
}
