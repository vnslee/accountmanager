package com.kakaopay.sec.repository.impl;

import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.kakaopay.sec.model.entity.Transaction;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class TransactionRepositoryImplTest {

	@Autowired
	private TransactionRepositoryImpl transactionRepository;

	@AfterEach
	public void reset() {
		this.transactionRepository.deleteAll();
	}

	@Test
	public void testFindAllMultiKeys() {
		String acctNo = "11111";
		LocalDate date1 = LocalDate.of(2020, 7, 9);
		long transactionNo1 = 1;
		long amt1 = 10000;
		long fee1 = 0;
		boolean isCanceled = false;

		Transaction transaction1 = new Transaction(acctNo, amt1, date1, fee1, isCanceled, transactionNo1);

		this.transactionRepository.save(transaction1);

		String acctNo2 = "12234";
		LocalDate date2 = LocalDate.of(2020, 7, 11);
		long transactionNo2 = 1;
		long amt2 = 100000;
		long fee2 = 0;

		Transaction transaction2 = new Transaction(acctNo2, amt2, date2, fee2, isCanceled, transactionNo2);

		this.transactionRepository.save(transaction2);

		Set<Transaction> actual = this.transactionRepository.findAll();

		Assertions.assertEquals(2, actual.size());

		Assertions.assertAll("포함된 거래 내역 확인", 
				() -> Assertions.assertTrue(actual.contains(transaction1)),
				() -> Assertions.assertTrue(actual.contains(transaction2))
		);
	}

	@Test
	public void testFindAllSingleKey() {
		String acctNo = "11111";
		LocalDate date1 = LocalDate.of(2020, 7, 9);
		long transactionNo1 = 1;
		long amt1 = 10000;
		long fee1 = 0;
		boolean isCanceled = false;

		Transaction transaction1 = new Transaction(acctNo, amt1, date1, fee1, isCanceled, transactionNo1);

		this.transactionRepository.save(transaction1);

		LocalDate date2 = LocalDate.of(2020, 7, 11);
		long transactionNo2 = 1;
		long amt2 = 100000;
		long fee2 = 0;

		Transaction transaction2 = new Transaction(acctNo, amt2, date2, fee2, isCanceled, transactionNo2);

		this.transactionRepository.save(transaction2);

		Set<Transaction> actual = this.transactionRepository.findAll();

		Assertions.assertEquals(2, actual.size());

		Assertions.assertAll("포함된 거래 내역 확인", 
				() -> Assertions.assertTrue(actual.contains(transaction1)),
				() -> Assertions.assertTrue(actual.contains(transaction2))
		);
	}

	@Test
	public void testFindByIdMulti() {

		String acctNo = "11111";
		LocalDate date1 = LocalDate.of(2020, 7, 9);
		long transactionNo1 = 1;
		long amt1 = 10000;
		long fee1 = 0;
		boolean isCanceled = false;

		Transaction transaction1 = new Transaction(acctNo, amt1, date1, fee1, isCanceled, transactionNo1);

		this.transactionRepository.save(transaction1);

		LocalDate date2 = LocalDate.of(2020, 7, 11);
		long transactionNo2 = 1;
		long amt2 = 100000;
		long fee2 = 0;

		Transaction transaction2 = new Transaction(acctNo, amt2, date2, fee2, isCanceled, transactionNo2);

		this.transactionRepository.save(transaction2);

		Optional<Set<Transaction>> actual = this.transactionRepository.findById(acctNo);

		Assertions.assertTrue(actual.isPresent(), "입력된값이 존재하는 경우 확인");
		Assertions.assertEquals(2, actual.get().size());

		Assertions.assertAll("포함된 거래 내역 확인", 
				() -> Assertions.assertTrue(actual.get().contains(transaction1)),
				() -> Assertions.assertTrue(actual.get().contains(transaction2))
		);
	}

	@Test
	public void testFindByIdSingle() {

		String acctNo = "11111";
		LocalDate date = LocalDate.of(2020, 7, 9);
		long transactionNo = 1;
		long amt = 10000;
		long fee = 0;
		boolean isCanceled = false;

		Transaction transaction = new Transaction(acctNo, amt, date, fee, isCanceled, transactionNo);

		this.transactionRepository.save(transaction);

		Optional<Set<Transaction>> actual = this.transactionRepository.findById(acctNo);

		Assertions.assertTrue(actual.isPresent(), "입력된값이 존재하는 경우 확인");
		Assertions.assertTrue(actual.get().contains(transaction), "동일 ID인 transaction 거래 내역 포함 확인");
	}

	@Test
	public void testGetAllKeys() {

		String acctNo = "11111";
		LocalDate date1 = LocalDate.of(2020, 7, 9);
		long transactionNo1 = 1;
		long amt1 = 10000;
		long fee1 = 0;
		boolean isCanceled = false;

		Transaction transaction1 = new Transaction(acctNo, amt1, date1, fee1, isCanceled, transactionNo1);

		this.transactionRepository.save(transaction1);

		String acctNo2 = "12234";
		LocalDate date2 = LocalDate.of(2020, 7, 11);
		long transactionNo2 = 1;
		long amt2 = 100000;
		long fee2 = 0;

		Transaction transaction2 = new Transaction(acctNo2, amt2, date2, fee2, isCanceled, transactionNo2);

		this.transactionRepository.save(transaction2);

		Set<String> actuals = this.transactionRepository.getAllKeys();

		Assertions.assertEquals(2, actuals.size(), "포함된 거래의 계좌 번호만큼 포함 확인");
		Assertions.assertAll("포함된 키의 내용 확인", 
				() -> Assertions.assertTrue(actuals.contains(acctNo)),
				() -> Assertions.assertTrue(actuals.contains(acctNo2))
		);
	}

	@Test
	public void testGetAllKeysEmpty() {

		Set<String> actuals = this.transactionRepository.getAllKeys();

		Assertions.assertTrue(actuals.isEmpty(), "등록된 거래가 없는 경우 빈값 반환");
	}

}
