package com.binance.platform.mbx.model;

import com.binance.platform.mbx.matchbox.model.mgmt.MbxApiKeysRequest;
import com.binance.platform.mbx.matchbox.model.mgmt.MbxApiKeysResult;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Li Fenggang
 * Date: 2020/7/2 11:23 上午
 */
public class ApiKeysRequestTest {
//    @Test
    public void toStringTest() {
        MbxApiKeysRequest request = new MbxApiKeysRequest(10);
        MbxApiKeysResult response = new MbxApiKeysResult();
        response.setAccountId(10L);
        response.setDesc("desc");
        response.setKeyId(11L);
        response.setType("type-value");
        List<String> permissions = Arrays.asList("perm1", "perm2");
        response.setPermissions(permissions);
        List<MbxApiKeysResult.Rule> rules = new ArrayList<>();
        MbxApiKeysResult.Rule rule = new MbxApiKeysResult.Rule();
        rule.setIp("127.0.0.1");
        rule.setEffectiveFrom(17433L);
        rule.setRuleId(18L);
        rules.add(rule);
        response.setRules(rules);

        System.out.println(request.toString());
        System.out.println(response.toString());

        Assert.assertEquals("请求对象序列化错误", "{\"accountId\":10,\"uri\":\"/v1/apiKeys\"}", request.toString());
        Assert.assertEquals("响应对象序列化错误", "{\"keyId\":11,\"accountId\":10,\"type\":\"type-value\",\"desc\":\"desc\",\"permissions\":[\"perm1\",\"perm2\"],\"rules\":[{\"ruleId\":18,\"ip\":\"127.0.0.1\",\"effectiveFrom\":17433}]}", response.toString());
    }
}
