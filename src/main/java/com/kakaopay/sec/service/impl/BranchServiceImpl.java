package com.kakaopay.sec.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Service;

import com.google.common.annotations.VisibleForTesting;
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

	private static final String CLOSED_BRANCH = "분당점";
	
	private final AccountService accountService;
	
	private final BranchRepository branchRepository;
	
	private final TransactionService transactionService;
	
	public BranchServiceImpl(BranchRepository branchRepository,
			TransactionService transactionService,
			AccountService accountService) {
		
		this.branchRepository = branchRepository;
		this.transactionService = transactionService;
		this.accountService = accountService;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Optional<BranchVo> getByName(String brName) {

		Optional<Branch> branchOpt = this.branchRepository.findById(brName);
		
		if(branchOpt.isEmpty() || brName.equals(CLOSED_BRANCH)) {
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

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<BranchVo> getByYear(int year) {

		Iterable<Branch> branches = this.branchRepository.findAll();
		
		List<BranchVo> branchesByYear = new ArrayList<>();
		
		for(Branch branch : branches) {
			
			if(branch == null) {
				continue;
			}
			
			long sumAmt = this.getSumAmount(year, branch);
			
			BranchVo branchVo = BranchVo.builder()
				.brName(branch.getBrName())
				.brCode(branch.getBrCode())
				.sumAmt(sumAmt)
				.build();
			
			branchesByYear.add(branchVo);
		}
		
		Collections.sort(branchesByYear);
		
		return branchesByYear;
	}

	/**
	 * 입력된 관리점의 총 거래금액 합계를 반환한다.
	 * 
	 * @param branch 관리점
	 * @return 거래 금액 합계
	 */
	@VisibleForTesting
	long getSumAmount(Branch branch) {
		List<Account> accounts = this.accountService.getByBrCode(branch.getBrCode());
		
		AtomicLong sumAmt = new AtomicLong(0);
		
		for(Account account : accounts) {
			List<Transaction> transactions = this.transactionService.findByAcctNo(account.getAcctNo());
			
			transactions.stream()
				.filter(
						(Transaction transaction) -> transaction.isCanceled() == false)
				.map(Transaction::getAmt)
				.forEach(
						(Long amount) -> sumAmt.set(sumAmt.addAndGet(amount)));
			
		}
		
		return sumAmt.get();
	}

	/**
	 * 입력받은 연도의 관리점의 총 거래 금액 합계를 반환한다.
	 * 
	 * @param year 대상 연도
	 * @param branch 관리점
	 * @return 거래 금액 합계
	 */
	@VisibleForTesting
	long getSumAmount(int year, Branch branch) {
		
		List<Account> accounts = this.accountService.getByBrCode(branch.getBrCode());
		AtomicLong sumAmt = new AtomicLong(0);
		
		for(Account account : accounts) {
			List<Transaction> transactions = this.transactionService.findByAcctNo(account.getAcctNo());
			
			transactions.stream()
				// 취소된 거래는 제외
				.filter(
						(Transaction transaction) -> transaction.isCanceled() == false)
				.filter(
						(Transaction transaction) -> transaction.getDate().getYear() == year)
				.map(Transaction::getAmt)
				.forEach(
						(Long amount) -> sumAmt.set(sumAmt.addAndGet(amount)
				)
			);
			
		}
		return sumAmt.get();
	}

}
