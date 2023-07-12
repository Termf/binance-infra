package com.binance.platform.amazon.s3.config;

import org.springframework.core.env.Environment;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;

public class BaseAmazonConfig {

    private Environment env;

    protected void setEnvironment(Environment environment) {
        this.env = environment;
    }

    protected String getAccessKey() {
        return this.env.getProperty("com.binance.aws.accesskey");
    }

    protected String getSecretKey() {
        return this.env.getProperty("com.binance.aws.secretkey");
    }

    protected String getRegion() {
        return this.env.getProperty("com.binance.aws.region");
    }

    protected AWSCredentialsProvider getCredentialProvider() {
        return new AWSStaticCredentialsProvider(new BasicAWSCredentials(getAccessKey(), getSecretKey()));
    }
}
