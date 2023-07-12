package com.binance.platform.openfeign.signature.support;

import java.util.Map;
import java.util.Properties;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionOutcome;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.SpringBootCondition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.core.type.AnnotatedTypeMetadata;

import com.binance.platform.apollo.SecretsManagerConstants;
import com.binance.platform.apollo.SecretsManagerService;
import com.binance.platform.openfeign.signature.SignerProvider;
import com.binance.platform.openfeign.signature.VerifierProvider;
import com.binance.platform.openfeign.signature.support.PrivatePublicKeyConfiguration.IntraPrivatePublicCondition;
import com.google.common.collect.Maps;

@Configuration
@ConditionalOnProperty(name = "com.binance.intra.security.switch", havingValue = "true", matchIfMissing = false)
@Conditional(IntraPrivatePublicCondition.class)
public class PrivatePublicKeyConfiguration {

    private static final Logger LOGGER = LoggerFactory.getLogger(PrivatePublicKeyConfiguration.class);

    static class IntraPrivatePublicCondition extends SpringBootCondition {

        @Override
        public ConditionOutcome getMatchOutcome(ConditionContext context, AnnotatedTypeMetadata metadata) {
            SecretsManagerService secretsManagerService = null;
            try {
                secretsManagerService = context.getBeanFactory().getBean(SecretsManagerService.class);
            } catch (Throwable e) {
                // igore
            }
            if (secretsManagerService == null) {
                return new ConditionOutcome(false, "spring.cloud.intranet.safe");
            } else {
                String secretName =
                    secretsManagerService.getProperty(SecretsManagerConstants.AWS_INTRA_SECURITY_SECRET_NAME);
                if (secretName != null) {
                    return new ConditionOutcome(true, "spring.cloud.intranet.safe");
                } else {
                    return new ConditionOutcome(false, "spring.cloud.intranet.safe");
                }

            }

        }

    }

    @Autowired
    private SecretsManagerService secretsManagerService;

    @Autowired
    private Environment enviroment;

    // binance-mgs.intra-security.public-key
    // binance-mgs.intra-security.private-key
    private static Map<String, Resource> NOSHARE_PRIVATEKEY = Maps.newConcurrentMap();

    private static Map<String, Resource> NOSHARE_PUBLICKEY = Maps.newConcurrentMap();

    private boolean isShareKey = false;

    private Resource sharePrivateKey;

    private Resource sharePublicKey;

    private void noShare(Properties properties) {
        properties.forEach((k, v) -> {
            String key = (String)k;
            Resource reousrce = new ByteArrayResource(((String)v).getBytes());
            String[] keys = StringUtils.split(key, ".");
            if (keys.length == 3) {
                if (keys[2].equalsIgnoreCase("public-key")) {
                    NOSHARE_PUBLICKEY.put(keys[0], reousrce);
                }
                if (keys[2].equalsIgnoreCase("private-key")) {
                    NOSHARE_PRIVATEKEY.put(keys[0], reousrce);
                }

            }

        });
    }

    private void share(Properties properties) {
        this.sharePublicKey = new ByteArrayResource(properties.getProperty("intra-security.public-key").getBytes());
        this.sharePrivateKey = new ByteArrayResource(properties.getProperty("intra-security.private-key").getBytes());
    }

    @PostConstruct
    public void init() {
        // 从SecretsManager中获取值并缓存起来
        String secretName = secretsManagerService.getProperty(SecretsManagerConstants.AWS_INTRA_SECURITY_SECRET_NAME);
        PropertySource<?> propertySource = secretsManagerService.fetchSecretValueByName(secretName);
        if (propertySource == null) {
            LOGGER.warn(
                "can not load private key and public key from secretsmanager,please config private key and public key in secretsManager");
            return;
        }
        Object source = propertySource.getSource();
        if (source instanceof Properties) {
            Properties properties = (Properties)source;
            // 公共的privatekey和publickey
            if (properties.size() == 2 && properties.containsKey("intra-security.public-key")
                && properties.containsKey("intra-security.private-key")) {
                this.isShareKey = true;
                this.share(properties);
            } // 私有
            else {
                this.noShare(properties);
            }
        }
    }

    @Conditional(IntraPrivatePublicCondition.class)
    @Bean
    public SignerProvider signerProvider() {

        return new SignerProvider() {

            @Override
            public Map<String, Resource> isolatePrivateKey() {
                return NOSHARE_PRIVATEKEY;
            }

            @Override
            public Resource shareprivateKey() {
                return sharePrivateKey;
            }

        };

    }

    @Conditional(IntraPrivatePublicCondition.class)
    @Bean
    public VerifierProvider verifierProvider() {

        return new VerifierProvider() {

            @Override
            public Resource publicKey() {
                if (isShareKey) {
                    return sharePublicKey;
                } else {
                    String appName = enviroment.getProperty("spring.application.name");
                    return NOSHARE_PUBLICKEY.get(appName);
                }

            }

        };

    }

}
