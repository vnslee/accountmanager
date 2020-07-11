package com.kakaopay.sec.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Service;

import com.kakaopay.sec.model.entity.Account;
import com.kakaopay.sec.model.entity.Branch;
import com.kakaopay.sec.model.entity.Transaction;
import com.kakaopay.sec.model.response.BranchVo;
import com.kakaopay.sec.repository.BranchRepository;
import com.kakaopay.sec.service.AccountService;
import com.kakaopay.sec.service.BranchService;
import com.kakaopay.sec.service.TransactionService;

@Service
public class BranchServiceImpl implements BranchService {

	private final BranchRepository branchRepository;
	
	private final TransactionService transactionService;
	
	private final AccountService accountService;
	
	public BranchServiceImpl(BranchRepository branchRepository,
			TransactionService transactionService,
			AccountService accountService) {
		
		this.branchRepository = branchRepository;
		this.transactionService = transactionService;
		this.accountService = accountService;
	}

	@Override
	public Optional<BranchVo> getByName(String brName) {

		Optional<Branch> branchOpt = this.branchRepository.findById(brName);
		
		if(branchOpt.isEmpty()) {
			return Optional.empty();
		}
		
		Branch branch = branchOpt.get();
		
		long sumAmt = this.getSumAmount(branch);
		
		return Optional.of(BranchVo.builder()
				.brName(branch.getBrName())
				.brCode(branch.getBrCode())
				.sumAmt(sumAmt)
				.build());
	}

	private long getSumAmount(Branch branch) {
		List<Account> accounts = this.accountService.getByBrCode(branch.getBrCode());
		
		AtomicLong sumAmt = new AtomicLong(0);
		
		for(Account account : accounts) {
			Optional<Set<Transaction>> transactions = this.transactionService.findByAcctNo(account.getAcctNo());
			
			transactions.ifPresent((Set<Transaction> trans) -> {
				trans.stream()
					.filter((Transaction transaction) -> transaction.isCanceled() == false)
					.map(Transaction::getAmt)
					.forEach((Long amount) -> sumAmt.set(sumAmt.addAndGet(amount)));
			});
			
		}
		
		return sumAmt.get();
	}

	@Override
	public List<BranchVo> getByYear(int year) {
		
		Map<String, Branch> branches = this.branchRepository.findAll();
		
		List<BranchVo> branchesByYear = new ArrayList<>();
		
		for(Branch branch : branches.values()) {
			
			List<Account> accounts = this.accountService.getByBrCode(branch.getBrCode());
			AtomicLong sumAmt = new AtomicLong(0);
			
			for(Account account : accounts) {
				Optional<Set<Transaction>> transactions = this.transactionService.findByAcctNo(account.getAcctNo());
				
				transactions.ifPresent((Set<Transaction> trans) -> {
					trans.stream()
						.filter((Transaction transaction) -> transaction.isCanceled() == false)
						.filter((Transaction transaction) -> transaction.getDate().getYear() == year)
						.map(Transaction::getAmt)
						.forEach((Long amount) -> sumAmt.set(sumAmt.addAndGet(amount)));
				});
				
			}
			
			BranchVo branchVo = BranchVo.builder()
				.brName(branch.getBrName())
				.brCode(branch.getBrCode())
				.sumAmt(sumAmt.get())
				.build();
			
			branchesByYear.add(branchVo);
		}
		
		Collections.sort(branchesByYear);
		
		return branchesByYear;
	}

}
