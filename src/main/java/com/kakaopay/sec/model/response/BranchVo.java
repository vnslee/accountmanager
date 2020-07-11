package com.kakaopay.sec.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

/**
 * 관리점 정보 반환 클래스
 */
@Builder
@AllArgsConstructor
@Getter
@JsonInclude(Include.NON_NULL)
public class BranchVo implements Comparable<BranchVo>{

	/** 관리점 코드 */
	private String brCode;
	
	/** 관리점 명 */
	private String brName;
	
	/** 거래 금액 합계 */
	private Long sumAmt;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int compareTo(BranchVo o) {
		// 내림 차순 정렬을 위해 반대로 비교
		return Long.compare(o.getSumAmt(), this.sumAmt);
	}
}
