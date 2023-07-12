
package com.binance.platform.monitor.metric.jvm.procfs;

import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ProcfsSmaps extends ProcfsEntry {

    public enum KEY implements ValueKey {
        /**
         * Virtual set size
         */
        VSS,
        /**
         * Resident set size
         */
        RSS,
        /**
         * Proportional set size
         */
        PSS,
        /**
         * Paged out memory
         */
        SWAP,
        /**
         * Paged out memory accounting shared pages. Since Linux 4.3.
         */
        SWAPPSS
    }

    private static final Pattern LINE_PATTERN = Pattern.compile("^\\w+:\\s+(\\d+)\\skB$");

    private static final int KILOBYTE = 1024;

    public ProcfsSmaps() {
        super(ProcfsReader.getInstance("smaps"));
    }

    /* default */ ProcfsSmaps(ProcfsReader reader) {
        super(reader);
    }

    @Override
    protected void handle(Map<ValueKey, Double> values, String line) {
        Objects.requireNonNull(values);
        Objects.requireNonNull(line);

        KEY valueKey = null;
        if (line.startsWith("Size:")) {
            valueKey = KEY.VSS;
        } else if (line.startsWith("Rss:")) {
            valueKey = KEY.RSS;
        } else if (line.startsWith("Pss:")) {
            valueKey = KEY.PSS;
        } else if (line.startsWith("Swap:")) {
            valueKey = KEY.SWAP;
        } else if (line.startsWith("SwapPss:")) {
            valueKey = KEY.SWAPPSS;
        }

        if (valueKey != null) {
            final Double kiloBytes = parseKiloBytes(line) * KILOBYTE;
            values.compute(valueKey, (key, value) -> (value == null) ? kiloBytes : value.doubleValue() + kiloBytes);
        }
    }

    private static Double parseKiloBytes(String line) {
        Objects.requireNonNull(line);

        final Matcher matcher = LINE_PATTERN.matcher(line);
        if (!matcher.matches()) {
            return Double.NaN;
        }

        return Double.parseDouble(matcher.group(1));
    }

}
