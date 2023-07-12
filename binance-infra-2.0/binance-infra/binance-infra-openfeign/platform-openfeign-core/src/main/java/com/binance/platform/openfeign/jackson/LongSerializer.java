package com.binance.platform.openfeign.jackson;

import java.io.IOException;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.std.NumberSerializer;

public class LongSerializer extends JsonSerializer<Long> implements ContextualSerializer {

    public final static LongSerializer instance = new LongSerializer();

    private Long2String ann;

    private ConfigurableApplicationContext applicationContext;

    public void setAnn(Long2String ann) {
        this.ann = ann;
    }

    public void setConfigurableApplicationContext(ConfigurableApplicationContext context) {
        this.applicationContext = context;
    }

    @Override
    public JsonSerializer<?> createContextual(SerializerProvider provider, BeanProperty property)
        throws JsonMappingException {
        // jackson自有的注解
        if (property != null) {
            // 如果打了注解
            Long2String ann = property.getAnnotation(Long2String.class);
            if (ann == null) {
                ann = property.getContextAnnotation(Long2String.class);
            }
            if (ann != null) {
                LongSerializer jsonSerializer = new LongSerializer();
                jsonSerializer.setAnn(ann);
                return jsonSerializer;
            }
        }
        // 从main入口函数获取
        Map<String, Object> applicationClass = applicationContext.getBeansWithAnnotation(SpringBootApplication.class);
        for (Object obj : applicationClass.values()) {
            Long2String ann = AnnotationUtils.findAnnotation(obj.getClass(), Long2String.class);
            if (ann != null) {
                LongSerializer jsonSerializer = new LongSerializer();
                jsonSerializer.setAnn(ann);
                return jsonSerializer;
            }
        }
        // 否则使用默认的
        return NumberSerializer.instance;

    }

    @Override
    public void serialize(Long value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
        if (ann != null) {
            String clientType = ann.clientType();
            if (clientType != null) {
                String deviceType = calculateDeviceType();
                // 如果设置仅为手机端转化
                if (StringUtils.equalsIgnoreCase(clientType, "mobile")) {
                    // 如果是手机端，则转化
                    if (deviceType != null && (StringUtils.containsIgnoreCase(deviceType, "Android")
                        || StringUtils.containsIgnoreCase(deviceType, "IOS"))) {
                        if (value != null) {
                            jgen.writeString(value.toString());
                        } else {
                            jgen.writeString("0");
                        }
                    }
                    // 如果是PC端，则不转化
                    else {
                        jgen.writeNumber(value);
                    }
                }
                // 如果设置的仅为PC端转化
                else if (StringUtils.equalsIgnoreCase(clientType, "pc")) {
                    // 如果是手机端，则不转化
                    if (deviceType != null && (StringUtils.containsIgnoreCase(deviceType, "Android")
                        || StringUtils.containsIgnoreCase(deviceType, "IOS"))) {
                        jgen.writeNumber(value);
                    }
                    // 如果是PC端则转化
                    else {
                        if (value != null) {
                            jgen.writeString(value.toString());
                        } else {
                            jgen.writeString("0");
                        }
                    }
                }
                // 如果设置的全部转化
                else if (StringUtils.equalsIgnoreCase(clientType, "all")) {
                    if (value != null) {
                        jgen.writeString(value.toString());
                    } else {
                        jgen.writeString("0");
                    }
                }
                // 其他则不转化
                else {
                    jgen.writeNumber(value);
                }
            }
            // clientType为空不转化
            else {
                jgen.writeNumber(value);
            }
        }
        // 没打注解不转化
        else {
            jgen.writeNumber(value);
        }

    }

    private String calculateDeviceType() {
        ServletRequestAttributes requestAttributes =
            (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
        if (requestAttributes != null && requestAttributes.getRequest() != null) {
            String clientType = requestAttributes.getRequest().getHeader("clientType");
            if (StringUtils.isBlank(clientType)) {
                clientType = requestAttributes.getRequest().getParameter("clientType");
            }
            if (StringUtils.isNotBlank(clientType) && !"H5".equals(clientType) && !"WX".equals(clientType)) {
                clientType = clientType.toLowerCase();
            }
            return clientType;
        }
        return null;
    }
}
