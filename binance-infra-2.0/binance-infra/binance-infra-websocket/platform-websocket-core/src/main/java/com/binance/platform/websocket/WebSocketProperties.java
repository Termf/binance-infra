package com.binance.platform.websocket;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "websocket")
public class WebSocketProperties {

	private Client client;

	private Server server;

	public Server getServer() {
		return server;
	}

	public void setServer(Server server) {
		this.server = server;
	}

	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	}

	public static class Client {
		private String url;
		private int workThreads = Runtime.getRuntime().availableProcessors();

		public String getUrl() {
			return url;
		}

		public void setUrl(String url) {
			this.url = url;
		}

		public int getWorkThreads() {
			return workThreads;
		}

		public void setWorkThreads(int workThreads) {
			this.workThreads = workThreads;
		}

	}

	public static class Server {
		private String host = "0.0.0.0";
		private int port;
		private int bossThreads = 1;
		private int workThreads = Runtime.getRuntime().availableProcessors();
		private String endpoint = "/ws";
		private int readerIdleTimeSeconds = 0;
		private int writerIdleTimeSeconds = 0;
		private int allIdleTimeSeconds = 70;

		public int getReaderIdleTimeSeconds() {
			return readerIdleTimeSeconds;
		}

		public void setReaderIdleTimeSeconds(int readerIdleTimeSeconds) {
			this.readerIdleTimeSeconds = readerIdleTimeSeconds;
		}

		public int getWriterIdleTimeSeconds() {
			return writerIdleTimeSeconds;
		}

		public void setWriterIdleTimeSeconds(int writerIdleTimeSeconds) {
			this.writerIdleTimeSeconds = writerIdleTimeSeconds;
		}

		public int getAllIdleTimeSeconds() {
			return allIdleTimeSeconds;
		}

		public void setAllIdleTimeSeconds(int allIdleTimeSeconds) {
			this.allIdleTimeSeconds = allIdleTimeSeconds;
		}

		public String getHost() {
			return host;
		}

		public void setHost(String host) {
			this.host = host;
		}

		public int getPort() {
			return port;
		}

		public void setPort(int port) {
			this.port = port;
		}

		public int getBossThreads() {
			return bossThreads;
		}

		public void setBossThreads(int bossThreads) {
			this.bossThreads = bossThreads;
		}

		public int getWorkThreads() {
			return workThreads;
		}

		public void setWorkThreads(int workThreads) {
			this.workThreads = workThreads;
		}

		public String getEndpoint() {
			return this.endpoint;
		}

		public void setEndpoint(String endpoint) {
			if (endpoint != null && endpoint.startsWith("/")) {
				this.endpoint = endpoint;
			} else if (endpoint.startsWith("/")) {
				this.endpoint = "/" + endpoint;
			} else {
				this.endpoint = endpoint;
			}

		}

	}

}
