package com.kakaopay.sec.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Service;

import com.kakaopay.sec.model.entity.Account;
import com.kakaopay.sec.model.entity.Transaction;
import com.kakaopay.sec.model.response.AccountVo;
import com.kakaopay.sec.repository.AccountRepository;
import com.kakaopay.sec.service.AccountService;
import com.kakaopay.sec.service.TransactionService;

@Service
public class AccountServiceImpl implements AccountService {

	private final AccountRepository accountRepository;

	private final TransactionService transactionService;

	public AccountServiceImpl(TransactionService transactionService, 
			AccountRepository accountRepository) {
		this.transactionService = transactionService;
		this.accountRepository = accountRepository;
	}

	@Override
	public List<Account> getByBrCode(String brCode) {

		Map<String, Account> accounts = this.accountRepository.findAll();

		List<Account> branchAccs = new ArrayList<>();

		for (Account account : accounts.values()) {

			if (account.getBrCode().equals(brCode)) {
				branchAccs.add(account);
			}
		}

		return branchAccs;
	}

	@Override
	public Map<Integer, List<AccountVo>> getDormantAccounts() {

		Map<String, Account> accounts = this.accountRepository.findAll();

		Map<Integer, List<AccountVo>> accountMap = new ConcurrentHashMap<>();

		Set<Integer> years = this.transactionService.getAllTransacionYears();
		for (int year : years) {

			for (Account account : accounts.values()) {

				Optional<Set<Transaction>> transations = this.transactionService.findByAcctNo(account.getAcctNo());

				AccountVo accountVo = AccountVo.builder().acctNo(account.getAcctNo()).name(account.getName()).build();

				if (transations.isPresent()) {
					long count = transations.get().stream()
							.filter((Transaction transaction) -> transaction.getDate().getYear() == year).count();

					if (count != 0) {
						continue;
					}
				}

				accountMap.putIfAbsent(year, new ArrayList<>());
				accountMap.get(year).add(accountVo);
			}

		}

		return accountMap;
	}

	@Override
	public Map<Integer, AccountVo> getMaximumAmount() {

		Map<String, Account> accounts = this.accountRepository.findAll();

		Map<Integer, AccountVo> accountMap = new ConcurrentHashMap<>();

		for (Account account : accounts.values()) {

			Optional<Set<Transaction>> transactions = this.transactionService.findByAcctNo(account.getAcctNo());

			Map<Integer, List<Transaction>> sumMap = getTransacionSumMap(transactions);
			for (Entry<Integer, List<Transaction>> entry : sumMap.entrySet()) {

				long sum = this.getSumAmount(entry.getValue());

				AccountVo accountVo = AccountVo.builder().acctNo(account.getAcctNo()).name(account.getName())
						.sumAmt(sum).build();

				AccountVo before = accountMap.get(entry.getKey());
				if (before == null || accountVo.compareTo(before) > 0) {
					accountMap.put(entry.getKey(), accountVo);
				}
			}

		}

		return accountMap;
	}

	private long getSumAmount(Collection<Transaction> transactions) {

		AtomicLong sum = new AtomicLong(0);

		transactions.stream().filter((Transaction transaction) -> transaction.isCanceled() == false)
				.forEach((Transaction transation) -> {
					long sumAmt = transation.getAmt() - transation.getFee();
					sum.set((sum.addAndGet(sumAmt)));
				});

		return sum.get();
	}

	private Map<Integer, List<Transaction>> getTransacionSumMap(Optional<Set<Transaction>> transactions) {
		Map<Integer, List<Transaction>> transMap = new ConcurrentHashMap<>();

		transactions.ifPresent((Set<Transaction> trans) -> {
			trans.stream().filter((Transaction transaction) -> transaction.isCanceled() == false)
					.forEach((Transaction transaction) -> {
						int year = transaction.getDate().getYear();
						transMap.putIfAbsent(year, new ArrayList<>());
						transMap.put(year, transMap.get(year));
					});
		});

		return transMap;
	}

}
