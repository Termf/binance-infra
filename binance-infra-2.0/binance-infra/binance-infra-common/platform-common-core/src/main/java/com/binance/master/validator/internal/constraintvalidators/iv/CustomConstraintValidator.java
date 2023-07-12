package com.binance.master.validator.internal.constraintvalidators.iv;

import java.util.Map.Entry;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationContext;

import com.binance.master.utils.WebUtils;
import com.binance.master.validator.constraints.CustomCheck;
import com.binance.master.validator.internal.constraintvalidators.iv.IVerifier.ValidResult;

@SuppressWarnings("all")
public class CustomConstraintValidator implements ConstraintValidator<CustomCheck, Object> {

    private IVerifier validator = null;

    @Override
    public void initialize(CustomCheck cus) {
        Class<? extends IVerifier> clazz = cus.verifier();
        ApplicationContext ctx = WebUtils.getApplicationContext();
        validator = ctx.getBean(clazz);
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        ValidResult result = new ValidResult();
        if (!validator.valid(value, result)) {
            context.disableDefaultConstraintViolation();// 禁用默认的约束
            if (!result.errors().isEmpty()) {
                context.buildConstraintViolationWithTemplate(StringUtils.join(result.errors().iterator(), " "))
                        .addBeanNode().addConstraintViolation();
            }
            for (Entry<String, String> error : result.fieldErrors()) {
                String fieldName = error.getKey();
                String message = error.getValue();
                context.buildConstraintViolationWithTemplate(message).addPropertyNode(fieldName)
                        .addConstraintViolation();
            }
            return false;
        }
        return true;
    }

}
