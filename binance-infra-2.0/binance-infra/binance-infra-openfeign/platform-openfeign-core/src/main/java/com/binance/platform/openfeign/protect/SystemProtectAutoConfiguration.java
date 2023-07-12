package com.binance.platform.openfeign.protect;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.binance.master.commons.NamedThreadFactory;
import com.ctrip.framework.apollo.ConfigService;

import static com.binance.platform.openfeign.protect.SystemProtectInterceptor.SYSTEM_RULE_APP_BLACKLIST;

/**
 * @author james.li
 */
@Configuration
public class SystemProtectAutoConfiguration {

    private final static ScheduledExecutorService scheduler =
            Executors.newScheduledThreadPool(1, new NamedThreadFactory("system-status-record-task"));

    private static SystemStatusListener statusListener = new SystemStatusListener();

    @PostConstruct
    public void init() {
        scheduler.scheduleAtFixedRate(statusListener, 5, 1, TimeUnit.SECONDS);
    }


    @Configuration
    @ConditionalOnProperty(name = "com.binance.intra.sys-protect.switch", havingValue = "true", matchIfMissing = false)
    @ConditionalOnWebApplication
    static class SystemProtectSupportConfig implements WebMvcConfigurer {

        @Autowired
        private Environment env;

        @Value("${server.error.path:${error.path:/error}}")
        private String errorPath;

        @Override
        public void addInterceptors(InterceptorRegistry registry) {
            String appBlackListStr = env.getProperty(SYSTEM_RULE_APP_BLACKLIST);
            boolean needInterceptor = true;
            if (appBlackListStr != null) {
                // ignore applications that already integrated with sentinel
                String appName = env.getProperty("spring.application.name");
                String[] blackList = appBlackListStr.split(",");
                for (String s : blackList) {
                    if (appName.equalsIgnoreCase(s) || appName.matches(s)) {
                        needInterceptor = false;
                        break;
                    }
                }
            }

            if (needInterceptor) {
                SystemProtectInterceptor interceptor = new SystemProtectInterceptor(env, statusListener);
                ConfigService.getAppConfig().addChangeListener(interceptor);
                registry.addInterceptor(interceptor)//
                        .addPathPatterns("/**").excludePathPatterns("/**" + errorPath);
            }
        }

    }
}
