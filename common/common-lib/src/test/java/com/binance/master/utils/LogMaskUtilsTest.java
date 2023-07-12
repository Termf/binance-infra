package com.binance.master.utils;


import static org.junit.Assert.assertEquals;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.junit.Test;

import java.io.Serializable;
import java.util.Arrays;

public class LogMaskUtilsTest {

    @Test
    public void maskJsonString() {
        LogMaskUtils.init(Arrays.asList("myPhone","email#"));
        String ret = LogMaskUtils.maskJsonString2("{'myPhone':20398420,'email':'test@binance.com','isValid':'false'}", "");
        assertEquals("{'isValid':'false','myPhone':'***','email':'##>E0B97E6ACDFC40C5389A0BD15888F29D'}", ret);

        String ret2 = LogMaskUtils.fastMaskJsonStr("{'myPhone':20398420,'email':'test@binance.com','isValid':'false'}");
        assertEquals("{\"myPhone\":\"***\",\"email\":\"##>E0B97E6ACDFC40C5389A0BD15888F29D\",\"isValid\":\"false\"}", ret2);

    }

    @Test
    public void testFastMask() {
        LogMaskUtils.init(Arrays.asList("email#"));
        String ret = LogMaskUtils.fastMaskJsonStr("{\"language\":\"EN_US\",\"terminal\":\"WEB\",\"version\":null,\"token\":null,\"trackingChain\":\"BinanceMgs:gray:09c8b522-42f3-48b0-a663-5c4d828bd110-(AWS=1-6034bb95-676517bd3f7391a04dc219f0)--(PinPoint=10.119.181.253^1613804146497^49683705)\",\"domain\":null,\"body\":{\"email\":\"1197881504@qq.com\",\"mobile\":null,\"mobileCode\":null}}");
        assertEquals("{\"language\":\"EN_US\",\"terminal\":\"WEB\",\"version\":null,\"token\":null,\"trackingChain\":\"BinanceMgs:gray:09c8b522-42f3-48b0-a663-5c4d828bd110-(AWS=1-6034bb95-676517bd3f7391a04dc219f0)--(PinPoint=10.119.181.253^1613804146497^49683705)\",\"domain\":null,\"body\":{\"email\":\"##>C9C34D6D4A2BF46D7B7E016E97BC7036\",\"mobile\":null,\"mobileCode\":null}}", ret);

    }

    @Test
    public void testMaskObject1() {
        LogMaskUtils.init(Arrays.asList("email"));
        TestBean obj = new TestBean("test", "test@binance.com", 3);
        String ret = LogMaskUtils.maskJsonObject(obj, "");
        assertEquals("{'name':'test','age':3,'email':'***'}", ret);

        String ret2 = LogMaskUtils.maskJsonObject("hello,world", "");
        assertEquals("", ret2);

        assertEquals(null, LogMaskUtils.maskJsonObject(null, ""));
    }

    @Test
    public void testFastMaskObject1() {
        LogMaskUtils.init(Arrays.asList("email"));
        TestBean obj = new TestBean("test", "test@binance.com", 3);
        String ret = LogMaskUtils.fastMaskObject(obj);
        assertEquals("{\"name\":\"test\",\"email\":\"***\",\"age\":3}", ret);

        String ret2 = LogMaskUtils.fastMaskObject("hello,world");
        assertEquals("", ret2);

        assertEquals(null, LogMaskUtils.fastMaskObject(null));
    }

    @Test
    public void testMaskObject2() {
        LogMaskUtils.init(Arrays.asList("email"));
        TestBean obj = new TestBean("test", "test@binance.com", 3);
        String ret = LogMaskUtils.maskJsonObject(obj, "");
        assertEquals("{'name':'test','age':3,'email':'***'}", ret);

        String ret2 = LogMaskUtils.maskJsonObject("hello,world", "");
        assertEquals("", ret2);

        assertEquals(null, LogMaskUtils.maskJsonObject(null, ""));
    }


    @Test
    public void testFastMaskObject2() {
        LogMaskUtils.init(Arrays.asList("email"));
        TestBean obj = new TestBean("test", "test@binance.com", 3);
        String ret = LogMaskUtils.fastMaskObject(obj);
        assertEquals("{\"name\":\"test\",\"email\":\"***\",\"age\":3}", ret);

        String ret2 = LogMaskUtils.fastMaskObject("hello,world");
        assertEquals("", ret2);

        assertEquals(null, LogMaskUtils.fastMaskObject(null));
    }


    @Test
    public void testMaskObject3() {
        LogMaskUtils.init(Arrays.asList("email"));
        String result = "[\"clientId\",\"name\"]";
        String ret = LogMaskUtils.maskJsonString2(result, "");
        assertEquals("[\"clientId\",\"name\"]", ret);
        //assertEquals("{'name':'test','age':3,'email':'******'}", ret);
    }

    @Test
    public void testFastMaskObject3() {
        LogMaskUtils.init(Arrays.asList("email"));
        String result = "[\"clientId\",\"name\"]";
        String ret = LogMaskUtils.fastMaskObject(result);
        System.out.println(ret);
    }

    @Test
    public void testFastMaskObject(){
        LogMaskUtils.init(Arrays.asList("myPhone","email#"));
        TestBean obj = new TestBean("test", "test@binance.com", 3);
        String ret = LogMaskUtils.fastMaskObject(obj);
        assertEquals("{\"name\":\"test\",\"email\":\"##>E0B97E6ACDFC40C5389A0BD15888F29D\",\"age\":3}", ret);
    }
    @Test
    public void testPerfMaskObject(){
        LogMaskUtils.init(Arrays.asList("myPhone", "email#", "age"));
        int N = 10000;
        long start = System.currentTimeMillis();
        for (int i = 0; i < N; i++) {
            String ret = LogMaskUtils.fastMaskJsonStr("{\"myPhone\":\"13984\", \"age\":3.00000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000, \"email\":\"test@binance.com\"}");
        }
        long end = System.currentTimeMillis();
        System.out.println("fastMaskJsonStr duration: " + (end - start));

        start = System.currentTimeMillis();
        for (int i = 0; i < N; i++) {
            String ret = LogMaskUtils.maskJsonString2("{\"myPhone\":\"13984\", \"age\":3.00000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000, \"email\":\"test@binance.com\"}");
        }
        end = System.currentTimeMillis();
        System.out.println("maskJsonString2 duration2: " + (end - start));

    }

    @Data
    @AllArgsConstructor
    static class TestBean{
        String name;
        String email;
        Integer age;
    }
}