package com.binance.platform.openfeign;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.util.StringUtils;

import com.binance.platform.common.UrlEncoderUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.collect.Lists;

import feign.Request;
import feign.RequestInterceptor;
import feign.RequestTemplate;

public class OpenFeignRequestInterceptor implements RequestInterceptor {

    public static final Logger logger = LoggerFactory.getLogger(OpenFeignRequestInterceptor.class);
    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public void apply(RequestTemplate template) {
        try {
            if (HttpMethod.GET.matches(template.method()) && template.requestBody().asBytes() != null) {
                try {
                    JsonNode jsonNode = objectMapper.readTree(template.requestBody().asBytes());
                    // 如果是对象才做这个这个转化,如果非对象不做任何转化
                    if (jsonNode instanceof ObjectNode) {
                        template.body(Request.Body.encoded(null, Charset.forName("UTF-8")));
                        Map<String, Collection<String>> queries = new HashMap<>(jsonNode.size());
                        buildQuery(jsonNode, "", queries);
                        template.queries(queries);
                    }
                } catch (IOException e) {
                    logger.error("build query error", e);
                }
            }
        } finally {
            // 全局进行urlencode
            if (!template.queries().equals(null)) {
                template.queries().forEach((key, value1) -> {
                    // 只要有一个值进行过urlEncode，就跳过
                    if (!UrlEncoderUtils.hasUrlEncoded(value1)) {
                        Collection<String> value =
                            value1.stream().map(str -> UrlEncoderUtils.urlEncode(str)).collect(Collectors.toList());
                        // 把原始参数去除掉
                        template.query(key, Lists.newArrayList());
                        // 添加encode后的参数
                        template.query(key, value);
                    }
                });
            }
        }
    }

    private void buildQuery(JsonNode jsonNode, String path, Map<String, Collection<String>> queries) {

        if (!jsonNode.isContainerNode()) {
            if (jsonNode.isNull()) {
                return;
            }
            Collection<String> values = queries.get(path);
            if (null == values) {
                values = new ArrayList<>();
                queries.put(path, values);
            }
            values.add(jsonNode.asText());
            return;
        }

        if (jsonNode.isArray()) {
            Iterator<JsonNode> it = jsonNode.elements();
            while (it.hasNext()) {
                buildQuery(it.next(), path, queries);
            }
        } else {
            Iterator<Map.Entry<String, JsonNode>> it = jsonNode.fields();
            while (it.hasNext()) {
                Map.Entry<String, JsonNode> entry = it.next();
                if (StringUtils.hasText(path)) {
                    buildQuery(entry.getValue(), path + "." + entry.getKey(), queries);
                } else {
                    buildQuery(entry.getValue(), entry.getKey(), queries);
                }
            }
        }
    }
}
