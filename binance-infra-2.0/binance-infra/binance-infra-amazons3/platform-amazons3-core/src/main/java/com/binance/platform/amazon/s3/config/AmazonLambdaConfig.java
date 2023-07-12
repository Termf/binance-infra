package com.binance.platform.amazon.s3.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.EC2ContainerCredentialsProviderWrapper;
import com.amazonaws.auth.WebIdentityTokenCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.lambda.AWSLambda;
import com.amazonaws.services.lambda.AWSLambdaClientBuilder;
import com.binance.platform.amazon.s3.service.LambdaService;
import com.binance.platform.amazon.s3.service.impl.LambdaServiceImpl;
import com.binance.platform.env.EnvUtil;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

@Configuration
@ConditionalOnClass(AWSLambda.class)
@Log4j2
public class AmazonLambdaConfig {

    @Value("${amazon.lambda.region:}")
    private String region;

    @Value("${amazon.lambda.accesskey:}")
    private String accessKey;

    @Value("${amazon.lambda.secretkey:}")
    private String secretKey;

    @Bean("awsLambda")
    public AWSLambda awsLambda() {
        if (StringUtils.isEmpty(region)) {
            return null;
        }
        // (2) Instantiate AWSLambdaClientBuilder to build the Lambda client
        AWSLambdaClientBuilder builder = AWSLambdaClientBuilder.standard().withRegion(Regions.fromName(region));
        if (StringUtils.isEmpty(accessKey) || StringUtils.isEmpty(secretKey)) {
            log.info("accessKey或secretKey为空, EC2ContainerCredentialsProviderWrapper");
            if (EnvUtil.isEcs()) {
                builder.withCredentials(new EC2ContainerCredentialsProviderWrapper());
            } else {
                builder.withCredentials(WebIdentityTokenCredentialsProvider.create());
            }
        } else {
            log.info("accessKey和secretKe不为空, 使用AWSStaticCredentialsProvider认证");
            builder.withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(accessKey, secretKey)));
        }

        // (3) Build the client, which will ultimately invoke the function
        AWSLambda client = builder.build();
        return client;
    }

    @Bean
    public LambdaService lambdaService() {
        return new LambdaServiceImpl(awsLambda());
    }
}
