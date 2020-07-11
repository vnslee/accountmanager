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
import com.kakaopay.sec.config.mapper.TransactionFieldSetterMapper;
import com.kakaopay.sec.config.processor.TransactionProcessor;
import com.kakaopay.sec.config.writer.TransactionWriter;
import com.kakaopay.sec.model.entity.Transaction;

/**
 * 거래 내역 정보 배치 등록을 위한 설정 클래스
 */
@Configuration
@EnableBatchProcessing
public class TransactionBatchConfiguration {

	private static final int CHUNK_SIZE = 20;

	private static final String DELIMITER = ",";

	// @EnableBatchProcessing enables Spring Batch features and provides a base
	// configuration for setting up batch jobs in an @Configuration class.
	@Autowired
	public JobBuilderFactory jobBuilderFactory;

	@Autowired
	private PropertiesConfig propertiesConfig;

	@Autowired
	public StepBuilderFactory stepBuilderFactory;
	
	@Bean
    public Step importTransationStep() {
        
		return stepBuilderFactory.get("importTransactions")
                .<Transaction, Transaction> chunk(CHUNK_SIZE)
                .reader(this.transactionReader())
                .processor(this.transactionProcessor())
                .writer(this.transactionWriter())
                .build();
    }

	@Bean
    public Job transactionItemWriterJob() {
        return jobBuilderFactory.get("importtransactionJob")
                .start(this.importTransationStep())
                .build();
	}

	@Bean
	public LineMapper<Transaction> transactionLineMapper() {
		final DefaultLineMapper<Transaction> defaultLineMapper = new DefaultLineMapper<>();
		final DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();

		lineTokenizer.setDelimiter(DELIMITER);
		lineTokenizer.setStrict(false);
		lineTokenizer.setNames(new String[] {  TransactionFieldSetterMapper.DATE,
				TransactionFieldSetterMapper.ACCT_NO,
				TransactionFieldSetterMapper.TRANSACTION_NO,
				TransactionFieldSetterMapper.AMOUNT,
				TransactionFieldSetterMapper.FEE,
				TransactionFieldSetterMapper.CANCELD
		});

		final TransactionFieldSetterMapper fieldSetMapper = new TransactionFieldSetterMapper();
		defaultLineMapper.setLineTokenizer(lineTokenizer);
		defaultLineMapper.setFieldSetMapper(fieldSetMapper);

		return defaultLineMapper;
	}

	@Bean
	public TransactionProcessor transactionProcessor() {

		return new TransactionProcessor();
	}

	@Bean
	public FlatFileItemReader<Transaction> transactionReader() {
		return new FlatFileItemReaderBuilder<Transaction>()
				.name("accountItemReader")
				.resource(
						// 리소스 폴더 내 거래 내역 파일을 설정
						new ClassPathResource(this.propertiesConfig.getTransactionDataPath()))
				.delimited()
				.names(new String[] { TransactionFieldSetterMapper.DATE,
						TransactionFieldSetterMapper.ACCT_NO,
						TransactionFieldSetterMapper.TRANSACTION_NO,
						TransactionFieldSetterMapper.AMOUNT,
						TransactionFieldSetterMapper.FEE,
						TransactionFieldSetterMapper.CANCELD
						})
				.lineMapper(this.transactionLineMapper())
				.linesToSkip(1)
				.fieldSetMapper(new BeanWrapperFieldSetMapper<Transaction>() {
					{
						setTargetType(Transaction.class);
					}
				}).build();
	}
	
	@Bean
	public TransactionWriter transactionWriter() {
		
		return new TransactionWriter();
	}

}
