package com.binance.platform.mbx.serielize;

import com.binance.platform.mbx.matchbox.model.MbxState;
import com.binance.platform.mbx.util.JsonUtil;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

/**
 * @author Li Fenggang
 * Date: 2020/7/27 10:51 上午
 */
public class MbxStateTest {

    @Test
    public void exception() throws IOException {
        deserializeExample("{}");
        deserializeExample("{\"code\":11, \"msg\":\"error\"}");
        deserializeExample("{\"name\":\"Li\"}");
    }

    private void deserializeExample(String jsonResponse) throws IOException {
        MbxState mbxState = JsonUtil.fromJson(jsonResponse, MbxState.class);
        System.out.println(mbxState);
        Assert.assertNotNull(mbxState);
    }
}
