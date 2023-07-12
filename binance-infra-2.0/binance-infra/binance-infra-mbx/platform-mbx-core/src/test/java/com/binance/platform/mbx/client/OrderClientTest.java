package com.binance.platform.mbx.client;

import com.binance.platform.mbx.model.order.DeleteAllOrderRequest;
import com.binance.platform.mbx.model.order.DeleteOcoOrderRequest;
import com.binance.platform.mbx.model.order.DeleteOcoOrderResponse;
import com.binance.platform.mbx.model.order.DeleteOrderRequest;
import com.binance.platform.mbx.model.order.DeleteOrderResponse;
import com.binance.platform.mbx.model.order.PlaceOcoOrderRequest;
import com.binance.platform.mbx.model.order.PlaceOcoOrderResponse;
import com.binance.platform.mbx.model.order.PlaceOrderRequest;
import com.binance.platform.mbx.model.order.PlaceOrderResponse;
import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;

/**
 * @author Li Fenggang
 * Date: 2020/7/18 7:33 上午
 */
//@RunWith(SpringRunner.class)
//@SpringBootTest(classes = Application.class)
public class OrderClientTest {
    private static final String DEFAULT_SYMBOL = "BTCUSDT";
    private static final long DEFAULT_USER_ID = 350790515;
    public static final String SIDE_SELL = "SELL";
    @Autowired
    private OrderClient orderClient;

    // @Test
    public void placeOrder() throws Exception {
        PlaceOrderRequest request = new PlaceOrderRequest(DEFAULT_USER_ID, DEFAULT_SYMBOL, SIDE_SELL, "MARKET");
        request.setQuantity(2D);
        PlaceOrderResponse placeOrderResponse = orderClient.placeOrder(request);
        System.out.println(placeOrderResponse);
        Assert.assertNotNull(placeOrderResponse);
    }

    // @Test
    public void placeOcoOrder() throws Exception {
        PlaceOcoOrderRequest request = new PlaceOcoOrderRequest(DEFAULT_USER_ID, DEFAULT_SYMBOL,
                SIDE_SELL, "1.0", "9200", "8800");
        PlaceOcoOrderResponse placeOcoOrderResponse = orderClient.placeOcoOrder(request);
        System.out.println(placeOcoOrderResponse);
        Assert.assertNotNull(placeOcoOrderResponse);
    }

    // @Test
    public void deleteOrder() throws Exception {
        DeleteOrderRequest request = new DeleteOrderRequest(DEFAULT_USER_ID,
                Arrays.asList(DEFAULT_SYMBOL, DEFAULT_SYMBOL, DEFAULT_SYMBOL), Arrays.asList("286", "287", "288"));
        DeleteOrderResponse deleteOrderResponse = orderClient.deleteOrder(request);
        System.out.println(deleteOrderResponse);
        Assert.assertNotNull(deleteOrderResponse);
    }

    // @Test
    public void deleteOcoOrder() throws Exception {
        DeleteOcoOrderRequest request = new DeleteOcoOrderRequest(DEFAULT_USER_ID, DEFAULT_SYMBOL, "292");
        request.setOrderListId("1");
        DeleteOcoOrderResponse deleteOcoOrderResponse = orderClient.deleteOcoOrder(request);
        System.out.println(deleteOcoOrderResponse);
        Assert.assertNotNull(deleteOcoOrderResponse);
    }

    //@Test
    public void mDeleteOrder() throws Exception {
        DeleteOrderRequest request = new DeleteOrderRequest(DEFAULT_USER_ID,
                Arrays.asList(DEFAULT_SYMBOL, DEFAULT_SYMBOL), Arrays.asList("1", "2"));
        DeleteOrderResponse deleteOrderResponse = orderClient.mDeleteOrder(request);
        System.out.println(deleteOrderResponse);
        Assert.assertNotNull(deleteOrderResponse);
    }

    // @Test
    public void deleteAllOrder() throws Exception {
        DeleteAllOrderRequest request = new DeleteAllOrderRequest(DEFAULT_USER_ID);
        DeleteOrderResponse deleteOrderResponse = orderClient.deleteAllOrder(request);
        System.out.println(deleteOrderResponse);
        Assert.assertNotNull(deleteOrderResponse);
    }
}
