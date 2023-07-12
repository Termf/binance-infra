package com.binance.platform.mbx.cloud.consumer;

import com.binance.platform.mbx.cloud.model.GetProductItemRequest;
import com.binance.platform.mbx.cloud.model.ProductItemResponse;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Li Fenggang
 * Date: 2020/7/20 1:57 下午
 */
//@RunWith(SpringRunner.class)
//@SpringBootTest(classes = Application.class)
public class ProductApiConsumerTest {
    private static final String DEFAULT_SYMBOL = "BTCUSDT";
    @Autowired
    private ProductApiConsumer productApiConsumer;
    // @Test
    public void getProductItem() {
        GetProductItemRequest request = new GetProductItemRequest();
        request.setSymbol(DEFAULT_SYMBOL);
        ProductItemResponse productItem = productApiConsumer.getProductItem(request);
        System.out.println(productItem);
    }
}
