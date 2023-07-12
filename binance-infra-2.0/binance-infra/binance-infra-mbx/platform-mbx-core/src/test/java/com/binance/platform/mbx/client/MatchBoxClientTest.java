package com.binance.platform.mbx.client;

import com.binance.platform.mbx.exception.MbxException;
import com.binance.platform.mbx.matchbox.model.MbxResponse;
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
import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @author Li Fenggang
 * Date: 2020/7/15 8:49 下午
 */
//@RunWith(SpringRunner.class)
//@SpringBootTest(classes = Application.class)
public class MatchBoxClientTest {
    private static final String DEFAULT_ASSET = "BTC";
    private static final String DEFAULT_SYMBOL = "BTCUSDT";
    private static final long DEFAULT_ACCOUNT_ID = 6;
    private static final long DEFAULT_USER_ID = 350788204;
    private static final Date DEFAULT_START_TIME = new Date(1);
    private static final Date DEFAULT_END_TIME = new Date();
    private static final long DEFAULT_START_TIME_LONG = DEFAULT_START_TIME.getTime();
    private static final long DEFAULT_END_TIME_LONG = DEFAULT_END_TIME.getTime();

    @Autowired
    private MatchBoxClient matchBoxClient;

    // @Test
    public void getTime() throws MbxException {
        MbxResponse<GetTimeResponse> mbxResponse = matchBoxClient.getTime();
        Assert.assertTrue(mbxResponse.isSuccess());
    }

    // @Test
    public void getSymbolOrders() throws MbxException {
        GetSymbolOrdersRequest request = new GetSymbolOrdersRequest(DEFAULT_SYMBOL);
        request.setLimit(2L);
        MbxResponse<List<GetSymbolOrdersResponse>> mbxResponse = matchBoxClient.getSymbolOrders(request);
        Assert.assertTrue(mbxResponse.isSuccess());
    }

    // @Test
    public void getAllOrders() throws MbxException {
        GetAllOrdersRequest request = new GetAllOrdersRequest(DEFAULT_ACCOUNT_ID);
        request.setLimit(2L);
        MbxResponse<List<GetAllOrdersResponse>> mbxResponse = matchBoxClient.getAllOrders(request);

        Assert.assertTrue(mbxResponse.isSuccess());
    }

    // @Test
    public void getBalanceLedger() throws MbxException {
        GetBalanceLedgerRequest request = new GetBalanceLedgerRequest(DEFAULT_ACCOUNT_ID, DEFAULT_ASSET);
        request.setStartTime(DEFAULT_START_TIME_LONG);
        request.setEndTime(DEFAULT_END_TIME_LONG);
        MbxResponse<List<GetBalanceLedgerResponse>> mbxResponse = matchBoxClient.getBalanceLedger(request);

        Assert.assertTrue(mbxResponse.isSuccess());
    }

    // @Test
    public void getAllAccounts() throws MbxException {
        MbxResponse<List<GetAllAccountsResponse>> mbxResponse = matchBoxClient.getAllAccounts();

        Assert.assertTrue(mbxResponse.isSuccess());
    }

    // @Test
    public void getOpenOrders() throws MbxException {
        GetOpenOrdersRequest request = new GetOpenOrdersRequest(DEFAULT_ACCOUNT_ID);
        MbxResponse<List<GetOpenOrdersResponse>> mbxResponse = matchBoxClient.getOpenOrders(request);
        Assert.assertTrue(mbxResponse.isSuccess());
    }

    // @Test
    public void getMsgAuthOrders() throws MbxException {
        GetMsgAuthOrdersRequest request = new GetMsgAuthOrdersRequest(DEFAULT_START_TIME_LONG, "LIQUIDATION");
        request.setAccountId(DEFAULT_ACCOUNT_ID);
        MbxResponse<List<GetMsgAuthOrdersResponse>> mbxResponse = matchBoxClient.getMsgAuthOrders(request);

        Assert.assertTrue(mbxResponse.isSuccess());
    }

    // @Test
    public void getOrder() throws MbxException {
        GetOrderRequest request = new GetOrderRequest(DEFAULT_SYMBOL);
        request.setOrderId(1L);
        MbxResponse<GetOrderResponse> mbxResponse = matchBoxClient.getOrder(request);
        Assert.assertTrue(mbxResponse.isSuccess());
    }

    // @Test
    public void getAccount() throws MbxException {
        GetAccountRequest request = new GetAccountRequest(DEFAULT_ACCOUNT_ID);
        MbxResponse<GetAccountResponse> mbxResponse = matchBoxClient.getAccount(request);
        Assert.assertTrue(mbxResponse.isSuccess());
    }

    // @Test
    public void getApiKeys() throws MbxException {
        GetApiKeysRequest request = new GetApiKeysRequest(DEFAULT_ACCOUNT_ID);
        MbxResponse<List<GetApiKeysResponse>> mbxResponse = matchBoxClient.getApiKeys(request);
        Assert.assertTrue(mbxResponse.isSuccess());
    }

    // @Test
    public void getApiKeyLock() throws MbxException {
        GetApiKeyLockRequest request = new GetApiKeyLockRequest(DEFAULT_ACCOUNT_ID);
        MbxResponse<GetApiKeyLockResponse> mbxResponse = matchBoxClient.getApiKeyLock(request);
        Assert.assertTrue(mbxResponse.isSuccess());
    }

    // @Test
    public void getAssetBalance() throws MbxException {
        GetAssetBalanceRequest request = new GetAssetBalanceRequest(DEFAULT_ASSET);
        MbxResponse<GetAssetBalanceResponse> mbxResponse = matchBoxClient.getAssetBalance(request);
        Assert.assertTrue(mbxResponse.isSuccess());
    }

    // @Test
    public void getAssetLedger() throws MbxException {
        GetAssetLedgerRequest request = new GetAssetLedgerRequest(DEFAULT_ASSET, DEFAULT_START_TIME_LONG, DEFAULT_END_TIME_LONG);
        MbxResponse<List<GetAssetLedgerResponse>> mbxResponse = matchBoxClient.getAssetLedger(request);
        Assert.assertTrue(mbxResponse.isSuccess());
    }

    // @Test
    public void getSymbols() throws MbxException {
        MbxResponse<List<GetSymbolsResponse>> mbxResponse = matchBoxClient.getSymbols();
        Assert.assertTrue(mbxResponse.isSuccess());
    }

    // @Test
    public void getFilters() throws MbxException {
        GetFiltersRequest request = new GetFiltersRequest(DEFAULT_SYMBOL);
        MbxResponse<GetFiltersResponse> mbxResponse = matchBoxClient.getFilters(request);
        Assert.assertTrue(mbxResponse.isSuccess());
    }

    // @Test
    public void getSymbolHistory() throws MbxException {
        GetSymbolHistoryRequest request = new GetSymbolHistoryRequest(DEFAULT_START_TIME_LONG, DEFAULT_SYMBOL);
        MbxResponse<List<GetSymbolHistoryResponse>> mbxResponse = matchBoxClient.getSymbolHistory(request);
        Assert.assertTrue(mbxResponse.isSuccess());
    }

    // @Test
    public void getCommission() throws MbxException {
        GetCommissionRequest request = new GetCommissionRequest(DEFAULT_SYMBOL);
        MbxResponse<List<GetCommissionResponse>> mbxResponse = matchBoxClient.getCommission(request);
        Assert.assertTrue(mbxResponse.isSuccess());
    }

    // @Test
    public void getUserTrades() throws MbxException {
        GetUserTradesRequest request = new GetUserTradesRequest(DEFAULT_ACCOUNT_ID);
        request.setLimit(2L);
        MbxResponse<List<GetUserTradesResponse>> mbxResponse = matchBoxClient.getUserTrades(request);
        Assert.assertTrue(mbxResponse.isSuccess());
    }

    // @Test
    public void getAssets() throws MbxException {
        MbxResponse<List<GetAssetsResponse>> mbxResponse = matchBoxClient.getAssets();
        Assert.assertTrue(mbxResponse.isSuccess());
    }

    // @Test
    public void getTrades() throws MbxException {
        GetTradesRequest request = new GetTradesRequest(DEFAULT_SYMBOL);
        request.setLimit(2L);
        MbxResponse<List<GetTradesResponse>> mbxResponse = matchBoxClient.getTrades(request);
        Assert.assertTrue(mbxResponse.isSuccess());
    }

    // @Test
    public void getAccountSymbolCommissionHistory() throws MbxException {
        GetAccountSymbolCommissionHistoryRequest request =
                new GetAccountSymbolCommissionHistoryRequest(DEFAULT_ACCOUNT_ID,
                        DEFAULT_END_TIME_LONG,
                        DEFAULT_START_TIME_LONG,
                        DEFAULT_SYMBOL);
        MbxResponse<List<GetAccountSymbolCommissionHistoryResponse>> mbxResponse = matchBoxClient.getAccountSymbolCommissionHistory(request);
        Assert.assertTrue(mbxResponse.isSuccess());
    }

    // @Test
    public void getSymbolCommissionHistory() throws MbxException {
        GetSymbolCommissionHistoryRequest request =
                new GetSymbolCommissionHistoryRequest(DEFAULT_END_TIME_LONG, DEFAULT_START_TIME_LONG, DEFAULT_SYMBOL);
        MbxResponse<List<GetSymbolCommissionHistoryResponse>> mbxResponse = matchBoxClient.getSymbolCommissionHistory(request);
        Assert.assertTrue(mbxResponse.isSuccess());
    }

    // @Test
    public void getSymbolCommission() throws MbxException {
        GetSymbolCommissionRequest request = new GetSymbolCommissionRequest(DEFAULT_SYMBOL);
        MbxResponse<GetSymbolCommissionResponse> mbxResponse = matchBoxClient.getSymbolCommission(request);
        Assert.assertTrue(mbxResponse.isSuccess());
    }

    // @Test
    public void getOrderRateLimit() throws MbxException {
        GetOrderRateLimitRequest request = new GetOrderRateLimitRequest("DAY");
        MbxResponse<GetOrderRateLimitResponse> mbxResponse = matchBoxClient.getOrderRateLimit(request);
        Assert.assertTrue(mbxResponse.isSuccess());
    }

    // @Test
    public void getAuctionReports() throws MbxException {
        GetAuctionReportsRequest request = new GetAuctionReportsRequest(DEFAULT_START_TIME_LONG, DEFAULT_SYMBOL);
        MbxResponse<String> mbxResponse = matchBoxClient.getAuctionReportsForString(request);
        Assert.assertTrue(mbxResponse.isSuccess());
    }

    // @Test
    public void getAccountByExternalId() throws MbxException {
        GetAccountByExternalIdRequest request = new GetAccountByExternalIdRequest(DEFAULT_USER_ID);
        MbxResponse<List<GetAccountResponse>> mbxResponse = matchBoxClient.getAccountByExternalId(request);
        Assert.assertTrue(mbxResponse.isSuccess());
    }

    // @Test
    public void getAccountByExternalIdV3() throws MbxException {
        GetAccountByExternalIdRequest request = new GetAccountByExternalIdRequest(DEFAULT_USER_ID);
        MbxResponse<List<GetAccountResponseV3>> mbxResponse = matchBoxClient.getAccountByExternalIdV3(request);
        Assert.assertTrue(mbxResponse.isSuccess());
    }

    // @Test
    public void getAccountSymbolsCommission() throws MbxException {
        GetAccountSymbolsCommissionRequest request = new GetAccountSymbolsCommissionRequest(DEFAULT_ACCOUNT_ID);
        MbxResponse<List<GetAccountSymbolsCommissionResponse>> mbxResponse = matchBoxClient.getAccountSymbolsCommission(request);
        Assert.assertTrue(mbxResponse.isSuccess());
    }

    // @Test
    public void getEstimateSymbol() throws MbxException {
        GetEstimateSymbolRequest request = new GetEstimateSymbolRequest("8", "8");
        request.setMaxQty(String.valueOf(10));
        MbxResponse<GetEstimateSymbolResponse> mbxResponse = matchBoxClient.getEstimateSymbol(request);
        Assert.assertTrue(mbxResponse.isSuccess());
    }

    // @Test
    public void getAccountTPlusLockState() throws MbxException {
        GetAccountTPlusLockStateRequest request = new GetAccountTPlusLockStateRequest(DEFAULT_ACCOUNT_ID, Arrays.asList(DEFAULT_SYMBOL));
        MbxResponse<List<GetAccountTPlusLockStateResponse>> mbxResponse = matchBoxClient.getAccountTPlusLockState(request);
        Assert.assertTrue(mbxResponse.isSuccess());
    }

    // @Test
    public void getPing() throws MbxException {
        MbxResponse<Void> mbxResponse = matchBoxClient.getPing();
        Assert.assertTrue(mbxResponse.isSuccess());
    }

    // @Test
    public void getCommands() throws MbxException {
        MbxResponse<List<GetCommandsResponse>> mbxResponse = matchBoxClient.getCommands();
        Assert.assertTrue(mbxResponse.isSuccess());
    }

    // @Test
    public void apiKeyTest() throws MbxException {
        // create api key
        PostApiKeyRequest postApiKeyRequest = new PostApiKeyRequest(DEFAULT_ACCOUNT_ID, "unitTest");
        MbxResponse<PostApiKeyResponse> postApiKeyResponseMbxResponse = matchBoxClient.postApiKey(postApiKeyRequest);

        Assert.assertTrue("create api key error", postApiKeyResponseMbxResponse.isSuccess());

        PostApiKeyResponse postApiKeyResponse = postApiKeyResponseMbxResponse.getData();
        String apiKey = postApiKeyResponse.getApiKey();
        Long keyId = postApiKeyResponse.getKeyId();
        try {
            String ip = "192.168.1.1";
            // create api key rule
            PostApiKeyRuleRequest postApiKeyRuleRequest = new PostApiKeyRuleRequest(DEFAULT_ACCOUNT_ID, ip, keyId);
            MbxResponse<Void> postApiKeyRuleMbxResponse = matchBoxClient.postApiKeyRule(postApiKeyRuleRequest);
            Assert.assertTrue("create api key rule error", postApiKeyRuleMbxResponse.isSuccess());
            Long ruleId = null;
            try {
                // api key permission
                PutApiKeyPermissionsRequest putApiKeyPermissionsRequest =
                        new PutApiKeyPermissionsRequest(DEFAULT_ACCOUNT_ID, keyId, false,
                                false, false, false, false);
                MbxResponse<Void> putApiKeyPermissionsResponse = matchBoxClient.putApiKeyPermissions(putApiKeyPermissionsRequest);
                Assert.assertTrue("Modify api key permissions error", putApiKeyPermissionsResponse.isSuccess());

                // Set api key lock
                PutApiKeyLockRequest putApiKeyLockRequest = new PutApiKeyLockRequest(DEFAULT_ACCOUNT_ID);
                MbxResponse<Void> putApiKeyLockResponse = matchBoxClient.putApiKeyLock(putApiKeyLockRequest);
                Assert.assertTrue("Set api key lock error", putApiKeyLockResponse.isSuccess());

                // delete api key lock
                DeleteApiKeyLockRequest deleteApiKeyLockRequest = new DeleteApiKeyLockRequest(DEFAULT_ACCOUNT_ID, true);
                MbxResponse<Void> deleteApiKeyLockResponse = matchBoxClient.deleteApiKeyLock(deleteApiKeyLockRequest);
                Assert.assertTrue("Delete api key lock error", deleteApiKeyLockResponse.isSuccess());

                // api key check
                GetApiKeyCheckRequest getApiKeyCheckRequest = new GetApiKeyCheckRequest(apiKey, ip);
                MbxResponse<GetApiKeyCheckResponse> mbxResponse = matchBoxClient.getApiKeyCheck(getApiKeyCheckRequest);
                Assert.assertTrue("check api key error", mbxResponse.isSuccess());
                GetApiKeyCheckResponse apiKeyCheckResponse = mbxResponse.getData();
                List<GetApiKeyCheckResponse.Rule> rules = apiKeyCheckResponse.getRules();
                for (GetApiKeyCheckResponse.Rule rule : rules) {
                    if (ip.equals(rule.getIp())) {
                        ruleId = rule.getRuleId();
                        break;
                    }
                }
            } finally {
                // delete api key rule
                Assert.assertNotNull("The target rule not found", ruleId);
                DeleteApiKeyRuleRequest deleteApiKeyRuleRequest = new DeleteApiKeyRuleRequest(DEFAULT_ACCOUNT_ID, keyId, ruleId);
                MbxResponse<Void> deleteApiKeyRuleResponse = matchBoxClient.deleteApiKeyRule(deleteApiKeyRuleRequest);
                Assert.assertTrue("delete api key rule error", deleteApiKeyRuleResponse.isSuccess());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // delete api key
            DeleteApiKeyRequest deleteApiKeyRequest = new DeleteApiKeyRequest(DEFAULT_ACCOUNT_ID, keyId);
            MbxResponse<Void> mbxResponse = matchBoxClient.deleteApiKey(deleteApiKeyRequest);
            Assert.assertTrue("delete api key error", mbxResponse.isSuccess());
        }
    }

    // @Test
    public void putAccountSymbolsCommission() throws MbxException {
        PutAccountSymbolsCommissionRequest request = new PutAccountSymbolsCommissionRequest(DEFAULT_ACCOUNT_ID,
                Arrays.asList(DEFAULT_SYMBOL), 2, 3, 4, 5);
        MbxResponse<Void> mbxResponse = matchBoxClient.putAccountSymbolsCommission(request);
        Assert.assertTrue(mbxResponse.isSuccess());
    }

    // @Test
    public void putAccountCommission() throws MbxException {
        PutAccountCommissionRequest request = new PutAccountCommissionRequest(DEFAULT_ACCOUNT_ID,  2,
                3, 4, 5);

        MbxResponse<Void> mbxResponse = matchBoxClient.putAccountCommission(request);
        Assert.assertTrue(mbxResponse.isSuccess());
    }

    // @Test
    public void postBalance() throws MbxException {
        PostBalanceRequest request = new PostBalanceRequest(DEFAULT_ACCOUNT_ID, DEFAULT_ASSET, "100", "1");
        MbxResponse<PostBalanceResponse> mbxResponse = matchBoxClient.postBalance(request);
        Assert.assertTrue(mbxResponse.isSuccess());
    }

    // @Test
    public void postBalanceBatch() {
        PostBalanceBatchRequest request = new PostBalanceBatchRequest();
        List<PostBalanceBatchRequest.Balance> balanceList = new ArrayList<>();

        balanceList.add(buildBalance(1, new BigDecimal("10.1")));
        balanceList.add(buildBalance(2, new BigDecimal("100.1")));

        request.setBalanceList(balanceList);
        MbxResponse<List<PostBalanceBatchResponse>> mbxResponse = matchBoxClient.postBalanceBatch(request);
        Assert.assertTrue(mbxResponse.isSuccess());
    }

    private PostBalanceBatchRequest.Balance buildBalance(long updateId, BigDecimal balanceDelta) {
        PostBalanceBatchRequest.Balance balance = new PostBalanceBatchRequest.Balance();
        balance.setAccountId(DEFAULT_ACCOUNT_ID);
        balance.setAsset(DEFAULT_ASSET);
        balance.setUpdateId(updateId);
        balance.setBalanceDelta(balanceDelta);
        return balance;
    }

    // @Test
    public void postOrder() throws MbxException {
        PostOrderRequest request = new PostOrderRequest(DEFAULT_ACCOUNT_ID, "SELL", DEFAULT_SYMBOL, "MARKET");
        request.setQuantity("1");
        MbxResponse<PostOrderResponse> mbxResponse = matchBoxClient.postOrder(request);
        Assert.assertFalse(mbxResponse.isSuccess());
        Assert.assertEquals(-1013, mbxResponse.getState().getCode().longValue());
    }

    // @Test
    public void deleteOrder() throws MbxException {
        DeleteOrderRequest request = new DeleteOrderRequest(DEFAULT_ACCOUNT_ID, DEFAULT_SYMBOL);
        request.setOrderId("1");
        MbxResponse<DeleteOrderResponse> mbxResponse = matchBoxClient.deleteOrder(request);
        Assert.assertFalse(mbxResponse.isSuccess());
        Assert.assertEquals(-2011, mbxResponse.getState().getCode().longValue());
    }

    // @Test
    public void deleteOpenOrders() throws MbxException {
        DeleteOpenOrdersRequest request = new DeleteOpenOrdersRequest(DEFAULT_ACCOUNT_ID, DEFAULT_SYMBOL);
        MbxResponse<DeleteOpenOrdersResponse> mbxResponse = matchBoxClient.deleteOpenOrders(request);
        Assert.assertFalse(mbxResponse.isSuccess());
        Assert.assertEquals(-2011, mbxResponse.getState().getCode().longValue());
    }

    // @Test
    public void postOcoOrder() throws MbxException {
        PostOcoOrderRequest request = new PostOcoOrderRequest(DEFAULT_ACCOUNT_ID, DEFAULT_SYMBOL, "SELL",
                "1", "9200", "9300");
        MbxResponse<PostOcoOrderResponse> mbxResponse = matchBoxClient.postOcoOrder(request);
        Assert.assertFalse(mbxResponse.isSuccess());
        Assert.assertEquals(-1013, mbxResponse.getState().getCode().longValue());
    }

    // @Test
    public void delOcoOrder() throws MbxException {
        DeleteOcoOrderReq request = new DeleteOcoOrderReq(DEFAULT_ACCOUNT_ID, DEFAULT_SYMBOL);
        request.setOrderListId("1");
        MbxResponse<DeleteOcoOrderResponse> mbxResponse = matchBoxClient.delOcoOrder(request);
        Assert.assertFalse(mbxResponse.isSuccess());
        Assert.assertEquals(-2011, mbxResponse.getState().getCode().longValue());
    }

    // @Test
    public void postAccount() throws MbxException {
        PostAccountRequest request = new PostAccountRequest(123L);
        MbxResponse<PostAccountResponse> mbxResponse = matchBoxClient.postAccount(request);
        Assert.assertTrue(mbxResponse.isSuccess());
    }

    // @Test
    public void postAccountV3() throws MbxException {
        PostAccountRequestV3 request = new PostAccountRequestV3(123L, 0);
        MbxResponse<PostAccountResponseV3> mbxResponse = matchBoxClient.postAccountV3(request);
        Assert.assertTrue(mbxResponse.isSuccess());
    }

    // @Test
    public void putAccount() throws MbxException {
        PutAccountRequest request = new PutAccountRequest(DEFAULT_ACCOUNT_ID, true, true, false);
        MbxResponse<Void> mbxResponse = matchBoxClient.putAccount(request);
        Assert.assertTrue(mbxResponse.isSuccess());
    }

    // @Test
    public void putAccountV3() throws MbxException {
        PutAccountRequestV3 request = new PutAccountRequestV3(DEFAULT_ACCOUNT_ID, 1);
        MbxResponse<PutAccountResponseV3> mbxResponse = matchBoxClient.putAccountV3(request);
        Assert.assertTrue(mbxResponse.isSuccess());
    }

    // @Test
    public void putAccountAsset() throws MbxException {
        PutAccountAssetRequest request = new PutAccountAssetRequest(DEFAULT_ACCOUNT_ID, DEFAULT_ASSET,
                new BigDecimal(100), new BigDecimal("200"), "SPOT");
        MbxResponse<Void> mbxResponse = matchBoxClient.putAccountAsset(request);
        Assert.assertTrue(mbxResponse.isSuccess());
    }

    // @Test
    public void putSymbolLimits() throws MbxException {
        PutSymbolLimitsRequest request = new PutSymbolLimitsRequest(DEFAULT_SYMBOL,
                new BigDecimal("1.1"), new BigDecimal("2.0"), new BigDecimal("3.0"));
        MbxResponse<Void> mbxResponse = matchBoxClient.putSymbolLimits(request);
        Assert.assertTrue(mbxResponse.isSuccess());
    }

    // @Test
    public void putGasOptOut() throws MbxException {
        PutGasOptOutRequest request = new PutGasOptOutRequest(DEFAULT_ACCOUNT_ID);
        MbxResponse<Void> mbxResponse = matchBoxClient.putGasOptOut(request);
        Assert.assertTrue(mbxResponse.isSuccess());
    }

    // @Test
    public void deleteGasOptOut() throws MbxException {
        DeleteGasOptOutRequest request = new DeleteGasOptOutRequest(DEFAULT_ACCOUNT_ID);
        MbxResponse<Void> mbxResponse = matchBoxClient.deleteGasOptOut(request);
        Assert.assertTrue(mbxResponse.isSuccess());
    }

    // @Test
    public void postPriceFilter() throws MbxException {
        PostPriceFilterRequest request = new PostPriceFilterRequest("0.3", "0.1", DEFAULT_SYMBOL, "0.01");
        MbxResponse<Void> mbxResponse = matchBoxClient.postPriceFilter(request);
        Assert.assertTrue(mbxResponse.isSuccess());
    }

    // @Test
    public void deletePriceFilter() throws MbxException {
        DeletePriceFilterRequest request = new DeletePriceFilterRequest(DEFAULT_SYMBOL);
        MbxResponse<Void> mbxResponse = matchBoxClient.deletePriceFilter(request);
        Assert.assertTrue(mbxResponse.isSuccess());
    }

    // @Test
    public void postAsset() throws MbxException {
        PostAssetRequest request = new PostAssetRequest(DEFAULT_ASSET, "8");
        MbxResponse<Void> mbxResponse = matchBoxClient.postAsset(request);
        Assert.assertFalse(mbxResponse.isSuccess());
        Assert.assertEquals(-1010, mbxResponse.getState().getCode().longValue());
    }

    // @Test
    public void putAllSymbols() throws MbxException {
        PutAllSymbolsRequest request = new PutAllSymbolsRequest("TRADING");
        MbxResponse<Void> mbxResponse = matchBoxClient.putAllSymbols(request);
        Assert.assertTrue(mbxResponse.isSuccess());
    }

    // @Test
    public void postSymbol() throws MbxException {
        PostSymbolRequest request = new PostSymbolRequest("BTCUSDT", "SPOT", DEFAULT_ASSET,
                "USDT", "2.00000000", "3.00000000", "1.00000000",
                "GAS_OR_RECV_OPTIONAL", 8, 8);
        MbxResponse<Void> mbxResponse = matchBoxClient.postSymbol(request);
        Assert.assertFalse(mbxResponse.isSuccess());
        Assert.assertEquals("-1010", mbxResponse.getState().getCode().toString());
    }

    // @Test
    public void putSymbol() throws MbxException {
        PutSymbolRequest request = new PutSymbolRequest(DEFAULT_SYMBOL, "TRADING");
        MbxResponse<Void> mbxResponse = matchBoxClient.putSymbol(request);
        Assert.assertFalse(mbxResponse.isSuccess());
        Assert.assertEquals("-1010", mbxResponse.getState().getCode().toString());
    }

    // @Test
    public void putExchangeRate() throws MbxException {
        PutExchangeRateRequest request = new PutExchangeRateRequest(DEFAULT_ASSET, "1.0", "USDT");
        MbxResponse<Void> mbxResponse = matchBoxClient.putExchangeRate(request);
        Assert.assertFalse(mbxResponse.isSuccess());
        Assert.assertEquals("-1010", mbxResponse.getState().getCode().toString());
    }

    // @Test
    public void putOrderTypes() throws MbxException {
        PutOrderTypesRequest request = new PutOrderTypesRequest(DEFAULT_SYMBOL,true,true,
                true, true, true, true, true);
        MbxResponse<Void> mbxResponse = matchBoxClient.putOrderTypes(request);
        Assert.assertTrue(mbxResponse.isSuccess());
    }

    // @Test
    public void putSymbolPermissions() throws MbxException {
        PutSymbolPermissionsRequest request = new PutSymbolPermissionsRequest(DEFAULT_SYMBOL);
        request.setAllowMargin(true);
        request.setAllowSpot(false);
        MbxResponse<Void> mbxResponse = matchBoxClient.putSymbolPermissions(request);
        Assert.assertTrue(mbxResponse.isSuccess());
    }

    // @Test
    public void putSymbolCommissionType() throws MbxException {
        PutSymbolCommissionTypeRequest request = new PutSymbolCommissionTypeRequest("GAS_OR_RECV_OPTIONAL", "BTCUSDT");
        MbxResponse<Void> mbxResponse = matchBoxClient.putSymbolCommissionType(request);
        Assert.assertFalse(mbxResponse.isSuccess());
        Assert.assertEquals(-1010, mbxResponse.getState().getCode().longValue());
    }

    // @Test
    public void deleteNumOrdersFilter() throws MbxException {
        DeleteNumOrdersFilterRequest request = new DeleteNumOrdersFilterRequest(DEFAULT_SYMBOL);
        MbxResponse<Void> mbxResponse = matchBoxClient.deleteNumOrdersFilter(request);
        Assert.assertTrue(mbxResponse.isSuccess());
    }

    // @Test
    public void postLotSizeFilter() throws MbxException {
        PostLotSizeFilterRequest request = new PostLotSizeFilterRequest("3.0", "1.0", "2.0", DEFAULT_SYMBOL);
        MbxResponse<Void> mbxResponse = matchBoxClient.postLotSizeFilter(request);
        Assert.assertTrue(mbxResponse.isSuccess());
    }

    // @Test
    public void postPercentPriceFilter() throws MbxException {
        PostPercentPriceFilterRequest request = new PostPercentPriceFilterRequest("2", "2", DEFAULT_SYMBOL);
        MbxResponse<Void> mbxResponse = matchBoxClient.postPercentPriceFilter(request);
        Assert.assertTrue(mbxResponse.isSuccess());
    }

    // @Test
    public void deleteTPlusSellFilter() throws MbxException {
        DeleteTPlusSellFilterRequest request = new DeleteTPlusSellFilterRequest(DEFAULT_SYMBOL);
        MbxResponse<Void> mbxResponse = matchBoxClient.deleteTPlusSellFilter(request);
        Assert.assertTrue(mbxResponse.isSuccess());
    }

    // @Test
    public void deletePercentPriceFilter() throws MbxException {
        DeletePercentPriceFilterRequest request = new DeletePercentPriceFilterRequest(DEFAULT_SYMBOL);
        MbxResponse<Void> mbxResponse = matchBoxClient.deletePercentPriceFilter(request);
        Assert.assertTrue(mbxResponse.isSuccess());
    }

    // @Test
    public void postPositionFilter() throws MbxException {
        PostPositionFilterRequest request = new PostPositionFilterRequest("3.0", DEFAULT_SYMBOL);
        MbxResponse<Void> mbxResponse = matchBoxClient.postPositionFilter(request);
        Assert.assertTrue(mbxResponse.isSuccess());
    }

    // @Test
    public void postMinNotionalFilter() throws MbxException {
        PostMinNotionalFilterRequest request = new PostMinNotionalFilterRequest("true", "1", DEFAULT_SYMBOL);
        MbxResponse<Void> mbxResponse = matchBoxClient.postMinNotionalFilter(request);
        Assert.assertTrue(mbxResponse.isSuccess());
    }

    // @Test
    public void deleteNumIcebergOrders() throws MbxException {
        DeleteNumIcebergOrdersRequest request = new DeleteNumIcebergOrdersRequest(DEFAULT_SYMBOL);
        MbxResponse<Void> mbxResponse = matchBoxClient.deleteNumIcebergOrders(request);
        Assert.assertTrue(mbxResponse.isSuccess());
    }

    // @Test
    public void putSymbolCommission() throws MbxException {
        PutSymbolCommissionRequest request = new PutSymbolCommissionRequest(DEFAULT_SYMBOL, 1L,
                2L, 3L, 4L);
        MbxResponse<Void> mbxResponse = matchBoxClient.putSymbolCommission(request);
        Assert.assertTrue(mbxResponse.isSuccess());
    }

    // @Test
    public void postNumOrdersFilter() throws MbxException {
        PostNumOrdersFilterRequest request = new PostNumOrdersFilterRequest("2", DEFAULT_SYMBOL);
        MbxResponse<Void> mbxResponse = matchBoxClient.postNumOrdersFilter(request);
        Assert.assertTrue(mbxResponse.isSuccess());
    }

    // @Test
    public void deleteMinNotionalFilter() throws MbxException {
        DeleteMinNotionalFilterRequest request = new DeleteMinNotionalFilterRequest(DEFAULT_SYMBOL);
        MbxResponse<Void> mbxResponse = matchBoxClient.deleteMinNotionalFilter(request);
        Assert.assertTrue(mbxResponse.isSuccess());
    }

    // @Test
    public void postTPlusSellFilter() throws MbxException {
        PostTPlusSellFilterRequest request = new PostTPlusSellFilterRequest(DEFAULT_END_TIME_LONG, DEFAULT_SYMBOL);
        MbxResponse<Void> mbxResponse = matchBoxClient.postTPlusSellFilter(request);
        Assert.assertTrue(mbxResponse.isSuccess());
    }

    // @Test
    public void postIcebergPartsFilter() throws MbxException {
        PostIcebergPartsFilterRequest request = new PostIcebergPartsFilterRequest(10, DEFAULT_SYMBOL);
        MbxResponse<Void> mbxResponse = matchBoxClient.postIcebergPartsFilter(request);
        Assert.assertTrue(mbxResponse.isSuccess());
    }

    // @Test
    public void userDataStream() throws MbxException {
        PostUserDataStreamRequest postRequest = new PostUserDataStreamRequest(DEFAULT_ACCOUNT_ID);
        MbxResponse<PostUserDataStreamResponse> postResponse = matchBoxClient.postUserDataStream(postRequest);
        Assert.assertTrue(postResponse.isSuccess());
        String listenKey = postResponse.getData().getListenKey();

        // put
        PutUserDataStreamRequest putRequest = new PutUserDataStreamRequest(listenKey, DEFAULT_ACCOUNT_ID);
        MbxResponse<Void> putResponse = matchBoxClient.putUserDataStream(putRequest);
        Assert.assertTrue(putResponse.isSuccess());

        // delete
        DeleteUserDataStreamRequest deleteRequest = new DeleteUserDataStreamRequest(listenKey, DEFAULT_ACCOUNT_ID);
        MbxResponse<Void> deleteResponse = matchBoxClient.deleteUserDataStream(deleteRequest);
        Assert.assertTrue(deleteResponse.isSuccess());
    }

    // @Test
    public void deletePositionFilter() throws MbxException {
        DeletePositionFilterRequest request = new DeletePositionFilterRequest(DEFAULT_SYMBOL);
        MbxResponse<Void> mbxResponse = matchBoxClient.deletePositionFilter(request);
        Assert.assertTrue(mbxResponse.isSuccess());
    }

    // @Test
    public void deleteIcebergPartsFilter() throws MbxException {
        DeleteIcebergPartsFilterRequest request = new DeleteIcebergPartsFilterRequest(DEFAULT_SYMBOL);
        MbxResponse<Void> mbxResponse = matchBoxClient.deleteIcebergPartsFilter(request);
        Assert.assertTrue(mbxResponse.isSuccess());
    }

    // @Test
    public void postNumAlgoOrdersFilter() throws MbxException {
        PostNumAlgoOrdersFilterRequest request = new PostNumAlgoOrdersFilterRequest("1", DEFAULT_SYMBOL);
        MbxResponse<Void> mbxResponse = matchBoxClient.postNumAlgoOrdersFilter(request);
        Assert.assertTrue(mbxResponse.isSuccess());
    }

    // @Test
    public void postNumIcebergOrders() throws MbxException {
        PostNumIcebergOrdersRequest request  = new PostNumIcebergOrdersRequest("1", DEFAULT_SYMBOL);
        MbxResponse<Void> mbxResponse = matchBoxClient.postNumIcebergOrders(request);
        Assert.assertTrue(mbxResponse.isSuccess());
    }

    // @Test
    public void deleteMarketLotSizeFilter() throws MbxException {
        DeleteMarketLotSizeFilterRequest request = new DeleteMarketLotSizeFilterRequest(DEFAULT_SYMBOL);
        MbxResponse<Void> mbxResponse = matchBoxClient.deleteMarketLotSizeFilter(request);
        Assert.assertTrue(mbxResponse.isSuccess());
    }

    // @Test
    public void deleteLotSizeFilter() throws MbxException {
        DeleteLotSizeFilterRequest request = new DeleteLotSizeFilterRequest(DEFAULT_SYMBOL);
        MbxResponse<Void> mbxResponse = matchBoxClient.deleteLotSizeFilter(request);
        Assert.assertTrue(mbxResponse.isSuccess());
    }

    // @Test
    public void deleteNumAlgoOrdersFilter() throws MbxException {
        DeleteNumAlgoOrdersFilterRequest request = new DeleteNumAlgoOrdersFilterRequest(DEFAULT_SYMBOL);
        MbxResponse<Void> mbxResponse = matchBoxClient.deleteNumAlgoOrdersFilter(request);
        Assert.assertTrue(mbxResponse.isSuccess());
    }

    // @Test
    public void postMarketLotSizeFilter() throws MbxException {
        PostMarketLotSizeFilterRequest request = new PostMarketLotSizeFilterRequest("3.0", "1.0",
                "2.0", DEFAULT_SYMBOL);
        MbxResponse<Void> mbxResponse = matchBoxClient.postMarketLotSizeFilter(request);
        Assert.assertTrue(mbxResponse.isSuccess());
    }

    // @Test
    public void putOrderRateLimit() throws MbxException {
        PutOrderRateLimitRequest request = new PutOrderRateLimitRequest(2, "DAY");
        MbxResponse<Void> mbxResponse = matchBoxClient.putOrderRateLimit(request);
        Assert.assertTrue(mbxResponse.isSuccess());
    }

    // @Test
    public void deleteOrderRateLimitAccount() throws MbxException {
        DeleteOrderRateLimitAccountRequest request = new DeleteOrderRateLimitAccountRequest(DEFAULT_ACCOUNT_ID,"DAY");
        MbxResponse<Void> mbxResponse = matchBoxClient.deleteOrderRateLimitAccount(request);
        Assert.assertTrue(mbxResponse.isSuccess());
    }

    // @Test
    public void putOrderRateLimitAccount() throws MbxException {
        PutOrderRateLimitAccountRequest request = new PutOrderRateLimitAccountRequest(DEFAULT_ACCOUNT_ID, 2, "DAY");
        MbxResponse<Void> mbxResponse = matchBoxClient.putOrderRateLimitAccount(request);
        Assert.assertTrue(mbxResponse.isSuccess());
    }

    // @Test
    public void putExchangeGas() throws MbxException {
        PutExchangeGasRequest request = new PutExchangeGasRequest(DEFAULT_ASSET, "2");
        MbxResponse<Void> mbxResponse = matchBoxClient.putExchangeGas(request);
        Assert.assertTrue(mbxResponse.isSuccess());
    }
}
