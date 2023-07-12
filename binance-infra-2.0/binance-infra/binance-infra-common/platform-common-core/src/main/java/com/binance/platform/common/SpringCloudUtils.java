package com.binance.platform.common;

import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

public class SpringCloudUtils {

    private static final String[] DEFAULT_SEARCH_LOCATIONS =
        new String[] {"classpath:/", "classpath:/config/", "file:./", "file:./config/"};

    private static final ResourceLoader CONFIGFILE_RESOURCELOADER = new DefaultResourceLoader();

    /**
     * 判断是否有bootstrap.property,bootstrap.yml
     * 
     * (如果有自定义配置了：spring.cloud.bootstrap.name，将不起作用，只生效于默认的配置
     */
    public static boolean isSpringCloudConfig() {
        boolean propertiesisExist = isFileExistInDefaultLocation("bootstrap.properties");
        boolean ymlExist = isFileExistInDefaultLocation("bootstrap.yml");
        return propertiesisExist || ymlExist;
    }

    public static boolean isFileExistInDefaultLocation(String configFileName) {
        boolean isExist = false;
        for (String location : DEFAULT_SEARCH_LOCATIONS) {
            try {
                Resource resource = CONFIGFILE_RESOURCELOADER.getResource(location + configFileName);
                isExist = resource.exists();
            } catch (Throwable e) {
            }
            if (isExist) {
                break;
            }
        }
        return isExist;

    }

}
