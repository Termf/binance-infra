package com.binance.platform.monitor.logging;

import com.binance.platform.env.EnvUtil;
import org.slf4j.MDC;
import org.springframework.core.env.Environment;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Sample for reduce log
 *
 * @author Li Fenggang
 * @since 2020/7/23 7:30 下午
 */
public class SampleUtil {
	/** The header name to mark the sample status */
	public static final String HTTP_HEADER_NAME = "X-LOG-SAMPLE";
	/** The header value - sampled */
	public static final String HTTP_HEADER_VALUE_SAMPLED = "true";
	/** The header value - not sampled */
	public static final String HTTP_HEADER_VALUE_NOT_SAMPLED = "false";
	/**
	 * upper limit of the sample rate
	 */
	private static final int SAMPLING_RATE_UPPER_LIMIT = 100;
	/**
	 * lower limit of the sample rate
	 */
	private static final int SAMPLING_RATE_LOWER_LIMIT = 0;
	/**
	 * Used to count the sampling times
	 */
	private static final AtomicInteger samplingRateCounter = new AtomicInteger(0);

	/**
	 * config prefix
	 */
	private static final String CONFIG_PREFIX = "com.binance.infra.log.sample.";
	protected static final String CONFIG_SAMPLING_RATE = CONFIG_PREFIX + "rate";

	/**
	 * To save the value in the Env to reduce the time spent to resolve the target
	 * value
	 */
	private static AtomicReference<String> currentSamplingRateConfig = new AtomicReference<>();
	/** To save the refresh timestamp of the sample rate */
	private static AtomicLong configSamplingRateTimestamp = new AtomicLong();

	/**
	 * To save the current sampling rate
	 */
	private static volatile Integer currentSamplingRate;

	private static Environment env;

	private static boolean isGray = false;

	private static boolean isBlue = false;

	public static void init(Environment env) {
		SampleUtil.env = env;

		String deployFlag = EnvUtil.getDeployFlag();
		if ("blue".equalsIgnoreCase(deployFlag)) {
			isBlue = true;
		}
		String envFlag = EnvUtil.getEnvFlag();
		if ("gray".equalsIgnoreCase(envFlag)) {
			isGray = true;
		}
	}

	/**
	 * Check if the request is sampled, if there is no sampling mark, resample
	 * it.<br/>
	 * If the param {@code request} is null, returns false.
	 *
	 * @param request
	 * @return true, should be sampled; false, otherwise.
	 */
	public static boolean sampleIfAbsent(HttpServletRequest request) {
		if (request == null) {
			return false;
		}

		String header = request.getHeader(HTTP_HEADER_NAME);
		if (HTTP_HEADER_VALUE_SAMPLED.equalsIgnoreCase(header)) {
			return true;
		} else if (HTTP_HEADER_VALUE_NOT_SAMPLED.equalsIgnoreCase(header)) {
			return false;
		} else {
			return sampleByRate();
		}
	}

	public static boolean isFullSample() {
		if (isGray || isBlue) {
			return true;
		}
		Integer samplingRate = getSamplingRate();
		// service custom
		if (samplingRate != null && samplingRate == SAMPLING_RATE_UPPER_LIMIT) {
			return true;
		}

		return false;
	}

	/**
	 * Check if the request will be sampled.<br/>
	 * If the param {@code request} is null, returns false.
	 * 已废弃，需要使用{@link #sampleIfAbsent(HttpServletRequest)}
	 * @param request
	 * @return
	 */
	@Deprecated
	public static boolean sample(HttpServletRequest request) {
		if (request == null) {
			return false;
		}

		return sampleByRate();
	}

	/**
	 * Sample by rate.
	 *
	 * @return
	 */
	protected static boolean sampleByRate() {
		Integer samplingRate = getSamplingRate();
		// default is no limit.
		if (samplingRate == null) {
			return true;
		}

		int nextI = samplingRateCounter.incrementAndGet();
		int position = Math.abs(nextI % SAMPLING_RATE_UPPER_LIMIT);
		if (position < samplingRate) {
			return true;
		}
		return false;
	}

	private static Integer parseInteger(String value, Integer defaultValue) {
		if (value == null) {
			return null;
		} else {
			try {
				return Integer.parseInt(value);
			} catch (RuntimeException e) {
				return defaultValue;
			}
		}
	}

	/**
	 * Retrieve the samping rate.
	 *
	 * @return null means no limit. Its value is between
	 *         {@link #SAMPLING_RATE_LOWER_LIMIT} and
	 *         {@link #SAMPLING_RATE_UPPER_LIMIT}.
	 */
	private static Integer getSamplingRate() {
		if (needRefresh(configSamplingRateTimestamp)) {
			String property = getProperty(CONFIG_SAMPLING_RATE);
			String expect = currentSamplingRateConfig.get();
			if (!Objects.equals(property, expect)) {
				if (currentSamplingRateConfig.compareAndSet(expect, property)) {
					Integer newSamplingRate = parseInteger(property, currentSamplingRate);
					if (newSamplingRate == null) {
						currentSamplingRate = null;
					} else {
						if (newSamplingRate > SAMPLING_RATE_UPPER_LIMIT) {
							currentSamplingRate = SAMPLING_RATE_UPPER_LIMIT;
						} else if (newSamplingRate < SAMPLING_RATE_LOWER_LIMIT) {
							currentSamplingRate = SAMPLING_RATE_LOWER_LIMIT;
						} else {
							currentSamplingRate = newSamplingRate;
						}
					}
				}
			}
		}

		return currentSamplingRate;
	}

	private static String getProperty(String key) {
		if (env == null) {
			return null;
		} else {
			return env.getProperty(key);
		}
	}

	/**
	 * refresh per second to reduce time cost.<br/>
	 * The method "getProperty" of the {@code Environment} has higher time cost.
	 *
	 * @param timestampHolder
	 * @return
	 */
	protected static boolean needRefresh(AtomicLong timestampHolder) {
		long systemTime = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis());
		long current = timestampHolder.get();
		if (current < systemTime) {
			return timestampHolder.compareAndSet(current, systemTime);
		}
		return false;
	}

	/**
	 * 检查这个采样标记是否是否代表采样了。
	 *
	 * @param sampleTagValue
	 * @return true，代表采样了；false，代表未采样
	 */
	public static boolean isSampled(Object sampleTagValue) {
		if (HTTP_HEADER_VALUE_NOT_SAMPLED.equals(sampleTagValue)) {
			return false;
		}
		return true;
	}

	/**
	 * 保存采样标记，用于后面判断是否采样
	 *
	 * @param sampled
	 */
	public static void saveSampleTag(boolean sampled) {
		if (sampled) {
			MDC.put(HTTP_HEADER_NAME, HTTP_HEADER_VALUE_SAMPLED);
		} else {
			MDC.put(HTTP_HEADER_NAME, HTTP_HEADER_VALUE_NOT_SAMPLED);
		}
	}

	public static void clearSampleTag() {
		MDC.remove(HTTP_HEADER_NAME);
	}

}
