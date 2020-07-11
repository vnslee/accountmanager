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
import com.kakaopay.sec.config.mapper.BranchFieldSetterMapper;
import com.kakaopay.sec.config.processor.BranchProcessor;
import com.kakaopay.sec.config.writer.BranchWriter;
import com.kakaopay.sec.model.entity.Branch;

/**
 * 관리점 정보 배치 등록을 위한 설정 클래스
 */
@Configuration
@EnableBatchProcessing
public class BranchBatchConfiguration {
	
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
    public Job branchItemWriterJob() {
        return jobBuilderFactory.get("importBranchJob")
                .start(this.importBranchStep())
                .build();
	}

	@Bean
	public LineMapper<Branch> branchLineMapper() {
		final DefaultLineMapper<Branch> defaultLineMapper = new DefaultLineMapper<>();
		final DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();

		lineTokenizer.setDelimiter(DELIMITER);
		lineTokenizer.setStrict(false);
		lineTokenizer.setNames(new String[] { BranchFieldSetterMapper.BR_CODE, BranchFieldSetterMapper.BR_NAME});

		final BranchFieldSetterMapper fieldSetMapper = new BranchFieldSetterMapper();
		defaultLineMapper.setLineTokenizer(lineTokenizer);
		defaultLineMapper.setFieldSetMapper(fieldSetMapper);

		return defaultLineMapper;
	}

	@Bean
	public BranchProcessor branchProcessor() {

		return new BranchProcessor();
	}

	@Bean
	public FlatFileItemReader<Branch> branchReader() {
		return new FlatFileItemReaderBuilder<Branch>()
				.name("accountItemReader")
				.resource(
						new ClassPathResource(this.propertiesConfig.getBranchDataPath()))
				.delimited()
				.names(new String[] { BranchFieldSetterMapper.BR_CODE,
						BranchFieldSetterMapper.BR_NAME})
				.lineMapper(this.branchLineMapper())
				.linesToSkip(1)
				.fieldSetMapper(new BeanWrapperFieldSetMapper<Branch>() {
					{
						setTargetType(Branch.class);
					}
				}).build();
	}

	@Bean
	public BranchWriter branchWriter() {
		
		return new BranchWriter();
	}
	
	@Bean
    public Step importBranchStep() {
        
		return stepBuilderFactory.get("importBranch")
                .<Branch, Branch> chunk(CHUNK_SIZE)
                .reader(this.branchReader())
                .processor(this.branchProcessor())
                .writer(this.branchWriter())
                .build();
    }

}
