package com.binance.platform;

import java.io.Serializable;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.javasimon.Manager;
import org.javasimon.SimonManager;
import org.javasimon.Split;
import org.javasimon.source.StopwatchSource;
import org.javasimon.spring.SpringStopwatchSource;

import lombok.extern.log4j.Log4j2;
import org.javasimon.utils.SimonUtils;

/**
 * @author james.li
 */
@Log4j2
public final class MonitoringInterceptor implements MethodInterceptor, Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = -5447240050039980605L;

    public static final String EXCEPTION_TAG = "failed";
    
    private final int costTimeThresholdMS;

    private boolean tagByExceptionType;

    private final StopwatchSource<MethodInvocation> stopwatchSource;

    /**
     * Constructor with specified {@link org.javasimon.source.MonitorSource}.
     *
     * @param costTimeThresholdMS
     * @param stopwatchSource stopwatch provider for method invocation
     */
    public MonitoringInterceptor(int costTimeThresholdMS, StopwatchSource<MethodInvocation> stopwatchSource) {
        this.costTimeThresholdMS = costTimeThresholdMS;
        this.stopwatchSource = stopwatchSource;
    }

    /** Constructor with specified {@link org.javasimon.Manager}. */
    public MonitoringInterceptor(Manager manager) {
        this(1000, new SpringStopwatchSource(manager));
    }

    /** Default constructor using {@link org.javasimon.SimonManager#manager}. */
    public MonitoringInterceptor(int costTimeThresholdMS) {
        this(costTimeThresholdMS, new SpringStopwatchSource(SimonManager.manager()));
    }

    /**
     * Sets whether all exceptions should report to {@link #EXCEPTION_TAG} sub-simon or sub-simon for
     * each exception type should be introduced (based on exception's simple name).
     *
     * @param tagByExceptionType {@code true} for fine grained exception-class-name-based sub-simons
     */
    public void setTagByExceptionType(boolean tagByExceptionType) {
        this.tagByExceptionType = tagByExceptionType;
    }

    /**
     * Performs method invocation and wraps it with Stopwatch.
     *
     * @param invocation method invocation
     * @return return object from the method
     * @throws Throwable anything thrown by the method
     */
    public final Object invoke(MethodInvocation invocation) throws Throwable {
        final Split split = stopwatchSource.start(invocation);
        try {
            return processInvoke(invocation, split);
        } finally {
            split.stop();
            // log.info("Method execution time: {}", split.toString());
            long total = split.runningFor();
            if (total > costTimeThresholdMS * 1000000L) {
                // 大于指定时间打印warning日志
                log.warn("Method '{}' execute more than {}ms: {}", invocation.getMethod().getName(), costTimeThresholdMS, SimonUtils.presentNanoTime(total));
            }
        }
    }

    /**
     * Method stops the split
     *
     * @param invocation method invocation
     * @param split running split for this monitored action
     * @return return object from the method
     */
    protected Object processInvoke(MethodInvocation invocation, Split split) throws Throwable {
        try {
            return invocation.proceed();
        } catch (Throwable t) {
            split.stop(tagByExceptionType ? t.getClass().getSimpleName() : EXCEPTION_TAG);
            throw t;
        }
    }
}
