package com.unihub.auth.accountmanagement.job.config;

import com.unihub.auth.accountmanagement.job.dto.UserCsvDto;
import com.unihub.auth.accountmanagement.job.processor.UserProcessor;
import com.unihub.auth.accountmanagement.strategy.UserRoles;
import com.unihub.auth.security.model.Gender;
import com.unihub.auth.security.model.User;
import com.unihub.auth.security.repository.RoleRepository;
import com.unihub.auth.security.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.Step;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.infrastructure.item.data.RepositoryItemWriter;
import org.springframework.batch.infrastructure.item.file.FlatFileItemReader;
import org.springframework.batch.infrastructure.item.file.LineMapper;
import org.springframework.batch.infrastructure.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.infrastructure.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.transaction.PlatformTransactionManager;

import java.time.LocalDate;
import java.util.UUID;

@Configuration
@RequiredArgsConstructor
public class BatchConfig {

    private final JobRepository jobRepository;

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final PlatformTransactionManager transactionManager;


    @Bean
    @StepScope
    public FlatFileItemReader<UserCsvDto> itemReader( @Value("#{jobParameters['csvFilePath']}") String csvFilePath) {
        FlatFileItemReader<UserCsvDto> reader = new FlatFileItemReader<>(lineMapper());
        reader.setName("csvReader");
        reader.setLinesToSkip(1);
        reader.setResource(new FileSystemResource(csvFilePath));
        return reader;
    }

    @Bean
    @StepScope
    public UserProcessor processor(@Value("#{jobParameters['tid']}") String tid) {
        return new UserProcessor(UUID.fromString(tid), roleRepository);
    }

    @Bean
    public RepositoryItemWriter<User> writer() {
        RepositoryItemWriter<User> writer = new RepositoryItemWriter<>(userRepository);
        writer.setMethodName("save");
        return writer;
    }

    @Bean
    public Step importStep() {
        return new StepBuilder("csvImport", jobRepository)
                .<UserCsvDto, User>chunk(1000)
                .reader(itemReader(null))
                .processor(processor(null))
                .writer(writer())
                .faultTolerant()
                .skip(IllegalArgumentException.class)
                .skip(DataIntegrityViolationException.class)
                .skip(NumberFormatException.class)
                .skipLimit(Integer.MAX_VALUE)
                .transactionManager(transactionManager)
                .build();
    }

    @Bean
    public Job runJob(Step importStep) {
        return new JobBuilder("importUsers", jobRepository)
                .start(importStep)
                .build();
    }

    private LineMapper<UserCsvDto> lineMapper() {
        DefaultLineMapper<UserCsvDto> lineMapper = new DefaultLineMapper<>();
        DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
        tokenizer.setDelimiter(",");
        tokenizer.setStrict(false);
        tokenizer.setNames("email", "firstName", "lastName", "dob", "gender", "roleName", "cid", "gpa");
        lineMapper.setFieldSetMapper(fieldSet -> {
            UserCsvDto dto = new UserCsvDto();
            dto.setEmail(fieldSet.readString("email"));
            dto.setFirstName(fieldSet.readString("firstName"));
            dto.setLastName(fieldSet.readString("lastName"));
            String dobStr = fieldSet.readString("dob");
            if (dobStr != null && !dobStr.isEmpty()) {
                dto.setDob(LocalDate.parse(dobStr));
         }
            String genderStr = fieldSet.readString("gender");
            if (genderStr != null && !genderStr.isEmpty()) {
                dto.setGender(Gender.valueOf(genderStr.trim().toUpperCase()));
            }
            String roleStr = fieldSet.readString("roleName");
            if (roleStr != null && !roleStr.isEmpty()) {
                dto.setRoleName(UserRoles.valueOf(roleStr.trim().toUpperCase()));
            }

            String cidStr = fieldSet.readString("cid");
            if (cidStr != null && !cidStr.isEmpty()) {
                dto.setCid(UUID.fromString(cidStr));
            }

            String gpaStr = fieldSet.readString("gpa");
            if (gpaStr != null && !gpaStr.isEmpty()) {
                dto.setGpa(Double.parseDouble(gpaStr));
            }

            return dto;
        });
        lineMapper.setLineTokenizer(tokenizer);
        return lineMapper;
    }
}