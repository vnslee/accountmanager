package com.kakaopay.sec.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.kakaopay.sec.model.entity.Branch;

/**
 * 관리점 관리 Repository
 */
@Repository
public interface BranchRepository extends CrudRepository<Branch, String> {
	
}
