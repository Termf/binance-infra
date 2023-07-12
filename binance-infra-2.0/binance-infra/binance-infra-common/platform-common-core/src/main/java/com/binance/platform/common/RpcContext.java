package com.binance.platform.common;

import java.util.HashMap;
import java.util.Map;

public class RpcContext {

	private static final ThreadLocal<RpcContext> LOCAL = new ThreadLocal<RpcContext>() {

		@Override
		protected RpcContext initialValue() {
			return new RpcContext();
		}
	};

	private final Map<String, Object> attachments = new HashMap<String, Object>();
	private final Map<String, String> values = new HashMap<String, String>();
	public static final String HTTP_CUSTOMIZE_HEADER = "X-CUSTOMIZE-";

	public static RpcContext getContext() {
		return LOCAL.get();
	}

	public static void clear() {
		LOCAL.remove();
	}

	private RpcContext() {
	}

	public Object getAttachment(String key) {
		return attachments.get(key);
	}

	public boolean containAttachment(String key) {
		return attachments.containsKey(key);
	}

	public RpcContext setAttachment(String key, String value) {
		if (value == null) {
			attachments.remove(key);
		} else {
			attachments.put(key, value);
		}
		return this;
	}

	public RpcContext removeAttachment(String key) {
		attachments.remove(key);
		return this;
	}

	public Map<String, Object> getAttachments() {
		return attachments;
	}

	public RpcContext setAttachments(Map<String, String> attachment) {
		if (attachment != null && attachment.size() > 0) {
			this.attachments.putAll(attachment);
		}
		return this;
	}

	public void clean() {
		this.attachments.clear();
		this.values.clear();
	}

	public Map<String, String> get() {
		return values;
	}

	public RpcContext set(String key, String value) {
		if (value == null) {
			values.remove(key);
		} else {
			values.put(key, value);
		}
		return this;
	}

	public RpcContext set(Map<String, String> value) {
		if (value != null && value.size() > 0) {
			values.putAll(value);
		}
		return this;
	}

	public RpcContext remove(String key) {
		values.remove(key);
		return this;
	}

	public String get(String key) {
		return values.get(key);
	}

	public boolean contain(String key) {
		return values.containsKey(key);
	}
}
