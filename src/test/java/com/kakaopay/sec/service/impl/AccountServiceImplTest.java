package com.kakaopay.sec.service.impl;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.kakaopay.sec.model.entity.Account;
import com.kakaopay.sec.repository.AccountRepository;
import com.kakaopay.sec.service.AccountService;
import com.kakaopay.sec.service.TransactionService;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class AccountServiceImplTest {

	@MockBean
	private AccountRepository accountRepository;
	
	@SpyBean
	private AccountService target;
	
	@MockBean
	private TransactionService transactionService;
	
	@Test
	void testGetByBrCodeExistBrCode() {

		String brCode = "B";
		
		String acctNo1 = "1234";
		String name1 = "james";
		String brCode1 = "A";
		Account account = Account.builder()
				.acctNo(acctNo1)
				.name(name1)
				.brCode(brCode1)
				.build();
		
		Map<String, Account> accounts = new ConcurrentHashMap<>();
		accounts.put(brCode, account);
		
		Mockito.doReturn(accounts).when(this.accountRepository).findAll();
		
		List<Account> actuals = this.target.getByBrCode(brCode);
		
		Assertions.assertTrue(actuals.isEmpty(), 
				"등록된 계좌 중 brCode가 동일한 계좌 없을 경우, 빈 값 반환 확인");
	}

	@Test
	void testGetByBrCodeNotExistAccount() {

		String brCode = "B";
		
		Map<String, Account> accounts = new ConcurrentHashMap<>();
		
		Mockito.doReturn(accounts).when(this.accountRepository).findAll();
		
		List<Account> actuals = this.target.getByBrCode(brCode);
		
		Assertions.assertTrue(actuals.isEmpty(), "등록된 계좌가 없을 경우, 빈 값 반환 확인");
	}

	@Test
	void testGetByBrCodeNotExistBrCode() {

		String brCode = "B";
		
		String acctNo1 = "1234";
		String name1 = "james";
		Account account = Account.builder()
				.acctNo(acctNo1)
				.name(name1)
				.brCode(brCode)
				.build();
		
		Map<String, Account> accounts = new ConcurrentHashMap<>();
		accounts.put(brCode, account);
		
		Mockito.doReturn(accounts).when(this.accountRepository).findAll();
		
		List<Account> actuals = this.target.getByBrCode(brCode);
		
		Assertions.assertTrue(actuals.contains(account), "코드가 동일한 계좌 포함 확인");
	}
	
}
