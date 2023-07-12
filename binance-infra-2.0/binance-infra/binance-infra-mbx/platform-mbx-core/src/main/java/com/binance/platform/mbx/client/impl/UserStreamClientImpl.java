package com.binance.platform.mbx.client.impl;

import com.binance.platform.mbx.client.UserStreamClient;
import com.binance.platform.mbx.config.SysConfigService;
import com.binance.platform.mbx.matchbox.MatchBoxManagementService;
import com.binance.platform.mbx.matchbox.model.MbxResponse;
import com.binance.platform.mbx.matchbox.model.mgmt.MbxDeleteUserDataStreamRequest;
import com.binance.platform.mbx.matchbox.model.mgmt.MbxPostUserDataStreamRequest;
import com.binance.platform.mbx.matchbox.model.mgmt.MbxPostUserDataStreamResponse;
import com.binance.platform.mbx.matchbox.model.mgmt.MbxPutUserDataStreamRequest;
import com.binance.platform.mbx.model.matchbox.PostUserDataStreamResponse;
import com.binance.platform.mbx.model.userstream.DeleteStreamRequest;
import com.binance.platform.mbx.model.userstream.InternalPingStreamRequest;
import com.binance.platform.mbx.model.userstream.PingSteamResponse;
import com.binance.platform.mbx.model.userstream.PingStreamRequest;
import com.binance.platform.mbx.model.userstream.StartStreamRequest;
import com.binance.platform.mbx.model.userstream.StartStreamResponse;
import com.binance.platform.mbx.service.AccountService;

/**
 * @author Li Fenggang
 * Date: 2020/7/16 10:07 上午
 */
public class UserStreamClientImpl implements UserStreamClient {
    private final MatchBoxManagementService matchBoxManagementService;
    private final SysConfigService sysConfigService;
    private final AccountService accountService;

    public UserStreamClientImpl(MatchBoxManagementService matchBoxManagementService, SysConfigService sysConfigService, AccountService accountService) {
        this.matchBoxManagementService = matchBoxManagementService;
        this.sysConfigService = sysConfigService;
        this.accountService = accountService;
    }

    @Override
    public MbxResponse<StartStreamResponse> internalStartStream(StartStreamRequest request) throws Exception {
        sysConfigService.checkSystemMaintenance();

        return startStream(request);
    }

    @Override
    public MbxResponse<StartStreamResponse> startStream(StartStreamRequest request) throws Exception {
        MbxResponse<PostUserDataStreamResponse> mbxResponse = postUserDataStreamForString(request.getUserId());
        MbxResponse<StartStreamResponse> response = MbxResponse.of(mbxResponse.getState());
        if (response.isSuccess()) {
            StartStreamResponse startStreamResponse = new StartStreamResponse();
            startStreamResponse.setListenKey(mbxResponse.getData().getListenKey());
            response.setData(startStreamResponse);
        }
        return response;
    }

    private MbxResponse<PostUserDataStreamResponse> postUserDataStreamForString(long userId) {
        Long tradingAccountId = accountService.retrieveTradingAccountIdByUserId(userId);

        MbxPostUserDataStreamRequest mbxRequest = new MbxPostUserDataStreamRequest(tradingAccountId);
        MbxResponse<MbxPostUserDataStreamResponse> mbxResponse = matchBoxManagementService.postUserDataStream(mbxRequest);

        MbxResponse<PostUserDataStreamResponse> response = MbxResponse.of(mbxResponse.getState());
        if (response.isSuccess()) {
            PostUserDataStreamResponse postBalanceResponse = new PostUserDataStreamResponse();
            postBalanceResponse.setListenKey(mbxResponse.getData().getListenKey());
            response.setData(postBalanceResponse);
        }
        return response;
    }

    @Override
    public MbxResponse<Void> internalPingStream(InternalPingStreamRequest request) throws Exception {
        Long tradingAccountId = accountService.retrieveTradingAccountIdByUserId(request.getUserId());

        MbxPutUserDataStreamRequest mbxRequest = new MbxPutUserDataStreamRequest(request.getListenKey(), tradingAccountId);
        MbxResponse<Void> mbxResponse = matchBoxManagementService.putUserDataStream(mbxRequest);

        return mbxResponse;
    }

    @Override
    public MbxResponse<PingSteamResponse> pingStream(PingStreamRequest request) throws Exception {
        sysConfigService.checkSystemMaintenance();

        MbxResponse<PostUserDataStreamResponse> mbxResponse = postUserDataStreamForString(request.getUserId());
        MbxResponse<PingSteamResponse> response = MbxResponse.of(mbxResponse.getState());
        if (response.isSuccess()) {
            PingSteamResponse pingSteamResponse = new PingSteamResponse();
            pingSteamResponse.setListenKey(mbxResponse.getData().getListenKey());
            response.setData(pingSteamResponse);
        }

        return response;
    }

    @Override
    public MbxResponse<Void> deleteStream(DeleteStreamRequest request) throws Exception {
        Long tradingAccountId = accountService.retrieveTradingAccountIdByUserId(request.getUserId());

        MbxDeleteUserDataStreamRequest mbxRequest = new MbxDeleteUserDataStreamRequest(request.getListenKey(), tradingAccountId);
        MbxResponse<Void> mbxResponse = matchBoxManagementService.deleteUserDataStream(mbxRequest);
        return mbxResponse;
    }
}
