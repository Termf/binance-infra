package com.binance.platform.monitor.metric.hazelcast;

import com.binance.platform.monitor.metric.CustomizerTag;
import com.hazelcast.core.HazelcastInstance;

import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tags;
import io.micrometer.core.instrument.binder.MeterBinder;

public class HazelCastInstanceMetrics implements MeterBinder {
    private HazelcastInstance hazelcastInstance;
    private String name;
    private CustomizerTag platformTag;

    public HazelCastInstanceMetrics(HazelcastInstance hazelcastInstance, String name, CustomizerTag platformTag) {
        this.hazelcastInstance = hazelcastInstance;
        this.name = name;
        this.platformTag = platformTag;
    }

    @Override
    public void bindTo(MeterRegistry registry) {

        Tags tags =
            Tags.of("name", name, "h_address", hazelcastInstance.getCluster().getLocalMember().getAddress().getHost())
                .and(platformTag.getTags());
        Gauge
            .builder("hazelcast.cluster.state", hazelcastInstance,
                hazelcastInstance -> hazelcastInstance.getCluster().getClusterState().ordinal())
            .tags(tags).description("ClusterState of HazelCast ").register(registry);
        Gauge
            .builder("hazelcast.client.connected.count", hazelcastInstance,
                hazelcastInstance -> hazelcastInstance.getClientService().getConnectedClients().size())
            .tags(tags).description("all connected clients to this member ").register(registry);
        Gauge
            .builder("hazelcast.partition.count", hazelcastInstance,
                hazelcastInstance -> hazelcastInstance.getPartitionService().getPartitions().size())
            .tags(tags).description("all partitions in the cluster").register(registry);
    }

}
