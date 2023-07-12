package com.binance.mbx.sample.controller;

import com.binance.platform.mbx.client.AccountClient;
import com.binance.platform.mbx.exception.MbxException;
import com.binance.platform.mbx.matchbox.model.MbxResponse;
import com.binance.platform.mbx.model.account.ActivateAccountRequest;
import com.binance.platform.mbx.model.account.ApiKeyInfoVo;
import com.binance.platform.mbx.model.account.BalanceLedgerResponse;
import com.binance.platform.mbx.model.account.CheckIfBalanceDoneInMbxByTransIdRequest;
import com.binance.platform.mbx.model.account.DeleteRuleByRuleIdRequest;
import com.binance.platform.mbx.model.account.GetAccountFromEngineV3Request;
import com.binance.platform.mbx.model.account.GetAccountFromEngineV3Response;
import com.binance.platform.mbx.model.account.GetAccountRequest;
import com.binance.platform.mbx.model.account.GetApiCheckRequest;
import com.binance.platform.mbx.model.account.GetApiInfoRequest;
import com.binance.platform.mbx.model.account.GetBlanceLedgerRequest;
import com.binance.platform.mbx.model.account.LockApiTradeRequest;
import com.binance.platform.mbx.model.account.RepairLockedRequest;
import com.binance.platform.mbx.model.account.SaveApiRuleRequest;
import com.binance.platform.mbx.model.account.SetAgentFeeRequest;
import com.binance.platform.mbx.model.account.SetGasRequest;
import com.binance.platform.mbx.model.account.SetTradeRequest;
import com.binance.platform.mbx.model.account.TradingAccountResponse;
import com.binance.platform.mbx.model.account.TradingAccountResponseV3;
import com.binance.platform.mbx.model.account.UnLockApiTradeRequest;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * An automatic conversion of userId to accountId was introduced, and a unified fuse switch was included that the service was not available。
 *
 * @author Li Fenggang
 * Date: 2020/7/13 2:49 下午
 */
@RestController
@RequestMapping("/account")
public class AccountController {
    @Autowired
    private AccountClient accountClient;

    @PostMapping("/mgmt/account/asset")
    @ApiOperation("修复locked")
    public MbxResponse<Void> repairLocked(RepairLockedRequest request) throws MbxException {
        return accountClient.repairLocked(request);
    }

    @PostMapping("/mgmt/account/getBlanceLedger")
    @ApiOperation("查询撮合出入金")
    public MbxResponse<List<BalanceLedgerResponse>> getBalanceLedger(GetBlanceLedgerRequest request) throws MbxException {
        return accountClient.getBalanceLedger(request);
    }

    public boolean checkIfBalanceDoneInMbx(CheckIfBalanceDoneInMbxByTransIdRequest request) {
        return accountClient.checkIfBalanceDoneInMbx(request);
    }

    @PostMapping("/mgmt/account/unlockApiTrade")
    @ApiOperation("解锁API交易")
    public MbxResponse<Void> unlockApiTrade(UnLockApiTradeRequest request) throws MbxException {
        return accountClient.unlockApiTrade(request);
    }

    @PostMapping("/mgmt/account/lockApiTrade")
    @ApiOperation("锁定API交易")
    public MbxResponse<Void> lockApiTrade(LockApiTradeRequest request) throws MbxException {
        return accountClient.lockApiTrade(request);
    }

    @PostMapping("/mgmt/getApiCheck")
    @ApiOperation("获取API签名验证")
    public MbxResponse<ApiKeyInfoVo> getApiCheck(GetApiCheckRequest request) throws MbxException {
        return accountClient.getApiCheck(request);
    }

    @PostMapping("/mgmt/getApiCheckForWithdraw")
    @ApiOperation("提现获取API签名验证")
    public MbxResponse<ApiKeyInfoVo> getApiCheckForWithdraw(GetApiCheckRequest request) throws MbxException {
        return accountClient.getApiCheckForWithdraw(request);
    }

    @PostMapping("/mgmt/account/setTrade")
    @ApiOperation("设置是否可以交易")
    public MbxResponse<Void> setTrade(SetTradeRequest request) throws MbxException {
        return accountClient.setTrade(request);
    }

    @PostMapping("/mgmt/setGas")
    @ApiOperation("设置手续费GAS")
    public MbxResponse<Void> setGas(SetGasRequest request) throws MbxException {
        return accountClient.setGas(request);
    }

    @PostMapping("/private/account")
    @ApiOperation("")
    public MbxResponse<TradingAccountResponse> getAccount(GetAccountRequest request) throws MbxException {
        return accountClient.getAccount(request);
    }

    @PostMapping("/private/accountV3")
    @ApiOperation("获取交易账户信息，返回中增加mbx v3版本的新增属性")
    public MbxResponse<TradingAccountResponseV3> getAccountV3(GetAccountRequest request) throws MbxException {
        return accountClient.getAccountV3(request);
    }

    @GetMapping("/private/accountFromEngineV3")
    public MbxResponse<GetAccountFromEngineV3Response> getAccountFromEngineV3(GetAccountFromEngineV3Request request)
            throws MbxException {
        return accountClient.getAccountFromEngineV3(request);
    }

    @PostMapping("/private/accountByExternalIdV3")
    @ApiOperation("获取交易账户信息，使用mbx v3接口。该接口第一次查询较慢，不建议常规用户流程使用")
    public MbxResponse<List<TradingAccountResponseV3>> getAccountByExternalIdV3(GetAccountRequest request) throws MbxException {
        return accountClient.getAccountByExternalIdV3(request);
    }

    @PostMapping("/mgmt/account/activate")
    @ApiOperation("")
    public MbxResponse<Void> activateAccount(ActivateAccountRequest request) throws MbxException {
        return accountClient.activateAccount(request);
    }

    @Deprecated
    @PostMapping("/mgmt/user/agentFee")
    @ApiOperation("设置返佣比例")
    public MbxResponse<Void> setAgentFee(SetAgentFeeRequest request) throws MbxException {
        return accountClient.setAgentFee(request);
    }

    @PostMapping("/mgmt/account/saveApiRule")
    @ApiOperation("")
    public MbxResponse<Void> saveApiRule(SaveApiRuleRequest request) throws MbxException {
        return accountClient.saveApiRule(request);
    }

    @PostMapping("/mgmt/account/deleteRuleByRuleId")
    @ApiOperation("")
    public MbxResponse<Void> deleteRuleByRuleId(DeleteRuleByRuleIdRequest request) throws MbxException {
        return accountClient.deleteRuleByRuleId(request);
    }

    @PostMapping("/mgmt/account/getApiInfo")
    @ApiOperation("")
    public MbxResponse<List<ApiKeyInfoVo>> getApiInfo(GetApiInfoRequest request) throws MbxException {
        return accountClient.getApiInfo(request);
    }
}
