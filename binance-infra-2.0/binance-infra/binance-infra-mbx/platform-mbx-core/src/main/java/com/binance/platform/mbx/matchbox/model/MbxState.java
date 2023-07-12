package com.binance.platform.mbx.matchbox.model;

/**
 * Represents the error object from match box.
 *
 * @author Li Fenggang
 * Date: 2020/7/8 10:38 上午
 */
public class MbxState extends MbxBaseModel {
    private Long code;
    private String msg;

    /**
     * 返回调用是否成功。
     *
     * @return true：成功；false：失败。
     */
    public boolean isSuccess() {
        return code == null && msg == null;
    }

    public Long getCode() {
        return code;
    }

    public void setCode(Long code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
