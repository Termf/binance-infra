package com.binance.platform.sapi.annotation;

import com.binance.platform.sapi.core.RetrofitClientScannerRegistrar;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author 陈添明
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import(RetrofitClientScannerRegistrar.class)
public @interface SapiScan {

    /**
     * Scan package path
     * Same meaning as basePackages
     *
     * @return basePackages
     */
    String[] value() default {};


    /**
     * Scan package path
     *
     * @return basePackages
     */
    String[] basePackages() default {};


    /**
     * Scan package classes
     *
     * @return Scan package classes
     */
    Class<?>[] basePackageClasses() default {};
}
