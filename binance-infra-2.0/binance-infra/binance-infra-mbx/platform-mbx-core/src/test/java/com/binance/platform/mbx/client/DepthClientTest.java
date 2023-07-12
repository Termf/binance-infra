package com.binance.platform.mbx.client;

import com.binance.platform.mbx.matchbox.model.MbxResponse;
import com.binance.platform.mbx.model.depth.DepthResponse;
import com.binance.platform.mbx.model.depth.GetDepthRequest;
import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Li Fenggang
 * Date: 2020/7/18 6:59 上午
 */
//@RunWith(SpringRunner.class)
//@SpringBootTest(classes = Application.class)
public class DepthClientTest {
    private static final String DEFAULT_SYMBOL = "BTCUSDT";
    @Autowired
    private DepthClient depthClient;

//    @Test
    public void getDepth() {
        GetDepthRequest request = new GetDepthRequest(DEFAULT_SYMBOL);
        request.setLimit(5);
        MbxResponse<DepthResponse> mbxResponse = depthClient.getDepth(request);
        Assert.assertTrue(mbxResponse.isSuccess());
    }
}
