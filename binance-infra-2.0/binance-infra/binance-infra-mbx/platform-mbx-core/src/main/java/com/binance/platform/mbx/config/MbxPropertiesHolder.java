package com.binance.platform.mbx.config;

import com.binance.master.error.GeneralCode;
import com.binance.platform.apollo.ApolloApplicationRunListener;
import com.binance.platform.mbx.exception.MbxException;
import com.binance.platform.mbx.util.JsonUtil;
import com.ctrip.framework.apollo.Config;
import com.ctrip.framework.apollo.ConfigChangeListener;
import com.ctrip.framework.apollo.ConfigService;
import com.ctrip.framework.apollo.enums.PropertyChangeType;
import com.ctrip.framework.apollo.model.ConfigChange;
import com.ctrip.framework.apollo.model.ConfigChangeEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author Li Fenggang
 * Date: 2020/7/31 7:24 下午
 */
public class MbxPropertiesHolder {
    private static final Logger LOGGER = LoggerFactory.getLogger(MbxPropertiesHolder.class);
    private static final AtomicBoolean started = new AtomicBoolean(false);
    /**
     * Property name for {@link MbxProperties}. Its value is {@value}.
     */
    static final String MBX_PROPERTY_NAME = "com.binance.intra.mbx.dynamic";
    private static MbxProperties mbxProperties;
    private static Config CommonConfig;

    public static MbxProperties getMbxProperties() {
        return mbxProperties;
    }

    static MbxProperties parseMbxProperties(String json) {
        if (json == null) {
            String msg = "The property \"" + MBX_PROPERTY_NAME + "\" is not configured correctly. Its value is null.";
            LOGGER.error(msg);
            return null;
        }
        try {
            MbxProperties mbxProperties = JsonUtil.fromJson(json, MbxProperties.class);
            return mbxProperties;
        } catch (IOException e) {
            LOGGER.error("The property \"" + MBX_PROPERTY_NAME + "\" can't be parsed normally.", e);
        }
        return null;
    }

    static void init() {
        if (started.compareAndSet(false, true)) {
            CommonConfig = ConfigService.getConfig(ApolloApplicationRunListener.CONFIGCENTER_INFRA_NAMESPACE);
            String property = CommonConfig.getProperty(MBX_PROPERTY_NAME, null);
            mbxProperties = parseMbxProperties(property);
            if (mbxProperties == null) {
                throw new MbxException(GeneralCode.SYS_ERROR, "MbxProperties is not configured correctly.");
            }

            ConfigChangeListener changeListener = new ConfigChangeListener() {
                @Override
                public void onChange(ConfigChangeEvent changeEvent) {
                    ConfigChange change = changeEvent.getChange(MBX_PROPERTY_NAME);
                    if (change != null) {
                        PropertyChangeType changeType = change.getChangeType();
                        if (changeType == PropertyChangeType.ADDED || changeType == PropertyChangeType.MODIFIED) {
                            String newValue = change.getNewValue();
                            MbxProperties mbxProperties = parseMbxProperties(newValue);
                            if (mbxProperties == null) {
                                LOGGER.warn("The new configuration of MbxProperties is wrong and ignored.");
                                return;
                            }
                            MbxPropertiesHolder.mbxProperties = mbxProperties;
                            LOGGER.info("MbxProperties changed. {}", mbxProperties.toString());
                        }
                    }
                }
            };
            CommonConfig.addChangeListener(changeListener);
        }
    }

}
