package com.binance.platform.mbx.url;

import com.google.common.collect.Lists;
import org.apache.commons.lang.StringUtils;
import org.junit.Test;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author Li Fenggang
 * Date: 2020/7/18 1:34 下午
 */
public class UrlVerification {
    @Test
    public void verify() {
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("symbol", "symbol");
        paramMap.put("minNotional", "minNotional");
        paramMap.put("enableMarket", null);

        String url = buildUrl(paramMap);
        System.out.println("source: " + url);
        String result = formatUrl(url);
        System.out.println("result:" + result);
    }

    public String buildUrl(Map<String, String> paramMap) {
        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromHttpUrl("http://localhost/minNotionalFilter");
        Iterator<Map.Entry<String, String>> entryIterator = paramMap.entrySet().iterator();
        while (entryIterator.hasNext()) {
            Map.Entry<String, String> next = entryIterator.next();
            uriComponentsBuilder.queryParam(next.getKey(), next.getValue());
        }
        return uriComponentsBuilder.build().toUriString();
    }

    private static String formatUrl(String url) {
        StringBuilder sb = new StringBuilder();
        String[] array = StringUtils.split(url, "?");
        sb.append(array[0]);
        if (array.length > 1) {
            array = StringUtils.split(array[1], "&");
            if (array != null && array.length > 0) {
                List<String> params = Lists.newArrayList(array);
                for (Iterator<String> iter = params.iterator(); iter.hasNext();) {
                    String p = iter.next();
                    if (!StringUtils.contains(p, "=")) {
                        iter.remove();
                    }
                }
                if (!params.isEmpty()) {
                    sb.append("?");
                    sb.append(StringUtils.join(params, "&"));
                }
            }
        }
        url = sb.toString();
        return url;
    }
}
