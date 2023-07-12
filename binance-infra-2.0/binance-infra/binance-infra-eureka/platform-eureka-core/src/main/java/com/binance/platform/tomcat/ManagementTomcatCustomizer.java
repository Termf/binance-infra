package com.binance.platform.tomcat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;

import javax.management.Attribute;
import javax.management.MBeanServer;
import javax.management.MBeanServerFactory;
import javax.management.ObjectName;
import java.lang.management.ManagementFactory;
import java.util.List;
import java.util.Set;

/**
 * binance-infra-whole
 *
 * @author Thomas Li
 * Date: 2021/9/24
 */
public class ManagementTomcatCustomizer implements CommandLineRunner, EnvironmentAware {
    private static final Logger log = LoggerFactory.getLogger(ManagementTomcatCustomizer.class);

    private String managementServerPort;

    @Override
    public void run(String... args) throws Exception {
        try {
            MBeanServer mBeanServer = getMBeanServer();
            ObjectName queryObjectName = new ObjectName("Tomcat*:type=ThreadPool,name=*" + managementServerPort + "*,*");
            Set<ObjectName> threadPoolObjectNames = mBeanServer.queryNames(queryObjectName, null);
            threadPoolObjectNames.forEach(threadPoolObjectName -> {
                try {
                    mBeanServer.setAttribute(threadPoolObjectName,new Attribute("minSpareThreads", 20));
                    mBeanServer.setAttribute(threadPoolObjectName,new Attribute("maxThreads", 100));
                } catch (Exception e) {
                    log.warn("customize management tomcat server error for {}", threadPoolObjectName, e);
                }
            });
        } catch (Throwable throwable) {
            log.warn("customize management tomcat server error", throwable);
        }
    }

    public static MBeanServer getMBeanServer() {
        List<MBeanServer> mBeanServers = MBeanServerFactory.findMBeanServer(null);
        if (!mBeanServers.isEmpty()) {
            return mBeanServers.get(0);
        }
        return ManagementFactory.getPlatformMBeanServer();
    }

    @Override
    public void setEnvironment(Environment environment) {
        managementServerPort = environment.getProperty("management.server.port", "28081");
    }
}
