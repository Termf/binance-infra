package com.binance.platform.mbx.config;

import com.binance.master.error.GeneralCode;
import com.binance.master.utils.StringUtils;
import com.binance.master.utils.WebUtils;
import com.binance.platform.mbx.cloud.consumer.BaseDataConsumer;
import com.binance.platform.mbx.cloud.model.SysConfigResp;
import com.binance.platform.mbx.exception.MbxException;
import com.binance.platform.mbx.monitor.MonitorContext;
import com.binance.platform.mbx.monitor.MonitorContextHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;

import javax.annotation.PostConstruct;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Li Fenggang
 * Date: 2020/7/14 4:41 下午
 */
public class SysConfigService implements CommandLineRunner {
    private static final Logger LOGGER = LoggerFactory.getLogger(SysConfigService.class);

    private static final String SYSTEM_MAINTENANCE = "system_maintenance";
    private static final String SYSTEM_MAINTENANCE_WHITELIST = "system_maintenance.whitelist";
    public static final String DELETE_ORDER_MAINTENANCE = "delete_order_maintenance";
    private static final int STATE_NOT_INIT = 0;
    private static final int STATE_INIT_DOING = 1;
    private static final int STATE_INIT_DONE = 2;

    private final BaseDataConsumer baseDataConsumer;

    private AtomicInteger state = new AtomicInteger(0);

    private Map<String, String> configCache = Collections.emptyMap();

    private volatile Set<String> whitelistSet = new HashSet<>();

    public SysConfigService(BaseDataConsumer baseDataConsumer) {
        this.baseDataConsumer = baseDataConsumer;
    }

    @PostConstruct
    public void init() {
        boolean result = state.compareAndSet(STATE_NOT_INIT, STATE_INIT_DOING);
        if (result) {
            MonitorContextHolder.set(new MonitorContext());
            try {
                List<SysConfigResp> allSysConfig = baseDataConsumer.getAllSysConfig();
                if (allSysConfig != null) {
                    Map<String, String> freshConfigCache = new HashMap<>();
                    for (SysConfigResp sysConfigResp : allSysConfig) {
                        String displayName = sysConfigResp.getDisplayName();
                        String code = sysConfigResp.getCode();
                        freshConfigCache.put(displayName, code);
                        if (SYSTEM_MAINTENANCE_WHITELIST.equals(displayName)) {
                            parseSystemMaintenanceWhitelist(code);
                        }
                    }
                    if (!freshConfigCache.isEmpty()) {
                        configCache = freshConfigCache;
                    }
                }
                state.set(STATE_INIT_DONE);
            } catch (Exception e) {
                LOGGER.warn(e.getMessage(), e);
                state.set(STATE_NOT_INIT);
            } finally {
                MonitorContextHolder.set(null);
            }
        }
    }

    private void parseSystemMaintenanceWhitelist(String code) {
        if (StringUtils.isNotBlank(code)) {
            Set<String> newSet = new HashSet<>();
            String[] whiteIps = code.split(",");
            for (String ip : whiteIps) {
                newSet.add(ip);
            }
            whitelistSet = newSet;
            if (LOGGER.isDebugEnabled()) {
                LOGGER.info("reloading whitelist for system maintenance. whitelist: {}", whitelistSet);
            }
        } else {
            whitelistSet = new HashSet<>();
            if (LOGGER.isDebugEnabled()) {
                LOGGER.info("no whitelist was specified for system maintenance, which can be set in sys_config by key {}", SYSTEM_MAINTENANCE_WHITELIST);
            }
        }
    }

    /**
     * Return value for the key
     *
     * @param key
     * @return
     */
    public String getValue(String key) {
        if (configCache.size() == 0) {
            init();
        }
        return configCache.get(key);
    }

    /**
     * Return true if {@link #SYSTEM_MAINTENANCE} is set to "1"
     *
     * @return
     */
    public boolean isSystemMaintenance() {
        String value = getValue(SYSTEM_MAINTENANCE);
        return Objects.equals(value, "1");
    }

    public boolean isRequestIpInWhiteListWhenSystemMaintenance() {
        if (!whitelistSet.isEmpty()) {
            String requestIp = WebUtils.getRequestIp();
            if (StringUtils.isNotBlank(requestIp) && whitelistSet.contains(requestIp)) {
                LOGGER.info("hit whitelist in system maintenance mode. requestIp: {}", requestIp);
                return true;
            }
        }
        return false;
    }

    /**
     * If {@link #isSystemMaintenance()} return true, throw an MbxException with {@link GeneralCode#SYS_MAINTENANCE}
     */
    public void checkSystemMaintenance() throws MbxException {
        if (isSystemMaintenance() && !isRequestIpInWhiteListWhenSystemMaintenance()) {
            throw new MbxException(GeneralCode.SYS_MAINTENANCE);
        }
    }

    /**
     * Return true if {@link #DELETE_ORDER_MAINTENANCE} is set to "1"
     *
     * @return
     */
    public boolean isDeleteOrderMaintenance() {
        String value = getValue(DELETE_ORDER_MAINTENANCE);
        return Objects.equals(value, "1");
    }

    /**
     * If {@link #isDeleteOrderMaintenance()} return true, throw an MbxException with {@link GeneralCode#SYS_MAINTENANCE}
     */
    public void checkDeleteOrderMaintenance() {
        if (isDeleteOrderMaintenance() && !isRequestIpInWhiteListWhenSystemMaintenance()) {
            throw new MbxException(GeneralCode.SYS_MAINTENANCE);
        }
    }

    @Override
    public void run(String... args) throws Exception {
        Executors.newScheduledThreadPool(1).scheduleAtFixedRate(new Runnable() {

            @Override
            public void run() {
                if (!state.compareAndSet(STATE_INIT_DONE, STATE_NOT_INIT) || state.get() == STATE_NOT_INIT) {
                    init();
                }
            }
        }, 0, 60, TimeUnit.SECONDS);
    }
}
