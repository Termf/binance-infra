package com.binance.platform.openfeign;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.ParseException;
import java.text.ParsePosition;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.TimeZone;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.FastDateFormat;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessorAdapter;
import org.springframework.cloud.openfeign.encoding.FeignAcceptGzipEncodingInterceptor;
import org.springframework.cloud.openfeign.encoding.FeignClientEncodingProperties;
import org.springframework.cloud.openfeign.ribbon.CachingSpringLoadBalancerFactory;
import org.springframework.cloud.openfeign.ribbon.LoadBalancerFeignClient;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;
import org.springframework.web.util.WebUtils;

import com.binance.platform.openfeign.compress.GzipFeignAcceptGzipEncodingInterceptor;
import com.binance.platform.openfeign.jackson.LongSerializer;
import com.binance.platform.openfeign.jackson.MyBigDecimalDeserializer;
import com.binance.platform.openfeign.jackson.MyBigDecimalSerializer;
import com.binance.platform.openfeign.jackson.MySimpleModule;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.deser.BeanDeserializerFactory;
import com.fasterxml.jackson.databind.deser.BeanDeserializerModifier;
import com.fasterxml.jackson.databind.deser.DeserializationProblemHandler;
import com.fasterxml.jackson.databind.deser.DeserializerFactory;
import com.fasterxml.jackson.databind.deser.Deserializers;
import com.fasterxml.jackson.databind.module.SimpleDeserializers;
import com.fasterxml.jackson.databind.module.SimpleSerializers;
import com.fasterxml.jackson.databind.ser.BeanSerializerFactory;
import com.fasterxml.jackson.databind.ser.SerializerFactory;
import com.fasterxml.jackson.databind.ser.Serializers;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.databind.type.ClassKey;
import com.fasterxml.jackson.databind.util.StdDateFormat;
import com.vip.vjtools.vjkit.time.DateFormatUtil;

public class FeignApplicationContextInitializer
    implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    private static final Logger LOGGER = LoggerFactory.getLogger(FeignApplicationContextInitializer.class);

    /**
     * 修改获取连接池的超时时间，默认是1s
     */
    private static void modifyCloseableHttpClientRequestConfigConnectionRequestTimeout(Object closeableHttpClient) {
        try {
            Field field = ReflectionUtils.findField(closeableHttpClient.getClass(), "defaultConfig");
            ReflectionUtils.makeAccessible(field);
            RequestConfig requestConfig = (RequestConfig)ReflectionUtils.getField(field, closeableHttpClient);
            Field fieldConnectionRequestTimeout =
                ReflectionUtils.findField(RequestConfig.class, "connectionRequestTimeout");
            ReflectionUtils.makeAccessible(fieldConnectionRequestTimeout);
            Field modifiersField = Field.class.getDeclaredField("modifiers");
            ReflectionUtils.makeAccessible(modifiersField);
            modifiersField.setInt(fieldConnectionRequestTimeout,
                fieldConnectionRequestTimeout.getModifiers() & ~Modifier.FINAL);
            ReflectionUtils.setField(fieldConnectionRequestTimeout, requestConfig, 1000);
        } catch (Throwable e) {
            LOGGER.warn(e.getMessage());
        }
    }

    /**
     * 修改Okhttp让Interceptors变成可写
     * 
     * @param okHttpClient
     */
    // private static void modifyOkHttpClientInterceptors(Object okHttpClient) {
    // try {
    // Field field = ReflectionUtils.findField(okHttpClient.getClass(), "interceptors");
    // ReflectionUtils.makeAccessible(field);
    // List<Interceptor> list = (List<Interceptor>)ReflectionUtils.getField(field, okHttpClient);
    // List<Interceptor> newList = Lists.newArrayList();
    // if (list != null) {
    // newList.addAll(list);
    // }
    // Field modifiersField = Field.class.getDeclaredField("modifiers");
    // ReflectionUtils.makeAccessible(modifiersField);
    // modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
    // ReflectionUtils.setField(field, okHttpClient, newList);
    // } catch (Throwable e) {
    // LOGGER.warn(e.getMessage());
    // }
    // }

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        applicationContext.getBeanFactory().addBeanPostProcessor(new InstantiationAwareBeanPostProcessorAdapter() {

            @Override
            public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
                if (bean instanceof LoadBalancerFeignClient) {
                    LoadBalancerFeignClient feignClient = (LoadBalancerFeignClient)bean;
                    LoadBalancerFeignClient wrappedFeignClient =
                        new ReWriteHeaderFeignClient(feignClient, applicationContext);
                    return wrappedFeignClient;
                }
                if (bean instanceof CloseableHttpClient) {
                    modifyCloseableHttpClientRequestConfigConnectionRequestTimeout(bean);
                }
                // if (bean instanceof okhttp3.OkHttpClient) {
                // okhttp3.OkHttpClient http1OkHttpClient = (okhttp3.OkHttpClient)bean;
                // modifyOkHttpClientInterceptors(http1OkHttpClient);
                // /**
                // * okhttp 对http2协议支持不是很好，容易出现 stream was reset: CANCEL
                // *
                // * https://github.com/square/okhttp/issues/6608
                // *
                // * @TODO 需要进一步查一下okhttp的原因，目前用jetty替代okhttp
                // */
                // // FeignHttpClientProperties feignHttpClientProperties =
                // // applicationContext.getBean(FeignHttpClientProperties.class);
                // // Environment env = applicationContext.getBean(Environment.class);
                // // http1OkHttpClient.interceptors()
                // // .add(new com.binance.platform.openfeign.http2.okhttp.H2CUpgradeRequestInterceptor(
                // // feignHttpClientProperties, env));
                // }
                if (bean instanceof FeignAcceptGzipEncodingInterceptor) {
                    return new GzipFeignAcceptGzipEncodingInterceptor(new FeignClientEncodingProperties());
                }

                if (bean instanceof CachingSpringLoadBalancerFactory) {
                    CachingSpringLoadBalancerFactory cachingSpringLoadBalancerFactory =
                        (CachingSpringLoadBalancerFactory)bean;
                    return new EncodeHeaderCachingSpringLoadBalancerFactory(cachingSpringLoadBalancerFactory);
                }
                /**
                 * @see https://stackoverflow.com/questions/25889925/apache-poolinghttpclientconnectionmanager-throwing-illegal-state-exception
                 *      https://my.oschina.net/xlj44400/blog/711341
                 */
                if (bean instanceof HttpClientBuilder) {
                    HttpClientBuilder httpClientBuilder = (HttpClientBuilder)bean;
                    httpClientBuilder.setConnectionManagerShared(true);
                    return bean;
                }
                if (bean instanceof ObjectMapper) {
                    ObjectMapper objectMapper = (ObjectMapper)bean;
                    objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                    objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
                    objectMapper.configure(MapperFeature.USE_GETTERS_AS_SETTERS, false);
                    // 不解析含有另外注释符
                    objectMapper.configure(JsonParser.Feature.ALLOW_COMMENTS, true);
                    // 不解析含有结束语的字符
                    objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
                    // 允许带有单引号
                    objectMapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
                    // 允许带有换行符
                    objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
                    objectMapper.configure(JsonParser.Feature.ALLOW_MISSING_VALUES, true);
                    objectMapper.configure(JsonParser.Feature.ALLOW_TRAILING_COMMA, true);
                    // 含有反斜杠的转义字符
                    objectMapper.configure(JsonParser.Feature.ALLOW_BACKSLASH_ESCAPING_ANY_CHARACTER, true);
                    // 时间反序列化支持yyy-MM-dd HH:mm:ss，yyyy-MM-dd'T'HH:mm:ss.SSSZ，yyyy-MM-dd，EEE, dd MMM
                    // yyyy HH:mm:ss zzz
                    objectMapper.setDateFormat(new DateFormatWrapper(objectMapper.getDateFormat()));
                    // 时间序列化都转化为TimeStamps出去，出去的时间格式都是long
                    objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, true);
                    // 忽略大小写
                    objectMapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);

                    objectMapper.enable(MapperFeature.USE_ANNOTATIONS);
                    objectMapper.enable(JsonGenerator.Feature.WRITE_BIGDECIMAL_AS_PLAIN);

                    objectMapper.addHandler(new DeserializationProblemHandler() {
                        public Object handleUnexpectedToken(DeserializationContext ctxt, Class<?> targetType,
                            JsonToken t, JsonParser jp, String failureMsg) throws IOException {
                            if (t == JsonToken.START_OBJECT) {
                                ObjectMapper mapper = (ObjectMapper)jp.getCodec();
                                JsonNode node = mapper.readTree(jp);
                                String value = mapper.writeValueAsString(node);
                                return value;
                            }
                            return NOT_HANDLED;
                        }
                    });
                    MySimpleModule module = new MySimpleModule();
                    module.setDeserializerModifier(new BeanDeserializerModifier() {
                        @Override
                        public JsonDeserializer<Enum> modifyEnumDeserializer(DeserializationConfig config,
                            final JavaType type, BeanDescription beanDesc, final JsonDeserializer<?> deserializer) {
                            return new JsonDeserializer<Enum>() {
                                @Override
                                public Enum deserialize(JsonParser jsonparser, DeserializationContext ctxt)
                                    throws IOException {
                                    Class<? extends Enum> rawClass = (Class<Enum<?>>)type.getRawClass();
                                    Enum myenum = null;
                                    /**
                                     * <pre>
                                     * 首先不分辨大写转化一下，看看转化是否成功
                                     * 其次再转化为大写转化下，看看是否成功
                                     * </pre>
                                     */
                                    try {
                                        myenum = Enum.valueOf(rawClass, jsonparser.getValueAsString());
                                    } catch (Throwable e) {
                                        if (StringUtils.equalsIgnoreCase(rawClass.getName(),
                                            "com.binance.master.enums.LanguageEnum")) {
                                            myenum = com.binance.master.enums.LanguageEnum.EN_US;
                                        } else {
                                            myenum =
                                                Enum.valueOf(rawClass, jsonparser.getValueAsString().toUpperCase());
                                        }
                                    }
                                    return myenum;
                                }
                            };
                        }
                    });
                    module.addSerializer(Enum.class, new StdSerializer<Enum>(Enum.class) {
                        @Override
                        public void serialize(Enum value, JsonGenerator jgen, SerializerProvider provider)
                            throws IOException {
                            jgen.writeString(value.name());
                        }
                    });
                    // 判断是否含有BigDecimalSerializer
                    boolean hasBigDecimalSerializer = hasBigDecimalSerializer(objectMapper);
                    if (!hasBigDecimalSerializer) {
                        // 对于bigdecimal的序列化及反序列化
                        MyBigDecimalSerializer instance = MyBigDecimalSerializer.instance;
                        instance.setConfigurableApplicationContext(applicationContext);
                        module.addSerializer(BigDecimal.class, instance);
                    }
                    // 对科学计数法反序列化进行校验
                    // 判断是否含有BigDecimalDeserializer
                    boolean hasBigDecimalDeserializer = hasBigDecimalDeserializer(objectMapper);
                    if (!hasBigDecimalDeserializer) {
                        module.addDeserializer(BigDecimal.class, MyBigDecimalDeserializer.instance);
                    }
                    LongSerializer longSerializerInstance = LongSerializer.instance;
                    longSerializerInstance.setConfigurableApplicationContext(applicationContext);
                    module.addSerializer(Long.class, longSerializerInstance);
                    // 对于bigdecimal的序列化及反序列化
                    objectMapper.registerModule(module);
                    return bean;
                }
                return bean;
            }

        });
    }

    private static boolean hasBigDecimalDeserializer(ObjectMapper objectMapper) {
        boolean hasBigDecimalSerializer = false;
        try {
            DeserializationContext deserializationContext = objectMapper.getDeserializationContext();
            DeserializerFactory deserializerFactory = deserializationContext.getFactory();
            if (deserializerFactory != null && deserializerFactory instanceof BeanDeserializerFactory) {
                BeanDeserializerFactory beanDeserializerFactory = (BeanDeserializerFactory)deserializerFactory;
                Iterable<Deserializers> iterable = beanDeserializerFactory.getFactoryConfig().deserializers();
                Iterator<Deserializers> it = iterable.iterator();
                while (it.hasNext()) {
                    Deserializers deserializers = it.next();
                    if (deserializers instanceof SimpleDeserializers) {
                        SimpleDeserializers simpleSerializers = (SimpleDeserializers)deserializers;
                        Field field = ReflectionUtils.findField(SimpleDeserializers.class, "_classMappings");
                        ReflectionUtils.makeAccessible(field);
                        HashMap<ClassKey, JsonDeserializer<?>> _classMappings =
                            (HashMap<ClassKey, JsonDeserializer<?>>)ReflectionUtils.getField(field, simpleSerializers);
                        ClassKey key = new ClassKey(BigDecimal.class);
                        if (_classMappings != null) {
                            JsonDeserializer<?> ser = _classMappings.get(key);
                            if (ser != null) {
                                hasBigDecimalSerializer = true;
                            }
                        }

                    }
                }
            }
            return hasBigDecimalSerializer;
        } catch (Throwable e) {
            return hasBigDecimalSerializer;
        }
    }

    /**
     * 非常丑陋的代码，如果应用有自定义的BigDecimalSerializer，就不添加BigDecimalSerializer了
     */
    private static boolean hasBigDecimalSerializer(ObjectMapper objectMapper) {
        boolean hasBigDecimalSerializer = false;
        try {
            SerializerFactory serializerFactory = objectMapper.getSerializerFactory();
            if (serializerFactory != null && serializerFactory instanceof BeanSerializerFactory) {
                BeanSerializerFactory beanSerializerFactory = (BeanSerializerFactory)serializerFactory;
                Iterable<Serializers> iterable = beanSerializerFactory.getFactoryConfig().serializers();
                Iterator<Serializers> it = iterable.iterator();
                while (it.hasNext()) {
                    Serializers serializers = it.next();
                    if (serializers instanceof SimpleSerializers) {
                        SimpleSerializers simpleSerializers = (SimpleSerializers)serializers;
                        Field field = ReflectionUtils.findField(SimpleSerializers.class, "_classMappings");
                        ReflectionUtils.makeAccessible(field);
                        HashMap<ClassKey, JsonSerializer<?>> _classMappings =
                            (HashMap<ClassKey, JsonSerializer<?>>)ReflectionUtils.getField(field, simpleSerializers);
                        ClassKey key = new ClassKey(BigDecimal.class);
                        if (_classMappings != null) {
                            JsonSerializer<?> ser = _classMappings.get(key);
                            if (ser != null) {
                                hasBigDecimalSerializer = true;
                            }
                        }

                    }
                }
            }
            return hasBigDecimalSerializer;
        } catch (Throwable e) {
            return hasBigDecimalSerializer;
        }
    }

    private static TimeZone getTimeZoneByRequest() {
        try {
            ServletRequestAttributes requestAttributes =
                (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
            if (requestAttributes != null && requestAttributes.getRequest() != null) {
                TimeZone timezone = (TimeZone)WebUtils.getSessionAttribute(requestAttributes.getRequest(),
                    SessionLocaleResolver.TIME_ZONE_SESSION_ATTRIBUTE_NAME);
                if (timezone == null) {
                    timezone = TimeZone.getDefault();
                }
                return timezone;
            } else {
                return TimeZone.getDefault();
            }
        } catch (Exception e) {
            return TimeZone.getDefault();
        }
    }

    public static class DateFormatWrapper extends DateFormat {

        private static final long serialVersionUID = 1L;

        private DateFormat sourceDateFormat;

        public DateFormatWrapper(DateFormat dateFormat) {
            this.sourceDateFormat = dateFormat;
        }

        @Override
        public StringBuffer format(Date date, StringBuffer toAppendTo, FieldPosition fieldPosition) {
            StringBuffer dateStr = null;
            try {
                dateStr = FastDateFormat.getInstance(DateFormatUtil.PATTERN_DEFAULT_ON_SECOND, getTimeZoneByRequest())
                    .format(date, toAppendTo, fieldPosition);
            } catch (Throwable e1) {
                try {
                    dateStr = StdDateFormat.instance.withTimeZone(getTimeZoneByRequest()).format(date, toAppendTo,
                        fieldPosition);
                } catch (Throwable e2) {
                    sourceDateFormat.setTimeZone(getTimeZoneByRequest());
                    dateStr = sourceDateFormat.format(date, toAppendTo, fieldPosition);
                }
            }
            return dateStr;

        }

        @Override
        public Date parse(String source, ParsePosition pos) {
            Date date = null;
            try {
                date = FastDateFormat.getInstance(DateFormatUtil.PATTERN_DEFAULT_ON_SECOND, getTimeZoneByRequest())
                    .parse(source, pos);
            } catch (Throwable e1) {
                try {
                    date = StdDateFormat.instance.withTimeZone(getTimeZoneByRequest()).parse(source, pos);
                } catch (Throwable e2) {
                    sourceDateFormat.setTimeZone(getTimeZoneByRequest());
                    date = sourceDateFormat.parse(source, pos);
                }
            }
            return date;
        }

        @Override
        public Date parse(String source) throws ParseException {
            Date date = null;
            try {
                date = FastDateFormat.getInstance(DateFormatUtil.PATTERN_DEFAULT_ON_SECOND, getTimeZoneByRequest())
                    .parse(source);
            } catch (Throwable e1) {
                try {
                    date = StdDateFormat.instance.withTimeZone(getTimeZoneByRequest()).parse(source);
                } catch (Throwable e2) {
                    sourceDateFormat.setTimeZone(getTimeZoneByRequest());
                    date = sourceDateFormat.parse(source);
                }
            }
            return date;
        }

        @Override
        public Object clone() {
            Object format = sourceDateFormat.clone();
            return new DateFormatWrapper((DateFormat)format);
        }

    }

}