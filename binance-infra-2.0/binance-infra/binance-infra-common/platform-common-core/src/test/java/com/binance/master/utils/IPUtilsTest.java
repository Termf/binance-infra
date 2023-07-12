package com.binance.master.utils;

import org.junit.Assert;
import org.junit.Test;

public class IPUtilsTest {

    @Test
    public void getIp() {
        String ip = IPUtils.getIp();
        Assert.assertNotNull(ip);
        Assert.assertNotEquals("127.0.0.1", ip);
    }
}
