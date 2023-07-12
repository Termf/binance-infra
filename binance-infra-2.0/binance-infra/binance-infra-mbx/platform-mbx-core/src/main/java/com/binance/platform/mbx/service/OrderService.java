package com.binance.platform.mbx.service;


import com.binance.platform.mbx.model.order.DeleteAllOrderRequest;
import com.binance.platform.mbx.model.order.DeleteOcoOrderRequest;
import com.binance.platform.mbx.model.order.DeleteOcoOrderResponse;
import com.binance.platform.mbx.model.order.DeleteOrderRequest;
import com.binance.platform.mbx.model.order.DeleteOrderResponse;
import com.binance.platform.mbx.model.order.PlaceOcoOrderRequest;
import com.binance.platform.mbx.model.order.PlaceOcoOrderResponse;
import com.binance.platform.mbx.model.order.PlaceOrderRequest;
import com.binance.platform.mbx.model.order.PlaceOrderResponse;

public interface OrderService {

    PlaceOrderResponse placeOrder(PlaceOrderRequest body) throws Exception;

    PlaceOcoOrderResponse placeOcoOrder(PlaceOcoOrderRequest body) throws Exception;

    /**
     * Delete orders
     *
     * @param body
     * @return
     * @throws Exception
     */
    DeleteOrderResponse deleteOrder(DeleteOrderRequest body) throws Exception;

    DeleteOcoOrderResponse deleteOcoOrder(DeleteOcoOrderRequest body) throws Exception;

    DeleteOrderResponse mDeleteOrder(DeleteOrderRequest body) throws Exception;

    DeleteOrderResponse deleteAllOrder(DeleteAllOrderRequest body) throws Exception;
}
