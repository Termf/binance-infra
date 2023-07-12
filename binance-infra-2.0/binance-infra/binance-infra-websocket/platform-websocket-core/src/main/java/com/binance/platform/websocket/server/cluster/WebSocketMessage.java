package com.binance.platform.websocket.server.cluster;

import java.io.IOException;
import java.io.Serializable;
import java.util.Base64;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

public class WebSocketMessage implements Serializable {

	private static final long serialVersionUID = -4905658079169021284L;

	private Boolean broadcast;

	private String toSessionId;

	private String textMessage;

	private Bytes binaryMessage;

	public WebSocketMessage() {

	}

	public WebSocketMessage(String toSessionId, String textMessage) {
		this.toSessionId = toSessionId;
		this.textMessage = textMessage;
	}

	public WebSocketMessage(String toSessionId, byte[] binaryMessage) {
		this.toSessionId = toSessionId;
		this.binaryMessage = new Bytes(binaryMessage);
	}

	public WebSocketMessage(Boolean broadcast, String textMessage) {
		this.broadcast = broadcast;
		this.textMessage = textMessage;
	}

	public WebSocketMessage(Boolean broadcast, byte[] binaryMessage) {
		this.broadcast = broadcast;
		this.binaryMessage = new Bytes(binaryMessage);
	}

	public Boolean getBroadcast() {
		return broadcast;
	}

	public void setBroadcast(Boolean broadcast) {
		this.broadcast = broadcast;
	}

	public String getToSessionId() {
		return toSessionId;
	}

	public void setToSessionId(String toSessionId) {
		this.toSessionId = toSessionId;
	}

	public String getTextMessage() {
		return textMessage;
	}

	public void setTextMessage(String textMessage) {
		this.textMessage = textMessage;
	}

	public byte[] getBinaryMessage() {
		return binaryMessage != null ? binaryMessage.getBytes() : null;
	}

	public void setBinaryMessage(byte[] binaryMessage) {
		this.binaryMessage = new Bytes(binaryMessage);
	}

	@JsonSerialize(using = BytesSerializer.class)
	@JsonDeserialize(using = BytesDeserializer.class)
	public static class Bytes {
		private byte[] bytes;

		public Bytes(byte[] bytes) {
			this.bytes = bytes;
		}

		public byte[] getBytes() {
			return bytes;
		}
	}

	public static class BytesSerializer extends StdSerializer<Bytes> {

		private static final long serialVersionUID = -5510353102817291511L;

		public BytesSerializer() {
			super(Bytes.class);
		}

		@Override
		public void serialize(Bytes value, JsonGenerator gen, SerializerProvider provider) throws IOException {
			gen.writeString(Base64.getEncoder().encodeToString(value.getBytes()));
		}
	}

	public static class BytesDeserializer extends StdDeserializer<Bytes> {

		private static final long serialVersionUID = 1514703510863497028L;

		public BytesDeserializer() {
			super(Bytes.class);
		}

		@Override
		public Bytes deserialize(JsonParser p, DeserializationContext ctxt)
				throws IOException, JsonProcessingException {
			JsonNode node = p.getCodec().readTree(p);
			String base64 = node.asText();
			return new Bytes(Base64.getDecoder().decode(base64));
		}
	}

}
