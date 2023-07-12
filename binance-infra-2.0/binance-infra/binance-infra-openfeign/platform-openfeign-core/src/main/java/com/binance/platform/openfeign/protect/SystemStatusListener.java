package com.binance.platform.openfeign.protect;


import java.lang.management.ManagementFactory;


import com.sun.management.OperatingSystemMXBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 */
public class SystemStatusListener implements Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(SystemStatusListener.class);


    volatile double currentLoad = -1;
    volatile double currentCpuUsage = -1;


    public double getSystemAverageLoad() {
        return currentLoad;
    }

    public double getCpuUsage() {
        return currentCpuUsage;
    }

    @Override
    public void run() {
        try {
            OperatingSystemMXBean osBean = ManagementFactory.getPlatformMXBean(OperatingSystemMXBean.class);
            currentLoad = osBean.getSystemLoadAverage();
            /**
             * Java Doc copied from {@link OperatingSystemMXBean#getSystemCpuLoad()}:</br>
             * Returns the "recent cpu usage" for the whole system. This value is a double in the [0.0,1.0]
             * interval. A value of 0.0 means that all CPUs were idle during the recent period of time observed,
             * while a value of 1.0 means that all CPUs were actively running 100% of the time during the recent
             * period being observed. All values betweens 0.0 and 1.0 are possible depending of the activities
             * going on in the system. If the system recent cpu usage is not available, the method returns a
             * negative value.
             */
            currentCpuUsage = osBean.getSystemCpuLoad();
        } catch (Throwable e) {
            LOGGER.info("could not get system error ", e);
        }
    }

}

