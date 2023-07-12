package com.binance.platform.openfeign.signature.rsa;

import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.BooleanUtils;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.web.method.HandlerMethod;

import com.binance.master.annotations.IntraSecurityController;
import com.binance.platform.openfeign.signature.IntranetSafe;
import com.binance.platform.openfeign.signature.VerifierProvider;
import com.google.common.collect.Sets;

public class RSAVerifier implements RSASignature {

    private static final Set<String> VERIFIER_RESOURCE = Sets.newHashSet();

    private final RSAVerifierInner verifierInner;

    private final Environment env;

    public RSAVerifier(VerifierProvider verifierProvider, Environment env) {
        try {
            this.env = env;
            Resource resource = verifierProvider.publicKey();
            InputStreamReader inputStreamReader = new InputStreamReader(resource.getInputStream());
            this.verifierInner = new RSAVerifierInner(inputStreamReader);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public boolean need(String method, String path) {
        if (BooleanUtils.toBoolean(env.getProperty(SIGNATURE_SWITCH))) {
            return false;
        }
        if (!VERIFIER_RESOURCE.contains(calculateKey(method, path))) {
            return false;
        }
        return true;
    }

    public boolean verify(byte[] body, String signature) {
        return verifierInner.verify(body, signature);
    }

    public void parsed(HandlerMethod handlerMethod, HttpServletRequest request) {
        final String methodName = request.getMethod();
        final String path = request.getRequestURI();
        String key = calculateKey(methodName, path);
        if (VERIFIER_RESOURCE.contains(key)) {
            return;
        }
        Method method = handlerMethod.getMethod();
        if (intraSecurity(method.getDeclaringClass(), method)) {
            VERIFIER_RESOURCE.add(key);
        }
    }

    private boolean intraSecurity(Class<?> clazz, Method method) {
        // 判断类头有没有注解 兼容升级之前的写法
        IntraSecurityController intraSecurityOld = AnnotationUtils.getAnnotation(clazz, IntraSecurityController.class);
        if (intraSecurityOld != null) {
            return true;
        }
        // 子类方法有没有注解
        if (AnnotationUtils.getAnnotation(method, IntranetSafe.class) != null) {
            return true;
        } else {
            // 判断类头有没有注解
            IntranetSafe intraSecurity = AnnotationUtils.getAnnotation(clazz, IntranetSafe.class);
            if (intraSecurity == null) {
                for (Class<?> itf : clazz.getInterfaces()) {
                    // 判断父接口有没有注解
                    intraSecurity = AnnotationUtils.getAnnotation(itf, IntranetSafe.class);
                    if (intraSecurity == null) {
                        try {
                            // 判断父接口对应的方法有没有注解
                            intraSecurity = AnnotationUtils.getAnnotation(
                                itf.getMethod(method.getName(), method.getParameterTypes()), IntranetSafe.class);
                        } catch (Exception e) {
                            continue;
                        }
                    } else {
                        break;
                    }
                }
                return intraSecurity != null ? true : false;
            } else {
                return true;
            }
        }

    }

}
