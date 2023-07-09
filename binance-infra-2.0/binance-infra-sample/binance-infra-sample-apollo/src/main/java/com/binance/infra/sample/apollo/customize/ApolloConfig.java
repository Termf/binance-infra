package com.binance.infra.sample.apollo.customize;

import com.ctrip.framework.apollo.Config;
import com.ctrip.framework.apollo.ConfigService;
import com.ctrip.framework.apollo.model.ConfigChange;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

/**
 * @author lucas.w
 * @version 1.0.0
 * @since 2021-08-16
 */
@Component
public class ApolloConfig {

    private Config getConfigService(){
        return ConfigService.getAppConfig();
    }

    /**
     * To observe a key in apollo config center, do handler whenever it changes.
     * @param key the key in apollo namespace(application as default)
     * @param handler what to do when value changes
     */
    public void observe(String key, Consumer<String> handler){
        getConfigService().addChangeListener(event -> {
            if(event.isChanged(key)){
                ConfigChange change = event.getChange(key);
                String json = change.getNewValue();
                handler.accept(json);
            }
        });
        handler.accept(getConfigService().getProperty(key, null));
    }
}
