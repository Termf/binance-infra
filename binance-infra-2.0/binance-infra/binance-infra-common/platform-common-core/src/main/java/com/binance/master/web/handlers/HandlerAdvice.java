package com.binance.master.web.handlers;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.method.HandlerMethod;

import com.binance.master.error.BusinessException;
import com.binance.master.error.GeneralCode;
import com.binance.master.models.APIResponse;

@RestControllerAdvice
public class HandlerAdvice {

    private static final Logger log = LoggerFactory.getLogger(HandlerAdvice.class);

    @Value("${com.binance.log.print-business-exception:false}")
    private boolean printBusinessException = false;

    @Resource
    private MessageHelper messageHelper;

    private APIResponse<?> getValid(BindingResult bindingResult) {
        Map<String, Object> data = new HashMap<>();
        for (FieldError error : bindingResult.getFieldErrors()) {
            data.put(error.getField(), error.getDefaultMessage());
        }
        return APIResponse.getErrorJsonResult(APIResponse.Type.VALID, GeneralCode.SYS_VALID.getCode(), data);
    }

    @ExceptionHandler(value = BindException.class)
    public APIResponse<?> exception(HttpServletResponse response, BindException exception, HandlerMethod handler)
            throws IOException {
        log.warn("system warn:", exception);
        return this.getValid(exception);
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public APIResponse<?> exception(HttpServletResponse response, MethodArgumentNotValidException exception,
                                    HandlerMethod handler) throws IOException {
        log.warn("system warn:", exception);
        return this.getValid(exception.getBindingResult());
    }

    @ExceptionHandler(value = IllegalArgumentException.class)
    public APIResponse<?> exception(IllegalArgumentException exception, HandlerMethod handler) throws IOException {
        log.warn("system warn:", exception);
        return APIResponse.getErrorJsonResult(APIResponse.Type.GENERAL, GeneralCode.SYS_ERROR.getCode(),
                exception.getMessage());
    }

    @ExceptionHandler(value = BusinessException.class)
    public APIResponse<?> exception(BusinessException exception, HandlerMethod handler, HttpServletRequest request)
            throws IOException {
        if (printBusinessException) {
            log.warn("system warn:", exception);
        }
        String code = exception.getBizCode();
        String message = exception.getBizMessage();
        if (exception.getErrorCode() != null) {
            code = exception.getErrorCode().getCode();
            message = messageHelper.getMessage(exception.getErrorCode(), exception.getParams());
        }
        return APIResponse.getErrorJsonResult(APIResponse.Type.GENERAL, code, message, exception.getParams(),
                exception.getSubData());
    }

    @ExceptionHandler(value = {HttpServerErrorException.ServiceUnavailable.class})
    public APIResponse<?> exception(HttpServletResponse response, HttpServerErrorException.ServiceUnavailable exception, HttpServletRequest request) {
        return processStatus503(response, exception);
    }

    @ExceptionHandler({Error.class, Exception.class, Throwable.class, RuntimeException.class})
    public APIResponse<?> exception(HttpServletResponse response, Throwable exception, HttpServletRequest request) throws IOException {
        Throwable rootCause = ExceptionUtils.getRootCause(exception.getCause());
        if (rootCause instanceof HttpServerErrorException.ServiceUnavailable) {
            return processStatus503(response, exception.getCause());
        } else if (rootCause instanceof SocketTimeoutException) {
            log.error("socket timeout error:", exception);
            return APIResponse.getErrorJsonResult(APIResponse.Type.SYS, GeneralCode.SOCKET_TIMEOUT.getCode(),
                    messageHelper.getMessage(GeneralCode.SOCKET_TIMEOUT));
        } else {
            log.error("system error:", exception);
            return APIResponse.getErrorJsonResult(APIResponse.Type.SYS, GeneralCode.SYS_ERROR.getCode(),
                    messageHelper.getMessage(GeneralCode.SYS_ERROR));
        }
    }

    private APIResponse<?> processStatus503(HttpServletResponse response, Throwable exception) {
        log.warn("HttpServerErrorException.ServiceUnavailable", exception);

        if(response != null){
            response.setStatus(HttpStatus.SERVICE_UNAVAILABLE.value());
            response.addHeader("Warning", "Internal Circuit Breaker");
        }

        return APIResponse.getErrorJsonResult(APIResponse.Type.GENERAL, GeneralCode.SERVICE_HEAVY_LOAD.getCode(),
                messageHelper.getMessage(GeneralCode.SERVICE_HEAVY_LOAD));
    }
}
