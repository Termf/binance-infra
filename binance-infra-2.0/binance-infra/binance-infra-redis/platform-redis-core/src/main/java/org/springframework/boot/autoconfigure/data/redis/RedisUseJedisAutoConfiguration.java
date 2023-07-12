package org.springframework.boot.autoconfigure.data.redis;

import java.net.UnknownHostException;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionOutcome;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.SpringBootCondition;
import org.springframework.boot.autoconfigure.data.redis.RedisUseJedisAutoConfiguration.RedisCondition;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.alibaba.fastjson.support.spring.GenericFastJsonRedisSerializer;
import com.alibaba.fastjson.util.IOUtils;
import com.binance.master.utils.RedisCacheUtils;

/**
 * 
 * 修改Spring Boot默认的redis客户端，使用jedis，而不使用lettuce
 * 
 * lettuce和jedis的话，在存在历史数据的情况下是不兼容的
 * 
 * 
 * @author liushiming
 *
 */
@Configuration
@Conditional(RedisCondition.class)
@ConditionalOnClass(RedisOperations.class)
@EnableConfigurationProperties(RedisProperties.class)
@Import({JedisConnectionConfiguration.class})
public class RedisUseJedisAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(name = "redisTemplate")
    public RedisTemplate<String, Object>
        redisTemplate(@Qualifier("redisConnectionFactory") RedisConnectionFactory redisConnectionFactory)
            throws UnknownHostException {
        RedisSerializer<Object> serializer = new CustomizeGenericFastJsonRedisSerializer();
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        RedisTemplate<String, Object> template = new RedisTemplate<String, Object>();
        template.setConnectionFactory(redisConnectionFactory);
        template.setValueSerializer(serializer);
        template.setHashValueSerializer(serializer);
        template.setKeySerializer(stringRedisSerializer);
        template.setDefaultSerializer(serializer);
        template.afterPropertiesSet();
        RedisCacheUtils.setRedisTemplate(template);
        return template;
    }

    @Bean
    @ConditionalOnMissingBean
    public StringRedisTemplate
        stringRedisTemplate(@Qualifier(value = "redisConnectionFactory") RedisConnectionFactory redisConnectionFactory)
            throws UnknownHostException {
        StringRedisTemplate template = new StringRedisTemplate();
        template.setConnectionFactory(redisConnectionFactory);
        return template;
    }

    public static class CustomizeGenericFastJsonRedisSerializer extends GenericFastJsonRedisSerializer {

        @Override
        public Object deserialize(byte[] bytes) throws SerializationException {
            Object object = null;
            try {
                object = super.deserialize(bytes);
            } catch (SerializationException e) {
                object = new String(bytes, IOUtils.UTF8);
            }
            return object;
        }
    }

    public static class RedisCondition extends SpringBootCondition {
        @Override
        public ConditionOutcome getMatchOutcome(final ConditionContext context, final AnnotatedTypeMetadata metadata) {
            try {
                Environment env = context.getEnvironment();
                String singleHost = env.getProperty("spring.redis.host");
                String singleUrl = env.getProperty("spring.redis.url");
                String cluster = env.getProperty("spring.redis.cluster.nodes");
                String sentinel = env.getProperty("spring.redis.sentinel.nodes");
                return new ConditionOutcome(!StringUtils.isAllBlank(singleHost, singleUrl, cluster, sentinel),
                    "redis not config");
            } catch (Exception e) {
                return new ConditionOutcome(false, "redis not config");
            }

        }
    }

}
