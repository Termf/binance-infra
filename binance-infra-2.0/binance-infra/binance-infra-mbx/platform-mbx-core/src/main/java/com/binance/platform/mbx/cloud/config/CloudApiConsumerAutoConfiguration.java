package com.binance.platform.mbx.cloud.config;

import com.binance.platform.mbx.cloud.consumer.BaseDataConsumer;
import com.binance.platform.mbx.cloud.consumer.CountryApiConsumer;
import com.binance.platform.mbx.cloud.consumer.OrderApiConsumer;
import com.binance.platform.mbx.cloud.consumer.ProductApiConsumer;
import com.binance.platform.mbx.cloud.consumer.SubAccountApiConsumer;
import com.binance.platform.mbx.cloud.consumer.UserApiConsumer;
import com.binance.platform.mbx.cloud.rpc.CloudApiCaller;
import com.binance.platform.mbx.cloud.rpc.impl.DiscoverableCloudApiCaller;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Li Fenggang
 * Date: 2020/7/13 7:15 下午
 */
@Configuration
public class CloudApiConsumerAutoConfiguration {

    @Bean
    public CloudApiCaller mbxCloudApiCaller(DiscoveryClient discoveryClient) {
        DiscoverableCloudApiCaller cloudApiCaller = new DiscoverableCloudApiCaller(discoveryClient);
        return cloudApiCaller;
    }

    @Bean
    public BaseDataConsumer mbxBaseDataConsumer(CloudApiCaller cloudApiCaller) {
        BaseDataConsumer baseDataConsumer = new BaseDataConsumer(cloudApiCaller);
        return baseDataConsumer;
    }

    @Bean
    public CountryApiConsumer mbxCountryConsumer(CloudApiCaller cloudApiCaller) {
        CountryApiConsumer countryApiConsumer = new CountryApiConsumer(cloudApiCaller);
        return countryApiConsumer;
    }

    @Bean
    public OrderApiConsumer mbxOrderConsumer(CloudApiCaller cloudApiCaller) {
        OrderApiConsumer orderApiConsumer = new OrderApiConsumer(cloudApiCaller);
        return orderApiConsumer;
    }

    @Bean
    public ProductApiConsumer mbxProductConsumer(CloudApiCaller cloudApiCaller) {
        ProductApiConsumer productApiConsumer = new ProductApiConsumer(cloudApiCaller);
        return productApiConsumer;
    }

    @Bean
    public SubAccountApiConsumer mbxSubAccountConsumer(CloudApiCaller cloudApiCaller) {
        SubAccountApiConsumer subAccountApiConsumer = new SubAccountApiConsumer(cloudApiCaller);
        return subAccountApiConsumer;
    }

    @Bean
    public UserApiConsumer mbxUserApiConsumer(CloudApiCaller cloudApiCaller) {
        UserApiConsumer userApiConsumer = new UserApiConsumer(cloudApiCaller);
        return userApiConsumer;
    }
}