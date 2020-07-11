package com.kakaopay.sec.config;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource("classpath:configuration.properties")
public class PropertiesConfig {

	private static final String ROOT_PATH = "data";
	
	@Value("${database.account}")
	private String accountData;
	
	@Value("${database.branch}")
	private String branchData;
	
	@Value("${database.transaction}")
	private String transactionData;

	public String getAccountDataPath() {
		return FilenameUtils.concat(ROOT_PATH, this.accountData);
	}

	public String getBranchDataPath() {
		return FilenameUtils.concat(ROOT_PATH, this.branchData);
	}

	public String getTransactionDataPath() {
		return FilenameUtils.concat(ROOT_PATH, this.transactionData);
	}
}
