package com.binance.platform.monitor.metric;

import static java.util.stream.Collectors.toList;
import static java.util.stream.StreamSupport.stream;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.autoconfigure.metrics.MeterRegistryCustomizer;

import com.binance.platform.monitor.Monitors;
import com.google.common.collect.Lists;

import io.micrometer.core.instrument.Meter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tag;
import io.micrometer.core.instrument.config.MeterFilter;
import io.micrometer.core.instrument.config.MeterFilterReply;

public class MyRegistryCustomizer implements MeterRegistryCustomizer<MeterRegistry> {

	private static final Logger LOGGER = LoggerFactory.getLogger(MyRegistryCustomizer.class);

	@Value("${micrometer.http.server.requests.uri.maximumAllowableTags:5000}")
	private Integer maximumAllowableTags;

	private static final String MONITOR_METRIC_TAGS_IGNORE_CHARACTER = "";

	private static final AtomicLong lastUpdateTs = new AtomicLong(0);

	private static class TagIgnoreMeterFilter implements MeterFilter {
		private List<String> tagKeys = new ArrayList<>();

		TagIgnoreMeterFilter() {
			onTagKeyChange(MONITOR_METRIC_TAGS_IGNORE_CHARACTER);
		}

		@Override
		public Meter.Id map(Meter.Id id) {
			if (null == tagKeys || tagKeys.size() == 0) {
				return id;
			}
			List<Tag> tags = stream(id.getTagsAsIterable().spliterator(), false).filter(t -> {
				for (String tagKey : tagKeys) {
					if (t.getKey().equals(tagKey))
						return false;
				}
				return true;
			}).collect(toList());

			return id.replaceTags(tags);
		}

		void onTagKeyChange(String tagKeyString) {
			if (!StringUtils.isEmpty(tagKeyString)) {
				tagKeys = Arrays.asList(tagKeyString.split(","));
			}
		}
	}

	private final CustomizerTag platformTag;

	public MyRegistryCustomizer(CustomizerTag platformTag) {
		this.platformTag = platformTag;
	}

	private static final Tag URI_UNKNOWN = Tag.of("uri", "None");

	private static final Tag EXCEPTION_UNKNOWN = Tag.of("exception", "None");

	private static final Tag STATUS_UNKNOWN = Tag.of("status", "None");

	private static final Tag OUTCOME_UNKNOWN = Tag.of("outcome", "None");

	private static final Tag METHOD_UNKNOWN = Tag.of("method", "None");

	private static final Tag CLASS_UNKNOWN = Tag.of("class", "None");

	private static List<Tag> webTags;

	static {
		webTags = Arrays.asList(URI_UNKNOWN, EXCEPTION_UNKNOWN, STATUS_UNKNOWN, STATUS_UNKNOWN, OUTCOME_UNKNOWN,
				OUTCOME_UNKNOWN, METHOD_UNKNOWN, CLASS_UNKNOWN);
	}

	@Override
	public void customize(MeterRegistry registry) {
		List<Tag> allTags = Lists.newArrayList();
		allTags.addAll(platformTag.getTags());
		allTags.addAll(webTags);
		registry.config().commonTags(allTags)//
				.meterFilter(new TagIgnoreMeterFilter())//
				.meterFilter(new H2CUpgradeIgnoreFilter())
				.meterFilter(new KafkaAuthenticationFilter())
				.meterFilter(MeterFilter.maximumAllowableTags("http.server.requests", "uri", maximumAllowableTags,
						new MeterFilter() {
							@Override
							public MeterFilterReply accept(Meter.Id id) {
								Monitors.MICROMTER_TAG_FULLED.set(true);
								LOGGER.error("http.server.requests has too many uri tag, id is :{}", id.toString());
								try {
									return MeterFilterReply.DENY;
								} finally {
									long currentTs = System.currentTimeMillis();
									if (currentTs - lastUpdateTs.get() >= 3600000) {
										lastUpdateTs.set(currentTs);
										System.gc();
									}
									id = null;
								}
							}
						}));// 至多能容纳一万个url作为tag
	}

	private static class H2CUpgradeIgnoreFilter implements MeterFilter {
		@Override
		public MeterFilterReply accept(Meter.Id id) {
			if (StringUtils.startsWith(id.getName(), "http.server.requests") &&
					StringUtils.equals(id.getTag("status"), "404") &&
					StringUtils.equals(id.getTag("uri"), "/**")) {
				return MeterFilterReply.DENY;
			}
			return MeterFilter.super.accept(id);
		}
	}

	/**
	 * Ignore authentication metrics
	 */
	private static class KafkaAuthenticationFilter implements MeterFilter {
		@Override
		public MeterFilterReply accept(Meter.Id id) {
			String metricName = id.getName();
			if (metricName.startsWith("kafka") && metricName.contains("authentication")) {
				return MeterFilterReply.DENY;
			}
			return MeterFilter.super.accept(id);
		}
	}
}
