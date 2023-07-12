package com.binance.platform.monitor.logging.appender.log4j.layout;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import com.binance.master.constant.Constant;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.Node;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginConfiguration;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.layout.AbstractStringLayout;
import org.apache.logging.log4j.core.layout.PatternLayout;

import com.binance.platform.common.TrackingUtils;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.apache.logging.log4j.util.ReadOnlyStringMap;

import static com.binance.platform.common.TrackingUtils.REQUEST_TIME;
import static com.binance.platform.common.TrackingUtils.TRACE_ID;
import static com.binance.platform.common.TrackingUtils.X_AMZN_TRACE_ID;

@Plugin(name = "CustomJsonLayout", category = Node.CATEGORY, elementType = Layout.ELEMENT_TYPE, printObject = true)
public class CustomJsonLayout extends AbstractStringLayout {

    public static final String MIXEDFIELDS_APP_NAME = "appName";

    public static final String MIXEDFIELDS_HOST_NAME = "ip";

    private static final String DEFAULT_FOOTER = "]";

    private static final String DEFAULT_HEADER = "[";

    private final ObjectMapper objectMapper;

    CustomJsonLayout(final Configuration config, final String headerPattern, final String footerPattern,
        final Charset charset, final Map<String, String> mixedFields) {
        super(config, charset,
            PatternLayout.createSerializer(config, null, headerPattern, DEFAULT_HEADER, null, false, false),
            PatternLayout.createSerializer(config, null, footerPattern, DEFAULT_FOOTER, null, false, false));
        SimpleModule module = new SimpleModule();
        module.addSerializer(LogEvent.class, new LogEventSerializer(mixedFields));
        module.addSerializer(Throwable.class, new ThrowableSerializer());
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(module);
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
    }

    @Override
    public String toSerializable(LogEvent logEvent) {
        try {
            String text = objectMapper.writeValueAsString(logEvent);
            return text;
        } catch (JsonProcessingException e) {
            throw new IllegalStateException(e);
        }
    }

    @PluginFactory
    public static CustomJsonLayout createLayout(@PluginConfiguration final Configuration config,
        @PluginAttribute(value = "header", defaultString = DEFAULT_HEADER) final String headerPattern,
        @PluginAttribute(value = "footer", defaultString = DEFAULT_FOOTER) final String footerPattern,
        @PluginAttribute(value = "charset", defaultString = "UTF-8") final Charset charset,
        @PluginAttribute(value = "mixedFields") final Map<String, String> mixedFields) {
        return new CustomJsonLayout(config, headerPattern, footerPattern, charset, mixedFields);
    }

    public static CustomJsonLayout createDefaultLayout(Configuration config, Map<String, String> mixedFields) {
        return new CustomJsonLayout(config, DEFAULT_HEADER, DEFAULT_FOOTER, StandardCharsets.UTF_8, mixedFields);
    }

    private static class LogEventSerializer extends StdSerializer<LogEvent> {

        private static final long serialVersionUID = 1L;
        final Map<String, String> mixedFields;

        LogEventSerializer(Map<String, String> mixedFields) {
            super(LogEvent.class);
            this.mixedFields = mixedFields;
        }

        @Override
        public void serialize(LogEvent value, JsonGenerator gen, SerializerProvider provider) throws IOException {
            gen.writeStartObject();
            gen.writeStringField(MIXEDFIELDS_APP_NAME, mixedFields.get(MIXEDFIELDS_APP_NAME));
            gen.writeStringField(MIXEDFIELDS_HOST_NAME, mixedFields.get(MIXEDFIELDS_HOST_NAME));
            ReadOnlyStringMap mdc = value.getContextData();
            gen.writeStringField("level", value.getLevel().name());
            gen.writeStringField("uuid", traceId(mdc));
            gen.writeStringField("thread", value.getThreadName());
            gen.writeStringField("costTime", requestTime(mdc));
            gen.writeStringField("userId", userId(mdc));
            gen.writeStringField("pinpointId", pinPointId(mdc));
            gen.writeStringField("awsId", awsId(mdc));
            gen.writeNumberField("timestamp", value.getTimeMillis());
            gen.writeStringField("class", value.getLoggerName());
            gen.writeStringField("message", buildMessage(value));
            gen.writeEndObject();
        }

        private String buildMessage(LogEvent value) {
            StringBuilder message = new StringBuilder();
            message.append(value.getMessage().getFormattedMessage());
            Throwable throwable = value.getThrown();
            if (throwable != null) {
                //TODO 这里printStackTrace很耗资源 考虑优化
                message.append(" ");
                StringWriter stringWriter = new StringWriter();
                PrintWriter printWriter = new PrintWriter(stringWriter);
                throwable.printStackTrace(printWriter);
                StringBuffer error = stringWriter.getBuffer();
                message.append(error.toString());
            }
            return message.toString();
        }
    }
    private static String traceId(ReadOnlyStringMap mdc) {
        String traceId = mdc.getValue(TRACE_ID);
        if (StringUtils.isBlank(traceId)) {
            traceId = StringUtils.EMPTY;
        }
        return traceId;
    }

    private static String requestTime(ReadOnlyStringMap mdc) {
        String requestTime = mdc.getValue(REQUEST_TIME);
        if (StringUtils.isBlank(requestTime)) {
            return "0";
        } else {
            return requestTime;
        }
    }

    private static String userId(ReadOnlyStringMap mdc) {
        String userId = mdc.getValue(Constant.HEADER_USER_ID);
        if (StringUtils.isBlank(userId)) {
            userId = StringUtils.EMPTY;
        }
        return userId;
    }

    private static String pinPointId(ReadOnlyStringMap mdc) {
        String pinpointId = mdc.getValue("PtxId");
        if (StringUtils.isBlank(pinpointId)) {
            pinpointId = StringUtils.EMPTY;
        }
        return pinpointId;
    }

    private static String awsId(ReadOnlyStringMap mdc) {
        String awsId = mdc.getValue(X_AMZN_TRACE_ID);
        if (StringUtils.isBlank(awsId)) {
            awsId = StringUtils.EMPTY;
        }
        return awsId;
    }

    private static class ThrowableSerializer extends StdSerializer<Throwable> {

        private static final long serialVersionUID = 1L;

        ThrowableSerializer() {
            super(Throwable.class);
        }

        @Override
        public void serialize(Throwable value, JsonGenerator gen, SerializerProvider provider) throws IOException {
            try (StringWriter stringWriter = new StringWriter()) {
                try (PrintWriter printWriter = new PrintWriter(stringWriter)) {
                    value.printStackTrace(printWriter);
                    gen.writeString(stringWriter.toString());
                }
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
        }
    }

}
