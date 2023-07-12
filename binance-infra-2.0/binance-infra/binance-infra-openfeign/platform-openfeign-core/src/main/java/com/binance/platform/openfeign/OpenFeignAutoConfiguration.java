package com.binance.platform.openfeign;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.validation.Valid;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.jetty.client.HttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.netflix.ribbon.SpringClientFactory;
import org.springframework.cloud.openfeign.AnnotatedParameterProcessor;
import org.springframework.cloud.openfeign.ribbon.CachingSpringLoadBalancerFactory;
import org.springframework.cloud.openfeign.ribbon.LoadBalancerFeignClient;
import org.springframework.cloud.openfeign.support.FeignHttpClientProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.annotation.SynthesizingMethodParameter;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.env.Environment;
import org.springframework.format.support.DefaultFormattingConversionService;
import org.springframework.format.support.FormattingConversionService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.method.annotation.RequestHeaderMethodArgumentResolver;
import org.springframework.web.method.annotation.RequestParamMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.mvc.method.annotation.PathVariableMethodArgumentResolver;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestResponseBodyMethodProcessor;
import org.springframework.web.servlet.mvc.method.annotation.ServletCookieValueMethodArgumentResolver;

import com.binance.platform.openfeign.compress.GzipProperties;
import com.binance.platform.openfeign.http2.jetty.JettyHttpClient;
import com.binance.platform.openfeign.http2.jetty.JettyHttpClientFactory;
import com.fasterxml.jackson.databind.ObjectMapper;

import feign.Client;
import feign.Feign;
import feign.FeignClientMethodMetadataParseHandler;
import feign.Response;
import feign.codec.ErrorDecoder;

@Configuration
@ConditionalOnClass(Feign.class)
public class OpenFeignAutoConfiguration {

    private static final Logger LOGGER = LoggerFactory.getLogger(OpenFeignAutoConfiguration.class);

    private ErrorDecoder fallbackErrorDecoder = new ErrorDecoder.Default();

    @SuppressWarnings("unchecked")
    public static MethodParameter interfaceMethodParameter(MethodParameter parameter,
        @SuppressWarnings("rawtypes") Class annotationType) {
        if (!parameter.hasParameterAnnotation(annotationType)) {
            for (Class<?> itf : parameter.getDeclaringClass().getInterfaces()) {
                try {
                    Method method =
                        itf.getMethod(parameter.getMethod().getName(), parameter.getMethod().getParameterTypes());
                    MethodParameter itfParameter = new InterfaceMethodParameter(method, parameter.getParameterIndex());
                    if (itfParameter.hasParameterAnnotation(annotationType)) {
                        return itfParameter;
                    }
                } catch (NoSuchMethodException e) {
                    continue;
                }
            }
        }
        return parameter;
    }

    private static class InterfaceMethodParameter extends SynthesizingMethodParameter {

        private volatile Annotation[] parameterAnnotations;

        private Method interfaceMethod;

        public InterfaceMethodParameter(Method interfaceMethod, int parameterIndex) {
            super(interfaceMethod, parameterIndex);
            this.interfaceMethod = interfaceMethod;
        }

        @Override
        public Annotation[] getParameterAnnotations() {
            Annotation[][] annotationArray = this.interfaceMethod.getParameterAnnotations();
            if (this.getParameterIndex() >= 0 && this.getParameterIndex() < annotationArray.length) {
                this.parameterAnnotations = annotationArray[this.getParameterIndex()];
                for (int i = 0; i < this.parameterAnnotations.length; i++) {
                    this.parameterAnnotations[i] =
                        AnnotationUtils.synthesizeAnnotation(this.parameterAnnotations[i], null);
                }
            } else {
                this.parameterAnnotations = new Annotation[0];
            }
            return this.parameterAnnotations;
        }

        @Override
        public boolean equals(Object other) {
            return super.equals(other);
        }

        @Override
        public int hashCode() {
            return super.hashCode();
        }

    }

    @Autowired
    private RequestMappingHandlerAdapter adapter;

    @Autowired
    private ConfigurableBeanFactory beanFactory;

    @Bean
    public OpenFeignBeanFactoryPostProcessor feignBeanFactoryPostProcessor() {
        return new OpenFeignBeanFactoryPostProcessor();
    }

    @Bean
    public OpenFeignRequestInterceptor feignRequestInterceptor() {
        return new OpenFeignRequestInterceptor();
    }

    @Bean
    public OpenFeignSpringMvcContract feignSpringMvcContract(
        @Autowired(required = false) List<AnnotatedParameterProcessor> parameterProcessors,
        @Autowired(required = false) List<FeignClientMethodMetadataParseHandler> parseMethodMetadataHandlers,
        List<ConversionService> conversionServices) {
        if (conversionServices.size() == 0) {
            throw new IllegalStateException("ConversionService can not be NULL");
        }
        ConversionService conversionService = null;
        if (conversionServices.size() == 1) {
            conversionService = conversionServices.get(0);
        } else {
            // 如果有多个实例，优先使用找到的第一个DefaultFormattingConversionService，如果没有，则使用FormattingConversionService
            conversionService = conversionServices.stream().filter(c -> c instanceof DefaultFormattingConversionService)
                .findFirst().orElseGet(() -> conversionServices.stream()
                    .filter(c -> c instanceof FormattingConversionService).findFirst().get());
        }
        if (null == parameterProcessors) {
            parameterProcessors = new ArrayList<>();
        }
        OpenFeignSpringMvcContract contract = new OpenFeignSpringMvcContract(parameterProcessors, conversionService);
        if (parseMethodMetadataHandlers != null) {
            contract.setParseMethodMetadataHandlers(parseMethodMetadataHandlers);
        }
        return contract;
    }

    @Bean
    public ErrorDecoder feignErrorDecoder(ObjectMapper objectMapper) {

        return new ErrorDecoder() {

            @Override
            public Exception decode(String methodKey, Response response) {
                HttpHeaders responseHeaders = new HttpHeaders();
                response.headers().entrySet().stream()
                    .forEach(entry -> responseHeaders.put(entry.getKey(), new ArrayList<>(entry.getValue())));
                HttpStatus statusCode = HttpStatus.valueOf(response.status());
                String statusText = response.reason();
                byte[] responseBody = StringUtils.EMPTY.getBytes();
                try {
                    responseBody = IOUtils.toByteArray(response.body().asInputStream());
                } catch (IOException e) {
                    throw new RuntimeException("Failed to process response body.", e);
                }
                if (statusCode.isError()) {
                    LOGGER.error(
                        "OpenFeign call return error; methodKey:{},statusCode:{},statusText{},responseHeaders:{},responseBody:{}",
                        methodKey, statusCode, statusText, responseHeaders,
                        new String(responseBody, Charset.forName("UTF-8")));
                }
                if (response.status() >= 400 && response.status() <= 499) {
                    return HttpClientErrorException.create(statusCode, statusText, responseHeaders, responseBody,
                        Charset.forName("UTF-8"));
                }
                if (response.status() >= 500 && response.status() <= 599) {
                    return HttpServerErrorException.create(statusCode, statusText, responseHeaders, responseBody,
                        Charset.forName("UTF-8"));
                }
                return fallbackErrorDecoder.decode(methodKey, response);
            }

        };

    }

    @PostConstruct
    public void modifyArgumentResolvers() {
        List<HandlerMethodArgumentResolver> list = new ArrayList<>(adapter.getArgumentResolvers());

        list.add(0, new RequestParamMethodArgumentResolver(true) {
            @Override
            protected NamedValueInfo createNamedValueInfo(MethodParameter parameter) {
                return super.createNamedValueInfo(interfaceMethodParameter(parameter, RequestParam.class));
            }

            @Override
            public boolean supportsParameter(MethodParameter parameter) {
                return super.supportsParameter(interfaceMethodParameter(parameter, RequestParam.class));
            }
        });

        list.add(0, new PathVariableMethodArgumentResolver() {
            @Override
            protected NamedValueInfo createNamedValueInfo(MethodParameter parameter) {
                return super.createNamedValueInfo(interfaceMethodParameter(parameter, PathVariable.class));
            }

            @Override
            public boolean supportsParameter(MethodParameter parameter) {
                return super.supportsParameter(interfaceMethodParameter(parameter, PathVariable.class));
            }
        });

        list.add(0, new RequestHeaderMethodArgumentResolver(beanFactory) {
            @Override
            protected NamedValueInfo createNamedValueInfo(MethodParameter parameter) {
                return super.createNamedValueInfo(interfaceMethodParameter(parameter, RequestHeader.class));
            }

            @Override
            public boolean supportsParameter(MethodParameter parameter) {
                return super.supportsParameter(interfaceMethodParameter(parameter, RequestHeader.class));
            }
        });

        list.add(0, new ServletCookieValueMethodArgumentResolver(beanFactory) {
            @Override
            protected NamedValueInfo createNamedValueInfo(MethodParameter parameter) {
                return super.createNamedValueInfo(interfaceMethodParameter(parameter, CookieValue.class));
            }

            @Override
            public boolean supportsParameter(MethodParameter parameter) {
                return super.supportsParameter(interfaceMethodParameter(parameter, CookieValue.class));
            }
        });

        list.add(0, new RequestResponseBodyMethodProcessor(adapter.getMessageConverters()) {
            @Override
            public boolean supportsParameter(MethodParameter parameter) {
                return super.supportsParameter(interfaceMethodParameter(parameter, RequestBody.class));
            }

            @Override
            protected void validateIfApplicable(WebDataBinder binder, MethodParameter methodParam) {
                super.validateIfApplicable(binder, interfaceMethodParameter(methodParam, Valid.class));
            }
        });

        List<HandlerMethodArgumentResolver> customResolvers = adapter.getCustomArgumentResolvers();
        list.removeAll(customResolvers);
        list.addAll(0, customResolvers);
        adapter.setArgumentResolvers(list);
    }

    @Bean
    public GzipProperties gzipProperties() {
        return new GzipProperties();
    }

    @Configuration
    @ConditionalOnClass(HttpClient.class)
    @ConditionalOnProperty(value = "feign.jetty.enable", havingValue = "true")
    public static class JettyHttpFeignClientConfiguration {

        @Bean
        @ConditionalOnMissingBean(JettyHttpClientFactory.class)
        public JettyHttpClientFactory jettyHttpClientHolder(FeignHttpClientProperties feignHttpClientProperties,
            Environment environment) {
            return new JettyHttpClientFactory(feignHttpClientProperties, environment);
        }

        @Bean
        @ConditionalOnBean(JettyHttpClientFactory.class)
        public Client feignClient(CachingSpringLoadBalancerFactory cachingFactory, SpringClientFactory clientFactory,
            JettyHttpClientFactory jettyHttpClientHolder) {
            LOGGER.info("jetty feign http client is enabled");
            JettyHttpClient jettyHttpClient = jettyHttpClientHolder.getJettyHttpClient();
            return new LoadBalancerFeignClient(jettyHttpClient, cachingFactory, clientFactory);
        }
    }
}
