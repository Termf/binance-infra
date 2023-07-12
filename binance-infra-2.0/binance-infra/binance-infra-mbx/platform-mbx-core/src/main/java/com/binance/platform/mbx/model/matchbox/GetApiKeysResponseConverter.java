package com.binance.platform.mbx.model.matchbox;

import com.binance.platform.mbx.matchbox.model.mgmt.MbxApiKeysResult;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Li Fenggang
 * Date: 2020/8/5 2:58 上午
 */
public class GetApiKeysResponseConverter {
    public static GetApiKeysResponse convert(MbxApiKeysResult mbxGetApiKeysResponse) {
        GetApiKeysResponse getApiKeysResponse = new GetApiKeysResponse();
        getApiKeysResponse.setKeyId(mbxGetApiKeysResponse.getKeyId());
        getApiKeysResponse.setAccountId(mbxGetApiKeysResponse.getAccountId());
        getApiKeysResponse.setType(mbxGetApiKeysResponse.getType());
        getApiKeysResponse.setDesc(mbxGetApiKeysResponse.getDesc());
        getApiKeysResponse.setPermissions(mbxGetApiKeysResponse.getPermissions());
        List<MbxApiKeysResult.Rule> mbxRules = mbxGetApiKeysResponse.getRules();
        if (mbxRules != null) {
            List<GetApiKeysResponse.Rule> rules = new ArrayList<>(mbxRules.size());
            for (MbxApiKeysResult.Rule mbxRule : mbxRules) {
                rules.add(convert(mbxRule));
            }
            getApiKeysResponse.setRules(rules);
        } else {
            getApiKeysResponse.setRules(Collections.emptyList());
        }
        return getApiKeysResponse;

    }

    private static GetApiKeysResponse.Rule convert(MbxApiKeysResult.Rule mbxRule) {
        GetApiKeysResponse.Rule rule = new GetApiKeysResponse.Rule();
        rule.setRuleId(mbxRule.getRuleId());
        rule.setIp(mbxRule.getIp());
        rule.setEffectiveFrom(mbxRule.getEffectiveFrom());
        return rule;
    }
}
