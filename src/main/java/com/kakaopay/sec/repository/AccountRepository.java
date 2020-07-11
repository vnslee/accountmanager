package com.kakaopay.sec.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.kakaopay.sec.model.entity.Account;

/**
 * 계좌 정보 관리 Repository
 */
@Repository
public interface AccountRepository extends CrudRepository<Account, String> {
	
}
