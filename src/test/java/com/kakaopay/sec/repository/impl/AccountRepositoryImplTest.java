package com.kakaopay.sec.repository.impl;

import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.kakaopay.sec.model.entity.Account;
import com.kakaopay.sec.repository.AccountRepository;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class AccountRepositoryImplTest {

	@Autowired
	private AccountRepository accountRepository;
	
	@AfterEach
	public void reset() {
		this.accountRepository.deleteAll();
	}
	
	@Test
	public void testFindAll() {
		
		String acctNo1 = "1111111";
		String name1 ="제임스";
		String brCode1 = "A";
		
		Account account1 = Account.builder()
				.acctNo(acctNo1)
				.name(name1)
				.brCode(brCode1)
				.build();
		
		this.accountRepository.save(account1);
		
		String acctNo2 = "1111112";
		String name2 ="제";
		String brCode2 = "A";
		
		Account account2 = Account.builder()
				.acctNo(acctNo2)
				.name(name2)
				.brCode(brCode2)
				.build();
		
		this.accountRepository.save(account2);
		
		Map<String, Account> actuals = this.accountRepository.findAll();
		
		Assertions.assertEquals(2, actuals.keySet().size(), "2개 포함 확인");
		
		Assertions.assertTrue(actuals.containsKey(account1.getAcctNo()),
				"account1 데이터 포함 확인");
		Assertions.assertTrue(actuals.containsKey(account2.getAcctNo()),
				"account2 데이터 포함 확인");
		
	}
	
	@Test
	public void testFindAllEmpty() {
		
		Map<String, Account> actuals = this.accountRepository.findAll();
		
		Assertions.assertTrue(actuals.isEmpty(), "등록된 계좌 없을 때 확인");
	}
	
	@Test
	public void testFindByID() {
		
		String acctNo = "1111111";
		String name ="제임스";
		String brCode = "A";
		
		Account account = Account.builder()
				.acctNo(acctNo)
				.name(name)
				.brCode(brCode)
				.build();
		
		this.accountRepository.save(account);
		
		Optional<Account> actual = this.accountRepository.findById(acctNo);
		
		Assertions.assertTrue(actual.isPresent(), "저장한 데이터가 존재하는 것을 확인");
		Assertions.assertEquals(acctNo, actual.get().getAcctNo(), "계좌 번호 확인");
		Assertions.assertEquals(name, actual.get().getName(), "계좌 이 확인");
		Assertions.assertEquals(brCode, actual.get().getBrCode(), "계좌 지점 코드 확인");
		
	}
	
	@Test
	public void testFindByIDNotExist() {
		
		String acctNo = "1111111";
		String name ="제임스";
		String brCode = "A";
		
		Account account = Account.builder()
				.acctNo(acctNo)
				.name(name)
				.brCode(brCode)
				.build();
		
		this.accountRepository.save(account);
		
		Optional<Account> actual = this.accountRepository.findById("not");
		
		Assertions.assertFalse(actual.isPresent(), 
				"저장한 데이터가 존재하지 않는 것을 확인");
		
	}
}
