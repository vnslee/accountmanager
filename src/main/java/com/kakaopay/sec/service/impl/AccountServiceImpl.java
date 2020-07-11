package com.kakaopay.sec.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Service;

import com.google.common.annotations.VisibleForTesting;
import com.kakaopay.sec.model.entity.Account;
import com.kakaopay.sec.model.entity.Transaction;
import com.kakaopay.sec.model.response.AccountVo;
import com.kakaopay.sec.repository.AccountRepository;
import com.kakaopay.sec.service.AccountService;
import com.kakaopay.sec.service.TransactionService;

/**
 * 계좌 관리 서비스
 */
@Service
public class AccountServiceImpl implements AccountService {

	/** 계좌 관리 Repository */
	private final AccountRepository accountRepository;

	/** 정보 추출 대상 연도 */
	private List<Integer> targetYear = Arrays.asList(2018, 2019);

	/** 트랜잭션 관리 Repository */
	private final TransactionService transactionService;
	
	public AccountServiceImpl(TransactionService transactionService, 
			AccountRepository accountRepository) {
		this.transactionService = transactionService;
		this.accountRepository = accountRepository;
	}

	/**
	 * 입력받은 계좌가 입력 받은 연도의 최대 합계 금액을 갖는다면 맵을 갱신한다.
	 * 
	 * @param accountMap 연도 별 최대 합계금액 계좌 맵
	 * @param account 확인 대상 계좌
	 * @param yearTransactionMap 계좌의 연도별 거래 내역 맵
	 */
	@VisibleForTesting
	void addMaximumAmtAccount(Map<Integer, AccountVo> accountMap, Account account,
			Map<Integer, List<Transaction>> yearTransactionMap) {
	
		for (Entry<Integer, List<Transaction>> entry : yearTransactionMap.entrySet()) {

			long sum = this.getSumAmount(entry.getValue());

			AccountVo accountVo = AccountVo.builder()
					.acctNo(account.getAcctNo())
					.name(account.getName())
					.sumAmt(sum)
					.build();

			AccountVo before = accountMap.get(entry.getKey());
			
			if (before == null || accountVo.compareTo(before) < 0) {
				accountMap.put(entry.getKey(), accountVo);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Account> getByBrCode(String brCode) {

		List<Account> accounts = (List<Account>) this.accountRepository.findAll();

		List<Account> branchAccs = new ArrayList<>();

		accounts.stream()
			.filter((Account account) -> account != null)
			.forEach((Account account) ->  {
				if (account.getBrCode().equals(brCode)) {
					branchAccs.add(account);
				}
		});

		return branchAccs;
	}

	/**
	 * 연도별 거래 내역이 없는 계좌 맵에 입력받은 연도의 거래내역이 없는 계좌를 추가한다.
	 * 
	 * @param accountMap 연도별 계좌맵
	 * @param year 계좌 추출 대상 연도
	 */
	@VisibleForTesting
	void getDormantAccount(Map<Integer, List<AccountVo>> accountMap, int year) {
		
		List<Account> accounts = (List<Account>) this.accountRepository.findAll();
		
		accounts.stream()
			.filter((Account account) -> account != null)
			.forEach((Account account) -> {
				
				List<Transaction> transations = this.transactionService.findByAcctNo(account.getAcctNo());
				
				// 해당 연도의 거래내역 갯수 확인
				long count = transations.stream()
						.filter((Transaction transaction) 
								-> transaction.isCanceled() == false)
						.filter((Transaction transaction) 
								-> transaction.getDate().getYear() == year)
						.count();
	
				// 거래 내역이 없으면 추가
				if (count == 0) {
					
					AccountVo accountVo = AccountVo.builder()
							.acctNo(account.getAcctNo())
							.name(account.getName())
							.build();
					
					accountMap.putIfAbsent(year, new ArrayList<>());
					accountMap.get(year).add(accountVo);
				}
	
		});
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Map<Integer, List<AccountVo>> getDormantAccounts() {

		Map<Integer, List<AccountVo>> accountMap = new TreeMap<>();

		// 거래 내역의 전체 연도 목록을 가져온다.
		//Set<Integer> years = this.transactionService.getAllTransacionYears();
		
		for (int year : this.targetYear) {

			this.getDormantAccount(accountMap, year);

		}

		return accountMap;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Map<Integer, AccountVo> getMaximumAmount() {

		List<Account> accounts = (List<Account>) this.accountRepository.findAll();

		Map<Integer, AccountVo> accountMap = new ConcurrentHashMap<>();

		accounts.stream()
			.filter((Account account) -> account != null)
			.forEach((Account account) ->  {
				List<Transaction> transactions = this.transactionService.findByAcctNo(account.getAcctNo());
				
				Map<Integer, List<Transaction>> yearTransactionMap = this.getTransactionYearMap(transactions);
				
				this.addMaximumAmtAccount(accountMap, account, yearTransactionMap);
			});

		return accountMap;
	}

	/**
	 * 입력받은 거래 내역의 합계 금액을 반환한다.
	 * 
	 * @param transactions 거래 내역
	 * @return 합계 금액(거래 금액 - 수수료)
	 */
	@VisibleForTesting
	long getSumAmount(Collection<Transaction> transactions) {

		AtomicLong sum = new AtomicLong(0);

		transactions.stream()
			.filter((Transaction transaction) -> transaction.isCanceled() == false)
				.forEach((Transaction transation) -> {
					long sumAmt = transation.getAmt() - transation.getFee();
					sum.set((sum.addAndGet(sumAmt)));
				});

		return sum.get();
	}

	/**
	 * 입력받은 거래 내역 목록을 연도별 거래 내역으로 반환한다.
	 * 
	 * @param transactions 거래 내력 목록
	 * @return 연도별 거래 내역
	 */
	@VisibleForTesting
	Map<Integer, List<Transaction>> getTransactionYearMap(List<Transaction> transactions) {
		Map<Integer, List<Transaction>> transMap = new ConcurrentHashMap<>();

		transactions.stream()
			// 취소된 거래는 제외한다.
			.filter(
					(Transaction transaction) -> transaction.isCanceled() == false)
			// 대상 연도에 포함되지 않은 경우 제외한다.
			.filter(
					(Transaction transaction) -> this.targetYear.contains(transaction.getDate().getYear()))
			.forEach((Transaction transaction) -> {
					int year = transaction.getDate().getYear();
					transMap.putIfAbsent(year, new ArrayList<>());
					transMap.get(year).add(transaction);
				});

		return transMap;
	}

}
