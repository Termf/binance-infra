package com.binance.platform.monitor.logging.aop;

import java.lang.annotation.Annotation;
import java.util.concurrent.TimeUnit;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import com.binance.platform.monitor.RequestUtil;

import io.micrometer.core.instrument.Metrics;
import io.micrometer.core.instrument.Tag;
import io.micrometer.core.instrument.Tags;

@Aspect
public class GenericControllerAspect extends MonitorAspect implements ControllerAspect {

	private final Logger LOG;

	private final RequestUtil requestUtil;

	public GenericControllerAspect() {
		this(org.slf4j.LoggerFactory.getLogger(GenericControllerAspect.class), new RequestUtil());
	}

	public GenericControllerAspect(Logger LOG, RequestUtil requestUtil) {
		this.LOG = LOG;
		this.requestUtil = requestUtil;
	}

	@Override
	@Pointcut("@annotation(com.binance.platform.monitor.logging.aop.Monitor) || @within(com.binance.platform.monitor.logging.aop.Monitor)")
	public void methodOrClassMonitorEnabledPointcut() {
	}

	@Override
	@AfterThrowing(pointcut = "methodOrClassMonitorEnabledPointcut()", throwing = "t")
	public void onException(JoinPoint joinPoint, Throwable t) {
		String methodName = joinPoint.getSignature().getName() + "()";
		LOG.info(methodName + " threw exception: [" + t + "]");
	}

	@Override
	@Around("methodOrClassMonitorEnabledPointcut()")
	public Object log(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
		Object result = null;
		String returnType = null;
		Throwable throwable = null;
		boolean needLog = true;
		MethodSignature methodSignature = (MethodSignature) proceedingJoinPoint.getSignature();
		if (methodSignature.getMethod().getAnnotation(DisableMonitor.class) != null
				|| proceedingJoinPoint.getTarget().getClass().getAnnotation(DisableMonitor.class) != null) {
			needLog = false;
		}
		RequestMapping methodRequestMapping = null;
		RequestMapping classRequestMapping = null;
		try {
			methodRequestMapping = methodSignature.getMethod().getAnnotation(RequestMapping.class);
			classRequestMapping = proceedingJoinPoint.getTarget().getClass().getAnnotation(RequestMapping.class);
			returnType = methodSignature.getReturnType().getName();
			if (needLog) {
				logPreExecutionData(proceedingJoinPoint, methodRequestMapping);
			}
		} catch (Throwable e) {
			LOG.error("Exception occurred in pre-proceed logic");
		}
		StopWatch timer = new StopWatch();
		try {
			timer.start();
			result = proceedingJoinPoint.proceed();
		} catch (Throwable t) {
			throwable = t;
			throw t;
		} finally {
			timer.stop();
			if (needLog) {
				if (returnType != null) {
					try {
						logPostExecutionData(proceedingJoinPoint, timer, result, returnType, methodRequestMapping,
								classRequestMapping, throwable);
					} catch (Throwable e) {
						LOG.error("Exception occurred in after-proceed logic");
					}
				}
			}
		}
		return result;
	}

	private void logFunctionArguments(String[] argNames, Object[] argValues, StringBuilder stringBuilder,
			Annotation annotations[][], RequestMapping methodRequestMapping) {
		boolean someArgNeedsSerialization = false;
		if (methodRequestMapping != null) {
			for (String consumes : methodRequestMapping.consumes()) {
				if (consumes.equals(MediaType.APPLICATION_JSON_VALUE)) {
					someArgNeedsSerialization = true;
					break;
				}
			}
		}
		stringBuilder.append(" called with arguments: ");
		for (int i = 0, length = argNames.length; i < length; ++i) {
			boolean needsSerialization = false;

			if (argValues[i] instanceof ByteArrayResource || argValues[i] instanceof MultipartFile) {
				needsSerialization = true;
			} else {
				if (someArgNeedsSerialization) {
					for (Annotation annotation : annotations[i]) {
						if (annotation instanceof RequestBody) {
							needsSerialization = true;
							break;
						}
					}
				}
			}
			stringBuilder.append(argNames[i]).append(": [");
			if (needsSerialization) {
				String argClassName = argValues[i] == null ? "NULL" : argValues[i].getClass().getName();
				serialize(argValues[i], argClassName, stringBuilder);
			} else {
				stringBuilder.append(getScrubbedValue(argNames[i], argValues[i]));
			}
			stringBuilder.append("]").append(i == (length - 1) ? "" : ", ");
		}
	}

	@Override
	public void logPostExecutionData(ProceedingJoinPoint proceedingJoinPoint, StopWatch timer, Object result,
			String returnType, RequestMapping methodRequestMapping, RequestMapping classRequestMapping,
			Throwable throwable) {
		MethodSignature methodSignature = (MethodSignature) proceedingJoinPoint.getSignature();
		String calssName = proceedingJoinPoint.getTarget().getClass().getSimpleName();
		String methodName = calssName + "." + methodSignature.getName() + "()";
		LOG.info(methodName + " took [" + timer.getTotalTimeMillis() + " ms] to complete");
		Tags tags = Tags.of(Tag.of("method", methodName)).and("url", requestUtil.getCurrentRequestUrl())
				.and("exception", throwable == null ? "None" : throwable.getMessage());
		Metrics.timer("method_with_url_timer", tags).record(timer.getTotalTimeMillis(), TimeUnit.MILLISECONDS);
		Metrics.counter("method_with_url_count", tags).increment();
		boolean needsSerialization = false;
		String produces[] = methodRequestMapping != null ? methodRequestMapping.produces() : new String[0];
		for (String produce : produces) {
			if (produce.equals(MediaType.APPLICATION_JSON_VALUE)) {
				needsSerialization = true;
				break;
			}
		}
		if (!needsSerialization) {
			produces = classRequestMapping != null ? classRequestMapping.produces() : new String[0];
			for (String produce : produces) {
				if (produce.equals(MediaType.APPLICATION_JSON_VALUE)) {
					needsSerialization = true;
					break;
				}
			}
		}
		StringBuilder postMessage = new StringBuilder().append(methodName).append(" returned: [");
		if (needsSerialization) {
			String resultClassName = result == null ? "null" : result.getClass().getName();
			resultClassName = returnType.equals("java.lang.Void") ? returnType : resultClassName;
			serialize(result, resultClassName, postMessage);
		} else {
			postMessage.append(result);
		}
		postMessage.append("]");
		// LOG.info(postMessage.toString());
	}

	@Override
	public void logPreExecutionData(ProceedingJoinPoint proceedingJoinPoint, RequestMapping methodRequestMapping) {
		MethodSignature methodSignature = (MethodSignature) proceedingJoinPoint.getSignature();
		String calssName = proceedingJoinPoint.getTarget().getClass().getSimpleName();
		String methodName = calssName + "." + methodSignature.getName() + "()";
		Object argValues[] = proceedingJoinPoint.getArgs();
		String argNames[] = methodSignature.getParameterNames();
		String requestContext = requestUtil.getRequestContext().toString();
		Annotation annotations[][] = methodSignature.getMethod().getParameterAnnotations();
		StringBuilder preMessage = new StringBuilder().append(methodName);
		if (argValues != null && argValues.length > 0 && argNames != null && argNames.length > 0) {
			logFunctionArguments(argNames, argValues, preMessage, annotations, methodRequestMapping);
		}
		preMessage.append(" called via ").append(requestContext);
		// LOG.info(preMessage.toString());
	}

	@Override
	public void serialize(Object object, String objClassName, StringBuilder logMessage) {
		boolean serializedSuccessfully = false;
		Exception exception = null;
		if (objClassName.equals("java.lang.Void")) {
			logMessage.append("void");
			serializedSuccessfully = true;
		}
		if (!serializedSuccessfully) {
			try {
				logMessage.append(String.valueOf(object));
				serializedSuccessfully = true;
			} catch (Exception e) {
				exception = e;
			}
		}
		if (!serializedSuccessfully) {
			long fileSize = -1;

			if (object instanceof ByteArrayResource) {
				fileSize = ((ByteArrayResource) object).contentLength();
			} else if (object instanceof MultipartFile) {
				fileSize = ((MultipartFile) object).getSize();
			}

			if (fileSize != -1) {
				logMessage.append("file of size:[").append(fileSize).append(" B]");
				serializedSuccessfully = true;
			}
		}
		if (!serializedSuccessfully && objClassName.toLowerCase().contains("mock")) {
			logMessage.append("Mock Object");
			serializedSuccessfully = true;
		}

		if (!serializedSuccessfully) {
			LOG.warn("Unable to serialize object of type [" + objClassName + "] for logging", exception);
		}
	}

	private Object getScrubbedValue(String argName, Object argValue) {
		Object argValueToUse = argValue;
		if (enableDataScrubbing) {
			if (paramBlacklist.contains(argName.toLowerCase())
					|| (paramBlacklistRegex != null && paramBlacklistRegex.matcher(argName).matches())) {
				argValueToUse = scrubbedValue;
			}
		}
		return argValueToUse;
	}

}
