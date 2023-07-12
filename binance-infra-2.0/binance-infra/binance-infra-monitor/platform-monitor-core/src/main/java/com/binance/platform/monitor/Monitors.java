package com.binance.platform.monitor;

import java.time.Duration;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ImmutableMap;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Metrics;
import io.micrometer.core.instrument.Tags;
import io.opentracing.ActiveSpan;
import io.opentracing.Span;
import io.opentracing.Tracer;

public class Monitors {
	private static final Logger LOGGER = LoggerFactory.getLogger(Monitors.class);

	private static final RequestUtil requestUtil = new RequestUtil();

	private static final InheritableThreadLocal<Span> TRANSACTION_CACHE = new InheritableThreadLocal<Span>();

	private static final Map<String, Number> GAUGE_CACHE = new HashMap<>();

	public static final AtomicBoolean MICROMTER_TAG_FULLED = new AtomicBoolean(false);

	public static void begin() {
		try {
			Tracer tracer = new org.apache.skywalking.apm.toolkit.opentracing.SkywalkingTracer();
			Span span = tracer.buildSpan("customerSpan").startManual();
			TRANSACTION_CACHE.set(span);
		} catch (Throwable e) {
			LOGGER.warn(" begin error", e);
		}
	}

	public static void finish() {
		try {
			Span span = TRANSACTION_CACHE.get();
			if (span != null) {
				span.finish();
			}
			TRANSACTION_CACHE.remove();
		} catch (Throwable e) {
			LOGGER.warn(" finish error", e);
		}
	}

	public static void traceSpan(String type, Object param) {
		try {
			String url = requestUtil.getCurrentRequestUrlOrDefault("none");
			Metrics.counter("trace_span_count", "type", type, "uri", url).increment();
			Span span = TRANSACTION_CACHE.get();
			LOGGER.info("trigger event:{},type:{}", type, param);
			if (span != null) {
				ImmutableMap.Builder<String, Object> eventMap = ImmutableMap.of("eventType", type, "eventParam", param)
						.builder();
				span.log(eventMap.build());
			} else {
				Tracer tracer = new org.apache.skywalking.apm.toolkit.opentracing.SkywalkingTracer();
				ActiveSpan activeSpan = tracer.buildSpan(type).startActive();
				ImmutableMap.Builder<String, Object> eventMap = ImmutableMap.of("eventType", type, "eventParam", param)
						.builder();
				activeSpan.log(eventMap.build());
				activeSpan.close();
			}
		} catch (Throwable e) {
			LOGGER.warn("log event error", e);
		}
	}

	public static final void count(String name, double amount, String... tags) {
		try {
			Metrics.counter(name, tags).increment(amount);
		} catch (Throwable e) {
			LOGGER.warn(" count error", e);
		}
	}

	public static final void count(String name, String... tags) {
		try {
			Metrics.counter(name, tags).increment();
		} catch (Throwable e) {
			LOGGER.warn(" count error", e);
		}
	}

	public static final <T extends Number> void gauge(String name, T number, String... tags) {
		try {
			Metrics.gauge(name, Tags.of(tags), number);
		} catch (Throwable e) {
			LOGGER.warn(" gauge error", e);
		}
	}

	public static final <T extends Number> void gaugeGCResisted(String name, T number, String... tags) {
		try {
			Tags t = Tags.of(tags);
			String key = name + t.toString();// name[tag(key=value),tag(key2=value2),...]
			GAUGE_CACHE.put(key, number);
			Metrics.gauge(name, t, GAUGE_CACHE, m -> m.get(key).doubleValue());
		} catch (Throwable e) {
			LOGGER.warn(" gauge error", e);
		}
	}

	public static final <T extends Collection<?>> T gaugeCollectionSize(String name, T collection, String... tags) {
		try {
			return Metrics.gaugeCollectionSize(name, Tags.of(tags), collection);
		} catch (Throwable e) {
			LOGGER.warn(" gaugeCollectionSize error", e);
		}
		return null;
	}

	public static final <T extends Map<?, ?>> T gaugeMapSize(String name, T map, String... tags) {
		try {
			return Metrics.gaugeMapSize(name, Tags.of(tags), map);
		} catch (Throwable e) {
			LOGGER.warn(" gaugeMapSize error", e);
		}
		return null;
	}

	public static final long getNanosecondsAfter(long start) {
		try {
			return registry().config().clock().monotonicTime() - start;
		} catch (Throwable e) {
			LOGGER.warn(" getNanosecondsAfter error", e);
		}
		return System.nanoTime();
	}

	public static final long monotonicTime() {
		try {
			return registry().config().clock().monotonicTime();
		} catch (Throwable e) {
			LOGGER.warn(" monotonicTime error", e);
		}
		return System.nanoTime();
	}

	public static final void recordNanoSecond(String name, long timeElapsedNanos, String... tags) {
		try {
			recordTime(name, timeElapsedNanos, TimeUnit.NANOSECONDS, tags);
		} catch (Throwable e) {
			LOGGER.warn(" recordNanoSecond error", e);
		}
	}

	public static final void recordNanoSecondAfterStartTime(String name, long startTime, String... tags) {
		try {
			recordTime(name, monotonicTime() - startTime, TimeUnit.NANOSECONDS, tags);
		} catch (Throwable e) {
			LOGGER.warn(" recordNanoSecondAfterStartTime error", e);
		}
	}

	public static final void recordTime(String name, long timeElapsed, TimeUnit timeUnit, double[] percentiles,
			Duration[] sla, String... tags) {
		try {
			Metrics.timer(name, Tags.of(tags)).record(timeElapsed, timeUnit);
		} catch (Throwable e) {
			LOGGER.warn(" recordTime error", e);
		}
	}

	public static final void recordTime(String name, long timeElapsed, TimeUnit timeUnit, String... tags) {
		try {
			Metrics.timer(name, Tags.of(tags)).record(timeElapsed, timeUnit);
		} catch (Throwable e) {
			LOGGER.warn(" recordTime error", e);
		}
	}

	private static final MeterRegistry registry() {
		return Metrics.globalRegistry;
	}

	public static final void summary(String name, double amount, double[] percentiles, long[] sla, String... tags) {
		try {
			Metrics.summary(name, Tags.of(tags)).record(amount);
		} catch (Throwable e) {
			LOGGER.warn(" summary error", e);
		}
	}

	public static final void summary(String name, double amount, double[] percentiles, String... tags) {
		try {
			Metrics.summary(name, Tags.of(tags)).record(amount);
		} catch (Throwable e) {
			LOGGER.warn(" summary error", e);
		}
	}

	public static final void summary(String name, double amount, String... tags) {
		try {
			Metrics.summary(name, Tags.of(tags)).record(amount);
		} catch (Throwable e) {
			LOGGER.warn(" summary error", e);
		}
	}

}
