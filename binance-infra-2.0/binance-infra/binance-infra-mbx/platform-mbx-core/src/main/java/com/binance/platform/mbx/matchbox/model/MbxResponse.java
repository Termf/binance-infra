package com.binance.platform.mbx.matchbox.model;

import com.binance.master.error.GeneralCode;
import com.binance.platform.mbx.exception.MbxException;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Common response
 *
 * @author Li Fenggang
 * Date: 2020/7/2 11:11 上午
 */
public class MbxResponse<T> extends MbxBaseModel {
    private MbxState state;

    private T data;

    public MbxState getState() {
        return state;
    }

    public void setState(MbxState state) {
        this.state = state;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @JsonIgnore
    public boolean isSuccess() {
        return state != null && state.isSuccess();
    }

    @JsonIgnore
    public String getMessage() {
        return state == null ? "" : state.getMsg();
    }

    /**
     * Build a MbxResponse with the target data.
     *
     * @param t
     * @param <T>
     * @return
     */
    public static <T> MbxResponse<T> success(T t) {
        MbxResponse<T> response = new MbxResponse<>();
        response.setData(t);
        return response;
    }

    public static <T> MbxResponse<T> of(MbxState state) {
        MbxResponse<T> response = new MbxResponse<>();
        response.setState(state);
        return response;
    }

    /**
     * Validate the result. If it is no successful, an {@link com.binance.platform.mbx.exception.MbxException} will be thrown.
     */
    public void checkState() {
        if (!isSuccess()) {
            throw new MbxException(GeneralCode.SYS_ERROR, state.toString());
        }
    }
}
