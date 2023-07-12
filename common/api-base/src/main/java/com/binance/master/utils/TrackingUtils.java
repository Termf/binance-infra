package com.binance.master.utils;

import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/***
 * 
 * 此类将在下版本删除，请用此方法替代
 * 
 * 
 */
@Deprecated
public final class TrackingUtils {

	public static final String X_AMZN_TRACE_ID = "x-amzn-trace-id";

	public static final String AWS_TRACE_ROOT = "Root=";

	public static final String TRACE_ID = "traceId";

	public static final String TRACKING_CHAIN = "tracking-chain";

	// type:context (例如: [request:a083188a-aeed-4e8f-a739-67f282b54629])
	public static final String SIMPLE_TEMPLATE = "%s:%s";

	public static final String TYPE_UUID = "uuid";

	public static final String TYPE_SKY_WALKING = "skywalking";

	@Deprecated
	private static boolean containSystemPrefix(String finalTraceId, String traceId) {
		boolean flag = org.apache.commons.lang3.StringUtils.containsAny(traceId, "ExchangeWeb", "BnbWeb", "BnbAdmin",
				"gateway-anonymous", "BinanceMgs", "BnbAnonymous", "pnk-anonymous", "ExchangeAnonym",
				"pnkadmin-anonymous");
		Boolean containFlag = org.apache.commons.lang3.StringUtils.containsOnly(finalTraceId, traceId)
				|| org.apache.commons.lang3.StringUtils.containsOnly(traceId, finalTraceId);
		return flag && !containFlag;
	}

	@Deprecated
	public static void putTracking(final String type, final String context) {
		// 为了兼容老系统两个id都要set
//        String finalTraceId = getTrackingChain();
//        if (StringUtils.isNotBlank(finalTraceId)) {
//            if (containSystemPrefix(finalTraceId, context)) {
//                ThreadContext.put(TRACKING_CHAIN, String.format(SIMPLE_TEMPLATE, context, finalTraceId));
//                ThreadContext.put(TRACE_ID, String.format(SIMPLE_TEMPLATE, context, finalTraceId));
//            } else {
//                ThreadContext.put(TRACKING_CHAIN, wrapAwsTrace(finalTraceId));
//                ThreadContext.put(TRACE_ID, wrapAwsTrace(finalTraceId));
//            }
//        } else {
//            ThreadContext.put(TRACKING_CHAIN, String.format(SIMPLE_TEMPLATE, type, wrapAwsTrace(context)));
//            ThreadContext.put(TRACE_ID, String.format(SIMPLE_TEMPLATE, type, wrapAwsTrace(context)));
//        }
	}

	/**
	 * 追加context。
	 * 
	 * @param context
	 */
	public static void putTracking(final String context) {
//        // 为了兼容老系统两个id都要set
//        String currentTraceIdFromLog4j = getTraceIdFromLog4J();
//        if (StringUtils.containsOnly(currentTraceIdFromLog4j, context)) {
//            String newTraceId = wrapAwsTrace(currentTraceIdFromLog4j);
//            ThreadContext.put(TRACKING_CHAIN, newTraceId);
//            ThreadContext.put(TRACE_ID, newTraceId);
//            return;
//        }
//        if (StringUtils.containsOnly(context, currentTraceIdFromLog4j)) {
//            String newTraceId = wrapAwsTrace(context);
//            ThreadContext.put(TRACKING_CHAIN, newTraceId);
//            ThreadContext.put(TRACE_ID, newTraceId);
//            return;
//        }
//        // 追加context
//        String newTraceId = String.format(SIMPLE_TEMPLATE, context, currentTraceIdFromLog4j);
//        ThreadContext.put(TRACKING_CHAIN, newTraceId);
//        ThreadContext.put(TRACE_ID, newTraceId);

	}

	public static String getTrackingChain() {
		return getTraceIdFromLog4J();
	}

	public static void removeTracking() {
		// removeTraceId();
	}

	/**
	 * 保存traceId给日志使用。
	 */
	public static void saveTraceId() {
//        // 首先从Skywaking中获取
//        String traceId = TraceContext.traceId();
//        String type = TYPE_SKY_WALKING;
//        // 如果为空，则使用UUID
//        if (isNull(traceId)) {
//            traceId = generateUUID();
//            type = TYPE_UUID;
//        }
//        ThreadContext.put(TRACE_ID, String.format(SIMPLE_TEMPLATE, type, traceId));
//        // 兼容老的日志文件
//        ThreadContext.put(TRACKING_CHAIN, String.format(SIMPLE_TEMPLATE, type, traceId));
	}

	public static String getTraceIdFromLog4J() {
		return MDC.get("traceId");
//        String finalTraceId = Strings.EMPTY;
//        if (com.binance.master.utils.StringUtils.isNotBlank(ThreadContext.get(TRACKING_CHAIN))) {
//            finalTraceId = ThreadContext.get(TRACKING_CHAIN);
//        }
//        if (com.binance.master.utils.StringUtils.isNotBlank(ThreadContext.get(TRACE_ID))) {
//            finalTraceId = ThreadContext.get(TRACE_ID);
//        }
//        if (org.apache.commons.lang3.StringUtils.containsIgnoreCase(finalTraceId, "Ignored")
//            || org.apache.commons.lang3.StringUtils.containsIgnoreCase(finalTraceId, "N/A")) {
//            return Strings.EMPTY;
//        }
//        // TRACE_ID 优先级大于TRACKING_CHAIN
//        return finalTraceId;
	}

	public static void removeTraceId() {
//        if (ThreadContext.containsKey(TRACE_ID)) {
//            ThreadContext.remove(TRACE_ID);
//        }
//        if (ThreadContext.containsKey(TRACKING_CHAIN)) {
//            ThreadContext.remove(TRACKING_CHAIN);
//        }
	}

	private static String wrapAwsTrace(String currentTraceId) {
//        if (StringUtils.isNotBlank(currentTraceId) && currentTraceId.contains("AWS")) {
//            return currentTraceId;
//        } else {
//            return currentTraceId + getAwsTrace();
//        }
		return MDC.get("traceId");
	}

//    public static boolean isNull(String traceId) {
//        return StringUtils.isBlank(traceId) || StringUtils.containsIgnoreCase(traceId, "Ignored")
//            || StringUtils.containsIgnoreCase(traceId, "N/A");
//    }

	public static String generateUUID() {
		String s = UUID.randomUUID().toString();
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			builder.append((c == '-') ? "" : c);
		}
		return builder.toString();
	}

//    private static String getAwsTrace() {
//        String awsTrace;
//        String mark = StringUtils.EMPTY;
//        try {
//            HttpServletRequest request =
//                ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
//            awsTrace = request.getHeader(X_AMZN_TRACE_ID);
//        } catch (Exception e) {
//            awsTrace = StringUtils.EMPTY;
//        }
//        if (StringUtils.isNotBlank(awsTrace)) {
//            try {
//                awsTrace = awsTrace.substring(awsTrace.indexOf(AWS_TRACE_ROOT) + 5);
//            } catch (Exception e) {
//                // Do nothing
//            }
//            mark = "-(AWS=" + awsTrace + ")";
//        }
//        return mark;
//    }

}
