package com.kakaopay.sec.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kakaopay.sec.model.entity.Transaction;
import com.kakaopay.sec.model.response.AccountVo;
import com.kakaopay.sec.service.AccountService;
import com.kakaopay.sec.service.TransactionService;

/**
 * 계좌 관리 Rest API 컨트롤러
 */
@RestController
@RequestMapping("/api/v1")
public class AccountController {

	private final AccountService accountService;

	private final TransactionService transactionService;

	public AccountController(AccountService accountService, TransactionService transactionService) {

		this.accountService = accountService;
		this.transactionService = transactionService;
	}

	/**
	 * 2018과 2019년 합계 금액이 최대인 계좌 정보를 반환한다.
	 * 
	 * @return
	 */
	@GetMapping("/accounts/maximums")
	public Map<Integer, AccountVo> getMaximumAmountAccounts() {
		
		return this.accountService.getMaximumAmount();
	}
	
	/**
	 * 2018년 또는 2019년에 거래 내역이 없는 계좌 정보를 반환한다.
	 * 
	 * @return
	 */
	@GetMapping("/accounts/dormant")
	public Map<Integer, List<AccountVo>> getDormantAccount() {

		return this.accountService.getDormantAccounts();
	}
	
}
