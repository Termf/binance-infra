package com.binance.platform.mbx.client;

import com.binance.platform.mbx.matchbox.model.MbxResponse;
import com.binance.platform.mbx.model.userstream.DeleteStreamRequest;
import com.binance.platform.mbx.model.userstream.InternalPingStreamRequest;
import com.binance.platform.mbx.model.userstream.PingSteamResponse;
import com.binance.platform.mbx.model.userstream.PingStreamRequest;
import com.binance.platform.mbx.model.userstream.StartStreamRequest;
import com.binance.platform.mbx.model.userstream.StartStreamResponse;
import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Li Fenggang
 * Date: 2020/7/20 8:45 上午
 */
//@RunWith(SpringRunner.class)
//@SpringBootTest(classes = Application.class)
public class UserStreamClientTest {
    private static final long DEFAULT_USER_ID = 350788204;
    public static final String LISTEN_KEY = "SSeQ3QlQAMhOBwkJrrzBPt9sGK0X8ZNjKMCyuuHRZV3RX4BbqfSEHXGOdV7R";
    @Autowired
    private UserStreamClient userStreamClient;

    // @Test
    public void internalStartStream() throws Exception {
        StartStreamRequest request = new StartStreamRequest(DEFAULT_USER_ID);
        MbxResponse<StartStreamResponse> mbxResponse = userStreamClient.internalStartStream(request);
        System.out.println(mbxResponse);
        Assert.assertTrue(mbxResponse.isSuccess());
    }

    // @Test
    public void startStream() throws Exception {
        StartStreamRequest request = new StartStreamRequest(DEFAULT_USER_ID);
        MbxResponse<StartStreamResponse> mbxResponse = userStreamClient.startStream(request);
        System.out.println(mbxResponse);
        Assert.assertTrue(mbxResponse.isSuccess());
    }

    // @Test
    public void internalPingStream() throws Exception {
        InternalPingStreamRequest request = new InternalPingStreamRequest(LISTEN_KEY, DEFAULT_USER_ID);
        MbxResponse<Void> mbxResponse = userStreamClient.internalPingStream(request);
        System.out.println(mbxResponse);
        Assert.assertTrue(mbxResponse.isSuccess());
    }

    // @Test
    public void pingStream() throws Exception {
        PingStreamRequest request = new PingStreamRequest(DEFAULT_USER_ID);
        MbxResponse<PingSteamResponse> mbxResponse = userStreamClient.pingStream(request);
        System.out.println(mbxResponse);
        Assert.assertTrue(mbxResponse.isSuccess());
    }

    // @Test
    public void deleteStream() throws Exception {
        DeleteStreamRequest request = new DeleteStreamRequest(DEFAULT_USER_ID, LISTEN_KEY);
        MbxResponse<Void> mbxResponse = userStreamClient.deleteStream(request);
        System.out.println(mbxResponse);
        Assert.assertTrue(mbxResponse.isSuccess());
    }
}
