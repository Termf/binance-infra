package com.binance.master.web;

import java.net.URI;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.net.telnet.TelnetClient;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import lombok.extern.log4j.Log4j2;

@Log4j2
public abstract class RestTemplateAbstract {

    @Resource
    protected RestTemplate restTemplate;

    /**
     * 指定url的host与port是否可以连接的通
     * 
     * @param url
     * @param msg
     * @return
     */
    protected boolean isConnect(String url, String msg) {
        try {
            if (StringUtils.isBlank(url)) {
                log.warn(msg + "{}", url);
                return false;
            }
            URI uri = URI.create(url);
            String host = uri.getHost();
            int port = uri.getPort();
            if (port == -1) {
                switch (StringUtils.lowerCase(uri.getScheme())) {
                    case "http":
                        port = 80;
                        break;
                    case "https":
                        port = 443;
                        break;
                    default:
                        break;
                }
            }
            TelnetClient telnetClient = new TelnetClient();
            telnetClient.connect(host, port);
            telnetClient.disconnect();
        } catch (Throwable e) {
            log.warn(msg + "{}:{}", url, e.getMessage());
            return false;
        }
        return true;
    }

    protected <T> T sendRequest(String url, HttpMethod method, Object body, Map<String, String> headerMap,
        Class<T> responseType) {
        try {
            MultiValueMap<String, String> headers = new LinkedMultiValueMap<String, String>();
            if (headerMap != null) {
                for (Map.Entry<String, String> header : headerMap.entrySet()) {
                    headers.add(header.getKey(), header.getValue());
                }
            }
            HttpEntity<Object> httpEntity = new HttpEntity<Object>(body, headers);
            T resp = this.restTemplate.exchange(url, method, httpEntity, responseType).getBody();
            return resp;
        } catch (RestClientResponseException e) {
            log.error("POST JSON异常Response:{}", e.getResponseBodyAsString());
            log.error("POST JSON异常:" + e.getMessage(), e);
            throw e;
        } catch (RestClientException e) {
            log.error("POST JSON异常:" + e.getMessage(), e);
            throw e;
        }
    }

    protected <T> T getRequest(String url, Class<T> responseType) {
        return this.sendRequest(url, HttpMethod.GET, null, null, responseType);
    }

    protected <T> T getRequest(String url, Class<T> responseType, Map<String, String> headerMap) {
        return this.sendRequest(url, HttpMethod.GET, null, headerMap, responseType);
    }

    protected String postFrom(String url, Map<String, Object> paramsMap, Map<String, String> headerMap)
        throws Exception {
        return this.postFrom(url, paramsMap, headerMap, String.class);
    }

    protected <T> T postFrom(String url, Map<String, Object> paramsMap, Map<String, String> headerMap,
        Class<T> responseType) throws Exception {
        if (headerMap == null) {
            headerMap = new HashMap<String, String>();
        }
        final MultiValueMap<String, Object> fromData = new LinkedMultiValueMap<String, Object>();
        if (paramsMap != null) {
            paramsMap.entrySet().forEach(e -> {
                fromData.add(e.getKey(), e.getValue());
            });
        }
        return this.sendRequest(url, HttpMethod.POST, fromData, headerMap, responseType);
    }

    protected String postFrom(String url, Map<String, Object> paramsMap) throws Exception {
        return this.postFrom(url, paramsMap, null);
    }

    protected <T> T postJson(String url, Object body, Map<String, String> headerMap, Class<T> responseType) {
        if (headerMap == null) {
            headerMap = new HashMap<String, String>();
        }
        headerMap.put(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_UTF8_VALUE);
        return this.sendRequest(url, HttpMethod.POST, body, headerMap, responseType);
    }

    protected String postJson(String url, Object body, Map<String, String> headerMap) {
        if (headerMap == null) {
            headerMap = new HashMap<String, String>();
        }
        headerMap.put(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_UTF8_VALUE);
        return this.postJson(url, body, headerMap, String.class);
    }

    protected String postJsonRpc(String url, String methodName, String rpcUser, String rpcPwd, Object obj) {
        Map<String, String> headerMap = new HashMap<String, String>();
        if (rpcUser != null && rpcPwd != null) {
            String cred = Base64.getEncoder().encodeToString((rpcUser + ":" + rpcPwd).getBytes());
            headerMap.put("Authorization", "Basic " + cred);
        }
        return postJsonRpc(url, methodName, obj, headerMap);
    }

    protected String postJsonRpc(String url, String methodName, Object obj, Map<String, String> headerMap) {
        Map<String, Object> body = new HashMap<String, Object>();
        body.put("jsonrpc", "2.0");
        body.put("method", methodName);
        body.put("params", obj);
        body.put("id", 1);
        String strJson = this.postJson(url, body, headerMap);
        JSONObject json = JSON.parseObject(strJson);
        String result = json.getString("result");
        if (StringUtils.isBlank(result)) {
            log.error("RPC error:{}", strJson);
        }
        return result;
    }

}
