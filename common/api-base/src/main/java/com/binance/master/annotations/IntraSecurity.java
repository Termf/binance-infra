package com.binance.master.annotations;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * @author james.li
 * used for enabling inner security on endpoint
 */
@Documented
@Retention(RUNTIME)
@Target(METHOD)
public @interface IntraSecurity {
}
