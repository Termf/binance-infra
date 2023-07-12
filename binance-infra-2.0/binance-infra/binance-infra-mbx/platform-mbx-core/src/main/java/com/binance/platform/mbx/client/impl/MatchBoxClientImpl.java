package com.binance.platform.mbx.client.impl;

import com.binance.master.error.GeneralCode;
import com.binance.platform.mbx.client.MatchBoxClient;
import com.binance.platform.mbx.exception.MbxException;
import com.binance.platform.mbx.matchbox.GetAssetsResponseConverter;
import com.binance.platform.mbx.matchbox.MatchBoxManagementService;
import com.binance.platform.mbx.matchbox.model.MbxResponse;
import com.binance.platform.mbx.matchbox.model.mgmt.MbxApiKeyWhitelistRequest;
import com.binance.platform.mbx.matchbox.model.mgmt.MbxApiKeyWhitelistResult;
import com.binance.platform.mbx.matchbox.model.mgmt.MbxApiKeysRequest;
import com.binance.platform.mbx.matchbox.model.mgmt.MbxApiKeysResult;
import com.binance.platform.mbx.matchbox.model.mgmt.MbxDeleteApiKeyLockRequest;
import com.binance.platform.mbx.matchbox.model.mgmt.MbxDeleteApiKeyRequest;
import com.binance.platform.mbx.matchbox.model.mgmt.MbxDeleteApiKeyRuleRequest;
import com.binance.platform.mbx.matchbox.model.mgmt.MbxDeleteApiKeyWhitelistRequest;
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
import com.binance.platform.mbx.matchbox.model.mgmt.MbxDeleteOpenOrdersResponse;
import com.binance.platform.mbx.matchbox.model.mgmt.MbxDeleteOrderRateLimitAccountRequest;
import com.binance.platform.mbx.matchbox.model.mgmt.MbxDeleteOrderRequest;
import com.binance.platform.mbx.matchbox.model.mgmt.MbxDeleteOrderResponse;
import com.binance.platform.mbx.matchbox.model.mgmt.MbxDeletePercentPriceFilterRequest;
import com.binance.platform.mbx.matchbox.model.mgmt.MbxDeletePositionFilterRequest;
import com.binance.platform.mbx.matchbox.model.mgmt.MbxDeletePriceFilterRequest;
import com.binance.platform.mbx.matchbox.model.mgmt.MbxDeleteTPlusSellFilterRequest;
import com.binance.platform.mbx.matchbox.model.mgmt.MbxDeleteUserDataStreamRequest;
import com.binance.platform.mbx.matchbox.model.mgmt.MbxGetAccountByExternalAccountIdV3Request;
import com.binance.platform.mbx.matchbox.model.mgmt.MbxGetAccountByExternalIdRequest;
import com.binance.platform.mbx.matchbox.model.mgmt.MbxGetAccountFromEngineV3Request;
import com.binance.platform.mbx.matchbox.model.mgmt.MbxGetAccountFromEngineV3Response;
import com.binance.platform.mbx.matchbox.model.mgmt.MbxGetAccountRequest;
import com.binance.platform.mbx.matchbox.model.mgmt.MbxGetAccountResult;
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
import com.binance.platform.mbx.matchbox.model.mgmt.MbxGetCommissionRequest;
import com.binance.platform.mbx.matchbox.model.mgmt.MbxGetCommissionResponse;
import com.binance.platform.mbx.matchbox.model.mgmt.MbxGetEstimateSymbolRequest;
import com.binance.platform.mbx.matchbox.model.mgmt.MbxGetEstimateSymbolResponse;
import com.binance.platform.mbx.matchbox.model.mgmt.MbxGetFiltersRequest;
import com.binance.platform.mbx.matchbox.model.mgmt.MbxGetFiltersResponse;
import com.binance.platform.mbx.matchbox.model.mgmt.MbxGetMsgAuthOrdersRequest;
import com.binance.platform.mbx.matchbox.model.mgmt.MbxGetMsgAuthOrdersResponse;
import com.binance.platform.mbx.matchbox.model.mgmt.MbxGetOcoAllOrderListRequest;
import com.binance.platform.mbx.matchbox.model.mgmt.MbxGetOcoAllOrderListResponse;
import com.binance.platform.mbx.matchbox.model.mgmt.MbxGetOcoOpenOrderListRequest;
import com.binance.platform.mbx.matchbox.model.mgmt.MbxGetOcoOpenOrderListResponse;
import com.binance.platform.mbx.matchbox.model.mgmt.MbxGetOcoOrderListRequest;
import com.binance.platform.mbx.matchbox.model.mgmt.MbxGetOcoOrderListResponse;
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
import com.binance.platform.mbx.matchbox.model.mgmt.MbxPostApiKeyWhitelistRequest;
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
import com.binance.platform.mbx.matchbox.model.mgmt.MbxPutAccountResponseV3;
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
import com.binance.platform.mbx.model.matchbox.DeleteApiKeyLockRequest;
import com.binance.platform.mbx.model.matchbox.DeleteApiKeyRequest;
import com.binance.platform.mbx.model.matchbox.DeleteApiKeyRuleRequest;
import com.binance.platform.mbx.model.matchbox.DeleteApiKeyWhitelistRequest;
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
import com.binance.platform.mbx.model.matchbox.DeleteOcoOrderResponseConverter;
import com.binance.platform.mbx.model.matchbox.DeleteOpenOrdersRequest;
import com.binance.platform.mbx.model.matchbox.DeleteOpenOrdersResponse;
import com.binance.platform.mbx.model.matchbox.DeleteOpenOrdersResponseConverter;
import com.binance.platform.mbx.model.matchbox.DeleteOrderRateLimitAccountRequest;
import com.binance.platform.mbx.model.matchbox.DeleteOrderRequest;
import com.binance.platform.mbx.model.matchbox.DeleteOrderResponse;
import com.binance.platform.mbx.model.matchbox.DeleteOrderResponseConverter;
import com.binance.platform.mbx.model.matchbox.DeletePercentPriceFilterRequest;
import com.binance.platform.mbx.model.matchbox.DeletePositionFilterRequest;
import com.binance.platform.mbx.model.matchbox.DeletePriceFilterRequest;
import com.binance.platform.mbx.model.matchbox.DeleteTPlusSellFilterRequest;
import com.binance.platform.mbx.model.matchbox.DeleteUserDataStreamRequest;
import com.binance.platform.mbx.model.matchbox.GetAccountByExternalIdRequest;
import com.binance.platform.mbx.model.matchbox.GetAccountFromEngineV3Converter;
import com.binance.platform.mbx.model.matchbox.GetAccountFromEngineV3Request;
import com.binance.platform.mbx.model.matchbox.GetAccountFromEngineV3Response;
import com.binance.platform.mbx.model.matchbox.GetAccountRequest;
import com.binance.platform.mbx.model.matchbox.GetAccountResponse;
import com.binance.platform.mbx.model.matchbox.GetAccountResponseConverter;
import com.binance.platform.mbx.model.matchbox.GetAccountResponseV3;
import com.binance.platform.mbx.model.matchbox.GetAccountResponseV3Converter;
import com.binance.platform.mbx.model.matchbox.GetAccountSymbolCommissionHistoryRequest;
import com.binance.platform.mbx.model.matchbox.GetAccountSymbolCommissionHistoryResponse;
import com.binance.platform.mbx.model.matchbox.GetAccountSymbolCommissionHistoryResponseVonverter;
import com.binance.platform.mbx.model.matchbox.GetAccountSymbolsCommissionRequest;
import com.binance.platform.mbx.model.matchbox.GetAccountSymbolsCommissionResponse;
import com.binance.platform.mbx.model.matchbox.GetAccountSymbolsCommissionResponseConverter;
import com.binance.platform.mbx.model.matchbox.GetAccountTPlusLockStateRequest;
import com.binance.platform.mbx.model.matchbox.GetAccountTPlusLockStateResponse;
import com.binance.platform.mbx.model.matchbox.GetAccountTPlusLockStateResponseConverter;
import com.binance.platform.mbx.model.matchbox.GetAllAccountsResponse;
import com.binance.platform.mbx.model.matchbox.GetAllAccountsResponseConverter;
import com.binance.platform.mbx.model.matchbox.GetAllOrdersRequest;
import com.binance.platform.mbx.model.matchbox.GetAllOrdersResponse;
import com.binance.platform.mbx.model.matchbox.GetAllOrdersResponseConverter;
import com.binance.platform.mbx.model.matchbox.GetApiKeyCheckRequest;
import com.binance.platform.mbx.model.matchbox.GetApiKeyCheckRequestConverter;
import com.binance.platform.mbx.model.matchbox.GetApiKeyCheckResponse;
import com.binance.platform.mbx.model.matchbox.GetApiKeyCheckResponseConverter;
import com.binance.platform.mbx.model.matchbox.GetApiKeyLockRequest;
import com.binance.platform.mbx.model.matchbox.GetApiKeyLockResponse;
import com.binance.platform.mbx.model.matchbox.GetApiKeyWhitelistRequest;
import com.binance.platform.mbx.model.matchbox.GetApiKeyWhitelistResponse;
import com.binance.platform.mbx.model.matchbox.GetApiKeyWhitelistResponseConverter;
import com.binance.platform.mbx.model.matchbox.GetApiKeysRequest;
import com.binance.platform.mbx.model.matchbox.GetApiKeysResponse;
import com.binance.platform.mbx.model.matchbox.GetApiKeysResponseConverter;
import com.binance.platform.mbx.model.matchbox.GetAssetBalanceRequest;
import com.binance.platform.mbx.model.matchbox.GetAssetBalanceResponse;
import com.binance.platform.mbx.model.matchbox.GetAssetBalanceResponseConverter;
import com.binance.platform.mbx.model.matchbox.GetAssetLedgerRequest;
import com.binance.platform.mbx.model.matchbox.GetAssetLedgerResponse;
import com.binance.platform.mbx.model.matchbox.GetAssetLedgerResponseConverter;
import com.binance.platform.mbx.model.matchbox.GetAssetsResponse;
import com.binance.platform.mbx.model.matchbox.GetAuctionReportsRequest;
import com.binance.platform.mbx.model.matchbox.GetBalanceLedgerRequest;
import com.binance.platform.mbx.model.matchbox.GetBalanceLedgerResponse;
import com.binance.platform.mbx.model.matchbox.GetBalanceLedgerResponseConverter;
import com.binance.platform.mbx.model.matchbox.GetCommandsResponse;
import com.binance.platform.mbx.model.matchbox.GetCommissionRequest;
import com.binance.platform.mbx.model.matchbox.GetCommissionResponse;
import com.binance.platform.mbx.model.matchbox.GetCommissionResponseConverter;
import com.binance.platform.mbx.model.matchbox.GetEstimateSymbolRequest;
import com.binance.platform.mbx.model.matchbox.GetEstimateSymbolResponse;
import com.binance.platform.mbx.model.matchbox.GetEstimateSymbolResponseConverter;
import com.binance.platform.mbx.model.matchbox.GetFiltersRequest;
import com.binance.platform.mbx.model.matchbox.GetFiltersResponse;
import com.binance.platform.mbx.model.matchbox.GetFiltersResponseConverter;
import com.binance.platform.mbx.model.matchbox.GetMsgAuthOrdersRequest;
import com.binance.platform.mbx.model.matchbox.GetMsgAuthOrdersResponse;
import com.binance.platform.mbx.model.matchbox.GetMsgAuthOrdersResponseConverter;
import com.binance.platform.mbx.model.matchbox.GetOcoAllOrderListRequest;
import com.binance.platform.mbx.model.matchbox.GetOcoAllOrderListResponse;
import com.binance.platform.mbx.model.matchbox.GetOcoAllOrderListResponseConverter;
import com.binance.platform.mbx.model.matchbox.GetOcoOpenOrderListRequest;
import com.binance.platform.mbx.model.matchbox.GetOcoOpenOrderListResponse;
import com.binance.platform.mbx.model.matchbox.GetOcoOpenOrderListResponseConverter;
import com.binance.platform.mbx.model.matchbox.GetOcoOrderListRequest;
import com.binance.platform.mbx.model.matchbox.GetOcoOrderListResponse;
import com.binance.platform.mbx.model.matchbox.GetOcoOrderListResponseConverter;
import com.binance.platform.mbx.model.matchbox.GetOpenOrdersRequest;
import com.binance.platform.mbx.model.matchbox.GetOpenOrdersResponse;
import com.binance.platform.mbx.model.matchbox.GetOpenOrdersResponseConverter;
import com.binance.platform.mbx.model.matchbox.GetOrderRateLimitRequest;
import com.binance.platform.mbx.model.matchbox.GetOrderRateLimitResponse;
import com.binance.platform.mbx.model.matchbox.GetOrderRateLimitResponseConverter;
import com.binance.platform.mbx.model.matchbox.GetOrderRequest;
import com.binance.platform.mbx.model.matchbox.GetOrderResponse;
import com.binance.platform.mbx.model.matchbox.GetOrderResponseConverter;
import com.binance.platform.mbx.model.matchbox.GetSymbolCommissionHistoryRequest;
import com.binance.platform.mbx.model.matchbox.GetSymbolCommissionHistoryResponse;
import com.binance.platform.mbx.model.matchbox.GetSymbolCommissionHistoryResponseConverter;
import com.binance.platform.mbx.model.matchbox.GetSymbolCommissionRequest;
import com.binance.platform.mbx.model.matchbox.GetSymbolCommissionResponse;
import com.binance.platform.mbx.model.matchbox.GetSymbolCommissionResponseConverter;
import com.binance.platform.mbx.model.matchbox.GetSymbolHistoryRequest;
import com.binance.platform.mbx.model.matchbox.GetSymbolHistoryResponse;
import com.binance.platform.mbx.model.matchbox.GetSymbolHistoryResponseConverter;
import com.binance.platform.mbx.model.matchbox.GetSymbolOrdersRequest;
import com.binance.platform.mbx.model.matchbox.GetSymbolOrdersResponse;
import com.binance.platform.mbx.model.matchbox.GetSymbolOrdersResponseConverter;
import com.binance.platform.mbx.model.matchbox.GetSymbolsResponse;
import com.binance.platform.mbx.model.matchbox.GetSymbolsResponseConverter;
import com.binance.platform.mbx.model.matchbox.GetTimeResponse;
import com.binance.platform.mbx.model.matchbox.GetTimeResponseConverter;
import com.binance.platform.mbx.model.matchbox.GetTradesRequest;
import com.binance.platform.mbx.model.matchbox.GetTradesResponse;
import com.binance.platform.mbx.model.matchbox.GetTradesResponseConverter;
import com.binance.platform.mbx.model.matchbox.GetUserTradesRequest;
import com.binance.platform.mbx.model.matchbox.GetUserTradesResponse;
import com.binance.platform.mbx.model.matchbox.GetUserTradesResponseConverter;
import com.binance.platform.mbx.model.matchbox.PostAccountRequest;
import com.binance.platform.mbx.model.matchbox.PostAccountRequestV3;
import com.binance.platform.mbx.model.matchbox.PostAccountResponse;
import com.binance.platform.mbx.model.matchbox.PostAccountResponseV3;
import com.binance.platform.mbx.model.matchbox.PostAccountResponseV3Converter;
import com.binance.platform.mbx.model.matchbox.PostApiKeyRequest;
import com.binance.platform.mbx.model.matchbox.PostApiKeyResponse;
import com.binance.platform.mbx.model.matchbox.PostApiKeyResponseConverter;
import com.binance.platform.mbx.model.matchbox.PostApiKeyRuleRequest;
import com.binance.platform.mbx.model.matchbox.PostApiKeyWhitelistRequest;
import com.binance.platform.mbx.model.matchbox.PostAssetRequest;
import com.binance.platform.mbx.model.matchbox.PostBalanceBatchRequest;
import com.binance.platform.mbx.model.matchbox.PostBalanceBatchResponse;
import com.binance.platform.mbx.model.matchbox.PostBalanceBatchResponseConverter;
import com.binance.platform.mbx.model.matchbox.PostBalanceRequest;
import com.binance.platform.mbx.model.matchbox.PostBalanceResponse;
import com.binance.platform.mbx.model.matchbox.PostBalanceResponseConverter;
import com.binance.platform.mbx.model.matchbox.PostIcebergPartsFilterRequest;
import com.binance.platform.mbx.model.matchbox.PostLotSizeFilterRequest;
import com.binance.platform.mbx.model.matchbox.PostMarketLotSizeFilterRequest;
import com.binance.platform.mbx.model.matchbox.PostMinNotionalFilterRequest;
import com.binance.platform.mbx.model.matchbox.PostNumAlgoOrdersFilterRequest;
import com.binance.platform.mbx.model.matchbox.PostNumIcebergOrdersRequest;
import com.binance.platform.mbx.model.matchbox.PostNumOrdersFilterRequest;
import com.binance.platform.mbx.model.matchbox.PostOcoOrderRequest;
import com.binance.platform.mbx.model.matchbox.PostOcoOrderResponse;
import com.binance.platform.mbx.model.matchbox.PostOcoOrderResponseConverter;
import com.binance.platform.mbx.model.matchbox.PostOrderRequest;
import com.binance.platform.mbx.model.matchbox.PostOrderResponse;
import com.binance.platform.mbx.model.matchbox.PostOrderResponseConverter;
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
import com.binance.platform.mbx.model.matchbox.PutAccountResponseV3Converter;
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
import com.binance.platform.mbx.util.JsonUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Li Fenggang
 * Date: 2020/7/15 5:02 下午
 */
public class MatchBoxClientImpl implements MatchBoxClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(MatchBoxClientImpl.class);
    private final MatchBoxManagementService matchBoxManagementService;

    public MatchBoxClientImpl(MatchBoxManagementService matchBoxManagementService) {
        this.matchBoxManagementService = matchBoxManagementService;
    }

    @Override
    public MbxResponse<Void> putAccountCommission(PutAccountCommissionRequest request) throws MbxException {
        MbxPutAccountCommissionRequest mbxRequest = new MbxPutAccountCommissionRequest(request.getAccountId(),
                request.getBuyerCommission(), request.getMakerCommission(), request.getSellerCommission(),
                request.getTakerCommission());
        MbxResponse<Void> mbxResponse = matchBoxManagementService.putAccountCommission(mbxRequest);
        return mbxResponse;
    }

    @Override
    public MbxResponse<GetTimeResponse> getTime() throws MbxException {
        MbxGetTimeRequest mbxRequest = new MbxGetTimeRequest();
        MbxResponse<MgmtGetTimeResponse> mbxResponse = matchBoxManagementService.getTime(mbxRequest);
        MbxResponse<GetTimeResponse> response = MbxResponse.of(mbxResponse.getState());
        if (response.isSuccess()) {
            GetTimeResponse getTimeResponse = GetTimeResponseConverter.convert(mbxResponse.getData());
            response.setData(getTimeResponse);
        }
        return response;
    }

    @Override
    public MbxResponse<List<GetSymbolOrdersResponse>> getSymbolOrders(GetSymbolOrdersRequest request) throws MbxException {
        MbxGetSymbolOrdersRequest mbxRequest = new MbxGetSymbolOrdersRequest(request.getSymbol());
        mbxRequest.setEndTime(request.getEndTime());
        mbxRequest.setFromId(request.getFromId());
        mbxRequest.setLimit(request.getLimit());
        mbxRequest.setStartTime(request.getStartTime());

        MbxResponse<List<MbxGetSymbolOrdersResponse>> mbxResponse = matchBoxManagementService.getSymbolOrders(mbxRequest);
        MbxResponse<List<GetSymbolOrdersResponse>> response = MbxResponse.of(mbxResponse.getState());
        if (response.isSuccess()) {
            List<MbxGetSymbolOrdersResponse> mbxList = mbxResponse.getData();
            List<GetSymbolOrdersResponse> targetList = new ArrayList<>(mbxList.size());
            for (MbxGetSymbolOrdersResponse mbxGetSymbolOrdersResponse : mbxList) {
                GetSymbolOrdersResponse target = GetSymbolOrdersResponseConverter.convert(mbxGetSymbolOrdersResponse);
                targetList.add(target);
            }
            response.setData(targetList);
        }
        return response;
    }

    @Override
    public MbxResponse<PostBalanceResponse> postBalance(PostBalanceRequest request) throws MbxException {
        MbxPostBalanceRequest mbxRequest = new MbxPostBalanceRequest(request.getAccountId(), request.getAsset(),
                request.getBalanceDelta(), request.getUpdateId());
        MbxResponse<MbxPostBalanceResponse> mbxResponse = matchBoxManagementService.postBalance(mbxRequest);
        MbxResponse<PostBalanceResponse> response = MbxResponse.of(mbxResponse.getState());
        if (response.isSuccess()) {
            response.setData(PostBalanceResponseConverter.convert(mbxResponse.getData()));
        }
        return response;
    }

    @Override
    public MbxResponse<List<PostBalanceBatchResponse>> postBalanceBatch(PostBalanceBatchRequest request) {
        List<PostBalanceBatchRequest.Balance> balanceList = request.getBalanceList();
        String jsonArray = null;
        try {
            jsonArray = JsonUtil.toJsonString(balanceList);
        } catch (JsonProcessingException e) {
            LOGGER.error("BalanceBatch serialize error", e);
            throw new MbxException(GeneralCode.SYS_ERROR);
        }
        Map<String, String> formData = new HashMap<>();
        formData.put("jsonObjArray", jsonArray);
        formData.put("allowFailure", "false");

        MbxPostBalanceBatchRequest mbxRequest = new MbxPostBalanceBatchRequest(formData);
        // Keep atomic to avoid transaction inconsistency.

        MbxResponse<List<MbxPostBalanceBatchResponse>> mbxResponse = matchBoxManagementService.postBalanceBatch(mbxRequest);
        MbxResponse<List<PostBalanceBatchResponse>> response = MbxResponse.of(mbxResponse.getState());
        if (response.isSuccess()) {
            List<MbxPostBalanceBatchResponse> mbxItemList = mbxResponse.getData();
            List<PostBalanceBatchResponse> itemList = new ArrayList<>(mbxItemList.size());
            for (MbxPostBalanceBatchResponse mbxItem : mbxItemList) {
                itemList.add(PostBalanceBatchResponseConverter.convert(mbxItem));
            }
            response.setData(itemList);
        }
        return response;
    }

    @Override
    public MbxResponse<DeleteOrderResponse> deleteOrder(DeleteOrderRequest request) throws MbxException {
        MbxDeleteOrderRequest mbxRequest = new MbxDeleteOrderRequest(request.getAccountId(), request.getSymbol());
        mbxRequest.setForce(request.getForce());
        mbxRequest.setNewClientOrderId(request.getNewClientOrderId());
        mbxRequest.setOrderId(request.getOrderId());
        mbxRequest.setOrigClientOrderId(request.getOrigClientOrderId());
        mbxRequest.setMsgAuth(request.getMsgAuth());

        MbxResponse<MbxDeleteOrderResponse> mbxResponse = matchBoxManagementService.deleteOrder(mbxRequest);
        MbxResponse<DeleteOrderResponse> response = new MbxResponse<DeleteOrderResponse>();
        response.setState(mbxResponse.getState());
        if (response.isSuccess()) {
            response.setData(DeleteOrderResponseConverter.convert(mbxResponse.getData()));
        }
        return response;
    }

    @Override
    public MbxResponse<DeleteOcoOrderResponse> delOcoOrder(DeleteOcoOrderReq request) throws MbxException {
        MbxDeleteOcoOrderRequest mbxRequest = new MbxDeleteOcoOrderRequest(request.getAccountId(), request.getSymbol());
        mbxRequest.setOrderListId(request.getOrderListId());
        mbxRequest.setListClientOrderId(request.getListClientOrderId());
        mbxRequest.setNewClientOrderId(request.getNewClientOrderId());
        mbxRequest.setMsgAuth(request.getMsgAuth());

        MbxResponse<MbxDeleteOcoOrderResponse> mbxResponse = matchBoxManagementService.deleteOcoOrder(mbxRequest);
        MbxResponse<DeleteOcoOrderResponse> response = new MbxResponse<>();
        response.setState(mbxResponse.getState());
        if (response.isSuccess()) {
            MbxDeleteOcoOrderResponse data = mbxResponse.getData();
            DeleteOcoOrderResponse targetData = DeleteOcoOrderResponseConverter.convert(data);
            response.setData(targetData);
        }
        return response;
    }

    @Override
    public MbxResponse<DeleteOpenOrdersResponse> deleteOpenOrders(DeleteOpenOrdersRequest request) throws MbxException {
        MbxDeleteOpenOrdersRequest mbxRequest = new MbxDeleteOpenOrdersRequest(request.getAccountId(), request.getSymbol());
        mbxRequest.setForce(request.getForce());
        mbxRequest.setMsgAuth(request.getMsgAuth());

        MbxResponse<List<MbxDeleteOpenOrdersResponse>> mbxResponse = matchBoxManagementService.deleteOpenOrders(mbxRequest);
        MbxResponse<DeleteOpenOrdersResponse> response = MbxResponse.of(mbxResponse.getState());
        if (response.isSuccess()) {
            response.setData(DeleteOpenOrdersResponseConverter.convert(mbxResponse.getData()));
        }
        return response;
    }

    @Override
    public MbxResponse<List<GetAllOrdersResponse>> getAllOrders(GetAllOrdersRequest request) throws MbxException {
        MbxGetAllOrdersRequest mbxRequest = new MbxGetAllOrdersRequest(request.getAccountId());
        mbxRequest.setEndTime(request.getEndTime());
        mbxRequest.setFromId(request.getFromId());
        mbxRequest.setLimit(request.getLimit());
        mbxRequest.setStartTime(request.getStartTime());
        mbxRequest.setSymbol(request.getSymbol());

        MbxResponse<List<MbxGetAllOrdersResponse>> mbxResponse = matchBoxManagementService.getAllOrders(mbxRequest);
        MbxResponse<List<GetAllOrdersResponse>> response = MbxResponse.of(mbxResponse.getState());
        if (response.isSuccess()) {
            List<MbxGetAllOrdersResponse> mbxList = mbxResponse.getData();
            List<GetAllOrdersResponse> targetList = new ArrayList<>(mbxList.size());
            for (MbxGetAllOrdersResponse mbxGetAllOrdersResponse : mbxList) {
                GetAllOrdersResponse target = GetAllOrdersResponseConverter.convert(mbxGetAllOrdersResponse);
                targetList.add(target);
            }

            response.setData(targetList);
        }

        return response;
    }

    @Override
    public MbxResponse<GetOcoOrderListResponse> getOcoOrderList(GetOcoOrderListRequest request) throws MbxException {
        MbxGetOcoOrderListRequest mbxRequest = new MbxGetOcoOrderListRequest(request.getAccountId());
        mbxRequest.setOrderListId(request.getOrderListId());
        mbxRequest.setOrigClientOrderId(request.getOrigClientOrderId());
        MbxResponse<MbxGetOcoOrderListResponse> mbxResponse = matchBoxManagementService.getOcoOrderList(mbxRequest);
        MbxResponse<GetOcoOrderListResponse> response = MbxResponse.of(mbxResponse.getState());

        if (response.isSuccess()) {
            MbxGetOcoOrderListResponse mbxResponseData = mbxResponse.getData();
            GetOcoOrderListResponse responseData = GetOcoOrderListResponseConverter.convert(mbxResponseData);
            response.setData(responseData);
        }
        return response;
    }

    @Override
    public MbxResponse<List<GetOcoOpenOrderListResponse>> getOcoOpenOrderList(GetOcoOpenOrderListRequest request) throws MbxException {
        MbxGetOcoOpenOrderListRequest mbxRequest = new MbxGetOcoOpenOrderListRequest(request.getAccountId());
        MbxResponse<List<MbxGetOcoOpenOrderListResponse>> mbxResponse = matchBoxManagementService.getOcoOpenOrderList(mbxRequest);
        MbxResponse<List<GetOcoOpenOrderListResponse>> response = MbxResponse.of(mbxResponse.getState());

        if (response.isSuccess()) {
            List<MbxGetOcoOpenOrderListResponse> mbxOpenOrderLists = mbxResponse.getData();
            if (mbxOpenOrderLists != null && !mbxOpenOrderLists.isEmpty()) {
                List<GetOcoOpenOrderListResponse> openOrderLists = new ArrayList<>(mbxOpenOrderLists.size());
                mbxOpenOrderLists.forEach(mbxOpenOrderList -> {
                    GetOcoOpenOrderListResponse openOrderList = GetOcoOpenOrderListResponseConverter.convert(mbxOpenOrderList);
                    openOrderLists.add(openOrderList);
                });
                response.setData(openOrderLists);
            } else {
                response.setData(Collections.emptyList());
            }
        }
        return response;
    }

    @Override
    public MbxResponse<List<GetOcoAllOrderListResponse>> getOcoAllOrderList(GetOcoAllOrderListRequest request) throws MbxException {
        MbxGetOcoAllOrderListRequest mbxRequest = new MbxGetOcoAllOrderListRequest(request.getAccountId());
        mbxRequest.setFromId(request.getFromId());
        mbxRequest.setStartTime(request.getStartTime());
        mbxRequest.setEndTime(request.getEndTime());
        mbxRequest.setLimit(request.getLimit());

        MbxResponse<List<MbxGetOcoAllOrderListResponse>> mbxResponse = matchBoxManagementService.getOcoAllOrderList(mbxRequest);
        MbxResponse<List<GetOcoAllOrderListResponse>> response = MbxResponse.of(mbxResponse.getState());

        if (response.isSuccess()) {
            List<MbxGetOcoAllOrderListResponse> mbxOpenOrderLists = mbxResponse.getData();
            if (mbxOpenOrderLists != null && !mbxOpenOrderLists.isEmpty()) {
                List<GetOcoAllOrderListResponse> openOrderLists = new ArrayList<>(mbxOpenOrderLists.size());
                mbxOpenOrderLists.forEach(mbxOpenOrderList -> {
                    GetOcoAllOrderListResponse openOrderList = GetOcoAllOrderListResponseConverter.convert(mbxOpenOrderList);
                    openOrderLists.add(openOrderList);
                });
                response.setData(openOrderLists);
            } else {
                response.setData(Collections.emptyList());
            }
        }
        return response;
    }

    @Override
    public MbxResponse<PostOrderResponse> postOrder(PostOrderRequest request) throws MbxException {
        MbxPostOrderRequest mbxRequest = build(request);
        MbxResponse<MbxPostOrderResponse> mbxResponse = matchBoxManagementService.postOrder(mbxRequest);
        MbxResponse<PostOrderResponse> response = MbxResponse.of(mbxResponse.getState());
        if (response.isSuccess()) {
            MbxPostOrderResponse mbxPostOrderResponse = mbxResponse.getData();
            PostOrderResponse postOrderResponse = PostOrderResponseConverter.convert(mbxPostOrderResponse);
            response.setData(postOrderResponse);
        }
        return response;
    }

    private MbxPostOrderRequest build(PostOrderRequest request) {
        MbxPostOrderRequest mbxPostOrderRequest = new MbxPostOrderRequest(request.getAccountId(), request.getSide(),
                request.getSymbol(), request.getType());
        mbxPostOrderRequest.setQuantity(request.getQuantity());
        mbxPostOrderRequest.setForce(request.getForce());
        mbxPostOrderRequest.setIcebergQty(request.getIcebergQty());
        mbxPostOrderRequest.setNewClientOrderId(request.getNewClientOrderId());
        mbxPostOrderRequest.setNewOrderRespType(request.getNewOrderRespType());
        mbxPostOrderRequest.setPrice(request.getPrice());
        mbxPostOrderRequest.setStopPrice(request.getStopPrice());
        mbxPostOrderRequest.setTargetOrderId(request.getTargetOrderId());
        mbxPostOrderRequest.setTimeInForce(request.getTimeInForce());
        mbxPostOrderRequest.setMsgAuth(request.getMsgAuth());
        mbxPostOrderRequest.setQuoteOrderQty(request.getQuoteOrderQty());
        return mbxPostOrderRequest;
    }

    @Override
    public MbxResponse<PostOcoOrderResponse> postOcoOrder(PostOcoOrderRequest request) throws MbxException {
        MbxPostOcoOrderRequest mbxRequest = build(request);
        MbxResponse<MbxPostOcoOrderResponse> mbxResponse = matchBoxManagementService.postOcoOrder(mbxRequest);
        MbxResponse<PostOcoOrderResponse> response = MbxResponse.of(mbxResponse.getState());
        if (response.isSuccess()) {
            MbxPostOcoOrderResponse mbxPostOcoOrderResponse = mbxResponse.getData();
            PostOcoOrderResponse postOcoOrderResponse = PostOcoOrderResponseConverter.convert(mbxPostOcoOrderResponse);
            response.setData(postOcoOrderResponse);
        }
        return response;
    }

    private MbxPostOcoOrderRequest build(PostOcoOrderRequest request) {
        MbxPostOcoOrderRequest mbxPostOcoOrderRequest = new MbxPostOcoOrderRequest(request.getAccountId(),
                request.getSymbol(), request.getSide(), request.getQuantity(), request.getPrice(),
                request.getStopPrice());
        mbxPostOcoOrderRequest.setListClientOrderId(request.getListClientOrderId());
        mbxPostOcoOrderRequest.setLimitIcebergQty(request.getLimitIcebergQty());
        mbxPostOcoOrderRequest.setStopClientOrderId(request.getStopClientOrderId());
        mbxPostOcoOrderRequest.setStopLimitPrice(request.getStopLimitPrice());
        mbxPostOcoOrderRequest.setStopIcebergQty(request.getStopIcebergQty());
        mbxPostOcoOrderRequest.setStopLimitTimeInForce(request.getStopLimitTimeInForce());
        mbxPostOcoOrderRequest.setMsgAuth(request.getMsgAuth());
        mbxPostOcoOrderRequest.setNewOrderRespType(request.getNewOrderRespType());
        return mbxPostOcoOrderRequest;
    }

    @Override
    public MbxResponse<List<GetBalanceLedgerResponse>> getBalanceLedger(GetBalanceLedgerRequest request) throws MbxException {
        MbxGetBalanceLedgerRequest mbxRequest = build(request);
        MbxResponse<List<MbxGetBalanceLedgerResponse>> mbxResponse = matchBoxManagementService.getBalanceLedger(mbxRequest);
        MbxResponse<List<GetBalanceLedgerResponse>> response = MbxResponse.of(mbxResponse.getState());
        if (response.isSuccess()) {
            List<MbxGetBalanceLedgerResponse> mbxList = mbxResponse.getData();
            List<GetBalanceLedgerResponse> targetList = new ArrayList<>(mbxList.size());
            for (MbxGetBalanceLedgerResponse mbxGetBalanceLedgerResponse : mbxList) {
                targetList.add(GetBalanceLedgerResponseConverter.convert(mbxGetBalanceLedgerResponse));
            }
            response.setData(targetList);
        }

        return response;
    }

    private MbxGetBalanceLedgerRequest build(GetBalanceLedgerRequest request) {
        MbxGetBalanceLedgerRequest mbxGetBalanceLedgerRequest = new MbxGetBalanceLedgerRequest(request.getAccountId(), request.getAsset());
        mbxGetBalanceLedgerRequest.setStartTime(request.getStartTime());
        mbxGetBalanceLedgerRequest.setEndTime(request.getEndTime());
        mbxGetBalanceLedgerRequest.setFromExternalUpdateId(request.getFromExternalUpdateId());
        mbxGetBalanceLedgerRequest.setToExternalUpdateId(request.getToExternalUpdateId());
//        mbxGetBalanceLedgerRequest.setExternalUpdateId();
        return mbxGetBalanceLedgerRequest;
    }

    @Override
    public MbxResponse<List<GetAllAccountsResponse>> getAllAccounts() throws MbxException {
        MbxGetAllAccountsRequest mbxRequest = new MbxGetAllAccountsRequest();
        MbxResponse<List<MbxGetAllAccountsResponse>> mbxResponse = matchBoxManagementService.getAllAccounts(mbxRequest);
        MbxResponse<List<GetAllAccountsResponse>> response = MbxResponse.of(mbxResponse.getState());

        if (response.isSuccess()) {
            List<MbxGetAllAccountsResponse> mbxList = mbxResponse.getData();
            List<GetAllAccountsResponse> targetList = new ArrayList<>(mbxList.size());
            for (MbxGetAllAccountsResponse mbxGetAllAccountsResponse : mbxList) {
                targetList.add(GetAllAccountsResponseConverter.convert(mbxGetAllAccountsResponse));
            }
            response.setData(targetList);
        }
        return response;
    }

    @Override
    public MbxResponse<Void> putAccountAsset(PutAccountAssetRequest request) throws MbxException {
        MbxPutAccountAssetRequest mbxRequest = build(request);
        MbxResponse<Void> mbxResponse = matchBoxManagementService.putAccountAsset(mbxRequest);
        return mbxResponse;
    }

    private MbxPutAccountAssetRequest build(PutAccountAssetRequest request) {
        MbxPutAccountAssetRequest mbxPutAccountAssetRequest = new MbxPutAccountAssetRequest(request.getAccountId(),
                request.getAsset(), request.getSymbolType(), request.getLocked(), request.getAvailable());
        return mbxPutAccountAssetRequest;
    }

    @Override
    public MbxResponse<PostAccountResponse> postAccount(PostAccountRequest request) throws MbxException {
        MbxPostAccountRequest mbxRequest = build(request);
        MbxResponse<MbxPostAccountResult> mbxResponse = matchBoxManagementService.postAccount(mbxRequest);
        MbxResponse<PostAccountResponse> response = MbxResponse.of(mbxResponse.getState());
        if (response.isSuccess()) {
            MbxPostAccountResult mbxPostAccountResult = mbxResponse.getData();
            PostAccountResponse postAccountResponse = new PostAccountResponse();
            postAccountResponse.setAccountId(mbxPostAccountResult.getAccountId());
            response.setData(postAccountResponse);
        }
        return response;
    }

    private MbxPostAccountRequest build(PostAccountRequest request) {
        MbxPostAccountRequest mbxPostAccountRequest = new MbxPostAccountRequest(request.getExternalId());
        mbxPostAccountRequest.setMakerCommission(request.getMakerCommission());
        mbxPostAccountRequest.setTakerCommission(request.getTakerCommission());
        mbxPostAccountRequest.setBuyerCommission(request.getBuyerCommission());
        mbxPostAccountRequest.setSellerCommission(request.getSellerCommission());
        mbxPostAccountRequest.setCanTrade(request.getCanTrade());
        mbxPostAccountRequest.setCanWithdraw(request.getCanWithdraw());
        mbxPostAccountRequest.setCanDeposit(request.getCanDeposit());
        mbxPostAccountRequest.setAccountType(request.getAccountType());
        return mbxPostAccountRequest;
    }

    @Override
    public MbxResponse<PostAccountResponseV3> postAccountV3(PostAccountRequestV3 request) throws MbxException {
        MbxPostAccountV3Request mbxRequest = build(request);
        MbxResponse<MbxPostAccountV3Response> mbxResponse = matchBoxManagementService.postAccountV3(mbxRequest);

        MbxResponse<PostAccountResponseV3> response = MbxResponse.of(mbxResponse.getState());
        if (response.isSuccess()) {
            response.setData(PostAccountResponseV3Converter.convert(mbxResponse.getData()));
        }
        return response;
    }

    private MbxPostAccountV3Request build(PostAccountRequestV3 request) {
        MbxPostAccountV3Request mbxPostAccountV3Request =
                new MbxPostAccountV3Request(request.getExternalAccountId(), request.getPermissionsBitmask());
        mbxPostAccountV3Request.setBuyerCommission(request.getBuyerCommission());
        mbxPostAccountV3Request.setCanDeposit(request.getCanDeposit());
        mbxPostAccountV3Request.setCanTrade(request.getCanTrade());
        mbxPostAccountV3Request.setCanWithdraw(request.getCanWithdraw());
        mbxPostAccountV3Request.setMakerCommission(request.getMakerCommission());
        mbxPostAccountV3Request.setSellerCommission(request.getSellerCommission());
        mbxPostAccountV3Request.setTakerCommission(request.getTakerCommission());
        mbxPostAccountV3Request.setAccountType(request.getAccountType());
        mbxPostAccountV3Request.setRestrictionMode(request.getRestrictionMode());
        mbxPostAccountV3Request.setSymbols(request.getSymbols());
        return mbxPostAccountV3Request;
    }

    @Override
    public MbxResponse<List<GetOpenOrdersResponse>> getOpenOrders(GetOpenOrdersRequest request) throws MbxException {
        MbxGetOpenOrdersRequest mbxRequest = build(request);
        MbxResponse<List<MbxGetOpenOrdersResponse>> mbxResponse = matchBoxManagementService.getOpenOrders(mbxRequest);
        MbxResponse<List<GetOpenOrdersResponse>> response = MbxResponse.of(mbxResponse.getState());
        if (response.isSuccess()) {
            List<MbxGetOpenOrdersResponse> mbxList = mbxResponse.getData();
            List<GetOpenOrdersResponse> targetList = new ArrayList<>(mbxList.size());
            for (MbxGetOpenOrdersResponse mbxGetOpenOrdersResponse : mbxList) {
                targetList.add(GetOpenOrdersResponseConverter.convert(mbxGetOpenOrdersResponse));
            }
            response.setData(targetList);
        }
        return response;
    }

    private MbxGetOpenOrdersRequest build(GetOpenOrdersRequest request) {
        MbxGetOpenOrdersRequest mbxGetOpenOrdersRequest = new MbxGetOpenOrdersRequest(request.getAccountId());
        mbxGetOpenOrdersRequest.setSymbol(request.getSymbol());
        return mbxGetOpenOrdersRequest;

    }

    @Override
    public MbxResponse<List<GetMsgAuthOrdersResponse>> getMsgAuthOrders(GetMsgAuthOrdersRequest request) throws MbxException {
        if (request.getAccountId() == null && StringUtils.isEmpty(request.getSymbol())) {
            throw new MbxException(GeneralCode.ILLEGAL_PARAM, "symbol OR accountId must be sent, and both can be sent.");
        }

        MbxGetMsgAuthOrdersRequest mbxRequest = build(request);
        MbxResponse<List<MbxGetMsgAuthOrdersResponse>> mbxResponse = matchBoxManagementService.getMsgAuthOrders(mbxRequest);
        MbxResponse<List<GetMsgAuthOrdersResponse>> response = MbxResponse.of(mbxResponse.getState());

        if (response.isSuccess()) {
            List<MbxGetMsgAuthOrdersResponse> mbxList = mbxResponse.getData();
            List<GetMsgAuthOrdersResponse> targetList = new ArrayList<>(mbxList.size());
            for (MbxGetMsgAuthOrdersResponse mbxGetMsgAuthOrdersResponse : mbxList) {
                targetList.add(GetMsgAuthOrdersResponseConverter.convert(mbxGetMsgAuthOrdersResponse));
            }
            response.setData(targetList);
        }
        return response;
    }

    private MbxGetMsgAuthOrdersRequest build(GetMsgAuthOrdersRequest request) {
        MbxGetMsgAuthOrdersRequest mbxGetMsgAuthOrdersRequest = new MbxGetMsgAuthOrdersRequest(request.getStartTime(),
                request.getMsgAuth());
        mbxGetMsgAuthOrdersRequest.setAccountId(request.getAccountId());
        mbxGetMsgAuthOrdersRequest.setSymbol(request.getSymbol());
        mbxGetMsgAuthOrdersRequest.setLimit(request.getLimit());
        return mbxGetMsgAuthOrdersRequest;

    }

    @Override
    public MbxResponse<Void> putSymbolLimits(PutSymbolLimitsRequest request) throws MbxException {
        MbxPutSymbolLimitsRequest mbxRequest = build(request);
        MbxResponse<Void> mbxResponse = matchBoxManagementService.putSymbolLimits(mbxRequest);
        return mbxResponse;
    }

    private MbxPutSymbolLimitsRequest build(PutSymbolLimitsRequest request) {
        MbxPutSymbolLimitsRequest mbxPutSymbolLimitsRequest = new MbxPutSymbolLimitsRequest(request.getSymbol(),
                request.getMinQty(), request.getMaxPrice(), request.getMaxQty());
        return mbxPutSymbolLimitsRequest;
    }

    @Override
    public MbxResponse<Void> putAccount(PutAccountRequest request) throws MbxException {
        MbxPutAccountRequest mbxRequest = build(request);
        MbxResponse<Void> mbxResponse = matchBoxManagementService.putAccount(mbxRequest);
        return mbxResponse;
    }

    private MbxPutAccountRequest build(PutAccountRequest request) {
        MbxPutAccountRequest mbxPutAccountRequest = new MbxPutAccountRequest(request.getAccountId(),
                request.isCanTrade(), request.isCanWithdraw(), request.isCanDeposit());
        return mbxPutAccountRequest;
    }

    @Override
    public MbxResponse<PutAccountResponseV3> putAccountV3(PutAccountRequestV3 request) throws MbxException {
        MbxPutAccountV3Request mbxRequest = build(request);
        MbxResponse<MbxPutAccountResponseV3> mbxResponse = matchBoxManagementService.putAccountV3(mbxRequest);

        MbxResponse<PutAccountResponseV3> response = MbxResponse.of(mbxResponse.getState());
        if (response.isSuccess()) {
            response.setData(PutAccountResponseV3Converter.convert(mbxResponse.getData()));
        }
        return response;
    }

    private MbxPutAccountV3Request build(PutAccountRequestV3 request) {
        MbxPutAccountV3Request mbxPutAccountV3Request = new MbxPutAccountV3Request(request.getAccountId());
        mbxPutAccountV3Request.setMakerCommission(request.getMakerCommission());
        mbxPutAccountV3Request.setTakerCommission(request.getTakerCommission());
        mbxPutAccountV3Request.setBuyerCommission(request.getBuyerCommission());
        mbxPutAccountV3Request.setSellerCommission(request.getSellerCommission());
        mbxPutAccountV3Request.setExternalAccountId(request.getExternalAccountId());
        mbxPutAccountV3Request.setCanDeposit(request.getCanDeposit());
        mbxPutAccountV3Request.setCanTrade(request.getCanTrade());
        mbxPutAccountV3Request.setCanWithdraw(request.getCanWithdraw());
        mbxPutAccountV3Request.setAccountType(request.getAccountType());
        mbxPutAccountV3Request.setRestrictionMode(request.getRestrictionMode());
        mbxPutAccountV3Request.setSymbols(request.getSymbols());
        mbxPutAccountV3Request.setPermissionsBitmask(request.getPermissionsBitmask());
        return mbxPutAccountV3Request;
    }

    @Override
    public MbxResponse<GetOrderResponse> getOrder(GetOrderRequest request) throws MbxException {
        if (request.getOrderId() == null && StringUtils.isEmpty(request.getOrigClientOrderId())) {
            throw new MbxException(GeneralCode.ILLEGAL_PARAM, "orderId and origClientOrderId, at least one of them is required.");
        }

        MbxGetOrderRequest mbxRequest = build(request);
        MbxResponse<MbxGetOrderResponse> mbxResponse = matchBoxManagementService.getOrder(mbxRequest);
        MbxResponse<GetOrderResponse> response = MbxResponse.of(mbxResponse.getState());
        if (response.isSuccess()) {
            response.setData(GetOrderResponseConverter.convert(mbxResponse.getData()));
        }
        return response;
    }

    private MbxGetOrderRequest build(GetOrderRequest request) {
        MbxGetOrderRequest mbxGetOrderRequest = new MbxGetOrderRequest(request.getSymbol());
        mbxGetOrderRequest.setOrderId(request.getOrderId());
        mbxGetOrderRequest.setOrigClientOrderId(request.getOrigClientOrderId());
        return mbxGetOrderRequest;
    }

    @Override
    public MbxResponse<GetAccountResponse> getAccount(GetAccountRequest request) throws MbxException {
        MbxGetAccountRequest mbxRequest = build(request);
        MbxResponse<MbxGetAccountResult> mbxResponse = matchBoxManagementService.getAccount(mbxRequest);

        MbxResponse<GetAccountResponse> response = MbxResponse.of(mbxResponse.getState());
        if (response.isSuccess()) {
            response.setData(GetAccountResponseConverter.convert(mbxResponse.getData()));
        }
        return response;
    }
    @Override
    public MbxResponse<GetAccountFromEngineV3Response> getAccountFromEngineV3(GetAccountFromEngineV3Request request) throws MbxException {
        MbxGetAccountFromEngineV3Request mbxRequest = GetAccountFromEngineV3Converter.convertRequest(request);
        MbxResponse<MbxGetAccountFromEngineV3Response> mbxResponse = matchBoxManagementService.getAccountFromEngineV3(mbxRequest);

        MbxResponse<GetAccountFromEngineV3Response> response = MbxResponse.of(mbxResponse.getState());
        if (response.isSuccess()) {
            response.setData(GetAccountFromEngineV3Converter.convertResponse(mbxResponse.getData()));
        }
        return response;
    }

    private MbxGetAccountRequest build(GetAccountRequest request) {
        MbxGetAccountRequest mbxGetAccountRequest = new MbxGetAccountRequest(request.getAccountId());
        return mbxGetAccountRequest;
    }

    @Override
    public MbxResponse<Void> putGasOptOut(PutGasOptOutRequest request) throws MbxException {
        MbxPutGasOptOutRequest mbxRequest = build(request);
        MbxResponse<Void> mbxResponse = matchBoxManagementService.putGasOptOut(mbxRequest);
        return mbxResponse;
    }

    private MbxPutGasOptOutRequest build(PutGasOptOutRequest request) {
        MbxPutGasOptOutRequest mbxPutGasOptOutRequest = new MbxPutGasOptOutRequest(request.getAccountId());
        return mbxPutGasOptOutRequest;
    }

    @Override
    public MbxResponse<Void> deleteGasOptOut(DeleteGasOptOutRequest request) throws MbxException {
        MbxDeleteGasOptOutRequest mbxRequest = build(request);
        MbxResponse<Void> mbxResponse = matchBoxManagementService.deleteGasOptOut(mbxRequest);
        return mbxResponse;
    }

    private MbxDeleteGasOptOutRequest build(DeleteGasOptOutRequest request) {
        MbxDeleteGasOptOutRequest mbxDeleteGasOptOutRequest = new MbxDeleteGasOptOutRequest(request.getAccountId());
        return mbxDeleteGasOptOutRequest;
    }

    @Override
    public MbxResponse<PostApiKeyResponse> postApiKey(PostApiKeyRequest request) throws MbxException {
        MbxPostApiKeyRequest mbxRequest = build(request);
        MbxResponse<MbxPostApiKeyResponse> mbxResponse = matchBoxManagementService.postApiKey(mbxRequest);
        MbxResponse<PostApiKeyResponse> response = MbxResponse.of(mbxResponse.getState());
        if (response.isSuccess()) {
            MbxPostApiKeyResponse data = mbxResponse.getData();

            PostApiKeyResponse targetDate = PostApiKeyResponseConverter.convert(data);
            response.setData(targetDate);
        }
        return response;
    }

    private MbxPostApiKeyRequest build(PostApiKeyRequest request) {
        MbxPostApiKeyRequest mbxPostApiKeyRequest = new MbxPostApiKeyRequest(request.getAccountId(), request.getDesc());
        mbxPostApiKeyRequest.setCanAccessSecureWs(request.getCanAccessSecureWs());
        mbxPostApiKeyRequest.setCanControlUserStreams(request.getCanControlUserStreams());
        mbxPostApiKeyRequest.setCanTrade(request.getCanTrade());
        mbxPostApiKeyRequest.setCanViewMarketData(request.getCanViewMarketData());
        mbxPostApiKeyRequest.setCanViewUserData(request.getCanViewUserData());
        mbxPostApiKeyRequest.setForce(request.getForce());
        mbxPostApiKeyRequest.setPublicKey(request.getPublicKey());
        return mbxPostApiKeyRequest;
    }

    @Override
    public MbxResponse<List<GetApiKeysResponse>> getApiKeys(GetApiKeysRequest request) throws MbxException {
        MbxApiKeysRequest mbxRequest = new MbxApiKeysRequest(request.getAccountId());
        MbxResponse<List<MbxApiKeysResult>> mbxResponse = matchBoxManagementService.getApiKeys(mbxRequest);

        MbxResponse<List<GetApiKeysResponse>> response = MbxResponse.of(mbxResponse.getState());
        if (response.isSuccess()) {
            List<MbxApiKeysResult> mbxList = mbxResponse.getData();

            List<GetApiKeysResponse> targetList = new ArrayList<>(mbxList.size());
            for (MbxApiKeysResult mbxApiKeysResult : mbxList) {
                targetList.add(GetApiKeysResponseConverter.convert(mbxApiKeysResult));
            }
            response.setData(targetList);
        }
        return response;
    }

    @Override
    public MbxResponse<Void> postApiKeyWhitelist(PostApiKeyWhitelistRequest request) throws MbxException {
        MbxPostApiKeyWhitelistRequest mbxRequest = build(request);
        MbxResponse<Void> mbxResponse = matchBoxManagementService.postApiKeyWhitelist(mbxRequest);
        return mbxResponse;
    }

    private MbxPostApiKeyWhitelistRequest build(PostApiKeyWhitelistRequest request) {
        List<String> symbols = request.getSymbols();
        StringBuilder stringBuilder = new StringBuilder();
        if (symbols != null && !symbols.isEmpty()) {
            stringBuilder.append("[");
            symbols.forEach(symbol -> {
                if (stringBuilder.length() > 1) {
                    stringBuilder.append(",");
                }
                stringBuilder.append("\"").append(symbol).append("\"");
            });
            stringBuilder.append("]");
        } else {
            stringBuilder.append("[]");
        }
        String mbxSymbols = stringBuilder.toString();
        MbxPostApiKeyWhitelistRequest mbxPostApiKeyWhitelistRequest = new MbxPostApiKeyWhitelistRequest(
                request.getKeyId(), request.getAccountId(), mbxSymbols);
        return mbxPostApiKeyWhitelistRequest;
    }

    @Override
    public MbxResponse<Void> deleteApiKeyWhitelist(DeleteApiKeyWhitelistRequest request) throws MbxException {
        MbxDeleteApiKeyWhitelistRequest mbxRequest = build(request);
        MbxResponse<Void> mbxResponse = matchBoxManagementService.deleteApiKeyWhitelist(mbxRequest);
        return mbxResponse;
    }

    private MbxDeleteApiKeyWhitelistRequest build(DeleteApiKeyWhitelistRequest request) {
        MbxDeleteApiKeyWhitelistRequest mbxRequest = new MbxDeleteApiKeyWhitelistRequest(request.getKeyId(), request.getAccountId());
        return mbxRequest;
    }

    @Override
    public MbxResponse<GetApiKeyWhitelistResponse> getApiKeyWhitelist(GetApiKeyWhitelistRequest request) throws MbxException {
        MbxApiKeyWhitelistRequest mbxRequest = new MbxApiKeyWhitelistRequest(request.getKeyId(), request.getAccountId());
        MbxResponse<MbxApiKeyWhitelistResult> mbxResponse = matchBoxManagementService.getApiKeyWhitelist(mbxRequest);

        MbxResponse<GetApiKeyWhitelistResponse> response = MbxResponse.of(mbxResponse.getState());
        if (response.isSuccess()) {
            GetApiKeyWhitelistResponse targetWhitelist = GetApiKeyWhitelistResponseConverter.convert(mbxResponse.getData());
            response.setData(targetWhitelist);
        }
        return response;
    }

    @Override
    public MbxResponse<Void> putApiKeyLock(PutApiKeyLockRequest request) throws MbxException {
        MbxPutApiKeyLockRequest mbxRequest = build(request);
        MbxResponse<Void> mbxResponse = matchBoxManagementService.putApiKeyLock(mbxRequest);
        return mbxResponse;
    }

    private MbxPutApiKeyLockRequest build(PutApiKeyLockRequest request) {
        MbxPutApiKeyLockRequest mbxPutApiKeyLockRequest = new MbxPutApiKeyLockRequest(request.getAccountId());
        return mbxPutApiKeyLockRequest;
    }

    @Override
    public MbxResponse<Void> postPriceFilter(PostPriceFilterRequest request) throws MbxException {
        MbxPostPriceFilterRequest mbxRequest = build(request);
        MbxResponse<Void> mbxResponse = matchBoxManagementService.postPriceFilter(mbxRequest);
        return mbxResponse;
    }

    private MbxPostPriceFilterRequest build(PostPriceFilterRequest request) {
        MbxPostPriceFilterRequest mbxPostPriceFilterRequest = new MbxPostPriceFilterRequest(request.getMaxPrice(),
                request.getMinPrice(), request.getSymbol(), request.getTickSize());
        return mbxPostPriceFilterRequest;
    }

    @Override
    public MbxResponse<Void> postAsset(PostAssetRequest request) throws MbxException {
        MbxPostAssetRequest mbxRequest = new MbxPostAssetRequest(request.getAsset(), request.getDecimalPlaces());
        MbxResponse<Void> mbxResponse = matchBoxManagementService.postAsset(mbxRequest);
        return mbxResponse;
    }

    @Override
    public MbxResponse<GetApiKeyLockResponse> getApiKeyLock(GetApiKeyLockRequest request) throws MbxException {
        MbxGetApiKeyLockRequest mbxRequest = new MbxGetApiKeyLockRequest(request.getAccountId());
        MbxResponse<MbxGetApiKeyLockResult> mbxResponse = matchBoxManagementService.getApiKeyLock(mbxRequest);

        MbxResponse<GetApiKeyLockResponse> response = MbxResponse.of(mbxResponse.getState());
        if (response.isSuccess()) {
            GetApiKeyLockResponse getApiKeyLockResponse = new GetApiKeyLockResponse();
            getApiKeyLockResponse.setIsLocked(mbxResponse.getData().isIsLocked());
            response.setData(getApiKeyLockResponse);
        }
        return response;
    }

    @Override
    public MbxResponse<GetAssetBalanceResponse> getAssetBalance(GetAssetBalanceRequest request) throws MbxException {
        MbxGetAssetBalanceRequest mbxRequest = new MbxGetAssetBalanceRequest(request.getAsset());
        MbxResponse<MbxGetAssetBalanceResponse> mbxResponse = matchBoxManagementService.getAssetBalance(mbxRequest);

        MbxResponse<GetAssetBalanceResponse> response = MbxResponse.of(mbxResponse.getState());
        if (response.isSuccess()) {
            response.setData(GetAssetBalanceResponseConverter.convert(mbxResponse.getData()));
        }
        return response;
    }

    @Override
    public MbxResponse<List<GetAssetLedgerResponse>> getAssetLedger(GetAssetLedgerRequest request) throws MbxException {
        MbxGetAssetLedgerRequest mbxRequest = new MbxGetAssetLedgerRequest(request.getAsset(), request.getStartTime(),
                request.getEndTime());
        MbxResponse<List<MbxGetAssetLedgerResponse>> mbxResponse = matchBoxManagementService.getAssetLedger(mbxRequest);
        MbxResponse<List<GetAssetLedgerResponse>> response = MbxResponse.of(mbxResponse.getState());
        if (response.isSuccess()) {
            List<MbxGetAssetLedgerResponse> mbxList = mbxResponse.getData();

            List<GetAssetLedgerResponse> targetList = new ArrayList<>(mbxList.size());
            for (MbxGetAssetLedgerResponse mbxGetAssetLedgerResponse : mbxList) {
                targetList.add(GetAssetLedgerResponseConverter.convert(mbxGetAssetLedgerResponse));
            }
            response.setData(targetList);
        }
        return response;
    }

    @Override
    public MbxResponse<Void> putAllSymbols(PutAllSymbolsRequest request) throws MbxException {
        MbxPutAllSymbolsRequest mbxRequest = new MbxPutAllSymbolsRequest(request.getSymbolStatus());
        MbxResponse<Void> mbxResponse = matchBoxManagementService.putAllSymbols(mbxRequest);
        return mbxResponse;
    }

    @Override
    public MbxResponse<Void> postSymbol(PostSymbolRequest request) throws MbxException {
        MbxPostSymbolRequest mbxRequest = build(request);
        MbxResponse<Void> mbxResponse = matchBoxManagementService.postSymbol(mbxRequest);
        return mbxResponse;
    }

    private MbxPostSymbolRequest build(PostSymbolRequest request) {
        MbxPostSymbolRequest mbxPostSymbolRequest = new MbxPostSymbolRequest(request.getSymbol(), request.getSymbolType(),
                request.getBaseAsset(), request.getQuoteAsset(), request.getMinQty(), request.getMaxQty(),
                request.getMaxPrice(), request.getCommissionType(), request.getBaseCommissionDecimalPlaces(),
                request.getQuoteCommissionDecimalPlaces());
        mbxPostSymbolRequest.setMatchingUnitType(request.getMatchingUnitType());
        mbxPostSymbolRequest.setMathSystemType(request.getMathSystemType());
        mbxPostSymbolRequest.setAllowSpot(request.getAllowSpot());
        mbxPostSymbolRequest.setAllowMargin(request.getAllowMargin());
        mbxPostSymbolRequest.setPermissionsBitmask(request.getPermissionsBitmask());
        return mbxPostSymbolRequest;
    }

    @Override
    public MbxResponse<Void> postApiKeyRule(PostApiKeyRuleRequest request) throws MbxException {
        MbxPostApiKeyRuleRequest mbxRequest = new MbxPostApiKeyRuleRequest(request.getKeyId(), request.getAccountId(),
                request.getIp());
        MbxResponse<Void> mbxResponse = matchBoxManagementService.postApiKeyRule(mbxRequest);
        return mbxResponse;
    }

    @Override
    public MbxResponse<Void> deleteApiKeyLock(DeleteApiKeyLockRequest request) throws MbxException {
        MbxDeleteApiKeyLockRequest mbxRequest = new MbxDeleteApiKeyLockRequest(request.getAccountId(), request.getRevert());
        MbxResponse<Void> mbxResponse = matchBoxManagementService.deleteApiKeyLock(mbxRequest);
        return mbxResponse;
    }

    @Override
    public MbxResponse<List<GetSymbolsResponse>> getSymbols() throws MbxException {
        MbxGetSymbolsRequest mbxRequest = new MbxGetSymbolsRequest();
        MbxResponse<List<MbxGetSymbolsResponse>> mbxResponse = matchBoxManagementService.getSymbols(mbxRequest);

        MbxResponse<List<GetSymbolsResponse>> response = MbxResponse.of(mbxResponse.getState());
        if (response.isSuccess()) {
            List<MbxGetSymbolsResponse> mbxList = mbxResponse.getData();

            List<GetSymbolsResponse> targetList = new ArrayList<>(mbxList.size());
            for (MbxGetSymbolsResponse mbxGetSymbolsResponse : mbxList) {
                targetList.add(GetSymbolsResponseConverter.convert(mbxGetSymbolsResponse));
            }
            response.setData(targetList);
        }
        return response;
    }

    @Override
    public MbxResponse<GetApiKeyCheckResponse> getApiKeyCheck(GetApiKeyCheckRequest request) throws MbxException {
        MbxGetApiKeyCheckRequest mbxRequest = GetApiKeyCheckRequestConverter.convert(request);
        MbxResponse<MbxGetApiKeyCheckResult> mbxResponse = matchBoxManagementService.getApiKeyCheck(mbxRequest);
        MbxResponse<GetApiKeyCheckResponse> response = MbxResponse.of(mbxResponse.getState());
        if (response.isSuccess()) {
            MbxGetApiKeyCheckResult data = mbxResponse.getData();
            GetApiKeyCheckResponse targetData = GetApiKeyCheckResponseConverter.convert(data);
            response.setData(targetData);
        }
        return response;
    }

    @Override
    public MbxResponse<GetFiltersResponse> getFilters(GetFiltersRequest request) throws MbxException {
        MbxGetFiltersRequest mbxRequest = new MbxGetFiltersRequest(request.getSymbol());
        MbxResponse<MbxGetFiltersResponse> mbxResponse = matchBoxManagementService.getFilters(mbxRequest);

        MbxResponse<GetFiltersResponse> response = MbxResponse.of(mbxResponse.getState());
        if (response.isSuccess()) {
            MbxGetFiltersResponse mbxGetFiltersResponse = mbxResponse.getData();
            response.setData(GetFiltersResponseConverter.convert(mbxGetFiltersResponse));
        }
        return response;
    }

    @Override
    public MbxResponse<Void> deleteApiKeyRule(DeleteApiKeyRuleRequest request) throws MbxException {
        MbxDeleteApiKeyRuleRequest mbxRequest = new MbxDeleteApiKeyRuleRequest(request.getRuleId(), request.getKeyId(),
                request.getAccountId());
        MbxResponse<Void> mbxResponse = matchBoxManagementService.deleteApiKeyRule(mbxRequest);
        return mbxResponse;
    }

    @Override
    public MbxResponse<List<GetSymbolHistoryResponse>> getSymbolHistory(GetSymbolHistoryRequest request) throws MbxException {
        MbxGetSymbolHistoryRequest mbxRequest = new MbxGetSymbolHistoryRequest(request.getStartTime(), request.getSymbol());
        MbxResponse<List<MbxGetSymbolHistoryResponse>> mbxResponse = matchBoxManagementService.getSymbolHistory(mbxRequest);

        MbxResponse<List<GetSymbolHistoryResponse>> response = MbxResponse.of(mbxResponse.getState());
        if (response.isSuccess()) {
            List<MbxGetSymbolHistoryResponse> mbxList = mbxResponse.getData();

            List<GetSymbolHistoryResponse> targetList = new ArrayList<>(mbxList.size());
            for (MbxGetSymbolHistoryResponse mbxGetSymbolHistoryResponse : mbxList) {
                targetList.add(GetSymbolHistoryResponseConverter.convert(mbxGetSymbolHistoryResponse));
            }
            response.setData(targetList);
        }
        return response;
    }

    @Override
    public MbxResponse<Void> putExchangeRate(PutExchangeRateRequest request) throws MbxException {
        MbxPutExchangeRateRequest mbxRequest = build(request);
        MbxResponse<Void> mbxResponse = matchBoxManagementService.putExchangeRate(mbxRequest);
        return mbxResponse;
    }

    private MbxPutExchangeRateRequest build(PutExchangeRateRequest request) {
        MbxPutExchangeRateRequest mbxPutExchangeRateRequest = new MbxPutExchangeRateRequest(request.getBaseAsset(),
                request.getPrice(), request.getQuoteAsset());
        mbxPutExchangeRateRequest.setInvert(request.getInvert());
        return mbxPutExchangeRateRequest;
    }

    @Override
    public MbxResponse<List<GetCommissionResponse>> getCommission(GetCommissionRequest request) throws MbxException {
        MbxGetCommissionRequest mbxRequest = new MbxGetCommissionRequest(request.getSymbol());
        MbxResponse<List<MbxGetCommissionResponse>> mbxResponse = matchBoxManagementService.getCommission(mbxRequest);

        MbxResponse<List<GetCommissionResponse>> response = MbxResponse.of(mbxResponse.getState());
        if (response.isSuccess()) {
            List<MbxGetCommissionResponse> mbxList = mbxResponse.getData();
            List<GetCommissionResponse> targetList = new ArrayList<>(mbxList.size());
            for (MbxGetCommissionResponse mbxGetCommissionResponse : mbxList) {
                targetList.add(GetCommissionResponseConverter.convert(mbxGetCommissionResponse));
            }
            response.setData(targetList);
        }
        return response;
    }

    @Override
    public MbxResponse<List<GetUserTradesResponse>> getUserTrades(GetUserTradesRequest request) throws MbxException {
        MbxGetUserTradesRequest mbxRequest = build(request);
        MbxResponse<List<MbxGetUserTradesResponse>> mbxResponse = matchBoxManagementService.getUserTrades(mbxRequest);

        MbxResponse<List<GetUserTradesResponse>> response = MbxResponse.of(mbxResponse.getState());
        if (response.isSuccess()) {
            List<MbxGetUserTradesResponse> mbxList = mbxResponse.getData();
            List<GetUserTradesResponse> targetList = new ArrayList<>(mbxList.size());
            for (MbxGetUserTradesResponse mbxGetUserTradesResponse : mbxList) {
                targetList.add(GetUserTradesResponseConverter.convert(mbxGetUserTradesResponse));
            }
            response.setData(targetList);
        }
        return response;
    }

    private MbxGetUserTradesRequest build(GetUserTradesRequest request) {
        MbxGetUserTradesRequest mbxGetUserTradesRequest = new MbxGetUserTradesRequest(request.getAccountId());
        mbxGetUserTradesRequest.setEndTime(request.getEndTime());
        mbxGetUserTradesRequest.setFromId(request.getFromId());
        mbxGetUserTradesRequest.setLimit(request.getLimit());
        mbxGetUserTradesRequest.setStartTime(request.getStartTime());
        mbxGetUserTradesRequest.setSymbol(request.getSymbol());
        return mbxGetUserTradesRequest;
    }

    @Override
    public MbxResponse<Void> putOrderTypes(PutOrderTypesRequest request) throws MbxException {
        MbxPutOrderTypesRequest mbxRequest = build(request);
        MbxResponse<Void> mbxResponse = matchBoxManagementService.putOrderTypes(mbxRequest);
        return mbxResponse;
    }

    private MbxPutOrderTypesRequest build(PutOrderTypesRequest request) {
        MbxPutOrderTypesRequest mbxPutOrderTypesRequest = new MbxPutOrderTypesRequest(request.getSymbol(),
                request.isEnableIceberg(), request.isEnableMarket(), request.isEnableStopLoss(),
                request.isEnableStopLossLimit(), request.isEnableTakeProfit(), request.isEnableTakeProfitLimit(),
                request.isEnableOco());
        mbxPutOrderTypesRequest.setEnableQuoteOrderQtyMarket(request.getEnableQuoteOrderQtyMarket());
        return mbxPutOrderTypesRequest;
    }

    @Override
    public MbxResponse<List<GetAssetsResponse>> getAssets() throws MbxException {
        MbxGetAssetsRequest mbxRequest = new MbxGetAssetsRequest();
        MbxResponse<List<MbxGetAssetsResponse>> mbxResponse = matchBoxManagementService.getAssets(mbxRequest);
        MbxResponse<List<GetAssetsResponse>> response = MbxResponse.of(mbxResponse.getState());
        if (response.isSuccess()) {
            List<MbxGetAssetsResponse> mbxList = mbxResponse.getData();
            List<GetAssetsResponse> targetList = new ArrayList<>(mbxList.size());
            for (MbxGetAssetsResponse mbxGetAssetsResponse : mbxList) {
                targetList.add(GetAssetsResponseConverter.convert(mbxGetAssetsResponse));
            }
            response.setData(targetList);
        }
        return response;
    }

    @Override
    public MbxResponse<Void> deleteApiKey(DeleteApiKeyRequest request) throws MbxException {
        MbxDeleteApiKeyRequest mbxRequest = new MbxDeleteApiKeyRequest(request.getAccountId(), request.getKeyId());
        MbxResponse<Void> mbxResponse = matchBoxManagementService.deleteApiKey(mbxRequest);
        return mbxResponse;
    }

    @Override
    public MbxResponse<Void> putSymbol(PutSymbolRequest request) throws MbxException {
        MbxPutSymbolRequest mbXRequest = new MbxPutSymbolRequest(request.getSymbol(), request.getSymbolStatus());
        MbxResponse<Void> mbxResponse = matchBoxManagementService.putSymbol(mbXRequest);
        return mbxResponse;
    }

    @Override
    public MbxResponse<Void> putSymbolPermissions(PutSymbolPermissionsRequest request) throws MbxException {
        MbxPutSymbolPermissionsRequest mbxRequest = build(request);
        MbxResponse<Void> mbxResponse = matchBoxManagementService.putSymbolPermissions(mbxRequest);
        return mbxResponse;
    }

    private MbxPutSymbolPermissionsRequest build(PutSymbolPermissionsRequest request) {
        MbxPutSymbolPermissionsRequest mbxPutSymbolPermissionsRequest = new MbxPutSymbolPermissionsRequest(request.getSymbol());
        mbxPutSymbolPermissionsRequest.setAllowSpot(request.getAllowSpot());
        mbxPutSymbolPermissionsRequest.setAllowMargin(request.getAllowMargin());
        mbxPutSymbolPermissionsRequest.setPermissionsBitmask(request.getPermissionsBitmask());
        return mbxPutSymbolPermissionsRequest;
    }

    @Override
    public MbxResponse<List<GetTradesResponse>> getTrades(GetTradesRequest request) throws MbxException {
        MbxGetTradesRequest mbxRequest = build(request);
        MbxResponse<List<MbxGetTradesResponse>> mbxResponse = matchBoxManagementService.getTrades(mbxRequest);


        MbxResponse<List<GetTradesResponse>> response = MbxResponse.of(mbxResponse.getState());
        if (response.isSuccess()) {
            List<MbxGetTradesResponse> mbxList = mbxResponse.getData();
            List<GetTradesResponse> targetList = new ArrayList<>(mbxList.size());
            for (MbxGetTradesResponse mbxGetTradesResponse : mbxList) {
                targetList.add(GetTradesResponseConverter.convert(mbxGetTradesResponse));
            }
            response.setData(targetList);
        }
        return response;
    }

    private MbxGetTradesRequest build(GetTradesRequest request) {
        MbxGetTradesRequest mbxGetTradesRequest = new MbxGetTradesRequest(request.getSymbol());
        mbxGetTradesRequest.setEndTime(request.getEndTime());
        mbxGetTradesRequest.setFromId(request.getFromId());
        mbxGetTradesRequest.setLimit(request.getLimit());
        mbxGetTradesRequest.setStartTime(request.getStartTime());
        return mbxGetTradesRequest;
    }

    @Override
    public MbxResponse<List<GetAccountSymbolCommissionHistoryResponse>> getAccountSymbolCommissionHistory(GetAccountSymbolCommissionHistoryRequest request) throws MbxException {
        MbxGetAccountSymbolCommissionHistoryRequest mbxRequest = new MbxGetAccountSymbolCommissionHistoryRequest(
                request.getAccountId(), request.getEndTime(), request.getStartTime(), request.getSymbol());
        MbxResponse<List<MbxGetAccountSymbolCommissionHistoryResponse>> mbxResponse = matchBoxManagementService.getAccountSymbolCommissionHistory(mbxRequest);

        MbxResponse<List<GetAccountSymbolCommissionHistoryResponse>> response = MbxResponse.of(mbxResponse.getState());
        if (response.isSuccess()) {
            List<MbxGetAccountSymbolCommissionHistoryResponse> mbxList = mbxResponse.getData();
            List<GetAccountSymbolCommissionHistoryResponse> targetList = new ArrayList<>(mbxList.size());
            for (MbxGetAccountSymbolCommissionHistoryResponse mbxGetAccountSymbolCommissionHistoryResponse : mbxList) {
                targetList.add(GetAccountSymbolCommissionHistoryResponseVonverter.convert(mbxGetAccountSymbolCommissionHistoryResponse));
            }
            response.setData(targetList);
        }
        return response;
    }

    @Override
    public MbxResponse<Void> putSymbolCommissionType(PutSymbolCommissionTypeRequest request) throws MbxException {
        MbxPutSymbolCommissionTypeRequest mbxRequest = new MbxPutSymbolCommissionTypeRequest(
                request.getCommissionType(), request.getSymbol());
        MbxResponse<Void> mbxResponse = matchBoxManagementService.putSymbolCommissionType((mbxRequest));
        return mbxResponse;
    }

    @Override
    public MbxResponse<List<GetSymbolCommissionHistoryResponse>> getSymbolCommissionHistory(GetSymbolCommissionHistoryRequest request) throws MbxException {
        MbxGetSymbolCommissionHistoryRequest mbxRequest = new MbxGetSymbolCommissionHistoryRequest(request.getEndTime(),
                request.getStartTime(), request.getSymbol());
        MbxResponse<List<MbxGetSymbolCommissionHistoryResponse>> mbxResponse = matchBoxManagementService.getSymbolCommissionHistory(mbxRequest);
        MbxResponse<List<GetSymbolCommissionHistoryResponse>> response = MbxResponse.of(mbxResponse.getState());
        if (response.isSuccess()) {
            List<MbxGetSymbolCommissionHistoryResponse> mbxList = mbxResponse.getData();
            List<GetSymbolCommissionHistoryResponse> targetList = new ArrayList<>(mbxList.size());
            for (MbxGetSymbolCommissionHistoryResponse mbxGetSymbolCommissionHistoryResponse : mbxList) {
                targetList.add(GetSymbolCommissionHistoryResponseConverter.convert(mbxGetSymbolCommissionHistoryResponse));
            }
            response.setData(targetList);
        }
        return response;
    }

    @Override
    public MbxResponse<Void> deleteNumOrdersFilter(DeleteNumOrdersFilterRequest request) throws MbxException {
        MbxDeleteNumOrdersFilterRequest mbxRequest = new MbxDeleteNumOrdersFilterRequest(request.getSymbol());
        MbxResponse<Void> mbxResponse = matchBoxManagementService.deleteNumOrdersFilter(mbxRequest);
        return mbxResponse;
    }

    @Override
    public MbxResponse<Void> postLotSizeFilter(PostLotSizeFilterRequest request) throws MbxException {
        MbxPostLotSizeFilterRequest mbxRequest = new MbxPostLotSizeFilterRequest(request.getMaxQty(),
                request.getMinQty(), request.getStepSize(), request.getSymbol());
        MbxResponse<Void> mbxResponse = matchBoxManagementService.postLotSizeFilter(mbxRequest);
        return mbxResponse;
    }

    @Override
    public MbxResponse<Void> postPercentPriceFilter(PostPercentPriceFilterRequest request) throws MbxException {
        MbxPostPercentPriceFilterRequest mbxRequest = new MbxPostPercentPriceFilterRequest(request.getMultiplierDown(),
                request.getMultiplierUp(), request.getSymbol());
        MbxResponse<Void> mbxResponse = matchBoxManagementService.postPercentPriceFilter(mbxRequest);
        return mbxResponse;
    }

    @Override
    public MbxResponse<Void> deleteTPlusSellFilter(DeleteTPlusSellFilterRequest request) throws MbxException {
        MbxDeleteTPlusSellFilterRequest mbxRequest = build(request);
        MbxResponse<Void> mbxResponse = matchBoxManagementService.deleteTPlusSellFilter(mbxRequest);
        return mbxResponse;
    }

    private MbxDeleteTPlusSellFilterRequest build(DeleteTPlusSellFilterRequest request) {
        MbxDeleteTPlusSellFilterRequest mbxDeleteTPlusSellFilterRequest = new MbxDeleteTPlusSellFilterRequest(
                request.getSymbol());
        mbxDeleteTPlusSellFilterRequest.setForce(request.getForce());
        return mbxDeleteTPlusSellFilterRequest;
    }

    @Override
    public MbxResponse<Void> deletePercentPriceFilter(DeletePercentPriceFilterRequest request) throws MbxException {
        MbxDeletePercentPriceFilterRequest mbxRequest = new MbxDeletePercentPriceFilterRequest(request.getSymbol());
        MbxResponse<Void> mbxResponse = matchBoxManagementService.deletePercentPriceFilter(mbxRequest);
        return mbxResponse;
    }

    @Override
    public MbxResponse<Void> postPositionFilter(PostPositionFilterRequest request) throws MbxException {
        MbxPostPositionFilterRequest mbxRequest = build(request);
        MbxResponse<Void> mbxResponse = matchBoxManagementService.postPositionFilter(mbxRequest);
        return mbxResponse;
    }

    private MbxPostPositionFilterRequest build(PostPositionFilterRequest request) {
        MbxPostPositionFilterRequest mbxPostPositionFilterRequest = new MbxPostPositionFilterRequest(request.getMaxQty(), request.getSymbol());
        mbxPostPositionFilterRequest.setExemptAcct(request.getExemptAcct());
        return mbxPostPositionFilterRequest;
    }

    @Override
    public MbxResponse<GetSymbolCommissionResponse> getSymbolCommission(GetSymbolCommissionRequest request) throws MbxException {
        MbxGetSymbolCommissionRequest mbxRequest = new MbxGetSymbolCommissionRequest(request.getSymbol());
        MbxResponse<MbxGetSymbolCommissionResponse> mbxResponse = matchBoxManagementService.getSymbolCommission(mbxRequest);

        MbxResponse<GetSymbolCommissionResponse> response = MbxResponse.of(mbxResponse.getState());
        if (response.isSuccess()) {
            response.setData(GetSymbolCommissionResponseConverter.convert(mbxResponse.getData()));
        }
        return response;
    }

    @Override
    public MbxResponse<Void> postMinNotionalFilter(PostMinNotionalFilterRequest request) throws MbxException {
        MbxPostMinNotionalFilterRequest mbxRequest = new MbxPostMinNotionalFilterRequest(
                request.getEnableMarket(),
                request.getMinNotional(),
                request.getSymbol());
        MbxResponse<Void> mbxResponse = matchBoxManagementService.postMinNotionalFilter(mbxRequest);
        return mbxResponse;
    }

    @Override
    public MbxResponse<Void> deleteNumIcebergOrders(DeleteNumIcebergOrdersRequest request) throws MbxException {
        MbxDeleteNumIcebergOrdersRequest mbxRequest = new MbxDeleteNumIcebergOrdersRequest(request.getSymbol());
        MbxResponse<Void> mbxResponse = matchBoxManagementService.deleteNumIcebergOrders(mbxRequest);
        return mbxResponse;
    }

    @Override
    public MbxResponse<Void> putSymbolCommission(PutSymbolCommissionRequest request) throws MbxException {
        MbxPutSymbolCommissionRequest mbxRequest = new MbxPutSymbolCommissionRequest(request.getSymbol(),
                request.getBuyerCommission(), request.getMakerCommission(), request.getSellerCommission(),
                request.getTakerCommission());
        MbxResponse<Void> mbxResponse = matchBoxManagementService.putSymbolCommission(mbxRequest);
        return mbxResponse;
    }

    @Override
    public MbxResponse<Void> postNumOrdersFilter(PostNumOrdersFilterRequest request) throws MbxException {
        MbxPostNumOrdersFilterRequest mbxRequest = build(request);
        MbxResponse<Void> mbxResponse = matchBoxManagementService.postNumOrdersFilter(mbxRequest);
        return mbxResponse;
    }

    private MbxPostNumOrdersFilterRequest build(PostNumOrdersFilterRequest request) {
        MbxPostNumOrdersFilterRequest mbxPostNumOrdersFilterRequest = new MbxPostNumOrdersFilterRequest(
                request.getNumOrders(), request.getSymbol());
        mbxPostNumOrdersFilterRequest.setExemptAcct(request.getExemptAcct());
        return mbxPostNumOrdersFilterRequest;
    }

    @Override
    public MbxResponse<Void> deletePriceFilter(DeletePriceFilterRequest request) throws MbxException {
        MbxDeletePriceFilterRequest mbxRequest = new MbxDeletePriceFilterRequest(request.getSymbol());
        MbxResponse<Void> mbxResponse = matchBoxManagementService.deletePriceFilter(mbxRequest);
        return mbxResponse;
    }

    @Override
    public MbxResponse<Void> deleteMinNotionalFilter(DeleteMinNotionalFilterRequest request) throws MbxException {
        MbxDeleteMinNotionalFilterRequest mbxRequest = new MbxDeleteMinNotionalFilterRequest(request.getSymbol());
        MbxResponse<Void> mbxResponse = matchBoxManagementService.deleteMinNotionalFilter(mbxRequest);
        return mbxResponse;
    }

    @Override
    public MbxResponse<Void> postTPlusSellFilter(PostTPlusSellFilterRequest request) throws MbxException {
        MbxPostTPlusSellFilterRequest mbxRequest = build(request);
        MbxResponse<Void> mbxResponse = matchBoxManagementService.postTPlusSellFilter(mbxRequest);
        return mbxResponse;
    }

    private MbxPostTPlusSellFilterRequest build(PostTPlusSellFilterRequest request) {
        MbxPostTPlusSellFilterRequest mbxPostTPlusSellFilterRequest = new MbxPostTPlusSellFilterRequest(
                request.getEndTime(), request.getSymbol());
        mbxPostTPlusSellFilterRequest.setExemptAcct(request.getExemptAcct());
        return mbxPostTPlusSellFilterRequest;
    }

    @Override
    public MbxResponse<Void> postIcebergPartsFilter(PostIcebergPartsFilterRequest request) throws MbxException {
        MbxPostIcebergPartsFilterRequest mbxRequest = new MbxPostIcebergPartsFilterRequest(request.getLimit(),
                request.getSymbol());
        MbxResponse<Void> mbxResponse = matchBoxManagementService.postIcebergPartsFilter(mbxRequest);
        return mbxResponse;
    }

    @Override
    public MbxResponse<Void> deleteUserDataStream(DeleteUserDataStreamRequest request) throws MbxException {
        MbxDeleteUserDataStreamRequest mbxRequest = new MbxDeleteUserDataStreamRequest(request.getListenKey(),
                request.getAccountId());
        MbxResponse<Void> mbxResponse = matchBoxManagementService.deleteUserDataStream(mbxRequest);
        return mbxResponse;
    }

    @Override
    public MbxResponse<Void> deletePositionFilter(DeletePositionFilterRequest request) throws MbxException {
        MbxDeletePositionFilterRequest mbxRequest = new MbxDeletePositionFilterRequest(request.getSymbol());
        MbxResponse<Void> mbxResponse = matchBoxManagementService.deletePositionFilter(mbxRequest);
        return mbxResponse;
    }

    @Override
    public MbxResponse<Void> deleteIcebergPartsFilter(DeleteIcebergPartsFilterRequest request) throws MbxException {
        MbxDeleteIcebergPartsFilterRequest mbxRequest = new MbxDeleteIcebergPartsFilterRequest(request.getSymbol());
        MbxResponse<Void> mbxResponse = matchBoxManagementService.deleteIcebergPartsFilter(mbxRequest);
        return mbxResponse;
    }

    @Override
    public MbxResponse<Void> postNumAlgoOrdersFilter(PostNumAlgoOrdersFilterRequest request) throws MbxException {
        MbxPostNumAlgoOrdersFilterRequest mbxRequest = new MbxPostNumAlgoOrdersFilterRequest(request.getNumOrders(),
                request.getSymbol());
        mbxRequest.setExemptAcct(request.getExemptAcct());
        MbxResponse<Void> mbxResponse = matchBoxManagementService.postNumAlgoOrdersFilter(mbxRequest);
        return mbxResponse;
    }

    @Override
    public MbxResponse<Void> postNumIcebergOrders(PostNumIcebergOrdersRequest request) throws MbxException {
        MbxPostNumIcebergOrdersRequest mbxRequest = new MbxPostNumIcebergOrdersRequest(request.getNumOrders(),
                request.getSymbol());
        MbxResponse<Void> mbxResponse = matchBoxManagementService.postNumIcebergOrders(mbxRequest);
        return mbxResponse;
    }

    @Override
    public MbxResponse<Void> deleteMarketLotSizeFilter(DeleteMarketLotSizeFilterRequest request) throws MbxException {
        MbxDeleteMarketLotSizeFilterRequest mbxRequest = new MbxDeleteMarketLotSizeFilterRequest(request.getSymbol());
        MbxResponse<Void> mbxResponse = matchBoxManagementService.deleteMarketLotSizeFilter(mbxRequest);
        return mbxResponse;
    }

    @Override
    public MbxResponse<PostUserDataStreamResponse> postUserDataStream(PostUserDataStreamRequest request)
            throws MbxException {
        MbxPostUserDataStreamRequest mbxRequest = new MbxPostUserDataStreamRequest(request.getAccountId());
        MbxResponse<MbxPostUserDataStreamResponse> mbxResponse = matchBoxManagementService.postUserDataStream(mbxRequest);
        MbxResponse<PostUserDataStreamResponse> response = MbxResponse.of(mbxResponse.getState());
        if (response.isSuccess()) {
            String listenKey = mbxResponse.getData().getListenKey();

            PostUserDataStreamResponse targetData = new PostUserDataStreamResponse();
            targetData.setListenKey(listenKey);
            response.setData(targetData);
        }
        return response;
    }

    @Override
    public MbxResponse<Void> putUserDataStream(PutUserDataStreamRequest request) throws MbxException {
        MbxPutUserDataStreamRequest mbxRequest = new MbxPutUserDataStreamRequest(request.getListenKey(), request.getAccountId());
        MbxResponse<Void> mbxResponse = matchBoxManagementService.putUserDataStream(mbxRequest);
        return mbxResponse;
    }

    @Override
    public MbxResponse<Void> deleteLotSizeFilter(DeleteLotSizeFilterRequest request) throws MbxException {
        MbxDeleteLotSizeFilterRequest mbxRequest = new MbxDeleteLotSizeFilterRequest(request.getSymbol());
        MbxResponse<Void> mbxResponse = matchBoxManagementService.deleteLotSizeFilter(mbxRequest);
        return mbxResponse;
    }

    @Override
    public MbxResponse<Void> deleteNumAlgoOrdersFilter(DeleteNumAlgoOrdersFilterRequest request) throws MbxException {
        MbxDeleteNumAlgoOrdersFilterRequest mbxRequest = new MbxDeleteNumAlgoOrdersFilterRequest(request.getSymbol());
        MbxResponse<Void> mbxResponse = matchBoxManagementService.deleteNumAlgoOrdersFilter(mbxRequest);
        return mbxResponse;
    }

    @Override
    public MbxResponse<Void> postMarketLotSizeFilter(PostMarketLotSizeFilterRequest request) throws MbxException {
        MbxPostMarketLotSizeFilterRequest mbxRequest = new MbxPostMarketLotSizeFilterRequest(request.getMaxQty(),
                request.getMinQty(), request.getStepSize(), request.getSymbol());
        MbxResponse<Void> mbxResponse = matchBoxManagementService.postMarketLotSizeFilter(mbxRequest);
        return mbxResponse;
    }

    @Override
    public MbxResponse<GetOrderRateLimitResponse> getOrderRateLimit(GetOrderRateLimitRequest request) throws MbxException {
        MbxGetOrderRateLimitRequest mbxRequest = new MbxGetOrderRateLimitRequest(request.getRateInterval());
        MbxResponse<MbxGetOrderRateLimitResponse> mbxResponse = matchBoxManagementService.getOrderRateLimit(mbxRequest);

        MbxResponse<GetOrderRateLimitResponse> response = MbxResponse.of(mbxResponse.getState());
        if (response.isSuccess()) {
            response.setData(GetOrderRateLimitResponseConverter.convert(mbxResponse.getData()));
        }
        return response;
    }

    @Override
    public MbxResponse<String> getAuctionReportsForString(GetAuctionReportsRequest request) throws MbxException {
        MbxGetAuctionReportsRequest mbxRequest = new MbxGetAuctionReportsRequest(request.getStartTime(), request.getSymbol());
        MbxResponse<String> mbxResponse = matchBoxManagementService.getAuctionReportsForString(mbxRequest);
        return mbxResponse;
    }

    @Override
    public MbxResponse<List<GetAccountResponse>> getAccountByExternalId(GetAccountByExternalIdRequest request) throws MbxException {
        MbxGetAccountByExternalIdRequest mbxRequest = new MbxGetAccountByExternalIdRequest(request.getExternalId());
        MbxResponse<List<MbxGetAccountResult>> mbxResponse = matchBoxManagementService.getAccountByExternalId(mbxRequest);

        MbxResponse<List<GetAccountResponse>> response = MbxResponse.of(mbxResponse.getState());
        if (response.isSuccess()) {
            List<MbxGetAccountResult> mbxList = mbxResponse.getData();
            List<GetAccountResponse> targetList = new ArrayList<>(mbxList.size());
            for (MbxGetAccountResult mbxGetAccountResult : mbxList) {
                targetList.add(GetAccountResponseConverter.convert(mbxGetAccountResult));
            }
            response.setData(targetList);
        }
        return response;
    }

    @Override
    public MbxResponse<List<GetAccountResponseV3>> getAccountByExternalIdV3(GetAccountByExternalIdRequest request) throws MbxException {
        MbxGetAccountByExternalAccountIdV3Request mbxRequest = new MbxGetAccountByExternalAccountIdV3Request(request.getExternalId());
        MbxResponse<List<MbxGetAccountResult>> mbxResponse = matchBoxManagementService.getAccountByExternalAccountIdV3(mbxRequest);

        MbxResponse<List<GetAccountResponseV3>> response = MbxResponse.of(mbxResponse.getState());
        if (response.isSuccess()) {
            List<MbxGetAccountResult> mbxList = mbxResponse.getData();
            List<GetAccountResponseV3> targetList = new ArrayList<>(mbxList.size());
            for (MbxGetAccountResult mbxGetAccountResult : mbxList) {
                targetList.add(GetAccountResponseV3Converter.convert(mbxGetAccountResult));
            }
            response.setData(targetList);
        }
        return response;
    }

    @Override
    public MbxResponse<List<GetAccountSymbolsCommissionResponse>> getAccountSymbolsCommission(GetAccountSymbolsCommissionRequest request) throws MbxException {
        MbxGetAccountSymbolsCommissionRequest mbxRequest = new MbxGetAccountSymbolsCommissionRequest(request.getAccountId());
        MbxResponse<List<MbxGetAccountSymbolsCommissionResponse>> mbxResponse = matchBoxManagementService.getAccountSymbolsCommission(mbxRequest);
        MbxResponse<List<GetAccountSymbolsCommissionResponse>> response = MbxResponse.of(mbxResponse.getState());
        if (response.isSuccess()) {
            List<MbxGetAccountSymbolsCommissionResponse> mbxList = mbxResponse.getData();
            List<GetAccountSymbolsCommissionResponse> targetList = new ArrayList<>(mbxList.size());
            for (MbxGetAccountSymbolsCommissionResponse mbxGetAccountSymbolsCommissionResponse : mbxList) {
                targetList.add(GetAccountSymbolsCommissionResponseConverter.convert(mbxGetAccountSymbolsCommissionResponse));
            }
            response.setData(targetList);
        }
        return response;
    }

    @Override
    public MbxResponse<Void> putOrderRateLimit(PutOrderRateLimitRequest request) throws MbxException {
        MbxPutOrderRateLimitRequest mbxRequest = new MbxPutOrderRateLimitRequest(request.getLimit(),
                request.getRateInterval());
        MbxResponse<Void> mbxResponse = matchBoxManagementService.putOrderRateLimit(mbxRequest);
        return mbxResponse;
    }

    @Override
    public MbxResponse<Void> deleteOrderRateLimitAccount(DeleteOrderRateLimitAccountRequest request) throws MbxException {
        MbxDeleteOrderRateLimitAccountRequest mbxRequest = new MbxDeleteOrderRateLimitAccountRequest(
                request.getAccountId(), request.getRateInterval());
        MbxResponse<Void> mbxResponse = matchBoxManagementService.deleteOrderRateLimitAccount(mbxRequest);
        return mbxResponse;
    }

    @Override
    public MbxResponse<Void> putApiKeyPermissions(PutApiKeyPermissionsRequest request) throws MbxException {
        MbxPutApiKeyPermissionsRequest mbxRequest = new MbxPutApiKeyPermissionsRequest(request.getAccountId(),
                 request.getKeyId(), request.isCanAccessSecureWs(), request.isCanControlUserStreams(),
                request.isCanTrade(), request.isCanViewMarketData(), request.isCanViewUserData());
        mbxRequest.setForce(request.getForce());

        MbxResponse<Void> mbxResponse = matchBoxManagementService.putApiKeyPermissions(mbxRequest);
        return mbxResponse;
    }

    @Override
    public MbxResponse<GetEstimateSymbolResponse> getEstimateSymbol(GetEstimateSymbolRequest request) throws MbxException {
        MbxGetEstimateSymbolRequest mbxRequest = build1(request);
        MbxResponse<MbxGetEstimateSymbolResponse> mbxResponse = matchBoxManagementService.getEstimateSymbol(mbxRequest);

        MbxResponse<GetEstimateSymbolResponse> response = MbxResponse.of(mbxResponse.getState());
        if (response.isSuccess()) {
            response.setData(GetEstimateSymbolResponseConverter.convert(mbxResponse.getData()));
        }
        return response;
    }

    private MbxGetEstimateSymbolRequest build1(GetEstimateSymbolRequest request) {
        MbxGetEstimateSymbolRequest mbxGetEstimateSymbolRequest = new MbxGetEstimateSymbolRequest(
                request.getBaseDecimalPlaces(), request.getQuoteDecimalPlaces());
        mbxGetEstimateSymbolRequest.setMathSystemType(request.getMathSystemType());
        mbxGetEstimateSymbolRequest.setMaxPrice(request.getMaxPrice());
        mbxGetEstimateSymbolRequest.setMaxQty(request.getMaxQty());
        return mbxGetEstimateSymbolRequest;
    }

    @Override
    public MbxResponse<Void> putAccountSymbolsCommission(PutAccountSymbolsCommissionRequest request) throws MbxException {
        MbxPutAccountSymbolsCommissionRequest mbxRequest = new MbxPutAccountSymbolsCommissionRequest(
                request.getAccountId(), request.getSymbols(), request.getBuyerCommission(), request.getMakerCommission(),
                request.getSellerCommission(), request.getTakerCommission());
        MbxResponse<Void> mbxResponse = matchBoxManagementService.putAccountSymbolsCommission(mbxRequest);
        return mbxResponse;
    }

    @Override
    public MbxResponse<List<GetAccountTPlusLockStateResponse>> getAccountTPlusLockState(GetAccountTPlusLockStateRequest request) throws MbxException {
        MbxGetAccountTPlusLockStateRequest mbxRequest = new MbxGetAccountTPlusLockStateRequest(request.getAccountId(),
                request.getSymbols());
        MbxResponse<List<MbxGetAccountTPlusLockStateResponse>> mbxResponse = matchBoxManagementService.getAccountTPlusLockState(mbxRequest);

        MbxResponse<List<GetAccountTPlusLockStateResponse>> response = MbxResponse.of(mbxResponse.getState());
        if (response.isSuccess()) {
            List<MbxGetAccountTPlusLockStateResponse> mbxList = mbxResponse.getData();

            List<GetAccountTPlusLockStateResponse> targetList = new ArrayList<>(mbxList.size());
            for (MbxGetAccountTPlusLockStateResponse mbxGetAccountTPlusLockStateResponse : mbxList) {
                targetList.add(GetAccountTPlusLockStateResponseConverter.convert(mbxGetAccountTPlusLockStateResponse));
            }
            response.setData(targetList);
        }
        return response;
    }

    @Override
    public MbxResponse<Void> putOrderRateLimitAccount(PutOrderRateLimitAccountRequest request) throws MbxException {
        MbxPutOrderRateLimitAccountRequest mbxRequest = new MbxPutOrderRateLimitAccountRequest(request.getAccountId(),
                request.getLimit(), request.getRateInterval());
        MbxResponse<Void> mbxResponse = matchBoxManagementService.putOrderRateLimitAccount(mbxRequest);
        return mbxResponse;
    }

    @Override
    public MbxResponse<Void> getPing() throws MbxException {
        MbxResponse<Void> mbxResponse = matchBoxManagementService.getPing(new MbxGetPingRequest());
        return mbxResponse;
    }

    @Override
    public MbxResponse<List<GetCommandsResponse>> getCommands() throws MbxException {
        MbxResponse<List<GetCommandsResponse>> mbxResponse = matchBoxManagementService.getCommands(new MbxGetCommandsRequest());
        return mbxResponse;
    }

    @Override
    public MbxResponse<Void> putExchangeGas(PutExchangeGasRequest request) throws MbxException {
        MbxPutExchangeGasRequest mbxRequest = new MbxPutExchangeGasRequest(request.getAsset(), request.getBips());
        MbxResponse<Void> mbxResponse = matchBoxManagementService.putExchangeGas(mbxRequest);
        return mbxResponse;
    }
}
