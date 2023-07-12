package com.binance.master.log.layout;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.binance.master.utils.Md5Tools;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;

/**
 * @author james.li
 */
public class MaskUtil {

	protected static final Logger LOGGER = LoggerFactory.getLogger(MaskUtil.class);

	private final static String REPLACEMENT = "敏感({0})******";

	private static Map<String, String> regexMap = new ConcurrentHashMap<>();

	private static Map<String, Pattern> regexPattenMap = new ConcurrentHashMap<>();

	private static Map<String, Boolean> hashOutput = new ConcurrentHashMap<>();

	public static void init(Resource[] resources) {
		for (Resource resource : resources) {
			try (Scanner scanner = new Scanner(resource.getInputStream())) {
				int num = 0;
				while (scanner.hasNextLine()) {
					String line = scanner.nextLine();
					num++;
					if (line == null || line.trim().length() == 0) {
						continue;
					}
					String resourceName = resource.getURI().toString();
					initPattern(num, line, resourceName);
				}
			} catch (IOException e) {
				LOGGER.error(e.getMessage(), e);
			}
		}
	}

	private static void initPattern(int num, String line, String resourceName) {
		line = StringUtils.trim(line);
		if (StringUtils.startsWith(line, "#")) {
			return;
		}
		regexMap.put(resourceName + "#" + num, line);
		regexPattenMap.put(line, Pattern.compile(line, Pattern.DOTALL));
	}

    /**
     * init the mask util with provided plain text patterns.
     *
     * @param plainTextPatterns
     * @param applicationName
     */
    public static void init(List<String> plainTextPatterns, String applicationName) {
        if (plainTextPatterns == null || plainTextPatterns.isEmpty()) {
            return;
        }

        int lineNum = 0;
        for (String line : plainTextPatterns) {
            int length = line.length();
            if (length > 1 && line.endsWith("#")) {
                if (line.charAt(length - 2) == '\\') {
                    // 正常的#符号匹配 需要去掉转义符号
                    line = line.substring(0, length - 1) + "#";
                } else {
                    // 脱敏字段需要采用MD5 hash方式输出
                    line = line.substring(0, length - 1);
                    hashOutput.put(line, Boolean.TRUE);
                }
            }
            initPattern(lineNum++, line, applicationName);
        }
    }

	public static StringBuilder maskSensitiveInfo(String logStr) {
		StringBuilder buf = new StringBuilder(logStr);
		Iterator<Map.Entry<String, String>> iter = regexMap.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry<String, String> entry = iter.next();
			String numWithPrefix = entry.getKey();
			int idx = numWithPrefix.lastIndexOf("#");
			if (idx > 0) {
				int num = Integer.parseInt(numWithPrefix.substring(idx + 1));
				String line = entry.getValue();
				doMask(buf, num, line);
			} else {
				LOGGER.warn("invalid number with prefix {}", numWithPrefix);
			}
		}
		return buf;
	}

    private static void doMask(StringBuilder logBuf, int num, String regex) {
        try {
            Pattern p = regexPattenMap.get(regex);
            if (p == null) {
                p = Pattern.compile(regex, Pattern.DOTALL);
            }
            Matcher m = p.matcher(logBuf);
            if (m.matches()) {
                if (Boolean.TRUE.equals(hashOutput.get(regex))) {
                    int originalSize = logBuf.length();
                    logBuf = maskWithHash(logBuf, 0, originalSize, logBuf.toString());
				} else {
                    logBuf.delete(0, logBuf.length());
                    logBuf.append(MessageFormat.format(REPLACEMENT, num));
                }
            } else {
                if (m.find(0)) {
                    int gStart = -1;
                    int gEnd = -1;
                    if (m.groupCount() > 0) {
                        gStart = m.start(1);
                        gEnd = m.end(1);
                    }
                    // replace the sensitive info in the braces
                    if (gStart > 0 && gEnd > gStart) {
                        if (Boolean.TRUE.equals(hashOutput.get(regex))) {
                            String value = m.group(1);
                            logBuf = maskWithHash(logBuf, gStart, gEnd, value);
                        } else {
                            logBuf = logBuf.replace(gStart, gEnd, MessageFormat.format(REPLACEMENT, num));
                        }
                    }
                }
            }
        } catch (Exception e) {
            LOGGER.error("{}", e);
        }
    }

    private static StringBuilder maskWithHash(StringBuilder logBuf, int start, int end, String realValue) {
        return logBuf.replace(start, end, "##>" + Md5Tools.MD5(realValue));
    }

}