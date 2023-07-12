package com.binance.platform.amazon.s3.config;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;

import com.amazonaws.auth.EC2ContainerCredentialsProviderWrapper;
import com.amazonaws.auth.WebIdentityTokenCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.binance.platform.amazon.s3.service.S3ObjectService;
import com.binance.platform.amazon.s3.service.impl.S3ObjectServiceImpl;
import com.binance.platform.amazon.s3.service.impl.S3ObjectWithSSEService;
import com.binance.platform.env.EnvUtil;

@Configuration
public class AmazonS3Config extends BaseAmazonConfig {
    private static Logger log = LoggerFactory.getLogger(AmazonS3Config.class);

    @Autowired
    private Environment environment;

    @Bean("AmazonS3")
    public AmazonS3 amazonS3() {
        setEnvironment(environment);
        String accessKey = getAccessKey();
        String secretKey = getSecretKey();
        String region = getRegion();
        AmazonS3ClientBuilder builder = AmazonS3ClientBuilder.standard().withRegion(region);
        if (StringUtils.isEmpty(accessKey) || StringUtils.isEmpty(secretKey)) {
            log.info("build AmazonS3 by role ");
            if (EnvUtil.isEcs()) {
                builder.withCredentials(new EC2ContainerCredentialsProviderWrapper());
            } else {
                builder.withCredentials(WebIdentityTokenCredentialsProvider.create());
            }
        } else {
            log.info("build AmazonS3 by accessKey and secretKey ");
            builder.withCredentials(getCredentialProvider());
        }
        return builder.build();
    }

    @Bean("S3ObjectService")
    @Primary
    public S3ObjectService s3ObjectService() {
        return new S3ObjectServiceImpl();
    }

    @Bean("S3ObjectWithSSEService")
    public S3ObjectService s3ObjectWithSSEService() {
        return new S3ObjectWithSSEService();
    }

    public String getBucketName() {
        return this.environment.getProperty("amazon.s3.bucket");
    }

}
