
package com.binance.platform;

import java.io.Serializable;
import java.lang.management.ManagementFactory;
import java.util.List;

import javax.management.MBeanServer;
import javax.management.MBeanServerFactory;
import javax.management.ObjectName;

import com.sun.management.OperatingSystemMXBean;

public class ServerLoadStatus implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final String tomcatEmbedDomain = "Tomcat";
    private static final String tomcatDomain = "Catalina";

    private String osName;

    private double systemLoadAverage;

    private long usedMemory;

    private int availableProcessors;

    private long freePhysicalMemorySize;

    private long totalPhysicalMemorySize;

    private int connectionCount;

    private int maxThreads;

    private int currentThreadCount;

    private int currentThreadsBusy;

    private int keepAliveCount;

    private int maxConnections;

    private int minSpareThreads;

    private String getTomcatDomian(MBeanServer mBeanServer) {
        try {
            mBeanServer.getAttribute(new ObjectName(tomcatEmbedDomain, "type", "Service"), "connectorNames");
            return tomcatEmbedDomain;
        } catch (Exception e) {
            return tomcatDomain;
        }
    }

    private static OperatingSystemMXBean getOperatingSystemMXBean() {
        OperatingSystemMXBean osmxb = (OperatingSystemMXBean)ManagementFactory.getOperatingSystemMXBean();
        return osmxb;
    }

    public void calculateSystemInfo() {
        this.osName = System.getProperty("os.name");
        system();
        tomcat();

    }

    public void tomcat() {
        List<MBeanServer> mBeanServers = MBeanServerFactory.findMBeanServer(null);
        if (!mBeanServers.isEmpty()) {
            MBeanServer mBeanServer = mBeanServers.get(0);
            String domain = getTomcatDomian(mBeanServer);
            try {
                ObjectName[] objNames =
                    (ObjectName[])mBeanServer.getAttribute(new ObjectName(domain, "type", "Service"), "connectorNames");
                for (ObjectName on : objNames) {
                    Object protocolHandlerClassName = mBeanServer.getAttribute(on, "protocolHandlerClassName");
                    Object localPort = mBeanServer.getAttribute(on, "localPort");
                    if (protocolHandlerClassName.toString().equals("org.apache.coyote.http11.Http11NioProtocol")) {
                        String threadPoolONStr = domain + ":type=ThreadPool,name=\"http-nio-" + localPort + "\"";
                        ObjectName threadPoolON = new ObjectName(threadPoolONStr);
                        this.connectionCount = convert(mBeanServer.getAttribute(threadPoolON, "connectionCount"));
                        this.currentThreadCount = convert(mBeanServer.getAttribute(threadPoolON, "currentThreadCount"));
                        this.currentThreadsBusy = convert(mBeanServer.getAttribute(threadPoolON, "currentThreadsBusy"));
                        this.keepAliveCount = convert(mBeanServer.getAttribute(threadPoolON, "keepAliveCount"));
                        this.maxConnections = convert(mBeanServer.getAttribute(threadPoolON, "maxConnections"));
                        this.maxThreads = convert(mBeanServer.getAttribute(threadPoolON, "maxThreads"));
                        this.minSpareThreads = convert(mBeanServer.getAttribute(threadPoolON, "minSpareThreads"));
                    }
                }
            } catch (Exception e) {
                // igore
            }
        }
    }

    private int convert(Object obj) {
        String o = obj.toString();
        Integer i = Integer.valueOf(o);
        return i;
    }

    private void system() {
        int kb = 1024;
        OperatingSystemMXBean osmxb = getOperatingSystemMXBean();
        this.systemLoadAverage = osmxb.getSystemLoadAverage();
        this.availableProcessors = osmxb.getAvailableProcessors();
        this.freePhysicalMemorySize = osmxb.getFreePhysicalMemorySize();
        this.totalPhysicalMemorySize = osmxb.getTotalPhysicalMemorySize();
        this.usedMemory = (osmxb.getTotalPhysicalMemorySize() - osmxb.getFreePhysicalMemorySize()) / kb;
    }

    public int getAvailableProcessors() {
        return availableProcessors;
    }

    public long getFreePhysicalMemorySize() {
        return freePhysicalMemorySize;
    }

    public String getOsName() {
        return osName;
    }

    public double getSystemLoadAverage() {
        return systemLoadAverage;
    }

    public long getTotalPhysicalMemorySize() {
        return totalPhysicalMemorySize;
    }

    public long getUsedMemory() {
        return usedMemory;
    }

    public void setAvailableProcessors(int availableProcessors) {
        this.availableProcessors = availableProcessors;
    }

    public void setFreePhysicalMemorySize(long freePhysicalMemorySize) {
        this.freePhysicalMemorySize = freePhysicalMemorySize;
    }

    public void setOsName(String osName) {
        this.osName = osName;
    }

    public void setSystemLoadAverage(double systemLoadAverage) {
        this.systemLoadAverage = systemLoadAverage;
    }

    public void setTotalPhysicalMemorySize(long totalPhysicalMemorySize) {
        this.totalPhysicalMemorySize = totalPhysicalMemorySize;
    }

    public void setUsedMemory(long usedMemory) {
        this.usedMemory = usedMemory;
    }

    public int getConnectionCount() {
        return connectionCount;
    }

    public void setConnectionCount(int connectionCount) {
        this.connectionCount = connectionCount;
    }

    public int getMaxThreads() {
        return maxThreads;
    }

    public void setMaxThreads(int maxThreads) {
        this.maxThreads = maxThreads;
    }

    public int getCurrentThreadCount() {
        return currentThreadCount;
    }

    public void setCurrentThreadCount(int currentThreadCount) {
        this.currentThreadCount = currentThreadCount;
    }

    public int getCurrentThreadsBusy() {
        return currentThreadsBusy;
    }

    public void setCurrentThreadsBusy(int currentThreadsBusy) {
        this.currentThreadsBusy = currentThreadsBusy;
    }

    public int getKeepAliveCount() {
        return keepAliveCount;
    }

    public void setKeepAliveCount(int keepAliveCount) {
        this.keepAliveCount = keepAliveCount;
    }

    public int getMaxConnections() {
        return maxConnections;
    }

    public void setMaxConnections(int maxConnections) {
        this.maxConnections = maxConnections;
    }

    public int getMinSpareThreads() {
        return minSpareThreads;
    }

    public void setMinSpareThreads(int minSpareThreads) {
        this.minSpareThreads = minSpareThreads;
    }

}
