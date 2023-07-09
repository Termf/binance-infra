package com.binance.platform.apollo;

import static com.binance.platform.apollo.SecretsManagerConstants.AWS_ACCESSKEY;
import static com.binance.platform.apollo.SecretsManagerConstants.AWS_REGION;
import static com.binance.platform.apollo.SecretsManagerConstants.AWS_SECRETKEY;
import static com.binance.platform.apollo.SecretsManagerConstants.AWS_SECRETNAME;
import static com.binance.platform.apollo.SecretsManagerConstants.SECRETSMANAGER_PROPERTY_SOURCE_NAME;

import java.util.Properties;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.CompositePropertySource;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.env.PropertySource;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.EC2ContainerCredentialsProviderWrapper;
import com.amazonaws.auth.WebIdentityTokenCredentialsProvider;
import com.amazonaws.services.secretsmanager.AWSSecretsManager;
import com.amazonaws.services.secretsmanager.AWSSecretsManagerClientBuilder;
import com.amazonaws.services.secretsmanager.model.GetSecretValueRequest;
import com.binance.platform.env.EnvUtil;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class SecretsManagerService {
    private static final Logger LOGGER = LoggerFactory.getLogger(SecretsManagerContextInitializer.class);

    private AWSSecretsManager secretsManager = null;
    private final ConfigurableEnvironment environment;

    public SecretsManagerService(ConfigurableEnvironment environment) {
        this.environment = environment;
        String accessKey = getProperty(AWS_ACCESSKEY);
        String secretKey = getProperty(AWS_SECRETKEY);
        String region = getProperty(AWS_REGION);
        if (region != null) {
            if (accessKey != null && secretKey != null) {
                LOGGER.info("build AWSSecretsManager by accessKey and secretKey");
                AWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);
                this.secretsManager = AWSSecretsManagerClientBuilder.standard().withRegion(region)
                    .withCredentials(new AWSStaticCredentialsProvider(credentials)).build();
            } else {
                LOGGER.info("build AWSSecretsManager by role ");
                try {
                    if (EnvUtil.isEcs()) {
                        this.secretsManager = AWSSecretsManagerClientBuilder.standard().withRegion(region)
                            .withCredentials(new EC2ContainerCredentialsProviderWrapper()).build();
                    } else if (EnvUtil.isK8s()) {
                        this.secretsManager = AWSSecretsManagerClientBuilder.standard().withRegion(region)
                            .withCredentials(WebIdentityTokenCredentialsProvider.create()).build();
                    } else {
                        this.secretsManager = AWSSecretsManagerClientBuilder.standard().withRegion(region)
                            .withCredentials(new EC2ContainerCredentialsProviderWrapper()).build();
                    }
                } catch (SecurityException e) {
                    LOGGER.error(
                        "Security manager did not allow access to the ECS credentials environment variable, please contact OPS to assign Role ");

                }
            }
        }
    }

    public String getProperty(String key) {
        return environment.getProperty(key);
    }

    public boolean isExistSecretsManager() {
        return secretsManager != null ? true : false;
    }

    public AWSSecretsManager getSecretsManager() {
        return secretsManager;
    }

    public CompositePropertySource fetchAll() {
        PropertySource<?> secretValuePropertySource = fetchSecretValueByName(getProperty(AWS_SECRETNAME));
        if (secretValuePropertySource != null) {
            CompositePropertySource composite = new CompositePropertySource(SECRETSMANAGER_PROPERTY_SOURCE_NAME);
            composite.addPropertySource(secretValuePropertySource);
            return composite;
        } else {
            return null;
        }
    }

    public PropertySource<?> fetchSecretValueByName(String secretName) {
        if (StringUtils.isBlank(secretName)) {
            LOGGER.warn("secretName is blank");
            return null;
        } else {
            CompositePropertySource propertySource = new CompositePropertySource(secretName);
            String[] secretNameArray = secretName.split(",");
            if (secretNameArray.length > 1) {
                for (String name : secretNameArray) {
                    PropertySource<?> subPropertySource = fetchSecretValueByName(name.trim());
                    if (subPropertySource != null) {
                        propertySource.addPropertySource(subPropertySource);
                    }
                }
                return propertySource;
            }
        }

        try {
            Properties secretProperties = new Properties();
            GetSecretValueRequest getSecretValueRequest =
                new GetSecretValueRequest().withSecretId(secretName).withVersionStage("AWSCURRENT");
            String secretValue = secretsManager.getSecretValue(getSecretValueRequest).getSecretString();
            Properties secretValueProp = null;
            try {
                // 如果是简单的json数据结构
                secretValueProp = new Gson().fromJson(secretValue, Properties.class);
            } catch (Throwable e) {
                // 如果是复杂的json数据结构，只取外层就好
                secretValueProp = new Properties();
                secretValue = StringEscapeUtils.unescapeJava(secretValue);
                JsonObject jsonObject = new JsonParser().parse(secretValue).getAsJsonObject();
                for (String key : jsonObject.keySet()) {
                    secretValueProp.put(key, jsonObject.get(key).toString());
                }
            }
            secretProperties.putAll(secretValueProp);
            PropertiesPropertySource mapPropertySource = new PropertiesPropertySource(secretName, secretProperties);
            return mapPropertySource;
        } catch (Throwable e) {
            LOGGER.warn(String.format("fetch secret value error, secretName is: %s", secretName), e);
            return null;
        }

    }

}
