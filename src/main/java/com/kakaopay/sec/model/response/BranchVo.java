package com.kakaopay.sec.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@AllArgsConstructor
@Getter
public class BranchVo implements Comparable<BranchVo>{

	private String brCode;
	
	private String brName;
	
	private long sumAmt;

	@Override
	public int compareTo(BranchVo o) {
		return Long.compare(this.sumAmt, o.getSumAmt());
	}
}
