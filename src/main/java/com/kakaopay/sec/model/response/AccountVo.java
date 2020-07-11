package com.kakaopay.sec.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@AllArgsConstructor
@Getter
@JsonInclude(Include.NON_NULL)
public class AccountVo implements Comparable<AccountVo> {

	private String acctNo;
	
	private String name;
	
	private long sumAmt;

	@Override
	public int compareTo(AccountVo o) {
		
		int compare = Long.compare(this.sumAmt, o.sumAmt);
		if(compare != 0) {
			return compare;
		}
		
		return this.acctNo.compareTo(o.getAcctNo());
	}
}
