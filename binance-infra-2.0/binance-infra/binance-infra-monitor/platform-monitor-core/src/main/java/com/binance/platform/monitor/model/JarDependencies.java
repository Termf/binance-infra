package com.binance.platform.monitor.model;

import lombok.Data;

import java.util.List;

@Data
public class JarDependencies {
    String summerframeworkVersion;
    String springCloudVersion;
    String springBootVersion;
    List<PomInfo> pomInfos;
}
