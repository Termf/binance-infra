package com.binance.platform.redis;

import static org.assertj.core.api.Assertions.assertThat;

import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.support.GenericApplicationContext;

import com.alicp.jetcache.anno.support.GlobalCacheConfig;
import com.alicp.jetcache.anno.support.SpringConfigProvider;
import com.alicp.jetcache.embedded.EmbeddedCacheBuilder;
import com.alicp.jetcache.embedded.EmbeddedCacheConfig;
import com.alicp.jetcache.external.ExternalCacheBuilder;
import com.alicp.jetcache.external.ExternalCacheConfig;
import com.binance.platform.TestContextUtils;
import com.binance.platform.redis.cache.JetCacheAutoConfiguration;
import com.binance.platform.redis.cache.LocalCacheAutoConfiguration;

public class BasicJetCacheAutoConfigurationTest {

	@Test
	public void jetCacheNotEnabled() {
		GenericApplicationContext context = TestContextUtils.load(EmptyConfiguration.class);
		assertThat(context.getBeansOfType(SpringConfigProvider.class)).isEmpty();
	}

	@Test
	public void jetCacheDisabled() {
		GenericApplicationContext context = TestContextUtils.load(EmptyConfiguration.class, "jetcache.enable:false");
		assertThat(context.getBeansOfType(SpringConfigProvider.class)).isEmpty();
	}

	@Test
	public void jetCacheEnabledWithDefaultSetting() {
		GenericApplicationContext context = contextWithJetCache("spring.application.name:test-app", "jetcache.enable:true");
		assertThat(context.getBeansOfType(SpringConfigProvider.class)).isNotEmpty();
		assertThat(context.getBeansOfType(EmbeddedCacheBuilder.class)).isNotEmpty();
		EmbeddedCacheConfig embeddedConf = context.getBean(EmbeddedCacheBuilder.class).getConfig();
		assertThat(embeddedConf.getLimit()).isEqualTo(1000);
		assertThat(embeddedConf.getExpireAfterWriteInMillis()).isEqualTo(30000);
		assertThat(embeddedConf.getExpireAfterAccessInMillis()).isEqualTo(0);

		assertThat(context.getBeansOfType(ExternalCacheBuilder.class)).isNotEmpty();
		ExternalCacheConfig externalConf = context.getBean(ExternalCacheBuilder.class).getConfig();
		assertThat(externalConf.getExpireAfterWriteInMillis()).isEqualTo(30000);
		assertThat(externalConf.getExpireAfterAccessInMillis()).isEqualTo(0);
		assertThat(externalConf.getKeyPrefix()).isEqualTo("jetcache.test-app.");

		GlobalCacheConfig globalCacheConfig = context.getBean(GlobalCacheConfig.class);
		assertThat(globalCacheConfig.isEnableMethodCache()).isTrue();
		assertThat(globalCacheConfig).isNotNull();
		assertThat(context.getBeansOfType(CacheManager.class)).isNotEmpty();
	}

	@Test
	public void jetCacheEnabledWithConfig() {
		GenericApplicationContext context = contextWithJetCache(
				"spring.application.name:test-app",
				"jetcache.enable:true",
				"jetcache.local.default.limit:500",

				"jetcache.local.default.expireAfterWriteInMillis:20000",
				"jetcache.local.default.expireAfterAccessInMillis:30000",

				"jetcache.remote.default.expireAfterWriteInMillis:50000",
				"jetcache.remote.default.expireAfterAccessInMillis:60000"
		);
		EmbeddedCacheConfig embeddedConf = context.getBean(EmbeddedCacheBuilder.class).getConfig();
		assertThat(embeddedConf.getLimit()).isEqualTo(500);
		assertThat(embeddedConf.getExpireAfterWriteInMillis()).isEqualTo(20000);
		assertThat(embeddedConf.getExpireAfterAccessInMillis()).isEqualTo(30000);

		ExternalCacheConfig externalConf = context.getBean(ExternalCacheBuilder.class).getConfig();
		assertThat(externalConf.getExpireAfterWriteInMillis()).isEqualTo(50000);
		assertThat(externalConf.getExpireAfterAccessInMillis()).isEqualTo(60000);
	}

	@NotNull
	private GenericApplicationContext contextWithJetCache(String ... properties) {
		return TestContextUtils.load(EmptyConfiguration.class, properties);
	}

	@Configuration
	@Import({ JetCacheAutoConfiguration.class, LocalCacheAutoConfiguration.class, RedisAutoConfiguration.class })
	static class EmptyConfiguration {
	}

}