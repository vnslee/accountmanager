package com.kakaopay.sec.service.impl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.kakaopay.sec.model.entity.Transaction;
import com.kakaopay.sec.repository.TransactionRepository;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class TransactionServiceImplTest {

	@SpyBean
	private TransactionServiceImpl target;
	
	@MockBean
	private TransactionRepository transactionRepository;
	
	@Test
	void testGetAllTransactionYearEmptyTransactions() {

		List<Transaction> transactions = new ArrayList<>();
		
		Mockito.doReturn(transactions)
			.when(this.target)
			.findAll();
		
		Set<Integer> actuals = this.target.getAllTransacionYears();
	
		Assertions.assertTrue(actuals.isEmpty(), "거래내역이 없는 경우 빈 값 반환 확인");
	}
	
	@Test
	public void testGetAllTransactionYears() {
		
		List<Transaction> transactions = new ArrayList<>();

		Transaction transaction1 = new Transaction("1234", 100, LocalDate.of(2018,3,20), 0, false, 1);
		Transaction transaction2 = new Transaction("1234", 1000, LocalDate.of(2018, 3,30), 0, false, 1);
		Transaction transaction3 = new Transaction("1111", 200, LocalDate.of(2019, 5, 30), 0, false, 1);
		
		transactions.add(transaction1);
		transactions.add(transaction2);
		transactions.add(transaction3);
		
		Mockito.doReturn(transactions)
			.when(this.target)
			.findAll();
		
		Set<Integer> actuals = this.target.getAllTransacionYears();
		
		Assertions.assertAll("거래 내역의 연도를 중복 없이 반환 확인",
				()-> Assertions.assertTrue(actuals.contains(transaction1.getDate().getYear())),
				()-> Assertions.assertTrue(actuals.contains(transaction2.getDate().getYear()))
		);
		
	}

}
