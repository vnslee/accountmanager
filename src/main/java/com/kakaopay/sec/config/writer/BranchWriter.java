package com.kakaopay.sec.config.writer;

import java.util.List;

import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;

import com.kakaopay.sec.model.entity.Branch;
import com.kakaopay.sec.repository.BranchRepository;

public class BranchWriter implements ItemWriter<Branch>{

	@Autowired
	private BranchRepository branchRepository;
	
	@Override
	public void write(List<? extends Branch> items) throws Exception {

		this.branchRepository.saveAll(items);
	}

}
