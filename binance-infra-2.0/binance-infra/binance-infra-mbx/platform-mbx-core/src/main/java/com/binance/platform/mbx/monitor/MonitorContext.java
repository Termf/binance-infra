package com.binance.platform.mbx.monitor;

import com.binance.platform.common.TrackingUtils;
import com.binance.platform.mbx.constant.LogConstants;

import java.util.ArrayList;
import java.util.List;

import static com.binance.platform.mbx.constant.LogConstants.LOG_MARK_DELIMITER;

/**
 *
 * @author Li Fenggang
 * Date: 2020/7/9 6:48 上午
 */
public class MonitorContext {
    private String entrance;
    private String uuid;
    private String logPrefix;
    private List<String> logPrefixList = new ArrayList<>();

    public String getEntrance() {
        return entrance;
    }

    public void setEntrance(String entrance) {
        this.entrance = entrance;
    }

    public String getUuid() {
        if (uuid != null) {
            return uuid;
        }
        uuid = TrackingUtils.generateUUID();
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    /**
     * Join the log prefixes with the delimiter {@link LogConstants#LOG_MARK_DELIMITER}
     *
     * @return
     */
    public String getLogPrefix() {
        if (logPrefix != null) {
            return logPrefix;
        }
        StringBuilder builder = new StringBuilder(getUuid());
        builder.append(LOG_MARK_DELIMITER);
        builder.append(LogConstants.PREFIX_ROOT);
        for (String prefix : logPrefixList) {
            builder.append(LOG_MARK_DELIMITER).append(prefix);
        }
        logPrefix = builder.toString();
        return logPrefix;
    }

    public String getLogPrefix(String logPrefix) {
        if (logPrefix == null || logPrefix.isEmpty()) {
            return getLogPrefix();
        }
        return getLogPrefix() + LOG_MARK_DELIMITER + logPrefix;
    }

    /**
     * Add a new logPrefix to the tail of the log prefix list.
     *
     * @param logPrefix null and empty will be ignored.
     */
    public void addLogPrefix(String logPrefix) {
        if (logPrefix != null && !logPrefix.isEmpty()) {
            logPrefixList.add(logPrefix);
            this.logPrefix = null;
        }
    }

    /**
     * Remove logPrefixes from the tail of the list, until the specified one is found.
     * The specified one will also be removed.
     *
     * @param logPrefix
     */
    public void removeLogPrefix(String logPrefix) {
        int size = logPrefixList.size();
        for (int i = size -1; i >= 0; i--) {
            String item = logPrefixList.get(i);
            logPrefixList.remove(i);
            if (item.equals(logPrefix)) {
                break;
            }
        }

        this.logPrefix = null;
    }
}
