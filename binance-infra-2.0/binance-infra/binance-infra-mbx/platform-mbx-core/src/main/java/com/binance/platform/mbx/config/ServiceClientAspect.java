package com.binance.platform.mbx.config;

import com.binance.master.error.GeneralCode;
import com.binance.platform.env.EnvUtil;
import com.binance.platform.mbx.constant.MetricsConstants;
import com.binance.platform.mbx.exception.MbxException;
import com.binance.platform.mbx.monitor.MonitorContext;
import com.binance.platform.mbx.monitor.MonitorContextHolder;
import com.binance.platform.mbx.monitor.MonitorUtil;
import com.binance.platform.mbx.util.ValidationUtil;
import com.binance.platform.monitor.Monitors;
import org.apache.commons.lang3.math.NumberUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * @author Li Fenggang
 * Date: 2020/7/8 8:02 下午
 */
@Aspect
public class ServiceClientAspect {
    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceClientAspect.class);

    @Pointcut("execution(* com.binance.platform.mbx.client.impl.*.*(..))")
    public void mbxSdkEntryPointcut() {}

    @Around("mbxSdkEntryPointcut()")
    public Object doAround(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        long start = System.nanoTime();
        // context
        MonitorContext monitorContext = MonitorContextHolder.get();
        boolean needClearMonitorContext = false;
        if (monitorContext == null) {
            MonitorContextHolder.set(new MonitorContext());
            needClearMonitorContext = true;
        }

        Signature signature = proceedingJoinPoint.getSignature();
        String methodName = signature.getName();
        String declaringTypeName = signature.getDeclaringTypeName();
        // record the request entrance
        MonitorUtil.entrance(methodName);

        Object[] args = proceedingJoinPoint.getArgs();

        MbxException ex = null;
        try {
            // validate the param
            validateParam(args);

            return proceedingJoinPoint.proceed();
        } catch (MbxException e) {
            ex = e;
            throw ex;
        } catch (Exception e) {
            LOGGER.error("Unexpect error", e);
            ex = new MbxException(GeneralCode.SYS_ERROR);
            throw ex;
        } finally {
            long elapseNs = System.nanoTime() - start;
            long elapseMs = TimeUnit.NANOSECONDS.toMillis(elapseNs);
            if (ex == null && elapseMs < NumberUtils.toInt(EnvUtil.getProperty("com.binance.infa.mbx.client-info-log-time", "3000"), 3000)) {
                LOGGER.debug("{}.{}, takes [{} ms]", declaringTypeName, methodName, elapseMs);
            } else if (ex == null && elapseMs < NumberUtils.toInt(EnvUtil.getProperty("com.binance.infa.mbx.client-error-log-time", "10000"), 10000)) {
                LOGGER.info("{}.{}, args:{} takes [{} ms]", declaringTypeName, methodName, getRequest(args), elapseMs);
            } else {
                LOGGER.error("{}.{}, args:{} takes [{} ms]", declaringTypeName, methodName, getRequest(args), elapseMs);
            }

            Monitors.recordTime(MetricsConstants.METRICS_LATENCY_SDK, elapseNs, TimeUnit.NANOSECONDS,
                    MetricsConstants.TAG_NAME_ENTRANCE, methodName,
                    MetricsConstants.TAG_NAME_SPAN, declaringTypeName,
                    MetricsConstants.TAG_NAME_EXCEPTION, MonitorUtil.getExceptionTag(ex));
            if (needClearMonitorContext) {
                MonitorContextHolder.set(null);
            }
        }
    }

    private Object getRequest(Object[] args) {
        Object request = null;
        if (args != null && args.length > 0) {
            request = args[0];
        }
        return request;
    }

    private void validateParam(Object[] args) throws MbxException {
        if (args != null && args.length > 0) {
            Object arg = args[0];
            ValidationUtil.validCheck(arg);
        }
    }
}
