package com.kakaopay.sec.service;

import java.util.List;
import java.util.Optional;

import com.kakaopay.sec.model.response.BranchVo;

public interface BranchService {

	// 3. 연도별 관리점별 거래금액 합계 순서대로 출력(연도-관리점명/관리점코드/합계)
	List<BranchVo> getByYear(int year);
	
	// 4. 지점명을 입력하면 해당 지점의 거래금액 합계 출력(관리점명/관리점코드/합계)
	Optional<BranchVo> getByName(String brName);
}
