package com.binance.platform.mbx.util;

import com.binance.master.error.GeneralCode;
import com.binance.platform.mbx.exception.MbxException;
import com.binance.platform.mbx.monitor.MonitorUtil;
import org.hibernate.validator.HibernateValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.ConstraintViolation;
import javax.validation.Path;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Iterator;
import java.util.Set;

/**
 * @author Li Fenggang
 * Date: 2020/7/13 7:45 下午
 */
public class ValidationUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(ValidationUtil.class);

    private static final Validator validator;

    static {
        ValidatorFactory factory = Validation.byProvider(HibernateValidator.class)
                .configure()
                .failFast(true)
                .buildValidatorFactory();
        validator = factory.getValidator();
    }

    /**
     * 面向注解的Validator校验
     *
     * @param requestParam
     * @throws MbxException
     */
    public static void validCheck(Object requestParam) throws MbxException {
        Set<ConstraintViolation<Object>> violationSet = validator.validate(requestParam);
        Iterator<ConstraintViolation<Object>> violationIterator = violationSet.iterator();
        StringBuilder stringBuilder = new StringBuilder();
        while(violationIterator.hasNext()) {
            ConstraintViolation<Object> violation = violationIterator.next();

            // field
            Path propertyPath = violation.getPropertyPath();
            stringBuilder.append(propertyPath).append("[");

            // violation
            String violationMessage = violation.getMessage();
            stringBuilder.append(violationMessage).append("];");
        }

        if (stringBuilder.length() > 0) {
            String invalidMessage = stringBuilder.toString();
            LOGGER.error("Validation failed:" + invalidMessage);
            throw new MbxException(GeneralCode.SYS_VALID, invalidMessage);
        }
    }

}
