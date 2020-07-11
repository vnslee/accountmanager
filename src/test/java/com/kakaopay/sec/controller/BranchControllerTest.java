package com.kakaopay.sec.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.kakaopay.sec.model.entity.Branch;
import com.kakaopay.sec.service.BranchService;
import com.kakaopay.sec.service.TransactionService;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class BranchControllerTest {

	@MockBean
	private BranchService branchService;
	
	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private TransactionService transactionService;
	
	@Test
	void testGetBranchAmount() throws Exception {

		String existBranchName = "판교점";
		
		Branch branch = Branch.builder()
				.brCode("1234")
				.brName(existBranchName)
				.build();
		
		Mockito.doReturn(Optional.of(branch))
			.when(this.branchService)
			.getByName(Mockito.eq(existBranchName));
		
        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/branches/" + existBranchName))
                .andExpect(status().isOk())
                .andReturn();     
	
	}

	@Test
	void testGetBranchAmountNotExistBranch() throws Exception {

		String notExistBranchName = "notExist";
		
		Mockito.doReturn(Optional.empty())
			.when(this.branchService)
			.getByName(Mockito.eq(notExistBranchName));
		
        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/branches/" + notExistBranchName))
                .andExpect(status().is(HttpStatus.NOT_FOUND.value()))
                .andReturn();     
	
	}
}
