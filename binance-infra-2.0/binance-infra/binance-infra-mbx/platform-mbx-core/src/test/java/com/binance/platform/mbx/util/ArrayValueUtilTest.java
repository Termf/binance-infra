package com.binance.platform.mbx.util;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author Li Fenggang
 * Date: 2020/7/18 11:01 上午
 */
public class ArrayValueUtilTest {
    @Test
    public void convertArrayToString() {
        convertArrayToStringSample(null, null);
        convertArrayToStringSample("[]", Collections.emptyList());
        convertArrayToStringSample("[\"a\"]", Arrays.asList("a"));
        convertArrayToStringSample("[\"a\",\"1\",\";\"]", Arrays.asList("a", "1", ";"));
    }

    private void convertArrayToStringSample(String expect, List<String> list) {
        String result = ArrayValueUtil.convert(list);
        System.out.println(result);
        Assert.assertEquals(expect, result);
    }
}
