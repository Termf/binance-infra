package com.binance.platform.mbx.client;

import com.binance.platform.mbx.exception.MbxException;
import com.binance.platform.mbx.matchbox.model.MbxResponse;
import com.binance.platform.mbx.model.account.ActivateAccountRequest;
import com.binance.platform.mbx.model.account.ApiKeyInfoVo;
import com.binance.platform.mbx.model.account.BalanceLedgerResponse;
import com.binance.platform.mbx.model.account.DeleteRuleByRuleIdRequest;
import com.binance.platform.mbx.model.account.GetAccountRequest;
import com.binance.platform.mbx.model.account.GetApiCheckRequest;
import com.binance.platform.mbx.model.account.GetApiInfoRequest;
import com.binance.platform.mbx.model.account.GetBlanceLedgerRequest;
import com.binance.platform.mbx.model.account.LockApiTradeRequest;
import com.binance.platform.mbx.model.account.RepairLockedRequest;
import com.binance.platform.mbx.model.account.SaveApiRuleRequest;
import com.binance.platform.mbx.model.account.SetGasRequest;
import com.binance.platform.mbx.model.account.SetTradeRequest;
import com.binance.platform.mbx.model.account.TradingAccountResponse;
import com.binance.platform.mbx.model.account.TradingAccountResponseV3;
import com.binance.platform.mbx.model.account.UnLockApiTradeRequest;
import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author Li Fenggang
 * Date: 2020/7/15 3:55 下午
 */
//@RunWith(SpringRunner.class)
//@SpringBootTest(classes = Application.class)
public class AccountClientTest {
    private static final String DEFAULT_ASSET = "BTC";
    private static final long DEFAULT_USER_ID = 350788204;
    public static final String API_KEY = "8XKiLkdvVKMR5kWSjAbihR64VJFB907uB8kvwINgQx7BheCPrkencmnUs315sgpT";
    public static final String IP = "192.168.1.2";
    public static final int KEY_ID = 56;

    @Autowired
    private AccountClient accountClient;

    // @Test
    public void repairLocked() throws MbxException {
        RepairLockedRequest request = new RepairLockedRequest(DEFAULT_USER_ID, DEFAULT_ASSET, new BigDecimal(1000));
        MbxResponse<Void> mbxResponse = accountClient.repairLocked(request);
        Assert.assertTrue(mbxResponse.isSuccess());
    }

    // @Test
    public void unlockApiTrade() throws MbxException {
        UnLockApiTradeRequest request = new UnLockApiTradeRequest(DEFAULT_USER_ID);
        MbxResponse<Void> mbxResponse = accountClient.unlockApiTrade(request);
        Assert.assertTrue(mbxResponse.isSuccess());
    }

    // @Test
    public void lockApiTrade() throws MbxException {
        LockApiTradeRequest request = new LockApiTradeRequest(DEFAULT_USER_ID);
        MbxResponse<Void> mbxResponse = accountClient.lockApiTrade(request);
        Assert.assertTrue(mbxResponse.isSuccess());
    }

    // @Test
    public void getApiCheck() throws MbxException {
        GetApiCheckRequest request =
                new GetApiCheckRequest(API_KEY,
                        DEFAULT_USER_ID, IP);
        MbxResponse<ApiKeyInfoVo> mbxResponse = accountClient.getApiCheck(request);
        Assert.assertTrue(mbxResponse.isSuccess());
    }

    // @Test
    public void getApiCheckForWithdraw() throws MbxException {
        GetApiCheckRequest request = new GetApiCheckRequest(API_KEY, DEFAULT_USER_ID, IP);
        MbxResponse<ApiKeyInfoVo> mbxResponse = accountClient.getApiCheckForWithdraw(request);
        Assert.assertTrue(mbxResponse.isSuccess());
    }

    // @Test
    public void setTrade() throws MbxException {
        SetTradeRequest request = new SetTradeRequest(DEFAULT_USER_ID, true);
        MbxResponse<Void> mbxResponse = accountClient.setTrade(request);
        Assert.assertTrue(mbxResponse.isSuccess());
    }

    // @Test
    public void setGas() throws MbxException {
        SetGasRequest request = new SetGasRequest(DEFAULT_USER_ID, true);
        MbxResponse<Void> mbxResponse = accountClient.setGas(request);
        Assert.assertTrue(mbxResponse.isSuccess());
    }

    // @Test
    public void getAccount() throws MbxException {
        GetAccountRequest request = new GetAccountRequest(DEFAULT_USER_ID);
        MbxResponse<TradingAccountResponse> mbxResponse = accountClient.getAccount(request);
        Assert.assertTrue(mbxResponse.isSuccess());
    }

    // @Test
    public void getAccountV3() throws MbxException {
        GetAccountRequest request = new GetAccountRequest(DEFAULT_USER_ID);
        MbxResponse<TradingAccountResponseV3> mbxResponse = accountClient.getAccountV3(request);
        Assert.assertTrue(mbxResponse.isSuccess());
    }

    // @Test
    public void getAccountByExternalIdV3() throws MbxException {
        GetAccountRequest request = new GetAccountRequest(DEFAULT_USER_ID);
        MbxResponse<List<TradingAccountResponseV3>> mbxResponse = accountClient.getAccountByExternalIdV3(request);
        Assert.assertTrue(mbxResponse.isSuccess());
    }

    // @Test
    public void activateAccount() throws MbxException {
        ActivateAccountRequest request = new ActivateAccountRequest(DEFAULT_USER_ID, true, true, true);
        MbxResponse<Void> mbxResponse = accountClient.activateAccount(request);
        Assert.assertTrue(mbxResponse.isSuccess());
    }

    // @Test
    public void saveApiRule() throws MbxException {
        SaveApiRuleRequest request = new SaveApiRuleRequest(DEFAULT_USER_ID, KEY_ID, IP);
        MbxResponse<Void> mbxResponse = accountClient.saveApiRule(request);
        Assert.assertTrue(mbxResponse.isSuccess());
    }

    // @Test
    public void deleteRuleByRuleId() throws MbxException {
        DeleteRuleByRuleIdRequest request = new DeleteRuleByRuleIdRequest(DEFAULT_USER_ID, KEY_ID, 57);
        MbxResponse<Void> mbxResponse = accountClient.deleteRuleByRuleId(request);
        Assert.assertTrue(mbxResponse.isSuccess());
    }

    // @Test
    public void getApiInfo() throws MbxException {
        GetApiInfoRequest request = new GetApiInfoRequest(DEFAULT_USER_ID);
        MbxResponse<List<ApiKeyInfoVo>> mbxResponse = accountClient.getApiInfo(request);
        Assert.assertTrue(mbxResponse.isSuccess());
    }

    // @Test
    public void getBalanceLedger() {
        GetBlanceLedgerRequest request = new GetBlanceLedgerRequest(350658106, "BTC");
        Date startDate = new Date();
        startDate.setTime(1);
        request.setStartTime(startDate);
        request.setEndTime(new Date());
        MbxResponse<List<BalanceLedgerResponse>> mbxResponse = accountClient.getBalanceLedger(request);
        System.out.println(mbxResponse);
        Assert.assertTrue(mbxResponse.isSuccess());
    }
}
