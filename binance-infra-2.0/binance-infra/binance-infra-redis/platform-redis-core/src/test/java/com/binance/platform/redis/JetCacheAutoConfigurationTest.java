package com.binance.platform.redis;


import static com.binance.platform.AnyUtils.anyInt;
import static com.binance.platform.AnyUtils.anyLong;
import static com.binance.platform.AnyUtils.anyString;
import static org.assertj.core.api.Assertions.assertThat;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.alicp.jetcache.CacheValueHolder;
import com.alicp.jetcache.support.JavaValueDecoder;
import com.alicp.jetcache.support.JavaValueEncoder;
import com.binance.platform.redis.cache.JetCacheAutoConfiguration;
import com.binance.platform.redis.cache.LocalCacheAutoConfiguration;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.data.redis.RedisUseJedisAutoConfiguration;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.support.GenericApplicationContext;

import com.binance.platform.AnyUtils;
import com.binance.platform.TestContextUtils;

import lombok.Data;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
public class JetCacheAutoConfigurationTest {

	@Container
	public GenericContainer redis = new GenericContainer<>("redis:5.0.3-alpine").withExposedPorts(6379);

	private final String userId1 = "userId1";
	private final String userId2 = "userId2";

	@Test
	public void queryServiceCacheWithJetCacheDisabled() {
		GenericApplicationContext context = TestContextUtils.load(ConfigurationWithQueryService.class, "spring.application.name:test-app", "jetcache.enable:false");
		QueryService service = context.getBean(QueryService.class);
		commonTests(service);
		InnerData queryParam1_1_result = service.queryParam1_1(userId2);
		service.clearAllCache1_1();
		assertThat(service.queryParam1_1(userId2)).isNotEqualTo(queryParam1_1_result);
	}

	@Test
	public void commonTestsWithJetCacheEnabled() {
		GenericApplicationContext context = contextWithJetCache();
		QueryService service = context.getBean(QueryService.class);
		commonTests(service);
		assertThat(allRedisKeys(context)).isNotEmpty();
	}

	@Test
	public void serializationCases() {
		GenericApplicationContext context = contextWithJetCache();
		QueryService service = context.getBean(QueryService.class);

		JsonDto param1_3_result_100 = service.queryParam1_3(100L);
		assertThat(service.queryParam1_3(100L)).isEqualTo(param1_3_result_100);

		// cache not work cases
		JsonDtoWithoutConstructor param1_4_result_100 = service.queryParam1_4(100L);
		assertThat(service.queryParam1_4(100L)).isNotEqualTo(param1_4_result_100);

		JsonDtoWithoutConstructor param1_5_result_100 = service.queryParam1_5(100L);
		assertThat(service.queryParam1_5(100L)).isNotEqualTo(param1_5_result_100);

	}

	@DisplayName("check redis storage when jetcached enabled")
	@Test
	public void checkRedisStore() {
		GenericApplicationContext context = contextWithJetCache("jetcache.remote.default.expireAfterWriteInMillis:5000");
		QueryService service = context.getBean(QueryService.class);
		InnerData param2_2_result = service.queryParam2_2(2000, userId2);
		assertThat(allRedisKeys(context)).isNotEmpty();
		String expectedKey = "jetcache.test-app.param2_2.com.binance.platform.redis.JetCacheAutoConfigurationTest$DummyQueryService.queryParam2_2:2000:userId2";
		assertThat(allRedisKeys(context)).containsExactly(expectedKey);
		assertThat(ttlOf(context, expectedKey)).isLessThanOrEqualTo(5).isGreaterThanOrEqualTo(3);
		CacheValueHolder<InnerData> holder = getRedisValue(context, expectedKey);
		assertThat(holder.getValue()).isEqualTo(param2_2_result);

		InnerData param1_1_result = service.queryParam1_1(userId2);
		String key_param1_1 = "jetcache.test-app.param1_1." + userId2;
		assertThat(allRedisKeys(context)).contains(key_param1_1);
		removeRedisKey(context, key_param1_1);
		assertThat(service.queryParam1_1(userId2)).isEqualTo(param1_1_result);
		//not populate redis again
		assertThat(allRedisKeys(context)).doesNotContain(key_param1_1);
		service.clearSingleCache1_1(userId2);
		assertThat(allRedisKeys(context)).doesNotContain(key_param1_1);

		service.queryReturnNull(1_000);
		assertThat(allRedisKeys(context)).contains("jetcache.test-app.param1_null.com.binance.platform.redis.JetCacheAutoConfigurationTest$DummyQueryService.queryReturnNull:1000");
	}

	@DisplayName("L1 cache miss & L2 cache hit, and populate L1 cache, L1 cache use local TTL after L1 populate")
	@Test
	public void readFromSecondLevelCache() throws InterruptedException {
		GenericApplicationContext context = contextWithJetCache("jetcache.local.default.expireAfterWriteInMillis:2000");
		QueryService service = context.getBean(QueryService.class);
		assertThat(allRedisKeys(context)).isEmpty();
		String query2_1_key = "jetcache.test-app.param2_1.8000_" + userId1;
		InnerData innerData = new InnerData(anyString());
		putRedisKeyValue(context, query2_1_key, new CacheValueHolder<>(innerData, 800));
		Thread.sleep(500);
		assertThat(service.queryParam2_1(8000, userId1)).isEqualTo(innerData);
		// 500
		Thread.sleep(500);
		assertThat(service.queryParam2_1(8000, userId1)).isEqualTo(innerData);
		// 1000
		Thread.sleep(500);
		assertThat(service.queryParam2_1(8000, userId1)).isEqualTo(innerData);
		// 1500
		Thread.sleep(500);
 		assertThat(service.queryParam2_1(8000, userId1)).isEqualTo(innerData);
 		// 2100
		Thread.sleep(600);
		assertThat(service.queryParam2_1(8000, userId1)).isNotEqualTo(innerData);
	}

	@DisplayName("L1 cache still work when redis is down")
	@Test
	public void failureTestWhenRedisIsDown() {
		GenericApplicationContext context = contextWithJetCache();
		QueryService service = context.getBean(QueryService.class);
		redis.stop();
		commonTests(service);
		InnerData innerData = service.queryParam2_1(100000, userId1);
		assertThat(service.queryParam2_1(100000, userId1)).isEqualTo(innerData);
	}

	@DisplayName("cache gone after expire ")
	@Test
	public void checkTTLExpire() {
		GenericApplicationContext context = contextWithJetCache(
				"jetcache.local.default.expireAfterWriteInMillis:500",
				"jetcache.remote.default.expireAfterWriteInMillis:500");
		QueryService service = context.getBean(QueryService.class);

		InnerData param1_1_result = service.queryParam1_1(userId2);
		String key_param1_1 = "jetcache.test-app.param1_1." + userId2;
		assertThat(allRedisKeys(context)).contains(key_param1_1);
		try {
			Thread.sleep(600);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
		assertThat(allRedisKeys(context)).doesNotContain(key_param1_1);
		assertThat(service.queryParam1_1(userId2)).isNotEqualTo(param1_1_result);
	}

	@NotNull
	private Set<String> allRedisKeys(GenericApplicationContext context) {
		RedisConnectionFactory redisConnectionFactory = context.getBean(RedisConnectionFactory.class);
		RedisConnection connection = redisConnectionFactory.getConnection();
		Set<byte[]> keyBytes = connection.keyCommands().keys("*".getBytes());
		connection.close();
		return keyBytes.stream().map(it -> new String(it)).collect(Collectors.toSet());
	}


	@NotNull
	private long ttlOf(GenericApplicationContext context, String key) {
		RedisConnectionFactory redisConnectionFactory = context.getBean(RedisConnectionFactory.class);
		RedisConnection connection = redisConnectionFactory.getConnection();
		long result = connection.ttl(key.getBytes());
		connection.close();
		return result;
	}

	@NotNull
	private void removeRedisKey(GenericApplicationContext context, String key) {
		RedisConnectionFactory redisConnectionFactory = context.getBean(RedisConnectionFactory.class);
		RedisConnection connection = redisConnectionFactory.getConnection();
		connection.del(key.getBytes());
		connection.close();
	}

	@NotNull
	private <T> T getRedisValue(GenericApplicationContext context, String key) {
		RedisConnectionFactory redisConnectionFactory = context.getBean(RedisConnectionFactory.class);
		JavaValueDecoder javaValueEncoder = new JavaValueDecoder(true);
		byte[] values = redisConnectionFactory.getConnection().get(key.getBytes());
		return (T) javaValueEncoder.apply(values);
	}

	@NotNull
	private void putRedisKeyValue(GenericApplicationContext context, String key, Object value) {
		RedisConnectionFactory redisConnectionFactory = context.getBean(RedisConnectionFactory.class);
		JavaValueEncoder javaValueEncoder = new JavaValueEncoder(true);
		redisConnectionFactory.getConnection().set(key.getBytes(), javaValueEncoder.apply(value));
	}

	private GenericApplicationContext contextWithJetCache(String ... extraProperties) {
		List<String> allProperties = Lists.newArrayList(
				"spring.application.name:test-app",
				"jetcache.enable:true",
				"spring.redis.host:localhost",
				"spring.redis.port:" + redis.getFirstMappedPort());
		allProperties.addAll(Arrays.asList(extraProperties));
		return TestContextUtils.load(ConfigurationWithQueryService.class, allProperties.toArray(new String[0]));
	}

	private void commonTests(QueryService service) {
		Long param0_1_result = service.queryParam0_1();
		String queryParam0_2_result = service.queryParam0_2();
		InnerData param1_1_result_userId1 = service.queryParam1_1(userId1);
		InnerData param1_1_result_userId2 = service.queryParam1_1(userId2);

		InnerData param1_2_result_userId2 = service.queryParam1_2(userId2);
		assertThat(param1_2_result_userId2).isNotEqualTo(param1_1_result_userId2);

		InnerData innerDataKey = new InnerData(anyString());
		TestDto param1_object_result = service.queryByObjectKey(innerDataKey);

		InnerData param2_1_result_1000_userId1 = service.queryParam2_1(1000, userId1);
		JsonDto param1_3_result_100 = service.queryParam1_3(100L);

		for(int i = 0; i< 3; i++) {
			assertThat(service.queryParam0_1()).isEqualTo(param0_1_result);
			assertThat(service.queryParam0_2()).isEqualTo(queryParam0_2_result);

			assertThat(service.queryParam1_1(userId1)).isEqualTo(param1_1_result_userId1);
			assertThat(service.queryParam1_1(anyString())).isNotEqualTo(param1_1_result_userId1);

			assertThat(service.queryParam1_3(100)).isEqualTo(param1_3_result_100);
			assertThat(service.queryParam1_3(anyLong())).isNotEqualTo(param1_3_result_100);

			assertThat(service.queryByObjectKey(innerDataKey)).isEqualTo(param1_object_result);
			assertThat(service.queryByObjectKey(new InnerData(anyString()))).isNotEqualTo(param1_object_result);

			assertThat(service.queryParam2_1(1000, userId1)).isEqualTo(param2_1_result_1000_userId1);
			assertThat(service.queryParam2_1(1000, anyString())).isNotEqualTo(param2_1_result_1000_userId1);
			assertThat(service.queryParam2_1(anyLong(), userId1)).isNotEqualTo(param2_1_result_1000_userId1);
		}

		service.clearSingleCache1_1(userId1);
		assertThat(service.queryParam1_1(userId1)).isNotEqualTo(param1_1_result_userId1);
		assertThat(service.queryParam1_1(userId2)).isEqualTo(param1_1_result_userId2);

		InnerData param2_1_result_1000_userId1_new = service.putCacheParam2_1(1000, userId1);
		assertThat(param2_1_result_1000_userId1_new).isNotEqualTo(param2_1_result_1000_userId1);
		assertThat(service.queryParam2_1(1000, userId1)).isEqualTo(param2_1_result_1000_userId1_new);

		service.queryReturnNull(10_000);
		service.queryReturnNull(10_000);
		assertThat(service.queryReturnNullCount()).isEqualTo(1);
		service.queryReturnNull(12_000);
		assertThat(service.queryReturnNullCount()).isEqualTo(2);
	}

	@Configuration
	@Import({LocalCacheAutoConfiguration.class, RedisUseJedisAutoConfiguration.class, JetCacheAutoConfiguration.class})
	@EnableCaching
	static class ConfigurationWithQueryService {
		@Bean
		public QueryService queryBean() {
			return new DummyQueryService();
		}
	}

	@CacheConfig(cacheNames = "default_query")
	interface QueryService {
		@Cacheable(cacheNames="param0_1")
		long queryParam0_1();

		@Cacheable(cacheNames="param0_2")
		String queryParam0_2();

		@Cacheable(cacheNames="param1_1", key = "#userId")
		InnerData queryParam1_1(String userId);

		@CacheEvict(cacheNames="param1_1", allEntries = true)
		void clearAllCache1_1();

		@CacheEvict(cacheNames="param1_1", key = "#userId")
		void clearSingleCache1_1(String userId);

		@Cacheable(cacheNames="param1_2", key = "#userId")
		InnerData queryParam1_2(String userId);

		@CacheEvict(cacheNames="param1_2", allEntries = true)
		void clearAllCache1_2();

		@Cacheable(value= "param1_object")
		TestDto queryByObjectKey(InnerData key);

		@Cacheable(value= "param1_null")
		TestDto queryReturnNull(long id);

		@Cacheable(cacheNames="param1_3")
		JsonDto queryParam1_3(long id);

		@Cacheable(cacheNames="param1_4")
		JsonDtoWithoutConstructor queryParam1_4(long id);

		@Cacheable(cacheNames="param1_5")
		JsonDtoWithoutConstructor queryParam1_5(long id);

		long queryReturnNullCount();

		@Cacheable(value="param2_1", key = "#id + '_' + #userId")
		InnerData queryParam2_1(long id, String userId);

		@CachePut(value = "param2_1",key = "#id + '_' + #userId")
		InnerData putCacheParam2_1(long id, String userId);

		@Cacheable(cacheNames="param2_2")
		InnerData queryParam2_2(long id, String userId);
	}

	static class DummyQueryService implements QueryService {
		public boolean failQuery = false;
		private int queryReturnNullCount = 0;

		@Override
		public long queryParam0_1() {
			return AnyUtils.anyLong();
		}

		@Override
		public String queryParam0_2() {
			return anyString();
		}

		@Override
		public InnerData queryParam1_1(String userId) {
			return new InnerData(userId + anyString());
		}

		@Override
		public void clearAllCache1_1() {

		}

		@Override
		public void clearSingleCache1_1(String userId) {
		}

		@Override
		public InnerData queryParam1_2(String userId) {
			return new InnerData(userId + "_" + anyLong());
		}

		@Override
		public void clearAllCache1_2() {

		}

		@Override
		public TestDto queryByObjectKey(InnerData innerData) {
			return new TestDto(anyInt(), ImmutableMap.of(innerData.value, innerData), Lists.newArrayList(innerData));
		}

		@Override
		public TestDto queryReturnNull(long id) {
			queryReturnNullCount ++;
			return null;
		}

		@Override
		public long queryReturnNullCount() {
			return queryReturnNullCount;
		}

		@Override
		public JsonDto queryParam1_3(long id) {
			HashMap<String, Long> data = Maps.newHashMap();
			data.put("100", id);
			return new JsonDto(id, data);
		}

		@Override
		public JsonDtoWithoutConstructor queryParam1_4(long id) {
			return new JsonDtoWithoutConstructor(anyLong(), null);
		}

		@Override
		public JsonDtoWithoutConstructor queryParam1_5(long id) {
			return new JsonDtoWithoutConstructor(anyLong(), ImmutableMap.of(anyString(), id));
		}

		@Override
		public InnerData queryParam2_1(long id, String userId) {
			if(failQuery) {
				failQuery = false;
				throw new RuntimeException("e");
			}
			return new InnerData(id + anyString());
		}

		@Override
		public InnerData putCacheParam2_1(long id, String userId) {
			return new InnerData(id + anyString());
		}

		@Override
		public InnerData queryParam2_2(long id, String userId) {
			return new InnerData(id + "_ " + userId);
		}
	}

	@Data
	static class InnerData implements Serializable {
		private final String value;
	}

	@Data
	static class TestDto implements Serializable{
		private final int value;
		private final Map<String,InnerData> map;
		private final List<InnerData> list;
	}

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	static class JsonDto {
		private long value;
		private Map<String, Long> data;
	}

	@EqualsAndHashCode
	static class JsonDtoWithoutConstructor {
		private long value;
		private Map<String, Long> data;
		public JsonDtoWithoutConstructor(long value, Map<String, Long> data) {
			this.value = value;
			this.data = data;
		}
	}

}