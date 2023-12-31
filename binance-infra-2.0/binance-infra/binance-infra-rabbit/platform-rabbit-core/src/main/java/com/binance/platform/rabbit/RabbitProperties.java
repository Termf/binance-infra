/*
 * Copyright (c) 2018 DISID CORPORATION S.L.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */

package com.binance.platform.rabbit;

import java.util.ArrayList;
import java.util.List;

import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory.CacheMode;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.DeprecatedConfigurationProperty;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

/**
 * Configuration properties for Rabbit.
 *
 * @author Greg Turnquist
 * @author Dave Syer
 * @author Stephane Nicoll
 * @author Andy Wilkinson
 * @author Josh Thornhill
 * @author Gary Russell
 */
@ConfigurationProperties(prefix = "spring.rabbitmq")
public class RabbitProperties {

    private static final class Address {

        private static final String PREFIX_AMQP = "amqp://";

        private static final int DEFAULT_PORT = 5672;

        private String host;

        private int port;

        private String username;

        private String password;

        private String virtualHost;

        private Address(String input) {
            input = input.trim();
            input = trimPrefix(input);
            input = parseUsernameAndPassword(input);
            input = parseVirtualHost(input);
            parseHostAndPort(input);
        }

        private void parseHostAndPort(String input) {
            int portIndex = input.indexOf(':');
            if (portIndex == -1) {
                this.host = input;
                this.port = DEFAULT_PORT;
            } else {
                this.host = input.substring(0, portIndex);
                this.port = Integer.valueOf(input.substring(portIndex + 1));
            }
        }

        private String parseUsernameAndPassword(String input) {
            if (input.contains("@")) {
                String[] split = StringUtils.split(input, "@");
                String creds = split[0];
                input = split[1];
                split = StringUtils.split(creds, ":");
                this.username = split[0];
                if (split.length > 0) {
                    this.password = split[1];
                }
            }
            return input;
        }

        private String parseVirtualHost(String input) {
            int hostIndex = input.indexOf("/");
            if (hostIndex >= 0) {
                this.virtualHost = input.substring(hostIndex + 1);
                if (this.virtualHost.isEmpty()) {
                    this.virtualHost = "/";
                }
                input = input.substring(0, hostIndex);
            }
            return input;
        }

        private String trimPrefix(String input) {
            if (input.startsWith(PREFIX_AMQP)) {
                input = input.substring(PREFIX_AMQP.length());
            }
            return input;
        }

    }

    public static class AmqpContainer {

        /**
         * Start the container automatically on startup.
         */
        private boolean autoStartup = true;

        /**
         * Acknowledge mode of container.
         */
        private AcknowledgeMode acknowledgeMode;

        /**
         * Minimum number of consumers.
         */
        private Integer concurrency;

        /**
         * Maximum number of consumers.
         */
        private Integer maxConcurrency;

        /**
         * Number of messages to be handled in a single request. It should be greater than or equal to the transaction
         * size (if used).
         */
        private Integer prefetch;

        /**
         * Number of messages to be processed in a transaction. For best results it should be less than or equal to the
         * prefetch count.
         */
        private Integer transactionSize;

        /**
         * Whether rejected deliveries are requeued by default; default true.
         */
        private Boolean defaultRequeueRejected;

        /**
         * How often idle container events should be published in milliseconds.
         */
        private Long idleEventInterval;

        /**
         * Optional properties for a retry interceptor.
         */
        @NestedConfigurationProperty
        private final ListenerRetry retry = new ListenerRetry();

        public AcknowledgeMode getAcknowledgeMode() {
            return this.acknowledgeMode;
        }

        public Integer getConcurrency() {
            return this.concurrency;
        }

        public Boolean getDefaultRequeueRejected() {
            return this.defaultRequeueRejected;
        }

        public Long getIdleEventInterval() {
            return this.idleEventInterval;
        }

        public Integer getMaxConcurrency() {
            return this.maxConcurrency;
        }

        public Integer getPrefetch() {
            return this.prefetch;
        }

        public ListenerRetry getRetry() {
            return this.retry;
        }

        public Integer getTransactionSize() {
            return this.transactionSize;
        }

        public boolean isAutoStartup() {
            return this.autoStartup;
        }

        public void setAcknowledgeMode(AcknowledgeMode acknowledgeMode) {
            this.acknowledgeMode = acknowledgeMode;
        }

        public void setAutoStartup(boolean autoStartup) {
            this.autoStartup = autoStartup;
        }

        public void setConcurrency(Integer concurrency) {
            this.concurrency = concurrency;
        }

        public void setDefaultRequeueRejected(Boolean defaultRequeueRejected) {
            this.defaultRequeueRejected = defaultRequeueRejected;
        }

        public void setIdleEventInterval(Long idleEventInterval) {
            this.idleEventInterval = idleEventInterval;
        }

        public void setMaxConcurrency(Integer maxConcurrency) {
            this.maxConcurrency = maxConcurrency;
        }

        public void setPrefetch(Integer prefetch) {
            this.prefetch = prefetch;
        }

        public void setTransactionSize(Integer transactionSize) {
            this.transactionSize = transactionSize;
        }

    }

    public static class Cache {

        public static class Channel {

            /**
             * Number of channels to retain in the cache. When "check-timeout" > 0, max channels per connection.
             */
            private Integer size;

            /**
             * Number of milliseconds to wait to obtain a channel if the cache size has been reached. If 0, always
             * create a new channel.
             */
            private Long checkoutTimeout;

            public Long getCheckoutTimeout() {
                return this.checkoutTimeout;
            }

            public Integer getSize() {
                return this.size;
            }

            public void setCheckoutTimeout(Long checkoutTimeout) {
                this.checkoutTimeout = checkoutTimeout;
            }

            public void setSize(Integer size) {
                this.size = size;
            }

        }

        public static class Connection {

            /**
             * Connection factory cache mode.
             */
            private CacheMode mode = CacheMode.CHANNEL;

            /**
             * Number of connections to cache. Only applies when mode is CONNECTION.
             */
            private Integer size;

            public CacheMode getMode() {
                return this.mode;
            }

            public Integer getSize() {
                return this.size;
            }

            public void setMode(CacheMode mode) {
                this.mode = mode;
            }

            public void setSize(Integer size) {
                this.size = size;
            }

        }

        private final Channel channel = new Channel();

        private final Connection connection = new Connection();

        public Channel getChannel() {
            return this.channel;
        }

        public Connection getConnection() {
            return this.connection;
        }

    }

    public static class Listener {

        @NestedConfigurationProperty
        private final AmqpContainer simple = new AmqpContainer();

        @DeprecatedConfigurationProperty(replacement = "spring.rabbitmq.listener.simple.acknowledge-mode")
        @Deprecated
        public AcknowledgeMode getAcknowledgeMode() {
            return getSimple().getAcknowledgeMode();
        }

        @DeprecatedConfigurationProperty(replacement = "spring.rabbitmq.listener.simple.concurrency")
        @Deprecated
        public Integer getConcurrency() {
            return getSimple().getConcurrency();
        }

        @DeprecatedConfigurationProperty(replacement = "spring.rabbitmq.listener.simple.default-requeue-rejected")
        @Deprecated
        public Boolean getDefaultRequeueRejected() {
            return getSimple().getDefaultRequeueRejected();
        }

        @DeprecatedConfigurationProperty(replacement = "spring.rabbitmq.listener.simple.idle-event-interval")
        @Deprecated
        public Long getIdleEventInterval() {
            return getSimple().getIdleEventInterval();
        }

        @DeprecatedConfigurationProperty(replacement = "spring.rabbitmq.listener.simple.max-concurrency")
        @Deprecated
        public Integer getMaxConcurrency() {
            return getSimple().getMaxConcurrency();
        }

        @DeprecatedConfigurationProperty(replacement = "spring.rabbitmq.listener.simple.prefetch")
        @Deprecated
        public Integer getPrefetch() {
            return getSimple().getPrefetch();
        }

        @DeprecatedConfigurationProperty(replacement = "spring.rabbitmq.listener.simple.retry")
        @Deprecated
        public ListenerRetry getRetry() {
            return getSimple().getRetry();
        }

        public AmqpContainer getSimple() {
            return this.simple;
        }

        @DeprecatedConfigurationProperty(replacement = "spring.rabbitmq.listener.simple.transaction-size")
        @Deprecated
        public Integer getTransactionSize() {
            return getSimple().getTransactionSize();
        }

        @DeprecatedConfigurationProperty(replacement = "spring.rabbitmq.listener.simple.auto-startup")
        @Deprecated
        public boolean isAutoStartup() {
            return getSimple().isAutoStartup();
        }

        @Deprecated
        public void setAcknowledgeMode(AcknowledgeMode acknowledgeMode) {
            getSimple().setAcknowledgeMode(acknowledgeMode);
        }

        @Deprecated
        public void setAutoStartup(boolean autoStartup) {
            getSimple().setAutoStartup(autoStartup);
        }

        @Deprecated
        public void setConcurrency(Integer concurrency) {
            getSimple().setConcurrency(concurrency);
        }

        @Deprecated
        public void setDefaultRequeueRejected(Boolean defaultRequeueRejected) {
            getSimple().setDefaultRequeueRejected(defaultRequeueRejected);
        }

        @Deprecated
        public void setIdleEventInterval(Long idleEventInterval) {
            getSimple().setIdleEventInterval(idleEventInterval);
        }

        @Deprecated
        public void setMaxConcurrency(Integer maxConcurrency) {
            getSimple().setMaxConcurrency(maxConcurrency);
        }

        @Deprecated
        public void setPrefetch(Integer prefetch) {
            getSimple().setPrefetch(prefetch);
        }

        @Deprecated
        public void setTransactionSize(Integer transactionSize) {
            getSimple().setTransactionSize(transactionSize);
        }

    }

    public static class ListenerRetry extends Retry {

        /**
         * Whether or not retries are stateless or stateful.
         */
        private boolean stateless = true;

        public boolean isStateless() {
            return this.stateless;
        }

        public void setStateless(boolean stateless) {
            this.stateless = stateless;
        }

    }

    public static class Retry {

        /**
         * Whether or not publishing retries are enabled.
         */
        private boolean enabled;

        /**
         * Maximum number of attempts to publish or deliver a message.
         */
        private int maxAttempts = 3;

        /**
         * Interval between the first and second attempt to publish or deliver a message.
         */
        private long initialInterval = 1000L;

        /**
         * A multiplier to apply to the previous retry interval.
         */
        private double multiplier = 1.0;

        /**
         * Maximum interval between attempts.
         */
        private long maxInterval = 10000L;

        public long getInitialInterval() {
            return this.initialInterval;
        }

        public int getMaxAttempts() {
            return this.maxAttempts;
        }

        public long getMaxInterval() {
            return this.maxInterval;
        }

        public double getMultiplier() {
            return this.multiplier;
        }

        public boolean isEnabled() {
            return this.enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        public void setInitialInterval(long initialInterval) {
            this.initialInterval = initialInterval;
        }

        public void setMaxAttempts(int maxAttempts) {
            this.maxAttempts = maxAttempts;
        }

        public void setMaxInterval(long maxInterval) {
            this.maxInterval = maxInterval;
        }

        public void setMultiplier(double multiplier) {
            this.multiplier = multiplier;
        }

    }

    public static class Ssl {

        /**
         * Enable SSL support.
         */
        private boolean enabled;

        /**
         * Path to the key store that holds the SSL certificate.
         */
        private String keyStore;

        /**
         * Password used to access the key store.
         */
        private String keyStorePassword;

        /**
         * Trust store that holds SSL certificates.
         */
        private String trustStore;

        /**
         * Password used to access the trust store.
         */
        private String trustStorePassword;

        /**
         * SSL algorithm to use (e.g. TLSv1.1). Default is set automatically by the rabbit client library.
         */
        private String algorithm;

        public String getAlgorithm() {
            return this.algorithm;
        }

        public String getKeyStore() {
            return this.keyStore;
        }

        public String getKeyStorePassword() {
            return this.keyStorePassword;
        }

        public String getTrustStore() {
            return this.trustStore;
        }

        public String getTrustStorePassword() {
            return this.trustStorePassword;
        }

        public boolean isEnabled() {
            return this.enabled;
        }

        public void setAlgorithm(String sslAlgorithm) {
            this.algorithm = sslAlgorithm;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        public void setKeyStore(String keyStore) {
            this.keyStore = keyStore;
        }

        public void setKeyStorePassword(String keyStorePassword) {
            this.keyStorePassword = keyStorePassword;
        }

        public void setTrustStore(String trustStore) {
            this.trustStore = trustStore;
        }

        public void setTrustStorePassword(String trustStorePassword) {
            this.trustStorePassword = trustStorePassword;
        }

    }

    public static class Template {

        @NestedConfigurationProperty
        private final Retry retry = new Retry();

        /**
         * Enable mandatory messages. If a mandatory message cannot be routed to a queue by the server, it will return
         * an unroutable message with a Return method.
         */
        private Boolean mandatory;

        /**
         * Timeout for receive() operations.
         */
        private Long receiveTimeout;

        /**
         * Timeout for sendAndReceive() operations.
         */
        private Long replyTimeout;

        public Boolean getMandatory() {
            return this.mandatory;
        }

        public Long getReceiveTimeout() {
            return this.receiveTimeout;
        }

        public Long getReplyTimeout() {
            return this.replyTimeout;
        }

        public Retry getRetry() {
            return this.retry;
        }

        public void setMandatory(Boolean mandatory) {
            this.mandatory = mandatory;
        }

        public void setReceiveTimeout(Long receiveTimeout) {
            this.receiveTimeout = receiveTimeout;
        }

        public void setReplyTimeout(Long replyTimeout) {
            this.replyTimeout = replyTimeout;
        }

    }

    /**
     * RabbitMQ host.
     */
    private String host = "localhost";

    /**
     * RabbitMQ port.
     */
    private int port = 5672;

    /**
     * Login user to authenticate to the broker.
     */
    private String username;

    /**
     * Login to authenticate against the broker.
     */
    private String password;

    /**
     * SSL configuration.
     */
    private final Ssl ssl = new Ssl();

    /**
     * Virtual host to use when connecting to the broker.
     */
    private String virtualHost;

    /**
     * Comma-separated list of addresses to which the client should connect.
     */
    private String addresses;

    /**
     * Requested heartbeat timeout, in seconds; zero for none.
     */
    private Integer requestedHeartbeat;

    /**
     * Enable publisher confirms.
     */
    private boolean publisherConfirms;

    /**
     * Enable publisher returns.
     */
    private boolean publisherReturns;

    /**
     * Connection timeout, in milliseconds; zero for infinite.
     */
    private Integer connectionTimeout;

    /**
     * Cache configuration.
     */
    private final Cache cache = new Cache();

    /**
     * Listener container configuration.
     */
    private final Listener listener = new Listener();

    private final Template template = new Template();

    private List<Address> parsedAddresses;

    /**
     * Returns the comma-separated addresses or a single address ({@code host:port}) created from the configured host
     * and port if no addresses have been set.
     * 
     * @return the addresses
     */
    public String determineAddresses() {
        if (CollectionUtils.isEmpty(this.parsedAddresses)) {
            return this.host + ":" + this.port;
        }
        List<String> addressStrings = new ArrayList<String>();
        for (Address parsedAddress : this.parsedAddresses) {
            addressStrings.add(parsedAddress.host + ":" + parsedAddress.port);
        }
        return StringUtils.collectionToCommaDelimitedString(addressStrings);
    }

    /**
     * Returns the host from the first address, or the configured host if no addresses have been set.
     * 
     * @return the host
     * @see #setAddresses(String)
     * @see #getHost()
     */
    public String determineHost() {
        if (CollectionUtils.isEmpty(this.parsedAddresses)) {
            return getHost();
        }
        return this.parsedAddresses.get(0).host;
    }

    /**
     * If addresses have been set and the first address has a password it is returned. Otherwise returns the result of
     * calling {@code getPassword()}.
     * 
     * @return the password or {@code null}
     * @see #setAddresses(String)
     * @see #getPassword()
     */
    public String determinePassword() {
        if (CollectionUtils.isEmpty(this.parsedAddresses)) {
            return getPassword();
        }
        Address address = this.parsedAddresses.get(0);
        return address.password == null ? getPassword() : address.password;
    }

    /**
     * Returns the port from the first address, or the configured port if no addresses have been set.
     * 
     * @return the port
     * @see #setAddresses(String)
     * @see #getPort()
     */
    public int determinePort() {
        if (CollectionUtils.isEmpty(this.parsedAddresses)) {
            return getPort();
        }
        Address address = this.parsedAddresses.get(0);
        return address.port;
    }

    /**
     * If addresses have been set and the first address has a username it is returned. Otherwise returns the result of
     * calling {@code getUsername()}.
     * 
     * @return the username
     * @see #setAddresses(String)
     * @see #getUsername()
     */
    public String determineUsername() {
        if (CollectionUtils.isEmpty(this.parsedAddresses)) {
            return this.username;
        }
        Address address = this.parsedAddresses.get(0);
        return address.username == null ? this.username : address.username;
    }

    /**
     * If addresses have been set and the first address has a virtual host it is returned. Otherwise returns the result
     * of calling {@code getVirtualHost()}.
     * 
     * @return the virtual host or {@code null}
     * @see #setAddresses(String)
     * @see #getVirtualHost()
     */
    public String determineVirtualHost() {
        if (CollectionUtils.isEmpty(this.parsedAddresses)) {
            return getVirtualHost();
        }
        Address address = this.parsedAddresses.get(0);
        return address.virtualHost == null ? getVirtualHost() : address.virtualHost;
    }

    public String getAddresses() {
        return this.addresses;
    }

    public Cache getCache() {
        return this.cache;
    }

    public Integer getConnectionTimeout() {
        return this.connectionTimeout;
    }

    public String getHost() {
        return this.host;
    }

    public Listener getListener() {
        return this.listener;
    }

    public String getPassword() {
        return this.password;
    }

    public int getPort() {
        return this.port;
    }

    public Integer getRequestedHeartbeat() {
        return this.requestedHeartbeat;
    }

    public Ssl getSsl() {
        return this.ssl;
    }

    public Template getTemplate() {
        return this.template;
    }

    public String getUsername() {
        return this.username;
    }

    public String getVirtualHost() {
        return this.virtualHost;
    }

    public boolean isPublisherConfirms() {
        return this.publisherConfirms;
    }

    public boolean isPublisherReturns() {
        return this.publisherReturns;
    }

    private List<Address> parseAddresses(String addresses) {
        List<Address> parsedAddresses = new ArrayList<Address>();
        for (String address : StringUtils.commaDelimitedListToStringArray(addresses)) {
            parsedAddresses.add(new Address(address));
        }
        return parsedAddresses;
    }

    public void setAddresses(String addresses) {
        this.addresses = addresses;
        this.parsedAddresses = parseAddresses(addresses);
    }

    public void setConnectionTimeout(Integer connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void setPublisherConfirms(boolean publisherConfirms) {
        this.publisherConfirms = publisherConfirms;
    }

    public void setPublisherReturns(boolean publisherReturns) {
        this.publisherReturns = publisherReturns;
    }

    public void setRequestedHeartbeat(Integer requestedHeartbeat) {
        this.requestedHeartbeat = requestedHeartbeat;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setVirtualHost(String virtualHost) {
        this.virtualHost = ("".equals(virtualHost) ? "/" : virtualHost);
    }

}
