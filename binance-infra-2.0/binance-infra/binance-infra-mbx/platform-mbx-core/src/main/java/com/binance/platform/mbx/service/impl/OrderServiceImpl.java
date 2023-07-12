package com.binance.platform.mbx.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.binance.master.enums.TerminalEnum;
import com.binance.master.error.ErrorCode;
import com.binance.master.error.GeneralCode;
import com.binance.master.models.APIResponse;
import com.binance.platform.env.EnvUtil;
import com.binance.platform.mbx.cloud.consumer.CountryApiConsumer;
import com.binance.platform.mbx.cloud.consumer.OrderApiConsumer;
import com.binance.platform.mbx.cloud.consumer.SubAccountApiConsumer;
import com.binance.platform.mbx.cloud.consumer.UserApiConsumer;
import com.binance.platform.mbx.cloud.model.FetchProductsResponse;
import com.binance.platform.mbx.cloud.model.OpenOrderVo;
import com.binance.platform.mbx.cloud.model.ProductItemVO;
import com.binance.platform.mbx.cloud.model.QueryOpenOrderRequest;
import com.binance.platform.mbx.cloud.model.QueryOpenOrderResponse;
import com.binance.platform.mbx.cloud.model.UserIdRequest;
import com.binance.platform.mbx.cloud.model.UserStatusEx;
import com.binance.platform.mbx.cloud.util.ApiResponseUtil;
import com.binance.platform.mbx.config.MbxPropertiesHolder;
import com.binance.platform.mbx.config.SysConfigService;
import com.binance.platform.mbx.enums.MbxGatewayErrorCode;
import com.binance.platform.mbx.exception.MbxException;
import com.binance.platform.mbx.matchbox.MatchBoxManagementService;
import com.binance.platform.mbx.matchbox.MatchBoxRestService;
import com.binance.platform.mbx.matchbox.model.MbxResponse;
import com.binance.platform.mbx.matchbox.model.MbxState;
import com.binance.platform.mbx.matchbox.model.mgmt.MbxDeleteOcoOrderRequest;
import com.binance.platform.mbx.matchbox.model.mgmt.MbxDeleteOrderRequest;
import com.binance.platform.mbx.matchbox.model.mgmt.MbxGetOrderRequest;
import com.binance.platform.mbx.matchbox.model.mgmt.MbxGetOrderResponse;
import com.binance.platform.mbx.matchbox.model.mgmt.MbxPostOcoOrderRequest;
import com.binance.platform.mbx.matchbox.model.mgmt.MbxPostOcoOrderResponse;
import com.binance.platform.mbx.matchbox.model.mgmt.MbxPostOrderRequest;
import com.binance.platform.mbx.matchbox.model.mgmt.MbxPostOrderResponse;
import com.binance.platform.mbx.matchbox.model.rest.RestGetAvgPriceV3Request;
import com.binance.platform.mbx.model.order.DeleteAllOrderRequest;
import com.binance.platform.mbx.model.order.DeleteOcoOrderRequest;
import com.binance.platform.mbx.model.order.DeleteOcoOrderResponse;
import com.binance.platform.mbx.model.order.DeleteOrderRequest;
import com.binance.platform.mbx.model.order.DeleteOrderResponse;
import com.binance.platform.mbx.model.order.PlaceOcoOrderRequest;
import com.binance.platform.mbx.model.order.PlaceOcoOrderResponse;
import com.binance.platform.mbx.model.order.PlaceOcoOrderResponseConverter;
import com.binance.platform.mbx.model.order.PlaceOrderRequest;
import com.binance.platform.mbx.model.order.PlaceOrderResponse;
import com.binance.platform.mbx.model.order.PlaceOrderResponseConverter;
import com.binance.platform.mbx.service.AccountService;
import com.binance.platform.mbx.service.OrderService;
import com.binance.platform.mbx.service.ProductService;
import com.binance.platform.mbx.util.FormatUtils;
import com.binance.platform.mbx.util.JsonUtil;
import com.binance.platform.mbx.util.PatchUtils;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.web.client.HttpStatusCodeException;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class OrderServiceImpl implements OrderService, SmartInitializingSingleton {
    private static final Logger LOGGER = LoggerFactory.getLogger(OrderServiceImpl.class);
    private static final String DEFAULT_CLIENT_TYPE = "default";

    @Resource
    private AccountService accountService;
    @Resource
    private MatchBoxManagementService matchBoxManagementService;
    @Resource
    private MatchBoxRestService matchBoxRestService;

    @Autowired
    private SysConfigService sysConfigService;
    @Resource
    private ProductService productService;

    @Resource
    private OrderApiConsumer orderApi;
    @Resource
    private SubAccountApiConsumer subUserApi;
    @Resource
    private CountryApiConsumer countryApi;

    private boolean getOrderForceDelete(){
        return MbxPropertiesHolder.getMbxProperties().isOrderForceDelete();
    }

    @Resource
    private UserApiConsumer userApi;

    private Cache<String, Map<String, Object>> internalCache;

    private static String DEFAULT_MIN_RATE = "0.2";
    private static String DEFAULT_MAX_RATE = "5";

    private static Map<String, String> errorMap = new HashMap<String, String>();
    static {
        errorMap.put("MARKET IS CLOSED", GeneralCode.ORDER_MARKET_IS_CLOSED.getCode());
        errorMap.put("INSUFFICIENT BALANCE", GeneralCode.ORDER_INSUFFICIENT_BALANCE.getCode());
        errorMap.put("TOO MANY NEW ORDERS", GeneralCode.ORDER_TOO_MANY_NEW_ORDERS.getCode());
        errorMap.put("LOT_SIZE", GeneralCode.ORDER_LOT_SIZE.getCode());
        errorMap.put("PERCENT_PRICE", GeneralCode.ORDER_PRICE_FILTER.getCode());
        errorMap.put("PRICE_FILTER", GeneralCode.ORDER_PRICE_FILTER.getCode());
        errorMap.put("MIN_NOTIONAL", GeneralCode.ORDER_MIN_NOTIONAL.getCode());
        errorMap.put("T_PLUS_SELL", GeneralCode.ORDER_T_PLUS_SELL.getCode());
        errorMap.put("MAX_POSITION", GeneralCode.ORDER_MAX_POSITION.getCode());
        errorMap.put("THIS ACTION DISABLED", GeneralCode.ORDER_THIS_ACTION_DISABLED.getCode());
        errorMap.put("MIN_MAX_", GeneralCode.ORDER_MIN_MAX.getCode());
        errorMap.put("PRICE X QTY", GeneralCode.ORDER_PRICE_X_QTY.getCode());
        errorMap.put("QTY IS UNDER THE SYMBOL'S MINIMUM QTY", GeneralCode.ORDER_QTY_UNDER_MINIMUM_QTY.getCode());
        errorMap.put("QTY IS OVER THE SYMBOL'S MAXIMUM QTY", GeneralCode.ORDER_QTY_OVER_MAXIMUM_QTY.getCode());
        errorMap.put("PRICE IS OVER THE SYMBOL'S MAXIMUM PRICE", GeneralCode.ORDER_PRICE_OVER_MAXIMUM_PRICE.getCode());
        errorMap.put("PRICE IS UNDER THE SYMBOL'S MINIMUM PRICE", "order.price_under_minimum_price");
        errorMap.put("MARKET_CLOSED", GeneralCode.ORDER_MARKET_CLOSED.getCode());
        errorMap.put("Price * QTY is zero or less.", GeneralCode.ORDER_LESS_ZERO.getCode());
        errorMap.put("INVALID QUANTITY.", GeneralCode.ORDER_INVALID_QUANTITY.getCode());
        errorMap.put("MAX_NUM_ALGO_ORDERS", GeneralCode.ORDER_MAX_NUM_ALGO_ORDERS.getCode());
    }

    public OrderServiceImpl() {
        // init cache
        internalCache = CacheBuilder.newBuilder()
                .expireAfterWrite(3, TimeUnit.HOURS)
                .build();
        // Regularly refresh the price rule
        Executors.newScheduledThreadPool(1).scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                initPriceLimit();
            }
        }, 0, 2, TimeUnit.MINUTES);
    }

    // Close external scheduling, use the self scheduling defined in the default constructor.
//    @MyScheduledJob(cron = "0 0/2 * * * ?", author = "wangxiao", alarmEmail = "louis.wang@binance.com",
//            jobDesc = "每隔2分钟执行一次initPriceLimit")
    public void initPriceLimit() {
        List<FetchProductsResponse> productsList = productService.queryProducts();
        for (FetchProductsResponse productsResponse : productsList) {
            String symbol = productsResponse.getSymbol();
            String symbolType  = productsResponse.getSymbolType();
            if ("ETF".equalsIgnoreCase(symbolType)) {
                LOGGER.info("current symbol is {}", JSON.toJSONString(productsResponse));
                Map<String, Object> priceFilterMap = new HashMap<String, Object>();
                priceFilterMap.put("tickSize", FormatUtils.getPriceNumericFormatter().format(0.01000000));
                priceFilterMap.put("symbolType",symbolType);
                // RedisCacheUtils.set(symbol, priceFilterMap, 3 * 3600, PRICE_FILTER_CACHE_KEY_PREFIX);
                internalCache.put(symbol, priceFilterMap);
                continue;
            }
            try {
                RestGetAvgPriceV3Request avgPriceRequest = new RestGetAvgPriceV3Request(symbol);
                MbxResponse<String> mbxResponse = matchBoxRestService.getAvgPriceV3ForString(avgPriceRequest);
                mbxResponse.checkState();

                String avgPrice = mbxResponse.getData();
                JSONObject priceObj = JSONObject.parseObject(avgPrice);
                String minPriceRate = StringUtils.defaultIfBlank(this.sysConfigService.getValue("min_price_rate"),
                        DEFAULT_MIN_RATE);
                String maxPriceRate = StringUtils.defaultIfBlank(this.sysConfigService.getValue("max_price_rate"),
                        DEFAULT_MAX_RATE);
                // 最小价格波动
                BigDecimal tickSize = productsResponse.getTickSize();
                // 最小数量波动
                int stepSize = productsResponse.getStepSize().stripTrailingZeros().scale();
                int tickSizeCount = tickSize.stripTrailingZeros().scale();

                BigDecimal maxPrice =
                        new BigDecimal(priceObj.getDouble("price")).multiply(new BigDecimal(maxPriceRate));
                BigDecimal minPrice =
                        new BigDecimal(priceObj.getDouble("price")).multiply(new BigDecimal(minPriceRate));
                Map<String, Object> priceFilterMap = new HashMap<String, Object>();
                priceFilterMap.put("updateTime", System.currentTimeMillis());
                priceFilterMap.put("symbol", symbol);
                priceFilterMap.put("maxrate", maxPriceRate);
                priceFilterMap.put("minrate", minPriceRate);
                priceFilterMap.put("stepSize", stepSize);
                priceFilterMap.put("price", priceObj.getDouble("price"));
                priceFilterMap.put("tickSize", FormatUtils.getPriceNumericFormatter().format(tickSize));
                priceFilterMap.put("maxPrice",
                        maxPrice.setScale(tickSizeCount, BigDecimal.ROUND_CEILING).toPlainString());
                priceFilterMap.put("minPrice",
                        minPrice.setScale(tickSizeCount, BigDecimal.ROUND_CEILING).toPlainString());
                // RedisCacheUtils.set(symbol, priceFilterMap, 3 * 3600, PRICE_FILTER_CACHE_KEY_PREFIX);
                internalCache.put(symbol, priceFilterMap);
                LOGGER.info("The priceFilterMap is : {}", JSON.toJSONString(priceFilterMap));
            } catch (HttpStatusCodeException e) {
                HttpStatusCodeException ee = (HttpStatusCodeException) e;
                LOGGER.warn("更新初始化下单价格限制出错(HttpStatusCodeException)：{},{},{}", symbol, ee.getResponseBodyAsString(),
                        ee);
            } catch (Exception e) {
                LOGGER.error("更新初始化下单价格限制出错：", e);
            }
        }

    }

    private void validatePlaceOrderRequest(PlaceOrderRequest body) {
        if (body.getQuantity() == null && body.getQuoteOrderQty() == null) {
            throw new MbxException(GeneralCode.ILLEGAL_PARAM, "quantity and quoteOrderQty can't both be null");
        }
        if (body.getQuantity() != null && body.getQuoteOrderQty() != null) {
            throw new MbxException(GeneralCode.ILLEGAL_PARAM, "quantity and quoteOrderQty can't both have values");
        }
        if (body.getSymbol() == null) {
            throw new MbxException(GeneralCode.ILLEGAL_PARAM, "symbol can not be null");
        }
        if (body.getSide() == null) {
            throw new MbxException(GeneralCode.ILLEGAL_PARAM, "side can not be null");
        }
        if ("LIMIT".equals(body.getType())) {
            // 限价单
            if (body.getPrice() == null) {
                throw new MbxException(GeneralCode.ILLEGAL_PARAM, "price can not be null");
            }
        }
        if ("MARKET".equals(body.getType())) {
            // 市价单
        }
        if ("TAKE_PROFIT_LIMIT".equals(body.getType()) || "STOP_LOSS_LIMIT".equals(body.getType())) {
            // 止盈止损单
            if (body.getPrice() == null) {
                throw new MbxException(GeneralCode.ILLEGAL_PARAM, "price can not be null");
            }
            if (body.getStopPrice() == null) {
                throw new MbxException(GeneralCode.ILLEGAL_PARAM, "stopPrice can not be null");
            }
        }
    }

    @Override
    public PlaceOrderResponse placeOrder(PlaceOrderRequest body) throws Exception {
        this.validatePlaceOrderRequest(body);
        sysConfigService.checkSystemMaintenance();

        checkAccount(body.getUserId());

        return this.internalPlaceOrder(body.getSymbol(), body.getSide(), body.getType(), body.getQuantity(),
                body.getPrice(), body.getStopPrice(), body.getOrderId(), body.getUserId(), body.getActive(), body.getMsgAuth(),
                body.getNewClientOrderId(),body.getIcebergQty(),body.getNewOrderRespType(), body.getTimeInForce(), body.getQuoteOrderQty());
    }

    @Override
    public PlaceOcoOrderResponse placeOcoOrder(PlaceOcoOrderRequest body) throws Exception {
        sysConfigService.checkSystemMaintenance();

        checkAccount(body.getUserId());

        return this.internalPlaceOcoOrder(body);
    }

    private void checkAccount(long userId) throws Exception {
        // 若为子账户，子账户须被母账户启用方可交易
        UserIdRequest userIdReq = new UserIdRequest();
        userIdReq.setUserId(userId);
        Boolean checkResp =
                ApiResponseUtil.getAPIRequestResponse(this.subUserApi.notSubUserOrIsEnabledSubUser(userIdReq));
        if (!BooleanUtils.isTrue(checkResp)) {
            throw new MbxException(GeneralCode.SUB_USER_NOT_ENABLED);
        }
        // 若为黑名单用户则不允许交易
        APIResponse<Boolean> response = this.countryApi.isUserInBlacklist(userId);
        Boolean inBlackList = ApiResponseUtil.getAPIRequestResponse(response);
        if (BooleanUtils.isTrue(inBlackList)) {
            throw new MbxException(GeneralCode.BLACKLISTUSER_NOT_ENABLED, response.getSubData(), new Object[] {});
        }
    }

    private PlaceOrderResponse internalPlaceOrder(String symbol, String side, String type, Double quantity,
            Double price, Double stopPrice, Long orderId, long userId, Boolean active, String msgAuth,
            String newClientOrderId,Double icebergQty,String newOrderRespType, String inputTimeInForce, Double quoteOrderQty) throws Exception {
        if (StringUtils.isNotBlank(type) && type.toUpperCase().contains("LIMIT") && price == null) {
            throw new MbxException(GeneralCode.PRODUCT_PRICE_EMPTY);
        }
        // 检查产品是否存在
        List<ProductItemVO> productResponse = productService.queryProductItemWithCheck(symbol);

        Long tradingAccountId = accountService.retrieveTradingAccountIdByUserId(userId);

        String timeInForce = null, strPrice = null;
        if (StringUtils.isNotBlank(type) && type.toUpperCase().contains("LIMIT")) {
            if (BooleanUtils.isNotTrue(active)) {
                timeInForce = "GTC";
            } else {
                timeInForce = "IOC";
            }
            if (StringUtils.isNotBlank(inputTimeInForce)) {
                LOGGER.info("timeInForce applied by input[{}]", inputTimeInForce);
                timeInForce = inputTimeInForce;
            }
            strPrice = FormatUtils.getAssetNumericFormatter().format(price);
        }

        if("LIMIT_MAKER".equals(type.toUpperCase())) {
            strPrice = FormatUtils.getAssetNumericFormatter().format(price);
            timeInForce = null;
        }

        String stopPriceStr = null;
        if (stopPrice != null) {
            stopPriceStr = FormatUtils.getAssetNumericFormatter().format(stopPrice);
        }
        Long targetOrderId = null;
        if (orderId != null) {
            strPrice = FormatUtils.getPriceNumericFormatter().format(price);
            targetOrderId = orderId;
        }
        
        if(newClientOrderId == null || newClientOrderId.trim().length() <= 0) {
             newClientOrderId = getNewClientOrderId();
        }
        
        try {
            MbxPostOrderRequest request = new MbxPostOrderRequest(tradingAccountId, side.toUpperCase(), symbol, type);
            request.setQuantity(quantity == null ? null : FormatUtils.getAssetNumericFormatter().format(quantity));
            request.setForce(null);
            request.setIcebergQty(Objects.toString(icebergQty, null));
            request.setNewClientOrderId(newClientOrderId.length() > 36 ? newClientOrderId.substring(0, 36) : newClientOrderId);
            request.setNewOrderRespType(newOrderRespType);
            request.setPrice(strPrice);
            request.setStopPrice(stopPriceStr);
            request.setTargetOrderId(targetOrderId == null ? null : targetOrderId.toString());
            request.setTimeInForce(timeInForce);
            request.setMsgAuth(msgAuth);
            request.setQuoteOrderQty(quoteOrderQty);

            MbxResponse<MbxPostOrderResponse> mbxResponse = matchBoxManagementService.postOrder(request);
            if (mbxResponse.isSuccess()) {
                MbxPostOrderResponse mbxPostOrderResponse = mbxResponse.getData();
                return PlaceOrderResponseConverter.convert(mbxPostOrderResponse);
            } else {
                throw new MbxException(GeneralCode.SYS_ERROR, mbxResponse.getState().toString());
            }
        } catch (HttpStatusCodeException e) {
            String responseString = e.getResponseBodyAsString();
            LOGGER.info("Failed to place order: {}", responseString);
            String error =
                    errorTranslate(responseString, symbol, userId, price, productResponse.get(0));
            // is like {"msg":"order.min_price","code":-1013,"objs":" 7.4"}
            LOGGER.info("After errorTranslate,Failed to place order:{}", error);
            JSONObject jsonError = JSON.parseObject(error);
            GeneralCode errorCode = GeneralCode.findByCode(jsonError.getString("msg"));
            if (errorCode == null) {
                LOGGER.warn("Failed to place order: {},{}", responseString, jsonError);
                throw new MbxException(GeneralCode.PLACE_ORDER_ERROR, new Object[] {jsonError.getString("msg")});
                // throw new Exception(String.format("Failed to place order: {},{}", responseString, jsonError));
            }
            if (errorCode == GeneralCode.ORDER_MIN_NOTIONAL && EnvUtil.isUSA()) {
                //errorCode 设为null 不走 com.binance.master.web.handlers.HandlerAdvice.exception(com.binance.master.error
                // .MbxException, org.springframework.web.method.HandlerMethod, javax.servlet.http.HttpServletRequest)
                // 处理国际化的部分 强制替换错误信息
                throw new MbxException(MbxGatewayErrorCode.ORDER_AMOUNT_NOT_ENOUGH, "Total value must be more than 10.");
            }
            if (jsonError.containsKey("objs")) {
                Object objs = jsonError.get("objs");
                if (objs instanceof Object[]) {
                    throw new MbxException(errorCode, (Object[]) objs);
                } else if (objs instanceof JSONArray) {
                    throw new MbxException(errorCode, ((JSONArray) objs).toArray());
                } else {
                    throw new MbxException(errorCode, new Object[] {objs});
                }
            } else {
                throw new MbxException(errorCode);
            }
        }
    }

    private PlaceOcoOrderResponse internalPlaceOcoOrder(PlaceOcoOrderRequest request) throws Exception {
        
        // 检查产品是否存在
        List<ProductItemVO> productResponse = productService.queryProductItemWithCheck(request.getSymbol());

        Long tradingAccountId = accountService.retrieveTradingAccountIdByUserId(request.getUserId());

        if(request.getStopClientOrderId() == null || request.getStopClientOrderId().trim().length() <= 0) {
            request.setStopClientOrderId(getNewClientOrderId());
        }

        try {
            MbxPostOcoOrderRequest postOcoOrderRequest = new MbxPostOcoOrderRequest(tradingAccountId, request.getSymbol(),
                    request.getSide(), request.getQuantity(), request.getPrice(), request.getStopPrice());
            postOcoOrderRequest.setListClientOrderId(request.getListClientOrderId());
            postOcoOrderRequest.setLimitIcebergQty(request.getLimitIcebergQty());
            postOcoOrderRequest.setStopClientOrderId(request.getStopClientOrderId());
            postOcoOrderRequest.setStopLimitPrice(request.getStopLimitPrice());
            postOcoOrderRequest.setStopIcebergQty(request.getStopIcebergQty());
            postOcoOrderRequest.setStopLimitTimeInForce(request.getStopLimitTimeInForce());
            postOcoOrderRequest.setMsgAuth(request.getMsgAuth());
            postOcoOrderRequest.setNewOrderRespType(request.getNewOrderRespType());

            MbxResponse<MbxPostOcoOrderResponse> mbxResponse = matchBoxManagementService.postOcoOrder(postOcoOrderRequest);

            mbxResponse.checkState();

            PlaceOcoOrderResponse placeOcoOrderResponse = PlaceOcoOrderResponseConverter.convert(mbxResponse.getData());
            return placeOcoOrderResponse;
        } catch (HttpStatusCodeException e) {
            String responseString = e.getResponseBodyAsString();
            LOGGER.info("Failed to place ocoOrder: {}", responseString);
            String error =
                    errorTranslate(responseString, request.getSymbol(), request.getUserId(), Double.valueOf(request.getPrice()), productResponse.get(0));
            // is like {"msg":"order.min_price","code":-1013,"objs":" 7.4"}
            LOGGER.info("After errorTranslate,Failed to place ocoOrder:{}", error);
            JSONObject jsonError = JSON.parseObject(error);
            GeneralCode errorCode = GeneralCode.findByCode(jsonError.getString("msg"));
            if (errorCode == null) {
                LOGGER.warn("Failed to place ocoOrder: {},{}", responseString, jsonError);
                throw new MbxException(GeneralCode.PLACE_ORDER_ERROR, new Object[] {jsonError.getString("msg")});
            }
            if (jsonError.containsKey("objs")) {
                Object objs = jsonError.get("objs");
                if (objs instanceof Object[]) {
                    throw new MbxException(errorCode, (Object[]) objs);
                } else if (objs instanceof JSONArray) {
                    throw new MbxException(errorCode, ((JSONArray) objs).toArray());
                } else {
                    throw new MbxException(errorCode, new Object[] {objs});
                }
            } else {
                throw new MbxException(errorCode);
            }
        }
    }

    private String getNewClientOrderId() {
        String newClientOrderId;
        TerminalEnum terminal = PatchUtils.getTerminal();
        if (!ObjectUtils.isEmpty(terminal)) {
            newClientOrderId = terminal.getCode();
        } else {
            newClientOrderId = DEFAULT_CLIENT_TYPE;
        }
        if (StringUtils.equals("android", newClientOrderId)) {
            newClientOrderId = "and";
        }
        newClientOrderId =
                (newClientOrderId + "_" + com.binance.master.utils.StringUtils.uuid()).replaceAll("\\s*", "");
        return newClientOrderId.length() > 36 ? newClientOrderId.substring(0, 36) : newClientOrderId;
    }

    @Override
    public DeleteOrderResponse deleteOrder(DeleteOrderRequest body) throws Exception {
        sysConfigService.checkDeleteOrderMaintenance();

        if (body.getSymbols().size() != body.getOrderIds().size()) {
            throw new MbxException(GeneralCode.ILLEGAL_PARAM);
        }
        Long tradingAccountId = accountService.retrieveTradingAccountIdByUserId(body.getUserId());
        List<String> symbols = body.getSymbols();
        List<String> orderIds = body.getOrderIds();
        return this.delOrder(tradingAccountId, symbols, orderIds, body.getMsgAuth(), body.getOrigClientOrderId(),
                body.getNewClientOrderId());
    }

    @Override
    public DeleteOcoOrderResponse deleteOcoOrder(DeleteOcoOrderRequest body) throws Exception {
        sysConfigService.checkDeleteOrderMaintenance();

        Long tradingAccountId = accountService.retrieveTradingAccountIdByUserId(body.getUserId());
        return this.delOcoOrder(tradingAccountId, body.getSymbol(), body.getOrderListId(), body.getOrderId(), body.getListClientOrderId(), body.getMsgAuth(), body.getNewClientOrderId());
    }

    @Override
    public DeleteOrderResponse mDeleteOrder(DeleteOrderRequest body) throws Exception {
        long userId = body.getUserId();
        validateUserStatus(userId);

        Long tradingAccountId = accountService.retrieveTradingAccountIdByUserId(userId);

        List<String> symbols = body.getSymbols();
        List<String> orderIds = body.getOrderIds();
        return this.delOrder(tradingAccountId, symbols, orderIds, body.getMsgAuth(),body.getOrigClientOrderId(),body.getNewClientOrderId());
    }

    private void validateUserStatus(long userId) throws Exception {
        APIResponse<UserStatusEx> userStatusResponse = userApi.getUserStatusByUserId(userId);
        if(!APIResponse.Status.OK.equals(userStatusResponse.getStatus())) {
            LOGGER.error("validateUserStatus 远程调用account失败, userId:{}", userId);
            throw new MbxException(GeneralCode.SYS_ERROR);
        }

        if((userStatusResponse.getData().getIsUserDisabled() == true) || (userStatusResponse.getData().getIsUserLock() == true)) {
            throw new MbxException("用户不可用");
        }
    }

    private DeleteOrderResponse addDeleteOrderResponse(DeleteOrderResponse response1, DeleteOrderResponse response2) {
        DeleteOrderResponse resp = new DeleteOrderResponse();
        resp.getCorrects().addAll(response1.getCorrects());
        resp.getErrors().addAll(response1.getErrors());
        resp.getCorrects().addAll(response2.getCorrects());
        resp.getErrors().addAll(response2.getErrors());
        return resp;
    }

    private DeleteOrderResponse delOrder(Long tradingAccountId, List<String> symbols, List<String> orderIds,
            String msgAuth,String origClientOrderId,String newClientOrderId) {
        DeleteOrderResponse response = new DeleteOrderResponse();
        for (int i = 0; i < symbols.size(); i++) {
            String symbol = symbols.get(i);
            String orderId = orderIds.get(i);
            if(newClientOrderId == null || newClientOrderId.trim().length() <= 0) {
                newClientOrderId = this.getNewClientOrderId();
            }

            deleteSingleOrder(tradingAccountId, symbol, orderId, origClientOrderId, newClientOrderId, msgAuth,
                    response);
        }
        return response;
    }

    private void deleteSingleOrder(Long tradingAccountId, String symbol, String orderId, String origClientOrderId,
                                   String newClientOrderId, String msgAuth, DeleteOrderResponse response) {
        MbxResponse<String> mbxResponse = null;
        try {
            /*
             *     原先没有使用deleteOrder的返回参数构建resp，而是通过入参orderId、symbol构建出参
             *     使用 deleteOrder的返回参数，将json格式转换成对应返回实体。
             */

            MbxDeleteOrderRequest mbxDeleteOrderRequest = new MbxDeleteOrderRequest(tradingAccountId, symbol);
            mbxDeleteOrderRequest.setForce(Objects.toString(getOrderForceDelete(), null));
            mbxDeleteOrderRequest.setNewClientOrderId(newClientOrderId);
            mbxDeleteOrderRequest.setOrderId(orderId);
            mbxDeleteOrderRequest.setOrigClientOrderId(origClientOrderId);
            mbxDeleteOrderRequest.setMsgAuth(msgAuth);

            mbxResponse = matchBoxManagementService.deleteOrderForString(mbxDeleteOrderRequest);

            if (mbxResponse.isSuccess()) {

                String jsonStr = mbxResponse.getData();
                LOGGER.debug(jsonStr);
                DeleteOrderResponse.DelCorrect delCorrect = JsonUtil.fromJsonSilently(jsonStr, DeleteOrderResponse.DelCorrect.class);
                DeleteOcoOrderResponse.DelCorrect ocoDelCorrect = JsonUtil.fromJsonSilently(jsonStr, DeleteOcoOrderResponse.DelCorrect.class);
                if ("OCO".equals(ocoDelCorrect.getContingencyType())) {
                    response.getCorrects().addAll(convertOcoDelCorrect(ocoDelCorrect));
                } else {
                    response.getCorrects().add(delCorrect);
                }
                delCorrect.setErrorCode(GeneralCode.ORDER_TRADE_CANCEL_SUCCESS);
            } else {
                MbxState state = mbxResponse.getState();
                if (state == null) {
                    return;
                }
                String errorMsg = state.getMsg();
                if ("Unknown order sent.".equalsIgnoreCase(errorMsg)) {
                    LOGGER.warn("订单状态过期:{}-{}", symbol, orderId);
                    ErrorCode errorCode = retrieveErrorCodeForOrderStatus(symbol, orderId);
                    if (errorCode != null) {
                        response.addError(orderId, symbol, errorCode);
                    }
                } else if ("MARKET_CLOSED".equalsIgnoreCase(errorMsg)) {
                    response.addError(orderId, symbol, GeneralCode.ORDER_MARKET_CLOSED);
                } else if (msgAuth == null || msgAuth.equalsIgnoreCase("NORMAL")) {
                    long errorCode = state.getCode().longValue();
                    if ("This action disabled is on this account.".equalsIgnoreCase(errorMsg) && errorCode == -2011) {
                        // 用户被禁用交易 并且 msgAuth没有特意设置，则设置 msgAuth=MANUAL，让用户可以撤单
                        deleteSingleOrder(tradingAccountId, symbol, orderId, origClientOrderId, newClientOrderId,
                                "MANUAL", response);
                    } else if ("Market is closed.".equalsIgnoreCase(errorMsg) && errorCode == -1013) {
                        // 交易对为break 并且 msgAuth没有特意设置，则设置 msgAuth=MANUAL，让用户可以撤单
                        deleteSingleOrder(tradingAccountId, symbol, orderId, origClientOrderId, newClientOrderId,
                                "MANUAL", response);
                    }
                }
            }
        } catch (MbxException e) {
            LOGGER.error("Delete order error. orderId=" + orderId + ", symbol=" + symbol, e);
        }
    }

    private List<DeleteOrderResponse.DelCorrect> convertOcoDelCorrect(DeleteOcoOrderResponse.DelCorrect ocoDelCorrect) {
        List<DeleteOrderResponse.DelCorrect> delCorrects = new ArrayList<>();
        for (DeleteOcoOrderResponse.Order order : ocoDelCorrect.getOrders()) {
            DeleteOrderResponse.DelCorrect delCorrect = new DeleteOrderResponse.DelCorrect();
            delCorrect.setSymbol(order.getSymbol());
            delCorrect.setOrderId("" + order.getOrderId());
            delCorrect.setClientOrderId(order.getClientOrderId());
            delCorrect.setErrorCode(GeneralCode.ORDER_TRADE_CANCEL_SUCCESS);
            delCorrect.setTransactTime(ocoDelCorrect.getTransactionTime());
            getOrderReport(delCorrect, ocoDelCorrect);
            delCorrects.add(delCorrect);
        }

        return delCorrects;
    }

    private void getOrderReport(DeleteOrderResponse.DelCorrect delCorrect, DeleteOcoOrderResponse.DelCorrect ocoDelCorrect) {
        if(delCorrect != null) {
            for(DeleteOcoOrderResponse.OrderReport orderReport : ocoDelCorrect.getOrderReports()) {
                if(orderReport.getOrderId().equals(Long.valueOf(delCorrect.getOrderId()))
                        && orderReport.getSymbol().equals(delCorrect.getSymbol())) {
                    BeanUtils.copyProperties(orderReport, delCorrect);
                    return ;
                }
            }
        }
    }

    private DeleteOcoOrderResponse delOcoOrder(Long tradingAccountId, String symbol, String orderListId,
                                               String orderId, String listClientOrderId, String msgAuth,String newClientOrderId) {
        DeleteOcoOrderResponse response = new DeleteOcoOrderResponse();
        if(newClientOrderId == null || newClientOrderId.trim().length() <= 0) {
            newClientOrderId = this.getNewClientOrderId();
        }

        try {
            MbxDeleteOcoOrderRequest deleteOcoOrderRequest = new MbxDeleteOcoOrderRequest(tradingAccountId, symbol);
            deleteOcoOrderRequest.setOrderListId(orderListId);
            deleteOcoOrderRequest.setListClientOrderId(listClientOrderId);
            deleteOcoOrderRequest.setNewClientOrderId(newClientOrderId);
            deleteOcoOrderRequest.setMsgAuth(msgAuth);

            MbxResponse<String> mbxResponse = matchBoxManagementService.deleteOcoOrderForString(deleteOcoOrderRequest);

            if (mbxResponse.isSuccess()) {
                String jsonStr = mbxResponse.getData();
                LOGGER.debug(jsonStr);
                DeleteOcoOrderResponse.DelCorrect delCorrect = JsonUtil.fromJsonSilently(jsonStr, DeleteOcoOrderResponse.DelCorrect.class);
                delCorrect.setErrorCode(GeneralCode.ORDER_TRADE_CANCEL_SUCCESS);
                response.getCorrects().add(delCorrect);
            } else {
                MbxState state = mbxResponse.getState();
                if (state != null) {
                    String errorMsg = state.getMsg();
                    if ("Unknown order list sent.".equalsIgnoreCase(errorMsg)) {
                        LOGGER.warn("订单状态过期:{}-{}", symbol, orderId);
                        ErrorCode errorCode = retrieveErrorCodeForOrderStatus(symbol, orderId);
                        if (errorCode != null) {
                            response.addError(orderListId, symbol, errorCode);
                        }
                    } else if ("MARKET_CLOSED".equalsIgnoreCase(errorMsg)) {
                        response.addError(orderListId, symbol, GeneralCode.ORDER_MARKET_CLOSED);
                    }
                }
            }
        } catch (MbxException e) {
            LOGGER.error("delete OCO order error. orderListId=" + orderListId + ", symbol=" + symbol, e);
        }

        return response;
    }

    private ErrorCode retrieveErrorCodeForOrderStatus(String symbol, String orderId) {
        MbxGetOrderResponse order = this.getOrderById(symbol, Long.parseLong(orderId));
        if (order != null) {
            String status = order.getStatus();
            if ("FILLED".equals(status)) {
                return GeneralCode.ORDER_TRADE_SUCCESS;
            } else if ("REJECTED".equals(status)) {
                return GeneralCode.ORDER_TRADE_REFUSE;
            } else if ("EXPIRED".equals(status)) {
                return GeneralCode.ORDER_TRADE_OVER_TIME;
            } else if ("CANCELED".equals(status)) {
                return GeneralCode.ORDER_TRADE_CANCEL;
            }
        }

        return null;
    }


    private MbxGetOrderResponse getOrderById(String symbol, Long orderId) {
        try {
            MbxGetOrderRequest getOrderRequest = new MbxGetOrderRequest(symbol);
            getOrderRequest.setOrderId(orderId);

            MbxResponse<MbxGetOrderResponse> mbxResponse = matchBoxManagementService.getOrder(getOrderRequest);
            mbxResponse.checkState();

            MbxGetOrderResponse mbxGetOrderResponse = mbxResponse.getData();

            if (mbxGetOrderResponse.getOrderId() != null) {
                return mbxGetOrderResponse;
            }
        } catch (HttpStatusCodeException e) {
            String responseString = e.getResponseBodyAsString();
            LOGGER.warn("Failed to fetch order, details: {}", responseString);
        } catch (Exception e) {
            LOGGER.error(String.format("Failed to fetch order"), e);
        }
        return null;
    }

    @Override
    public DeleteOrderResponse deleteAllOrder(DeleteAllOrderRequest body) throws Exception {
        /*if (StringUtils.equals("1", this.sysConfigService.getValue(Constants.SYSTEM_MAINTENANCE))) {
            throw new MbxException(GeneralCode.SYS_MAINTENANCE);
        }
        DeleteOrderResponse response = new DeleteOrderResponse();
        GetUserResponse getUserResponse = this.getCheckedUser(body.getUserId());
        QueryOpenOrderRequest queryOpenOrderRequest = new QueryOpenOrderRequest();
        queryOpenOrderRequest.setUserId(getUserResponse.getUser().getUserId());
        queryOpenOrderRequest.setSymbol(body.getSymbol());
        JSONArray openOrders = null;
        try {
            String result = "";
            if(body.getSymbol() == null) {
                result = this.matchboxService.getOpenOrders(getUserResponse.getUserInfo().getTradingAccount());
            } else {
                result = this.matchboxService.getOpenOrders(getUserResponse.getUserInfo().getTradingAccount(), body.getSymbol());
            }
            openOrders = JSON.parseArray(result);
        } catch (HttpStatusCodeException e) {
            String responseString = e.getResponseBodyAsString();
            log.warn("Failed to fetch allOpenOrders: {}", responseString);
            throw new MbxException("Unknown order sent.");
        } catch (Exception e) {
            log.error("Failed to fetch allOpenOrders: {}", e);
            throw new MbxException("Unknown order sent.");
        }

        boolean notFound = true;
        for (int i = 0; i < openOrders.size(); i++){
            String type = openOrders.getJSONObject(i).getString("type");
            String symbol = openOrders.getJSONObject(i).getString("symbol");
            String orderId = openOrders.getJSONObject(i).getString("orderId");

            // if (StringUtils.equals(order.getType(), body.getType()) || StringUtils.equals("ALL",
            // body.getType())
            // || StringUtils.isBlank(body.getType())
            // || (StringUtils.equals("STOP_LIMIT", body.getType()) &&
            // ("STOP_LOSS_LIMIT".equals(order.getType())
            // || "TAKE_PROFIT_LIMIT".equals(order.getType())))) {
            // }
            if (StringUtils.isBlank(body.getType()) || StringUtils.equals("ALL",
                    body.getType()) || StringUtils.equals(type, body.getType())
                    || ("STOP_LIMIT".equals(body.getType()) && ("STOP_LOSS_LIMIT".equals(type)
                            || "TAKE_PROFIT_LIMIT".equals(type)))) {
                notFound = false;
                DeleteOrderResponse resp = this.delOrder(getUserResponse, Lists.newArrayList(symbol),
                        Lists.newArrayList(orderId), body.getMsgAuth(),
                        null,null);
                response = this.addDeleteOrderResponse(response, resp);
            }
        }
        if (notFound) {
            log.warn("条件没有满足，没有实质进行撤单处理。条件是：{}", body);
        }
        return response;*/

        sysConfigService.checkDeleteOrderMaintenance();

        DeleteOrderResponse response = new DeleteOrderResponse();

        long userId = body.getUserId();
        Long tradingAccountId = accountService.retrieveTradingAccountIdByUserId(userId);

        QueryOpenOrderRequest queryOpenOrderRequest = new QueryOpenOrderRequest();
        queryOpenOrderRequest.setUserId(userId);
        queryOpenOrderRequest.setSymbol(body.getSymbol());
        QueryOpenOrderResponse queryOpenOrderResponse =
                ApiResponseUtil.getAPIRequestResponse(this.orderApi.queryOpenOrder(queryOpenOrderRequest));
        if (queryOpenOrderResponse == null || queryOpenOrderResponse.getOpenOrderList() == null
                || queryOpenOrderResponse.getOpenOrderList().isEmpty()) {
            throw new MbxException(GeneralCode.SYS_NOT_EXIST);
        }
        boolean notFound = true;
        for (OpenOrderVo order : queryOpenOrderResponse.getOpenOrderList()) {
            if (StringUtils.isBlank(body.getType()) || StringUtils.equals("ALL",
                    body.getType()) || StringUtils.equals(order.getType(), body.getType())
                    || ("STOP_LIMIT".equals(body.getType()) && ("STOP_LOSS_LIMIT".equals(order.getType())
                    || "TAKE_PROFIT_LIMIT".equals(order.getType())))) {
                notFound = false;
                DeleteOrderResponse resp = this.delOrder(tradingAccountId, Lists.newArrayList(order.getSymbol()),
                        Lists.newArrayList(order.getOrderId().toString()), body.getMsgAuth(),
                        null,null);
                response = this.addDeleteOrderResponse(response, resp);
            }
        }
        if (notFound) {
            LOGGER.warn("条件没有满足，没有实质进行撤单处理。条件是：{}", body);
        }
        return response;
    }

    private String errorTranslate(String error, String symbol, long userId, Double price, ProductItemVO productItem) {
        String uppercase = error.toUpperCase();
        for (String key : errorMap.keySet()) {
            if (uppercase.contains(key)) {
                if ("LOT_SIZE".equals(key) || "MIN_NOTIONAL".equals(key)
                        || "QTY IS OVER THE SYMBOL'S MAXIMUM QTY".equals(key)
                        || "PRICE IS OVER THE SYMBOL'S MAXIMUM PRICE".equals(key)) {
                    JSONObject errJson = JSON.parseObject(error);
                    errJson.put("objs", getRuleObject(symbol));
                    error = errJson.toString();
                }
                if ("MAX_NUM_ALGO_ORDERS".equals(key)) {
                    JSONObject errJson = JSON.parseObject(error);
                    error = errJson.toString();
                }
                LOGGER.info("价格限制之key:{}", key);
                if ("PERCENT_PRICE".equals(key)) {
                    Map<String, Object> priceFilter = internalCache.getIfPresent(symbol);
                            //RedisCacheUtils.get(symbol, Map.class, PRICE_FILTER_CACHE_KEY_PREFIX);

                    if (priceFilter == null) {
                        this.initPriceLimit();
                        priceFilter = internalCache.getIfPresent(symbol);
                        //RedisCacheUtils.get(symbol, Map.class, PRICE_FILTER_CACHE_KEY_PREFIX);
                    }
                    BigDecimal tickSize = new BigDecimal(priceFilter.get("tickSize").toString());
                    BigDecimal a = new BigDecimal(String.valueOf(price)).divide(tickSize, 8, RoundingMode.CEILING);
                    LOGGER.info("价格限制之priceFilter1:{}", priceFilter.toString());
                    if (new BigDecimal(a.intValue()).compareTo(a) == 0) {
                        if (price >= Double.parseDouble(priceFilter.get("maxPrice").toString())) {
                            errorMap.put("PERCENT_PRICE", GeneralCode.ORDER_MAX_PRICE.getCode());
                            JSONObject errJson = JSON.parseObject(error);
                            errJson.put("objs", priceFilter.get("maxPrice").toString());
                            LOGGER.info("价格限制之maxPirce:{},{},{}", priceFilter.get("maxPrice").toString(), symbol, price);
                            error = errJson.toString();
                        } else if (price <= Double.parseDouble(priceFilter.get("minPrice").toString())) {
                            errorMap.put("PERCENT_PRICE", GeneralCode.ORDER_MIN_PRICE.getCode());
                            JSONObject errJson = JSON.parseObject(error);
                            errJson.put("objs", priceFilter.get("minPrice").toString());
                            LOGGER.info("价格限制之minPrice:{},{},{}", priceFilter.get("minPrice").toString(), symbol, price);
                            error = errJson.toString();
                        } else {
                            LOGGER.warn("价格限制之priceFilter2:{}", priceFilter.toString());
                            JSONObject errJson = JSON.parseObject(error);
                            error = errJson.toString();
                        }
                    } else {
                        errorMap.put("PERCENT_PRICE", GeneralCode.ORDER_TICK_SIZE_LIMIT.getCode());
                        JSONObject errJson = JSON.parseObject(error);
                        errJson.put("objs", priceFilter.get("tickSize").toString());
                        LOGGER.info("价格限制之tickSize:{},{},{}", priceFilter.get("tickSize").toString(), symbol, price);
                        error = errJson.toString();
                    }
                }
                if ("PRICE_FILTER".equals(key)) {
                    Map<String, Object> priceFilter = internalCache.getIfPresent(symbol);
                    // RedisCacheUtils.get(symbol, Map.class, PRICE_FILTER_CACHE_KEY_PREFIX);
                    if (priceFilter == null) {
                        this.initPriceLimit();
                        priceFilter = internalCache.getIfPresent(symbol);
                        // RedisCacheUtils.get(symbol, Map.class, PRICE_FILTER_CACHE_KEY_PREFIX);
                    }
                    JSONObject errJson = JSON.parseObject(error);
                    if (priceFilter == null) {
                        errorMap.put("PRICE_FILTER", GeneralCode.ORDER_PRICE_FILTER.getCode());
                        LOGGER.info("PRICE_FILTER is null,symbol:{}",symbol);
                    } else if (priceFilter.get("symbolType") != null
                            && "ETF".equalsIgnoreCase(priceFilter.get("symbolType").toString())) {
                        errorMap.put("PRICE_FILTER", GeneralCode.ORDER_PRICE_FILTER.getCode());
                        LOGGER.info("PRICE_FILTER is ETF,symbol:{}",symbol);
                    } else {
                        errorMap.put("PRICE_FILTER", GeneralCode.ORDER_TICK_SIZE_LIMIT.getCode());
                        errJson.put("objs", priceFilter.get("tickSize").toString());
                        LOGGER.info("价格限制之tickSize:{},{},{}", priceFilter.get("tickSize").toString(), symbol, price);
                    }
                    error = errJson.toString();
                }
                return error.replaceAll("(?i)\"msg\":\"[^\"]*\"", "\"msg\":\"" + errorMap.get(key) + "\"");
            }
        }
        return error;
    }

    private String[] getRuleObject(String symbol) {
        return new String[] {"0", "0", "0", "0", "0"};
        /**
        try {
            RuleModel ruleItem = this.tradeRuleService.getRuleBySymbol(symbol);
            DecimalFormat df = new DecimalFormat("#");
            df.setMinimumIntegerDigits(1);
            df.setMaximumFractionDigits(8);
            String[] strings =
                    new String[] {ruleItem.getMinNotional() == null ? "0" : df.format(ruleItem.getMinNotional()),
                            ruleItem.getMaxTradeValue() == null ? "0" : df.format(ruleItem.getMaxTradeValue()),
                            ruleItem.getMaxPrice() == null ? "0" : df.format(ruleItem.getMaxPrice()),
                            ruleItem.getMinTrade() == null ? "0" : df.format(ruleItem.getMinTrade()),
                            ruleItem.getMinPrice() == null ? "0" : df.format(ruleItem.getMinPrice())};
            return strings;
        } catch (Exception e) {
            log.error("get Rule By Symbol Error", e);
            return new String[] {"0", "0", "0", "0", "0"};
        }

         */
    }

    @Override
    public void afterSingletonsInstantiated() {}
}
