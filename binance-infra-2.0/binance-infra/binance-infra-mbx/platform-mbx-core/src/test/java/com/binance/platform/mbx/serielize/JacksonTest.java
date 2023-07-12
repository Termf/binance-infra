package com.binance.platform.mbx.serielize;

import com.binance.platform.mbx.model.order.PlaceOrderResponse;
import com.binance.platform.mbx.util.JsonUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

/**
 * @author Li Fenggang
 * Date: 2020/7/18 9:10 下午
 */
public class JacksonTest {
    @Test
    public void depth() throws IOException {
        String json = "{\"symbol\":\"BTCUSDT\",\"orderId\":283,\"orderListId\":-1,\"clientOrderId\":\"default_5d648b8e42cf417b911b72568aea\",\"transactTime\":1595229061051,\"price\":\"0.00000000\",\"origQty\":\"2.00000000\",\"executedQty\":\"2.00000000\",\"cummulativeQuoteQty\":\"18000.00000000\",\"status\":\"FILLED\",\"timeInForce\":\"GTC\",\"type\":\"MARKET\",\"side\":\"SELL\",\"fills\":[{\"price\":\"9000.00000000\",\"qty\":\"2.00000000\",\"commission\":\"27.00000000\",\"commissionAsset\":\"USDT\",\"tradeId\":153}]}";
        PlaceOrderResponse response = JsonUtil.fromJson(json, PlaceOrderResponse.class);

        System.out.println(response);
    }

    @Test
    public void typeReferenceTest() {
        typeReferenceTestSample(new TypeReference<Void>() {}, Void.class);
        typeReferenceTestSample(new TypeReference<String>() {}, String.class);
        typeReferenceTestSample(new TypeReference<List<String>>() {}, List.class);
//        typeReferenceTestSample(new TypeReference() {}, Object.class);
    }

    private <T> void typeReferenceTestSample(TypeReference<T> typeReference, Class<?> typeClass) {
        Type type = typeReference.getType();
        System.out.println("type is " + type);
        if (type == Void.class) {
            System.out.println("rawType is Void");
        }
        if (type == String.class) {
            System.out.println("rawType is String");
        }
        if (type == List.class) {
            System.out.println("rawType is List");
        }
    }
}
