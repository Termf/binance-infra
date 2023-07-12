package com.binance.master.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.*;

public final class HttpClientUtils {

    private static final String ENCODING = "UTF-8";

    private static String sendRequest(String url, HttpEntity requestEntity, Map<String, String> headerMap)
            throws Exception {
        String responseString = null;
        HttpPost httpPost = new HttpPost(url);
        CloseableHttpClient httpClient = HttpClients.createDefault();
        try {
            httpPost.setEntity(requestEntity);
            if (headerMap != null) {
                for (Map.Entry<String, String> header : headerMap.entrySet()) {
                    httpPost.addHeader(header.getKey(), header.getValue());
                }
            }
            HttpResponse httpResponse = httpClient.execute(httpPost);
            HttpEntity entity = httpResponse.getEntity();
            if (entity != null) {
                responseString = EntityUtils.toString(entity, Charset.forName(ENCODING));
                EntityUtils.consume(entity);
            }
        } catch (Exception e) {
            throw e;
        } finally {
            httpPost.releaseConnection();
            try {
                httpClient.close();
            } catch (IOException e) {
                LogUtils.error(e.getMessage(), e);
            }
        }
        return responseString;
    }

    public static String postFrom(String url, Map<String, String> paramsMap, Map<String, String> headerMap)
            throws Exception {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        if (paramsMap != null) {
            for (Map.Entry<String, String> temp : paramsMap.entrySet()) {
                params.add(new BasicNameValuePair(temp.getKey(), temp.getValue()));
            }
        }
        return sendRequest(url, new UrlEncodedFormEntity(params, Charset.forName(ENCODING)), headerMap);
    }

    public static String postFrom(String url, Map<String, String> paramsMap, Map<String, String> headerMap,
            Map<String, String> cookieMap) throws Exception {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        if (paramsMap != null) {
            for (Map.Entry<String, String> param : paramsMap.entrySet()) {
                params.add(new BasicNameValuePair(param.getKey(), param.getValue()));
            }
        }
        if (cookieMap != null) {
            if (headerMap == null) {
                headerMap = new HashMap<String, String>();
            }
            StringBuffer cookies = new StringBuffer();
            for (Map.Entry<String, String> cookie : cookieMap.entrySet()) {
                cookies.append(cookie.getKey());
                cookies.append("=");
                cookies.append(cookie.getValue());
                cookies.append(";");
            }
            headerMap.put("Cookie", cookies.toString());
        }
        return sendRequest(url, new UrlEncodedFormEntity(params, Charset.forName(ENCODING)), headerMap);
    }

    public static String postJson(String url, Object obj) throws Exception {
        return postJson(url, obj, null);
    }

    public static String postJson(String url, Object obj, Map<String, String> headerMap) throws Exception {
        String jsonString = JSON.toJSONString(obj);
        StringEntity requestEntity = new StringEntity(jsonString, Charset.forName(ENCODING));
        if (headerMap == null) {
            headerMap = new HashMap<String, String>();
        }
        headerMap.put("content-type", "application/json; encoding=utf-8");
        return sendRequest(url, requestEntity, headerMap);
    }



    public static String postJsonRpc(String url, String methodName, Object obj, Map<String, String> headerMap)
            throws Exception {
        Map<String, Object> requestMap = new HashMap<String, Object>();
        requestMap.put("jsonrpc", "2.0");
        requestMap.put("method", methodName);
        requestMap.put("params", obj);
        requestMap.put("id", 1);
        String strJson = postJson(url, requestMap, headerMap);
        JSONObject json = JSON.parseObject(strJson);
        String result = json.getString("result");
        if (StringUtils.isBlank(result)) {
            LogUtils.error("RPC error:{}", strJson);
        }
        return result;
    }

    public static String postJsonRpc(String url, String methodName, String rpcUser, String rpcPwd, Object obj)
            throws Exception {
        Map<String, String> headerMap = new HashMap<String, String>();
        if (rpcUser != null && rpcPwd != null) {
            String cred = Base64.getEncoder().encodeToString((rpcUser + ":" + rpcPwd).getBytes());
            headerMap.put("Authorization", "Basic " + cred);
        }
        return postJsonRpc(url, methodName, obj, headerMap);
    }

    public static byte[] postByte(String url, byte[] date) throws Exception {
        byte[] responseBytes = null;
        HttpPost httpPost = new HttpPost(url);
        CloseableHttpClient httpClient = HttpClients.createDefault();
        try {
            if (date != null) {
                httpPost.setEntity(new ByteArrayEntity(date, ContentType.DEFAULT_BINARY));
            }
            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            if (null != entity) {
                responseBytes = EntityUtils.toByteArray(entity);
                EntityUtils.consume(entity);
            }
        } catch (Exception e) {
            LogUtils.error(e.getMessage(), e);
            throw e;
        } finally {
            httpPost.releaseConnection();
            try {
                httpClient.close();
            } catch (IOException e) {
                LogUtils.error(e.getMessage(), e);
            }
        }
        return responseBytes;
    }

}
