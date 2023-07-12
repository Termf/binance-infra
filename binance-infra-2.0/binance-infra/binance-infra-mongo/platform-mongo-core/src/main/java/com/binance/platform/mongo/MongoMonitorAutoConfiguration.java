package com.binance.platform.mongo;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;
import org.springframework.core.type.AnnotatedTypeMetadata;

@Configuration
@Import(value = {MongoTemplateInterceptor.class})
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class MongoMonitorAutoConfiguration {

    // @Bean
    // @ConditionalOnClass(name = {"com.mongodb.MongoClientSettings"})
    // public MongoDataSourceBeanPostProcessor mongoDataSourceBeanPostProcessor() {
    // return new MongoDataSourceBeanPostProcessor();
    // }


    @Bean
    @Conditional(MongoTemplateConditon.class)
    public MongoPreferenceProcessor MongoPreferenceProcessor() {
        return new MongoPreferenceProcessor();
    }

    public static class MongoTemplateConditon implements Condition {
        @Override
        public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
            return isPresent("org.springframework.data.mongodb.core.MongoTemplate");
        }
    }

    private static boolean isPresent(String className) {
        try {
            Class.forName(className);
            return true;
        } catch (Throwable ex) {
            return false;
        }
    }
}
