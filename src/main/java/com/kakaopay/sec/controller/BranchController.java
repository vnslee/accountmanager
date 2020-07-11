package com.kakaopay.sec.controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kakaopay.sec.model.response.BranchVo;
import com.kakaopay.sec.model.response.ResponseError;
import com.kakaopay.sec.service.BranchService;
import com.kakaopay.sec.service.TransactionService;

import lombok.extern.slf4j.Slf4j;

/**
 * 지점 관리 Rest API 컨트롤러
 */
@Slf4j
@RestController
@RequestMapping("/api/v1")
public class BranchController {

	/** 관리점 관리 서비스 */
	private final BranchService branchService;
	
	/** 거래 내역 관리 서비스 */
	private final TransactionService transactionService;
	
	public BranchController(BranchService branchService,
			TransactionService transactionService) {
		this.branchService = branchService;
		this.transactionService = transactionService;
	}

	/**
	 * 입력받은 관리점의 정보를 반환한다.
	 * @param brName 관리점 명
	 * @return 관리점 정보
	 */
	@GetMapping("/branches/{brName}")
	public ResponseEntity<Object> getBranchAmount(@PathVariable("brName") String brName) {
		
		Optional<BranchVo> branch= this.branchService.getByName(brName);
		
		if(branch.isEmpty()) {
			log.debug("존재하지 않는 지점명 입력 : {}", brName);
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body(ResponseError.builder()
							.errCode(HttpStatus.NOT_FOUND.value())
							.message("br code not found error")
							.build());
		}
		
		return ResponseEntity.ok(branch.get());
	}
	
	/**
	 * 관리점의 연도별 합계 금액 정보를 반환한다.
	 * 
	 * @return 연도별 관리점별 거래 금액 합계 정보
	 */
	@GetMapping("/branches")
	public Map<Integer, List<BranchVo>> getBranches() {

		Set<Integer> years = this.transactionService.getAllTransacionYears();
	
		Map<Integer, List<BranchVo>> branches = new ConcurrentHashMap<>();
		for(int year : years) {
			branches.put(year, this.branchService.getByYear(year));
		}

		return branches;
	}
}
