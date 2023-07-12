package com.binance.platform.monitor.model;

import lombok.Data;

@Data
public class HaloMetricResponse {

    private String jvmThreadslive;

    private String jvmMemoryUsedHeap;

    private String jvmMemoryUsedNonHeap;

    private String heapCommitted;

    private String nonheapCommitted;

    private String heapInit;

    private String heapMax;

    private String gcPsMarksweepCount;

    private String gcPsMarksweepTime;

    private String gcPsScavengeCount;

    private String gcPsScavengeTime;

    private String systemloadAverage;

    private String processors;

}
