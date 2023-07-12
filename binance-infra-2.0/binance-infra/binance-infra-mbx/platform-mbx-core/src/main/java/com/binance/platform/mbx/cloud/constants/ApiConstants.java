package com.binance.platform.mbx.cloud.constants;

import java.util.Arrays;
import java.util.List;

/**
 * @author Li Fenggang
 * Date: 2020/7/27 10:01 上午
 */
public interface ApiConstants {
    String SERVICE_ID_BASS_DATA = "basedata";
    String SERVICE_ID_ACCOUNT = "account";
    String SERVICE_ID_STREAMER_WEB = "streamer-web";
    String SERVICE_ID_ASSET_SERVICE = "asset-service";

    List<String> LOG_IGNORE_LIST = Arrays.asList(SERVICE_ID_BASS_DATA);

    /**
     * Check if the log of the service should be record.
     *
     * @param serviceId
     * @return true, record; false, not record.
     */
    static boolean loggable(String serviceId) {
        if (!LOG_IGNORE_LIST.contains(serviceId)) {
            return true;
        }

        return false;
    }
}
