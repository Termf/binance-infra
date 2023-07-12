package com.binance.platform.mbx.client;

import com.binance.platform.mbx.matchbox.model.MbxResponse;
import com.binance.platform.mbx.model.trade.GetAggTradesRequest;
import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Li Fenggang
 * Date: 2020/7/20 8:45 上午
 */
//@RunWith(SpringRunner.class)
//@SpringBootTest(classes = Application.class)
public class TradeClientTest {
    private static final String DEFAULT_SYMBOL = "BTCUSDT";
    @Autowired
    private TradeClient tradeClient;

//    @Test
    public void getAggTrades() throws Exception {
        GetAggTradesRequest request = new GetAggTradesRequest(DEFAULT_SYMBOL);
        MbxResponse<String> mbxResponse = tradeClient.getAggTrades(request);
        System.out.println(mbxResponse);
        Assert.assertTrue(mbxResponse.isSuccess());
    }
}
