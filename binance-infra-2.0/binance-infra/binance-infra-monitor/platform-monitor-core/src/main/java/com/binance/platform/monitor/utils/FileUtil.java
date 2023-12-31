package com.binance.platform.monitor.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileUtil {
    private static final Logger LOG = LoggerFactory.getLogger(FileUtil.class);

    private static final String READING_LOG = "Reading file {}";
    private static final String READ_LOG = "Read {}";

    private FileUtil() {}

    /**
     * Read an entire file at one time. Intended primarily for Linux /proc filesystem to avoid recalculating file
     * contents on iterative reads.
     *
     * @param filename
     *            The file to read
     * @return A list of Strings representing each line of the file, or an empty list if file could not be read or is
     *         empty
     */
    public static List<String> readFile(String filename) {
        return readFile(filename, true);
    }

    /**
     * Read an entire file at one time. Intended primarily for Linux /proc filesystem to avoid recalculating file
     * contents on iterative reads.
     *
     * @param filename
     *            The file to read
     * @param reportError
     *            Whether to log errors reading the file
     * @return A list of Strings representing each line of the file, or an empty list if file could not be read or is
     *         empty
     */
    public static List<String> readFile(String path, boolean reportError) {
        ArrayList<String> results = new ArrayList<String>();
        FileReader fileReader = null;
        BufferedReader reader = null;
        try {
            fileReader = new FileReader(new File(path));
            reader = new BufferedReader(fileReader);
            String tempString = null;
            while ((tempString = reader.readLine()) != null) {
                results.add(tempString);
            }
            reader.close();
        } catch (Throwable e) {
            // do nothing
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
            if (fileReader != null) {
                try {
                    fileReader.close();
                } catch (IOException e1) {
                }
            }
        }
        return results;
    }

    public static Map<String, String> readFileToMap(String path) {
        Map<String, String> results = new HashMap<String, String>();
        FileReader fileReader = null;
        BufferedReader reader = null;
        try {
            fileReader = new FileReader(new File(path));
            reader = new BufferedReader(fileReader);
            String tempString = null;
            while ((tempString = reader.readLine()) != null) {
                String[] strArray = StringUtils.split(tempString, StringUtils.SPACE);
                if (strArray != null && strArray.length == 2) {
                    results.put(strArray[0].trim(), strArray[1].trim());
                }
            }
            reader.close();
        } catch (Throwable e) {
            // do nothing
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
            if (fileReader != null) {
                try {
                    fileReader.close();
                } catch (IOException e1) {
                }
            }
        }
        return results;
    }

    public static void main(String[] args) {
        String path =
            "/Users/liushiming/project/java/binance/binance-infra/binance-infra-2.0/binance-infra/binance-infra-monitor/platform-monitor-core/src/main/java/com/binance/platform/monitor/utils/memorystat.txt";
        Map<String, String> maps = readFileToMap(path);
        maps.forEach((k, v) -> {
            if (k.startsWith("total")) {
                String t = String.format("MEMORY_STAT_CACHE.add(\"%s\");", k);
                System.out.println(t);
            }
        });
    }

    /**
     * Read a file and return the long value contained therein. Intended primarily for Linux /sys filesystem
     *
     * @param filename
     *            The file to read
     * @return The value contained in the file, if any; otherwise zero
     */
    public static long getLongFromFile(String filename) {
        if (LOG.isDebugEnabled()) {
            LOG.debug(READING_LOG, filename);
        }
        List<String> read = FileUtil.readFile(filename, false);
        if (!read.isEmpty()) {
            if (LOG.isTraceEnabled()) {
                LOG.trace(READ_LOG, read.get(0));
            }
            return ParseUtil.parseLongOrDefault(read.get(0), 0L);
        }
        return 0L;
    }

    /**
     * Read a file and return the unsigned long value contained therein as a long. Intended primarily for Linux /sys
     * filesystem
     *
     * @param filename
     *            The file to read
     * @return The value contained in the file, if any; otherwise zero
     */
    public static long getUnsignedLongFromFile(String filename) {
        if (LOG.isDebugEnabled()) {
            LOG.debug(READING_LOG, filename);
        }
        List<String> read = FileUtil.readFile(filename, false);
        if (!read.isEmpty()) {
            if (LOG.isTraceEnabled()) {
                LOG.trace(READ_LOG, read.get(0));
            }
            return ParseUtil.parseUnsignedLongOrDefault(read.get(0), 0L);
        }
        return 0L;
    }

    /**
     * Read a file and return the int value contained therein. Intended primarily for Linux /sys filesystem
     *
     * @param filename
     *            The file to read
     * @return The value contained in the file, if any; otherwise zero
     */
    public static int getIntFromFile(String filename) {
        if (LOG.isDebugEnabled()) {
            LOG.debug(READING_LOG, filename);
        }
        try {
            List<String> read = FileUtil.readFile(filename, false);
            if (!read.isEmpty()) {
                if (LOG.isTraceEnabled()) {
                    LOG.trace(READ_LOG, read.get(0));
                }
                return Integer.parseInt(read.get(0));
            }
        } catch (NumberFormatException ex) {
            LOG.warn("Unable to read value from {}. {}", filename, ex.getMessage());
        }
        return 0;
    }

    /**
     * Read a file and return the String value contained therein. Intended primarily for Linux /sys filesystem
     *
     * @param filename
     *            The file to read
     * @return The value contained in the file, if any; otherwise empty string
     */
    public static String getStringFromFile(String filename) {
        if (LOG.isDebugEnabled()) {
            LOG.debug(READING_LOG, filename);
        }
        List<String> read = FileUtil.readFile(filename, false);
        if (!read.isEmpty()) {
            if (LOG.isTraceEnabled()) {
                LOG.trace(READ_LOG, read.get(0));
            }
            return read.get(0);
        }
        return "";
    }

    /**
     * Read a file and return a map of string keys to string values contained therein. Intended primarily for Linux
     * /proc/[pid]/io
     *
     * @param filename
     *            The file to read
     * @param separator
     *            Characters in each line of the file that separate the key and the value
     * @return The map contained in the file, if any; otherwise empty map
     */
    public static Map<String, String> getKeyValueMapFromFile(String filename, String separator) {
        Map<String, String> map = new HashMap<>();
        if (LOG.isDebugEnabled()) {
            LOG.debug(READING_LOG, filename);
        }
        List<String> lines = FileUtil.readFile(filename, false);
        for (String line : lines) {
            String[] parts = line.split(separator);
            if (parts.length == 2) {
                map.put(parts[0], parts[1].trim());
            }
        }
        return map;
    }

    /**
     * Read a configuration file from the sequence of context classloader, system classloader and classloader of the
     * current class, and return its properties
     *
     * @param propsFilename
     *            The filename
     * @return A {@link Properties} object containing the properties.
     */
    public static Properties readPropertiesFromFilename(String propsFilename) {
        Properties archProps = new Properties();
        // Load the configuration file from the different classloaders
        if (readPropertiesFromClassLoader(propsFilename, archProps, Thread.currentThread().getContextClassLoader())
            || readPropertiesFromClassLoader(propsFilename, archProps, ClassLoader.getSystemClassLoader())
            || readPropertiesFromClassLoader(propsFilename, archProps, FileUtil.class.getClassLoader())) {
            return archProps;
        }

        LOG.warn("Failed to load default configuration");
        return archProps;
    }

    private static boolean readPropertiesFromClassLoader(String propsFilename, Properties archProps,
        ClassLoader loader) {
        if (loader == null) {
            return false;
        }
        // Load the configuration file from the classLoader
        try {
            List<URL> resources = Collections.list(loader.getResources(propsFilename));
            if (resources.isEmpty()) {
                LOG.info("No {} file found from ClassLoader {}", propsFilename, loader);
                return false;
            }
            if (resources.size() > 1) {
                LOG.warn("Configuration conflict: there is more than one {} file on the classpath", propsFilename);
                return true;
            }

            try (InputStream in = resources.get(0).openStream()) {
                if (in != null) {
                    archProps.load(in);
                }
            }
        } catch (IOException e) {
            return false;
        }
        return true;
    }
}
