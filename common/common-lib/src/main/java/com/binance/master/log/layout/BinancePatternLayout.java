package com.binance.master.log.layout;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.ConsoleAppender;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.DefaultConfiguration;
import org.apache.logging.log4j.core.config.Node;
import org.apache.logging.log4j.core.config.plugins.*;
import org.apache.logging.log4j.core.config.xml.XmlConfiguration;
import org.apache.logging.log4j.core.layout.*;
import org.apache.logging.log4j.core.pattern.LogEventPatternConverter;
import org.apache.logging.log4j.core.pattern.PatternFormatter;
import org.apache.logging.log4j.core.pattern.PatternParser;
import org.apache.logging.log4j.core.pattern.RegexReplacement;
import org.apache.logging.log4j.status.StatusLogger;
import org.apache.logging.log4j.util.Strings;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import java.nio.charset.Charset;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Plugin(name = "BinancePatternLayout", category = Node.CATEGORY, elementType = Layout.ELEMENT_TYPE, printObject = true)
public class BinancePatternLayout extends AbstractStringLayout {

    protected static final org.apache.logging.log4j.Logger LOGGER = StatusLogger.getLogger();

    /**
     * Default pattern string for log output. Currently set to the string <b>"%m%n"</b> which just prints the
     * application supplied message.
     */
    public static final String DEFAULT_CONVERSION_PATTERN = "%m%n";

    /**
     * A conversion pattern equivalent to the TTCCLayout. Current value is <b>%r [%t] %p %c %notEmpty{%x }- %m%n</b>.
     */
    public static final String TTCC_CONVERSION_PATTERN = "%r [%t] %p %c %notEmpty{%x }- %m%n";

    /**
     * A simple pattern. Current value is <b>%d [%t] %p %c - %m%n</b>.
     */
    public static final String SIMPLE_CONVERSION_PATTERN = "%d [%t] %p %c - %m%n";

    /** Key to identify pattern converters. */
    public static final String KEY = "Converter";

    /**
     * Conversion pattern.
     */
    private final String conversionPattern;
    private final PatternSelector patternSelector;
    private final Serializer eventSerializer;
    private final static String EXTRA_LONG_MARK = "";
    // private final static String EXTRA_LONG_MARK = "超长日志";

    private String appName;
    private final boolean jsonFormat;

    private final static String LINE_BREAK = System.getProperty("line.separator");
    private final static int maxSize = 8000;
    private final static int actualMaxSize = maxSize - EXTRA_LONG_MARK.length() - LINE_BREAK.length();

    static {
        PathMatchingResourcePatternResolver pmrpr = new PathMatchingResourcePatternResolver();
        try {
            Resource[] resources = pmrpr.getResources("classpath*:log/log-mask-pattern.txt");
            MaskUtil.init(resources);
        } catch (Exception e) {
            LOGGER.warn("cannot load or parse the log-mask-pattern.txt file!");
        }
    }

    /**
     * Constructs a PatternLayout using the supplied conversion pattern.
     *
     * @param config
     *            The Configuration.
     * @param replace
     *            The regular expression to match.
     * @param eventPattern
     *            conversion pattern.
     * @param patternSelector
     *            The PatternSelector.
     * @param charset
     *            The character set.
     * @param alwaysWriteExceptions
     *            Whether or not exceptions should always be handled in this pattern (if {@code true}, exceptions will
     *            be written even if the pattern does not specify so).
     * @param noConsoleNoAnsi
     *            If {@code "true"} (default) and {@link System#console()} is null, do not output ANSI escape codes
     * @param headerPattern
     *            header conversion pattern.
     * @param footerPattern
     *            footer conversion pattern.
     * @param appName
     *            The application name.
     * @param jsonFormat
     *            whether to use json format as the output. @
     */
    private BinancePatternLayout(final Configuration config, final RegexReplacement replace, final String eventPattern,
        final PatternSelector patternSelector, final Charset charset, final boolean alwaysWriteExceptions,
        final boolean noConsoleNoAnsi, final String headerPattern, final String footerPattern, final String appName,
        final boolean jsonFormat) {
        super(config, charset,
            createSerializer(config, replace, headerPattern, null, patternSelector, alwaysWriteExceptions,
                noConsoleNoAnsi, appName, jsonFormat),
            createSerializer(config, replace, footerPattern, null, patternSelector, alwaysWriteExceptions,
                noConsoleNoAnsi, appName, jsonFormat));
        this.conversionPattern = eventPattern;
        this.patternSelector = patternSelector;
        this.eventSerializer = createSerializer(config, replace, eventPattern, DEFAULT_CONVERSION_PATTERN,
            patternSelector, alwaysWriteExceptions, noConsoleNoAnsi, appName, jsonFormat);
        this.appName = appName;
        this.jsonFormat = jsonFormat;
    }

    public static Serializer createSerializer(final Configuration configuration, final RegexReplacement replace,
        final String pattern, final String defaultPattern, final PatternSelector patternSelector,
        final boolean alwaysWriteExceptions, final boolean noConsoleNoAnsi, final String appName,
        final boolean jsonFormat) {
        if (Strings.isEmpty(pattern) && Strings.isEmpty(defaultPattern)) {
            return null;
        }
        if (patternSelector == null) {
            try {
                final PatternParser parser = createPatternParser(configuration);
                final List<PatternFormatter> list =
                    parser.parse(pattern == null ? defaultPattern : pattern, alwaysWriteExceptions, noConsoleNoAnsi);
                final PatternFormatter[] formatters = list.toArray(new PatternFormatter[0]);
                return new PatternSerializer(formatters, replace, appName, jsonFormat);
            } catch (final RuntimeException ex) {
                throw new IllegalArgumentException("Cannot parse pattern '" + pattern + "'", ex);
            }
        }
        return new PatternSelectorSerializer(patternSelector, replace, appName, jsonFormat);
    }

    /**
     * Gets the conversion pattern.
     *
     * @return the conversion pattern.
     */
    public String getConversionPattern() {
        return conversionPattern;
    }

    /**
     * Gets this PatternLayout's content format. Specified by:
     * <ul>
     * <li>Key: "structured" Value: "false"</li>
     * <li>Key: "formatType" Value: "conversion" (format uses the keywords supported by OptionConverter)</li>
     * <li>Key: "format" Value: provided "conversionPattern" param</li>
     * </ul>
     *
     * @return Map of content format keys supporting PatternLayout
     */
    @Override
    public Map<String, String> getContentFormat() {
        final Map<String, String> result = new HashMap<>();
        result.put("structured", "false");
        result.put("formatType", "conversion");
        result.put("format", conversionPattern);
        return result;
    }

    /**
     * Formats a logging event to a writer.
     *
     * @param event
     *            logging event to be formatted.
     * @return The event formatted as a String.
     */
    @Override
    public String toSerializable(final LogEvent event) {
        return eventSerializer.toSerializable(event);
    }

    @Override
    public void encode(final LogEvent event, final ByteBufferDestination destination) {
        LOGGER.info("============================encode");

        if (!(eventSerializer instanceof Serializer2)) {
            super.encode(event, destination);
            return;
        }
        final StringBuilder text = toText((Serializer2)eventSerializer, event, getStringBuilder());
        final Encoder<StringBuilder> encoder = getStringBuilderEncoder();
        encoder.encode(text, destination);
        if (!jsonFormat) {
            trimToMaxSize(text);
        }
    }

    /**
     * Creates a text representation of the specified log event and writes it into the specified StringBuilder.
     * <p>
     * Implementations are free to return a new StringBuilder if they can detect in advance that the specified
     * StringBuilder is too small.
     */

    private StringBuilder toText(final Serializer2 serializer, final LogEvent event, final StringBuilder destination) {
        StringBuilder sb = serializer.toSerializable(event, destination);
        // mask for non-json format
        if (MyEnvUtil.isProd() && !jsonFormat) {
            sb = MaskUtil.maskSensitiveInfo(sb.toString());
        }

        // trim to max size in console appender
        if (sb.length() > actualMaxSize && isConsoleAppender()) {
            CharSequence cs = sb.subSequence(0, actualMaxSize);
            sb = new StringBuilder();
            sb.append(EXTRA_LONG_MARK);
            sb.append(cs);
            sb.append(LINE_BREAK);
        }
        return sb;
    }

    private boolean isConsoleAppender() {
        XmlConfiguration config = (XmlConfiguration)this.getConfiguration();
        Map<String, Appender> map = config.getAppenders();
        Iterator<Map.Entry<String, Appender>> iter = map.entrySet().iterator();
        for (; iter.hasNext();) {
            Map.Entry<String, Appender> entry = iter.next();
            Appender apder = entry.getValue();
            if (apder instanceof ConsoleAppender && apder.getLayout() == this) {
                return true;
            }
        }
        return false;
    }

    /**
     * Creates a PatternParser.
     * 
     * @param config
     *            The Configuration.
     * @return The PatternParser.
     */
    public static PatternParser createPatternParser(final Configuration config) {
        if (config == null) {
            return new PatternParser(config, KEY, LogEventPatternConverter.class);
        }
        PatternParser parser = config.getComponent(KEY);
        if (parser == null) {
            parser = new PatternParser(config, KEY, LogEventPatternConverter.class);
            config.addComponent(KEY, parser);
            parser = config.getComponent(KEY);
        }
        return parser;
    }

    @Override
    public String toString() {
        return patternSelector == null ? conversionPattern : patternSelector.toString();
    }

    /**
     * Creates a pattern layout.
     *
     * @param pattern
     *            The pattern. If not specified, defaults to DEFAULT_CONVERSION_PATTERN.
     * @param patternSelector
     *            Allows different patterns to be used based on some selection criteria.
     * @param config
     *            The Configuration. Some Converters require access to the Interpolator.
     * @param replace
     *            A Regex replacement String.
     * @param charset
     *            The character set. The platform default is used if not specified.
     * @param alwaysWriteExceptions
     *            If {@code "true"} (default) exceptions are always written even if the pattern contains no exception
     *            tokens.
     * @param noConsoleNoAnsi
     *            If {@code "true"} (default is false) and {@link System#console()} is null, do not output ANSI escape
     *            codes
     * @param headerPattern
     *            The footer to place at the top of the document, once.
     * @param footerPattern
     *            The footer to place at the bottom of the document, once.
     * @param appName
     *            The appName for this log.
     * @param jsonFormat
     *            whether to use json as the output format.
     * @return The PatternLayout.
     */
    @PluginFactory
    public static BinancePatternLayout createLayout(
        @PluginAttribute(value = "pattern", defaultString = DEFAULT_CONVERSION_PATTERN) final String pattern,
        @PluginElement("PatternSelector") final PatternSelector patternSelector,
        @PluginConfiguration final Configuration config, @PluginElement("Replace") final RegexReplacement replace,
        // LOG4J2-783 use platform default by default, so do not specify defaultString for
        // charset
        @PluginAttribute(value = "charset") final Charset charset,
        @PluginAttribute(value = "alwaysWriteExceptions", defaultBoolean = true) final boolean alwaysWriteExceptions,
        @PluginAttribute(value = "noConsoleNoAnsi", defaultBoolean = false) final boolean noConsoleNoAnsi,
        @PluginAttribute("header") final String headerPattern, @PluginAttribute("footer") final String footerPattern,
        @PluginAttribute(value = "appName") final String appName,
        @PluginAttribute(value = "jsonFormat", defaultBoolean = false) final boolean jsonFormat) {
        return newBuilder().withPattern(pattern).withPatternSelector(patternSelector).withConfiguration(config)
            .withRegexReplacement(replace).withCharset(charset).withAlwaysWriteExceptions(alwaysWriteExceptions)
            .withNoConsoleNoAnsi(noConsoleNoAnsi).withHeader(headerPattern).withFooter(footerPattern)
            .withAppName(appName).withJsonFormat(jsonFormat).build();
    }

    private static abstract class BaseSerializer implements Serializer, Serializer2 {
        protected String appName;
        protected boolean jsonFormat;

        public BaseSerializer(String appName, boolean jsonFormat) {
            this.appName = appName;
            this.jsonFormat = jsonFormat;
        }
    }

    static Map<String, String> fieldNameMap = new HashMap<>();

    static {
        fieldNameMap.put("Level", "level");
        fieldNameMap.put("Date", "timestamp");
        fieldNameMap.put("MDC{traceId}", "uuid");
        // fieldNameMap.put("MDC{tracking-chain}", "uuid"); 禁止使用
        fieldNameMap.put("Thread", "thread");
        fieldNameMap.put("Logger", "class");
        fieldNameMap.put("Message", "message");
    }

    private static class PatternSerializer extends BaseSerializer implements Serializer, Serializer2 {

        private final PatternFormatter[] formatters;
        private final RegexReplacement replace;

        private PatternSerializer(final PatternFormatter[] formatters, final RegexReplacement replace, String appName,
            boolean jsonFormat) {
            super(appName, jsonFormat);
            this.formatters = formatters;
            this.replace = replace;
        }

        @Override
        public String toSerializable(final LogEvent event) {
            final StringBuilder sb = getStringBuilder();
            try {
                return toSerializable(event, sb).toString();
            } finally {
                if (!jsonFormat) {
                    // only trim to max as a whole in non-json format
                    trimToMaxSize(sb);
                }
            }
        }

        @Override
        public StringBuilder toSerializable(final LogEvent event, final StringBuilder buffer) {
            final int len = formatters.length;
            if (jsonFormat) {
                toJsonStr(event, buffer, len);
            } else {
                for (int i = 0; i < len; i++) {
                    formatters[i].format(event, buffer);
                }
            }
            // TODO:
            if (replace != null) { // creates temporary objects
                String str = buffer.toString();
                str = replace.format(str);
                buffer.setLength(0);
                buffer.append(str);
            }
            return buffer;
        }

        private void toJsonStr(LogEvent event, StringBuilder buffer, int len) {
            buffer.append("{");
            // ip
            buildIpField(event, buffer);
            // appName
            if (appName != null) {
                LayoutUtil.buildJsonItem("appName", appName, buffer);
            }
            // separator field
            LayoutUtil.buildJsonItem("separator", "-", buffer);
            StringBuilder tmpBuf = new StringBuilder();
            for (int i = 0; i < len; i++) {
                buildField(event, buffer, formatters[i], tmpBuf);
            }
            if (buffer.charAt(buffer.length() - 1) == ',') {
                // delete last comma for json normalization
                buffer.deleteCharAt(buffer.length() - 1);
            }
            buffer.append("}");
        }

        private void buildField(LogEvent event, StringBuilder buffer, PatternFormatter formatter,
            StringBuilder tmpBuf) {
            String fieldName = formatter.getConverter().getName();
            String destName = fieldNameMap.get(fieldName);
            if (destName != null) {
                StringBuilder fieldBuf = new StringBuilder();
                formatter.format(event, fieldBuf);
                // message end
                boolean isMessage = "message".equals(destName);
                if (isMessage) {
                    tmpBuf.append(fieldBuf);
                } else {
                    LayoutUtil.buildJsonItem(destName, fieldBuf.toString(), buffer);
                }
            } else if ("Line".equals(fieldName)) {
                // special handling for line number: delete comma symbol and escape symbol
                buffer.delete(buffer.length() - 2, buffer.length());
                buffer.append("(");
                formatter.format(event, buffer);
                buffer.append(")\",");
            } else if ("Line Sep".equals(fieldName)) {
                formatter.format(event, tmpBuf);
            } else if ("ExtendedThrowable".equals(fieldName)) {
                formatter.format(event, tmpBuf);
                buildMessage(buffer, tmpBuf);
            } else if (!"Literal".equals(fieldName)) {
                buildMessage(buffer, tmpBuf);
            }
        }

        private void buildMessage(StringBuilder buffer, StringBuilder tmpBuf) {
            // just mask message field
            if (MyEnvUtil.isProd()) {
                tmpBuf = MaskUtil.maskSensitiveInfo(tmpBuf.toString());
            }
            String value = LayoutUtil.escape(tmpBuf);
            LayoutUtil.buildJsonItem("message", value, buffer);
            // clear the tmpBuf
            tmpBuf.delete(0, tmpBuf.length());
        }

        private void buildIpField(LogEvent event, StringBuilder buffer) {
            // IP field: assume the IP field is in the 5th position of the formatters
            if (formatters.length > 4 && formatters[4] != null) {
                StringBuilder ipBuf = new StringBuilder();
                formatters[4].format(event, ipBuf);
                String ip = LayoutUtil.extractIP(ipBuf);
                LayoutUtil.buildJsonItem("ip", ip, buffer);
            }
        }

        @Override
        public String toString() {
            final StringBuilder builder = new StringBuilder();
            builder.append(super.toString());
            builder.append("[formatters=");
            builder.append(Arrays.toString(formatters));
            builder.append(", replace=");
            builder.append(replace);
            builder.append("]");
            return builder.toString();
        }
    }

    private static class PatternSelectorSerializer extends BaseSerializer implements Serializer, Serializer2 {

        private final PatternSelector patternSelector;
        private final RegexReplacement replace;

        private PatternSelectorSerializer(final PatternSelector patternSelector, final RegexReplacement replace,
            String appName, boolean jsonFormat) {
            super(appName, jsonFormat);
            this.patternSelector = patternSelector;
            this.replace = replace;
        }

        @Override
        public String toSerializable(final LogEvent event) {
            final StringBuilder sb = getStringBuilder();
            try {
                return toSerializable(event, sb).toString();
            } finally {
                if (!jsonFormat) {
                    trimToMaxSize(sb);
                }
            }
        }

        @Override
        public StringBuilder toSerializable(final LogEvent event, final StringBuilder buffer) {
            final PatternFormatter[] formatters = patternSelector.getFormatters(event);
            final int len = formatters.length;
            for (int i = 0; i < len; i++) {
                formatters[i].format(event, buffer);
            }
            if (replace != null) { // creates temporary objects
                String str = buffer.toString();
                str = replace.format(str);
                buffer.setLength(0);
                buffer.append(str);
            }
            return buffer;
        }

        @Override
        public String toString() {
            final StringBuilder builder = new StringBuilder();
            builder.append(super.toString());
            builder.append("[patternSelector=");
            builder.append(patternSelector);
            builder.append(", replace=");
            builder.append(replace);
            builder.append("]");
            return builder.toString();
        }
    }

    /**
     * Creates a PatternLayout using the default options. These options include using UTF-8, the default conversion
     * pattern, exceptions being written, and with ANSI escape codes.
     *
     * @return the PatternLayout.
     * @see #DEFAULT_CONVERSION_PATTERN Default conversion pattern
     */
    public static BinancePatternLayout createDefaultLayout() {
        return newBuilder().build();
    }

    /**
     * Creates a PatternLayout using the default options and the given configuration. These options include using UTF-8,
     * the default conversion pattern, exceptions being written, and with ANSI escape codes.
     *
     * @param configuration
     *            The Configuration.
     *
     * @return the PatternLayout.
     * @see #DEFAULT_CONVERSION_PATTERN Default conversion pattern
     */
    public static BinancePatternLayout createDefaultLayout(final Configuration configuration) {
        return newBuilder().withConfiguration(configuration).build();
    }

    /**
     * Creates a builder for a custom PatternLayout.
     *
     * @return a PatternLayout builder.
     */
    @PluginBuilderFactory
    public static Builder newBuilder() {
        return new Builder();
    }

    /**
     * Custom PatternLayout builder. Use the {@link PatternLayout#newBuilder() builder factory method} to create this.
     */
    public static class Builder implements org.apache.logging.log4j.core.util.Builder<BinancePatternLayout> {

        @PluginBuilderAttribute
        private String pattern = PatternLayout.DEFAULT_CONVERSION_PATTERN;

        @PluginElement("PatternSelector")
        private PatternSelector patternSelector;

        @PluginConfiguration
        private Configuration configuration;

        @PluginElement("Replace")
        private RegexReplacement regexReplacement;

        // LOG4J2-783 use platform default by default
        @PluginBuilderAttribute
        private Charset charset = Charset.defaultCharset();

        @PluginBuilderAttribute
        private boolean alwaysWriteExceptions = true;

        @PluginBuilderAttribute
        private boolean noConsoleNoAnsi;

        @PluginBuilderAttribute
        private String header;

        @PluginBuilderAttribute
        private String footer;

        @PluginBuilderAttribute
        private String appName;

        @PluginBuilderAttribute
        private boolean jsonFormat = false;

        private Builder() {}

        public Builder withPattern(final String pattern) {
            this.pattern = pattern;
            return this;
        }

        public Builder withPatternSelector(final PatternSelector patternSelector) {
            this.patternSelector = patternSelector;
            return this;
        }

        public Builder withConfiguration(final Configuration configuration) {
            this.configuration = configuration;
            return this;
        }

        public Builder withRegexReplacement(final RegexReplacement regexReplacement) {
            this.regexReplacement = regexReplacement;
            return this;
        }

        public Builder withCharset(final Charset charset) {
            // LOG4J2-783 if null, use platform default by default
            if (charset != null) {
                this.charset = charset;
            }
            return this;
        }

        public Builder withAlwaysWriteExceptions(final boolean alwaysWriteExceptions) {
            this.alwaysWriteExceptions = alwaysWriteExceptions;
            return this;
        }

        public Builder withNoConsoleNoAnsi(final boolean noConsoleNoAnsi) {
            this.noConsoleNoAnsi = noConsoleNoAnsi;
            return this;
        }

        public Builder withHeader(final String header) {
            this.header = header;
            return this;
        }

        public Builder withFooter(final String footer) {
            this.footer = footer;
            return this;
        }

        public Builder withAppName(final String appName) {
            this.appName = appName;
            return this;
        }

        public Builder withJsonFormat(final boolean jsonFormat) {
            this.jsonFormat = jsonFormat;
            return this;
        }

        @Override
        public BinancePatternLayout build() {
            // fall back to DefaultConfiguration
            if (configuration == null) {
                configuration = new DefaultConfiguration();
            }
            return new BinancePatternLayout(configuration, regexReplacement, pattern, patternSelector, charset,
                alwaysWriteExceptions, noConsoleNoAnsi, header, footer, appName, jsonFormat);
        }
    }

}
