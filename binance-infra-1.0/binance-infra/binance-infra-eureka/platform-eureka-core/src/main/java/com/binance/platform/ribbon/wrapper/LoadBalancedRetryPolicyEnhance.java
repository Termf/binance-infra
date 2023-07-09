package com.binance.platform.ribbon.wrapper;

import java.lang.reflect.Field;
import java.net.ConnectException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.List;
import java.util.Set;

import org.apache.http.ConnectionClosedException;
import org.apache.http.NoHttpResponseException;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.ConnectionPoolTimeoutException;
import org.apache.http.conn.HttpHostConnectException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.client.loadbalancer.LoadBalancedRetryContext;
import org.springframework.cloud.client.loadbalancer.LoadBalancedRetryPolicy;
import org.springframework.cloud.client.loadbalancer.ServiceInstanceChooser;
import org.springframework.cloud.netflix.ribbon.RibbonLoadBalancedRetryPolicy;
import org.springframework.cloud.netflix.ribbon.RibbonLoadBalancerContext;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpRequest;
import org.springframework.util.ReflectionUtils;

import com.binance.platform.ribbon.LbRetryConfigProvider;
import com.google.common.collect.Lists;
import com.netflix.client.Utils;
import com.netflix.client.config.IClientConfig;

/**
 * <pre>
   注意：ribbon的重试如果打开了，则feign的重试没必要打开，不然会产生多次重试的问题
   
   ribbon:
     # 服务最大重试次数,不包含第一次请求
     MaxAutoRetries: 1
     # 负载均衡切换次数,如果服务注册列表小于 nextServer count 那么会循环请求  A > B >　A
     MaxAutoRetriesNextServer: 1
     # 是否所有操作都进行重试
     OkToRetryOnAllOperations: false   //如果开启了幂等的注解，该值是可以为true的，否则的话，要慎重开启
 * </pre>
 * 
 * 这样配置是：总共重试3次，包括有一次没计数的
 */
public class LoadBalancedRetryPolicyEnhance extends RibbonLoadBalancedRetryPolicy {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoadBalancedRetryPolicy.class);

    // 默认重试最大次数
    private int MAX_RETRY_TIMES = 3;

    private Set<String> retryConfig;

    private Field sameServerCountField =
        ReflectionUtils.findField(LoadBalancedRetryPolicyEnhance.class.getSuperclass(), "sameServerCount");

    private Field nextServerCountField =
        ReflectionUtils.findField(LoadBalancedRetryPolicyEnhance.class.getSuperclass(), "nextServerCount");

    {
        ReflectionUtils.makeAccessible(sameServerCountField);
        ReflectionUtils.makeAccessible(nextServerCountField);
    }

    /**
     * 服务端没有收到请求
     * 
     * <pre>
     * ConnectException：连接异常 
     * ConnectTimeoutException：连接超时异常 
     * NoHttpResponseException：当服务器端由于负载过大等情况发生时，可能会导致在收到请求后无法处理(比如没有足够的线程资源)，会直接丢弃链接而不进行处理
     * ConnectionPoolTimeoutException:连接池超时
     * HttpHostConnectException: 域名解析失败
     * </pre>
     */
    protected final List<Class<? extends Throwable>> SERVER_HAVE_NOT_RECEIVE =
        Lists.<Class<? extends Throwable>>newArrayList(ConnectException.class, ConnectTimeoutException.class,
            NoHttpResponseException.class, ConnectionPoolTimeoutException.class, HttpHostConnectException.class,
            SocketException.class);
    /**
     * 服务端已经收到请求
     * 
     * <pre>
     * SocketTimeoutException ： 服务端处理慢 ，服务端性能低下
     * ConnectionClosedException ： 服务端已经处理完了，客户端处理有问题
     * </pre>
     */
    protected final List<Class<? extends Throwable>> SERVER_HAVE_RECEIVE =
        Lists.<Class<? extends Throwable>>newArrayList(SocketTimeoutException.class, ConnectionClosedException.class);

    public LoadBalancedRetryPolicyEnhance(String serviceId, RibbonLoadBalancerContext context,
        ServiceInstanceChooser loadBalanceChooser, IClientConfig clientConfig) {
        super(serviceId, context, loadBalanceChooser, clientConfig);
        String max_retry_times = LoadBalancedRetryPolicyFactoryWrapper.getCustomizeMaxAutoRetries();
        MAX_RETRY_TIMES = max_retry_times != null ? Integer.valueOf(max_retry_times) : 3;
    }

    /**
     * 扩展retry条件 1: 遵循原有的retry逻辑 2: 当非Get请求时，看是是网络方面的异常 3: 当用户配置了指定的HTTP接口
     * 
     * 重试逻辑是： 先重试当前节点，次数达到后，再重试下一次节点
     */
    @Override
    public boolean canRetry(LoadBalancedRetryContext context) {
        // 首先看下是否是get请求
        boolean springCloudRetry = super.canRetry(context);
        if (springCloudRetry) {
            return true;
        } else {
            Integer sameServerCount = (Integer)ReflectionUtils.getField(sameServerCountField, this);
            Integer nextServerCount = (Integer)ReflectionUtils.getField(nextServerCountField, this);
            if (sameServerCount >= MAX_RETRY_TIMES) {
                LOGGER.debug("RetrySameServer maximum number of times exceeded,the count is {},the maxcount is {}",
                    sameServerCount, MAX_RETRY_TIMES);
                return false;
            }
            if (nextServerCount >= MAX_RETRY_TIMES) {
                LOGGER.debug("RetryNextServer maximum number of times exceeded,the count is {},the maxcount is {}",
                    sameServerCount, MAX_RETRY_TIMES);
                return false;
            }
            if ((sameServerCount + nextServerCount) >= MAX_RETRY_TIMES) {
                LOGGER.debug(
                    "RetryNextServer+ RetrySameServer maximum number of times exceeded,the count is {},the maxcount is {}",
                    sameServerCount + nextServerCount, MAX_RETRY_TIMES);
                return false;
            }

            Throwable exception = context.getLastThrowable();
            HttpMethod method = context.getRequest().getMethod();
            // 如果服务端没有收到该请求
            if (Utils.isPresentAsCause(exception, SERVER_HAVE_NOT_RECEIVE)) {
                return true;
            } else {
                // 再看下是否对路径做了配置
                Set<String> retryConfig = this.getRetryConfig();
                if (retryConfig != null) {
                    HttpRequest httpRequest = context.getRequest();
                    String path = httpRequest.getURI().getPath();
                    final String pathKey = this.calculateKey(path, method);
                    Boolean retryConfiged = retryConfig.contains(pathKey);
                    if (retryConfiged) {
                        return true;
                    } else {
                        return false;
                    }
                } else {
                    return false;
                }
            }
        }
    }

    @Override
    public boolean canRetrySameServer(LoadBalancedRetryContext context) {
        Throwable exception = context.getLastThrowable();
        // 如果是网络相关的异常，表明当前该节点的确不可用，用下一个节点来重试
        if (Utils.isPresentAsCause(exception, SERVER_HAVE_NOT_RECEIVE)) {
            return false;
        } else {
            boolean canRetry = super.canRetrySameServer(context);
            if (!canRetry) {
                canRetry = this.canRetry(context);
            }
            if (canRetry) {
                LOGGER.debug("Ribbon will retrySameServer,currentInstance:{}, HttpMethod:{}, exception:{}",
                    context.getServiceInstance().getHost(), context.getRequest().getMethod().name(),
                    context.getLastThrowable());
            }
            return canRetry;
        }
    }

    @Override
    public boolean canRetryNextServer(LoadBalancedRetryContext context) {
        boolean canRetry = super.canRetryNextServer(context);
        if (!canRetry) {
            canRetry = this.canRetry(context);
        }
        if (canRetry) {
            LOGGER.debug("Ribbon will retryNextServer,currentInstance:{}, HttpMethod:{}, exception:{}",
                context.getServiceInstance().getHost(), context.getRequest().getMethod(), context.getLastThrowable());
        }
        return canRetry;
    }

    @Override
    public void close(LoadBalancedRetryContext context) {
        super.close(context);
    }

    @Override
    public void registerThrowable(LoadBalancedRetryContext context, Throwable throwable) {
        super.registerThrowable(context, throwable);
    }

    @Override
    public boolean retryableStatusCode(int statusCode) {
        return super.retryableStatusCode(statusCode);
    }

    /** Help Method ***/

    private Set<String> getRetryConfig() {
        if (retryConfig == null) {
            LbRetryConfigProvider provider = LoadBalancedRetryPolicyFactoryWrapper.getLbRetryConfigProvider();
            this.retryConfig = provider != null ? provider.retryPathConfig() : null;
        }
        return this.retryConfig;
    }

    private String calculateKey(String path, HttpMethod method) {
        return path + method.name();
    }

}
