package com.binance.platform.mbx.client.impl;

import com.binance.master.error.GeneralCode;
import com.binance.platform.mbx.client.AccountClient;
import com.binance.platform.mbx.config.SysConfigService;
import com.binance.platform.mbx.exception.MbxException;
import com.binance.platform.mbx.matchbox.MatchBoxManagementService;
import com.binance.platform.mbx.matchbox.model.MbxResponse;
import com.binance.platform.mbx.matchbox.model.MbxState;
import com.binance.platform.mbx.matchbox.model.mgmt.MbxApiKeysRequest;
import com.binance.platform.mbx.matchbox.model.mgmt.MbxApiKeysResult;
import com.binance.platform.mbx.matchbox.model.mgmt.MbxDeleteApiKeyLockRequest;
import com.binance.platform.mbx.matchbox.model.mgmt.MbxDeleteApiKeyRuleRequest;
import com.binance.platform.mbx.matchbox.model.mgmt.MbxDeleteGasOptOutRequest;
import com.binance.platform.mbx.matchbox.model.mgmt.MbxGetAccountByExternalAccountIdV3Request;
import com.binance.platform.mbx.matchbox.model.mgmt.MbxGetAccountFromEngineV3Request;
import com.binance.platform.mbx.matchbox.model.mgmt.MbxGetAccountFromEngineV3Response;
import com.binance.platform.mbx.matchbox.model.mgmt.MbxGetAccountRequest;
import com.binance.platform.mbx.matchbox.model.mgmt.MbxGetAccountResult;
import com.binance.platform.mbx.matchbox.model.mgmt.MbxGetApiKeyCheckRequest;
import com.binance.platform.mbx.matchbox.model.mgmt.MbxGetApiKeyCheckRequestBuilder;
import com.binance.platform.mbx.matchbox.model.mgmt.MbxGetApiKeyCheckResult;
import com.binance.platform.mbx.matchbox.model.mgmt.MbxGetBalanceLedgerRequest;
import com.binance.platform.mbx.matchbox.model.mgmt.MbxGetBalanceLedgerResponse;
import com.binance.platform.mbx.matchbox.model.mgmt.MbxPostApiKeyRuleRequest;
import com.binance.platform.mbx.matchbox.model.mgmt.MbxPutAccountAssetRequest;
import com.binance.platform.mbx.matchbox.model.mgmt.MbxPutAccountRequest;
import com.binance.platform.mbx.matchbox.model.mgmt.MbxPutApiKeyLockRequest;
import com.binance.platform.mbx.matchbox.model.mgmt.MbxPutGasOptOutRequest;
import com.binance.platform.mbx.model.account.ActivateAccountRequest;
import com.binance.platform.mbx.model.account.ApiKeyInfoVo;
import com.binance.platform.mbx.model.account.BalanceLedgerResponse;
import com.binance.platform.mbx.model.account.CheckIfBalanceDoneInMbxByTransIdRequest;
import com.binance.platform.mbx.model.account.DeleteRuleByRuleIdRequest;
import com.binance.platform.mbx.model.account.GetAccountFromEngineV3Converter;
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
import com.binance.platform.mbx.service.AccountService;
import com.binance.platform.mbx.util.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * @author Li Fenggang
 * Date: 2020/7/13 3:42 下午
 */
public class AccountClientImpl implements AccountClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(AccountClientImpl.class);
    private final MatchBoxManagementService matchBoxManagementService;
    private final AccountService accountService;
    private final SysConfigService sysConfigService;

    public AccountClientImpl(MatchBoxManagementService matchBoxManagementService, AccountService accountService, SysConfigService sysConfigService) {
        this.matchBoxManagementService = matchBoxManagementService;
        this.accountService = accountService;
        this.sysConfigService = sysConfigService;
    }

    @Override
    public MbxResponse<Void> repairLocked(RepairLockedRequest request) throws MbxException {

        Long tradingAccountId = accountService.retrieveTradingAccountIdByUserId(request.getUserId());

        MbxPutAccountAssetRequest mbxRequest = new MbxPutAccountAssetRequest(tradingAccountId, request.getAsset(),
                "SPOT", new BigDecimal(0), request.getAvailable());
        MbxResponse<Void> mbxResponse = matchBoxManagementService.putAccountAsset(mbxRequest);

        return mbxResponse;
    }

    @Override
    public MbxResponse<List<BalanceLedgerResponse>> getBalanceLedger(GetBlanceLedgerRequest request) throws MbxException {
        Long tradingAccountId = accountService.retrieveTradingAccountIdByUserId(request.getUserId());

        MbxGetBalanceLedgerRequest mbxRequest = new MbxGetBalanceLedgerRequest(tradingAccountId, request.getAsset());
        mbxRequest.setStartTime(DateUtil.getTime(request.getStartTime()));
        mbxRequest.setEndTime(DateUtil.getTime(request.getEndTime()));
        mbxRequest.setFromExternalUpdateId(request.getFromExternalUpdateId());
        mbxRequest.setToExternalUpdateId(request.getToExternalUpdateId());

        MbxResponse<List<MbxGetBalanceLedgerResponse>> mbxResponse = matchBoxManagementService.getBalanceLedger(mbxRequest);
        MbxResponse<List<BalanceLedgerResponse>> response = new MbxResponse<>();
        response.setState(mbxResponse.getState());
        if (response.isSuccess()) {
            List<MbxGetBalanceLedgerResponse> list = mbxResponse.getData();
            if (list == null && list.isEmpty()) {
                response.setData(Collections.emptyList());
            } else {
                List<BalanceLedgerResponse> targetList = new ArrayList<>(list.size());
                response.setData(targetList);
                for (MbxGetBalanceLedgerResponse item : list) {
                    BalanceLedgerResponse targetItem = new BalanceLedgerResponse();
                    targetItem.setAccountId(item.getAccountId());
                    targetItem.setAsset(item.getAsset());
                    targetItem.setBalanceDelta(item.getBalanceDelta());
                    targetItem.setTime(item.getTime());
                    targetItem.setUpdateId(item.getUpdateId());
                    targetItem.setExternalUpdateId(item.getExternalUpdateId());
                    targetItem.setMbxUpdateId(item.getMbxUpdateId());

                    targetList.add(targetItem);
                }
            }
        }
        return response;
    }

    @Override
    public boolean checkIfBalanceDoneInMbx(CheckIfBalanceDoneInMbxByTransIdRequest request) {
        Long tradingAccountId = accountService.retrieveTradingAccountIdByUserId(request.getUserId());
        MbxGetBalanceLedgerRequest mbxRequest = new MbxGetBalanceLedgerRequest(tradingAccountId, request.getAsset());
        mbxRequest.setFromExternalUpdateId(request.getTransId());
        mbxRequest.setToExternalUpdateId(request.getTransId());

        MbxResponse<List<MbxGetBalanceLedgerResponse>> mbxResponse = matchBoxManagementService.getBalanceLedger(mbxRequest);
        if (!mbxResponse.isSuccess()) {
            MbxState state = mbxResponse.getState();
            throw new MbxException(String.valueOf(state.getCode()), state.getMsg());
        }
        List<MbxGetBalanceLedgerResponse> dataList = mbxResponse.getData();
        for (MbxGetBalanceLedgerResponse item : dataList) {
            if (Objects.equals(item.getAccountId(), tradingAccountId)
            && Objects.equals(item.getAsset(), request.getAsset())
            && Objects.equals(item.getExternalUpdateId(), request.getTransId())
            && Objects.nonNull(item.getMbxUpdateId())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public MbxResponse<Void> unlockApiTrade(UnLockApiTradeRequest request) throws MbxException {
        Long tradingAccountId = accountService.retrieveTradingAccountIdByUserId(request.getUserId());

        MbxDeleteApiKeyLockRequest mbxRequest = new MbxDeleteApiKeyLockRequest(tradingAccountId, true);
        MbxResponse<Void> mbxResponse = matchBoxManagementService.deleteApiKeyLock(mbxRequest);
        return mbxResponse;
    }

    @Override
    public MbxResponse<Void> lockApiTrade(LockApiTradeRequest request) throws MbxException {
        Long tradingAccountId = accountService.retrieveTradingAccountIdByUserId(request.getUserId());

        MbxPutApiKeyLockRequest mbxRequest = new MbxPutApiKeyLockRequest(tradingAccountId);
        MbxResponse<Void> mbxResponse = matchBoxManagementService.putApiKeyLock(mbxRequest);
        return mbxResponse;
    }

    @Override
    public MbxResponse<ApiKeyInfoVo> getApiCheck(GetApiCheckRequest request) throws MbxException {
        sysConfigService.checkSystemMaintenance();

        MbxResponse<ApiKeyInfoVo> response = getApiKeyInfoVoMbxResponse(request);
        return response;
    }

    private MbxResponse<ApiKeyInfoVo> getApiKeyInfoVoMbxResponse(GetApiCheckRequest request) {
        // check account exist
        accountService.retrieveTradingAccountIdByUserId(request.getUserId());

        MbxGetApiKeyCheckRequest mbxRequest = MbxGetApiKeyCheckRequestBuilder.build(request.getIp(),
                request.getPayload(), request.getRecvWindow(), request.getTimestamp(),
                request.getSignature(), request.getApiKey());
                new MbxGetApiKeyCheckRequest(request.getIp());

        MbxResponse<MbxGetApiKeyCheckResult> mbxResponse = matchBoxManagementService.getApiKeyCheck(mbxRequest);
        MbxResponse<ApiKeyInfoVo> response = new MbxResponse<ApiKeyInfoVo>();
        response.setState(mbxResponse.getState());
        if (response.isSuccess()) {
            MbxGetApiKeyCheckResult mbxData = mbxResponse.getData();
            ApiKeyInfoVo apiKeyInfoVo = new ApiKeyInfoVo();
            response.setData(apiKeyInfoVo);

            apiKeyInfoVo.setKeyId(mbxData.getKeyId());
            apiKeyInfoVo.setAccountId(mbxData.getAccountId());
            apiKeyInfoVo.setDesc(mbxData.getDesc());
            apiKeyInfoVo.setPermissions(mbxData.getPermissions());
            List<MbxGetApiKeyCheckResult.Rule> rulesList = mbxData.getRules();
            List<ApiKeyInfoVo.Rule> targetRuleList = Collections.emptyList();
            if (rulesList != null && !rulesList.isEmpty()) {
                targetRuleList = new ArrayList<>(rulesList.size());
                for (MbxGetApiKeyCheckResult.Rule rule : rulesList) {
                    ApiKeyInfoVo.Rule targetRule = new ApiKeyInfoVo.Rule();
                    targetRule.setRuleId(rule.getRuleId());
                    targetRule.setIp(rule.getIp());
                    targetRule.setEffectiveFrom(rule.getEffectiveFrom());
                    targetRuleList.add(targetRule);
                }
            }
            apiKeyInfoVo.setRules(targetRuleList);
        }
        return response;
    }

    @Override
    public MbxResponse<ApiKeyInfoVo> getApiCheckForWithdraw(GetApiCheckRequest request) throws MbxException {
        sysConfigService.checkSystemMaintenance();

        MbxResponse<ApiKeyInfoVo> response = getApiKeyInfoVoMbxResponse(request);
        return response;
    }

    @Override
    public MbxResponse<Void> setTrade(SetTradeRequest request) throws MbxException {
        Long tradingAccountId = accountService.retrieveTradingAccountIdByUserId(request.getUserId());

        MbxPutAccountRequest mbxRequest = new MbxPutAccountRequest(tradingAccountId, request.getCanTrade(),
                true, true);
        MbxResponse<Void> mbxResponse = matchBoxManagementService.putAccount(mbxRequest);
        return mbxResponse;
    }

    @Override
    public MbxResponse<Void> setGas(SetGasRequest request) throws MbxException {
        Long tradingAccountId = accountService.retrieveTradingAccountIdByUserId(request.getUserId());

        boolean useBnbFee = request.isUseBnbFee();
        if (useBnbFee) {
            // 自动
            MbxDeleteGasOptOutRequest mbxRequest = new MbxDeleteGasOptOutRequest(tradingAccountId);
            MbxResponse<Void> mbxResponse = matchBoxManagementService.deleteGasOptOut(mbxRequest);
            return mbxResponse;
        }
        // 锁定（设置例外）
        MbxPutGasOptOutRequest mbxRequest = new MbxPutGasOptOutRequest(tradingAccountId);
        MbxResponse<Void> mbxResponse = matchBoxManagementService.putGasOptOut(mbxRequest);
        return mbxResponse;
    }

    @Override
    public MbxResponse<TradingAccountResponse> getAccount(GetAccountRequest request) throws MbxException {
        Long tradingAccountId = accountService.retrieveTradingAccountIdByUserId(request.getUserId());

        MbxGetAccountRequest mbxRequest = new MbxGetAccountRequest(tradingAccountId);
        MbxResponse<MbxGetAccountResult> mbxResponse = matchBoxManagementService.getAccount(mbxRequest);
        MbxResponse<TradingAccountResponse> response = new MbxResponse<TradingAccountResponse>();
        response.setState(mbxResponse.getState());
        if (response.isSuccess()) {
            MbxGetAccountResult data = mbxResponse.getData();
            TradingAccountResponse targetDate = convertToTargetAccountDetail(data);
            response.setData(targetDate);
        }
        return response;
    }

    @Override
    public MbxResponse<GetAccountFromEngineV3Response> getAccountFromEngineV3(GetAccountFromEngineV3Request request) throws MbxException {
        Long tradingAccountId = accountService.retrieveTradingAccountIdByUserId(request.getUserId());

        MbxGetAccountFromEngineV3Request mbxRequest = new MbxGetAccountFromEngineV3Request(tradingAccountId);
        MbxResponse<MbxGetAccountFromEngineV3Response> mbxResponse = matchBoxManagementService.getAccountFromEngineV3(mbxRequest);
        MbxResponse<GetAccountFromEngineV3Response> response = new MbxResponse<GetAccountFromEngineV3Response>();
        response.setState(mbxResponse.getState());
        if (response.isSuccess()) {
            MbxGetAccountFromEngineV3Response data = mbxResponse.getData();
            GetAccountFromEngineV3Response targetDate = GetAccountFromEngineV3Converter.convertResponse(data);
            response.setData(targetDate);
        }
        return response;
    }

    private TradingAccountResponse convertToTargetAccountDetail(MbxGetAccountResult data) {
        TradingAccountResponse targetData = new TradingAccountResponse();

        targetData.setMakerCommission(data.getMakerCommission());
        targetData.setTakerCommission(data.getTakerCommission());
        targetData.setBuyerCommission(data.getBuyerCommission());
        targetData.setSellerCommission(data.getSellerCommission());
        targetData.setCanTrade(data.isCanTrade());
        targetData.setCanWithdraw(data.isCanWithdraw());
        targetData.setCanDeposit(data.isCanDeposit());
        targetData.setAccountId(data.getAccountId());
        targetData.setExternalId(data.getExternalId());
        targetData.setUpdateId(data.getUpdateId());
        Date updateTime = new Date();
        updateTime.setTime(data.getUpdateTime());
        targetData.setUpdateTime(updateTime);
        List<TradingAccountResponse.Balance> targetBalanceList = Collections.emptyList();
        List<MbxGetAccountResult.Balance> balanceList = data.getBalances();
        if (balanceList != null && !balanceList.isEmpty()) {
            targetBalanceList = new ArrayList<>(balanceList.size());
            for (MbxGetAccountResult.Balance balance : balanceList) {
                TradingAccountResponse.Balance targetBalance = new TradingAccountResponse.Balance();
                targetBalanceList.add(targetBalance);

                targetBalance.setAsset(balance.getAsset());
                targetBalance.setFree(balance.getFree());
                targetBalance.setLocked(balance.getLocked());
                targetBalance.setExtLocked(balance.getExtLocked());
            }
        }

        targetData.setBalances(targetBalanceList);
        return targetData;
    }

    @Override
    public MbxResponse<TradingAccountResponseV3> getAccountV3(GetAccountRequest request) throws MbxException {
        Long tradingAccountId = accountService.retrieveTradingAccountIdByUserId(request.getUserId());

        MbxGetAccountRequest mbxRequest = new MbxGetAccountRequest(tradingAccountId);
        MbxResponse<MbxGetAccountResult> mbxResponse = matchBoxManagementService.getAccount(mbxRequest);
        MbxResponse<TradingAccountResponseV3> response = new MbxResponse<TradingAccountResponseV3>();
        response.setState(mbxResponse.getState());
        if (response.isSuccess()) {
            MbxGetAccountResult data = mbxResponse.getData();
            TradingAccountResponseV3 targetDate = convertToTargetAccountDetailV3(data);
            response.setData(targetDate);
        }
        return response;
    }

    private TradingAccountResponseV3 convertToTargetAccountDetailV3(MbxGetAccountResult data) {
        TradingAccountResponseV3 targetData = new TradingAccountResponseV3();
        targetData.setMakerCommission(data.getMakerCommission());
        targetData.setTakerCommission(data.getTakerCommission());
        targetData.setBuyerCommission(data.getBuyerCommission());
        targetData.setSellerCommission(data.getSellerCommission());
        targetData.setCanTrade(data.isCanTrade());
        targetData.setCanWithdraw(data.isCanWithdraw());
        targetData.setCanDeposit(data.isCanDeposit());
        targetData.setAccountId(data.getAccountId());
        targetData.setExternalId(data.getExternalId());
        targetData.setExternalAccountId(data.getExternalAccountId());
        targetData.setUpdateId(data.getUpdateId());
        Date updateTime = new Date();
        updateTime.setTime(data.getUpdateTime());
        targetData.setUpdateTime(updateTime);
        targetData.setAccountType(data.getAccountType());
        List<TradingAccountResponseV3.Balance> targetBalanceList = Collections.emptyList();
        List<MbxGetAccountResult.Balance> balanceList = data.getBalances();
        if (balanceList != null && !balanceList.isEmpty()) {
            targetBalanceList = new ArrayList<>(balanceList.size());
            for (MbxGetAccountResult.Balance balance : balanceList) {
                TradingAccountResponseV3.Balance targetBalance = new TradingAccountResponseV3.Balance();
                targetBalanceList.add(targetBalance);

                targetBalance.setAsset(balance.getAsset());
                targetBalance.setFree(balance.getFree());
                targetBalance.setLocked(balance.getLocked());
                targetBalance.setExtLocked(balance.getExtLocked());
            }
        }
        targetData.setBalances(targetBalanceList);
        MbxGetAccountResult.Restrictions restrictions = data.getRestrictions();
        if (restrictions != null) {
            TradingAccountResponseV3.Restriction targetRestrictions = new TradingAccountResponseV3.Restriction();
            targetData.setRestrictions(targetRestrictions);

            targetRestrictions.setRestrictionId(restrictions.getRestrictionId());
            targetRestrictions.setMode(restrictions.getMode());
            targetRestrictions.setSymbols(restrictions.getSymbols());
        }
        targetData.setPermissions(data.getPermissions());

        return targetData;
    }

    @Override
    public MbxResponse<List<TradingAccountResponseV3>> getAccountByExternalIdV3(GetAccountRequest request) throws MbxException {
        MbxGetAccountByExternalAccountIdV3Request mbxRequest = new MbxGetAccountByExternalAccountIdV3Request(request.getUserId());
        MbxResponse<List<MbxGetAccountResult>> mbxResponse = matchBoxManagementService.getAccountByExternalAccountIdV3(mbxRequest);
        MbxResponse<List<TradingAccountResponseV3>> response = new MbxResponse<List<TradingAccountResponseV3>>();
        response.setState(mbxResponse.getState());
        if (response.isSuccess()) {
            List<MbxGetAccountResult> list = mbxResponse.getData();
            List<TradingAccountResponseV3> targetList = Collections.emptyList();
            if (list != null && !list.isEmpty()) {
                targetList = new ArrayList<>(list.size());
                for (MbxGetAccountResult mbxGetAccountResult : list) {

                    TradingAccountResponseV3 targetItem = convertToTargetAccountDetailV3(mbxGetAccountResult);
                    targetList.add(targetItem);
                }
            }
            response.setData(targetList);
        }

        return response;
    }

    @Override
    public MbxResponse<Void> activateAccount(ActivateAccountRequest request) throws MbxException {
        Long tradingAccountId = accountService.retrieveTradingAccountIdByUserId(request.getUserId());

        MbxPutAccountRequest mbxRequest = new MbxPutAccountRequest(tradingAccountId, request.isCanTrade(),
                request.isCanWithdraw(), request.isCanDeposit());
        MbxResponse<Void> mbxResponse = matchBoxManagementService.putAccount(mbxRequest);
        return mbxResponse;
    }

    @Override
    public MbxResponse<Void> setAgentFee(SetAgentFeeRequest request) throws MbxException {
        // TODO DB
        throw new MbxException(GeneralCode.SYS_NOT_SUPPORT);
    }

    @Override
    public MbxResponse<Void> saveApiRule(SaveApiRuleRequest request) throws MbxException {
        sysConfigService.checkSystemMaintenance();

        Long tradingAccountId = accountService.retrieveTradingAccountIdByUserId(request.getUserId());

        MbxPostApiKeyRuleRequest mbxRequest = new MbxPostApiKeyRuleRequest(request.getKeyId(), tradingAccountId, request.getIp());
        MbxResponse<Void> mbxResponse = matchBoxManagementService.postApiKeyRule(mbxRequest);

        return mbxResponse;
    }

    @Override
    public MbxResponse<Void> deleteRuleByRuleId(DeleteRuleByRuleIdRequest request) throws MbxException {
        sysConfigService.checkSystemMaintenance();

        Long tradingAccountId = accountService.retrieveTradingAccountIdByUserId(request.getUserId());

        MbxDeleteApiKeyRuleRequest mbxRequest = new MbxDeleteApiKeyRuleRequest(request.getRuleId(), request.getKeyId(), tradingAccountId);
        MbxResponse<Void> mbxResponse = matchBoxManagementService.deleteApiKeyRule(mbxRequest);

        return mbxResponse;
    }

    @Override
    public MbxResponse<List<ApiKeyInfoVo>> getApiInfo(GetApiInfoRequest request) throws MbxException {
        sysConfigService.checkSystemMaintenance();

        Long tradingAccountId = accountService.retrieveTradingAccountIdByUserId(request.getUserId());

        MbxApiKeysRequest mbxRequest = new MbxApiKeysRequest(tradingAccountId);
        MbxResponse<List<MbxApiKeysResult>> mbxResponse = matchBoxManagementService.getApiKeys(mbxRequest);
        MbxResponse<List<ApiKeyInfoVo>> response = new MbxResponse<List<ApiKeyInfoVo>>();
        response.setState(mbxResponse.getState());
        if (response.isSuccess()) {
            List<MbxApiKeysResult> data = mbxResponse.getData();
            List<ApiKeyInfoVo> targetData = Collections.emptyList();
            if (data != null && !data.isEmpty()) {
                targetData = new ArrayList<>(data.size());
                response.setData(targetData);
                for (MbxApiKeysResult item : data) {
                    ApiKeyInfoVo targetItem = new ApiKeyInfoVo();
                    targetItem.setKeyId(item.getKeyId());
                    targetItem.setAccountId(item.getAccountId());
                    targetItem.setDesc(item.getDesc());
                    targetItem.setPermissions(item.getPermissions());
                    List<MbxApiKeysResult.Rule> rules = item.getRules();
                    List<ApiKeyInfoVo.Rule> targetRules = Collections.emptyList();
                    if (rules != null && !rules.isEmpty()) {
                        targetRules = new ArrayList<>(rules.size());
                        for (MbxApiKeysResult.Rule rule : rules) {
                            ApiKeyInfoVo.Rule targetRule = new ApiKeyInfoVo.Rule();
                            targetRules.add(targetRule);

                            targetRule.setRuleId(rule.getRuleId());
                            targetRule.setIp(rule.getIp());
                            targetRule.setEffectiveFrom(rule.getEffectiveFrom());
                        }
                    }
                    targetItem.setRules(targetRules);

                    targetData.add(targetItem);
                }
            }
        }
        return response;
    }

}
