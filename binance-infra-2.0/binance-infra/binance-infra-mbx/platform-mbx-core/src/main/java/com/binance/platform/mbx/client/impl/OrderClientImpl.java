package com.binance.platform.mbx.client.impl;

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
import com.binance.platform.mbx.service.OrderService;

/**
 * @author Li Fenggang
 * Date: 2020/7/16 8:04 上午
 */
public class OrderClientImpl implements OrderClient {
    private final OrderService orderService;

    public OrderClientImpl(OrderService orderService) {
        this.orderService = orderService;
    }

    @Override
    public PlaceOrderResponse placeOrder(PlaceOrderRequest request) throws Exception {
        return orderService.placeOrder(request);
    }

    @Override
    public PlaceOcoOrderResponse placeOcoOrder(PlaceOcoOrderRequest request) throws Exception {
        return orderService.placeOcoOrder(request);
    }

    @Override
    public DeleteOrderResponse deleteOrder(DeleteOrderRequest request) throws Exception {
        return orderService.deleteOrder(request);
    }

    @Override
    public DeleteOcoOrderResponse deleteOcoOrder(DeleteOcoOrderRequest request) throws Exception {
        return orderService.deleteOcoOrder(request);
    }

    @Override
    public DeleteOrderResponse mDeleteOrder(DeleteOrderRequest request) throws Exception {
        return orderService.mDeleteOrder(request);
    }

    @Override
    public DeleteOrderResponse deleteAllOrder(DeleteAllOrderRequest request) throws Exception {
        return orderService.deleteAllOrder(request);
    }
}
