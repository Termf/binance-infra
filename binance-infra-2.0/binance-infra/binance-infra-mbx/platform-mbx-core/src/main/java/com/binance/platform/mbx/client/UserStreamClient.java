package com.binance.platform.mbx.client;

import com.binance.platform.mbx.matchbox.model.MbxResponse;
import com.binance.platform.mbx.model.userstream.DeleteStreamRequest;
import com.binance.platform.mbx.model.userstream.InternalPingStreamRequest;
import com.binance.platform.mbx.model.userstream.PingSteamResponse;
import com.binance.platform.mbx.model.userstream.PingStreamRequest;
import com.binance.platform.mbx.model.userstream.StartStreamRequest;
import com.binance.platform.mbx.model.userstream.StartStreamResponse;
import org.springframework.web.bind.annotation.PostMapping;

public interface UserStreamClient {

    /**
     * userStream：输入userId
     *
     * @param request
     * @return
     * @throws Exception
     */
    @PostMapping(value = "/mgmt/startStream")
    MbxResponse<StartStreamResponse> internalStartStream(StartStreamRequest request)
            throws Exception;

    /**
     * userStream：输入userId
     *
     * @param request
     * @return
     * @throws Exception
     */
    @PostMapping(value = "/private/startStream")
    MbxResponse<StartStreamResponse> startStream(StartStreamRequest request) throws Exception;

    /**
     * pingStream：输入userId
     *
     * @param request
     * @return
     * @throws Exception
     */
    @PostMapping(value = "/mgmt/pingStream")
    MbxResponse<Void> internalPingStream(InternalPingStreamRequest request) throws Exception;

    /**
     * pingStream：输入userId
     *
     * @param request
     * @return
     * @throws Exception
     */
    @PostMapping(value = "/private/pingStream")
    MbxResponse<PingSteamResponse> pingStream(PingStreamRequest request) throws Exception;

    /**
     * deleteStream：输入userId,listenKey
     *
     * @param request
     * @return
     * @throws Exception
     */
    @PostMapping(value = "/mgmt/deleteStream")
    MbxResponse<Void> deleteStream(DeleteStreamRequest request) throws Exception;
}
