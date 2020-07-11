package com.kakaopay.sec.service.impl;

import java.time.LocalDate;
import java.util.ArrayList;
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
import com.kakaopay.sec.model.entity.Transaction;
import com.kakaopay.sec.model.response.AccountVo;
import com.kakaopay.sec.repository.AccountRepository;
import com.kakaopay.sec.service.TransactionService;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class AccountServiceImplTest {

	@MockBean
	private AccountRepository accountRepository;
	
	@SpyBean
	private AccountServiceImpl target;
	
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
		
		List<Account> accounts = new ArrayList<>();
		accounts.add(account);
		
		Iterable<Account> accountIter = accounts;
		Mockito.doReturn(accountIter)
			.when(this.accountRepository).findAll();
		
		List<Account> actuals = this.target.getByBrCode(brCode);
		
		Assertions.assertTrue(actuals.isEmpty(), 
				"등록된 계좌 중 brCode가 동일한 계좌 없을 경우, 빈 값 반환 확인");
	}

	@Test
	void testGetByBrCodeNotExistAccount() {

		String brCode = "B";
		
		Iterable<Account> accounts = new ArrayList<>();

		Mockito.doReturn(accounts)
			.when(this.accountRepository).findAll();
		
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
		
		List<Account> accounts = new ArrayList<>();
		accounts.add(account);
		
		Iterable<Account> accountIter = accounts;
		Mockito.doReturn(accountIter)
			.when(this.accountRepository).findAll();
		
		List<Account> actuals = this.target.getByBrCode(brCode);
		
		Assertions.assertTrue(actuals.contains(account), "코드가 동일한 계좌 포함 확인");
	}
	
	@Test
	public void testGetDormantAccountEmptyAccounts() {
		
		List<Account> accounts = new ArrayList<>();
		
		Iterable<Account> accountIter = accounts;
		Mockito.doReturn(accountIter)
			.when(this.accountRepository).findAll();
		
		Map<Integer, List<AccountVo>> actual = new ConcurrentHashMap<>();
		
		int year = 2018;
		
		this.target.getDormantAccount(actual, year);
		
		Assertions.assertTrue(actual.isEmpty(), "계좌 정보가 없는 경우 빈값 반환");
	}
	
	@Test
	public void testGetDormantAccountNotEixstTransaction() {
		
		String acctNo1 = "1234";
		String name1 = "james";
		String brCode1 = "A";
		Account account1 = Account.builder()
				.acctNo(acctNo1)
				.name(name1)
				.brCode(brCode1)
				.build();
		
		List<Account> accounts = new ArrayList<>();
		accounts.add(account1);
		
		Iterable<Account> accountIter = accounts;
		Mockito.doReturn(accountIter)
			.when(this.accountRepository).findAll();

		long amt1 = 10000;
		LocalDate date1 = LocalDate.of(2018, 2, 19);
		long transactionNo1 = 1;
		Transaction transaction1 = new Transaction(acctNo1, amt1, date1, 0, false, transactionNo1 );
		
		List<Transaction> transactions = new ArrayList<>();
		transactions.add(transaction1);
		
		Mockito.doReturn(transactions)
			.when(this.transactionService)
			.findByAcctNo(Mockito.anyString());
		
		Map<Integer, List<AccountVo>> actual = new ConcurrentHashMap<>();
		
		int year = date1.getYear();
		
		this.target.getDormantAccount(actual, year);
		
		Assertions.assertTrue(actual.isEmpty(),"입력된 연도에 거래 내역이 있는 경우 빈 목록 반환 확인");
		
	}
	
	@Test
	public void testGetDormantAccountNotExistTransaction() {
		
		String acctNo1 = "1234";
		String name1 = "james";
		String brCode1 = "A";
		Account account1 = Account.builder()
				.acctNo(acctNo1)
				.name(name1)
				.brCode(brCode1)
				.build();
		
		List<Account> accounts = new ArrayList<>();
		accounts.add(account1);
		
		Iterable<Account> accountIter = accounts;
		Mockito.doReturn(accountIter)
			.when(this.accountRepository).findAll();

		long amt1 = 10000;
		LocalDate date1 = LocalDate.of(2018, 2, 19);
		long transactionNo1 = 1;
		Transaction transaction1 = new Transaction(acctNo1, amt1, date1, 0, false, transactionNo1 );
		
		List<Transaction> transactions = new ArrayList<>();
		transactions.add(transaction1);
		Mockito.doReturn(transactions)
			.when(this.transactionService)
			.findByAcctNo(Mockito.anyString());
		Map<Integer, List<AccountVo>> actual = new ConcurrentHashMap<>();
		
		int year = 2019;
		
		this.target.getDormantAccount(actual, year);
		
		Assertions.assertAll("입력된 연도의 거래 내역이 없는 계좌 정보를 추가하는 것을 확인",
				() -> Assertions.assertTrue(actual.containsKey(year)),
				() -> Assertions.assertFalse(actual.get(year).isEmpty()),
				() -> Assertions.assertEquals(acctNo1, actual.get(year).get(0).getAcctNo()),
				() -> Assertions.assertEquals(name1, actual.get(year).get(0).getName())
			);
	}
	
	@Test
	public void testGetMaximumAmountEmptyAccount() {
		
		List<Account> accounts = new ArrayList<>();
		
		Iterable<Account> accountIter = accounts;
		
		Mockito.doReturn(accountIter)
			.when(this.accountRepository)
			.findAll();
		
		Map<Integer, AccountVo> actuals = this.target.getMaximumAmount();
		
		Assertions.assertTrue(actuals.isEmpty(), "계좌 정보가 없는 경우 빈 값 반환 확인");
	}
	
	@Test
	public void testGetMaximumAmountEmptyTransactions() {
		
		Map<Integer, AccountVo> accountMap = new ConcurrentHashMap<>();
		
		String acctNo = "1111";
		String name = "제이";
		String brCode = "C";
		
		Account account = Account.builder()
				.acctNo(acctNo)
				.name(name)
				.brCode(brCode)
				.build();
		
		Map<Integer, List<Transaction>> yearTransactionMap = new ConcurrentHashMap<>();
		
		this.target.addMaximumAmtAccount(accountMap, account, yearTransactionMap);
		
		Assertions.assertTrue(yearTransactionMap.isEmpty(), "거래 내역이 없는 경우 빈 값 확인");
	}
	
	@Test
	public void testGetMaximumAmountLargerBefore() {
		
		Map<Integer, AccountVo> accountMap = new ConcurrentHashMap<>();
		
		String beforeAcctNo = "1234";
		String beforeName = "james";
		long sumAmt = 500;
		AccountVo accountVo = AccountVo.builder()
				.acctNo(beforeAcctNo)
				.name(beforeName)
				.sumAmt(sumAmt)
				.build();
		
		accountMap.put(2019, accountVo);
		
		String acctNo = "1111";
		String name = "제이";
		String brCode = "C";
		
		Account account = Account.builder()
				.acctNo(acctNo)
				.name(name)
				.brCode(brCode)
				.build();
		
		Map<Integer, List<Transaction>> yearTransactionMap = new ConcurrentHashMap<>();
		
		List<Transaction> transactions = new ArrayList<>();

		LocalDate date = LocalDate.of(2019, 3, 20);
		boolean isCanceled = false;
		long amount1 = 10000;
		long fee1 = 100;
		long transactionNo1 = 1;
		Transaction transaction1 = new Transaction(acctNo,
				amount1,
				date,
				fee1,
				isCanceled,
				transactionNo1);
		
		transactions.add(transaction1);
		
		yearTransactionMap.put(date.getYear(), transactions);
		
		this.target.addMaximumAmtAccount(accountMap, account, yearTransactionMap);
		
		Assertions.assertAll("동일한 거래 연도의 거래 금액 합계가 더 작은 경우, 변경하는 것을 확인",
				()->Assertions.assertTrue(accountMap.containsKey(date.getYear())),
				()->Assertions.assertEquals(acctNo, accountMap.get(date.getYear()).getAcctNo()),
				()->Assertions.assertEquals(name, accountMap.get(date.getYear()).getName()),
				()->Assertions.assertEquals(amount1-fee1, accountMap.get(date.getYear()).getSumAmt())
				);
	}
	

	@Test
	public void testGetMaximumAmountNullBefore() {
		
		Map<Integer, AccountVo> accountMap = new ConcurrentHashMap<>();
		
		String acctNo = "1111";
		String name = "제이";
		String brCode = "C";
		
		Account account = Account.builder()
				.acctNo(acctNo)
				.name(name)
				.brCode(brCode)
				.build();
		
		Map<Integer, List<Transaction>> yearTransactionMap = new ConcurrentHashMap<>();
		
		List<Transaction> transactions = new ArrayList<>();

		LocalDate date = LocalDate.of(2019, 3, 20);
		boolean isCanceled = false;
		long amount1 = 10000;
		long fee1 = 100;
		long transactionNo1 = 1;
		Transaction transaction1 = new Transaction(acctNo,
				amount1,
				date,
				fee1,
				isCanceled,
				transactionNo1);
		
		transactions.add(transaction1);
		
		yearTransactionMap.put(date.getYear(), transactions);
		
		this.target.addMaximumAmtAccount(accountMap, account, yearTransactionMap);
		
		Assertions.assertAll("입력된 계정 맵이 빈 경우, 입력된 거래 내역 맵의 계정을 추가하는 것을 확인",
				()->Assertions.assertFalse(accountMap.isEmpty()),
				()->Assertions.assertTrue(accountMap.containsKey(date.getYear())),
				()->Assertions.assertEquals(acctNo, accountMap.get(date.getYear()).getAcctNo()),
				()->Assertions.assertEquals(name, accountMap.get(date.getYear()).getName()),
				()->Assertions.assertEquals(amount1-fee1, accountMap.get(date.getYear()).getSumAmt())
				);
	}
	
	@Test
	public void testGetMaximumAmountSmallBefore() {
		
		Map<Integer, AccountVo> accountMap = new ConcurrentHashMap<>();
		
		String beforeAcctNo = "1234";
		String beforeName = "james";
		long sumAmt = 2000000;
		AccountVo accountVo = AccountVo.builder()
				.acctNo(beforeAcctNo)
				.name(beforeName)
				.sumAmt(sumAmt)
				.build();
		
		accountMap.put(2019, accountVo);
		
		String acctNo = "1111";
		String name = "제이";
		String brCode = "C";
		
		Account account = Account.builder()
				.acctNo(acctNo)
				.name(name)
				.brCode(brCode)
				.build();
		
		Map<Integer, List<Transaction>> yearTransactionMap = new ConcurrentHashMap<>();
		
		List<Transaction> transactions = new ArrayList<>();

		LocalDate date = LocalDate.of(2019, 3, 20);
		boolean isCanceled = false;
		long amount1 = 10000;
		long fee1 = 100;
		long transactionNo1 = 1;
		Transaction transaction1 = new Transaction(acctNo,
				amount1,
				date,
				fee1,
				isCanceled,
				transactionNo1);
		
		transactions.add(transaction1);
		
		yearTransactionMap.put(date.getYear(), transactions);
		
		this.target.addMaximumAmtAccount(accountMap, account, yearTransactionMap);
		
		Assertions.assertAll("동일한 거래 연도의 거래 금액 합계가 더 큰 경우, 변경되지 않는 것을 확인",
				()->Assertions.assertTrue(accountMap.containsKey(date.getYear())),
				()->Assertions.assertEquals(beforeAcctNo, accountMap.get(date.getYear()).getAcctNo()),
				()->Assertions.assertEquals(beforeName, accountMap.get(date.getYear()).getName()),
				()->Assertions.assertEquals(sumAmt, accountMap.get(date.getYear()).getSumAmt())
				);
	}
	
	@Test
	public void testGetSumAmountEmptyTransacions() {
		
		List<Transaction> transactions = new ArrayList<>();
		
		long actual = this.target.getSumAmount(transactions);
		
		Assertions.assertEquals(0, actual, "입력된 거래 내역이 없는 경우 0 반환 확인");
	}
	
	@Test
	public void testGetSumAmountOnlyNotCanceled() {
		
		List<Transaction> transactions = new ArrayList<>();

		String acctNo = "1111";
		LocalDate date = LocalDate.of(2019, 3, 20);
		boolean isCanceled = false;
		long amount1 = 10000;
		long fee1 = 100;
		long transactionNo1 = 1;
		Transaction transaction1 = new Transaction(acctNo,
				amount1,
				date,
				fee1,
				isCanceled,
				transactionNo1);
		
		transactions.add(transaction1);
		
		long amount2 = 20000;
		long fee2 = 0;
		long transactionNo2 = 2;
		Transaction transaction2 = new Transaction(acctNo,
				amount2,
				date,
				fee2, 
				isCanceled,
				transactionNo2);
		
		transactions.add(transaction2);
		
		long actual = this.target.getSumAmount(transactions);
		
		Assertions.assertEquals((amount1-fee1 + amount2 - fee2), actual,
				"취소되지 않은 거래 내역의 거래금액에서 수수료를 뺀 금액 반환 확인");
	}
	
	@Test
	public void testGetSumAmountWithCanceled() {
		
		List<Transaction> transactions = new ArrayList<>();

		String acctNo = "1111";
		LocalDate date = LocalDate.of(2019, 3, 20);
		boolean isCanceled1 = false;
		long amount1 = 10000;
		long fee1 = 100;
		long transactionNo1 = 1;
		Transaction transaction1 = new Transaction(acctNo,
				amount1,
				date,
				fee1,
				isCanceled1,
				transactionNo1);
		
		transactions.add(transaction1);
		
		long amount2 = 20000;
		long fee2 = 0;
		long transactionNo2 = 2;
		boolean isCanceled2 = true;
		Transaction transaction2 = new Transaction(acctNo,
				amount2,
				date,
				fee2, 
				isCanceled2,
				transactionNo2);
		
		transactions.add(transaction2);
		
		long actual = this.target.getSumAmount(transactions);
		
		Assertions.assertEquals((amount1-fee1), actual,
				"취소되지 않은 거래 내역의 거래금액에서 수수료를 뺀 금액 반환 확인");
	}
	
	@Test
	public void testGetTransactionMultiYear() {
		
		List<Transaction> transactions = new ArrayList<>();

		String acctNo = "1111";
		LocalDate date = LocalDate.of(2019, 3, 20);
		boolean isCanceled1 = false;
		long amount1 = 10000;
		long fee1 = 100;
		long transactionNo1 = 1;
		Transaction transaction1 = new Transaction(acctNo,
				amount1,
				date,
				fee1,
				isCanceled1,
				transactionNo1);
		
		transactions.add(transaction1);

		LocalDate date2 = LocalDate.of(2018, 4, 12);
		long amount2 = 20000;
		long fee2 = 0;
		long transactionNo2 = 2;
		Transaction transaction2 = new Transaction(acctNo,
				amount2,
				date2,
				fee2, 
				isCanceled1,
				transactionNo2);
		
		transactions.add(transaction2);
		
		Map<Integer, List<Transaction>> actuals = this.target.getTransactionYearMap(transactions);
		
		Assertions.assertAll("입력된 거래 내역의 연도를 키로, 거래내역을 포함하는 것을 확인",
			() -> Assertions.assertTrue(actuals.containsKey(date.getYear())),
			() -> Assertions.assertTrue(actuals.containsKey(date2.getYear())),
			() -> Assertions.assertTrue(actuals.get(date.getYear()).contains(transaction1)),
			() -> Assertions.assertTrue(actuals.get(date2.getYear()).contains(transaction2))
		);
	}
	
	@Test
	public void testGetTransactionOnlyOneYear() {
		
		List<Transaction> transactions = new ArrayList<>();

		String acctNo = "1111";
		LocalDate date = LocalDate.of(2019, 3, 20);
		boolean isCanceled1 = false;
		long amount1 = 10000;
		long fee1 = 100;
		long transactionNo1 = 1;
		Transaction transaction1 = new Transaction(acctNo,
				amount1,
				date,
				fee1,
				isCanceled1,
				transactionNo1);
		
		transactions.add(transaction1);

		LocalDate date2 = LocalDate.of(2019, 4, 12);
		long amount2 = 20000;
		long fee2 = 0;
		long transactionNo2 = 2;
		Transaction transaction2 = new Transaction(acctNo,
				amount2,
				date2,
				fee2, 
				isCanceled1,
				transactionNo2);
		
		transactions.add(transaction2);
		
		Map<Integer, List<Transaction>> actuals = this.target.getTransactionYearMap(transactions);
		
		Assertions.assertAll("입력된 거래 내역의 연도를 키로, 거래내역을 포함하는 것을 확인",
			() -> Assertions.assertTrue(actuals.containsKey(date.getYear())),
			() -> Assertions.assertTrue(actuals.get(date.getYear()).contains(transaction1)),
			() -> Assertions.assertTrue(actuals.get(date.getYear()).contains(transaction2))
		);
	}
	
	@Test
	public void testGetTransactionSumMapEmptyTransacions() {
		
		List<Transaction> transactions = new ArrayList<>();
		
		Map<Integer, List<Transaction>> actuals = this.target.getTransactionYearMap(transactions);
		
		Assertions.assertTrue(actuals.isEmpty(), "입력된 거래내역이 없는 경우 빈 값 반환 확인");
	}
	
	@Test
	public void testGetTransactionWithCanceled() {
		
		List<Transaction> transactions = new ArrayList<>();

		String acctNo = "1111";
		LocalDate date = LocalDate.of(2019, 3, 20);
		boolean isCanceled1 = false;
		long amount1 = 10000;
		long fee1 = 100;
		long transactionNo1 = 1;
		Transaction transaction1 = new Transaction(acctNo,
				amount1,
				date,
				fee1,
				isCanceled1,
				transactionNo1);
		
		transactions.add(transaction1);

		LocalDate date2 = LocalDate.of(2018, 4, 12);
		long amount2 = 20000;
		long fee2 = 0;
		long transactionNo2 = 2;
		boolean isCanceled2 = true;
		Transaction transaction2 = new Transaction(acctNo,
				amount2,
				date2,
				fee2, 
				isCanceled2,
				transactionNo2);
		
		transactions.add(transaction2);
		
		Map<Integer, List<Transaction>> actuals = this.target.getTransactionYearMap(transactions);
		
		Assertions.assertAll("입력된 거래 내역의 연도를 키로, 거래내역을 포함하는 것을 확인",
			() -> Assertions.assertTrue(actuals.containsKey(date.getYear())),
			() -> Assertions.assertFalse(actuals.containsKey(date2.getYear())),
			() -> Assertions.assertTrue(actuals.get(date.getYear()).contains(transaction1))
		);
	}
}
