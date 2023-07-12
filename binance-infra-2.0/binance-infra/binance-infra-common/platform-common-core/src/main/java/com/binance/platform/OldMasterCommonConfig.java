package com.binance.platform;

import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;

import org.apache.http.client.HttpClient;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.ssl.SSLContexts;
import org.javasimon.spring.MonitoredMeasuringPointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import com.binance.master.log.layout.MaskUtil;
import com.binance.master.utils.FileUtils;
import com.binance.master.utils.Geoip2Utils;
import com.binance.master.utils.IP2LocationUtils;
import com.binance.master.utils.WebUtils;
import com.binance.master.web.handlers.HandlerAdvice;
import com.binance.master.web.handlers.MessageHelper;
import com.ip2location.IP2Location;

/**
 * 禁止再做修改（代码不可控）
 * 
 * 
 * 
 * @author liushiming
 * @date 2020/03/04
 */
@Configuration
@ConditionalOnWebApplication
@ConditionalOnClass(name = "com.ip2location.IP2Location")
public class OldMasterCommonConfig {

    private static final Logger log = LoggerFactory.getLogger(OldMasterCommonConfig.class);

    @Autowired
    private ApplicationContext applicationContext;

    @Value("${ip2.data.base.path:/nas/ip2location/IP-COUNTRY-REGION-CITY.BIN}")
    private String ip2DatabasePath;

    @Value("${ip2.license.path:/nas/ip2location/license.key}")
    private String ip2LicensePath;

    @Value("${geoip2.data.base.path:/nas/geoip2/GeoIP2-City.mmdb}")
    private String geoip2DatabasePath;

    @Value("${ip2.memory.map.switch:false}")
    private boolean memoryMapped;

    @Value("${management.monitor.totalcost:2000}")
    private int costTimeThresholdMS;

    @PostConstruct
    private void init() {
        WebUtils.setApplicationContext(applicationContext);
        log.info("初始化 Ip2Location");
        log.info("初始化 Ip2Location ip2DatabasePath：{}, ip2LicensePath:{}, memoryMapped:{}", ip2DatabasePath,
            ip2LicensePath, memoryMapped);
        if (FileUtils.exists(ip2DatabasePath) && FileUtils.exists(ip2LicensePath)) {
            IP2Location ip2 = new IP2Location();
            ip2.IPDatabasePath = ip2DatabasePath;
            ip2.IPLicensePath = ip2LicensePath;
            ip2.UseMemoryMappedFile = memoryMapped;
            IP2LocationUtils.init(ip2);
            log.info("初始化 Ip2Location 成功！");
        } else {
            IP2LocationUtils.init(null);
            log.warn("初始化 Ip2Location 失败 配置路径不存在");
        }
        log.info("初始化 geoIp2");
        Geoip2Utils.init(geoip2DatabasePath);
        PathMatchingResourcePatternResolver pmrpr = new PathMatchingResourcePatternResolver();
        try {
            Resource[] resources = pmrpr.getResources("classpath*:log/log-mask-pattern.txt");
            MaskUtil.init(resources);
        } catch (Exception e) {
            log.warn("cannot load or parse the log-mask-pattern.txt file!");
        }
    }

    @Bean
    @ConditionalOnMissingBean
    public MessageHelper messageHelper() {
        return new MessageHelper();
    }

    @Bean
    @ConditionalOnMissingBean
    public HandlerAdvice handleAdvice() {
        return new HandlerAdvice();
    }

    @Bean(name = "monitoringAdvisor")
    @ConditionalOnMissingBean
    public DefaultPointcutAdvisor monitoringAdvisor() {
        DefaultPointcutAdvisor monitoringAdvisor = new DefaultPointcutAdvisor();
        monitoringAdvisor.setAdvice(new MonitoringInterceptor(costTimeThresholdMS));
        monitoringAdvisor.setPointcut(new MonitoredMeasuringPointcut());
        return monitoringAdvisor;
    }

    @Configuration
    @ConditionalOnProperty(value = "feign.httpclient.enabled", havingValue = "false")
    protected static class HttpClientNotEnableedConfiguration {

        @Value("${restTemplate.connectionTimeout:3000}")
        private int restTemplateConnectionTimeout;

        @Value("${restTemplate.readTimeout:10000}")
        private int restTemplateReadTimeout;

        @Bean(name = "platformHttpClient")
        public CloseableHttpClient platformHttpClient() {
            RegistryBuilder<ConnectionSocketFactory> registryBuilder =
                RegistryBuilder.<ConnectionSocketFactory>create();
            registryBuilder.register("http", PlainConnectionSocketFactory.getSocketFactory());
            try {
                KeyStore truststore = KeyStore.getInstance(KeyStore.getDefaultType());
                TrustStrategy trustStrategy = new TrustStrategy() {// 信任所有
                    @Override
                    public boolean isTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
                        return true;
                    }
                };
                SSLContext sslContext = SSLContexts.custom().loadTrustMaterial(truststore, trustStrategy).build();
                HostnameVerifier hostnameVerifier = new NoopHostnameVerifier();
                SSLConnectionSocketFactory connectionSocketFactory =
                    new SSLConnectionSocketFactory(sslContext, hostnameVerifier);
                registryBuilder.register("https", connectionSocketFactory);
            } catch (GeneralSecurityException e) {
                throw new RuntimeException(e);
            }
            Registry<ConnectionSocketFactory> registry = registryBuilder.build();
            PoolingHttpClientConnectionManager connManager =
                new PoolingHttpClientConnectionManager(registry, null, null, null, 10, TimeUnit.MINUTES);
            connManager.setDefaultMaxPerRoute(200);
            connManager.setMaxTotal(400);
            CloseableHttpClient httpClient =
                HttpClientBuilder.create().setConnectionManager(connManager).setConnectionManagerShared(true).build();
            return httpClient;
        }

        @Bean(name = "restTemplate")
        public RestTemplate platformRestTemplate(@Qualifier("platformHttpClient") CloseableHttpClient httpClient) {
            HttpComponentsClientHttpRequestFactory httpComponentsClientHttpRequestFactory =
                new HttpComponentsClientHttpRequestFactory(httpClient);
            httpComponentsClientHttpRequestFactory.setConnectTimeout(restTemplateConnectionTimeout);
            httpComponentsClientHttpRequestFactory.setReadTimeout(restTemplateReadTimeout);
            ClientHttpRequestFactory clientHttpRequestFactory =
                new BufferingClientHttpRequestFactory(httpComponentsClientHttpRequestFactory);
            RestTemplate restTemplate = new RestTemplate(clientHttpRequestFactory);
            return restTemplate;
        }
    }

    @Configuration
    @ConditionalOnProperty(value = "feign.httpclient.enabled", havingValue = "true")
    protected static class HttpClientHasEnabledConfiguration {

        @Value("${restTemplate.connectionTimeout:3000}")
        private int restTemplateConnectionTimeout;

        @Value("${restTemplate.readTimeout:10000}")
        private int restTemplateReadTimeout;

        @Bean
        public RestTemplate restTemplate() {
            RegistryBuilder<ConnectionSocketFactory> registryBuilder =
                RegistryBuilder.<ConnectionSocketFactory>create();
            registryBuilder.register("http", PlainConnectionSocketFactory.getSocketFactory());
            try {
                KeyStore truststore = KeyStore.getInstance(KeyStore.getDefaultType());
                TrustStrategy trustStrategy = new TrustStrategy() {// 信任所有
                    @Override
                    public boolean isTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
                        return true;
                    }
                };
                SSLContext sslContext = SSLContexts.custom().loadTrustMaterial(truststore, trustStrategy).build();
                HostnameVerifier hostnameVerifier = new NoopHostnameVerifier();
                SSLConnectionSocketFactory connectionSocketFactory =
                    new SSLConnectionSocketFactory(sslContext, hostnameVerifier);
                registryBuilder.register("https", connectionSocketFactory);
            } catch (GeneralSecurityException e) {
                throw new RuntimeException(e);
            }
            Registry<ConnectionSocketFactory> registry = registryBuilder.build();
            PoolingHttpClientConnectionManager connManager =
                new PoolingHttpClientConnectionManager(registry, null, null, null, 10, TimeUnit.MINUTES);
            connManager.setDefaultMaxPerRoute(200);
            connManager.setMaxTotal(400);
            HttpClient httpClient =
                HttpClientBuilder.create().setConnectionManager(connManager).setConnectionManagerShared(true).build();
            HttpComponentsClientHttpRequestFactory httpComponentsClientHttpRequestFactory =
                new HttpComponentsClientHttpRequestFactory(httpClient);
            httpComponentsClientHttpRequestFactory.setConnectTimeout(restTemplateConnectionTimeout);
            httpComponentsClientHttpRequestFactory.setReadTimeout(restTemplateReadTimeout);
            ClientHttpRequestFactory clientHttpRequestFactory =
                new BufferingClientHttpRequestFactory(httpComponentsClientHttpRequestFactory);
            RestTemplate restTemplate = new RestTemplate(clientHttpRequestFactory);
            return restTemplate;
        }
    }

}
