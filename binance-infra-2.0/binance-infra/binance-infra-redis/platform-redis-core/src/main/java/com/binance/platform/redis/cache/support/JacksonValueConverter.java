package com.binance.platform.redis.cache.support;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.springframework.cache.support.NullValue;
import org.springframework.data.redis.serializer.SerializationException;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.io.IOException;

public class JacksonValueConverter {

    public static final String CLASS_IDENTIFIER = "@class";

    private final ObjectMapper mapper;

    public JacksonValueConverter() {
        this((String) null);
    }

    public JacksonValueConverter(ObjectMapper mapper) {
        Assert.notNull(mapper, "ObjectMapper must not be null!");
        this.mapper = mapper;
    }

    public JacksonValueConverter(@Nullable String classPropertyTypeName) {
        this(new ObjectMapper());
        // simply setting {@code mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS)} does not help
        // here since we need
        // the type hint embedded for deserialization using the default typing feature.
        mapper.registerModule(new SimpleModule().addSerializer(new NullValueSerializer(classPropertyTypeName)));

        if (StringUtils.hasText(classPropertyTypeName)) {
            mapper.enableDefaultTypingAsProperty(ObjectMapper.DefaultTyping.NON_FINAL, classPropertyTypeName);
        } else {
            mapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY);
        }
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }

    public String serialize(@Nullable Object source) throws SerializationException {
        if (source == null) {
            return com.binance.master.utils.StringUtils.EMPTY;
        }
        try {
            return mapper.writeValueAsString(source);
        } catch (JsonProcessingException e) {
            throw new SerializationException("Could not write JSON: " + e.getMessage(), e);
        }
    }


    public Object deserialize(@Nullable String source) throws SerializationException {
        return deserialize(source, Object.class);
    }

    public <T> T deserialize(@Nullable String source, Class<T> type) throws SerializationException {
        Assert.notNull(type, "Deserialization type must not be null! Pleaes provide Object.class to make use of Jackson2 default typing.");
        if (StringUtils.isEmpty(source)) {
            return null;
        }
        try {
            return mapper.readValue(source, type);
        } catch (Exception ex) {
            throw new SerializationException("Could not read JSON: " + ex.getMessage(), ex);
        }
    }

    private class NullValueSerializer extends StdSerializer<NullValue> {

        private static final long serialVersionUID = 1999052150548658808L;
        private final String classIdentifier;

        /**
         * @param classIdentifier can be {@literal null} and will be defaulted to {@code @class}.
         */
        NullValueSerializer(@Nullable String classIdentifier) {

            super(NullValue.class);
            this.classIdentifier = StringUtils.hasText(classIdentifier) ? classIdentifier : CLASS_IDENTIFIER;
        }

        /*
         * (non-Javadoc)
         *
         * @see com.fasterxml.jackson.databind.ser.std.StdSerializer#serialize(java.lang.Object,
         * com.fasterxml.jackson.core.JsonGenerator, com.fasterxml.jackson.databind.SerializerProvider)
         */
        @Override
        public void serialize(NullValue value, JsonGenerator jgen, SerializerProvider provider) throws IOException {

            jgen.writeStartObject();
            jgen.writeStringField(classIdentifier, NullValue.class.getName());
            jgen.writeEndObject();
        }
    }

}
