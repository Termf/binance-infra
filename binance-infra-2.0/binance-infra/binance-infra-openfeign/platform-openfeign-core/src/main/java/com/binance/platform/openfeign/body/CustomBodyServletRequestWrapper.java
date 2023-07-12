package com.binance.platform.openfeign.body;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.IOUtils;

public class CustomBodyServletRequestWrapper extends CustomeHeaderServletRequestWrapper {

	private byte[] body;

	public CustomBodyServletRequestWrapper(HttpServletRequest request) throws IOException {
		super(request);
		body = IOUtils.toByteArray(super.getInputStream());
	}

	public void setBody(byte[] body) {
		this.body = body;
	}

	public byte[] getBody() {
		return body;
	}

	@Override
	public BufferedReader getReader() throws IOException {
		return new BufferedReader(new InputStreamReader(getInputStream()));
	}

	@Override
	public ServletInputStream getInputStream() throws IOException {
		return new RequestBodyCachingInputStream(body);
	}

	@Override
	public int getContentLength() {
		return this.body.length;
	}

	@Override
	public long getContentLengthLong() {
		return this.body.length;
	}

	private class RequestBodyCachingInputStream extends ServletInputStream {
		private byte[] body;
		private int lastIndexRetrieved = -1;
		private ReadListener listener;

		public RequestBodyCachingInputStream(byte[] body) {
			this.body = body;
		}

		@Override
		public int read() throws IOException {
			if (isFinished()) {
				return -1;
			}
			int i = body[lastIndexRetrieved + 1];
			lastIndexRetrieved++;
			if (isFinished() && listener != null) {
				try {
					listener.onAllDataRead();
				} catch (IOException e) {
					listener.onError(e);
					throw e;
				}
			}
			return i;
		}

		@Override
		public boolean isFinished() {
			return lastIndexRetrieved == body.length - 1;
		}

		@Override
		public boolean isReady() {
			return isFinished();
		}

		@Override
		public void setReadListener(ReadListener listener) {
			if (listener == null) {
				throw new IllegalArgumentException("listener cann not be null");
			}
			if (this.listener != null) {
				throw new IllegalArgumentException("listener has been set");
			}
			this.listener = listener;
			if (!isFinished()) {
				try {
					listener.onAllDataRead();
				} catch (IOException e) {
					listener.onError(e);
				}
			} else {
				try {
					listener.onAllDataRead();
				} catch (IOException e) {
					listener.onError(e);
				}
			}
		}

		@Override
		public int available() throws IOException {
			return body.length - lastIndexRetrieved - 1;
		}

		@Override
		public void close() throws IOException {
			lastIndexRetrieved = body.length - 1;
			body = null;
		}
	}
}
