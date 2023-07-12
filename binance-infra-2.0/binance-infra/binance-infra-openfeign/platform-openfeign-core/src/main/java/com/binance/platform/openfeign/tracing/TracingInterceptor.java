package com.binance.platform.openfeign.tracing;

import java.util.Collection;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.binance.platform.common.TrackingUtils;
import com.binance.platform.openfeign.ReWriteHeaderFeignClient.ReWriteFeignClientHeaderHandler;
import com.google.common.collect.Lists;

import feign.Request;

public class TracingInterceptor implements ReWriteFeignClientHeaderHandler {

	private static final Logger logger = LoggerFactory.getLogger(TracingInterceptor.class);

	private final String feignClientServiceName;

	public TracingInterceptor(String feignClientServiceName) {
		this.feignClientServiceName = feignClientServiceName;
	}

	@Override
	public void headers(Request request, Request.Options options, Map<String, Collection<String>> headers) {
		// 调用的微服务时添加TRACE_ID头部
		String traceId = TrackingUtils.getTrace();
		logger.debug(String.format("traceId will transfer next service,and the value is %s", traceId));
		headers.put(TrackingUtils.TRACE_ID_HEAD, Lists.newArrayList(traceId));
		headers.put(TrackingUtils.TRACE_APP_HEAD, Lists.newArrayList(feignClientServiceName));
	}

}
