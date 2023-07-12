package com.binance.platform.ribbon.wrapper;

import java.lang.reflect.Field;

import org.springframework.cloud.client.loadbalancer.LoadBalancedRetryFactory;
import org.springframework.cloud.client.loadbalancer.LoadBalancedRetryPolicy;
import org.springframework.cloud.client.loadbalancer.ServiceInstanceChooser;
import org.springframework.cloud.netflix.ribbon.RibbonLoadBalancedRetryFactory;
import org.springframework.cloud.netflix.ribbon.RibbonLoadBalancerContext;
import org.springframework.cloud.netflix.ribbon.SpringClientFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.retry.RetryListener;
import org.springframework.retry.backoff.BackOffPolicy;
import org.springframework.util.ReflectionUtils;

import com.binance.platform.ribbon.LbRetryConfigProvider;

public class LoadBalancedRetryPolicyFactoryWrapper implements LoadBalancedRetryFactory {

    private static ConfigurableApplicationContext context;

    private SpringClientFactory clientFactory;
    private final LoadBalancedRetryFactory loadBalancedRetryPolicyFactory;

    public LoadBalancedRetryPolicyFactoryWrapper(LoadBalancedRetryFactory loadBalancedRetryPolicyFactory) {
        this.loadBalancedRetryPolicyFactory = loadBalancedRetryPolicyFactory;
        if (loadBalancedRetryPolicyFactory instanceof RibbonLoadBalancedRetryFactory) {
            RibbonLoadBalancedRetryFactory ribbonLoadBalancedRetryPolicyFactory =
                (RibbonLoadBalancedRetryFactory)loadBalancedRetryPolicyFactory;
            Field field = ReflectionUtils.findField(RibbonLoadBalancedRetryFactory.class, "clientFactory");
            ReflectionUtils.makeAccessible(field);
            this.clientFactory =
                (SpringClientFactory)ReflectionUtils.getField(field, ribbonLoadBalancedRetryPolicyFactory);
        } else {
            this.clientFactory = null;
        }
    }

    @Override
    public LoadBalancedRetryPolicy createRetryPolicy(String serviceId, ServiceInstanceChooser loadBalanceChooser) {
        if (this.clientFactory != null) {
            RibbonLoadBalancerContext lbContext = this.clientFactory.getLoadBalancerContext(serviceId);
            return new LoadBalancedRetryPolicyEnhance(serviceId, lbContext, loadBalanceChooser,
                clientFactory.getClientConfig(serviceId));
        } else {
            return loadBalancedRetryPolicyFactory.createRetryPolicy(serviceId, loadBalanceChooser);
        }
    }

    @Override
    public RetryListener[] createRetryListeners(String service) {
        return new RetryListener[0];
    }

    @Override
    public BackOffPolicy createBackOffPolicy(String service) {
        return null;
    }

    public static void setContext(ConfigurableApplicationContext context) {
        LoadBalancedRetryPolicyFactoryWrapper.context = context;
    }

    public static String getCustomizeMaxAutoRetries() {
        try {
            return LoadBalancedRetryPolicyFactoryWrapper.context.getBean(Environment.class)
                .getProperty("ribbon.CustomizeMaxAutoRetries");
        } catch (Exception e) {
            return null;
        }
    }

    public static LbRetryConfigProvider getLbRetryConfigProvider() {
        try {
            return context.getBean(LbRetryConfigProvider.class);
        } catch (Exception e) {
            return null;
        }
    }

}
