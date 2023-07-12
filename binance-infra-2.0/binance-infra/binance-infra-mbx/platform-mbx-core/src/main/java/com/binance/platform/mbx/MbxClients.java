package com.binance.platform.mbx;

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

/**
 * mbx service client
 *
 * @author Li Fenggang
 * Date: 2020/7/8 5:52 下午
 */
public class MbxClients {
    private final AccountClient accountClient;
    private final DepthClient depthClient;
    private final KlineClient klineClient;
    private final MatchBoxClient matchBoxClient;
    private final OrderClient orderClient;
    private final PriceClient priceClient;
    private final ProductClient productClient;
    private final TradeClient tradeClient;
    private final TradeRuleClient tradeRuleClient;
    private final UserStreamClient userStreamClient;

    public MbxClients(AccountClient accountClient, DepthClient depthClient,
                      KlineClient klineClient, MatchBoxClient matchBoxClient, OrderClient orderClient,
                      PriceClient priceClient, ProductClient productClient, TradeClient tradeClient,
                      TradeRuleClient tradeRuleClient, UserStreamClient userStreamClient) {
        this.accountClient = accountClient;
        this.depthClient = depthClient;
        this.klineClient = klineClient;
        this.matchBoxClient = matchBoxClient;
        this.orderClient = orderClient;
        this.priceClient = priceClient;
        this.productClient = productClient;
        this.tradeClient = tradeClient;
        this.tradeRuleClient = tradeRuleClient;
        this.userStreamClient = userStreamClient;
    }

    public AccountClient getAccountClient() {
        return accountClient;
    }

    public DepthClient getDepthClient() {
        return depthClient;
    }

    public KlineClient getKlineClient() {
        return klineClient;
    }

    public MatchBoxClient getMatchBoxClient() {
        return matchBoxClient;
    }

    public OrderClient getOrderClient() {
        return orderClient;
    }

    public PriceClient getPriceClient() {
        return priceClient;
    }

    public ProductClient getProductClient() {
        return productClient;
    }

    public TradeClient getTradeClient() {
        return tradeClient;
    }

    public TradeRuleClient getTradeRuleClient() {
        return tradeRuleClient;
    }

    public UserStreamClient getUserStreamClient() {
        return userStreamClient;
    }
}
