package com.kakaopay.sec.controller;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kakaopay.sec.model.response.BranchVo;
import com.kakaopay.sec.model.response.ResponseError;
import com.kakaopay.sec.service.BranchService;

import lombok.extern.slf4j.Slf4j;

/**
 * 지점 관리 Rest API 컨트롤러
 */
@Slf4j
@RestController
@RequestMapping("/api/v1")
public class BranchController {

	private final BranchService branchService;
	
	public BranchController(BranchService branchService) {
		this.branchService = branchService;
	}

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
}
