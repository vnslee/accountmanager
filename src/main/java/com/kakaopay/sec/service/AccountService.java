package com.kakaopay.sec.service;

import java.util.List;
import java.util.Map;

import com.kakaopay.sec.model.entity.Account;
import com.kakaopay.sec.model.response.AccountVo;

public interface AccountService {

	List<Account> getByBrCode(String brCode);
	
	// 2. 연도별 거래가 없는 고객(연도/계좌명/계좌번호)
	Map<Integer, List<AccountVo>> getDormantAccounts();
	
	// 1. 연도별 합계 금액이 가장 많은 고객(연도/계좌명/계좌번호/합계금액)
	Map<Integer, AccountVo> getMaximumAmount();
}
