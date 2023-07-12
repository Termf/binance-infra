package com.binance.platform.common;

import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.skywalking.apm.toolkit.trace.TraceContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import com.binance.master.constant.Constant;
import com.binance.master.utils.WebUtils;

/**
 * 当开启异步的时候，
 *
 * 1: 先要去父线程里获取traceId，然后塞到子线程的ThreadLocal里面去 2:
 * 先要去父线程获取ServletRequestAttributes，然后塞到子线程的ThreadLocal里面去
 * <p>
 * 使用示例如下：
 *
 * <pre>
 * public static class TestRunnable implements Runnable {
 *
 *     private final String traceId;
 *     private final ServletRequestAttributes servletRequestAttributes;
 *
 *     public TestRunnable() {
 *         traceId = TrackingUtils.getTrace();
 *         servletRequestAttributes = TrackingUtils.getServletRequestAttributes();
 *     }
 *
 *     public void run() {
 *         try {
 *             TrackingUtils.saveTrace(traceId);
 *             TrackingUtils.saveServletRequestAttributes(servletRequestAttributes);
 *             System.out.println(TrackingUtils.getTrace());
 *         } finally {
 *             TrackingUtils.clearTrace();
 *         }
 *     }
 *
 * }
 *
 * Executors.newSingleThreadExecutor().submit(new TestRunnable());
 * </pre>
 */
public class TrackingUtils {

    private static final Logger logger = LoggerFactory.getLogger(TrackingUtils.class);

    public static final String TRACE_ID = "traceId";
    public static final String REQUEST_TIME = "requestTime";
    public static final String TRACE_ID_HEAD = "X-TRACE-ID";
    public static final String TRACE_APP_HEAD = "X-TRACE-APP";
    public static final String SIMPLE_TEMPLATE = "%s:%s";
    public static final String X_AMZN_TRACE_ID = "x-amzn-trace-id";
    public static final String AWS_TRACE_ROOT = "Root=";

    private static final String LOG_TRACE_UUID_PREFIX = "(UUID=";
    private static final String LOG_TRACE_UUID_SUFFIX = ")-";
    private static final String LOG_TRACE_SKY_WALKING_PREFIX = "(SKYWALKING=";
    private static final String LOG_TRACE_SKY_WALKING_SUFFIX = ")-";
    private static final String LOG_TRACE_PINPOINT_PREFIX = "(PinPoint=";
    private static final String LOG_TRACE_PINPOINT_SUFFIX = ")-";


    /**
     * 手动保存traceId
     */
    public static void saveTrace(String traceId) {
        MDC.put(TRACE_ID, traceId);
        RpcContext.getContext().set(TRACE_ID, traceId);
        logger.debug(String.format("receive traceId from upstream services and the traceId is: %s", traceId));
    }

    /**
     * 从请求中获取traceId
     *
     * @param request
     * @return
     */
    public static String getTraceIdFromRequest(HttpServletRequest request) {
        if (WebUtils.isDubboRequest()) {
            return getTraceIdFromDubboRequest();
        }
        String envFlag = request.getHeader(Constant.GRAY_ENV_HEADER);
        String traceId = request.getHeader(TRACE_ID_HEAD);
        String skyWalkingTraceId = TraceContext.traceId();
        String userId = request.getHeader(Constant.HEADER_USER_ID);
        String pinPointTraceId = MDC.get("PtxId");
        // 把UserID保存起来放到日志中
        if (StringUtils.isNotEmpty(userId)) {
            MDC.put(Constant.HEADER_USER_ID, userId);
        } else {
            MDC.put(Constant.HEADER_USER_ID, "N/A");
        }
        if (isNull(traceId)) {
            // 如果请求头里没有,则新建一个UUID
            traceId = LOG_TRACE_UUID_PREFIX + generateUUID() + LOG_TRACE_UUID_SUFFIX;
            if (!isNull(skyWalkingTraceId)) {
                traceId += LOG_TRACE_SKY_WALKING_PREFIX + skyWalkingTraceId + LOG_TRACE_SKY_WALKING_SUFFIX;
            }
            if (!isNull(pinPointTraceId)) {
                traceId += LOG_TRACE_PINPOINT_PREFIX + pinPointTraceId + LOG_TRACE_PINPOINT_SUFFIX;
            }
        }
        // 有非nomal的灰度标签
        if (StringUtils.isNotBlank(envFlag) && !envFlag.equalsIgnoreCase(Constant.ENV_FLAG_NORMAL)
                && !StringUtils.containsIgnoreCase(traceId, envFlag)) {
            traceId = String.format(SIMPLE_TEMPLATE, envFlag, traceId);
        }
        // 添加Aws头部
        traceId = wrapAwsTraceIfNo(traceId, request);
        return traceId;
    }

    /**
     * 从请求中获取traceId
     *
     * @return
     */
    public static String getTraceIdFromDubboRequest() {
        String envFlag = WebUtils.getDubboHeader(Constant.GRAY_ENV_HEADER);
        String traceId = WebUtils.getDubboHeader(TRACE_ID_HEAD);
        String skyWalkingTraceId = TraceContext.traceId();
        String userId = WebUtils.getDubboHeader(Constant.HEADER_USER_ID);
        String pinPointTraceId = MDC.get("PtxId");
        // 把UserID保存起来放到日志中
        if (StringUtils.isNotEmpty(userId)) {
            MDC.put(Constant.HEADER_USER_ID, userId);
        } else {
            MDC.put(Constant.HEADER_USER_ID, "N/A");
        }
        if (isNull(traceId)) {
            // 如果请求头里没有,则新建一个UUID
            traceId = LOG_TRACE_UUID_PREFIX + generateUUID() + LOG_TRACE_UUID_SUFFIX;
            if (!isNull(skyWalkingTraceId)) {
                traceId += LOG_TRACE_SKY_WALKING_PREFIX + skyWalkingTraceId + LOG_TRACE_SKY_WALKING_SUFFIX;
            }
            if (!isNull(pinPointTraceId)) {
                traceId += LOG_TRACE_PINPOINT_PREFIX + pinPointTraceId + LOG_TRACE_PINPOINT_SUFFIX;
            }
        }
        // 有非nomal的灰度标签
        if (StringUtils.isNotBlank(envFlag) && !envFlag.equalsIgnoreCase(Constant.ENV_FLAG_NORMAL)
                && !StringUtils.containsIgnoreCase(traceId, envFlag)) {
            traceId = String.format(SIMPLE_TEMPLATE, envFlag, traceId);
        }
        // 添加Aws头部
        traceId = wrapAwsTraceIfNoFromDubbo(traceId);
        return traceId;
    }

    /**
     * =========日志所需的字段===========
     */
    public static String getRequestTime() {
        String requestTime = MDC.get(REQUEST_TIME);
        if (StringUtils.isBlank(requestTime)) {
            requestTime = RpcContext.getContext().get(REQUEST_TIME);
        }
        return requestTime;
    }

    public static String getPinpointId() {
        return MDC.get("PtxId");
    }

    public static String getAwsId() {
        return MDC.get(X_AMZN_TRACE_ID);
    }

    public static String getUserId() {
        String userId = MDC.get(Constant.HEADER_USER_ID);
        if (StringUtils.isEmpty(userId)) {
            userId = RpcContext.getContext().get(Constant.HEADER_USER_ID);
        }
        return userId;
    }

    /**
     * =========日志所需的字段===========
     */

    /**
     * 手动获取traceId
     */
    public static String getTrace() {
        String traceId = MDC.get(TRACE_ID);
        if (StringUtils.isBlank(traceId)) {
            traceId = RpcContext.getContext().get(TRACE_ID);
        }
        if (StringUtils.isBlank(traceId)) {
            traceId = "AutoGenerate:" + generateUUID();
            saveTrace(traceId);
        }
        return traceId;
    }

    /**
     * 手动删除trace
     */
    public static void clearTrace() {
        MDC.remove(TRACE_ID);
        RpcContext.getContext().remove(TRACE_ID);
    }

    private static boolean isNull(String traceId) {
        return StringUtils.isBlank(traceId) || StringUtils.containsIgnoreCase(traceId, "Ignored") || StringUtils.containsIgnoreCase(traceId, "N/A");
    }

    public static String generateUUID() {
        String s = UUID.randomUUID().toString();
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            builder.append((c == '-') ? "" : c);
        }
        return builder.toString();
    }

    private static String wrapAwsTraceIfNo(String traceId, HttpServletRequest request) {
        if (WebUtils.isDubboRequest()) {
            return wrapAwsTraceIfNoFromDubbo(traceId);
        }
        if (StringUtils.isNotBlank(traceId) && traceId.contains("AWS")) {
            return traceId;
        } else {
            return traceId + getAwsTrace(request);
        }
    }

    private static String wrapAwsTraceIfNoFromDubbo(String traceId) {
        if (StringUtils.isNotBlank(traceId) && traceId.contains("AWS")) {
            return traceId;
        } else {
            return traceId + getAwsTraceFromDubbo();
        }
    }

    private static String getAwsTrace(HttpServletRequest request) {
        if (WebUtils.isDubboRequest()) {
            return getAwsTraceFromDubbo();
        }
        String mark = StringUtils.EMPTY;
        String awsTrace = request.getHeader(X_AMZN_TRACE_ID);
        MDC.put(X_AMZN_TRACE_ID, awsTrace);
        if (StringUtils.isNotBlank(awsTrace)) {
            try {
                awsTrace = awsTrace.substring(awsTrace.indexOf(AWS_TRACE_ROOT) + 5);
            } catch (Exception e) {
                // Do nothing
            }
            mark = "-(AWS=" + awsTrace + ")";
        }
        return mark;
    }

    private static String getAwsTraceFromDubbo() {
        String mark = StringUtils.EMPTY;
        String awsTrace = WebUtils.getDubboHeader(X_AMZN_TRACE_ID);
        MDC.put(X_AMZN_TRACE_ID, awsTrace);
        if (StringUtils.isNotBlank(awsTrace)) {
            try {
                awsTrace = awsTrace.substring(awsTrace.indexOf(AWS_TRACE_ROOT) + 5);
            } catch (Exception e) {
                // Do nothing
            }
            mark = "-(AWS=" + awsTrace + ")";
        }
        return mark;
    }

}
