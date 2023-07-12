package com.binance.platform.mbx.matchbox.config;

import com.binance.platform.mbx.matchbox.MatchBoxManagementService;
import com.binance.platform.mbx.matchbox.MatchBoxRestService;
import com.binance.platform.mbx.matchbox.processor.MbxRequestProcessor;
import com.binance.platform.mbx.matchbox.rpc.MbxCaller;
import com.binance.platform.mbx.matchbox.rpc.impl.OkHttp3MbxCaller;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Li Fenggang
 * Date: 2020/7/13 7:11 下午
 */
@Configuration
public class MatchBoxClientAutoConfiguration {

    @Bean
    public MbxCaller mbxCaller() {
        return new OkHttp3MbxCaller();
    }

    @Bean
    public MatchBoxManagementService matchBoxManagementService(MbxCaller mbxCaller) {
        MbxRequestProcessor mbxRequestProcessor = new MbxRequestProcessor(mbxCaller);
        return new MatchBoxManagementService(mbxRequestProcessor);
    }

    @Bean
    public MatchBoxRestService matchBoxRestService(MbxCaller mbxCaller) {
        MbxRequestProcessor mbxRequestProcessor = new MbxRequestProcessor(mbxCaller);
        MatchBoxRestService matchBoxRestService = new MatchBoxRestService(mbxRequestProcessor);
        return matchBoxRestService;
    }
}
