package com.binance.platform.mbx.client;

import com.binance.platform.mbx.matchbox.model.MbxResponse;
import com.binance.platform.mbx.model.kline.GetKlineRequest;
import com.binance.platform.mbx.model.kline.GetKlinesResponse;
import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Li Fenggang
 * Date: 2020/7/18 7:27 上午
 */
//@RunWith(SpringRunner.class)
//@SpringBootTest(classes = Application.class)
public class KlineClientTest {
    private static final String DEFAULT_SYMBOL = "BTCUSDT";
    @Autowired
    private KlineClient klineClient;

    // @Test
    public void getKlines() throws Exception {
        GetKlineRequest request = new GetKlineRequest(DEFAULT_SYMBOL, "5m");
        MbxResponse<GetKlinesResponse> mbxResponse = klineClient.getKlines(request);
        Assert.assertTrue(mbxResponse.isSuccess());
    }
}
