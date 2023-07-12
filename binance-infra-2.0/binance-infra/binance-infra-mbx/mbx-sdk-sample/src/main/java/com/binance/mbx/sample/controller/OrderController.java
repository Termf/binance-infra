package com.binance.mbx.sample.controller;

import com.binance.platform.mbx.client.OrderClient;
import com.binance.platform.mbx.model.order.DeleteAllOrderRequest;
import com.binance.platform.mbx.model.order.DeleteOcoOrderRequest;
import com.binance.platform.mbx.model.order.DeleteOcoOrderResponse;
import com.binance.platform.mbx.model.order.DeleteOrderRequest;
import com.binance.platform.mbx.model.order.DeleteOrderResponse;
import com.binance.platform.mbx.model.order.PlaceOcoOrderRequest;
import com.binance.platform.mbx.model.order.PlaceOcoOrderResponse;
import com.binance.platform.mbx.model.order.PlaceOrderRequest;
import com.binance.platform.mbx.model.order.PlaceOrderResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Li Fenggang
 * Date: 2020/8/7 10:58 下午
 */
@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderClient orderClient;

    @GetMapping(value = "/placeOrder", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public PlaceOrderResponse placeOrder(PlaceOrderRequest request) throws Exception {
        return orderClient.placeOrder(request);
    }

    @GetMapping(value = "/placeOcoOrder", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public PlaceOcoOrderResponse placeOcoOrder(PlaceOcoOrderRequest request) throws Exception {
        return orderClient.placeOcoOrder(request);
    }

    @GetMapping(value = "/deleteOrder", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public DeleteOrderResponse deleteOrder(DeleteOrderRequest request) throws Exception {
        return orderClient.deleteOrder(request);
    }

    @GetMapping(value = "/deleteOcoOrder", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public DeleteOcoOrderResponse deleteOcoOrder(DeleteOcoOrderRequest request) throws Exception {
        return orderClient.deleteOcoOrder(request);
    }

    @GetMapping(value = "/mDeleteOrder", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public DeleteOrderResponse mDeleteOrder(DeleteOrderRequest request) throws Exception {
        return orderClient.mDeleteOrder(request);
    }

    @GetMapping(value = "/deleteAllOrder", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public DeleteOrderResponse deleteAllOrder(DeleteAllOrderRequest request) throws Exception {
        return orderClient.deleteAllOrder(request);
    }
}
