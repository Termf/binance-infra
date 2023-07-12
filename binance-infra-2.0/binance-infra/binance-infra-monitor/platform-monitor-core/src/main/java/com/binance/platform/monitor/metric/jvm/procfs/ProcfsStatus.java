package com.binance.platform.monitor.metric.jvm.procfs;

import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ProcfsStatus extends ProcfsEntry {

    public enum KEY implements ValueKey {
        /**
         * Threads
         */
        THREADS,
        /**
         * Virtual set size
         */
        VSS,
        /**
         * Resident set size
         */
        RSS,
        /**
         * Paged out memory
         */
        SWAP
    }

    private static final Pattern VAL_LINE_PATTERN = Pattern.compile("^\\w+:\\s+(\\d+)$");

    private static final Pattern KB_LINE_PATTERN = Pattern.compile("^\\w+:\\s+(\\d+)\\skB$");

    private static final int KILOBYTE = 1024;

    private static class InstanceHolder {

        /* default */ static final ProcfsStatus INSTANCE = new ProcfsStatus();

    }

    /* default */ ProcfsStatus() {
        super(ProcfsReader.getInstance("status"));
    }

    /* default */ ProcfsStatus(ProcfsReader reader) {
        super(reader);
    }

    @Override
    protected void handle(Map<ValueKey, Double> values, String line) {
        Objects.requireNonNull(values);
        Objects.requireNonNull(line);

        if (line.startsWith("Threads:")) {
            values.put(KEY.THREADS, parseValue(line));
        } else if (line.startsWith("VmSize:")) {
            values.put(KEY.VSS, parseKiloBytes(line) * KILOBYTE);
        } else if (line.startsWith("VmRSS:")) {
            values.put(KEY.RSS, parseKiloBytes(line) * KILOBYTE);
        } else if (line.startsWith("VmSwap:")) {
            values.put(KEY.SWAP, parseKiloBytes(line) * KILOBYTE);
        }
    }

    private static Double parseValue(String line) {
        Objects.requireNonNull(line);

        final Matcher matcher = VAL_LINE_PATTERN.matcher(line);
        if (!matcher.matches()) {
            return Double.NaN;
        }

        return Double.parseDouble(matcher.group(1));
    }

    private static Double parseKiloBytes(String line) {
        Objects.requireNonNull(line);

        final Matcher matcher = KB_LINE_PATTERN.matcher(line);
        if (!matcher.matches()) {
            return Double.NaN;
        }

        return Double.parseDouble(matcher.group(1));
    }

    public static ProcfsStatus getInstance() {
        return InstanceHolder.INSTANCE;
    }

}
