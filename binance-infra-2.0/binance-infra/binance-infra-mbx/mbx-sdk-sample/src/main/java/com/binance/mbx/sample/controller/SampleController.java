package com.binance.mbx.sample.controller;

import com.binance.master.models.APIRequest;
import com.binance.platform.mbx.MbxClients;
import com.binance.platform.mbx.client.AccountClient;
import com.binance.platform.mbx.client.DepthClient;
import com.binance.platform.mbx.exception.MbxException;
import com.binance.platform.mbx.matchbox.model.MbxResponse;
import com.binance.platform.mbx.matchbox.model.MbxState;
import com.binance.platform.mbx.model.account.BalanceLedgerResponse;
import com.binance.platform.mbx.model.account.GetBlanceLedgerRequest;
import com.binance.platform.mbx.model.account.RepairLockedRequest;
import com.binance.platform.mbx.model.depth.DepthResponse;
import com.binance.platform.mbx.model.depth.GetDepthRequest;
import com.binance.platform.mbx.model.matchbox.GetAccountRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @Author Li Fenggang
 * @Date 2020/7/8 10:32 上午
 */
@RestController
public class SampleController {
    private static final Logger LOGGER = LoggerFactory.getLogger(SampleController.class);

    // 注入方式1：注入入口类，其他API接口实例都可以通过它获取到
    @Autowired
    private MbxClients mbxClients;

    // 注入方式2：直接注入指定的API接口实例
    @Autowired
    private AccountClient accountClient;

    @Autowired
    private DepthClient depthClient;

    @GetMapping("/getBalanceLadger")
    public String getBalanceLadger(long userId, String asset) {
        GetBlanceLedgerRequest request = new GetBlanceLedgerRequest(userId, asset);
        // 查询近1小时的数据
        long currentMs = System.currentTimeMillis();
        long startMs = currentMs - TimeUnit.HOURS.toMillis(1);
        Date startTime = new Date(startMs);
        Date endTime = new Date(currentMs);
        request.setStartTime(startTime);
        request.setEndTime(endTime);

        MbxResponse<List<BalanceLedgerResponse>> response = accountClient.getBalanceLedger(request);
        // 本地参数校验失败，网络异常等异常会抛出运行时异常MbxException
        // Match box服务端返回的错误码会存储在MbxResponse的state属性中
        if (response.isSuccess()) {
            List<BalanceLedgerResponse> list = response.getData();
            StringBuilder stringBuilder = new StringBuilder("list is:");
            for (BalanceLedgerResponse balanceLedgerResponse : list) {
                stringBuilder.append(balanceLedgerResponse).append(";");
            }
            return stringBuilder.toString();
        } else {
            // Match box服务端返回的响应码
            MbxState state = response.getState();
            state.getCode();
            state.getMsg();
        }

        return response.getState().toString();
    }

    @GetMapping("/putAccountAsset")
    public String putAccountAsset(long userId, String asset, BigDecimal available) throws MbxException {
        RepairLockedRequest repairLockedRequest = new RepairLockedRequest(userId, asset, available);

        MbxResponse<Void> mbxResponse = mbxClients.getAccountClient().repairLocked(repairLockedRequest);

        return mbxResponse.toString();
    }

    @GetMapping("/rest/getDepth")
    public String getDepth(@RequestParam("symbol") String symbol) {
        GetDepthRequest request = new GetDepthRequest(symbol);
        request.setLimit(5);
        MbxResponse<DepthResponse> depth = depthClient.getDepth(request);
        return depth.toString();
    }

    @GetMapping("/api/getAccountByUserId")
    public String getAccountByUserId(APIRequest<GetAccountRequest> apiRequest) {
        return apiRequest.getBody().toString();
    }
}
