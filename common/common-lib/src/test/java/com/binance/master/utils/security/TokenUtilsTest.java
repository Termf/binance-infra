package com.binance.master.utils.security;

import org.junit.Assert;
import org.junit.Test;

public class TokenUtilsTest {

    @Test
    public void emailRedisToken() {
        String token = TokenUtils.emailRedisToken();
        Assert.assertNotNull(token);
        System.out.println(token);
        Assert.assertEquals(46, token.length());
    }

}