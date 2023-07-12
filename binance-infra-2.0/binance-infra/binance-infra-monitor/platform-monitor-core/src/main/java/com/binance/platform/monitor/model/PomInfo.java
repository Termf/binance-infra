package com.binance.platform.monitor.model;

import lombok.Data;

import java.util.List;

@Data
public class PomInfo {
    public String groupId;
    public String artifactId;
    public String version;
    public String location;
    public long size;
    public List<PomDependency> dependencies;
}
