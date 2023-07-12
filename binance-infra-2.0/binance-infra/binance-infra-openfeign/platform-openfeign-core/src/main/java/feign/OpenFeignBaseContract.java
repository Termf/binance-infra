package feign;

import static feign.Util.checkState;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.URI;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;

public abstract class OpenFeignBaseContract extends Contract.BaseContract {

    private List<FeignClientMethodMetadataParseHandler> parseMethodMetadataHandlers;

    private static void checkMapString(String name, Class<?> type, Type genericType) {
        checkState(Map.class.isAssignableFrom(type), "%s parameter must be a Map: %s", name, type);
        checkMapKeys(name, genericType);
    }

    private static void checkMapKeys(String name, Type genericType) {
        Class<?> keyClass = null;

        // assume our type parameterized
        if (ParameterizedType.class.isAssignableFrom(genericType.getClass())) {
            Type[] parameterTypes = ((ParameterizedType)genericType).getActualTypeArguments();
            keyClass = (Class<?>)parameterTypes[0];
        } else if (genericType instanceof Class<?>) {
            // raw class, type parameters cannot be inferred directly, but we can scan any extended
            // interfaces looking for any explict types
            Type[] interfaces = ((Class)genericType).getGenericInterfaces();
            if (interfaces != null) {
                for (Type extended : interfaces) {
                    if (ParameterizedType.class.isAssignableFrom(extended.getClass())) {
                        // use the first extended interface we find.
                        Type[] parameterTypes = ((ParameterizedType)extended).getActualTypeArguments();
                        keyClass = (Class<?>)parameterTypes[0];
                        break;
                    }
                }
            }
        }

        if (keyClass != null) {
            checkState(String.class.equals(keyClass), "%s key must be a String: %s", name, keyClass.getSimpleName());
        }
    }

    @Override
    protected MethodMetadata parseAndValidateMetadata(Class<?> targetType, Method method) {
        MethodMetadata data = new MethodMetadata();
        data.returnType(Types.resolve(targetType, targetType, method.getGenericReturnType()));
        data.configKey(Feign.configKey(targetType, method));

        if (targetType.getInterfaces().length == 1) {
            processAnnotationOnClass(data, targetType.getInterfaces()[0]);
        }
        processAnnotationOnClass(data, targetType);

        for (Annotation methodAnnotation : method.getAnnotations()) {
            processAnnotationOnMethod(data, methodAnnotation, method);
        }
        checkState(data.template().method() != null, "Method %s not annotated with HTTP method type (ex. GET, POST)",
            method.getName());
        Class<?>[] parameterTypes = method.getParameterTypes();
        Type[] genericParameterTypes = method.getGenericParameterTypes();

        Annotation[][] parameterAnnotations = method.getParameterAnnotations();
        int count = parameterAnnotations.length;
        for (int i = 0; i < count; i++) {
            boolean isHttpAnnotation = false;
            if (parameterAnnotations[i] != null) {
                isHttpAnnotation = processAnnotationsOnParameter(data, parameterAnnotations[i], i);
            }
            if (parameterTypes[i] == URI.class) {
                data.urlIndex(i);
            } else if (!isHttpAnnotation) {
                // checkState(data.formParams().isEmpty(),
                // "Body parameters cannot be used with form parameters.");
                checkState(data.bodyIndex() == null, "Method has too many Body parameters: %s", method);
                data.bodyIndex(i);
                data.bodyType(Types.resolve(targetType, targetType, genericParameterTypes[i]));
            }
        }
        if (data.headerMapIndex() != null) {
            checkMapString("HeaderMap", parameterTypes[data.headerMapIndex()],
                genericParameterTypes[data.headerMapIndex()]);
        }
        // 支持SpringQueryMap
        if (data.queryMapIndex() != null) {
            if (Map.class.isAssignableFrom(parameterTypes[data.queryMapIndex()])) {
                checkMapKeys("QueryMap", genericParameterTypes[data.queryMapIndex()]);
            }
        }
        if (parseMethodMetadataHandlers != null && parseMethodMetadataHandlers.size() > 0) {
            for (FeignClientMethodMetadataParseHandler handler : parseMethodMetadataHandlers) {
                handler.parsed(data, targetType, method);
            }
        }

        return data;
    }

    public void setParseMethodMetadataHandler(FeignClientMethodMetadataParseHandler parseMethodMetadataHandler) {
        if (this.parseMethodMetadataHandlers != null) {
            this.parseMethodMetadataHandlers.add(parseMethodMetadataHandler);
        } else {
            this.parseMethodMetadataHandlers = Lists.newArrayList();
            this.parseMethodMetadataHandlers.add(parseMethodMetadataHandler);
        }
    }

    public void setParseMethodMetadataHandlers(List<FeignClientMethodMetadataParseHandler> parseMethodMetadataHandler) {
        if (this.parseMethodMetadataHandlers != null) {
            this.parseMethodMetadataHandlers.addAll(parseMethodMetadataHandler);
        } else {
            this.parseMethodMetadataHandlers = Lists.newArrayList();
            this.parseMethodMetadataHandlers.addAll(parseMethodMetadataHandler);
        }
    }

}
