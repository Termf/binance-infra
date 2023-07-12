package com.binance.platform.mbx.monitor;

import com.binance.platform.mbx.constant.LogConstants;
import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.List;

/**
 * @author Li Fenggang
 * Date: 2020/7/9 5:13 下午
 */
public class MonitorContextTest {
    @Test
    public void addLogPrefixTest() throws NoSuchFieldException, IllegalAccessException {
        MonitorContext monitorContext = new MonitorContext();
        monitorContext.addLogPrefix("A");
        monitorContext.addLogPrefix("");
        monitorContext.addLogPrefix(null);
        monitorContext.addLogPrefix("1");
        monitorContext.addLogPrefix(" ");

        List logPrefixList = getLogPrefixList(monitorContext);

        Assert.assertEquals("addLogPrefix error", logPrefixList.get(0), "A");
        Assert.assertEquals("addLogPrefix error", logPrefixList.get(1), "1");
        Assert.assertEquals("addLogPrefix error", logPrefixList.get(2), " ");
    }

    private List getLogPrefixList(MonitorContext monitorContext) throws NoSuchFieldException, IllegalAccessException {
        Field logPrefixListField = MonitorContext.class.getDeclaredField("logPrefixList");
        logPrefixListField.setAccessible(true);
        return (List)logPrefixListField.get(monitorContext);
    }

    @Test
    public void getLogPrefixTest() {
        MonitorContext monitorContext = new MonitorContext();
        monitorContext.addLogPrefix("A");
        monitorContext.addLogPrefix("B");

        String actualLogPrefix = monitorContext.getLogPrefix();

        String uuid = monitorContext.getUuid();
        String expectLogPrefix = uuid + LogConstants.LOG_MARK_DELIMITER +
                LogConstants.PREFIX_ROOT + LogConstants.LOG_MARK_DELIMITER +
                "A" + LogConstants.LOG_MARK_DELIMITER +
                "B";
        Assert.assertEquals("getLogPrefix error", expectLogPrefix, actualLogPrefix);
    }

    @Test
    public void removeLogPrefixTest1() throws NoSuchFieldException, IllegalAccessException {
        MonitorContext monitorContext = new MonitorContext();
        monitorContext.addLogPrefix("A");
        monitorContext.addLogPrefix("B");
        monitorContext.addLogPrefix("C");

        monitorContext.removeLogPrefix("C");

        List logPrefixList = getLogPrefixList(monitorContext);

        Assert.assertEquals("removeLogPrefix error", logPrefixList.size(), 2);
        Assert.assertEquals("removeLogPrefix error", logPrefixList.get(0), "A");
        Assert.assertEquals("removeLogPrefix error", logPrefixList.get(1), "B");
    }

    @Test
    public void removeLogPrefixTest2() throws NoSuchFieldException, IllegalAccessException {
        MonitorContext monitorContext = new MonitorContext();
        monitorContext.addLogPrefix("A");
        monitorContext.addLogPrefix("B");
        monitorContext.addLogPrefix("C");

        monitorContext.removeLogPrefix("A");

        List logPrefixList = getLogPrefixList(monitorContext);

        Assert.assertEquals("removeLogPrefix error", logPrefixList.size(), 0);
    }

    @Test
    public void removeLogPrefixTest3() throws NoSuchFieldException, IllegalAccessException {
        MonitorContext monitorContext = new MonitorContext();
        monitorContext.addLogPrefix("A");
        monitorContext.addLogPrefix("B");
        monitorContext.addLogPrefix("C");

        monitorContext.removeLogPrefix("");

        List logPrefixList = getLogPrefixList(monitorContext);

        Assert.assertEquals("removeLogPrefix error", logPrefixList.size(), 0);
    }

    @Test
    public void removeLogPrefixTest4() throws NoSuchFieldException, IllegalAccessException {
        MonitorContext monitorContext = new MonitorContext();
        monitorContext.addLogPrefix("A");
        monitorContext.addLogPrefix("B");
        monitorContext.addLogPrefix("C");

        monitorContext.removeLogPrefix(null);

        List logPrefixList = getLogPrefixList(monitorContext);

        Assert.assertEquals("removeLogPrefix error", logPrefixList.size(), 0);
    }
}
