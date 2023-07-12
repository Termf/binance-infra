package com.binance.sapi.sample.controller;

import com.binance.sapi.sample.model.TransferRequest;
import com.binance.sapi.sample.sapi.SapiApi;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import retrofit2.Response;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * binance-sample
 *
 * @author Thomas Li
 * Date: 2021/7/16
 */
@RestController
public class SampleController {
    private static final Logger log = LoggerFactory.getLogger(SampleController.class);
    public static final String API_KEY = "paE8a7zGNr9b9YTTsCy97yYbX5RMrDQXRTqWcex1ejwo3iIcsT77NtWGkt0YDpMu";
    public static final String SECRET_KEY = "W8twXJDUc6Alt1HZFaG2EEBpOpXii1MvYnpcNGm3df3pMMan9KcN8PKBPgWjdygM";
    @Resource
    private SapiApi sapiApi;

    @GetMapping("/sample")
    public String sample(@ApiParam(defaultValue = API_KEY) @RequestParam("apiKey") String apiKey,
                         @ApiParam(defaultValue = SECRET_KEY) @RequestParam("secretKey") String secretKey) {
        Map<String,String> asset = new HashMap<String,String>();
        asset.put("asset", "BNB");
        asset.put("timestamp", Long.toString(System.currentTimeMillis()));
        asset.put("recvWindow", "5000");
        Response<String> assetDetail = sapiApi.assetDetail(apiKey, secretKey, asset);
        log.info("assetDetail: {}", assetDetail.body());


        // transfer
        TransferRequest request = new TransferRequest();
        request.setAmount(0.001);
        request.setAsset("USDT");
        request.setType("MAIN_MARGIN");
        Response<String> transfer = sapiApi.transfer(apiKey, secretKey, request);
        log.info("transfer: {}", transfer.body());

        return "OK";
    }
}
