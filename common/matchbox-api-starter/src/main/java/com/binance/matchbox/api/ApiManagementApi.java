package com.binance.matchbox.api;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.binance.master.constant.Constant;
import com.binance.matchbox.support.BeanConfig;
import com.binance.matchbox.vo.ApiKeyDetail;
import com.binance.matchbox.vo.CreateApiKeyResponse;

import feign.Param;

/**
 * Created by Fei.Huang on 2018/5/30.
 */
@FeignClient(name = Constant.MATCHBOX_WEB_SERVICE, url = "${com.binance.matchbox.api.url}",
    configuration = BeanConfig.class)
public interface ApiManagementApi {

    /**
     * 删除所有API
     * 
     * @param tradingAccountId
     * @param keyId
     * @return
     */
    @DeleteMapping(value = "/v1/apiKey")
    Void deleteApiKeyByKeyId(@RequestParam("accountId") Long tradingAccountId, @RequestParam("keyId") String keyId);

    /**
     * 创建API key
     */
    @PostMapping("/v1/apiKey")
    CreateApiKeyResponse createApiKey(@RequestParam("accountId") Long tradingAccountId,
        @RequestParam("desc") String desc, @RequestParam("force") boolean force);

    /**
     * apiKey增加规则
     */
    @PostMapping("/v1/apiKey/rule")
    Object addApiRule(@RequestParam("accountId") Long tradingAccountId, @RequestParam("keyId") Long keyId,
        @RequestParam("ip") String ip);

    /**
     * 更新api key的权限
     */
    @PutMapping("/v1/apiKey/permissions")
    Object updatePermission(@Param("accountId") Long accountId, @RequestParam("keyId") Long keyId,
        @RequestParam("canTrade") boolean canTrade, @RequestParam("canViewUserData") boolean canViewUserData,
        @RequestParam("canControlUserStreams") boolean canControlUserStreams,
        @RequestParam("canViewMarketData") boolean canViewMarketData,
        @RequestParam("canAccessSecureWs") boolean canAccessSecureWs);

    /**
     * 查询所有的api key详情
     */
    @GetMapping("/v1/apiKeys")
    List<ApiKeyDetail> getApiKeys(@Param("accountId") Long accountId);
}
