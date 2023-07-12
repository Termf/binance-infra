package com.binance.master.log.layout;

import static org.junit.Assert.assertEquals;

import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import java.util.Arrays;


public class MaskUtilTest {

    @BeforeClass
    public static void init() {
        PathMatchingResourcePatternResolver pmrpr = new PathMatchingResourcePatternResolver();
        try {
            Resource[] resources = pmrpr.getResources(PathMatchingResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX + "/log/log-mask-pattern.txt");
            MaskUtil.init(resources);
        } catch (Exception e) {
        }
    }

    @Test
    public void maskSensitiveInfo() {
        String s =
                "正在调用：getApiList，参数：APIRequest[body=GetApiListRequest[id=0,userId=,apiKey=7C91FEA7ABC4FC0B2AFE53DFE6B4C1BFB786B165179205BC74A0F9825FBC27E6B0ADA41370638E9616BB32C99B362AA4B10835CA297F88D9C4F138BB9430E673617C62BE9ACBF27D9F80600122DCF62F,apiName=string],language=EN_US,terminal=WEB,version=string,token=<null>,trackingChain=string,domain=string]";
        StringBuilder ret = MaskUtil.maskSensitiveInfo(s);
        // System.out.println(ret);
        assertEquals(
                "正在调用：getApiList，参数：APIRequest[body=GetApiListRequest[id=0,userId=,apiKey=敏感(33)******,apiName=string],language=EN_US,terminal=WEB,version=string,token=<null>,trackingChain=string,domain=string]",
                ret.toString());
    }


    @Test
    public void maskSensitiveInfo2() {
        String s =
                "Invalid cookie header: \"Set-Cookie: AWSALB=lyd0Dgv95V3ohuUs5odN8RRotRUKRrAirlss592CixeVqq/qLy31ew487CrCzd4fA+DZYdSrmDDM+HiE53+IUHtTabL5yM89tpW20K4tFNrWSkx5moDeeCrEvZr+; Expires=Fri, 05 Jun 2020 15:15:46 GMT; Path=/\". Invalid 'expires' attribute: Fri, 05 Jun 2020 15:15:46 GMT";
        StringBuilder ret = MaskUtil.maskSensitiveInfo(s);
        assertEquals(
                "Invalid cookie header: \"Set-Cookie: 敏感(40)******; Expires=Fri, 05 Jun 2020 15:15:46 GMT; Path=/\". Invalid 'expires' attribute: Fri, 05 Jun 2020 15:15:46 GMT",
                ret.toString());
    }


    @Test
    public void maskSensitiveInfo3() {
        String s = "put request body field: mobile->13812282466 into sensorProp";
        StringBuilder ret = MaskUtil.maskSensitiveInfo(s);
        System.out.println(ret.toString());
        assertEquals("put request body field: mobile->敏感(41)****** into sensorProp", ret.toString());
    }

    @Test
    public void maskInit() {
        MaskUtil.init(Arrays.asList("total-users: ([0-9]+)"), "test");
        StringBuilder result = MaskUtil.maskSensitiveInfo("total-users: 678 total-devices: 121 total-ios-devices: 4 total-android-devices: 115");
        assertEquals("total-users: 敏感(0)****** total-devices: 121 total-ios-devices: 4 total-android-devices: 115", result.toString());
    }

    @Test
    public void maskWithHash() {
        MaskUtil.init(Arrays.asList("myPhone: ([0-9]+)#"), "test2");
        StringBuilder result = MaskUtil.maskSensitiveInfo("myPhone: 32049204204");
        System.out.println(result.toString());

        result = MaskUtil.maskSensitiveInfo("myPhone: 32049204204 hello, world!");
        assertEquals("myPhone: ##>104F1CEF63F0989063735AEEFDB58F29 hello, world!", result.toString());
    }

    @Test
    public void maskWithNoHash() {
        MaskUtil.init(Arrays.asList("myPhone: ([0-9]+)\\#"), "test3");
        StringBuilder result = MaskUtil.maskSensitiveInfo("myPhone: 32049204204# hello, world!");
        assertEquals("myPhone: 敏感(0)******# hello, world!", result.toString());
    }
}
