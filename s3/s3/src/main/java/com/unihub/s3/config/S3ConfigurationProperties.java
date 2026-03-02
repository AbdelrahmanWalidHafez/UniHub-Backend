package com.unihub.s3.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "aws")
public record S3ConfigurationProperties(
        String bucketname,
        String region,
        String accesskey,
        String secretkey
) { }
