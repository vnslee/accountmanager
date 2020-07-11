package com.kakaopay.sec.service.impl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.kakaopay.sec.model.entity.Account;
import com.kakaopay.sec.model.entity.Branch;
import com.kakaopay.sec.model.entity.Transaction;
import com.kakaopay.sec.model.response.BranchVo;
import com.kakaopay.sec.repository.BranchRepository;
import com.kakaopay.sec.service.AccountService;
import com.kakaopay.sec.service.TransactionService;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class BranchServiceImplTest {

	@MockBean
	private AccountService accountService;
	
	@MockBean
	private BranchRepository branchRepository;
	
	@SpyBean
	private BranchServiceImpl target;
	
	@MockBean
	private TransactionService transactionService;
	
	@Test
	public void testGetAmountExistEqualsYear() {
		String brName = "종로점";
		String brCode = "C";
		
		Branch branch = Branch.builder()
				.brName(brName)
				.brCode(brCode)
				.build();
		
		Account account1 = Account.builder()
				.acctNo("1234")
				.name("james")
				.brCode(brCode)
				.build();
		
		Account account2 = Account.builder()
				.acctNo("1111")
				.name("valteri")
				.brCode(brCode)
				.build();
		
		List<Account> accounts = new ArrayList<>();
		accounts.add(account1);
		accounts.add(account2);
		
		Mockito.doReturn(accounts)
			.when(this.accountService)
			.getByBrCode(Mockito.eq(brCode));
		
		List<Transaction> transactions = new ArrayList<>();
		Transaction transaction1 = new Transaction("1111", 1000, LocalDate.of(2019, 2, 2), 0, false, 2);
		Transaction transaction2 = new Transaction("1111", 500, LocalDate.of(2018, 2, 2), 10, false, 2);
		transactions.add(transaction1);
		transactions.add(transaction2);
		
		Mockito.doReturn(transactions)
			.when(this.transactionService)
			.findByAcctNo(Mockito.eq(account2.getAcctNo()));
		
		int year = 2018;
		long actual = this.target.getSumAmount(year, branch);
		
		Assertions.assertEquals(500, actual, 
				"입력된 년도와 같은 거래 내역의 거래 금액 합계 반환 확인");
	}
	

	@Test
	public void testGetAmountNotExistEqualsYear() {
		String brName = "종로점";
		String brCode = "C";
		
		Branch branch = Branch.builder()
				.brName(brName)
				.brCode(brCode)
				.build();
		
		Account account1 = Account.builder()
				.acctNo("1234")
				.name("james")
				.brCode(brCode)
				.build();
		
		Account account2 = Account.builder()
				.acctNo("1111")
				.name("valteri")
				.brCode(brCode)
				.build();
		
		List<Account> accounts = new ArrayList<>();
		accounts.add(account1);
		accounts.add(account2);
		
		Mockito.doReturn(accounts)
			.when(this.accountService)
			.getByBrCode(Mockito.eq(brCode));
		
		List<Transaction> transactions = new ArrayList<>();
		Transaction transaction = new Transaction("1111", 1000, LocalDate.of(2019, 2, 2), 0, false, 2);
		transactions.add(transaction);
		
		Mockito.doReturn(transactions)
			.when(this.transactionService)
			.findByAcctNo(Mockito.anyString());
		
		int year = 2018;
		long actual = this.target.getSumAmount(year, branch);
		
		Assertions.assertEquals(0, actual, 
				"입력된 관리점에 포함된 계좌 정보에 포함된 거래내역에 해당 년도가 없는 경우 반환 확인");
	}
	
	@Test
	public void testGetByName() {
		String brName = "종로점";
		String brCode = "C";
		
		Branch branch = Branch.builder()
				.brName(brName)
				.brCode(brCode)
				.build();
		
		Mockito.doReturn(Optional.ofNullable(branch))
			.when(this.branchRepository)
			.findById(Mockito.eq(brName));
		
		long expected = 200000;
		Mockito.doReturn(expected)
			.when(this.target)
			.getSumAmount(Mockito.eq(branch));
		
		Optional<BranchVo> actual = this.target.getByName(brName);
		
		Assertions.assertAll("관리점의 거래 금액 합계를 포함하는 정보 반환 확인",
			() -> Assertions.assertTrue(actual.isPresent(), "입력된 관리점 명이 있는 경우 확인"),
			() -> Assertions.assertEquals(brName, actual.get().getBrName()),
			() -> Assertions.assertEquals(brCode, actual.get().getBrCode()),
			() -> Assertions.assertEquals(expected, actual.get().getSumAmt())
				);
		
	}
	
	@Test
	public void testGetByNameNotExistName() {

		String brName = "종로점";
		
		Branch branch = null;
		Mockito.doReturn(Optional.ofNullable(branch))
			.when(this.branchRepository)
			.findById(Mockito.eq(brName));
		
		Optional<BranchVo> actual = this.target.getByName(brName);
		
		Assertions.assertFalse(actual.isPresent(), "입력된 관리점 명이 없는 경우 확인");
	}

	@Test
	public void testGetByYear() {
		
		List<Branch> branches = new ArrayList<>();
		Branch branch1 = Branch.builder()
				.brCode("A")
				.brName("강남점")
				.build();
		branches.add(branch1);
		
		Branch branch2 = Branch.builder()
				.brCode("B")
				.brName("판교점")
				.build();
		branches.add(branch2);
		
		Mockito.doReturn(branches)
			.when(this.branchRepository)
			.findAll();
		
		int year = 2019;
		Mockito.doReturn(10L)
			.when(this.target)
			.getSumAmount(Mockito.eq(year), Mockito.eq(branch1));
		Mockito.doReturn(20L)
			.when(this.target)
			.getSumAmount(Mockito.eq(year), Mockito.eq(branch2));
		
		List<BranchVo> actuals = this.target.getByYear(year);
		
		Assertions.assertAll("관리점의 거래 금액 합계로 오름차순 정렬하여 반환 확인",
				() -> Assertions.assertEquals(2, actuals.size()),
				() -> Assertions.assertEquals(branch2.getBrName(), actuals.get(0).getBrName()),
				() -> Assertions.assertEquals(branch1.getBrName(), actuals.get(1).getBrName())
		);
		
	}

	@Test
	public void testGetByYearEmptyBranches() {
		
		List<Branch> branches = new ArrayList<>();
		
		Mockito.doReturn(branches)
			.when(this.branchRepository)
			.findAll();
		
		int year = 2019;
		
		List<BranchVo> actuals = this.target.getByYear(year);
		
		Assertions.assertTrue(actuals.isEmpty(), "관리점정보가 없는 경우 빈 값 반환 확인");
		
	}
	
	@Test
	public void testGetSumAmount() {
		
		String brName = "종로점";
		String brCode = "C";
		
		Branch branch = Branch.builder()
				.brName(brName)
				.brCode(brCode)
				.build();
		
		Account account1 = Account.builder()
				.acctNo("1234")
				.name("james")
				.brCode(brCode)
				.build();
		
		List<Account> accounts = new ArrayList<>();
		accounts.add(account1);
		
		Mockito.doReturn(accounts)
			.when(this.accountService)
			.getByBrCode(Mockito.eq(brCode));
		
		List<Transaction> transactions = new ArrayList<>();
		Transaction transaction1 = new Transaction("1234", 1000, LocalDate.now(), 0, false, 1);
		Transaction transaction2 = new Transaction("1234", 20000, LocalDate.now(), 0, true, 2);
		transactions.add(transaction1);
		transactions.add(transaction2);
		
		Mockito.doReturn(transactions)
			.when(this.transactionService)
			.findByAcctNo(Mockito.anyString());
		
		long actual = this.target.getSumAmount(branch);
		
		Assertions.assertEquals(transaction1.getAmt(), actual,
				"관리점에 소속된 계좌에 거래 내역 중 취소되지 않은 거래 내역의 거래 금액 합계 반환 확인");
	}
	
	@Test
	public void testGetSumAmountNotExistAccount() {
		
		String brName = "종로점";
		String brCode = "C";
		
		Branch branch = Branch.builder()
				.brName(brName)
				.brCode(brCode)
				.build();
		
		List<Account> accounts = new ArrayList<>();
		
		Mockito.doReturn(accounts)
			.when(this.accountService)
			.getByBrCode(Mockito.eq(brCode));
		
		long actual = this.target.getSumAmount(branch);
		
		Assertions.assertEquals(0, actual, "관리점에 소속된 계좌가 없는 경우 반환 확인");
	}
	
	@Test
	public void testGetSumAmountNotExistTransaction() {
		String brName = "종로점";
		String brCode = "C";
		
		Branch branch = Branch.builder()
				.brName(brName)
				.brCode(brCode)
				.build();
		
		Account account1 = Account.builder()
				.acctNo("1234")
				.name("james")
				.brCode(brCode)
				.build();
		
		Account account2 = Account.builder()
				.acctNo("1111")
				.name("valteri")
				.brCode(brCode)
				.build();
		
		List<Account> accounts = new ArrayList<>();
		accounts.add(account1);
		accounts.add(account2);
		
		Mockito.doReturn(accounts)
			.when(this.accountService)
			.getByBrCode(Mockito.eq(brCode));
		
		List<Transaction> transactions = new ArrayList<>();
		Mockito.doReturn(transactions)
			.when(this.transactionService)
			.findByAcctNo(Mockito.anyString());
		
		int year = 2018;
		long actual = this.target.getSumAmount(year, branch);
		
		Assertions.assertEquals(0, actual, 
				"입력된 관리점에 포함된 계좌 정보에 포함된 거래내역이 없는 경우 반환 확인");
	}
	
	@Test
	public void testGetSumAmountNotExistTransactions() {
		
		String brName = "종로점";
		String brCode = "C";
		
		Branch branch = Branch.builder()
				.brName(brName)
				.brCode(brCode)
				.build();
		
		Account account1 = Account.builder()
				.acctNo("1234")
				.name("james")
				.brCode(brCode)
				.build();
		
		Account account2 = Account.builder()
				.acctNo("1111")
				.name("valteri")
				.brCode(brCode)
				.build();
		
		List<Account> accounts = new ArrayList<>();
		accounts.add(account1);
		accounts.add(account2);
		
		Mockito.doReturn(accounts)
			.when(this.accountService)
			.getByBrCode(Mockito.eq(brCode));
		
		List<Transaction> transactions = new ArrayList<>();
		Mockito.doReturn(transactions)
			.when(this.transactionService)
			.findByAcctNo(Mockito.anyString());
		
		long actual = this.target.getSumAmount(branch);
		
		Assertions.assertEquals(0, actual, "관리점에 소속된 계좌에 거래 내역이 없는 경우 반환 확인");
	}
	
	@Test
	public void testGetSumAmtEmptyAccounts() {
		
		String brName = "종로점";
		String brCode = "C";
		
		Branch branch = Branch.builder()
				.brName(brName)
				.brCode(brCode)
				.build();
		
		List<Account> accounts = new ArrayList<>();
		
		Mockito.doReturn(accounts)
			.when(this.accountService)
			.getByBrCode(Mockito.eq(brCode));
		
		List<Transaction> transactions = new ArrayList<>();
		Mockito.doReturn(transactions)
			.when(this.transactionService)
			.findByAcctNo(Mockito.anyString());
		
		int year = 2018;
		long actual = this.target.getSumAmount(year, branch);
		
		Assertions.assertEquals(0, actual, "관리점에 포함된 계좌 정보가 없는 경우 반환 확인");
	}
}
