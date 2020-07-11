package com.kakaopay.sec.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

/**
 * 계좌 정보 반환 클래스
 */
@Builder
@AllArgsConstructor
@Getter
@JsonInclude(Include.NON_NULL)
public class AccountVo implements Comparable<AccountVo> {

	/** 계좌 번호 */
	private String acctNo;
	
	/** 계좌 명 */
	private String name;
	
	/** 합계 금액 */
	private Long sumAmt;

	/**
	 * ({@inheritDoc}
	 */
	@Override
	public int compareTo(AccountVo o) {
		
		// 내림차순 정렬을 위한 합계 금액 비교
		int compare = Long.compare(o.sumAmt, this.sumAmt);
		
		if(compare != 0) {
			return compare;
		}
		
		// 합계 금액이 같은 경우 계좌 번호로 정렬
		return this.acctNo.compareTo(o.getAcctNo());
	}
}
