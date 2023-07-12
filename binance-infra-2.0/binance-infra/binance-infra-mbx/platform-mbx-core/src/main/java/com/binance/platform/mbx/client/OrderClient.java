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
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * This interface include some business logic. It may be out of date, so use it with caution.
 *
 * @author Li Fenggang
 * Date: 2020/8/4 5:02 下午
 */
public interface OrderClient {

    /**
     * 下单
     *
     * @param request
     * @return
     * @throws Exception
     */
    @PostMapping(value = "/private/order", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    PlaceOrderResponse placeOrder(PlaceOrderRequest request)
            throws Exception;

    /**
     * oco下单
     *
     * @param request
     * @return
     * @throws Exception
     */
    @PostMapping(value = "/private/order/oco", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    PlaceOcoOrderResponse placeOcoOrder(PlaceOcoOrderRequest request)
            throws Exception;

    /**
     * 撤单
     *
     * @param request
     * @return
     * @throws Exception
     */
    @PostMapping(value = "/private/deleteOrder", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    DeleteOrderResponse deleteOrder(DeleteOrderRequest request) throws Exception;

    /**
     * oco撤单
     *
     * @param request
     * @return
     * @throws Exception
     */
    @PostMapping(value = "/private/deleteOcoOrder", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    DeleteOcoOrderResponse deleteOcoOrder(DeleteOcoOrderRequest request) throws Exception;

    /**
     * 撤单
     *
     * @param request
     * @return
     * @throws Exception
     */
    @PostMapping(value = "/mgmt/deleteOrder", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public DeleteOrderResponse mDeleteOrder(DeleteOrderRequest request) throws Exception;

    /**
     * 撤单
     *
     * @param request
     * @return
     * @throws Exception
     */
    @PostMapping(value = "/private/deleteAll", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public DeleteOrderResponse deleteAllOrder(DeleteAllOrderRequest request) throws Exception;

}
