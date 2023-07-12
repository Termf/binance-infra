package com.binance.mbx.sample.controller;

import com.binance.mbx.sample.util.SignatureUtil;
import com.binance.platform.mbx.client.MatchBoxClient;
import com.binance.platform.mbx.exception.MbxException;
import com.binance.platform.mbx.matchbox.model.MbxResponse;
import com.binance.platform.mbx.model.matchbox.DeleteApiKeyWhitelistRequest;
import com.binance.platform.mbx.model.matchbox.GetAccountFromEngineV3Request;
import com.binance.platform.mbx.model.matchbox.GetAccountFromEngineV3Response;
import com.binance.platform.mbx.model.matchbox.GetApiKeyWhitelistRequest;
import com.binance.platform.mbx.model.matchbox.GetApiKeyWhitelistResponse;
import com.binance.platform.mbx.model.matchbox.GetOcoAllOrderListRequest;
import com.binance.platform.mbx.model.matchbox.GetOcoAllOrderListResponse;
import com.binance.platform.mbx.model.matchbox.GetOcoOpenOrderListRequest;
import com.binance.platform.mbx.model.matchbox.GetOcoOpenOrderListResponse;
import com.binance.platform.mbx.model.matchbox.GetOcoOrderListRequest;
import com.binance.platform.mbx.model.matchbox.GetOcoOrderListResponse;
import com.binance.platform.mbx.model.matchbox.PostApiKeyWhitelistRequest;
import com.binance.platform.mbx.model.matchbox.PostBalanceBatchResponse;
import com.binance.platform.mbx.model.matchbox.DeleteApiKeyLockRequest;
import com.binance.platform.mbx.model.matchbox.DeleteApiKeyRequest;
import com.binance.platform.mbx.model.matchbox.DeleteApiKeyRuleRequest;
import com.binance.platform.mbx.model.matchbox.DeleteGasOptOutRequest;
import com.binance.platform.mbx.model.matchbox.DeleteIcebergPartsFilterRequest;
import com.binance.platform.mbx.model.matchbox.DeleteLotSizeFilterRequest;
import com.binance.platform.mbx.model.matchbox.DeleteMarketLotSizeFilterRequest;
import com.binance.platform.mbx.model.matchbox.DeleteMinNotionalFilterRequest;
import com.binance.platform.mbx.model.matchbox.DeleteNumAlgoOrdersFilterRequest;
import com.binance.platform.mbx.model.matchbox.DeleteNumIcebergOrdersRequest;
import com.binance.platform.mbx.model.matchbox.DeleteNumOrdersFilterRequest;
import com.binance.platform.mbx.model.matchbox.DeleteOcoOrderReq;
import com.binance.platform.mbx.model.matchbox.DeleteOcoOrderResponse;
import com.binance.platform.mbx.model.matchbox.DeleteOpenOrdersRequest;
import com.binance.platform.mbx.model.matchbox.DeleteOpenOrdersResponse;
import com.binance.platform.mbx.model.matchbox.DeleteOrderRateLimitAccountRequest;
import com.binance.platform.mbx.model.matchbox.DeleteOrderRequest;
import com.binance.platform.mbx.model.matchbox.DeleteOrderResponse;
import com.binance.platform.mbx.model.matchbox.DeletePercentPriceFilterRequest;
import com.binance.platform.mbx.model.matchbox.DeletePositionFilterRequest;
import com.binance.platform.mbx.model.matchbox.DeletePriceFilterRequest;
import com.binance.platform.mbx.model.matchbox.DeleteTPlusSellFilterRequest;
import com.binance.platform.mbx.model.matchbox.DeleteUserDataStreamRequest;
import com.binance.platform.mbx.model.matchbox.GetAccountByExternalIdRequest;
import com.binance.platform.mbx.model.matchbox.GetAccountRequest;
import com.binance.platform.mbx.model.matchbox.GetAccountResponse;
import com.binance.platform.mbx.model.matchbox.GetAccountResponseV3;
import com.binance.platform.mbx.model.matchbox.GetAccountSymbolCommissionHistoryRequest;
import com.binance.platform.mbx.model.matchbox.GetAccountSymbolCommissionHistoryResponse;
import com.binance.platform.mbx.model.matchbox.GetAccountSymbolsCommissionRequest;
import com.binance.platform.mbx.model.matchbox.GetAccountSymbolsCommissionResponse;
import com.binance.platform.mbx.model.matchbox.GetAccountTPlusLockStateRequest;
import com.binance.platform.mbx.model.matchbox.GetAccountTPlusLockStateResponse;
import com.binance.platform.mbx.model.matchbox.GetAllAccountsResponse;
import com.binance.platform.mbx.model.matchbox.GetAllOrdersRequest;
import com.binance.platform.mbx.model.matchbox.GetAllOrdersResponse;
import com.binance.platform.mbx.model.matchbox.GetApiKeyCheckRequest;
import com.binance.platform.mbx.model.matchbox.GetApiKeyCheckResponse;
import com.binance.platform.mbx.model.matchbox.GetApiKeyLockRequest;
import com.binance.platform.mbx.model.matchbox.GetApiKeyLockResponse;
import com.binance.platform.mbx.model.matchbox.GetApiKeysRequest;
import com.binance.platform.mbx.model.matchbox.GetApiKeysResponse;
import com.binance.platform.mbx.model.matchbox.GetAssetBalanceRequest;
import com.binance.platform.mbx.model.matchbox.GetAssetBalanceResponse;
import com.binance.platform.mbx.model.matchbox.GetAssetLedgerRequest;
import com.binance.platform.mbx.model.matchbox.GetAssetLedgerResponse;
import com.binance.platform.mbx.model.matchbox.GetAssetsResponse;
import com.binance.platform.mbx.model.matchbox.GetAuctionReportsRequest;
import com.binance.platform.mbx.model.matchbox.GetBalanceLedgerRequest;
import com.binance.platform.mbx.model.matchbox.GetBalanceLedgerResponse;
import com.binance.platform.mbx.model.matchbox.GetCommandsResponse;
import com.binance.platform.mbx.model.matchbox.GetCommissionRequest;
import com.binance.platform.mbx.model.matchbox.GetCommissionResponse;
import com.binance.platform.mbx.model.matchbox.GetEstimateSymbolRequest;
import com.binance.platform.mbx.model.matchbox.GetEstimateSymbolResponse;
import com.binance.platform.mbx.model.matchbox.GetFiltersRequest;
import com.binance.platform.mbx.model.matchbox.GetFiltersResponse;
import com.binance.platform.mbx.model.matchbox.GetMsgAuthOrdersRequest;
import com.binance.platform.mbx.model.matchbox.GetMsgAuthOrdersResponse;
import com.binance.platform.mbx.model.matchbox.GetOpenOrdersRequest;
import com.binance.platform.mbx.model.matchbox.GetOpenOrdersResponse;
import com.binance.platform.mbx.model.matchbox.GetOrderRateLimitRequest;
import com.binance.platform.mbx.model.matchbox.GetOrderRateLimitResponse;
import com.binance.platform.mbx.model.matchbox.GetOrderRequest;
import com.binance.platform.mbx.model.matchbox.GetOrderResponse;
import com.binance.platform.mbx.model.matchbox.GetSymbolCommissionHistoryRequest;
import com.binance.platform.mbx.model.matchbox.GetSymbolCommissionHistoryResponse;
import com.binance.platform.mbx.model.matchbox.GetSymbolCommissionRequest;
import com.binance.platform.mbx.model.matchbox.GetSymbolCommissionResponse;
import com.binance.platform.mbx.model.matchbox.GetSymbolHistoryRequest;
import com.binance.platform.mbx.model.matchbox.GetSymbolHistoryResponse;
import com.binance.platform.mbx.model.matchbox.GetSymbolOrdersRequest;
import com.binance.platform.mbx.model.matchbox.GetSymbolOrdersResponse;
import com.binance.platform.mbx.model.matchbox.GetSymbolsResponse;
import com.binance.platform.mbx.model.matchbox.GetTimeResponse;
import com.binance.platform.mbx.model.matchbox.GetTradesRequest;
import com.binance.platform.mbx.model.matchbox.GetTradesResponse;
import com.binance.platform.mbx.model.matchbox.GetUserTradesRequest;
import com.binance.platform.mbx.model.matchbox.GetUserTradesResponse;
import com.binance.platform.mbx.model.matchbox.PostAccountRequest;
import com.binance.platform.mbx.model.matchbox.PostAccountRequestV3;
import com.binance.platform.mbx.model.matchbox.PostAccountResponse;
import com.binance.platform.mbx.model.matchbox.PostAccountResponseV3;
import com.binance.platform.mbx.model.matchbox.PostApiKeyRequest;
import com.binance.platform.mbx.model.matchbox.PostApiKeyResponse;
import com.binance.platform.mbx.model.matchbox.PostApiKeyRuleRequest;
import com.binance.platform.mbx.model.matchbox.PostAssetRequest;
import com.binance.platform.mbx.model.matchbox.PostBalanceBatchRequest;
import com.binance.platform.mbx.model.matchbox.PostBalanceRequest;
import com.binance.platform.mbx.model.matchbox.PostBalanceResponse;
import com.binance.platform.mbx.model.matchbox.PostIcebergPartsFilterRequest;
import com.binance.platform.mbx.model.matchbox.PostLotSizeFilterRequest;
import com.binance.platform.mbx.model.matchbox.PostMarketLotSizeFilterRequest;
import com.binance.platform.mbx.model.matchbox.PostMinNotionalFilterRequest;
import com.binance.platform.mbx.model.matchbox.PostNumAlgoOrdersFilterRequest;
import com.binance.platform.mbx.model.matchbox.PostNumIcebergOrdersRequest;
import com.binance.platform.mbx.model.matchbox.PostNumOrdersFilterRequest;
import com.binance.platform.mbx.model.matchbox.PostOcoOrderRequest;
import com.binance.platform.mbx.model.matchbox.PostOcoOrderResponse;
import com.binance.platform.mbx.model.matchbox.PostOrderRequest;
import com.binance.platform.mbx.model.matchbox.PostOrderResponse;
import com.binance.platform.mbx.model.matchbox.PostPercentPriceFilterRequest;
import com.binance.platform.mbx.model.matchbox.PostPositionFilterRequest;
import com.binance.platform.mbx.model.matchbox.PostPriceFilterRequest;
import com.binance.platform.mbx.model.matchbox.PostSymbolRequest;
import com.binance.platform.mbx.model.matchbox.PostTPlusSellFilterRequest;
import com.binance.platform.mbx.model.matchbox.PostUserDataStreamRequest;
import com.binance.platform.mbx.model.matchbox.PostUserDataStreamResponse;
import com.binance.platform.mbx.model.matchbox.PutAccountAssetRequest;
import com.binance.platform.mbx.model.matchbox.PutAccountCommissionRequest;
import com.binance.platform.mbx.model.matchbox.PutAccountRequest;
import com.binance.platform.mbx.model.matchbox.PutAccountRequestV3;
import com.binance.platform.mbx.model.matchbox.PutAccountResponseV3;
import com.binance.platform.mbx.model.matchbox.PutAccountSymbolsCommissionRequest;
import com.binance.platform.mbx.model.matchbox.PutAllSymbolsRequest;
import com.binance.platform.mbx.model.matchbox.PutApiKeyLockRequest;
import com.binance.platform.mbx.model.matchbox.PutApiKeyPermissionsRequest;
import com.binance.platform.mbx.model.matchbox.PutExchangeGasRequest;
import com.binance.platform.mbx.model.matchbox.PutExchangeRateRequest;
import com.binance.platform.mbx.model.matchbox.PutGasOptOutRequest;
import com.binance.platform.mbx.model.matchbox.PutOrderRateLimitAccountRequest;
import com.binance.platform.mbx.model.matchbox.PutOrderRateLimitRequest;
import com.binance.platform.mbx.model.matchbox.PutOrderTypesRequest;
import com.binance.platform.mbx.model.matchbox.PutSymbolCommissionRequest;
import com.binance.platform.mbx.model.matchbox.PutSymbolCommissionTypeRequest;
import com.binance.platform.mbx.model.matchbox.PutSymbolLimitsRequest;
import com.binance.platform.mbx.model.matchbox.PutSymbolPermissionsRequest;
import com.binance.platform.mbx.model.matchbox.PutSymbolRequest;
import com.binance.platform.mbx.model.matchbox.PutUserDataStreamRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


/**
 * matchbox mgmt interfaces. There is no business logic, just forwarding.
 */
@RestController
@RequestMapping("/matchbox")
public class MatchBoxContoller {
    @Autowired
    private MatchBoxClient matchBoxClient;

    @GetMapping(value = "/putAccountCommission", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public MbxResponse<Void> putAccountCommission(PutAccountCommissionRequest request) throws MbxException {
        return matchBoxClient.putAccountCommission(request);
    }

    @GetMapping(value = "/getTime", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public MbxResponse<GetTimeResponse> getTime() throws MbxException {
        return matchBoxClient.getTime();
    }

    @GetMapping(value = "/getSymbolOrders", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public MbxResponse<List<GetSymbolOrdersResponse>> getSymbolOrders(GetSymbolOrdersRequest request) throws MbxException {
        return matchBoxClient.getSymbolOrders(request);
    }

    @GetMapping(value = "/postBalance", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public MbxResponse<PostBalanceResponse> postBalance(PostBalanceRequest request) throws MbxException {
        return matchBoxClient.postBalance(request);
    }

    @GetMapping(value = "/postBalanceBatch", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public MbxResponse<List<PostBalanceBatchResponse>> postBalanceBatch(PostBalanceBatchRequest request) {
        return matchBoxClient.postBalanceBatch(request);
    }

    @GetMapping(value = "/deleteOrder", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public MbxResponse<DeleteOrderResponse> deleteOrder(DeleteOrderRequest request) throws MbxException {
        return matchBoxClient.deleteOrder(request);
    }

    @GetMapping(value = "/deleteOcoOrder", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public MbxResponse<DeleteOcoOrderResponse> delOcoOrder(DeleteOcoOrderReq request) throws MbxException {
        return matchBoxClient.delOcoOrder(request);
    }

    @GetMapping(value = "/deleteOpenOrders", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public MbxResponse<DeleteOpenOrdersResponse> deleteOpenOrders(DeleteOpenOrdersRequest request) throws MbxException {
        return matchBoxClient.deleteOpenOrders(request);
    }

    @GetMapping(value = "/getAllOrders", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public MbxResponse<List<GetAllOrdersResponse>> getAllOrders(GetAllOrdersRequest request) throws MbxException {
        return matchBoxClient.getAllOrders(request);
    }

    @GetMapping(value = "/getOcoOrderList", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public MbxResponse<GetOcoOrderListResponse> getOcoOrderList(GetOcoOrderListRequest request) throws MbxException {
        return matchBoxClient.getOcoOrderList(request);
    }

    @GetMapping(value = "/getOcoOpenOrderList", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public MbxResponse<List<GetOcoOpenOrderListResponse>> getOcoOpenOrderList(GetOcoOpenOrderListRequest request) throws MbxException {
        return matchBoxClient.getOcoOpenOrderList(request);
    }

    @GetMapping(value = "/getOcoAllOrderList", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public MbxResponse<List<GetOcoAllOrderListResponse>> getOcoAllOrderList(GetOcoAllOrderListRequest request) throws MbxException {
        return matchBoxClient.getOcoAllOrderList(request);
    }

    @GetMapping(value = "/postOrder", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public MbxResponse<PostOrderResponse> postOrder(PostOrderRequest request) throws MbxException {
        return matchBoxClient.postOrder(request);
    }

    @GetMapping(value = "/postOcoOrder", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public MbxResponse<PostOcoOrderResponse> postOcoOrder(PostOcoOrderRequest request) throws MbxException {
        return matchBoxClient.postOcoOrder(request);
    }

    @GetMapping(value = "/getBalanceLedger", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public MbxResponse<List<GetBalanceLedgerResponse>> getBalanceLedger(GetBalanceLedgerRequest request) throws MbxException {
        return matchBoxClient.getBalanceLedger(request);
    }

    @GetMapping(value = "/getAllAccounts", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public MbxResponse<List<GetAllAccountsResponse>> getAllAccounts() throws MbxException {
        return matchBoxClient.getAllAccounts();
    }

    @GetMapping(value = "/putAccountAsset", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public MbxResponse<Void> putAccountAsset(PutAccountAssetRequest request) throws MbxException {
        return matchBoxClient.putAccountAsset(request);
    }

    @GetMapping(value = "/postAccount", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public MbxResponse<PostAccountResponse> postAccount(PostAccountRequest request) throws MbxException {
        return matchBoxClient.postAccount(request);
    }

    @GetMapping(value = "/postAccountV3", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public MbxResponse<PostAccountResponseV3> postAccountV3(PostAccountRequestV3 request) throws MbxException {
        return matchBoxClient.postAccountV3(request);
    }

    @GetMapping(value = "/getOpenOrders", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public MbxResponse<List<GetOpenOrdersResponse>> getOpenOrders(GetOpenOrdersRequest request) throws MbxException {
        return matchBoxClient.getOpenOrders(request);
    }

    @GetMapping(value = "/getMsgAuthOrders", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public MbxResponse<List<GetMsgAuthOrdersResponse>> getMsgAuthOrders(GetMsgAuthOrdersRequest request) throws MbxException {
        return matchBoxClient.getMsgAuthOrders(request);
    }

    @GetMapping(value = "/putSymbolLimits", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public MbxResponse<Void> putSymbolLimits(PutSymbolLimitsRequest request) throws MbxException {
        return matchBoxClient.putSymbolLimits(request);
    }

    @GetMapping(value = "/putAccount", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public MbxResponse<Void> putAccount(PutAccountRequest request) throws MbxException {
        return matchBoxClient.putAccount(request);
    }

    @GetMapping(value = "/putAccountV3", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public MbxResponse<PutAccountResponseV3> putAccountV3(PutAccountRequestV3 request) throws MbxException {
        return matchBoxClient.putAccountV3(request);
    }

    @GetMapping(value = "/getOrder", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public MbxResponse<GetOrderResponse> getOrder(GetOrderRequest request) throws MbxException {
        return matchBoxClient.getOrder(request);
    }

    @GetMapping(value = "/getAccount", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public MbxResponse<GetAccountResponse> getAccount(GetAccountRequest request) throws MbxException {
        return matchBoxClient.getAccount(request);
    }

    @GetMapping(value = "/getAccountFromEngineV3", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public MbxResponse<GetAccountFromEngineV3Response> getAccountFromEngineV3(GetAccountFromEngineV3Request request) throws MbxException {
        return matchBoxClient.getAccountFromEngineV3(request);
    }

    @GetMapping(value = "/putGasOptOut", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public MbxResponse<Void> putGasOptOut(PutGasOptOutRequest request) throws MbxException {
        return matchBoxClient.putGasOptOut(request);
    }

    @GetMapping(value = "/deleteGasOptOut", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public MbxResponse<Void> deleteGasOptOut(DeleteGasOptOutRequest request) throws MbxException {
        return matchBoxClient.deleteGasOptOut(request);
    }

    @GetMapping(value = "/postApiKey", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public MbxResponse<PostApiKeyResponse> postApiKey(PostApiKeyRequest request) throws MbxException {
        return matchBoxClient.postApiKey(request);
    }

    @GetMapping(value = "/getApiKeys", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public MbxResponse<List<GetApiKeysResponse>> getApiKeys(GetApiKeysRequest request) throws MbxException {
        return matchBoxClient.getApiKeys(request);
    }

    @GetMapping(value = "/putApiKeyLock", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public MbxResponse<Void> putApiKeyLock(PutApiKeyLockRequest request) throws MbxException {
        return matchBoxClient.putApiKeyLock(request);
    }

    @GetMapping(value = "/postPriceFilter", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public MbxResponse<Void> postPriceFilter(PostPriceFilterRequest request) throws MbxException {
        return matchBoxClient.postPriceFilter(request);
    }

    @GetMapping(value = "/postAsset", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public MbxResponse<Void> postAsset(PostAssetRequest request) throws MbxException {
        return matchBoxClient.postAsset(request);
    }

    @GetMapping(value = "/getApiKeyLock", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public MbxResponse<GetApiKeyLockResponse> getApiKeyLock(GetApiKeyLockRequest request) throws MbxException {
        return matchBoxClient.getApiKeyLock(request);
    }

    @GetMapping(value = "/getAssetBalance", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public MbxResponse<GetAssetBalanceResponse> getAssetBalance(GetAssetBalanceRequest request) throws MbxException {
        return matchBoxClient.getAssetBalance(request);
    }

    @GetMapping(value = "/getAssetLedger", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public MbxResponse<List<GetAssetLedgerResponse>> getAssetLedger(GetAssetLedgerRequest request) throws MbxException {
        return matchBoxClient.getAssetLedger(request);
    }

    @GetMapping(value = "/putAllSymbols", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public MbxResponse<Void> putAllSymbols(PutAllSymbolsRequest request) throws MbxException {
        return matchBoxClient.putAllSymbols(request);
    }

    @GetMapping(value = "/postSymbol", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public MbxResponse<Void> postSymbol(PostSymbolRequest request) throws MbxException {
        return matchBoxClient.postSymbol(request);
    }

    @GetMapping(value = "/postApiKeyRule", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public MbxResponse<Void> postApiKeyRule(PostApiKeyRuleRequest request) throws MbxException {
        return matchBoxClient.postApiKeyRule(request);
    }

    @GetMapping(value = "/deleteApiKeyLock", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public MbxResponse<Void> deleteApiKeyLock(DeleteApiKeyLockRequest request) throws MbxException {
        return matchBoxClient.deleteApiKeyLock(request);
    }

    @GetMapping(value = "/getSymbols", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public MbxResponse<List<GetSymbolsResponse>> getSymbols() throws MbxException {
        return matchBoxClient.getSymbols();
    }

    @GetMapping(value = "/getApiKeyCheck", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public MbxResponse<GetApiKeyCheckResponse> getApiKeyCheck(GetApiKeyCheckRequest request) throws MbxException {
        return matchBoxClient.getApiKeyCheck(request);
    }
    @GetMapping(value = "/getApiKeyCheckWithAutoSignature", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public MbxResponse<GetApiKeyCheckResponse> getApiKeyCheckMock(
            @RequestParam(value = "apiKey", defaultValue = "4Vrlx8fOgfInmZR1Rt6WKktDLTZohXoTAUB4DrZ0k7Eh45uNbOacXa7IpnWmh32j") String apiKey,
            @RequestParam(value = "secretKey", defaultValue = "lf1V8BdXSmz1q7TNXWN4prxyhYjwcbw874Jy4rCeI1qqd72E4ezhxuEHMLGscYTL") String secretKey,
            @RequestParam(value = "ip", defaultValue = "0.0.0.0") String ip,
            @RequestParam(value = "payload", defaultValue = "type=SPOT&limit=1") String payload,
            @RequestParam(value = "recvWindow", defaultValue = "50000") long recvWindow
            ) throws MbxException {

        GetApiKeyCheckRequest request = new GetApiKeyCheckRequest(apiKey, ip);
        long timeStamp = System.currentTimeMillis();

        StringBuilder stringBuilder = new StringBuilder(payload);
        if (stringBuilder.length() > 0) {
            stringBuilder.append("&");
        }
        stringBuilder.append("recvWindow=").append(recvWindow);

        stringBuilder.append("&timeStamp=").append(timeStamp);

        payload = stringBuilder.toString();
        String signature = SignatureUtil.sha256_HMAC(secretKey, payload);

        request.setPayload(payload);
        request.setRecvWindow(recvWindow);
        request.setTimestamp(timeStamp);
        request.setSignature(signature);

        return matchBoxClient.getApiKeyCheck(request);
    }

    @GetMapping(value = "/postApiKeyWhitelist", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public MbxResponse<Void> postApiKeyWhitelist(PostApiKeyWhitelistRequest request) throws MbxException {
        return matchBoxClient.postApiKeyWhitelist(request);
    }

    @GetMapping(value = "/deleteApiKeyWhitelist", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public MbxResponse<Void> deleteApiKeyWhitelist(DeleteApiKeyWhitelistRequest request) throws MbxException {
        return matchBoxClient.deleteApiKeyWhitelist(request);
    }

    @GetMapping(value = "/getApiKeyWhitelist", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public MbxResponse<GetApiKeyWhitelistResponse> getApiKeyWhitelist(GetApiKeyWhitelistRequest request) throws MbxException {
        return matchBoxClient.getApiKeyWhitelist(request);
    }

    @GetMapping(value = "/getFilters", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public MbxResponse<GetFiltersResponse> getFilters(GetFiltersRequest request) throws MbxException {
        return matchBoxClient.getFilters(request);
    }

    @GetMapping(value = "/deleteApiKeyRule", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public MbxResponse<Void> deleteApiKeyRule(DeleteApiKeyRuleRequest request) throws MbxException {
        return matchBoxClient.deleteApiKeyRule(request);
    }

    @GetMapping(value = "/getSymbolHistory", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public MbxResponse<List<GetSymbolHistoryResponse>> getSymbolHistory(GetSymbolHistoryRequest request) throws MbxException {
        return matchBoxClient.getSymbolHistory(request);
    }

    @GetMapping(value = "/putExchangeRate", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public MbxResponse<Void> putExchangeRate(PutExchangeRateRequest request) throws MbxException {
        return matchBoxClient.putExchangeRate(request);
    }

    @GetMapping(value = "/getCommission", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public MbxResponse<List<GetCommissionResponse>> getCommission(GetCommissionRequest request) throws MbxException {
        return matchBoxClient.getCommission(request);
    }

    @GetMapping(value = "/getUserTrades", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public MbxResponse<List<GetUserTradesResponse>> getUserTrades(GetUserTradesRequest request) throws MbxException {
        return matchBoxClient.getUserTrades(request);
    }

    @GetMapping(value = "/putOrderTypes", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public MbxResponse<Void> putOrderTypes(PutOrderTypesRequest request) throws MbxException {
        return matchBoxClient.putOrderTypes(request);
    }

    @GetMapping(value = "/getAssets", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public MbxResponse<List<GetAssetsResponse>> getAssets() throws MbxException {
        return matchBoxClient.getAssets();
    }

    @GetMapping(value = "/deleteApiKey", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public MbxResponse<Void> deleteApiKey(DeleteApiKeyRequest request) throws MbxException {
        return matchBoxClient.deleteApiKey(request);
    }

    @GetMapping(value = "/putSymbol", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public MbxResponse<Void> putSymbol(PutSymbolRequest request) throws MbxException {
        return matchBoxClient.putSymbol(request);
    }

    @GetMapping(value = "/putSymbolPermissions", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public MbxResponse<Void> putSymbolPermissions(PutSymbolPermissionsRequest request) throws MbxException {
        return matchBoxClient.putSymbolPermissions(request);
    }

    @GetMapping(value = "/getTrades", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public MbxResponse<List<GetTradesResponse>> getTrades(GetTradesRequest request) throws MbxException {
        return matchBoxClient.getTrades(request);
    }

    @GetMapping(value = "/getAccountSymbolCommissionHistory", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public MbxResponse<List<GetAccountSymbolCommissionHistoryResponse>> getAccountSymbolCommissionHistory(GetAccountSymbolCommissionHistoryRequest request) throws MbxException {
        return matchBoxClient.getAccountSymbolCommissionHistory(request);
    }

    @GetMapping(value = "/putSymbolCommissionType", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public MbxResponse<Void> putSymbolCommissionType(PutSymbolCommissionTypeRequest request) throws MbxException {
        return matchBoxClient.putSymbolCommissionType(request);
    }

    @GetMapping(value = "/getSymbolCommissionHistory", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public MbxResponse<List<GetSymbolCommissionHistoryResponse>> getSymbolCommissionHistory(GetSymbolCommissionHistoryRequest request) throws MbxException {
        return matchBoxClient.getSymbolCommissionHistory(request);
    }

    @GetMapping(value = "/deleteNumOrdersFilter", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public MbxResponse<Void> deleteNumOrdersFilter(DeleteNumOrdersFilterRequest request) throws MbxException {
        return matchBoxClient.deleteNumOrdersFilter(request);
    }

    @GetMapping(value = "/postLotSizeFilter", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public MbxResponse<Void> postLotSizeFilter(PostLotSizeFilterRequest request) throws MbxException {
        return matchBoxClient.postLotSizeFilter(request);
    }

    @GetMapping(value = "/postPercentPriceFilter", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public MbxResponse<Void> postPercentPriceFilter(PostPercentPriceFilterRequest request) throws MbxException {
        return matchBoxClient.postPercentPriceFilter(request);
    }

    @GetMapping(value = "/deleteTPlusSellFilter", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public MbxResponse<Void> deleteTPlusSellFilter(DeleteTPlusSellFilterRequest request) throws MbxException {
        return matchBoxClient.deleteTPlusSellFilter(request);
    }

    @GetMapping(value = "/deletePercentPriceFilter", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public MbxResponse<Void> deletePercentPriceFilter(DeletePercentPriceFilterRequest request) throws MbxException {
        return matchBoxClient.deletePercentPriceFilter(request);
    }

    @GetMapping(value = "/postPositionFilter", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public MbxResponse<Void> postPositionFilter(PostPositionFilterRequest request) throws MbxException {
        return matchBoxClient.postPositionFilter(request);
    }

    @GetMapping(value = "/getSymbolCommission", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public MbxResponse<GetSymbolCommissionResponse> getSymbolCommission(GetSymbolCommissionRequest request) throws MbxException {
        return matchBoxClient.getSymbolCommission(request);
    }

    @GetMapping(value = "/postMinNotionalFilter", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public MbxResponse<Void> postMinNotionalFilter(PostMinNotionalFilterRequest request) throws MbxException {
        return matchBoxClient.postMinNotionalFilter(request);
    }

    @GetMapping(value = "/deleteNumIcebergOrders", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public MbxResponse<Void> deleteNumIcebergOrders(DeleteNumIcebergOrdersRequest request) throws MbxException {
        return matchBoxClient.deleteNumIcebergOrders(request);
    }

    @GetMapping(value = "/putSymbolCommission", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public MbxResponse<Void> putSymbolCommission(PutSymbolCommissionRequest request) throws MbxException {
        return matchBoxClient.putSymbolCommission(request);
    }

    @GetMapping(value = "/postNumOrdersFilter", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public MbxResponse<Void> postNumOrdersFilter(PostNumOrdersFilterRequest request) throws MbxException {
        return matchBoxClient.postNumOrdersFilter(request);
    }

    @GetMapping(value = "/deletePriceFilter", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public MbxResponse<Void> deletePriceFilter(DeletePriceFilterRequest request) throws MbxException {
        return matchBoxClient.deletePriceFilter(request);
    }

    @GetMapping(value = "/deleteMinNotionalFilter", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public MbxResponse<Void> deleteMinNotionalFilter(DeleteMinNotionalFilterRequest request) throws MbxException {
        return matchBoxClient.deleteMinNotionalFilter(request);
    }

    @GetMapping(value = "/postTPlusSellFilter", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public MbxResponse<Void> postTPlusSellFilter(PostTPlusSellFilterRequest request) throws MbxException {
        return matchBoxClient.postTPlusSellFilter(request);
    }

    @GetMapping(value = "/postIcebergPartsFilter", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public MbxResponse<Void> postIcebergPartsFilter(PostIcebergPartsFilterRequest request) throws MbxException {
        return matchBoxClient.postIcebergPartsFilter(request);
    }

    @GetMapping(value = "/deleteUserDataStream", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public MbxResponse<Void> deleteUserDataStream(DeleteUserDataStreamRequest request) throws MbxException {
        return matchBoxClient.deleteUserDataStream(request);
    }

    @GetMapping(value = "/deletePositionFilter", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public MbxResponse<Void> deletePositionFilter(DeletePositionFilterRequest request) throws MbxException {
        return matchBoxClient.deletePositionFilter(request);
    }

    @GetMapping(value = "/deleteIcebergPartsFilter", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public MbxResponse<Void> deleteIcebergPartsFilter(DeleteIcebergPartsFilterRequest request) throws MbxException {
        return matchBoxClient.deleteIcebergPartsFilter(request);
    }

    @GetMapping(value = "/postNumAlgoOrdersFilter", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public MbxResponse<Void> postNumAlgoOrdersFilter(PostNumAlgoOrdersFilterRequest request) throws MbxException {
        return matchBoxClient.postNumAlgoOrdersFilter(request);
    }

    @GetMapping(value = "/postNumIcebergOrders", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public MbxResponse<Void> postNumIcebergOrders(PostNumIcebergOrdersRequest request) throws MbxException {
        return matchBoxClient.postNumIcebergOrders(request);
    }

    @GetMapping(value = "/deleteMarketLotSizeFilter", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public MbxResponse<Void> deleteMarketLotSizeFilter(DeleteMarketLotSizeFilterRequest request) throws MbxException {
        return matchBoxClient.deleteMarketLotSizeFilter(request);
    }

    @GetMapping(value = "/postUserDataStream", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public MbxResponse<PostUserDataStreamResponse> postUserDataStream(PostUserDataStreamRequest request) throws MbxException {
        return matchBoxClient.postUserDataStream(request);
    }

    @GetMapping(value = "/putUserDataStream", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public MbxResponse<Void> putUserDataStream(PutUserDataStreamRequest request) throws MbxException {
        return matchBoxClient.putUserDataStream(request);
    }

    @GetMapping(value = "/deleteLotSizeFilter", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public MbxResponse<Void> deleteLotSizeFilter(DeleteLotSizeFilterRequest request) throws MbxException {
        return matchBoxClient.deleteLotSizeFilter(request);
    }

    @GetMapping(value = "/deleteNumAlgoOrdersFilter", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public MbxResponse<Void> deleteNumAlgoOrdersFilter(DeleteNumAlgoOrdersFilterRequest request) throws MbxException {
        return matchBoxClient.deleteNumAlgoOrdersFilter(request);
    }

    @GetMapping(value = "/postMarketLotSizeFilter", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public MbxResponse<Void> postMarketLotSizeFilter(PostMarketLotSizeFilterRequest request) throws MbxException {
        return matchBoxClient.postMarketLotSizeFilter(request);
    }

    @GetMapping(value = "/getOrderRateLimit", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public MbxResponse<GetOrderRateLimitResponse> getOrderRateLimit(GetOrderRateLimitRequest request) throws MbxException {
        return matchBoxClient.getOrderRateLimit(request);
    }

    @GetMapping(value = "/getAuctionReports", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public MbxResponse<String> getAuctionReportsForString(GetAuctionReportsRequest request) throws MbxException {
        return matchBoxClient.getAuctionReportsForString(request);
    }

    @GetMapping(value = "/getAccountByExternalId", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public MbxResponse<List<GetAccountResponse>> getAccountByExternalId(GetAccountByExternalIdRequest request) throws MbxException {
        return matchBoxClient.getAccountByExternalId(request);
    }

    @GetMapping(value = "/getAccountByExternalIdV3", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public MbxResponse<List<GetAccountResponseV3>> getAccountByExternalIdV3(GetAccountByExternalIdRequest request) throws MbxException {
        return matchBoxClient.getAccountByExternalIdV3(request);
    }

    @GetMapping(value = "/getAccountSymbolsCommission", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public MbxResponse<List<GetAccountSymbolsCommissionResponse>> getAccountSymbolsCommission(GetAccountSymbolsCommissionRequest request) throws MbxException {
        return matchBoxClient.getAccountSymbolsCommission(request);
    }

    @GetMapping(value = "/putOrderRateLimit", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public MbxResponse<Void> putOrderRateLimit(PutOrderRateLimitRequest request) throws MbxException {
        return matchBoxClient.putOrderRateLimit(request);
    }

    @GetMapping(value = "/deleteOrderRateLimitAccount", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public MbxResponse<Void> deleteOrderRateLimitAccount(DeleteOrderRateLimitAccountRequest request) throws MbxException {
        return matchBoxClient.deleteOrderRateLimitAccount(request);
    }

    @GetMapping(value = "/putApiKeyPermissions", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public MbxResponse<Void> putApiKeyPermissions(PutApiKeyPermissionsRequest request) throws MbxException {
        return matchBoxClient.putApiKeyPermissions(request);
    }

    @GetMapping(value = "/getEstimateSymbol", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public MbxResponse<GetEstimateSymbolResponse> getEstimateSymbol(GetEstimateSymbolRequest request) throws MbxException {
        return matchBoxClient.getEstimateSymbol(request);
    }

    @GetMapping(value = "/putAccountSymbolsCommission", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public MbxResponse<Void> putAccountSymbolsCommission(PutAccountSymbolsCommissionRequest request) throws MbxException {
        return matchBoxClient.putAccountSymbolsCommission(request);
    }

    @GetMapping(value = "/getAccountTPlusLockState", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public MbxResponse<List<GetAccountTPlusLockStateResponse>> getAccountTPlusLockState(GetAccountTPlusLockStateRequest request) throws MbxException {
        return matchBoxClient.getAccountTPlusLockState(request);
    }

    @GetMapping(value = "/putOrderRateLimitAccount", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public MbxResponse<Void> putOrderRateLimitAccount(PutOrderRateLimitAccountRequest request) throws MbxException {
        return matchBoxClient.putOrderRateLimitAccount(request);
    }

    @GetMapping(value = "/getPing", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public MbxResponse<Void> getPing() throws MbxException {
        return matchBoxClient.getPing();
    }

    @GetMapping(value = "/getCommands", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public MbxResponse<List<GetCommandsResponse>> getCommands() throws MbxException {
        return matchBoxClient.getCommands();
    }

    @GetMapping(value = "/putExchangeGas", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public MbxResponse<Void> putExchangeGas(PutExchangeGasRequest request) throws MbxException {
        return matchBoxClient.putExchangeGas(request);
    }
}
