package com.binance.master.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author james.li
 * used for enabling inner security on endpoint
 */
@Documented
@Retention(RUNTIME)
@Target(TYPE)
public @interface IntraSecurityController {
}
