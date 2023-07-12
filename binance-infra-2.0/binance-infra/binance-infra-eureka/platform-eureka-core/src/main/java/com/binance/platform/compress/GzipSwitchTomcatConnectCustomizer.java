package com.binance.platform.compress;

import com.google.common.base.Joiner;
import org.apache.catalina.connector.Connector;
import org.apache.coyote.ProtocolHandler;
import org.apache.coyote.UpgradeProtocol;
import org.apache.coyote.http11.AbstractHttp11Protocol;
import org.apache.coyote.http2.Http2Protocol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.web.embedded.tomcat.TomcatConnectorCustomizer;
import org.springframework.boot.web.server.Compression;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.util.unit.DataSize;

import javax.annotation.PostConstruct;

/**
 * Tomcat gzip compression switch
 *
 * @author Thomas Li
 * Date: 2021/1/11 2:23 下午
 */
public class GzipSwitchTomcatConnectCustomizer implements TomcatConnectorCustomizer {
    private static final Logger log = LoggerFactory.getLogger(GzipSwitchTomcatConnectCustomizer.class);
    /**
     * MIME_TYPES can be compressed
     */
    public static final String[] COMPRESSIBLE_MIME_TYPES = new String[]{MediaType.APPLICATION_JSON_VALUE};

    /**
     * MIME_TYPES can be compressed
     */
    public static final String COMPRESSIBLE_MIME_TYPE = Joiner.on(",").join(COMPRESSIBLE_MIME_TYPES);

    @Autowired
    private ServerProperties serverProperties;
    private Compression compression;
    private Connector connector;

    private boolean started = false;

    @Override
    public void customize(Connector connector) {
        this.connector = connector;
        refreshCompressionConfig();
    }

    @Value("${server.compression.enabled:false}")
    public void compressEnableChanged(String enable) {
        if (started) {
            Boolean isEnable = Boolean.valueOf(enable);
            log.info("tomcat compress enabled is changed to {}", isEnable);
            compression.setEnabled(isEnable);
            refreshCompressionConfig();
        }
    }

    @Value("${server.compression.min-response-size:2KB}")
    public void compressMinResponseSize(String minResponseSize) {
        if (started) {
            try {
                DataSize minSize = DataSize.parse(minResponseSize);
                compression.setMinResponseSize(minSize);
                refreshCompressionConfig();
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }
    }

    @PostConstruct
    public void init() {
        compression = serverProperties.getCompression();
        if (compression == null) {
            compression = new Compression();
            compression.setMimeTypes(COMPRESSIBLE_MIME_TYPES);
            compression.setEnabled(false);
        }
        log.info("Tomcat compress enable is {}", compression.getEnabled());

        started = true;
    }

    private void refreshCompressionConfig() {
        if (compression == null || !compression.getEnabled()) {
            disable();
        } else {
            enable();
        }
    }

    /**
     * Enable gzip
     */
    private void enable() {
        ProtocolHandler handler = connector.getProtocolHandler();
        if (handler instanceof AbstractHttp11Protocol) {
            enable((AbstractHttp11Protocol<?>) handler);
        }
        for (UpgradeProtocol upgradeProtocol : connector.findUpgradeProtocols()) {
            if (upgradeProtocol instanceof Http2Protocol) {
                enable((Http2Protocol) upgradeProtocol);
            }
        }
    }

    private void enable(AbstractHttp11Protocol<?> protocol) {
        Compression compression = this.compression;
        protocol.setCompression("on");
        protocol.setCompressionMinSize(getMinResponseSize(compression));
        protocol.setCompressibleMimeType(getMimeTypes(compression));
        if (this.compression.getExcludedUserAgents() != null) {
            protocol.setNoCompressionUserAgents(getExcludedUserAgents());
        }
    }

    private void enable(Http2Protocol protocol) {
        Compression compression = this.compression;
        protocol.setCompression("on");
        protocol.setCompressionMinSize(getMinResponseSize(compression));
        protocol.setCompressibleMimeType(getMimeTypes(compression));
        if (this.compression.getExcludedUserAgents() != null) {
            protocol.setNoCompressionUserAgents(getExcludedUserAgents());
        }
    }

    /**
     * Disable gzip
     */
    private void disable() {
        ProtocolHandler handler = connector.getProtocolHandler();
        if (handler instanceof AbstractHttp11Protocol) {
            ((AbstractHttp11Protocol<?>) handler).setCompression("off");
        }
        for (UpgradeProtocol upgradeProtocol : connector.findUpgradeProtocols()) {
            if (upgradeProtocol instanceof Http2Protocol) {
                ((Http2Protocol) upgradeProtocol).setCompression("off");
            }
        }
    }

    private int getMinResponseSize(Compression compression) {
        return (int) compression.getMinResponseSize().toBytes();
    }

    private String getMimeTypes(Compression compression) {
        return StringUtils.arrayToCommaDelimitedString(compression.getMimeTypes());
    }

    private String getExcludedUserAgents() {
        return StringUtils.arrayToCommaDelimitedString(this.compression.getExcludedUserAgents());
    }
}
