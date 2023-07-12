package com.binance.sapi.sample.sapi;

import com.binance.platform.sapi.annotation.Sapi;
import com.binance.platform.sapi.constant.Headers;
import com.binance.sapi.sample.model.TransferRequest;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;

import java.util.Map;

/**
 * binance-infra-whole
 *
 * @author Thomas Li
 * Date: 2021/7/21
 */
@Sapi(baseUrl = "${sapi.url}")
public interface SapiApi {

    @GET("/sapi/v1/asset/assetDetail")
    Response<String> assetDetail(@Header(Headers.API_KEY) String apiKey, @Header(Headers.SECURITY_KEY) String secretKey,
                                 @QueryMap Map<String, String> request);

    @POST("/sapi/v1/asset/transfer")
    Response<String> transfer(@QueryMap Map<String, String> request);

    @POST("/sapi/v1/asset/transfer")
    Response<String> transfer(@Header(Headers.API_KEY) String apiKey, @Header(Headers.SECURITY_KEY) String secretKey,
                              @Body TransferRequest qRequest);

}
