package com.binance.platform.monitor.logging.appender.logback.layout;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.skywalking.apm.toolkit.trace.TraceContext;
import org.slf4j.MDC;

import com.binance.platform.common.RpcContext;
import com.binance.platform.common.TrackingUtils;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.IThrowableProxy;
import ch.qos.logback.classic.spi.ThrowableProxy;
import ch.qos.logback.core.LayoutBase;

public class CustomJsonLayout extends LayoutBase<ILoggingEvent> {

	public static final String MIXEDFIELDS_APP_NAME = "appName";

	public static final String MIXEDFIELDS_HOST_NAME = "ip";

	private final ObjectMapper objectMapper;

	CustomJsonLayout(final Map<String, String> mixedFields) {
		SimpleModule module = new SimpleModule();
		module.addSerializer(ILoggingEvent.class, new LogEventSerializer(mixedFields));
		module.addSerializer(Throwable.class, new ThrowableSerializer());
		objectMapper = new ObjectMapper();
		objectMapper.registerModule(module);
		objectMapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
	}

	public static CustomJsonLayout createLayout(final Map<String, String> mixedFields) {
		return new CustomJsonLayout(mixedFields);
	}

	@Override
	public String doLayout(ILoggingEvent event) {
		try {
			String text = objectMapper.writeValueAsString(event);
			return text;
		} catch (JsonProcessingException e) {
			throw new IllegalStateException(e);
		}
	}

	private static class LogEventSerializer extends StdSerializer<ILoggingEvent> {

		private static final long serialVersionUID = 1L;
		final Map<String, String> mixedFields;

		LogEventSerializer(Map<String, String> mixedFields) {
			super(ILoggingEvent.class);
			this.mixedFields = mixedFields;
		}

		@Override
		public void serialize(ILoggingEvent value, JsonGenerator gen, SerializerProvider provider) throws IOException {
			gen.writeStartObject();
			gen.writeStringField(MIXEDFIELDS_APP_NAME, mixedFields.get(MIXEDFIELDS_APP_NAME));
			gen.writeStringField(MIXEDFIELDS_HOST_NAME, mixedFields.get(MIXEDFIELDS_HOST_NAME));
			gen.writeStringField("level", value.getLevel().toString());
			gen.writeStringField("uuid", traceId());
			gen.writeStringField("thread", value.getThreadName());
			gen.writeStringField("separator", "-");
			gen.writeNumberField("timestamp", value.getTimeStamp());
			gen.writeStringField("class", value.getLoggerName());
			gen.writeStringField("message", buildMessage(value));
			gen.writeEndObject();
		}

		private String buildMessage(ILoggingEvent value) {
			StringBuilder message = new StringBuilder();
			message.append(value.getMessage());
			IThrowableProxy throwable = value.getThrowableProxy();
			if (throwable != null && throwable instanceof ThrowableProxy) {
				message.append(" ");
				StringWriter stringWriter = new StringWriter();
				PrintWriter printWriter = new PrintWriter(stringWriter);
				((ThrowableProxy) throwable).getThrowable().printStackTrace(printWriter);
				StringBuffer error = stringWriter.getBuffer();
				message.append(error.toString());
			}
			return message.toString();
		}
	}

	private static String traceId() {
		return TrackingUtils.getTrace();
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
