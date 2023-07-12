package com.binance.master.utils;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import com.binance.platform.redis.RedisLock;

@Deprecated
@Slf4j
public class RedisCacheUtils {

	private static final Map<String, Object> LOCAL_CACHE_MAP = new ConcurrentHashMap<String, Object>();

	private static RedisTemplate<String, Object> redisTemplate;

	private static RedisLock redisLock;

	public static final int DEFAULT_LOCK_TIMEOUT = 10;

	public static void setRedisLock(RedisLock redisLock) {
		RedisCacheUtils.redisLock = redisLock;
	}

	public static void setRedisTemplate(RedisTemplate<String, Object> redisTemplate) {
		RedisCacheUtils.redisTemplate = redisTemplate;
	}

	public static RedisTemplate<String, Object> getRedisTemplate() {
		return RedisCacheUtils.redisTemplate;
	}

	private static String getKey(String key, String prefix) {
		return StringUtils.join(prefix, ":", key);
	}

	/**
	 * 模糊删除缓存
	 */
	public static void delFuzzy(String... key) {
		if (key != null && key.length > 0) {
			Set<String> keysSet = new HashSet<String>();
			for (String string : key) {
				Set<String> keys = getRedisTemplate().keys(string);
				if (null != keys && keys.size() > 0) {
					keysSet.addAll(keys);
				}
			}
			getRedisTemplate().delete(keysSet);
		}
	}

	/**
	 * 根据key精确匹配删除
	 *
	 * @param key
	 */
	public static void del(String... key) {
		if (key != null && key.length > 0) {
			if (key.length == 1) {
				getRedisTemplate().delete(key[0]);
			} else {
				getRedisTemplate().delete(Arrays.asList(key));
			}
		}
	}

	public static void del(String key) {
		LOCAL_CACHE_MAP.remove(key);
		getRedisTemplate().delete(key);
	}

	public static void del(String key, String prefix) {
		final String keyTemp = prefix + ":" + key;
		del(keyTemp);
	}

	@SuppressWarnings("unchecked")
	public static <T> T get(String key, Class<T> clazz) {
		Object cacheObj = getRedisTemplate().boundValueOps(key).get();
		if (cacheObj == null)
			return null;
		try {
			if (cacheObj instanceof String && clazz.getName() != String.class.getName()) {
				return JSON.parseObject((String) cacheObj, clazz, Feature.IgnoreAutoType);
			}
			if (cacheObj instanceof JSONObject && clazz.getName() != JSONObject.class.getName()) {
				return JSON.parseObject(((JSONObject) cacheObj).toJSONString(), clazz, Feature.IgnoreAutoType);
			}
			return (T) cacheObj;
		} catch (Exception e) {
			return JSON.parseObject(cacheObj.toString(), clazz, Feature.IgnoreAutoType);
		}
	}

	@SuppressWarnings("unchecked")
	public static <T> T get(String key, Class<T> clazz, String prefix) {
		final String keyTemp = prefix + ":" + key;
		Object obj = getRedisTemplate().boundValueOps(keyTemp).get();
		if (null != obj) {
			if (!StringUtils.equals(obj.getClass().getName(), clazz.getName())) {
				obj = JSON.parseObject(JSON.toJSONString(obj), clazz, Feature.IgnoreAutoType);
			}
			return (T) obj;
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public static <T> T get(String key, Class<T> clazz, String prefix, Object defaultResult) {
		T result = get(key, clazz, prefix);
		if (result == null) {
			result = (T) defaultResult;
		}
		return result;
	}

	public static String get(String key) {
		return (String) getRedisTemplate().boundValueOps(key).get();
	}

	public static String get(String key, String defaultResult, boolean isLocalCache) {
		String result = null;
		if (isLocalCache) {
			result = (String) LOCAL_CACHE_MAP.get(key);
		}
		if (StringUtils.isBlank(result)) {
			Object temp = getRedisTemplate().boundValueOps(key).get();
			result = temp != null ? temp.toString() : null;
			if (StringUtils.isBlank(result)) {
				result = defaultResult;
			}
		}
		if (isLocalCache) {
			LOCAL_CACHE_MAP.put(key, result);
		}
		return result;
	}

	/**
	 * 将value对象写入缓存
	 *
	 * @param key
	 * @param value
	 * @param time  失效时间(秒)
	 */
	public static void set(String key, Object value, long time) {
		if (time < 0L) {
			Long expire = getRedisTemplate().getExpire(key, TimeUnit.SECONDS);
			if (expire != null) {
				time = expire;
			} else {
				time = 60L;
			}
		}
		getRedisTemplate().opsForValue().set(key, value);
		if (time > 0L) {
			getRedisTemplate().expire(key, time, TimeUnit.SECONDS);
		}
	}

	public static void set(String key, Object value, long time, String prefix) {
		final String keyTemp = prefix + ":" + key;
		if (time < 0L) {
			Long expire = getRedisTemplate().getExpire(keyTemp, TimeUnit.SECONDS);
			if (expire != null) {
				time = expire;
			} else {
				time = 60L;
			}
		}
		getRedisTemplate().opsForValue().set(keyTemp, value);
		if (time > 0) {
			getRedisTemplate().expire(keyTemp, time, TimeUnit.SECONDS);
		}
	}

	public static void setJSON(String key, Object value, long time, String prefix) {
		set(key, JSON.toJSONString(value), time, prefix);
	}

	public static JSONObject getJSON(String key, String prefix) {
		final String keyTemp = prefix + ":" + key;
		final String jsonStr = get(keyTemp);
		if (StringUtils.isNotBlank(jsonStr)) {
			return JSON.parseObject(jsonStr);
		}
		return null;
	}

	/**
	 * 增量操作
	 *
	 * @param key
	 * @param delta
	 * @return
	 */
	public static Long increment(String key, long delta) {
		return getRedisTemplate().opsForValue().increment(key, delta);
	}

	public static Long increment(String key, String prefix, long delta) {
		final String keyTemp = prefix + ":" + key;
		return increment(keyTemp, delta);
	}

	public static Long increment(String key, String prefix, long delta, long time, TimeUnit unit) {
		final String keyTemp = prefix + ":" + key;
		Long num = increment(keyTemp, delta);
		expire(keyTemp, time, unit);
		return num;
	}

	/**
	 * 设置指定key的超时时间
	 *
	 * @param key
	 * @param time
	 * @param unit
	 * @return
	 */
	public static boolean expire(String key, long time, TimeUnit unit) {
		if (time > 0) {
			return getRedisTemplate().expire(key, time, unit);
		}
		return false;
	}

	public static boolean expire(String key, long time, TimeUnit unit, String prefix) {
		return expire(getKey(key, prefix), time, unit);
	}

	/**
	 * 设置指定key的超时时间
	 *
	 * @param key
	 * @param time
	 * @return
	 */
	public static boolean expire(String key, long time) {
		if (time > 0) {
			return getRedisTemplate().expire(key, time, TimeUnit.SECONDS);
		}
		return false;
	}

	public static boolean expire(String key, long time, String prefix) {
		return expire(getKey(key, prefix), time);
	}

	/**
	 * 从右边开始获取数据
	 *
	 * @param key
	 * @return
	 */
	public static Object getRightPop(String key) {
		return getRedisTemplate().opsForList().rightPop(key);
	}

	/**
	 * 从左边开始存数据
	 *
	 * @param key
	 * @param value
	 * @return
	 */
	public static Long setLeftPush(String key, Object value) {
		return getRedisTemplate().opsForList().leftPush(key, value);
	}

	public static Long count(String key) {
		return getRedisTemplate().boundListOps(key).size();
	}

	/**
	 * 获取锁
	 *
	 * @param lockKey
	 * @return
	 */
	@Deprecated // 直接AutoWire注入RedisLock，不要使用静态工具
	public static Lock getLock(Object lockKey) {
		return redisLock;
	}

	public static void hashPut(String key, Object hashKey, Object value) {
		getRedisTemplate().opsForHash().put(key, hashKey, value);
	}

	public static void hashPutAll(String key, Map<String, ?> values) {
		getRedisTemplate().opsForHash().putAll(key, values);
	}

	public static void hashRemove(String key, Object... hashKeys) {
		getRedisTemplate().opsForHash().delete(key, hashKeys);
	}

	@SuppressWarnings("unchecked")
	public static <T> T hashGet(String key, Object hashKey, Class<T> clazz) {
		Object value = getRedisTemplate().opsForHash().get(key, hashKey);
		return (T) value;
	}

	@SuppressWarnings("unchecked")
	public static <T> List<T> hashMultiGet(String key, Class<T> clazz, List<Object> hashKeys) {
		return (List<T>) getRedisTemplate().opsForHash().multiGet(key, hashKeys);
	}

	/**
	 * zset add
	 *
	 * @param key
	 * @param val
	 * @param score
	 * @return
	 */
	public static Boolean zadd(String key, Object val, double score) {
		return getRedisTemplate().opsForZSet().add(key, val, score);
	}

	/**
	 * zset rangeByScore
	 *
	 * @param key
	 * @param min
	 * @param max
	 * @return
	 */
	public static Set<Object> zrangeByScore(String key, double min, double max) {
		return getRedisTemplate().opsForZSet().rangeByScore(key, min, max);
	}

	/**
	 * zset rangeByScore 分页
	 *
	 * @param key
	 * @param min
	 * @param max
	 * @return
	 */
	public static Set<Object> zrangeByScore(String key, double min, double max, long offset, long count) {
		return getRedisTemplate().opsForZSet().rangeByScore(key, min, max, offset, count);
	}

	/**
	 * zset remove elements
	 *
	 * @param key
	 * @param vals
	 * @return
	 */
	public static Long zrem(String key, Object... vals) {
		return getRedisTemplate().opsForZSet().remove(key, vals);
	}

	public static boolean setNX(String key, String value, long time) {
		boolean isOK = getRedisTemplate().opsForValue().setIfAbsent(key, value);
		getRedisTemplate().expire(key, time, TimeUnit.SECONDS);
		return isOK;
	}

}
