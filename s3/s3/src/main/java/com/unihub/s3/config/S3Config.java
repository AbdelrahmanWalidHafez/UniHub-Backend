package com.unihub.s3.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

@Configuration
@RequiredArgsConstructor
public class S3Config {

    private final S3ConfigurationProperties s3ConfigurationProperties;

    @Bean
    public S3Client s3Client() {
        AwsBasicCredentials awsBasicCredentials=AwsBasicCredentials.create(s3ConfigurationProperties.accesskey(),s3ConfigurationProperties.secretkey());
        return S3Client.builder()
                .region(Region.of(s3ConfigurationProperties.region()))
                .credentialsProvider(StaticCredentialsProvider.create(awsBasicCredentials))
                .build();
    }
}