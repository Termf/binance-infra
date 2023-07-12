package com.binance.platform.monitor.model;

import lombok.Data;

@Data
public class PomDependency {
    public String groupId;
    public String artifactId;
    public String version;
    public String scope;
}
