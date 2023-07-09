package com.binance.platform.ribbon.wrapper;

import java.lang.reflect.Field;

import org.springframework.cloud.client.loadbalancer.LoadBalancedRetryPolicy;
import org.springframework.cloud.client.loadbalancer.LoadBalancedRetryPolicyFactory;
import org.springframework.cloud.client.loadbalancer.ServiceInstanceChooser;
import org.springframework.cloud.netflix.ribbon.RibbonLoadBalancedRetryPolicyFactory;
import org.springframework.cloud.netflix.ribbon.RibbonLoadBalancerContext;
import org.springframework.cloud.netflix.ribbon.SpringClientFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.util.ReflectionUtils;

import com.binance.platform.ribbon.LbRetryConfigProvider;

public class LoadBalancedRetryPolicyFactoryWrapper implements LoadBalancedRetryPolicyFactory {

    private static ConfigurableApplicationContext context;

    private SpringClientFactory clientFactory;
    private final LoadBalancedRetryPolicyFactory loadBalancedRetryPolicyFactory;

    public LoadBalancedRetryPolicyFactoryWrapper(LoadBalancedRetryPolicyFactory loadBalancedRetryPolicyFactory) {
        this.loadBalancedRetryPolicyFactory = loadBalancedRetryPolicyFactory;
        if (loadBalancedRetryPolicyFactory instanceof RibbonLoadBalancedRetryPolicyFactory) {
            RibbonLoadBalancedRetryPolicyFactory ribbonLoadBalancedRetryPolicyFactory =
                (RibbonLoadBalancedRetryPolicyFactory)loadBalancedRetryPolicyFactory;
            Field field = ReflectionUtils.findField(RibbonLoadBalancedRetryPolicyFactory.class, "clientFactory");
            ReflectionUtils.makeAccessible(field);
            this.clientFactory =
                (SpringClientFactory)ReflectionUtils.getField(field, ribbonLoadBalancedRetryPolicyFactory);
        } else {
            this.clientFactory = null;
        }
    }

    @Override
    public LoadBalancedRetryPolicy create(String serviceId, ServiceInstanceChooser loadBalanceChooser) {
        if (this.clientFactory != null) {
            RibbonLoadBalancerContext lbContext = this.clientFactory.getLoadBalancerContext(serviceId);
            return new LoadBalancedRetryPolicyEnhance(serviceId, lbContext, loadBalanceChooser,
                clientFactory.getClientConfig(serviceId));
        } else {
            return loadBalancedRetryPolicyFactory.create(serviceId, loadBalanceChooser);
        }
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
