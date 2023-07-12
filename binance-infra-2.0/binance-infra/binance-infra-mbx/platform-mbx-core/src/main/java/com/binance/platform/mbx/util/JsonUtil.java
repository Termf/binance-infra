package com.binance.platform.mbx.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import static com.fasterxml.jackson.core.JsonGenerator.Feature.WRITE_BIGDECIMAL_AS_PLAIN;

/**
 * @author Li Fenggang
 * Date: 2020/7/6 5:03 下午
 */
public class JsonUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(JsonUtil.class);
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    static {
        // serialization
        OBJECT_MAPPER.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        OBJECT_MAPPER.configure(WRITE_BIGDECIMAL_AS_PLAIN, true);

        // deserialization
        OBJECT_MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    /**
     * Convert an object to a json.
     *
     * @param object
     * @return
     * @throws JsonProcessingException
     */
    public static String toJsonString(Object object) throws JsonProcessingException {
        String jsonString = OBJECT_MAPPER.writeValueAsString(object);
        return jsonString;
    }

    /**
     * Convert an object to a json.
     *
     * @param object
     * @return
     */
    public static String toJsonStringSilently(Object object) {
        String jsonString = null;
        try {
            jsonString = OBJECT_MAPPER.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            LOGGER.warn(e.getMessage());
        }
        return jsonString;
    }

    /**
     * read value from a json.
     *
     * @param json
     * @param typeReference
     * @param <T>
     * @return
     * @throws IOException
     */
    public static <T> T fromJson(String json, TypeReference<T> typeReference) throws IOException {
        T t  = OBJECT_MAPPER.readValue(json, typeReference);
        return t;
    }

    /**
     * read value from a json.
     *
     * @param json
     * @param valueType
     * @param <T>
     * @return
     * @throws IOException
     */
    public static <T> T fromJson(String json, Class<T> valueType) throws IOException {
        T t = OBJECT_MAPPER.readValue(json, valueType);
        return t;
    }

    /**
     * read value from a json.
     *
     * @param json
     * @param valueType
     * @param <T>
     * @return
     * @throws IOException
     */
    public static <T> T fromJsonSilently(String json, Class<T> valueType) {
        T t = null;
        try {
            t = OBJECT_MAPPER.readValue(json, valueType);
        } catch (IOException e) {
            LOGGER.warn(e.getMessage());
        }
        return t;
    }

}
