package com.kakaopay.sec.config.batch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import com.kakaopay.sec.config.PropertiesConfig;
import com.kakaopay.sec.config.mapper.AccountFieldSetMapper;
import com.kakaopay.sec.config.processor.AccountProcessor;
import com.kakaopay.sec.config.writer.AccountWriter;
import com.kakaopay.sec.model.entity.Account;

/**
 * 계좌 정보 배치 등록을 위한 설정 클래스
 */
@Configuration
@EnableBatchProcessing
public class AccountBatchConfiguration {

	private static final int CHUNK_SIZE = 20;

	private static final String DELIMITER = ",";

	@Autowired
	public JobBuilderFactory jobBuilderFactory;

	@Autowired
	private PropertiesConfig propertiesConfig;

	@Autowired
	public StepBuilderFactory stepBuilderFactory;
	
	@Bean
    public Job accountItemWriterJob() {
        return jobBuilderFactory.get("importAccountJob")
                .start(this.importAccountStep())
                .build();
	}

	@Bean
	public LineMapper<Account> accountLineMapper() {
		final DefaultLineMapper<Account> defaultLineMapper = new DefaultLineMapper<>();
		final DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();

		lineTokenizer.setDelimiter(DELIMITER);
		lineTokenizer.setStrict(false);
		lineTokenizer.setNames(new String[] { AccountFieldSetMapper.ACCT_NO,
				AccountFieldSetMapper.NAME,
				AccountFieldSetMapper.BR_CODE });

		final AccountFieldSetMapper fieldSetMapper = new AccountFieldSetMapper();
		defaultLineMapper.setLineTokenizer(lineTokenizer);
		defaultLineMapper.setFieldSetMapper(fieldSetMapper);

		return defaultLineMapper;
	}

	@Bean
	public AccountProcessor accountProcessor() {

		return new AccountProcessor();
	}

	@Bean
	public FlatFileItemReader<Account> accountReader() {
		return new FlatFileItemReaderBuilder<Account>()
				.name("accountItemReader")
				.resource(
						new ClassPathResource(this.propertiesConfig.getAccountDataPath()))
				.delimited()
				.names(new String[] { AccountFieldSetMapper.ACCT_NO,
						AccountFieldSetMapper.NAME,
						AccountFieldSetMapper.BR_CODE })
				.lineMapper(this.accountLineMapper())
				.linesToSkip(1)
				.fieldSetMapper(new BeanWrapperFieldSetMapper<Account>() {
					{
						setTargetType(Account.class);
					}
				}).build();
	}

	@Bean
	public AccountWriter accountWriter() {
		
		return new AccountWriter();
	}
	
	@Bean
    public Step importAccountStep() {
        
		return stepBuilderFactory.get("step1")
                .<Account, Account> chunk(CHUNK_SIZE)
                .reader(this.accountReader())
                .processor(this.accountProcessor())
                .writer(this.accountWriter())
                .build();
    }

}
