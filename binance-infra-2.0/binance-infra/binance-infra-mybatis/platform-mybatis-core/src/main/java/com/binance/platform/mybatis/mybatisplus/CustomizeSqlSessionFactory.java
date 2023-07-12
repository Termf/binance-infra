package com.binance.platform.mybatis.mybatisplus;

import static org.springframework.util.StringUtils.hasLength;
import static org.springframework.util.StringUtils.tokenizeToStringArray;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.toolkit.ExceptionUtils;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

import com.baomidou.mybatisplus.core.toolkit.Assert;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.binance.platform.mybatis.handler.EncryptedColumnHandler;
import com.binance.platform.mybatis.handler.SmartEnumTypeHandler;
import com.binance.platform.mybatis.handler.encrypt.CryptType;

public class CustomizeSqlSessionFactory {

    private String typeEnumsPackage;

    private static final ResourcePatternResolver RESOURCE_PATTERN_RESOLVER = new PathMatchingResourcePatternResolver();
    private static final MetadataReaderFactory METADATA_READER_FACTORY =
        new CachingMetadataReaderFactory(RESOURCE_PATTERN_RESOLVER);

    public CustomizeSqlSessionFactory(String typeEnumsPackage, List<SqlSessionFactory> sqlSessionFactoryList,
        ApplicationContext applicationContext) {
        for (SqlSessionFactory sqlSessionFactory : sqlSessionFactoryList) {
            // 取得类型转换注册器
            TypeHandlerRegistry typeHandlerRegistry = sqlSessionFactory.getConfiguration().getTypeHandlerRegistry();
            // 注册加密解密handler
            typeHandlerRegistry.register(CryptType.class, EncryptedColumnHandler.class);
            // 注册Enum Handler
            List<String> basePackage = new ArrayList<>();
            if (!StringUtils.isEmpty(this.typeEnumsPackage)) {
                basePackage.addAll(Arrays.asList(this.typeEnumsPackage.split(",")));
            } else {
                // 从注解中解析basePackage
                Map<String, Object> applicationClass =
                    applicationContext.getBeansWithAnnotation(SpringBootApplication.class);
                applicationClass.forEach((k, v) -> {
                    SpringBootApplication ann =
                        AnnotationUtils.findAnnotation(v.getClass(), SpringBootApplication.class);
                    if (ann.scanBasePackages().length == 0 && ann.scanBasePackageClasses().length == 0) {
                        basePackage.add(v.getClass().getPackage().getName());
                    } else {
                        basePackage.addAll(Arrays.asList(ann.scanBasePackages()));
                        basePackage.addAll(Arrays.asList(ann.scanBasePackageClasses()).stream()
                            .map(s -> s.getPackage().getName()).collect(Collectors.toList()));
                    }
                });
                List<String> newPackage =
                    basePackage.stream().map(s -> s.endsWith("*") ? s : s + ".**").collect(Collectors.toList());
                basePackage.clear();
                basePackage.addAll(newPackage);
            }
            this.typeEnumsPackage = basePackage.stream().collect(Collectors.joining(","));
            if (hasLength(this.typeEnumsPackage)) {
                Set<Class> classes;
                if (typeEnumsPackage.contains(StringPool.STAR) && !typeEnumsPackage.contains(StringPool.COMMA)
                    && !typeEnumsPackage.contains(StringPool.SEMICOLON)) {
                    classes = scanTypePackage(typeEnumsPackage);
                } else {
                    String[] typeEnumsPackageArray = tokenizeToStringArray(this.typeEnumsPackage,
                        ConfigurableApplicationContext.CONFIG_LOCATION_DELIMITERS);
                    Assert.notNull(typeEnumsPackageArray, "not find typeEnumsPackage:" + typeEnumsPackage);
                    classes = new HashSet<>();
                    for (String typePackage : typeEnumsPackageArray) {
                        Set<Class> scanTypePackage = scanTypePackage(typePackage);
                        if (scanTypePackage.isEmpty()) {
                        } else {
                            classes.addAll(scanTypePackage);
                        }
                    }
                }
                for (Class cls : classes) {
                    if (cls.isEnum()) {
                        // 原生方式
                        typeHandlerRegistry.register(cls, SmartEnumTypeHandler.class);
                    }
                }
            }
        }
    }

    /**
     * <p>
     * 扫描获取指定包路径所有类
     * </p>
     *
     * @param typePackage
     *            扫描类包路径
     * @return
     */
    public static Set<Class> scanTypePackage(String typePackage) {
        String pkg = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX
            + ClassUtils.convertClassNameToResourcePath(typePackage) + "/*.class";
        /*
         * 将加载多个绝对匹配的所有Resource
         * 将首先通过ClassLoader.getResource("META-INF")加载非模式路径部分，然后进行遍历模式匹配，排除重复包路径
         */
        try {
            Set<Class> set = new HashSet<>();
            Resource[] resources = RESOURCE_PATTERN_RESOLVER.getResources(pkg);
            if (resources != null && resources.length > 0) {
                MetadataReader metadataReader;
                for (Resource resource : resources) {
                    if (resource.isReadable()) {
                        metadataReader = METADATA_READER_FACTORY.getMetadataReader(resource);
                        set.add(Class.forName(metadataReader.getClassMetadata().getClassName()));
                    }
                }
            }
            return set;
        } catch (Exception e) {
            throw ExceptionUtils.mpe("not find scanTypePackage: %s", e, pkg);
        }
    }

}
