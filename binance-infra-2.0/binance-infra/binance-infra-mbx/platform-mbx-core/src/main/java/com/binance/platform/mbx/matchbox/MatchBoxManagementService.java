package com.binance.platform.mbx.matchbox;

import com.binance.master.error.GeneralCode;
import com.binance.platform.mbx.exception.MbxException;
import com.binance.platform.mbx.matchbox.model.MbxResponse;
import com.binance.platform.mbx.matchbox.model.mgmt.MbxApiKeyWhitelistRequest;
import com.binance.platform.mbx.matchbox.model.mgmt.MbxApiKeyWhitelistResult;
import com.binance.platform.mbx.matchbox.model.mgmt.MbxDeleteApiKeyWhitelistRequest;
import com.binance.platform.mbx.matchbox.model.mgmt.MbxDeleteOpenOrdersResponse;
import com.binance.platform.mbx.matchbox.model.mgmt.MbxGetAccountFromEngineV3Request;
import com.binance.platform.mbx.matchbox.model.mgmt.MbxGetAccountFromEngineV3Response;
import com.binance.platform.mbx.matchbox.model.mgmt.MbxGetAccountResult;
import com.binance.platform.mbx.matchbox.model.mgmt.MbxApiKeysRequest;
import com.binance.platform.mbx.matchbox.model.mgmt.MbxApiKeysResult;
import com.binance.platform.mbx.matchbox.model.mgmt.MbxDeleteApiKeyLockRequest;
import com.binance.platform.mbx.matchbox.model.mgmt.MbxDeleteApiKeyRequest;
import com.binance.platform.mbx.matchbox.model.mgmt.MbxDeleteApiKeyRuleRequest;
import com.binance.platform.mbx.matchbox.model.mgmt.MbxDeleteGasOptOutRequest;
import com.binance.platform.mbx.matchbox.model.mgmt.MbxDeleteIcebergPartsFilterRequest;
import com.binance.platform.mbx.matchbox.model.mgmt.MbxDeleteLotSizeFilterRequest;
import com.binance.platform.mbx.matchbox.model.mgmt.MbxDeleteMarketLotSizeFilterRequest;
import com.binance.platform.mbx.matchbox.model.mgmt.MbxDeleteMinNotionalFilterRequest;
import com.binance.platform.mbx.matchbox.model.mgmt.MbxDeleteNumAlgoOrdersFilterRequest;
import com.binance.platform.mbx.matchbox.model.mgmt.MbxDeleteNumIcebergOrdersRequest;
import com.binance.platform.mbx.matchbox.model.mgmt.MbxDeleteNumOrdersFilterRequest;
import com.binance.platform.mbx.matchbox.model.mgmt.MbxDeleteOcoOrderRequest;
import com.binance.platform.mbx.matchbox.model.mgmt.MbxDeleteOcoOrderResponse;
import com.binance.platform.mbx.matchbox.model.mgmt.MbxDeleteOpenOrdersRequest;
import com.binance.platform.mbx.matchbox.model.mgmt.MbxDeleteOrderRateLimitAccountRequest;
import com.binance.platform.mbx.matchbox.model.mgmt.MbxDeleteOrderRequest;
import com.binance.platform.mbx.matchbox.model.mgmt.MbxDeleteOrderResponse;
import com.binance.platform.mbx.matchbox.model.mgmt.MbxDeletePercentPriceFilterRequest;
import com.binance.platform.mbx.matchbox.model.mgmt.MbxDeletePositionFilterRequest;
import com.binance.platform.mbx.matchbox.model.mgmt.MbxDeletePriceFilterRequest;
import com.binance.platform.mbx.matchbox.model.mgmt.MbxDeleteTPlusSellFilterRequest;
import com.binance.platform.mbx.matchbox.model.mgmt.MbxDeleteUserDataStreamRequest;
import com.binance.platform.mbx.matchbox.model.mgmt.MbxGetAccountByExternalIdRequest;
import com.binance.platform.mbx.matchbox.model.mgmt.MbxGetAccountByExternalAccountIdV3Request;
import com.binance.platform.mbx.matchbox.model.mgmt.MbxGetAccountRequest;
import com.binance.platform.mbx.matchbox.model.mgmt.MbxGetAccountSymbolCommissionHistoryRequest;
import com.binance.platform.mbx.matchbox.model.mgmt.MbxGetAccountSymbolCommissionHistoryResponse;
import com.binance.platform.mbx.matchbox.model.mgmt.MbxGetAccountSymbolsCommissionRequest;
import com.binance.platform.mbx.matchbox.model.mgmt.MbxGetAccountSymbolsCommissionResponse;
import com.binance.platform.mbx.matchbox.model.mgmt.MbxGetAccountTPlusLockStateRequest;
import com.binance.platform.mbx.matchbox.model.mgmt.MbxGetAccountTPlusLockStateResponse;
import com.binance.platform.mbx.matchbox.model.mgmt.MbxGetAllAccountsRequest;
import com.binance.platform.mbx.matchbox.model.mgmt.MbxGetAllAccountsResponse;
import com.binance.platform.mbx.matchbox.model.mgmt.MbxGetAllOrdersRequest;
import com.binance.platform.mbx.matchbox.model.mgmt.MbxGetAllOrdersResponse;
import com.binance.platform.mbx.matchbox.model.mgmt.MbxGetApiKeyCheckRequest;
import com.binance.platform.mbx.matchbox.model.mgmt.MbxGetApiKeyCheckResult;
import com.binance.platform.mbx.matchbox.model.mgmt.MbxGetApiKeyLockRequest;
import com.binance.platform.mbx.matchbox.model.mgmt.MbxGetApiKeyLockResult;
import com.binance.platform.mbx.matchbox.model.mgmt.MbxGetAssetBalanceRequest;
import com.binance.platform.mbx.matchbox.model.mgmt.MbxGetAssetBalanceResponse;
import com.binance.platform.mbx.matchbox.model.mgmt.MbxGetAssetLedgerRequest;
import com.binance.platform.mbx.matchbox.model.mgmt.MbxGetAssetLedgerResponse;
import com.binance.platform.mbx.matchbox.model.mgmt.MbxGetAssetsRequest;
import com.binance.platform.mbx.matchbox.model.mgmt.MbxGetAssetsResponse;
import com.binance.platform.mbx.matchbox.model.mgmt.MbxGetAuctionReportsRequest;
import com.binance.platform.mbx.matchbox.model.mgmt.MbxGetBalanceLedgerRequest;
import com.binance.platform.mbx.matchbox.model.mgmt.MbxGetBalanceLedgerResponse;
import com.binance.platform.mbx.matchbox.model.mgmt.MbxGetCommandsRequest;
import com.binance.platform.mbx.matchbox.model.mgmt.MbxGetOcoAllOrderListRequest;
import com.binance.platform.mbx.matchbox.model.mgmt.MbxGetOcoAllOrderListResponse;
import com.binance.platform.mbx.matchbox.model.mgmt.MbxGetOcoOpenOrderListRequest;
import com.binance.platform.mbx.matchbox.model.mgmt.MbxGetOcoOpenOrderListResponse;
import com.binance.platform.mbx.matchbox.model.mgmt.MbxGetOcoOrderListRequest;
import com.binance.platform.mbx.matchbox.model.mgmt.MbxGetOcoOrderListResponse;
import com.binance.platform.mbx.matchbox.model.mgmt.MbxPostApiKeyWhitelistRequest;
import com.binance.platform.mbx.matchbox.model.mgmt.MbxPutAccountResponseV3;
import com.binance.platform.mbx.model.matchbox.GetCommandsResponse;
import com.binance.platform.mbx.matchbox.model.mgmt.MbxGetCommissionRequest;
import com.binance.platform.mbx.matchbox.model.mgmt.MbxGetCommissionResponse;
import com.binance.platform.mbx.matchbox.model.mgmt.MbxGetEstimateSymbolRequest;
import com.binance.platform.mbx.matchbox.model.mgmt.MbxGetEstimateSymbolResponse;
import com.binance.platform.mbx.matchbox.model.mgmt.MbxGetFiltersRequest;
import com.binance.platform.mbx.matchbox.model.mgmt.MbxGetFiltersResponse;
import com.binance.platform.mbx.matchbox.model.mgmt.MbxGetMsgAuthOrdersRequest;
import com.binance.platform.mbx.matchbox.model.mgmt.MbxGetMsgAuthOrdersResponse;
import com.binance.platform.mbx.matchbox.model.mgmt.MbxGetOpenOrdersRequest;
import com.binance.platform.mbx.matchbox.model.mgmt.MbxGetOpenOrdersResponse;
import com.binance.platform.mbx.matchbox.model.mgmt.MbxGetOrderRateLimitRequest;
import com.binance.platform.mbx.matchbox.model.mgmt.MbxGetOrderRateLimitResponse;
import com.binance.platform.mbx.matchbox.model.mgmt.MbxGetOrderRequest;
import com.binance.platform.mbx.matchbox.model.mgmt.MbxGetOrderResponse;
import com.binance.platform.mbx.matchbox.model.mgmt.MbxGetPingRequest;
import com.binance.platform.mbx.matchbox.model.mgmt.MbxGetSymbolCommissionHistoryRequest;
import com.binance.platform.mbx.matchbox.model.mgmt.MbxGetSymbolCommissionHistoryResponse;
import com.binance.platform.mbx.matchbox.model.mgmt.MbxGetSymbolCommissionRequest;
import com.binance.platform.mbx.matchbox.model.mgmt.MbxGetSymbolCommissionResponse;
import com.binance.platform.mbx.matchbox.model.mgmt.MbxGetSymbolHistoryRequest;
import com.binance.platform.mbx.matchbox.model.mgmt.MbxGetSymbolHistoryResponse;
import com.binance.platform.mbx.matchbox.model.mgmt.MbxGetSymbolOrdersRequest;
import com.binance.platform.mbx.matchbox.model.mgmt.MbxGetSymbolOrdersResponse;
import com.binance.platform.mbx.matchbox.model.mgmt.MbxGetSymbolsRequest;
import com.binance.platform.mbx.matchbox.model.mgmt.MbxGetSymbolsResponse;
import com.binance.platform.mbx.matchbox.model.mgmt.MbxGetTimeRequest;
import com.binance.platform.mbx.matchbox.model.mgmt.MbxGetTradesRequest;
import com.binance.platform.mbx.matchbox.model.mgmt.MbxGetTradesResponse;
import com.binance.platform.mbx.matchbox.model.mgmt.MbxGetUserTradesRequest;
import com.binance.platform.mbx.matchbox.model.mgmt.MbxGetUserTradesResponse;
import com.binance.platform.mbx.matchbox.model.mgmt.MbxPostAccountRequest;
import com.binance.platform.mbx.matchbox.model.mgmt.MbxPostAccountResult;
import com.binance.platform.mbx.matchbox.model.mgmt.MbxPostAccountV3Request;
import com.binance.platform.mbx.matchbox.model.mgmt.MbxPostAccountV3Response;
import com.binance.platform.mbx.matchbox.model.mgmt.MbxPostApiKeyRequest;
import com.binance.platform.mbx.matchbox.model.mgmt.MbxPostApiKeyResponse;
import com.binance.platform.mbx.matchbox.model.mgmt.MbxPostApiKeyRuleRequest;
import com.binance.platform.mbx.matchbox.model.mgmt.MbxPostAssetRequest;
import com.binance.platform.mbx.matchbox.model.mgmt.MbxPostBalanceBatchRequest;
import com.binance.platform.mbx.matchbox.model.mgmt.MbxPostBalanceBatchResponse;
import com.binance.platform.mbx.matchbox.model.mgmt.MbxPostBalanceRequest;
import com.binance.platform.mbx.matchbox.model.mgmt.MbxPostBalanceResponse;
import com.binance.platform.mbx.matchbox.model.mgmt.MbxPostIcebergPartsFilterRequest;
import com.binance.platform.mbx.matchbox.model.mgmt.MbxPostLotSizeFilterRequest;
import com.binance.platform.mbx.matchbox.model.mgmt.MbxPostMarketLotSizeFilterRequest;
import com.binance.platform.mbx.matchbox.model.mgmt.MbxPostMinNotionalFilterRequest;
import com.binance.platform.mbx.matchbox.model.mgmt.MbxPostNumAlgoOrdersFilterRequest;
import com.binance.platform.mbx.matchbox.model.mgmt.MbxPostNumIcebergOrdersRequest;
import com.binance.platform.mbx.matchbox.model.mgmt.MbxPostNumOrdersFilterRequest;
import com.binance.platform.mbx.matchbox.model.mgmt.MbxPostOcoOrderRequest;
import com.binance.platform.mbx.matchbox.model.mgmt.MbxPostOcoOrderResponse;
import com.binance.platform.mbx.matchbox.model.mgmt.MbxPostOrderRequest;
import com.binance.platform.mbx.matchbox.model.mgmt.MbxPostOrderResponse;
import com.binance.platform.mbx.matchbox.model.mgmt.MbxPostPercentPriceFilterRequest;
import com.binance.platform.mbx.matchbox.model.mgmt.MbxPostPositionFilterRequest;
import com.binance.platform.mbx.matchbox.model.mgmt.MbxPostPriceFilterRequest;
import com.binance.platform.mbx.matchbox.model.mgmt.MbxPostSymbolRequest;
import com.binance.platform.mbx.matchbox.model.mgmt.MbxPostTPlusSellFilterRequest;
import com.binance.platform.mbx.matchbox.model.mgmt.MbxPostUserDataStreamRequest;
import com.binance.platform.mbx.matchbox.model.mgmt.MbxPostUserDataStreamResponse;
import com.binance.platform.mbx.matchbox.model.mgmt.MbxPutAccountAssetRequest;
import com.binance.platform.mbx.matchbox.model.mgmt.MbxPutAccountCommissionRequest;
import com.binance.platform.mbx.matchbox.model.mgmt.MbxPutAccountRequest;
import com.binance.platform.mbx.matchbox.model.mgmt.MbxPutAccountSymbolsCommissionRequest;
import com.binance.platform.mbx.matchbox.model.mgmt.MbxPutAccountV3Request;
import com.binance.platform.mbx.matchbox.model.mgmt.MbxPutAllSymbolsRequest;
import com.binance.platform.mbx.matchbox.model.mgmt.MbxPutApiKeyLockRequest;
import com.binance.platform.mbx.matchbox.model.mgmt.MbxPutApiKeyPermissionsRequest;
import com.binance.platform.mbx.matchbox.model.mgmt.MbxPutExchangeGasRequest;
import com.binance.platform.mbx.matchbox.model.mgmt.MbxPutExchangeRateRequest;
import com.binance.platform.mbx.matchbox.model.mgmt.MbxPutGasOptOutRequest;
import com.binance.platform.mbx.matchbox.model.mgmt.MbxPutOrderRateLimitAccountRequest;
import com.binance.platform.mbx.matchbox.model.mgmt.MbxPutOrderRateLimitRequest;
import com.binance.platform.mbx.matchbox.model.mgmt.MbxPutOrderTypesRequest;
import com.binance.platform.mbx.matchbox.model.mgmt.MbxPutSymbolCommissionRequest;
import com.binance.platform.mbx.matchbox.model.mgmt.MbxPutSymbolCommissionTypeRequest;
import com.binance.platform.mbx.matchbox.model.mgmt.MbxPutSymbolLimitsRequest;
import com.binance.platform.mbx.matchbox.model.mgmt.MbxPutSymbolPermissionsRequest;
import com.binance.platform.mbx.matchbox.model.mgmt.MbxPutSymbolRequest;
import com.binance.platform.mbx.matchbox.model.mgmt.MbxPutUserDataStreamRequest;
import com.binance.platform.mbx.matchbox.model.mgmt.MgmtGetTimeResponse;
import com.binance.platform.mbx.matchbox.processor.MbxRequestProcessor;
import com.binance.platform.mbx.matchbox.processor.MbxRequestProcessorHook;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;

import java.util.List;

/**
 * @author Li Fenggang
 * Date: 2020/6/30 4:17 下午
 */
public class MatchBoxManagementService {
    private final MbxRequestProcessor mbxRequestProcessor;

    @Value("${com.binance.matchbox.api.url}")
    private String managementRootUrl;

    public MatchBoxManagementService(MbxRequestProcessor mbxRequestProcessor) {
        this.mbxRequestProcessor = mbxRequestProcessor;
    }

    private String getManagementRootUrl() {
        return managementRootUrl;
    }

    /**
     * Retrieving API keys
     *
     * @param request
     * @return
     * @throws MbxException
     */
    public MbxResponse<List<MbxApiKeysResult>> getApiKeys(MbxApiKeysRequest request) throws MbxException {
        return mbxRequestProcessor.process(getManagementRootUrl(), HttpMethod.GET, request, new TypeReference<List<MbxApiKeysResult>>() {});
    }

    /**
     * Setting Account Asset Update
     *
     * @param request
     * @return
     * @throws MbxException
     */
    public MbxResponse<Void> putAccountAsset(MbxPutAccountAssetRequest request) throws MbxException {
        return mbxRequestProcessor.process(getManagementRootUrl(), HttpMethod.PUT, request, new TypeReference<Void>() {});
    }

    /**
     * Retrieving the balance ledger of an account
     *
     * @param request
     * @return
     * @throws MbxException
     */
    public MbxResponse<List<MbxGetBalanceLedgerResponse>> getBalanceLedger(MbxGetBalanceLedgerRequest request) throws MbxException {
        return mbxRequestProcessor.process(getManagementRootUrl(), HttpMethod.GET, request,
                new TypeReference<List<MbxGetBalanceLedgerResponse>>() {},
                new MbxRequestProcessorHook() {
                    public void checkParam(MbxGetBalanceLedgerRequest request) {
                        Long startTime = request.getStartTime();
                        Long endTime = request.getEndTime();
                        if (startTime != null || endTime != null) {
                            if (startTime == null || endTime == null || startTime > endTime) {
                                LOGGER.error("startTime must be less than endTime");
                                throw new MbxException(GeneralCode.SYS_VALID);
                            }
                        }

                        Long fromExternalUpdateId = request.getFromExternalUpdateId();
                        Long toExternalUpdateId = request.getToExternalUpdateId();
                        if (fromExternalUpdateId != null || toExternalUpdateId != null) {
                            if (fromExternalUpdateId == null || toExternalUpdateId == null) {
                                LOGGER.error("fromExternalUpdateId and toExternalUpdateId must both be null or botn not be null.");
                            }
                        }
                    }
                });
    }

    /**
     * Retrieving details regarding if there if the Api Key is locked.
     *
     * @param request
     * @return
     * @throws MbxException
     */
    public MbxResponse<MbxGetApiKeyLockResult> getApiKeyLock(MbxGetApiKeyLockRequest request) throws MbxException {
        return mbxRequestProcessor.process(getManagementRootUrl(), HttpMethod.GET,
                request,
                new TypeReference<MbxGetApiKeyLockResult>(){});
    }

    /**
     * Setting a lock on the account
     *
     * @param request
     * @return
     * @throws MbxException
     */
    public MbxResponse<Void> putApiKeyLock(MbxPutApiKeyLockRequest request) throws MbxException {
        return mbxRequestProcessor.process(getManagementRootUrl(), HttpMethod.PUT,
                request,
                new TypeReference<Void>(){});
    }

    /**
     * Deleting a lock on the account
     *
     * @param request
     * @return
     * @throws MbxException
     */
    public MbxResponse<Void> deleteApiKeyLock(MbxDeleteApiKeyLockRequest request) throws MbxException {
        return mbxRequestProcessor.process(getManagementRootUrl(), HttpMethod.DELETE,
                request,
                new TypeReference<Void>(){});
    }

    /**
     * Retrieving the API Key Details via the API Key
     *
     * @param request
     * @return
     * @throws MbxException
     */
    public MbxResponse<MbxGetApiKeyCheckResult> getApiKeyCheck(MbxGetApiKeyCheckRequest request) throws MbxException {
        return mbxRequestProcessor.process(getManagementRootUrl(), HttpMethod.GET,
                request,
                new TypeReference<MbxGetApiKeyCheckResult>(){});
    }

    /**
     * Creating a new account (v1)
     *
     * @param request
     * @return
     * @throws MbxException
     */
    public MbxResponse<MbxPostAccountResult> postAccount(MbxPostAccountRequest request) throws MbxException {
        return mbxRequestProcessor.process(getManagementRootUrl(), HttpMethod.POST,
                request,
                new TypeReference<MbxPostAccountResult>(){});
    }

    /**
     * Creating a new account (v3)
     *
     * @param request
     * @return
     * @throws MbxException
     */
    public MbxResponse<MbxPostAccountV3Response> postAccountV3(MbxPostAccountV3Request request) throws MbxException {
        return mbxRequestProcessor.process(getManagementRootUrl(), HttpMethod.POST,
                request,
                new TypeReference<MbxPostAccountV3Response>(){});
    }

    /**
     * Modifying Account Permissions (v1)
     *
     * @param request
     * @return
     * @throws MbxException
     */
    public MbxResponse<Void> putAccount(MbxPutAccountRequest request) throws MbxException {
        return mbxRequestProcessor.process(getManagementRootUrl(), HttpMethod.PUT,
                request,
                new TypeReference<Void>(){});
    }

    /**
     * Modifying Account Permissions (v3)
     *
     * @param request
     * @return
     * @throws MbxException
     */
    public MbxResponse<MbxPutAccountResponseV3> putAccountV3(MbxPutAccountV3Request request) throws MbxException {
        return mbxRequestProcessor.process(getManagementRootUrl(), HttpMethod.PUT,
                request,
                new TypeReference<MbxPutAccountResponseV3>(){});
    }

    /**
     * Retrieving account details via AccountId
     *
     * @param request
     * @return
     * @throws MbxException
     */
    public MbxResponse<MbxGetAccountResult> getAccount(MbxGetAccountRequest request) throws MbxException {
        return mbxRequestProcessor.process(getManagementRootUrl(), HttpMethod.GET,
                request,
                new TypeReference<MbxGetAccountResult>(){});
    }

    /**
     * Retrieving account details via AccountId
     *
     * @param request
     * @return
     * @throws MbxException
     */
    public MbxResponse<MbxGetAccountFromEngineV3Response> getAccountFromEngineV3(MbxGetAccountFromEngineV3Request request) throws MbxException {
        return mbxRequestProcessor.process(getManagementRootUrl(), HttpMethod.GET,
                request,
                new TypeReference<MbxGetAccountFromEngineV3Response>(){});
    }

    /**
     * Retrieving account details via AccountId
     *
     * @param request
     * @return
     * @throws MbxException
     */
    public MbxResponse<List<MbxGetAccountResult>> getAccountByExternalAccountIdV3(
            MbxGetAccountByExternalAccountIdV3Request request) throws MbxException {
        return mbxRequestProcessor.process(getManagementRootUrl(), HttpMethod.GET,
                request,
                new TypeReference<List<MbxGetAccountResult>>(){});
    }

    /**
     * Setting the GasOptOut
     *
     * @param request
     * @return
     * @throws MbxException
     */
    public MbxResponse<Void> putGasOptOut(MbxPutGasOptOutRequest request) throws MbxException {
        return mbxRequestProcessor.process(getManagementRootUrl(), HttpMethod.PUT,
                request,
                new TypeReference<Void>(){});
    }

    /**
     * Removing the GasOptOut
     *
     * @param request
     * @return
     * @throws MbxException
     */
    public MbxResponse<Void> deleteGasOptOut(MbxDeleteGasOptOutRequest request) throws MbxException {
        return mbxRequestProcessor.process(getManagementRootUrl(), HttpMethod.DELETE,
                request,
                new TypeReference<Void>(){});
    }

    /**
     * Setting the Api Key IP Address
     *
     * @param request
     * @return
     * @throws MbxException
     */
    public MbxResponse<Void> postApiKeyRule(MbxPostApiKeyRuleRequest request) throws MbxException {
        return mbxRequestProcessor.process(getManagementRootUrl(), HttpMethod.POST,
                request,
                new TypeReference<Void>(){});
    }

    /**
     * Deleting the Api Key Rule
     *
     * @param request
     * @return
     * @throws MbxException
     */
    public MbxResponse<Void> deleteApiKeyRule(MbxDeleteApiKeyRuleRequest request) throws MbxException {
        return mbxRequestProcessor.process(getManagementRootUrl(), HttpMethod.DELETE,
                request,
                new TypeReference<Void>(){});
    }

    /**
     * Setting commission on the account
     *
     * @param request
     * @return
     * @throws MbxException
     */
    public MbxResponse<Void> putAccountCommission(MbxPutAccountCommissionRequest request) throws MbxException {
        return mbxRequestProcessor.process(getManagementRootUrl(), HttpMethod.PUT,
                request,
                new TypeReference<Void>(){});
    }

    /**
     * Retrieving the Server time
     *
     * @param request
     * @return
     * @throws MbxException
     */
    public MbxResponse<MgmtGetTimeResponse> getTime(MbxGetTimeRequest request) throws MbxException {
        return mbxRequestProcessor.process(getManagementRootUrl(), HttpMethod.GET,
                request,
                new TypeReference<MgmtGetTimeResponse>(){});
    }

    /**
     * Retrieving all Orders on a Symbol
     *
     * @param request
     * @return
     * @throws MbxException
     */
    public MbxResponse<List<MbxGetSymbolOrdersResponse>> getSymbolOrders(MbxGetSymbolOrdersRequest request) throws MbxException {
        return mbxRequestProcessor.process(getManagementRootUrl(), HttpMethod.GET,
                request,
                new TypeReference<List<MbxGetSymbolOrdersResponse>>(){});
    }

    /**
     * Updating the asset balance on an account
     *
     * @param request
     * @return
     * @throws MbxException
     */
    public MbxResponse<MbxPostBalanceResponse> postBalance(MbxPostBalanceRequest request) throws MbxException {
        return mbxRequestProcessor.process(getManagementRootUrl(), HttpMethod.POST,
                request,
                new TypeReference<MbxPostBalanceResponse>(){});
    }

    /**
     * Batching of Balance Updates
     *
     * @param request
     * @return
     * @throws MbxException
     */
    public MbxResponse<List<MbxPostBalanceBatchResponse>> postBalanceBatch(MbxPostBalanceBatchRequest request) throws MbxException {
        return mbxRequestProcessor.process(getManagementRootUrl(), HttpMethod.POST,
                request,
                new TypeReference<List<MbxPostBalanceBatchResponse>>(){});
    }

    /**
     * Canceling a Single Order
     *
     * @param request
     * @return
     * @throws MbxException
     */
    public MbxResponse<MbxDeleteOrderResponse> deleteOrder(MbxDeleteOrderRequest request) throws MbxException {
        return mbxRequestProcessor.process(getManagementRootUrl(), HttpMethod.DELETE,
                request,
                new TypeReference<MbxDeleteOrderResponse>(){});
    }

    /**
     * Canceling a Single Order
     *
     * @param request
     * @return
     * @throws MbxException
     */
    public MbxResponse<String> deleteOrderForString(MbxDeleteOrderRequest request) throws MbxException {
        return mbxRequestProcessor.process(getManagementRootUrl(), HttpMethod.DELETE,
                request,
                new TypeReference<String>(){});
    }

    /**
     * Canceling the entire OCO
     *
     * @param request
     * @return
     * @throws MbxException
     */
    public MbxResponse<MbxDeleteOcoOrderResponse> deleteOcoOrder(MbxDeleteOcoOrderRequest request) throws MbxException {
        return mbxRequestProcessor.process(getManagementRootUrl(), HttpMethod.DELETE,
                request,
                new TypeReference<MbxDeleteOcoOrderResponse>(){});
    }

    /**
     * Canceling the entire OCO
     *
     * @param request
     * @return
     * @throws MbxException
     */
    public MbxResponse<String> deleteOcoOrderForString(MbxDeleteOcoOrderRequest request) throws MbxException {
        return mbxRequestProcessor.process(getManagementRootUrl(), HttpMethod.DELETE,
                request,
                new TypeReference<String>(){});
    }

    /**
     * Canceling All Open Orders based on an Account and Symbol
     *
     * @param request
     * @return
     * @throws MbxException
     */
    public MbxResponse<List<MbxDeleteOpenOrdersResponse>> deleteOpenOrders(MbxDeleteOpenOrdersRequest request) throws MbxException {
        return mbxRequestProcessor.process(getManagementRootUrl(), HttpMethod.DELETE,
                request,
                new TypeReference<List<MbxDeleteOpenOrdersResponse>>(){});
    }

    /**
     *
     * @param request
     * @return
     * @throws MbxException
     */
    public MbxResponse<List<MbxGetAllOrdersResponse>> getAllOrders(MbxGetAllOrdersRequest request) throws MbxException {
        return mbxRequestProcessor.process(getManagementRootUrl(), HttpMethod.GET,
                request,
                new TypeReference<List<MbxGetAllOrdersResponse>>(){});
    }

    public MbxResponse<MbxGetOcoOrderListResponse> getOcoOrderList(MbxGetOcoOrderListRequest request) throws MbxException {
        return mbxRequestProcessor.process(getManagementRootUrl(), HttpMethod.GET,
                request,
                new TypeReference<MbxGetOcoOrderListResponse>(){});
    }

    public MbxResponse<List<MbxGetOcoOpenOrderListResponse>> getOcoOpenOrderList(MbxGetOcoOpenOrderListRequest request) throws MbxException {
        return mbxRequestProcessor.process(getManagementRootUrl(), HttpMethod.GET,
                request,
                new TypeReference<List<MbxGetOcoOpenOrderListResponse>>(){});
    }

    public MbxResponse<List<MbxGetOcoAllOrderListResponse>> getOcoAllOrderList(MbxGetOcoAllOrderListRequest request) throws MbxException {
        return mbxRequestProcessor.process(getManagementRootUrl(), HttpMethod.GET,
                request,
                new TypeReference<List<MbxGetOcoAllOrderListResponse>>(){});
    }

    public MbxResponse<MbxPostOrderResponse> postOrder(MbxPostOrderRequest request) throws MbxException {
        return mbxRequestProcessor.process(getManagementRootUrl(), HttpMethod.POST,
                request,
                new TypeReference<MbxPostOrderResponse>(){});
    }

    public MbxResponse<MbxPostOcoOrderResponse> postOcoOrder(MbxPostOcoOrderRequest request) throws MbxException {
        return mbxRequestProcessor.process(getManagementRootUrl(), HttpMethod.POST,
                request,
                new TypeReference<MbxPostOcoOrderResponse>(){});
    }

    public MbxResponse<List<MbxGetAllAccountsResponse>> getAllAccounts(MbxGetAllAccountsRequest request) throws MbxException {
        return mbxRequestProcessor.process(getManagementRootUrl(), HttpMethod.GET,
                request,
                new TypeReference<List<MbxGetAllAccountsResponse>>(){});
    }

    public MbxResponse<List<MbxGetOpenOrdersResponse>> getOpenOrders(MbxGetOpenOrdersRequest request) throws MbxException {
        return mbxRequestProcessor.process(getManagementRootUrl(), HttpMethod.GET,
                request,
                new TypeReference<List<MbxGetOpenOrdersResponse>>(){});
    }

    public MbxResponse<List<MbxGetMsgAuthOrdersResponse>> getMsgAuthOrders(MbxGetMsgAuthOrdersRequest request) throws MbxException {
        return mbxRequestProcessor.process(getManagementRootUrl(), HttpMethod.GET,
                request,
                new TypeReference<List<MbxGetMsgAuthOrdersResponse>>(){});
    }

    public MbxResponse<Void> putSymbolLimits(MbxPutSymbolLimitsRequest request) throws MbxException {
        return mbxRequestProcessor.process(getManagementRootUrl(), HttpMethod.PUT,
                request,
                new TypeReference<Void>(){});
    }

    public MbxResponse<MbxGetOrderResponse> getOrder(MbxGetOrderRequest request) throws MbxException {
        return mbxRequestProcessor.process(getManagementRootUrl(), HttpMethod.GET,
                request,
                new TypeReference<MbxGetOrderResponse>(){});
    }

    public MbxResponse<MbxPostApiKeyResponse> postApiKey(MbxPostApiKeyRequest request) throws MbxException {
        return mbxRequestProcessor.process(getManagementRootUrl(), HttpMethod.POST,
                request,
                new TypeReference<MbxPostApiKeyResponse>(){});
    }

    public MbxResponse<Void> postApiKeyWhitelist(MbxPostApiKeyWhitelistRequest request) throws MbxException {
        return mbxRequestProcessor.process(getManagementRootUrl(), HttpMethod.POST,
                request,
                new TypeReference<Void>(){});
    }

    public MbxResponse<Void> deleteApiKeyWhitelist(MbxDeleteApiKeyWhitelistRequest request) throws MbxException {
        return mbxRequestProcessor.process(getManagementRootUrl(), HttpMethod.DELETE,
                request,
                new TypeReference<Void>(){});
    }

    /**
     * Retrieving API key whitelist
     *
     * @param request
     * @return
     * @throws MbxException
     */
    public MbxResponse<MbxApiKeyWhitelistResult> getApiKeyWhitelist(MbxApiKeyWhitelistRequest request) throws MbxException {
        return mbxRequestProcessor.process(getManagementRootUrl(), HttpMethod.GET, request, new TypeReference<MbxApiKeyWhitelistResult>() {});
    }

    public MbxResponse<Void> postPriceFilter(MbxPostPriceFilterRequest request) throws MbxException {
        return mbxRequestProcessor.process(getManagementRootUrl(), HttpMethod.POST,
                request,
                new TypeReference<Void>(){});
    }

    public MbxResponse<Void> postAsset(MbxPostAssetRequest request) throws MbxException {
        return mbxRequestProcessor.process(getManagementRootUrl(), HttpMethod.POST,
                request,
                new TypeReference<Void>(){});
    }

    public MbxResponse<MbxGetAssetBalanceResponse> getAssetBalance(MbxGetAssetBalanceRequest request) throws MbxException {
        return mbxRequestProcessor.process(getManagementRootUrl(), HttpMethod.GET,
                request,
                new TypeReference<MbxGetAssetBalanceResponse>(){});
    }

    public MbxResponse<List<MbxGetAssetLedgerResponse>> getAssetLedger(MbxGetAssetLedgerRequest request) throws MbxException {
        return mbxRequestProcessor.process(getManagementRootUrl(), HttpMethod.GET,
                request,
                new TypeReference<List<MbxGetAssetLedgerResponse>>(){});
    }

    public MbxResponse<Void> putAllSymbols(MbxPutAllSymbolsRequest request) throws MbxException {
        return mbxRequestProcessor.process(getManagementRootUrl(), HttpMethod.PUT,
                request,
                new TypeReference<Void>(){});
    }

    /**
     * Creating a new asset.<br/>
     * Response:
     * <pre>
     * {
     *   "asset": "1528700331071",
     *   "free": "0.000000",
     *   "locked": "10.000000"
     * }
     * </pre>
     *
     * @param request
     * @return
     * @throws MbxException
     */
    public MbxResponse<Void> postSymbol(MbxPostSymbolRequest request) throws MbxException {
        return mbxRequestProcessor.process(getManagementRootUrl(), HttpMethod.POST,
                request,
                new TypeReference<Void>(){});
    }

    public MbxResponse<List<MbxGetSymbolsResponse>> getSymbols(MbxGetSymbolsRequest request) throws MbxException {
        return mbxRequestProcessor.process(getManagementRootUrl(), HttpMethod.GET,
                request,
                new TypeReference<List<MbxGetSymbolsResponse>>(){});
    }

    public MbxResponse<MbxGetFiltersResponse> getFilters(MbxGetFiltersRequest request) throws MbxException {
        return mbxRequestProcessor.process(getManagementRootUrl(), HttpMethod.GET,
                request,
                new TypeReference<MbxGetFiltersResponse>(){});
    }

    public MbxResponse<List<MbxGetSymbolHistoryResponse>> getSymbolHistory(MbxGetSymbolHistoryRequest request) throws MbxException {
        return mbxRequestProcessor.process(getManagementRootUrl(), HttpMethod.GET,
                request,
                new TypeReference<List<MbxGetSymbolHistoryResponse>>(){});
    }

    public MbxResponse<Void> putExchangeRate(MbxPutExchangeRateRequest request) throws MbxException {
        return mbxRequestProcessor.process(getManagementRootUrl(), HttpMethod.PUT,
                request,
                new TypeReference<Void>(){});
    }

    public MbxResponse<List<MbxGetCommissionResponse>> getCommission(MbxGetCommissionRequest request) throws MbxException {
        return mbxRequestProcessor.process(getManagementRootUrl(), HttpMethod.GET,
                request,
                new TypeReference<List<MbxGetCommissionResponse>>(){});
    }

    public MbxResponse<List<MbxGetUserTradesResponse>> getUserTrades(MbxGetUserTradesRequest request) throws MbxException {
        return mbxRequestProcessor.process(getManagementRootUrl(), HttpMethod.GET,
                request,
                new TypeReference<List<MbxGetUserTradesResponse>>(){});
    }

    public MbxResponse<Void> putOrderTypes(MbxPutOrderTypesRequest request) throws MbxException {
        return mbxRequestProcessor.process(getManagementRootUrl(), HttpMethod.PUT,
                request,
                new TypeReference<Void>(){});
    }

    public MbxResponse<List<MbxGetAssetsResponse>> getAssets(MbxGetAssetsRequest request) throws MbxException {
        return mbxRequestProcessor.process(getManagementRootUrl(), HttpMethod.GET,
                request,
                new TypeReference<List<MbxGetAssetsResponse>>(){});
    }

    public MbxResponse<Void> deleteApiKey(MbxDeleteApiKeyRequest request) throws MbxException {
        return mbxRequestProcessor.process(getManagementRootUrl(), HttpMethod.DELETE,
                request,
                new TypeReference<Void>(){});
    }

    public MbxResponse<Void> putSymbol(MbxPutSymbolRequest request) throws MbxException {
        return mbxRequestProcessor.process(getManagementRootUrl(), HttpMethod.PUT,
                request,
                new TypeReference<Void>(){});
    }

    public MbxResponse<Void> putSymbolPermissions(MbxPutSymbolPermissionsRequest request) throws MbxException {
        return mbxRequestProcessor.process(getManagementRootUrl(), HttpMethod.PUT,
                request,
                new TypeReference<Void>(){});
    }

    public MbxResponse<List<MbxGetTradesResponse>> getTrades(MbxGetTradesRequest request) throws MbxException {
        return mbxRequestProcessor.process(getManagementRootUrl(), HttpMethod.GET,
                request,
                new TypeReference<List<MbxGetTradesResponse>>(){});
    }

    public MbxResponse<List<MbxGetAccountSymbolCommissionHistoryResponse>> getAccountSymbolCommissionHistory(MbxGetAccountSymbolCommissionHistoryRequest request) throws MbxException {
        return mbxRequestProcessor.process(getManagementRootUrl(), HttpMethod.GET,
                request,
                new TypeReference<List<MbxGetAccountSymbolCommissionHistoryResponse>>(){});
    }

    public MbxResponse<Void> putSymbolCommissionType(MbxPutSymbolCommissionTypeRequest request) throws MbxException {
        return mbxRequestProcessor.process(getManagementRootUrl(), HttpMethod.PUT,
                request,
                new TypeReference<Void>(){});
    }

    public MbxResponse<List<MbxGetSymbolCommissionHistoryResponse>> getSymbolCommissionHistory(MbxGetSymbolCommissionHistoryRequest request) throws MbxException {
        return mbxRequestProcessor.process(getManagementRootUrl(), HttpMethod.GET,
                request,
                new TypeReference<List<MbxGetSymbolCommissionHistoryResponse>>(){});
    }

    public MbxResponse<Void> deleteNumOrdersFilter(MbxDeleteNumOrdersFilterRequest request) throws MbxException {
        return mbxRequestProcessor.process(getManagementRootUrl(), HttpMethod.DELETE,
                request,
                new TypeReference<Void>(){});
    }

    public MbxResponse<Void> postLotSizeFilter(MbxPostLotSizeFilterRequest request) throws MbxException {
        return mbxRequestProcessor.process(getManagementRootUrl(), HttpMethod.POST,
                request,
                new TypeReference<Void>(){});
    }

    public MbxResponse<Void> postPercentPriceFilter(MbxPostPercentPriceFilterRequest request) throws MbxException {
        return mbxRequestProcessor.process(getManagementRootUrl(), HttpMethod.POST,
                request,
                new TypeReference<Void>(){});
    }

    public MbxResponse<Void> deleteTPlusSellFilter(MbxDeleteTPlusSellFilterRequest request) throws MbxException {
        return mbxRequestProcessor.process(getManagementRootUrl(), HttpMethod.DELETE,
                request,
                new TypeReference<Void>(){});
    }

    public MbxResponse<Void> deletePercentPriceFilter(MbxDeletePercentPriceFilterRequest request) throws MbxException {
        return mbxRequestProcessor.process(getManagementRootUrl(), HttpMethod.DELETE,
                request,
                new TypeReference<Void>(){});
    }

    public MbxResponse<Void> postPositionFilter(MbxPostPositionFilterRequest request) throws MbxException {
        return mbxRequestProcessor.process(getManagementRootUrl(), HttpMethod.POST,
                request,
                new TypeReference<Void>(){});
    }

    public MbxResponse<MbxGetSymbolCommissionResponse> getSymbolCommission(MbxGetSymbolCommissionRequest request) throws MbxException {
        return mbxRequestProcessor.process(getManagementRootUrl(), HttpMethod.GET,
                request,
                new TypeReference<MbxGetSymbolCommissionResponse>(){});
    }

    public MbxResponse<Void> postMinNotionalFilter(MbxPostMinNotionalFilterRequest request) throws MbxException {
        return mbxRequestProcessor.process(getManagementRootUrl(), HttpMethod.POST,
                request,
                new TypeReference<Void>(){});
    }

    public MbxResponse<Void> deleteNumIcebergOrders(MbxDeleteNumIcebergOrdersRequest request) throws MbxException {
        return mbxRequestProcessor.process(getManagementRootUrl(), HttpMethod.DELETE,
                request,
                new TypeReference<Void>(){});
    }

    public MbxResponse<Void> putSymbolCommission(MbxPutSymbolCommissionRequest request) throws MbxException {
        return mbxRequestProcessor.process(getManagementRootUrl(), HttpMethod.PUT,
                request,
                new TypeReference<Void>(){});
    }

    public MbxResponse<Void> postNumOrdersFilter(MbxPostNumOrdersFilterRequest request) throws MbxException {
        return mbxRequestProcessor.process(getManagementRootUrl(), HttpMethod.POST,
                request,
                new TypeReference<Void>(){});
    }

    public MbxResponse<Void> deletePriceFilter(MbxDeletePriceFilterRequest request) throws MbxException {
        return mbxRequestProcessor.process(getManagementRootUrl(), HttpMethod.DELETE,
                request,
                new TypeReference<Void>(){});
    }

    public MbxResponse<Void> deleteMinNotionalFilter(MbxDeleteMinNotionalFilterRequest request) throws MbxException {
        return mbxRequestProcessor.process(getManagementRootUrl(), HttpMethod.DELETE,
                request,
                new TypeReference<Void>(){});
    }

    public MbxResponse<Void> postTPlusSellFilter(MbxPostTPlusSellFilterRequest request) throws MbxException {
        return mbxRequestProcessor.process(getManagementRootUrl(), HttpMethod.POST,
                request,
                new TypeReference<Void>(){});
    }

    public MbxResponse<Void> postIcebergPartsFilter(MbxPostIcebergPartsFilterRequest request) throws MbxException {
        return mbxRequestProcessor.process(getManagementRootUrl(), HttpMethod.POST,
                request,
                new TypeReference<Void>(){});
    }

    public MbxResponse<Void> deleteUserDataStream(MbxDeleteUserDataStreamRequest request) throws MbxException {
        return mbxRequestProcessor.process(getManagementRootUrl(), HttpMethod.DELETE,
                request,
                new TypeReference<Void>(){});
    }

    public MbxResponse<Void> deletePositionFilter(MbxDeletePositionFilterRequest request) throws MbxException {
        return mbxRequestProcessor.process(getManagementRootUrl(), HttpMethod.DELETE,
                request,
                new TypeReference<Void>(){});
    }

    public MbxResponse<Void> deleteIcebergPartsFilter(MbxDeleteIcebergPartsFilterRequest request) throws MbxException {
        return mbxRequestProcessor.process(getManagementRootUrl(), HttpMethod.DELETE,
                request,
                new TypeReference<Void>(){});
    }

    public MbxResponse<Void> postNumAlgoOrdersFilter(MbxPostNumAlgoOrdersFilterRequest request) throws MbxException {
        return mbxRequestProcessor.process(getManagementRootUrl(), HttpMethod.POST,
                request,
                new TypeReference<Void>(){});
    }

    public MbxResponse<Void> postNumIcebergOrders(MbxPostNumIcebergOrdersRequest request) throws MbxException {
        return mbxRequestProcessor.process(getManagementRootUrl(), HttpMethod.POST,
                request,
                new TypeReference<Void>(){});
    }

    public MbxResponse<Void> deleteMarketLotSizeFilter(MbxDeleteMarketLotSizeFilterRequest request) throws MbxException {
        return mbxRequestProcessor.process(getManagementRootUrl(), HttpMethod.DELETE,
                request,
                new TypeReference<Void>(){});
    }

    public MbxResponse<MbxPostUserDataStreamResponse> postUserDataStream(MbxPostUserDataStreamRequest request) throws MbxException {
        return mbxRequestProcessor.process(getManagementRootUrl(), HttpMethod.POST,
                request,
                new TypeReference<MbxPostUserDataStreamResponse>(){});
    }

    public MbxResponse<Void> putUserDataStream(MbxPutUserDataStreamRequest request) throws MbxException {
        return mbxRequestProcessor.process(getManagementRootUrl(), HttpMethod.PUT,
                request,
                new TypeReference<Void>(){});
    }

    public MbxResponse<Void> deleteLotSizeFilter(MbxDeleteLotSizeFilterRequest request) throws MbxException {
        return mbxRequestProcessor.process(getManagementRootUrl(), HttpMethod.DELETE,
                request,
                new TypeReference<Void>(){});
    }

    public MbxResponse<Void> deleteNumAlgoOrdersFilter(MbxDeleteNumAlgoOrdersFilterRequest request) throws MbxException {
        return mbxRequestProcessor.process(getManagementRootUrl(), HttpMethod.DELETE,
                request,
                new TypeReference<Void>(){});
    }

    public MbxResponse<Void> postMarketLotSizeFilter(MbxPostMarketLotSizeFilterRequest request) throws MbxException {
        return mbxRequestProcessor.process(getManagementRootUrl(), HttpMethod.POST,
                request,
                new TypeReference<Void>(){});
    }

    public MbxResponse<MbxGetOrderRateLimitResponse> getOrderRateLimit(MbxGetOrderRateLimitRequest request) throws MbxException {
        return mbxRequestProcessor.process(getManagementRootUrl(), HttpMethod.GET,
                request,
                new TypeReference<MbxGetOrderRateLimitResponse>(){});
    }

    public MbxResponse<String> getAuctionReportsForString(MbxGetAuctionReportsRequest request) throws MbxException {
        return mbxRequestProcessor.process(getManagementRootUrl(), HttpMethod.GET,
                request,
                new TypeReference<String>(){});
    }

    public MbxResponse<List<MbxGetAccountResult>> getAccountByExternalId(MbxGetAccountByExternalIdRequest request) throws MbxException {
        return mbxRequestProcessor.process(getManagementRootUrl(), HttpMethod.GET,
                request,
                new TypeReference<List<MbxGetAccountResult>>(){});
    }

    public MbxResponse<List<MbxGetAccountSymbolsCommissionResponse>> getAccountSymbolsCommission(MbxGetAccountSymbolsCommissionRequest request) throws MbxException {
        return mbxRequestProcessor.process(getManagementRootUrl(), HttpMethod.GET,
                request,
                new TypeReference<List<MbxGetAccountSymbolsCommissionResponse>>(){});
    }

    public MbxResponse<Void> putOrderRateLimit(MbxPutOrderRateLimitRequest request) throws MbxException {
        return mbxRequestProcessor.process(getManagementRootUrl(), HttpMethod.PUT,
                request,
                new TypeReference<Void>(){});
    }

    public MbxResponse<Void> deleteOrderRateLimitAccount(MbxDeleteOrderRateLimitAccountRequest request) throws MbxException {
        return mbxRequestProcessor.process(getManagementRootUrl(), HttpMethod.DELETE,
                request,
                new TypeReference<Void>(){});
    }

    public MbxResponse<Void> putApiKeyPermissions(MbxPutApiKeyPermissionsRequest request) throws MbxException {
        return mbxRequestProcessor.process(getManagementRootUrl(), HttpMethod.PUT,
                request,
                new TypeReference<Void>(){});
    }

    public MbxResponse<MbxGetEstimateSymbolResponse> getEstimateSymbol(MbxGetEstimateSymbolRequest request) throws MbxException {
        return mbxRequestProcessor.process(getManagementRootUrl(), HttpMethod.GET,
                request,
                new TypeReference<MbxGetEstimateSymbolResponse>(){});
    }

    public MbxResponse<Void> putAccountSymbolsCommission(MbxPutAccountSymbolsCommissionRequest request) throws MbxException {
        return mbxRequestProcessor.process(getManagementRootUrl(), HttpMethod.PUT,
                request,
                new TypeReference<Void>(){});
    }

    public MbxResponse<List<MbxGetAccountTPlusLockStateResponse>> getAccountTPlusLockState(MbxGetAccountTPlusLockStateRequest request) throws MbxException {
        return mbxRequestProcessor.process(getManagementRootUrl(), HttpMethod.GET,
                request,
                new TypeReference<List<MbxGetAccountTPlusLockStateResponse>>(){});
    }

    public MbxResponse<Void> putOrderRateLimitAccount(MbxPutOrderRateLimitAccountRequest request) throws MbxException {
        return mbxRequestProcessor.process(getManagementRootUrl(), HttpMethod.PUT,
                request,
                new TypeReference<Void>(){});
    }

    public MbxResponse<Void> getPing(MbxGetPingRequest request) throws MbxException {
        return mbxRequestProcessor.process(getManagementRootUrl(), HttpMethod.GET,
                request,
                new TypeReference<Void>(){});
    }

    public MbxResponse<List<GetCommandsResponse>> getCommands(MbxGetCommandsRequest request) throws MbxException {
        return mbxRequestProcessor.process(getManagementRootUrl(), HttpMethod.GET,
                request,
                new TypeReference<List<GetCommandsResponse>>(){});
    }

    public MbxResponse<Void> putExchangeGas(MbxPutExchangeGasRequest request) throws MbxException {
        return mbxRequestProcessor.process(getManagementRootUrl(), HttpMethod.PUT,
                request,
                new TypeReference<Void>(){});
    }

}
