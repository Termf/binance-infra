package com.binance.master.validator.constraints;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import com.binance.master.validator.internal.constraintvalidators.iv.CustomConstraintValidator;
import com.binance.master.validator.internal.constraintvalidators.iv.IVerifier;

@Target({TYPE})
@Retention(RUNTIME)
@Constraint(validatedBy = CustomConstraintValidator.class)
@Documented
public @interface CustomCheck {

    String message() default "{com.binance.master.validator.constraints.CustomCheck.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    Class<? extends IVerifier<?>> verifier();

    @Target({TYPE})
    @Retention(RUNTIME)
    @Documented
    @interface List {

        CustomCheck[] value();
    }
}
