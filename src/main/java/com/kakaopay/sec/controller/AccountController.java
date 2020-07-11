package com.kakaopay.sec.controller;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kakaopay.sec.model.response.AccountVo;
import com.kakaopay.sec.service.AccountService;

/**
 * 계좌 관리 Rest API 컨트롤러
 */
@RestController
@RequestMapping("/api/v1")
public class AccountController {

	private final AccountService accountService;

	public AccountController(AccountService accountService) {

		this.accountService = accountService;
	}

	/**
	 * 대상 데이터 내 연도별 거래 내역이 없는 계좌 정보를 반환한다.
	 * 
	 * @return 연도별 거래 내역이 없는 계좌 정보 맵
	 */
	@GetMapping("/accounts/dormant")
	public Map<Integer, List<AccountVo>> getDormantAccount() {

		return this.accountService.getDormantAccounts();
	}
	
	/**
	 * 대상 데이터 내 합계 금액이 최대인 계좌 정보를 반환한다.
	 * 
	 * @return
	 */
	@GetMapping("/accounts/maximums")
	public Map<Integer, AccountVo> getMaximumAmountAccounts() {
		
		return this.accountService.getMaximumAmount();
	}
	
}
