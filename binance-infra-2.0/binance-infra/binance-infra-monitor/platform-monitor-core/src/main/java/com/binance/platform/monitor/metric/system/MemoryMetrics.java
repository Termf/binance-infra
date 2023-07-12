package com.binance.platform.monitor.metric.system;

import java.util.Collections;
import java.util.Map;

import com.binance.platform.monitor.utils.FileUtil;

import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tag;
import io.micrometer.core.instrument.binder.MeterBinder;

public class MemoryMetrics implements MeterBinder {

    private static final String DOCKER_MEMORY_USAGE = "/sys/fs/cgroup/memory/memory.usage_in_bytes";

    private static final String DOCKER_MEMORY_LIMIT = "/sys/fs/cgroup/memory/memory.limit_in_bytes";

    private static final String DOCKER_MEMORY_STAT = "/sys/fs/cgroup/memory/memory.stat";

    private final Iterable<Tag> tags;

    public MemoryMetrics() {
        this(Collections.emptyList());

    }

    public MemoryMetrics(Iterable<Tag> tags) {
        this.tags = tags;
    }

    public void bindTo(MeterRegistry meterRegistry) {
        try {
            Gauge.builder("system.memory.total", () -> FileUtil.getLongFromFile(DOCKER_MEMORY_LIMIT)).tags(tags)
                .baseUnit("bytes").description("Memory available").register(meterRegistry);
            Gauge.builder("system.memory.used", () -> FileUtil.getLongFromFile(DOCKER_MEMORY_USAGE)).tags(tags)
                .baseUnit("bytes").description("Memory available").register(meterRegistry);

            final String stat1 = "total_pgfault";
            final String stat2 = "total_unevictable";
            final String stat3 = "total_pgpgout";
            final String stat4 = "total_rss";
            final String stat5 = "total_inactive_anon";
            final String stat6 = "total_swap";
            final String stat7 = "total_mapped_file";
            final String stat8 = "total_rss_huge";
            final String stat9 = "total_inactive_file";
            final String stat10 = "total_pgpgin";
            final String stat11 = "total_dirty";
            final String stat12 = "total_active_file";
            final String stat13 = "total_cache";
            final String stat14 = "total_shmem";
            final String stat15 = "total_pgmajfault";
            final String stat16 = "total_writeback";
            final String stat17 = "total_active_anon";

            Gauge.builder("system.memory.detail", () -> getValueByStat(stat1)).tags("stat", stat1).baseUnit("bytes")
                .description("Memory available").register(meterRegistry);
            Gauge.builder("system.memory.detail", () -> getValueByStat(stat2)).tags("stat", stat2).baseUnit("bytes")
                .description("Memory available").register(meterRegistry);
            Gauge.builder("system.memory.detail", () -> getValueByStat(stat3)).tags("stat", stat3).baseUnit("bytes")
                .description("Memory available").register(meterRegistry);
            Gauge.builder("system.memory.detail", () -> getValueByStat(stat4)).tags("stat", stat4).baseUnit("bytes")
                .description("Memory available").register(meterRegistry);
            Gauge.builder("system.memory.detail", () -> getValueByStat(stat5)).tags("stat", stat5).baseUnit("bytes")
                .description("Memory available").register(meterRegistry);
            Gauge.builder("system.memory.detail", () -> getValueByStat(stat6)).tags("stat", stat6).baseUnit("bytes")
                .description("Memory available").register(meterRegistry);
            Gauge.builder("system.memory.detail", () -> getValueByStat(stat7)).tags("stat", stat7).baseUnit("bytes")
                .description("Memory available").register(meterRegistry);
            Gauge.builder("system.memory.detail", () -> getValueByStat(stat8)).tags("stat", stat8).baseUnit("bytes")
                .description("Memory available").register(meterRegistry);
            Gauge.builder("system.memory.detail", () -> getValueByStat(stat9)).tags("stat", stat9).baseUnit("bytes")
                .description("Memory available").register(meterRegistry);
            Gauge.builder("system.memory.detail", () -> getValueByStat(stat10)).tags("stat", stat10).baseUnit("bytes")
                .description("Memory available").register(meterRegistry);
            Gauge.builder("system.memory.detail", () -> getValueByStat(stat11)).tags("stat", stat11).baseUnit("bytes")
                .description("Memory available").register(meterRegistry);
            Gauge.builder("system.memory.detail", () -> getValueByStat(stat12)).tags("stat", stat12).baseUnit("bytes")
                .description("Memory available").register(meterRegistry);
            Gauge.builder("system.memory.detail", () -> getValueByStat(stat13)).tags("stat", stat13).baseUnit("bytes")
                .description("Memory available").register(meterRegistry);
            Gauge.builder("system.memory.detail", () -> getValueByStat(stat14)).tags("stat", stat14).baseUnit("bytes")
                .description("Memory available").register(meterRegistry);
            Gauge.builder("system.memory.detail", () -> getValueByStat(stat15)).tags("stat", stat15).baseUnit("bytes")
                .description("Memory available").register(meterRegistry);
            Gauge.builder("system.memory.detail", () -> getValueByStat(stat16)).tags("stat", stat16).baseUnit("bytes")
                .description("Memory available").register(meterRegistry);
            Gauge.builder("system.memory.detail", () -> getValueByStat(stat17)).tags("stat", stat17).baseUnit("bytes")
                .description("Memory available").register(meterRegistry);
        } catch (Throwable e) {
            // igore
        }

    }

    private Number getValueByStat(String stat) {
        Map<String, String> memorystat = FileUtil.readFileToMap(DOCKER_MEMORY_STAT);
        String num = memorystat.get(stat);
        if (num != null) {
            return Long.valueOf(num);
        } else {
            return 0;
        }
    }

}