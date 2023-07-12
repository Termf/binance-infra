package com.binance.platform.amazon.s3.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import com.amazonaws.auth.EC2ContainerCredentialsProviderWrapper;
import com.amazonaws.auth.WebIdentityTokenCredentialsProvider;
import com.amazonaws.services.rekognition.AmazonRekognition;
import com.amazonaws.services.rekognition.AmazonRekognitionClientBuilder;
import com.binance.platform.amazon.s3.service.RekongnitionService;
import com.binance.platform.amazon.s3.service.impl.RekongnitionServiceImpl;
import com.binance.platform.env.EnvUtil;

@Configuration
@ConditionalOnClass(AmazonRekognition.class)
public class AmazonRekognitionConfig extends BaseAmazonConfig {

    @Autowired
    private Environment environment;

    @Bean
    public AmazonRekognition amazonRekognition() {
        setEnvironment(environment);
        String accessKey = getAccessKey();
        String secretKey = getSecretKey();
        String region = getRegion();
        AmazonRekognitionClientBuilder builder = AmazonRekognitionClientBuilder.standard().withRegion(region);
        if (org.apache.commons.lang3.StringUtils.isEmpty(accessKey)
            || org.apache.commons.lang3.StringUtils.isEmpty(secretKey)) {
            if (EnvUtil.isEcs()) {
                builder.withCredentials(new EC2ContainerCredentialsProviderWrapper());
            } else {
                builder.withCredentials(WebIdentityTokenCredentialsProvider.create());
            }
        } else {
            builder.withCredentials(getCredentialProvider());
        }
        return builder.build();
    }

    @Bean
    public RekongnitionService rekongnitionService(AmazonRekognition amazonRekognition) {
        return new RekongnitionServiceImpl(amazonRekognition);
    }
}
