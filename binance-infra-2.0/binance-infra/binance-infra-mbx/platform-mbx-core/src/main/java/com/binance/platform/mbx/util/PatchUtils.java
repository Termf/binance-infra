package com.binance.platform.mbx.util;

import com.binance.master.enums.TerminalEnum;
import com.binance.master.utils.WebUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.ObjectUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * @author lishichao
 */
public class PatchUtils {

    /**
     * get terminal enum
     *
     * @return TerminalEnum
     */
    public static TerminalEnum getTerminal() {
        TerminalEnum terminalEnum = null;
        HttpServletRequest request = WebUtils.getHttpServletRequest();
        if (!ObjectUtils.isEmpty(request)) {
            String clientType = request.getParameter("clientType");
            if (StringUtils.isBlank(clientType)) {
                clientType = request.getHeader("clientType");
            }
            if (StringUtils.isBlank(clientType)) {
                clientType = request.getParameter("clienttype");
            }
            if (StringUtils.isBlank(clientType)) {
                clientType = request.getHeader("clienttype");
            }
            if (StringUtils.isBlank(clientType)) {
                Object clientTypeTemp = request.getAttribute("clientTypeTemp");
                if (!ObjectUtils.isEmpty(clientTypeTemp)) {
                    clientType = clientTypeTemp.toString();
                }
            }
            if (StringUtils.isNotBlank(clientType)) {
                clientType = clientType.toLowerCase();
            }
            terminalEnum = TerminalEnum.findByCode(clientType);
        }
        return terminalEnum;
    }
}
