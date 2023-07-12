package com.binance.platform.openfeign.signature.rsa;

import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.BooleanUtils;
import org.bouncycastle.crypto.RuntimeCryptoException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.env.Environment;

import com.binance.master.annotations.IntraSecurity;
import com.binance.platform.openfeign.signature.IntranetSafe;
import com.binance.platform.openfeign.signature.SignerProvider;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import feign.FeignClientMethodMetadataParseHandler;
import feign.MethodMetadata;
import feign.RequestTemplate;

public class RSASigner implements RSASignature, FeignClientMethodMetadataParseHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(RSASigner.class);

    private static final Map<String, Set<String>> SIGNER_RESOURCE = Maps.newConcurrentMap();

    private static final Map<String, RSASignerInner> SIGNER_CACHE = Maps.newConcurrentMap();

    private RSASignerInner shareRSASignerInner = null;

    private final Environment env;

    public RSASigner(SignerProvider signerProvider, Environment env) {
        this.env = env;
        if (signerProvider.shareprivateKey() != null) {
            try {
                InputStreamReader inputStreamReader =
                    new InputStreamReader(signerProvider.shareprivateKey().getInputStream());
                this.shareRSASignerInner = new RSASignerInner(inputStreamReader);
            } catch (IOException e) {
                LOGGER.error("failed to load signer for service ", e);
            }

        } else {
            signerProvider.isolatePrivateKey().forEach((k, v) -> {
                try {
                    InputStreamReader inputStreamReader = new InputStreamReader(v.getInputStream());
                    RSASignerInner rsaSigner = new RSASignerInner(inputStreamReader);
                    SIGNER_CACHE.put(k, rsaSigner);
                } catch (IOException e) {
                    LOGGER.error("failed to load signer for service " + k, e);
                }

            });
        }

    }

    public boolean need(String clientName, String method, String path) {
        if (!BooleanUtils.toBoolean(env.getProperty(SIGNATURE_SWITCH))) {
            return false;
        }
        Set<String> resources = SIGNER_RESOURCE.get(clientName);
        if (resources == null || !resources.contains(calculateKey(method, path))) {
            return false;
        }
        return true;
    }

    public String sign(String clientName, byte[] body) {
        RSASignerInner signer = SIGNER_CACHE.getOrDefault(clientName, shareRSASignerInner);
        if (signer == null) {
            throw new RuntimeCryptoException("cannot get the signer for service client " + clientName);
        }
        return signer.doSign(body);
    }

    @Override
    public void parsed(MethodMetadata meta, Class<?> targetType, Method method) {
        FeignClient feignClient = AnnotationUtils.getAnnotation(targetType, FeignClient.class);
        if (feignClient == null) {
            return;
        }
        IntranetSafe methodIntraSecurity = AnnotationUtils.getAnnotation(method, IntranetSafe.class);
        // 判断类头有没有注解 兼容升级之前的写法
        IntraSecurity masterIntraSecurity = AnnotationUtils.getAnnotation(method, IntraSecurity.class);
        IntranetSafe classIntraSecurity = AnnotationUtils.getAnnotation(targetType, IntranetSafe.class);
        if (methodIntraSecurity != null || classIntraSecurity != null || masterIntraSecurity != null) {
            String serviceId = feignClient.name();
            RequestTemplate template = meta.template();
            String path = calculateKey(template.method(), template.url());
            Set<String> resource = SIGNER_RESOURCE.get(serviceId);
            if (resource != null) {
                resource.add(path);
            } else {
                resource = Sets.newHashSet();
                resource.add(path);
                SIGNER_RESOURCE.put(serviceId, resource);
            }
        }
    }

}
