package com.binance.platform.monitor.metric;

import java.util.Arrays;
import java.util.List;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;

import com.vip.vjtools.vjkit.net.NetUtil;

import io.micrometer.core.instrument.Tag;

@Getter
public class CustomizerTag {

	@Value("${spring.profiles.active:None}")
	private String active;
	@Value("${spring.application.name:None}")
	private String application;
	@Value("${eureka.instance.metadata-map.zone:None}")
	private String zone;

	private List<Tag> tags;

	private String getEurekaZone() {
		return zone;
	}

	public List<Tag> getTags() {
		if (tags == null) {
			tags = Arrays.asList(Tag.of("application", application), Tag.of("active", active),
					Tag.of("zone", getEurekaZone()), Tag.of("instance", NetUtil.getLocalHost()));
		}
		return tags;
	}

}
