package com.binance.platform.monitor.configuration.cloudwatch.internal;

import java.util.AbstractList;
import java.util.List;

import com.amazonaws.services.cloudwatch.model.MetricDatum;

public class MetricDatumPartition extends AbstractList<List<MetricDatum>> {
    private final List<MetricDatum> list;
    private final int partitionSize;
    private final int partitionCount;

    public MetricDatumPartition(List<MetricDatum> metricData, int partitionSize) {
        this.list = metricData;
        this.partitionSize = partitionSize;
        this.partitionCount = MathUtils.divideWithCeilingRoundingMode(list.size(), partitionSize);
    }

    public static List<List<MetricDatum>> partition(List<MetricDatum> metricData, int partitionSize) {
        return new MetricDatumPartition(metricData, partitionSize);
    }

    @Override
    public List<MetricDatum> get(int index) {
        int start = index * partitionSize;
        int end = Math.min(start + partitionSize, list.size());
        return list.subList(start, end);
    }

    @Override
    public int size() {
        return this.partitionCount;
    }

    @Override
    public boolean isEmpty() {
        return list.isEmpty();
    }
}
