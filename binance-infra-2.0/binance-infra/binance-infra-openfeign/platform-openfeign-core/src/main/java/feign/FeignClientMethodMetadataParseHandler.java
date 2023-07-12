package feign;

import java.lang.reflect.Method;

import feign.MethodMetadata;

/**
 * 对feign Client进行解析的时候进行拦截
 */
public interface FeignClientMethodMetadataParseHandler {

	/**
	 * <pre>
	 * 此方法是获取所有的Feign Client的配置，但是如果是用PathVariable这种带有占位符的path的话，将有问题，不支持此种路径
	 * 原因是：
	 *    这个方法是在启动时获取所有的信息，并不是运行期间获取信息
	 * </pre>
	 */
	public void parsed(MethodMetadata meta, Class<?> targetType, Method method);
}
