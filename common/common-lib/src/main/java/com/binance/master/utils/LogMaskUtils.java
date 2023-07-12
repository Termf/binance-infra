package com.binance.master.utils;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.serializer.ValueFilter;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import com.fasterxml.jackson.databind.node.TextNode;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import lombok.extern.slf4j.Slf4j;

/**
 * Created by Fei.Huang on 2018/7/9.
 */
@Slf4j
public final class LogMaskUtils {

    private static final String[] DEFAULT_MASK_KEYS = {"salt", "password", "passwordConfirm", "secretKey",
            "registerToken", "newPassword", "oldPassword", "token", "antiPhishingCode", "serialNo", "disableToken",
            "csrfToken", "serialNoValue", "secretKey", "cookie", "code", "forbiddenLink", "link", "recipient", "mobile", "authKey", "device_info",
            "deviceInfo", "mobileCode", "mobileNum", "mobile_code", "tableAntiPhishing", "antiCode", "verifyCode", "verify_code", "emailVerifyCode",
            "smsCode", "googleCode", "apiKey", "appKey", "authorization", "signature", "apiSecret", "contrastImage", "sourceImage", "apiSecret",
            "accessToken", "refreshToken", "parentCode", "apikey", "taxId", "deviceInfo", "cardNumber","cardNum", "cvv", "lastName", "firstName", "expiryMonth",
            "expiryYear","safePassword","confirmSafePassword","mobileVerifyCode", "googleVerifyCode", "emailVerifyCode", "yubikeyVerifyCode", "payAccount", "phone"};

    private static final Set<String> defaultMaskKeys = new HashSet<>(Arrays.asList(DEFAULT_MASK_KEYS));

    /**
     * customized mask keys and whether to hash the value.
     */
    private static Map<String, Boolean> maskKeySet = new ConcurrentHashMap<>();

    /**
     * add additional mask keys that defined by users.
     *
     * @param additionalMaskKeys
     */
    public static void init(List<String> additionalMaskKeys) {
        if (additionalMaskKeys != null) {
            for (String key : additionalMaskKeys) {
                initWithKey(key);
            }
        }
    }

    private static void initWithKey(String key) {
        if (key.length() > 1 && key.endsWith("#")) {
            key = key.substring(0, key.length() - 1);
            maskKeySet.put(key, Boolean.TRUE);
        } else {
            maskKeySet.put(key, Boolean.FALSE);
        }
    }


    /**
     * 按需日志掩码
     * Note: 该方法性能很低, 建议使用maskJsonString2和getMaskJson
     *
     * @param jsonString JsonString
     * @param maskKeys   自定义需要进行掩码的Key (DEFAULT_MASK_KEYS为默认进行掩码的Key)
     * @return
     */
    @Deprecated
    public static String maskJsonString(String jsonString, String... maskKeys) {
        return maskJsonString2(jsonString, maskKeys);
    }

    private static JSONObject parseJsonObject(String jsonString) {
        try {
            return JSONObject.parseObject(jsonString);
        } catch (Exception e) {
            return null;
        }
    }

    private static JSONArray parseJsonArray(String jsonString) {
        try {
            return JSONArray.parseArray(jsonString);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * filter to process the json object filed value to mask strings.
     */
    static class MaskValueFilter implements ValueFilter {

        public MaskValueFilter(String... maskKeys) {
            if (maskKeys.length > 0) {
                for (String key : maskKeys) {
                    initWithKey(key);
                }
            }
        }

        @Override
        public Object process(Object object, String name, Object value) {
            Boolean hashMaskFlag = maskKeySet.get(name);
            if (Boolean.TRUE.equals(hashMaskFlag)) {
                return value == null ? "null" : "##>" + Md5Tools.MD5(value.toString());
            } else if (hashMaskFlag != null) {
                return "***";
            }
            if (defaultMaskKeys.contains(name)) {
                return "******";
            }
            return value;
        }
    }

    /**
     *
     * @param obj
     * @param maskKeys
     * @return
     */
    @Deprecated
    public static String maskJsonObject(Object obj, String... maskKeys) {
        if (obj == null) {
            return null;
        }
        ValueFilter valueFilter = new MaskValueFilter(maskKeys);
        try {
            Object jsonObject = JSON.toJSON(obj);
            if (jsonObject instanceof JSONObject) {
                return getMaskJson((JSONObject) jsonObject, valueFilter);
            } else {
                log.warn("cannot convert to JSONObject. class: {}", obj.getClass().getCanonicalName());
            }
        } catch (Exception e) {
            log.error("get mask json error", e);

        }
        return "";
    }


    /**
     * optimised method to mask the specified <code>jsonString</code> and <code>maskKeys</code>. Note:
     * please use fastMaskJsonStr instead.
     *
     * @param jsonString
     * @param maskKeys
     * @return
     */
    @Deprecated
    public static String maskJsonString2(String jsonString, String... maskKeys) {
        ValueFilter valueFilter = new MaskValueFilter(maskKeys);
        try {
            JSONObject jsonObject = parseJsonObject(jsonString);
            if (jsonObject != null) {
                return getMaskJson(jsonObject, valueFilter);
            }
            JSONArray jsonArray = parseJsonArray(jsonString);
            if (jsonArray == null) {
                return "";
            } else {
                return getMaskJson(jsonArray, valueFilter);
            }
        } catch (Exception e) {
            log.error("get mask json error", e);
            return "";
        }
    }

    // serialize with jackson
    private static ObjectMapper objectMapper = new ObjectMapper();
    static {
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        objectMapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        objectMapper.configure(JsonGenerator.Feature.WRITE_NUMBERS_AS_STRINGS, false);
        objectMapper.configure(JsonParser.Feature.ALLOW_BACKSLASH_ESCAPING_ANY_CHARACTER, true);
        objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);

        SensitiveInfoModule module = new SensitiveInfoModule();
        module.addSerializer(String.class, new AutoMaskSerializer(String.class));
        module.addSerializer(Integer.class, new AutoMaskSerializer(Integer.class));
        module.addSerializer(Long.class, new AutoMaskSerializer(Long.class));
        objectMapper.registerModule(module);
    }

    static class SensitiveInfoModule extends SimpleModule {
        private static final long serialVersionUID = 1L;

        @Override
        public Object getTypeId() {
            return SensitiveInfoModule.class;
        }
    }

    static class AutoMaskSerializer<T> extends StdSerializer<T> {
        private JsonSerializer base;

        protected AutoMaskSerializer(Class<T> t) {
            super(t);
            if (Integer.class.isAssignableFrom(t)) {
                base = new NumberSerializers.IntegerSerializer(t);
            } else if (Long.class.isAssignableFrom(t)) {
                base = new NumberSerializers.LongSerializer(t);
            }
        }

        @Override
        public void serialize(T value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            String name = gen.getOutputContext().getCurrentName();
            if (name == null) {
                return;
            }
            String result = getMaskedStr(value != null ? value.toString() : null, name);
            if (result != null) {
                gen.writeString(result);
            } else if (base != null) {
                base.serialize(value, gen, serializers);
            } else {
                gen.writeString(value != null ? value.toString() : "null");
            }
        }
    }

    private static String getMaskedStr(String value, String name) {
        Boolean hashMaskFlag = maskKeySet.get(name);
        String result = null;
        if (Boolean.TRUE.equals(hashMaskFlag) && value != null) {
            result = value == null ? "null" : "##>" + Md5Tools.MD5(value);
        } else if (hashMaskFlag != null) {
            result = "***";
        }
        if (defaultMaskKeys.contains(name)) {
            result = "******";
        }
        return result;
    }


    public static String fastMaskObject(Object obj) {
        if (obj == null) {
            return null;
        }
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (IOException e) {
            log.error("fail to mask json object", e);
        }
        return "{}";
    }

    /**
     *
     * @param jsonStr
     * @return
     */
    public static String fastMaskJsonStr(String jsonStr) {
        if (StringUtils.isNotBlank(jsonStr)) {
            try {
                JsonNode node = objectMapper.readTree(jsonStr);
                if (node == null) {
                    log.warn("cannot parse from json string");
                    return jsonStr;
                }
                doMaskJsonNode(node);
                return objectMapper.writeValueAsString(node);
            } catch (IOException e) {
                log.error("fail to mask json object", e);
            }
        }
        return jsonStr;
    }

    private static void doMaskJsonNode(JsonNode node) {
        Iterator<Map.Entry<String, JsonNode>> itr;
        for (itr = node.fields(); itr.hasNext();) {
            Map.Entry<String, JsonNode> n = itr.next();
            if (maskKeySet.containsKey(n.getKey()) || defaultMaskKeys.contains(n.getKey())) {
                if (n.getValue().getNodeType() != JsonNodeType.NULL) {
                    String value = n.getValue().asText();
                    n.setValue(new TextNode(getMaskedStr(value, n.getKey())));
                }
            } else {
                doMaskJsonNode(n.getValue());
            }
        }
    }

    static ValueFilter defaultMaskFilter = new MaskValueFilter();

    /**
     *
     * @param jsonObject
     * @return
     */
    private static String getMaskJson(JSON jsonObject) {
        if (jsonObject == null) {
            return "";
        }
        return getMaskJson(jsonObject, defaultMaskFilter);
    }

    /**
     *
     * @param jsonObject
     * @param valueFilter
     * @return
     */
    private static String getMaskJson(JSON jsonObject, ValueFilter valueFilter) {
        if (jsonObject instanceof JSONObject) {
            String result = toJSONString(valueFilter, (JSONObject) jsonObject);
            return result;
        } else if(jsonObject instanceof JSONArray){
            JSONArray jsonArray = (JSONArray) jsonObject;
            if (jsonObject == null && jsonArray == null) {
                return org.apache.commons.lang3.StringUtils.EMPTY;
            }
            StringBuilder buf = new StringBuilder();
            if (null != jsonArray && jsonArray.size() > 0) {
                jsonArray.stream().forEach(e -> {
                    if (e instanceof JSON) {
                        buf.append(getMaskJson((JSON) e, valueFilter));
                    } else if (e != null) {
                        buf.append("\"" + e.toString() + "\",");
                    }
                });
            }
            if (buf.length() > 0 && buf.charAt(buf.length() - 1) == ',') {
                buf.insert(0, '[');
                buf.setCharAt(buf.length() - 1, ']');
            }
            return buf.toString();
        }
        return "";
    }

    private static String toJSONString(ValueFilter filter, JSONObject jsonObject) {
        return JSON.toJSONString(jsonObject, filter, SerializerFeature.UseSingleQuotes);
    }
}
