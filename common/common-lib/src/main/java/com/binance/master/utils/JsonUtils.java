package com.binance.master.utils;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.StringWriter;
import java.util.*;

/**
 * Json工具类
 *
 * @author weichao.xing
 */
public class JsonUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(JsonUtils.class);

    private static final String EMPTY_JSON = "{}";
    private static final String EMPTY_JSONS = "[]";

    private static final ObjectMapper MAPPER;

    private static final ObjectMapper MAPPER_HAS_NULL;

    static {
        MAPPER = new ObjectMapper();
        MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        MAPPER.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        MAPPER.setSerializationInclusion(Include.NON_NULL);

        MAPPER_HAS_NULL = new ObjectMapper();
        MAPPER_HAS_NULL.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        MAPPER_HAS_NULL.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }

    /**
     * java实体对象转换成json字符串 </br>
     * 默认对象中出现值为null时 隐藏json中的key
     */
    public static String toJsonNotNullKey(Object obj) {
        return JsonUtils.toJson(obj, MAPPER);
    }

    /**
     * java实体对象转换成json字符串 </br>
     * 默认对象中出现值为null时 显示json中的key,其值为null
     */
    public static String toJsonHasNullKey(Object obj) {
        return JsonUtils.toJson(obj, MAPPER_HAS_NULL);
    }

    /**
     * java实体对象转换成json字符串
     */
    private static String toJson(Object obj, ObjectMapper mapper) {
        if (obj == null) {
            return StringUtils.EMPTY;
        }

        try {
            StringWriter writer = new StringWriter();
            mapper.writeValue(writer, obj);
            return writer.toString();
        } catch (IOException e) {
            LOGGER.error("JsonUtils transformation obj to string has error!", e);
        }
        return StringUtils.EMPTY;
    }

    /**
     * json字符串转换为对应的java实体对象
     */
    public static <T> T toObj(String jsonStr, Class<T> objClass) {
        if (isEmpty(jsonStr)) {
            return newInstance(objClass);
        }

        try {
            return MAPPER.readValue(jsonStr, objClass);
        } catch (IOException e) {
            LOGGER.error("JsonUtils transformation string to obj has Eroor!", e);
        }
        return newInstance(objClass);
    }

    private static <T> T newInstance(Class<T> objClass) {
        try {
            return objClass.newInstance();
        } catch (Exception e) {
            LOGGER.error("failed to create new instance", e);
            throw new RuntimeException("cannot create new instance", e);
        }
    }

    public static <T> List<T> toObjList(String jsonStr, Class<T> objClass) {
        if (isEmpty(jsonStr)) {
            return Collections.emptyList();
        }

        try {
            return MAPPER.readValue(jsonStr,
                MAPPER.getTypeFactory().constructParametrizedType(ArrayList.class, List.class, objClass));
        } catch (IOException e) {
            LOGGER.error("JsonUtils transformation string to objList has Eroor!", e);
        }
        return Collections.emptyList();
    }

    public static boolean isEmpty(String content) {
        return StringUtils.isBlank(content) || EMPTY_JSON.equals(content) || EMPTY_JSONS.equals(content);
    }

    public static boolean isNotEmpty(String content) {
        return !isEmpty(content);
    }

    public static JavaType getCollectionType(Class<?> paramClass, Class<?>... elementClasses) {
        return MAPPER.getTypeFactory().constructParametricType(paramClass, elementClasses);
    }

    public static <T> T parse(String jsonStr, Class<T> paramClass, Class<?>... elementClass) {
        try {
            return MAPPER.readValue(jsonStr, getCollectionType(paramClass, elementClass));
        } catch (IOException e) {
            LOGGER.error("parse error: ", e);
        }
        return null;
    }

    public static <T> T parse(String jsonStr, TypeReference<T> typeReference) {
        try {
            return MAPPER.readValue(jsonStr, typeReference);
        } catch (IOException e) {
            LOGGER.error("parse error: ", e);
        }
        return null;
    }

    /**
     * 将json字符串转换为ObjectNode（允许是个数组）
     *
     * @param jsonString
     * @return
     */
    public static JsonNode tree(String jsonString) {
        try {
            return MAPPER.readTree(jsonString);
        } catch (IOException e) {
            LOGGER.error("jsonString to ObjectNode error: ", e);
        }
        return null;
    }

    @SuppressWarnings("rawtypes")
    public static Map toMap(String jsonStr, Class<?> keyClass, Class<?> valueClass) {
        try {
            return MAPPER.readValue(jsonStr,
                MAPPER.getTypeFactory().constructParametricType(HashMap.class, keyClass, valueClass));
        } catch (Throwable e) {
            LOGGER.warn("parese error,the json is:" + jsonStr);
            return Collections.EMPTY_MAP;
        }
    }

    @SuppressWarnings("rawtypes")
    public static Map toMapForSecretManager(String jsonStr, Class<?> keyClass, Class<?> valueClass) throws Exception {
        return MAPPER.readValue(jsonStr,
            MAPPER.getTypeFactory().constructParametricType(HashMap.class, keyClass, valueClass));
    }

    /**
     * 创建新ObjectNode
     *
     * @return
     */
    public static ObjectNode createObjectNode() {
        return MAPPER.createObjectNode();
    }

    /**
     * 创建新ArrayNode
     *
     * @return
     */
    public static ArrayNode createArrayNode() {
        return MAPPER.createArrayNode();
    }

    /**
     * object->jsonNode
     *
     * @param fromObj
     * @return
     */
    public static JsonNode valueToTree(Object fromObj) {
        return MAPPER.valueToTree(fromObj);
    }

    public static boolean isJson(String json) {
        if (json == null || json.trim().isEmpty()) {
            return false;
        }
        try {
            JsonParser parser = MAPPER.getFactory().createParser(json);
            while (parser.nextToken() != null);
            return true;
        } catch (JsonParseException e) {
            return false;
        } catch (IOException e) {
            return false;
        }
    }

}
