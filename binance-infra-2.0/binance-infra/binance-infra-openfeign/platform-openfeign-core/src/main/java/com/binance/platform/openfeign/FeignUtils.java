package com.binance.platform.openfeign;

import static java.util.Optional.ofNullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpHeaders;

public final class FeignUtils {

	private FeignUtils() {
		throw new IllegalStateException("Can't instantiate a utility class");
	}

	static HttpHeaders getHttpHeaders(Map<String, Collection<String>> headers) {
		HttpHeaders httpHeaders = new HttpHeaders();
		for (Map.Entry<String, Collection<String>> entry : headers.entrySet()) {
			httpHeaders.put(entry.getKey(), new ArrayList<>(entry.getValue()));
		}
		return httpHeaders;
	}

	static Map<String, Collection<String>> getHeaders(HttpHeaders httpHeaders) {
		LinkedHashMap<String, Collection<String>> headers = new LinkedHashMap<>();

		for (Map.Entry<String, List<String>> entry : httpHeaders.entrySet()) {
			headers.put(entry.getKey(), entry.getValue());
		}

		return headers;
	}

	static Collection<String> addTemplateParameter(Collection<String> possiblyNull, String paramName) {
		Collection<String> params = ofNullable(possiblyNull).orElse(new ArrayList<>());
		params.add(String.format("{%s}", paramName));
		return params;
	}

}