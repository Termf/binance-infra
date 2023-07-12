package com.binance.platform.mbx.client;

import com.binance.platform.mbx.matchbox.model.MbxResponse;
import com.binance.platform.mbx.model.traderule.QueryAssetGasRequest;
import com.binance.platform.mbx.model.traderule.QueryUserProductFeeRequest;
import com.binance.platform.mbx.model.traderule.RefreshTradingRuleRequest;
import com.binance.platform.mbx.model.traderule.SetFeeRequest;
import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author Li Fenggang
 * Date: 2020/7/20 8:45 上午
 */
//@RunWith(SpringRunner.class)
//@SpringBootTest(classes = Application.class)
public class TradeRuleClientTest {
    private static final String DEFAULT_ASSET = "BTC";
    private static final String DEFAULT_SYMBOL = "BTCUSDT";
    private static final long DEFAULT_ACCOUNT_ID = 6;
    private static final long DEFAULT_USER_ID = 350788204;
    private static final Date DEFAULT_START_TIME = new Date(1);
    private static final Date DEFAULT_END_TIME = new Date();
    private static final long DEFAULT_START_TIME_LONG = DEFAULT_START_TIME.getTime();
    private static final long DEFAULT_END_TIME_LONG = DEFAULT_END_TIME.getTime();
    @Autowired
    private TradeRuleClient tradeRuleClient;

    // @Test
    public void setAssetGas() throws Exception {
        QueryAssetGasRequest request = new QueryAssetGasRequest(DEFAULT_ASSET, new BigDecimal("0.01"));
        MbxResponse<Void> mbxResponse = tradeRuleClient.setAssetGas(request);
        System.out.println(mbxResponse);
        Assert.assertTrue(mbxResponse.isSuccess());
    }

    // @Test
    public void setUserProductFee() throws Exception {
        QueryUserProductFeeRequest request = new QueryUserProductFeeRequest(DEFAULT_ACCOUNT_ID, DEFAULT_SYMBOL,
                1L, 2, 3, 4);
        MbxResponse<Void> mbxResponse = tradeRuleClient.setUserProductFee(request);
        System.out.println(mbxResponse);
        Assert.assertTrue(mbxResponse.isSuccess());
    }

    // @Test
    public void setFee() throws Exception {
        SetFeeRequest request = new SetFeeRequest(DEFAULT_SYMBOL, 1, 2, 3, 4);
        MbxResponse<Void> mbxResponse = tradeRuleClient.setFee(request);
        System.out.println(mbxResponse);
        Assert.assertTrue(mbxResponse.isSuccess());
    }

    // @Test
    public void refreshTradingRule() throws Exception {
        RefreshTradingRuleRequest request = new RefreshTradingRuleRequest(1L);
        request.setTickSize(2.0);
        MbxResponse<Void> mbxResponse = tradeRuleClient.refreshTradingRule(request);
        System.out.println(mbxResponse);
        Assert.assertTrue(mbxResponse.isSuccess());
    }
}
