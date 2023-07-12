package com.binance.platform.mbx.client;

import com.binance.platform.mbx.exception.MbxException;
import com.binance.platform.mbx.matchbox.model.MbxResponse;
import com.binance.platform.mbx.model.account.ActivateAccountRequest;
import com.binance.platform.mbx.model.account.ApiKeyInfoVo;
import com.binance.platform.mbx.model.account.BalanceLedgerResponse;
import com.binance.platform.mbx.model.account.DeleteRuleByRuleIdRequest;
import com.binance.platform.mbx.model.account.GetAccountFromEngineV3Request;
import com.binance.platform.mbx.model.account.GetAccountFromEngineV3Response;
import com.binance.platform.mbx.model.account.GetAccountRequest;
import com.binance.platform.mbx.model.account.GetApiCheckRequest;
import com.binance.platform.mbx.model.account.GetApiInfoRequest;
import com.binance.platform.mbx.model.account.CheckIfBalanceDoneInMbxByTransIdRequest;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

/**
 * An automatic conversion of userId to accountId was introduced, and a unified fuse switch was included that the service was not available。
 *
 * @author Li Fenggang
 * Date: 2020/7/13 2:49 下午
 */
public interface AccountClient {

    /**
     * 修复locked
     *
     * @param request
     * @return
     * @throws MbxException
     */
    @ApiOperation("修复locked")
    @PostMapping("/mgmt/account/asset")
    MbxResponse<Void> repairLocked(RepairLockedRequest request) throws MbxException;

    @ApiOperation("查询撮合出入金")
    @PostMapping("/mgmt/account/getBlanceLedger")
    MbxResponse<List<BalanceLedgerResponse>> getBalanceLedger(
            GetBlanceLedgerRequest request) throws MbxException;

    /**
     * Check if the transaction is done in matchbox.
     *
     * @param request
     * @return true, if it is done; false, otherwise.
     */
    boolean checkIfBalanceDoneInMbx(CheckIfBalanceDoneInMbxByTransIdRequest request);

    /**
     * 解锁API交易
     *
     * @param request
     * @return
     * @throws MbxException
     */
    @ApiOperation("解锁API交易")
    @PostMapping("/mgmt/account/unlockApiTrade")
    MbxResponse<Void> unlockApiTrade(UnLockApiTradeRequest request) throws MbxException;

    /**
     * 锁定API交易
     *
     * @param request
     * @return
     * @throws MbxException
     */
    @ApiOperation("锁定API交易")
    @PostMapping("/mgmt/account/lockApiTrade")
    MbxResponse<Void> lockApiTrade(LockApiTradeRequest request) throws MbxException;

    /**
     * 获取API签名验证
     *
     * @param request
     * @return
     * @throws MbxException
     */
    @ApiOperation("获取API签名验证")
    @PostMapping("/mgmt/getApiCheck")
    MbxResponse<ApiKeyInfoVo> getApiCheck(GetApiCheckRequest request) throws MbxException;

    /**
     * 提现获取API签名验证
     *
     * @param request
     * @return
     * @throws MbxException
     */
    @ApiOperation("提现获取API签名验证")
    @PostMapping("/mgmt/getApiCheckForWithdraw")
    MbxResponse<ApiKeyInfoVo> getApiCheckForWithdraw(GetApiCheckRequest request) throws MbxException;

    /**
     * 设置是否可以交易
     *
     * @param request
     * @return
     * @throws MbxException
     */
    @ApiOperation("设置是否可以交易")
    @PostMapping("/mgmt/account/setTrade")
    MbxResponse<Void> setTrade(SetTradeRequest request) throws MbxException;

    /**
     * 设置手续费GAS
     *
     * @param request
     * @return
     * @throws MbxException
     */
    @ApiOperation("设置手续费GAS")
    @PostMapping("/mgmt/setGas")
    MbxResponse<Void> setGas(SetGasRequest request) throws MbxException;

    @ApiOperation("")
    @PostMapping("/private/account")
    MbxResponse<TradingAccountResponse> getAccount(GetAccountRequest request)
            throws MbxException;

    @GetMapping("/private/accountFromEngineV3")
    MbxResponse<GetAccountFromEngineV3Response> getAccountFromEngineV3(GetAccountFromEngineV3Request request)
            throws MbxException;

    @ApiOperation("获取交易账户信息，返回中增加mbx v3版本的新增属性")
    @PostMapping("/private/accountV3")
    MbxResponse<TradingAccountResponseV3> getAccountV3(GetAccountRequest request)
            throws MbxException;

    @ApiOperation("获取交易账户信息，使用mbx v3接口。该接口第一次查询较慢，不建议常规用户流程使用")
    @PostMapping("/private/accountByExternalIdV3")
    MbxResponse<List<TradingAccountResponseV3>> getAccountByExternalIdV3(GetAccountRequest request)
            throws MbxException;

    @ApiOperation("")
    @PostMapping("/mgmt/account/activate")
    MbxResponse<Void> activateAccount(ActivateAccountRequest request) throws MbxException;


    /**
     * 这个操作不被支持，因为它需要更新pnk的数据库表
     *
     * @param request
     * @return
     * @throws MbxException
     */
    @ApiOperation("设置返佣比例")
    @PostMapping("/mgmt/user/agentFee")
    @Deprecated
    MbxResponse<Void> setAgentFee(SetAgentFeeRequest request) throws MbxException;

    @ApiOperation("")
    @PostMapping("/mgmt/account/saveApiRule")
    MbxResponse<Void> saveApiRule(SaveApiRuleRequest request) throws MbxException;

    @ApiOperation("")
    @PostMapping("/mgmt/account/deleteRuleByRuleId")
    MbxResponse<Void> deleteRuleByRuleId(DeleteRuleByRuleIdRequest request)
            throws MbxException;

    @ApiOperation("")
    @PostMapping("/mgmt/account/getApiInfo")
    MbxResponse<List<ApiKeyInfoVo>> getApiInfo(GetApiInfoRequest request)
            throws MbxException;
}
