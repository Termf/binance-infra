package com.binance.platform.mbx.client;

import com.binance.platform.mbx.exception.MbxException;
import com.binance.platform.mbx.matchbox.model.MbxResponse;
import com.binance.platform.mbx.model.price.GetMbxPriceRequest;
import com.binance.platform.mbx.model.price.GetPriceRequest;
import com.binance.platform.mbx.model.price.GetTickerPriceRequest;
import com.binance.platform.mbx.model.price.SymbolClosePriceResponse;
import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author Li Fenggang
 * Date: 2020/7/18 8:07 上午
 */
//@RunWith(SpringRunner.class)
//@SpringBootTest(classes = Application.class)
public class PriceClientTest {
    private static final String DEFAULT_SYMBOL = "BTCUSDT";
    @Autowired
    private PriceClient priceClient;

    // @Test
    public void getPrice() throws MbxException {
        GetMbxPriceRequest request = new GetMbxPriceRequest(DEFAULT_SYMBOL);
        Double price = priceClient.getPrice(request);
        System.out.println("price=" + price);
        Assert.assertNotNull(price);
    }

    // @Test
    public void getTickerPrice() throws MbxException {
        GetTickerPriceRequest request = new GetTickerPriceRequest();
        request.setSymbol(DEFAULT_SYMBOL);
        List<SymbolClosePriceResponse> priceResponseList = priceClient.getTickerPrice(request);
        if (priceResponseList != null && !priceResponseList.isEmpty()) {
            for (SymbolClosePriceResponse symbolClosePriceResponse : priceResponseList) {
                System.out.println(symbolClosePriceResponse);
            }
        } else {
            System.out.println("priceResponseList is null or empty");
        }

        Assert.assertNotNull(priceResponseList);
    }

    // @Test
    public void get24hrTicker() throws MbxException {
        GetPriceRequest request = new GetPriceRequest(DEFAULT_SYMBOL);
        MbxResponse<String> mbxResponse = priceClient.get24hrTicker(request);
        System.out.println(mbxResponse);
        Assert.assertTrue(mbxResponse.isSuccess());
    }
}
