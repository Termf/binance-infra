package com.binance.master.validator.internal.constraintvalidators.iv;

import java.util.List;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.binance.master.validator.constraints.ListNotHasNull;

/**
 * @验证集合是否满足条件，各项不能为空
 * @author lion wang
 *
 */
public class ListNotHasNullValidator implements ConstraintValidator<ListNotHasNull, List<?>> {

    private int minSize;
    private int maxSize;

    @Override
    public void initialize(ListNotHasNull constraintAnnotation) {
        this.minSize = constraintAnnotation.minSize();
        this.maxSize = constraintAnnotation.maxSize();
    }

    @Override
    public boolean isValid(List<?> list, ConstraintValidatorContext context) {
        if (list == null || this.minSize > list.size() || this.maxSize < list.size()) {
            return false;
        }
        for (Object object : list) {
            if (object == null) {
                // 如果List集合中含有Null元素，校验失败
                return false;
            }
        }
        return true;
    }

}
