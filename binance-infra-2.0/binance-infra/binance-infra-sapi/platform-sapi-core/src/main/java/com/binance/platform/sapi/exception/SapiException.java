package com.binance.platform.sapi.exception;

import com.binance.platform.common.MyJsonUtil;
import com.binance.platform.sapi.util.RetrofitUtils;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.util.StringUtils;

import java.io.IOException;

/**
 * @author 陈添明
 */
public class SapiException extends RuntimeException {
    private ErrorResult errorResult;

    public SapiException(ErrorResult errorResult, String message) {
        super(message);
    }

    public SapiException(String message, Throwable cause) {
        super(message, cause);
    }

    public static SapiException errorStatus(Request request, Response response) {
        ErrorResult errorResult = null;
        String msg = ErrorResult.DEFAULT_ERROR_MSG;
        try {
            String responseBody = RetrofitUtils.readResponseBody(response);
            if (StringUtils.hasText(responseBody)) {
                msg = responseBody;
                errorResult = MyJsonUtil.fromJson(responseBody, ErrorResult.class);
            }
        } catch (ReadResponseBodyException e) {
            throw new SapiException(msg, e);
        } catch (Exception e) {
            throw new SapiException(msg, e);
        } finally {
            response.close();
        }
        return new SapiException(errorResult, msg);
    }

    public static SapiException errorExecuting(Request request, IOException cause) {
        return new SapiIOException(cause.getMessage(), cause);
    }

    public static SapiException errorUnknown(Request request, Exception cause) {
        if (cause instanceof SapiException) {
            return (SapiException) cause;
        }
        StringBuilder errorMessage = new StringBuilder(cause.toString());
        if (request != null) {
            errorMessage.append(". method=").append(request.method())
                    .append(", url=").append(request.url());
        }
        return new SapiException(errorMessage.toString(), cause);
    }

    public int getCode() {
        return errorResult == null ? ErrorResult.DEFAULT_ERROR_CODE : errorResult.getCode();
    }

    /**
     * SAPI返回的错误响应体，可能为null。
     *
     * @return
     */
    public ErrorResult getErrorResult() {
        return errorResult;
    }
}
