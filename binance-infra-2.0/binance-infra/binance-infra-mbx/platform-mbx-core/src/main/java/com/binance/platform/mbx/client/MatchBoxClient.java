package com.binance.platform.mbx.client;

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
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;


/**
 * matchbox mgmt interfaces. There is no business logic, just forwarding.
 */
public interface MatchBoxClient {

    @PostMapping(value = "/putAccountCommission", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public MbxResponse<Void> putAccountCommission(PutAccountCommissionRequest request)
            throws MbxException;

    @PostMapping(value = "/getTime", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public MbxResponse<GetTimeResponse> getTime() throws MbxException;

    @PostMapping(value = "/getSymbolOrders", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public MbxResponse<List<GetSymbolOrdersResponse>> getSymbolOrders(GetSymbolOrdersRequest request)
            throws MbxException;

    @PostMapping(value = "/postBalance", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public MbxResponse<PostBalanceResponse> postBalance(PostBalanceRequest request) throws MbxException;

    @PostMapping(value = "/postBalanceBatch", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    MbxResponse<List<PostBalanceBatchResponse>> postBalanceBatch(PostBalanceBatchRequest request);

    @PostMapping(value = "/deleteOrder", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public MbxResponse<DeleteOrderResponse> deleteOrder(DeleteOrderRequest request) throws MbxException;

    @PostMapping(value = "/deleteOcoOrder", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public MbxResponse<DeleteOcoOrderResponse> delOcoOrder(DeleteOcoOrderReq request) throws MbxException;

    @PostMapping(value = "/deleteOpenOrders", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public MbxResponse<DeleteOpenOrdersResponse> deleteOpenOrders(DeleteOpenOrdersRequest request)
            throws MbxException;

    @PostMapping(value = "/getAllOrders", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public MbxResponse<List<GetAllOrdersResponse>> getAllOrders(GetAllOrdersRequest request) throws MbxException;

    @PostMapping(value = "/getOcoOrderList", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    MbxResponse<GetOcoOrderListResponse> getOcoOrderList(GetOcoOrderListRequest request) throws MbxException;

    @PostMapping(value = "/getOcoOpenOrderList", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    MbxResponse<List<GetOcoOpenOrderListResponse>> getOcoOpenOrderList(GetOcoOpenOrderListRequest request) throws MbxException;

    @PostMapping(value = "/getOcoAllOrderList", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    MbxResponse<List<GetOcoAllOrderListResponse>> getOcoAllOrderList(GetOcoAllOrderListRequest request) throws MbxException;

    @PostMapping(value = "/postOrder", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public MbxResponse<PostOrderResponse> postOrder(PostOrderRequest request) throws MbxException;

    @PostMapping(value = "/postOcoOrder", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    MbxResponse<PostOcoOrderResponse> postOcoOrder(PostOcoOrderRequest request) throws MbxException;

    @PostMapping(value = "/getBalanceLedger", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public MbxResponse<List<GetBalanceLedgerResponse>> getBalanceLedger(GetBalanceLedgerRequest request)
            throws MbxException;

    @PostMapping(value = "/getAllAccounts", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public MbxResponse<List<GetAllAccountsResponse>> getAllAccounts() throws MbxException;

    @PostMapping(value = "/putAccountAsset", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public MbxResponse<Void> putAccountAsset(PutAccountAssetRequest request) throws MbxException;

    @PostMapping(value = "/postAccount", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public MbxResponse<PostAccountResponse> postAccount(PostAccountRequest request) throws MbxException;


    @PostMapping(value = "/postAccountV3", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public MbxResponse<PostAccountResponseV3> postAccountV3(PostAccountRequestV3 request) throws MbxException;

    @PostMapping(value = "/getOpenOrders", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public MbxResponse<List<GetOpenOrdersResponse>> getOpenOrders(GetOpenOrdersRequest request) throws MbxException;

    @PostMapping(value = "/getMsgAuthOrders", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public MbxResponse<List<GetMsgAuthOrdersResponse>> getMsgAuthOrders(GetMsgAuthOrdersRequest request) throws MbxException;

    @PostMapping(value = "/putSymbolLimits", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public MbxResponse<Void> putSymbolLimits(PutSymbolLimitsRequest request) throws MbxException;

    @PostMapping(value = "/putAccount", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public MbxResponse<Void> putAccount(PutAccountRequest request) throws MbxException;

    @PostMapping(value = "/putAccountV3", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public MbxResponse<PutAccountResponseV3> putAccountV3(PutAccountRequestV3 request) throws MbxException;

    @PostMapping(value = "/getOrder", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public MbxResponse<GetOrderResponse> getOrder(GetOrderRequest request) throws MbxException;

    @PostMapping(value = "/getAccount", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public MbxResponse<GetAccountResponse> getAccount(GetAccountRequest request) throws MbxException;

    @PostMapping(value = "/getAccountFromEngineV3", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    MbxResponse<GetAccountFromEngineV3Response> getAccountFromEngineV3(GetAccountFromEngineV3Request request) throws MbxException;

    @PostMapping(value = "/putGasOptOut", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public MbxResponse<Void> putGasOptOut(PutGasOptOutRequest request) throws MbxException;

    @PostMapping(value = "/deleteGasOptOut", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public MbxResponse<Void> deleteGasOptOut(DeleteGasOptOutRequest request) throws MbxException;

    @PostMapping(value = "/postApiKey", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public MbxResponse<PostApiKeyResponse> postApiKey(PostApiKeyRequest request) throws MbxException;

    @PostMapping(value = "/getApiKeys", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public MbxResponse<List<GetApiKeysResponse>> getApiKeys(GetApiKeysRequest request) throws MbxException;

    @GetMapping(value = "/postApiKeyWhitelist", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    MbxResponse<Void> postApiKeyWhitelist(PostApiKeyWhitelistRequest request) throws MbxException;

    @GetMapping(value = "/deleteApiKeyWhitelist", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    MbxResponse<Void> deleteApiKeyWhitelist(DeleteApiKeyWhitelistRequest request) throws MbxException;

    @GetMapping(value = "/getApiKeyWhitelist", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    MbxResponse<GetApiKeyWhitelistResponse> getApiKeyWhitelist(GetApiKeyWhitelistRequest request) throws MbxException;

    @PostMapping(value = "/putApiKeyLock", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public MbxResponse<Void> putApiKeyLock(PutApiKeyLockRequest request) throws MbxException;

    @PostMapping(value = "/postPriceFilter", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public MbxResponse<Void> postPriceFilter(PostPriceFilterRequest request)
            throws MbxException;

    @PostMapping(value = "/postAsset", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public MbxResponse<Void> postAsset(PostAssetRequest request) throws MbxException;

    @PostMapping(value = "/getApiKeyLock", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public MbxResponse<GetApiKeyLockResponse> getApiKeyLock(GetApiKeyLockRequest request) throws MbxException;

    @PostMapping(value = "/getAssetBalance", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public MbxResponse<GetAssetBalanceResponse> getAssetBalance(GetAssetBalanceRequest request)
            throws MbxException;

    @PostMapping(value = "/getAssetLedger", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public MbxResponse<List<GetAssetLedgerResponse>> getAssetLedger(GetAssetLedgerRequest request) throws MbxException;

    /**
     * Setting trading status for ALL Symbols
     *
     * @param request
     * @return
     * @throws MbxException
     */
    @PostMapping(value = "/putAllSymbols", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public MbxResponse<Void> putAllSymbols(PutAllSymbolsRequest request) throws MbxException;

    @PostMapping(value = "/postSymbol", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public MbxResponse<Void> postSymbol(PostSymbolRequest request) throws MbxException;

    @PostMapping(value = "/postApiKeyRule", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public MbxResponse<Void> postApiKeyRule(PostApiKeyRuleRequest request) throws MbxException;

    @PostMapping(value = "/deleteApiKeyLock", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public MbxResponse<Void> deleteApiKeyLock(DeleteApiKeyLockRequest request)
            throws MbxException;

    @PostMapping(value = "/getSymbols", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public MbxResponse<List<GetSymbolsResponse>> getSymbols() throws MbxException;

    @PostMapping(value = "/getApiKeyCheck", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public MbxResponse<GetApiKeyCheckResponse> getApiKeyCheck(GetApiKeyCheckRequest request) throws MbxException;

    @PostMapping(value = "/getFilters", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public MbxResponse<GetFiltersResponse> getFilters(GetFiltersRequest request) throws MbxException;

    @PostMapping(value = "/deleteApiKeyRule", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public MbxResponse<Void> deleteApiKeyRule(DeleteApiKeyRuleRequest request)
            throws MbxException;

    @PostMapping(value = "/getSymbolHistory", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public MbxResponse<List<GetSymbolHistoryResponse>> getSymbolHistory(GetSymbolHistoryRequest request)
            throws MbxException;

    @PostMapping(value = "/putExchangeRate", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public MbxResponse<Void> putExchangeRate(PutExchangeRateRequest request) throws MbxException;

    @PostMapping(value = "/getCommission", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public MbxResponse<List<GetCommissionResponse>> getCommission(GetCommissionRequest request) throws MbxException;

    @PostMapping(value = "/getUserTrades", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public MbxResponse<List<GetUserTradesResponse>> getUserTrades(GetUserTradesRequest request) throws MbxException;

    @PostMapping(value = "/putOrderTypes", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public MbxResponse<Void> putOrderTypes(PutOrderTypesRequest request) throws MbxException;

    @PostMapping(value = "/getAssets", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public MbxResponse<List<GetAssetsResponse>> getAssets() throws MbxException;

    @PostMapping(value = "/deleteApiKey", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public MbxResponse<Void> deleteApiKey(DeleteApiKeyRequest request) throws MbxException;

    @PostMapping(value = "/putSymbol", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public MbxResponse<Void> putSymbol(PutSymbolRequest request) throws MbxException;

    @PostMapping(value = "/putSymbolPermissions", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public MbxResponse<Void> putSymbolPermissions(PutSymbolPermissionsRequest request) throws MbxException;

    @PostMapping(value = "/getTrades", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public MbxResponse<List<GetTradesResponse>> getTrades(GetTradesRequest request) throws MbxException;

    @PostMapping(value = "/getAccountSymbolCommissionHistory", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public MbxResponse<List<GetAccountSymbolCommissionHistoryResponse>> getAccountSymbolCommissionHistory(
            GetAccountSymbolCommissionHistoryRequest request) throws MbxException;

    @PostMapping(value = "/putSymbolCommissionType", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public MbxResponse<Void> putSymbolCommissionType(PutSymbolCommissionTypeRequest request)
            throws MbxException;

    @PostMapping(value = "/getSymbolCommissionHistory", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public MbxResponse<List<GetSymbolCommissionHistoryResponse>> getSymbolCommissionHistory(
            GetSymbolCommissionHistoryRequest request) throws MbxException;

    @PostMapping(value = "/deleteNumOrdersFilter", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public MbxResponse<Void> deleteNumOrdersFilter(DeleteNumOrdersFilterRequest request)
            throws MbxException;

    @PostMapping(value = "/postLotSizeFilter", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public MbxResponse<Void> postLotSizeFilter(PostLotSizeFilterRequest request)
            throws MbxException;

    @PostMapping(value = "/postPercentPriceFilter", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public MbxResponse<Void> postPercentPriceFilter(PostPercentPriceFilterRequest request)
            throws MbxException;

    @PostMapping(value = "/deleteTPlusSellFilter", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public MbxResponse<Void> deleteTPlusSellFilter(DeleteTPlusSellFilterRequest request)
            throws MbxException;

    @PostMapping(value = "/deletePercentPriceFilter", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public MbxResponse<Void> deletePercentPriceFilter(DeletePercentPriceFilterRequest request)
            throws MbxException;

    @PostMapping(value = "/postPositionFilter", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public MbxResponse<Void> postPositionFilter(PostPositionFilterRequest request)
            throws MbxException;

    @PostMapping(value = "/getSymbolCommission", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public MbxResponse<GetSymbolCommissionResponse> getSymbolCommission(GetSymbolCommissionRequest request)
            throws MbxException;

    @PostMapping(value = "/postMinNotionalFilter", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public MbxResponse<Void> postMinNotionalFilter(PostMinNotionalFilterRequest request)
            throws MbxException;

    @PostMapping(value = "/deleteNumIcebergOrders", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public MbxResponse<Void> deleteNumIcebergOrders(DeleteNumIcebergOrdersRequest request)
            throws MbxException;

    @PostMapping(value = "/putSymbolCommission", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public MbxResponse<Void> putSymbolCommission(PutSymbolCommissionRequest request)
            throws MbxException;

    @PostMapping(value = "/postNumOrdersFilter", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public MbxResponse<Void> postNumOrdersFilter(PostNumOrdersFilterRequest request)
            throws MbxException;

    @PostMapping(value = "/deletePriceFilter", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public MbxResponse<Void> deletePriceFilter(DeletePriceFilterRequest request)
            throws MbxException;

    @PostMapping(value = "/deleteMinNotionalFilter", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public MbxResponse<Void> deleteMinNotionalFilter(DeleteMinNotionalFilterRequest request)
            throws MbxException;

    @PostMapping(value = "/postTPlusSellFilter", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public MbxResponse<Void> postTPlusSellFilter(PostTPlusSellFilterRequest request)
            throws MbxException;

    @PostMapping(value = "/postIcebergPartsFilter", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public MbxResponse<Void> postIcebergPartsFilter(PostIcebergPartsFilterRequest request)
            throws MbxException;

    @PostMapping(value = "/deleteUserDataStream", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public MbxResponse<Void> deleteUserDataStream(DeleteUserDataStreamRequest request)
            throws MbxException;

    @PostMapping(value = "/deletePositionFilter", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public MbxResponse<Void> deletePositionFilter(DeletePositionFilterRequest request)
            throws MbxException;

    @PostMapping(value = "/deleteIcebergPartsFilter", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public MbxResponse<Void> deleteIcebergPartsFilter(DeleteIcebergPartsFilterRequest request)
            throws MbxException;

    @PostMapping(value = "/postNumAlgoOrdersFilter", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public MbxResponse<Void> postNumAlgoOrdersFilter(PostNumAlgoOrdersFilterRequest request)
            throws MbxException;

    @PostMapping(value = "/postNumIcebergOrders", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public MbxResponse<Void> postNumIcebergOrders(PostNumIcebergOrdersRequest request)
            throws MbxException;

    @PostMapping(value = "/deleteMarketLotSizeFilter", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public MbxResponse<Void> deleteMarketLotSizeFilter(
            DeleteMarketLotSizeFilterRequest request) throws MbxException;

    @PostMapping(value = "/postUserDataStream", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public MbxResponse<PostUserDataStreamResponse> postUserDataStream(PostUserDataStreamRequest request)
            throws MbxException;

    /**
     * Keep Alive User Data Stream
     *
     * @param request
     * @return
     * @throws MbxException
     */
    @PostMapping(value = "/putUserDataStream", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public MbxResponse<Void> putUserDataStream(PutUserDataStreamRequest request)
            throws MbxException;

    @PostMapping(value = "/deleteLotSizeFilter", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public MbxResponse<Void> deleteLotSizeFilter(DeleteLotSizeFilterRequest request)
            throws MbxException;

    @PostMapping(value = "/deleteNumAlgoOrdersFilter", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public MbxResponse<Void> deleteNumAlgoOrdersFilter(
            DeleteNumAlgoOrdersFilterRequest request) throws MbxException;

    @PostMapping(value = "/postMarketLotSizeFilter", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public MbxResponse<Void> postMarketLotSizeFilter(PostMarketLotSizeFilterRequest request)
            throws MbxException;

    @PostMapping(value = "/getOrderRateLimit", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public MbxResponse<GetOrderRateLimitResponse> getOrderRateLimit(GetOrderRateLimitRequest request)
            throws MbxException;

    @PostMapping(value = "/getAuctionReports", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public MbxResponse<String> getAuctionReportsForString(GetAuctionReportsRequest request)
            throws MbxException;

    @PostMapping(value = "/getAccountByExternalId", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public MbxResponse<List<GetAccountResponse>> getAccountByExternalId(GetAccountByExternalIdRequest request)
            throws MbxException;

    @PostMapping(value = "/getAccountByExternalIdV3", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public MbxResponse<List<GetAccountResponseV3>> getAccountByExternalIdV3(GetAccountByExternalIdRequest request)
            throws MbxException;

    @PostMapping(value = "/getAccountSymbolsCommission", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public MbxResponse<List<GetAccountSymbolsCommissionResponse>> getAccountSymbolsCommission(
            GetAccountSymbolsCommissionRequest request) throws MbxException;

    @PostMapping(value = "/putOrderRateLimit", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public MbxResponse<Void> putOrderRateLimit(PutOrderRateLimitRequest request)
            throws MbxException;

    @PostMapping(value = "/deleteOrderRateLimitAccount", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public MbxResponse<Void> deleteOrderRateLimitAccount(
            DeleteOrderRateLimitAccountRequest request) throws MbxException;

    @PostMapping(value = "/putApiKeyPermissions", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public MbxResponse<Void> putApiKeyPermissions(PutApiKeyPermissionsRequest request)
            throws MbxException;

    @PostMapping(value = "/getEstimateSymbol", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public MbxResponse<GetEstimateSymbolResponse> getEstimateSymbol(GetEstimateSymbolRequest request)
            throws MbxException;

    @PostMapping(value = "/putAccountSymbolsCommission", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public MbxResponse<Void> putAccountSymbolsCommission(
            PutAccountSymbolsCommissionRequest request) throws MbxException;

    @PostMapping(value = "/getAccountTPlusLockState", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public MbxResponse<List<GetAccountTPlusLockStateResponse>> getAccountTPlusLockState(
            GetAccountTPlusLockStateRequest request) throws MbxException;

    @PostMapping(value = "/putOrderRateLimitAccount", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public MbxResponse<Void> putOrderRateLimitAccount(PutOrderRateLimitAccountRequest request)
            throws MbxException;

    @PostMapping(value = "/getPing", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public MbxResponse<Void> getPing() throws MbxException;

    @PostMapping(value = "/getCommands", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public MbxResponse<List<GetCommandsResponse>> getCommands() throws MbxException;

    @PostMapping(value = "/putExchangeGas", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public MbxResponse<Void> putExchangeGas(PutExchangeGasRequest request) throws MbxException;

}
