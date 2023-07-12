package com.binance.platform.redis.cache.support;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Function;

public class JacksonKeyConverter implements Function<Object, Object> {

    public static final JacksonKeyConverter INSTANCE = new JacksonKeyConverter();

    public static final ObjectMapper mapper = new ObjectMapper();

    private static final Logger LOGGER = LoggerFactory.getLogger(JacksonKeyConverter.class);

    @Override
    public Object apply(Object originalKey) {
        if (originalKey == null) {
            return null;
        }
        if (originalKey instanceof String) {
            return originalKey;
        }
        try {
            return mapper.writeValueAsString(originalKey);
        } catch (JsonProcessingException ex) {
            LOGGER.error("JacksonKeyConverter apply has error", ex);
            return originalKey.toString();
        }
    }
}
