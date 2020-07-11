package com.kakaopay.sec.repository.impl;

import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.kakaopay.sec.model.entity.Branch;
import com.kakaopay.sec.repository.BranchRepository;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class BranchRepositoryImplTest {

	@Autowired
	private BranchRepository branchRepository;
	
	@AfterEach
	public void reset() {
		this.branchRepository.deleteAll();
	}
	
	@Test
	public void testFindAll() {
		
		String brName1 = "강남점";
		String brCode1 = "A";
		
		Branch branch1 = Branch.builder()
				.brName(brName1)
				.brCode(brCode1)
				.build();
		
		this.branchRepository.save(branch1);
		
		String brName2 = "판교";
		String brCode2 = "B";
		
		Branch branch2 = Branch.builder()
				.brName(brName2)
				.brCode(brCode2)
				.build();
		
		this.branchRepository.save(branch2);
		
		Map<String, Branch> actuals = this.branchRepository.findAll();
		
		Assertions.assertEquals(2, actuals.keySet().size(), "2개 포함 확인");
		
		Assertions.assertTrue(actuals.containsKey(branch1.getBrName()),
				"branch1 데이터 포함 확인");
		Assertions.assertTrue(actuals.containsKey(branch2.getBrName()),
				"branch2 데이터 포함 확인");
		
	}

	@Test
	void testFindByID() {
	
		String brName = "강남점";
		String brCode = "A";
		
		Branch branch = Branch.builder()
				.brName(brName)
				.brCode(brCode)
				.build();
		
		this.branchRepository.save(branch);
		
		Optional<Branch> actual = this.branchRepository.findById(brName);
	
		Assertions.assertTrue(actual.isPresent(), "입력된 관리점 명이 존재하는 경우 확인");
		Assertions.assertEquals(brName, actual.get().getBrName(),
				"관리점명 확인");
		Assertions.assertEquals(brCode, actual.get().getBrCode(),
				"관리점 코드 확인");
	}
	
	@Test
	void testFindByIDNotExist() {
	
		String brName = "강남점";
		String brCode = "A";
		
		Branch branch = Branch.builder()
				.brName(brName)
				.brCode(brCode)
				.build();
		
		this.branchRepository.save(branch);
		
		Optional<Branch> actual = this.branchRepository.findById("Not");
	
		Assertions.assertFalse(actual.isPresent(),
				"입력된 관리점 명이 존재하지 않는 경우 확인");
	}
}
