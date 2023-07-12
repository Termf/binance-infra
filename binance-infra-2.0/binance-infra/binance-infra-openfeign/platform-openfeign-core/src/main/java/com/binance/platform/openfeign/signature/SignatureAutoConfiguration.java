package com.binance.platform.openfeign.signature;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionOutcome;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.condition.SpringBootCondition;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.binance.platform.openfeign.signature.rsa.RSASigner;
import com.binance.platform.openfeign.signature.rsa.RSAVerifier;
import com.binance.platform.openfeign.signature.support.PrivatePublicKeyConfiguration;

@Configuration
@Import(PrivatePublicKeyConfiguration.class)
public class SignatureAutoConfiguration {

    static class FeignClientCondition extends SpringBootCondition {
        @Override
        public ConditionOutcome getMatchOutcome(final ConditionContext context, final AnnotatedTypeMetadata metadata) {
            try {
                Map<String, Object> feignClients =
                    context.getBeanFactory().getBeansWithAnnotation(EnableFeignClients.class);
                return new ConditionOutcome(feignClients != null && !feignClients.isEmpty(),
                    "spring.cloud.feignclients");
            } catch (Exception e) {
                return new ConditionOutcome(false, "spring.cloud.feignclients");
            }

        }
    }

    @Configuration
    @Conditional(FeignClientCondition.class)
    @ConditionalOnProperty(name = "com.binance.intra.security.switch", havingValue = "true", matchIfMissing = false)
    @ConditionalOnBean(SignerProvider.class)
    static class ConsumerSupportConfig {

        @Bean
        public RSASigner rsaSigner(SignerProvider signerProvider, Environment env) {
            RSASigner rasSigner = new RSASigner(signerProvider, env);
            return rasSigner;
        }

        @Bean
        public SignatureInterceptor signatureInterceptor(RSASigner rsaSigner) {
            return new SignatureInterceptor(rsaSigner);
        }

    }

    @Configuration
    @ConditionalOnBean(VerifierProvider.class)
    @ConditionalOnProperty(name = "com.binance.intra.security.switch", havingValue = "true", matchIfMissing = false)
    @ConditionalOnWebApplication
    static class ProviderSupportConfig extends WebMvcConfigurerAdapter {

        @Value("${server.error.path:${error.path:/error}}")
        private String errorPath;

        @Autowired
        private VerifierProvider verifierProvider;

        @Autowired
        private Environment env;

        @Override
        public void addInterceptors(InterceptorRegistry registry) {
            registry.addInterceptor(new SignatureInterceptor(new RSAVerifier(verifierProvider, env)))//
                .addPathPatterns("/**").excludePathPatterns("/**" + errorPath);
        }

    }
}
