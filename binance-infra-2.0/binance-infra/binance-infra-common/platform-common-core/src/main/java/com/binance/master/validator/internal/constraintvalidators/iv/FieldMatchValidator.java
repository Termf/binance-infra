package com.binance.master.validator.internal.constraintvalidators.iv;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.commons.beanutils.BeanUtils;

import com.binance.master.validator.constraints.FieldMatch;

public class FieldMatchValidator implements ConstraintValidator<FieldMatch, Object> {

    private String first;

    private String second;

    @Override
    public void initialize(FieldMatch constraintAnnotation) {
        this.first = constraintAnnotation.first();
        this.second = constraintAnnotation.second();
        this.validator(this.first, this.second);
    }

    @Override
    public boolean isValid(final Object value, ConstraintValidatorContext context) {
        try {
            final Object firstObj = BeanUtils.getProperty(value, first);
            final Object secondObj = BeanUtils.getProperty(value, second);
            return firstObj == secondObj || firstObj != null && firstObj.equals(secondObj);
        } catch (final Exception ignore) {
            // ignore
        }
        return true;
    }

    private void validator(String first, String second) {
        if (first == null) {
            new IllegalArgumentException("first is null");
        }
        if (second == null) {
            new IllegalArgumentException("second is null");
        }
    }

}
