package com.binance.master.utils;

import java.io.Serializable;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.validation.metadata.ConstraintDescriptor;

import org.springframework.beans.NotReadablePropertyException;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

public class ValidatorUtils {

    private static final Set<String> INTERNAL_ANNOTATION_ATTRIBUTES = new HashSet<String>(3);

    static {
        INTERNAL_ANNOTATION_ATTRIBUTES.add("message");
        INTERNAL_ANNOTATION_ATTRIBUTES.add("groups");
        INTERNAL_ANNOTATION_ATTRIBUTES.add("payload");
    }

    private static Validator validator;

    public static void init(Validator validator) {
        ValidatorUtils.validator = validator;
    }

    public static boolean validate(Object data, BindingResult errors, Class<?>... groups) {
        if (validator == null) {
            return true;
        } else {
            Set<?> errorSet = (Set<?>)validator.validate(data, groups);
            if (errorSet.size() > 0) {
                for (Object object : errorSet) {
                    ConstraintViolation<Object> violation = (ConstraintViolation<Object>)object;
                    String field = determineField(violation);
                    FieldError fieldError = errors.getFieldError(field);
                    if (fieldError == null || !fieldError.isBindingFailure()) {
                        try {
                            ConstraintDescriptor<?> cd = violation.getConstraintDescriptor();
                            String errorCode = determineErrorCode(cd);
                            Object[] errorArgs = getArgumentsForConstraint(errors.getObjectName(), field, cd);
                            if (errors instanceof BindingResult) {
                                // Can do custom FieldError registration with invalid value from
                                // ConstraintViolation,
                                // as necessary for Hibernate Validator compatibility (non-indexed set
                                // path in field)
                                BindingResult bindingResult = (BindingResult)errors;
                                String nestedField = bindingResult.getNestedPath() + field;
                                if ("".equals(nestedField)) {
                                    String[] errorCodes = bindingResult.resolveMessageCodes(errorCode);
                                    bindingResult.addError(new ObjectError(errors.getObjectName(), errorCodes,
                                        errorArgs, violation.getMessage()));
                                } else {
                                    Object rejectedValue = getRejectedValue(field, violation, bindingResult);
                                    String[] errorCodes = bindingResult.resolveMessageCodes(errorCode, field);
                                    bindingResult.addError(new FieldError(errors.getObjectName(), nestedField,
                                        rejectedValue, false, errorCodes, errorArgs, violation.getMessage()));
                                }
                            } else {
                                // got no BindingResult - can only do standard rejectValue call
                                // with automatic extraction of the current field value
                                errors.rejectValue(field, errorCode, errorArgs, violation.getMessage());
                            }
                        } catch (NotReadablePropertyException ex) {
                            throw new IllegalStateException("JSR-303 validated property '" + field
                                + "' does not have a corresponding accessor for Spring data binding - "
                                + "check your DataBinder's configuration (bean property versus direct field access)",
                                ex);
                        }
                    }
                }
                return false;
            }
            return true;
        }
    }

    private static String determineField(ConstraintViolation<Object> violation) {
        String path = violation.getPropertyPath().toString();
        int elementIndex = path.indexOf(".<");
        return (elementIndex >= 0 ? path.substring(0, elementIndex) : path);
    }

    private static String determineErrorCode(ConstraintDescriptor<?> descriptor) {
        return descriptor.getAnnotation().annotationType().getSimpleName();
    }

    private static Object[] getArgumentsForConstraint(String objectName, String field,
        ConstraintDescriptor<?> descriptor) {
        List<Object> arguments = new LinkedList<Object>();
        arguments.add(getResolvableField(objectName, field));
        // Using a TreeMap for alphabetical ordering of attribute names
        Map<String, Object> attributesToExpose = new TreeMap<String, Object>();
        for (Map.Entry<String, Object> entry : descriptor.getAttributes().entrySet()) {
            String attributeName = entry.getKey();
            Object attributeValue = entry.getValue();
            if (!INTERNAL_ANNOTATION_ATTRIBUTES.contains(attributeName)) {
                if (attributeValue instanceof String) {
                    attributeValue = new ResolvableAttribute(attributeValue.toString());
                }
                attributesToExpose.put(attributeName, attributeValue);
            }
        }
        arguments.addAll(attributesToExpose.values());
        return arguments.toArray(new Object[arguments.size()]);
    }

    private static MessageSourceResolvable getResolvableField(String objectName, String field) {
        String[] codes = new String[] {objectName + Errors.NESTED_PATH_SEPARATOR + field, field};
        return new DefaultMessageSourceResolvable(codes, field);
    }

    private static Object getRejectedValue(String field, ConstraintViolation<Object> violation,
        BindingResult bindingResult) {
        Object invalidValue = violation.getInvalidValue();
        if (!"".equals(field) && !field.contains("[]")
            && (invalidValue == violation.getLeafBean() || field.contains("[") || field.contains("."))) {
            // Possibly a bean constraint with property path: retrieve the actual property value.
            // However, explicitly avoid this for "address[]" style paths that we can't handle.
            invalidValue = bindingResult.getRawFieldValue(field);
        }
        return invalidValue;
    }

    @SuppressWarnings("serial")
    private static class ResolvableAttribute implements MessageSourceResolvable, Serializable {

        private final String resolvableString;

        public ResolvableAttribute(String resolvableString) {
            this.resolvableString = resolvableString;
        }

        @Override
        public String[] getCodes() {
            return new String[] {this.resolvableString};
        }

        @Override
        public Object[] getArguments() {
            return null;
        }

        @Override
        public String getDefaultMessage() {
            return this.resolvableString;
        }
    }

}
