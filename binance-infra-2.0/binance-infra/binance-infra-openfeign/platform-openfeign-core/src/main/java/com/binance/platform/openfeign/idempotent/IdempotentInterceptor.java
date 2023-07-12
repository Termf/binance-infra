package com.binance.platform.openfeign.idempotent;

import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.net.URI;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.binance.master.models.APIResponse;
import com.binance.platform.MyJsonUtil;
import com.binance.platform.openfeign.ReWriteHeaderFeignClient.ReWriteFeignClientHeaderHandler;
import com.fasterxml.uuid.EthernetAddress;
import com.fasterxml.uuid.Generators;
import com.fasterxml.uuid.impl.TimeBasedGenerator;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import feign.FeignClientMethodMetadataParseHandler;
import feign.MethodMetadata;
import feign.Request;
import feign.Request.Options;
import feign.RequestTemplate;

/**
 * 幂等操作
 */
public class IdempotentInterceptor extends HandlerInterceptorAdapter
		implements ReWriteFeignClientHeaderHandler, FeignClientMethodMetadataParseHandler {

	private static final String INTRA_IDEMPOTENCE_HEADER_KEY = "intra-idemotence";

	private static final String IDEMOTENCE_TURE_SWITCH = "com.binance.intra.idempotence.switch";

	private static final Map<String, Set<String>> IDEMPOTENT_RESOURCE = Maps.newConcurrentMap();

	private TimeBasedGenerator uidGenerator;

	private Environment env;

	private String appName;

	private IdempotentRepository idempotentRepository;

	/**
	 * 客户端
	 */
	public IdempotentInterceptor(Environment env) {
		this.uidGenerator = Generators.timeBasedGenerator(EthernetAddress.fromInterface());
		this.env = env;
	}

	/**
	 * 服务端
	 */
	public IdempotentInterceptor(Environment env, IdempotentRepository idempotentRepository) {
		this.env = env;
		this.appName = env.getProperty("spring.application.name");
		this.idempotentRepository = idempotentRepository;
	}

	private String calculateKey(String method, String path) {
		return method + "#" + path;
	}

	/**
	 * 从FeiClient的配置里面获取是否带有Idempotent标记幂等的方法
	 */
	@Override
	public void parsed(MethodMetadata meta, Class<?> targetType, Method method) {
		FeignClient feignClient = AnnotationUtils.getAnnotation(targetType, FeignClient.class);
		if (feignClient == null) {
			return;
		}
		Idempotent methodIdempotent = AnnotationUtils.getAnnotation(method, Idempotent.class);
		Idempotent classIdempotent = AnnotationUtils.getAnnotation(targetType, Idempotent.class);
		if (methodIdempotent != null || classIdempotent != null) {
			String serviceId = feignClient.name();
			RequestTemplate template = meta.template();
			String path = calculateKey(template.method(), template.url());
			Set<String> resource = IDEMPOTENT_RESOURCE.get(serviceId);
			if (resource != null) {
				resource.add(path);
			} else {
				resource = Sets.newHashSet();
				resource.add(path);
				IDEMPOTENT_RESOURCE.put(serviceId, resource);
			}
		}
	}

	/**
	 * 客户端生成幂等键，并放在请求头中传给服务端
	 */

	@Override
	public void headers(Request request, Options options, Map<String, Collection<String>> headers) {
		if (BooleanUtils.toBoolean(env.getProperty(IDEMOTENCE_TURE_SWITCH))) {
			if (!StringUtils.equalsIgnoreCase(request.httpMethod().name(), "GET")) {
				final String method = request.httpMethod().name();
				final URI requestURI = URI.create(request.url());
				String serviceId = requestURI.getHost();
				String path = requestURI.getRawPath();
				Set<String> resources = IDEMPOTENT_RESOURCE.get(serviceId);
				if (resources != null && resources.contains(calculateKey(method, path))) {
					String requestId = uidGenerator.generate().toString();
					headers.put(INTRA_IDEMPOTENCE_HEADER_KEY, Lists.newArrayList(requestId));
				}
			}
		}
	}

	/**
	 * 服务端校验幂等键，如果校验不通过，直接返回出去，假如有幂等的header的话，否则将不处理
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		String idempotencyKey = request.getHeader(INTRA_IDEMPOTENCE_HEADER_KEY);
		if (idempotencyKey != null && BooleanUtils.toBoolean(env.getProperty(IDEMOTENCE_TURE_SWITCH))) {
			// 如果改请求存在，说明该请求正在处理，幂等直接返回
			// 如果存储幂等键的redis、DB、hazelcast挂掉了，他会返回false，就直接过，这样起码不影响业务，不做拦截
			if (idempotentRepository.exist(appName, idempotencyKey)) {
				response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
				response.setContentType(MediaType.APPLICATION_JSON_VALUE);
				APIResponse<Object> apiResp = APIResponse
						.getErrorJsonResult("idempotency check failed, there is a request is processing ");
				try (PrintWriter out = response.getWriter()) {
					out.print(MyJsonUtil.toJson(apiResp));
				}
				return false;
			} else {
				idempotentRepository.register(appName, idempotencyKey, request.getRequestURI());
				return true;
			}
		}
		return true;
	}

	/**
	 * 请求完成后，清理幂等键
	 */
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		String idempotencyKey = request.getHeader(INTRA_IDEMPOTENCE_HEADER_KEY);
		if (idempotencyKey != null && BooleanUtils.toBoolean(env.getProperty(IDEMOTENCE_TURE_SWITCH))) {
			idempotentRepository.unregister(appName, idempotencyKey);
		}
	}

}
