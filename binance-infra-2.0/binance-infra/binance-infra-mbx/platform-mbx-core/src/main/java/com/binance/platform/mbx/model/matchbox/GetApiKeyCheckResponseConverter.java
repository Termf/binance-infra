package com.binance.platform.mbx.model.matchbox;

import com.binance.platform.mbx.matchbox.model.mgmt.MbxGetApiKeyCheckResult;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Li Fenggang
 * Date: 2020/7/19 9:19 上午
 */
public class GetApiKeyCheckResponseConverter {

    public static GetApiKeyCheckResponse convert(MbxGetApiKeyCheckResult source) {
        GetApiKeyCheckResponse getApiKeyCheckResponse = new GetApiKeyCheckResponse();
        getApiKeyCheckResponse.setKeyId(source.getKeyId());
        getApiKeyCheckResponse.setAccountId(source.getAccountId());
        getApiKeyCheckResponse.setType(source.getType());
        getApiKeyCheckResponse.setPermissions(source.getPermissions());
        List<GetApiKeyCheckResponse.Rule> targetRules = Collections.emptyList();
        List<MbxGetApiKeyCheckResult.Rule> rules = source.getRules();
        if (rules != null && !rules.isEmpty()) {
            targetRules = new ArrayList<>();
            for (MbxGetApiKeyCheckResult.Rule rule : rules) {
                GetApiKeyCheckResponse.Rule targetRule = convert(rule);
                targetRules.add(targetRule);
            }
        }
        getApiKeyCheckResponse.setRules(targetRules);
        return getApiKeyCheckResponse;

    }

    public static GetApiKeyCheckResponse.Rule convert(MbxGetApiKeyCheckResult.Rule source) {
        GetApiKeyCheckResponse.Rule rule = new GetApiKeyCheckResponse.Rule();
        rule.setRuleId(source.getRuleId());
        rule.setIp(source.getIp());
        return rule;

    }
}
