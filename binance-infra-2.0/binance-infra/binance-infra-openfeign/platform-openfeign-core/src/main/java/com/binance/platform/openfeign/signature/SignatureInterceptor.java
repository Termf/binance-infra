package com.binance.platform.openfeign.signature;

import java.io.PrintWriter;
import java.net.URI;
import java.util.Collection;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.binance.master.models.APIResponse;
import com.binance.platform.MyJsonUtil;
import com.binance.platform.openfeign.ReWriteHeaderFeignClient.ReWriteFeignClientHeaderHandler;
import com.binance.platform.openfeign.body.CustomBodyServletRequestWrapper;
import com.binance.platform.openfeign.signature.rsa.RSASigner;
import com.binance.platform.openfeign.signature.rsa.RSAVerifier;
import com.google.common.collect.Lists;

import feign.Request;
import feign.Request.Options;

public class SignatureInterceptor extends HandlerInterceptorAdapter implements ReWriteFeignClientHeaderHandler {

    private static final String INTRA_SECURITY_SIGNATURE_HEADER_KEY = "intra-sig";

    private RSASigner requestRSASigner;

    private RSAVerifier requestRSAVerifier;

    /**
     * 签名
     */
    public SignatureInterceptor(RSASigner requestRSASigner) {
        this.requestRSASigner = requestRSASigner;
    }

    /**
     * 验签
     */
    public SignatureInterceptor(RSAVerifier requestRSAVerifier) {
        this.requestRSAVerifier = requestRSAVerifier;
    }

    /**
     * 添加签名
     */
    @Override
    public void headers(Request request, Options options, Map<String, Collection<String>> headers) {
        if (this.requestRSASigner != null) {
            final String method = request.method();
            final byte[] body = request.body();
            final URI requestURI = URI.create(request.url());
            String signature = doSign(requestURI.getHost(), method, requestURI.getRawPath(), body);
            if (signature != null) {
                headers.put(INTRA_SECURITY_SIGNATURE_HEADER_KEY, Lists.newArrayList(signature));
            }
        }
    }

    private String doSign(final String serviceId, final String methodName, final String path, final byte[] body) {
        if (requestRSASigner.need(serviceId, methodName, path) && body != null) {
            return requestRSASigner.sign(serviceId, body);
        } else {
            return null;
        }
    }

    /**
     * 验证签名
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
        throws Exception {
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod)handler;
            requestRSAVerifier.parsed(handlerMethod, request);
        }
        if (request instanceof CustomBodyServletRequestWrapper) {
            CustomBodyServletRequestWrapper requestWrapper = (CustomBodyServletRequestWrapper)request;
            final byte[] body = requestWrapper.getBody();
            final String signature = requestWrapper.getHeader(INTRA_SECURITY_SIGNATURE_HEADER_KEY);
            final String methodName = requestWrapper.getMethod();
            final String path = requestWrapper.getRequestURI();
            boolean verified = this.doVerify(methodName, path, body, signature);
            if (!verified) {
                response.setStatus(HttpStatus.FORBIDDEN.value());
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                APIResponse<Object> apiResp = APIResponse.getErrorJsonResult("Access denied(No signature)");
                try (PrintWriter out = response.getWriter()) {
                    out.print(MyJsonUtil.toJson(apiResp));
                }
                return false;
            }
        }
        return true;
    }

    private boolean doVerify(final String methodName, final String path, final byte[] body, final String signature) {
        if (requestRSAVerifier.need(methodName, path) && body != null && signature != null) {
            return requestRSAVerifier.verify(body, signature);
        } else {
            return true;
        }
    }

}
