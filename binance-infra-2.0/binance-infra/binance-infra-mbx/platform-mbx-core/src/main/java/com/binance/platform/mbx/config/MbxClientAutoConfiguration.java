package com.binance.platform.mbx.config;

import com.binance.platform.mbx.MbxClients;
import com.binance.platform.mbx.client.AccountClient;
import com.binance.platform.mbx.client.DepthClient;
import com.binance.platform.mbx.client.KlineClient;
import com.binance.platform.mbx.client.MatchBoxClient;
import com.binance.platform.mbx.client.OrderClient;
import com.binance.platform.mbx.client.PriceClient;
import com.binance.platform.mbx.client.ProductClient;
import com.binance.platform.mbx.client.TradeClient;
import com.binance.platform.mbx.client.TradeRuleClient;
import com.binance.platform.mbx.client.UserStreamClient;
import com.binance.platform.mbx.client.impl.AccountClientImpl;
import com.binance.platform.mbx.client.impl.DepthClientImpl;
import com.binance.platform.mbx.client.impl.KlineClientImpl;
import com.binance.platform.mbx.client.impl.MatchBoxClientImpl;
import com.binance.platform.mbx.client.impl.OrderClientImpl;
import com.binance.platform.mbx.client.impl.PriceClientImpl;
import com.binance.platform.mbx.client.impl.ProductClientImpl;
import com.binance.platform.mbx.client.impl.TradeClientImpl;
import com.binance.platform.mbx.client.impl.TradeRuleClientImpl;
import com.binance.platform.mbx.client.impl.UserStreamClientImpl;
import com.binance.platform.mbx.cloud.consumer.BaseDataConsumer;
import com.binance.platform.mbx.cloud.consumer.ProductApiConsumer;
import com.binance.platform.mbx.cloud.consumer.UserApiConsumer;
import com.binance.platform.mbx.matchbox.MatchBoxManagementService;
import com.binance.platform.mbx.matchbox.MatchBoxRestService;
import com.binance.platform.mbx.service.AccountService;
import com.binance.platform.mbx.service.OrderService;
import com.binance.platform.mbx.service.ProductService;
import com.binance.platform.mbx.service.impl.AccountServiceImpl;
import com.binance.platform.mbx.service.impl.OrderServiceImpl;
import com.binance.platform.mbx.service.impl.ProductServiceImpl;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

/**
 * @author Li Fenggang
 * Date: 2020/7/6 9:46 上午
 */
@Configuration
@EnableConfigurationProperties(MbxCustomProperties.class)
public class MbxClientAutoConfiguration {

    @PostConstruct
    public void init() {
        MbxPropertiesHolder.init();
    }

    // service
    @Bean
    public AccountService mbxAccountService(UserApiConsumer userApiConsumer, MbxCustomProperties mbxCustomProperties) {
        AccountService accountService = new AccountServiceImpl(userApiConsumer, mbxCustomProperties);
        return accountService;
    }

    @Bean
    public OrderService mbxOrderService() {
        return new OrderServiceImpl();
    }

    @Bean
    public ProductService mbxProductService(ProductApiConsumer productApiConsumer) {
        return new ProductServiceImpl(productApiConsumer);
    }

    @Bean
    public AccountClient mbxAccountClient(MatchBoxManagementService matchBoxManagementService, AccountService accountService, SysConfigService sysConfigService) {
        AccountClientImpl accountClient = new AccountClientImpl(matchBoxManagementService, accountService, sysConfigService);
        return accountClient;
    }

    @Bean
    public DepthClient mbxDepthClientImpl(MatchBoxRestService matchBoxRestService) {
        DepthClientImpl depthClient = new DepthClientImpl(matchBoxRestService);
        return depthClient;
    }

    @Bean
    public KlineClient mbxKlineClient(MatchBoxRestService matchBoxRestService) {
        KlineClientImpl klineClient = new KlineClientImpl(matchBoxRestService);
        return klineClient;
    }

    @Bean
    public MatchBoxClient matchBoxClient(MatchBoxManagementService matchBoxManagementService) {
        MatchBoxClientImpl matchBoxClient = new MatchBoxClientImpl(matchBoxManagementService);
        return matchBoxClient;
    }

    @Bean
    public OrderClient mbxOrderClient(OrderService orderService) {
        OrderClientImpl orderClient = new OrderClientImpl(orderService);
        return orderClient;
    }

    @Bean
    public PriceClient mbxPriceClient(MatchBoxRestService matchBoxRestService) {
        PriceClientImpl priceClient = new PriceClientImpl(matchBoxRestService);
        return priceClient;
    }

    @Bean
    public ProductClient mbxProductClient(MatchBoxManagementService matchBoxManagementService,
                                          ProductApiConsumer productApi) {
        ProductClientImpl productClient = new ProductClientImpl(matchBoxManagementService, productApi);
        return productClient;
    }

    @Bean
    public TradeClient mbxTradeClient(ProductService productService,
                                      MatchBoxRestService matchBoxRestService) {
        TradeClient tradeClient = new TradeClientImpl(productService, matchBoxRestService);
        return tradeClient;
    }

    @Bean
    public TradeRuleClient mbxTradeRuleClient(MatchBoxManagementService matchBoxManagementService, ProductService productService) {
        TradeRuleClientImpl tradeRuleClient = new TradeRuleClientImpl(matchBoxManagementService, productService);
        return tradeRuleClient;
    }

    @Bean
    public UserStreamClient mbxUserStreamClient(MatchBoxManagementService matchBoxManagementService,
                                                SysConfigService sysConfigService,
                                                AccountService accountService) {
        UserStreamClientImpl userStreamClient = new UserStreamClientImpl(matchBoxManagementService, sysConfigService,
                accountService);
        return userStreamClient;
    }

    @Bean
    public MbxClients mbxServiceClient(AccountClient accountClient, DepthClient depthClient,
                                       KlineClient klineClient, MatchBoxClient matchBoxClient, OrderClient orderClient,
                                       PriceClient priceClient, ProductClient productClient, TradeClient tradeClient,
                                       TradeRuleClient tradeRuleClient, UserStreamClient userStreamClient) {
        return new MbxClients(accountClient, depthClient, klineClient, matchBoxClient, orderClient,
                priceClient, productClient, tradeClient, tradeRuleClient, userStreamClient);
    }

    @Bean
    public ServiceClientAspect mbxServiceClientAspect() {
        return new ServiceClientAspect();
    }

    @Bean
    public SysConfigService mbxSysConfigService(BaseDataConsumer baseDataConsumer) {
        return new SysConfigService(baseDataConsumer);
    }

}
