package com.binance.platform.mbx.cloud.model;

import java.io.Serializable;
import java.util.Objects;

public class SysConfigResp implements Serializable {

    private static final long serialVersionUID = -7389657782694104388L;

    /**
     * 名称
     */
    private String displayName;

    /**
     * 编码
     */
    private String code;

    /**
     * id
     */
    private String id;


    /**
     * 描述
     */
    private String description;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SysConfigResp that = (SysConfigResp) o;
        return Objects.equals(displayName, that.displayName) &&
                Objects.equals(code, that.code) &&
                Objects.equals(id, that.id) &&
                Objects.equals(description, that.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(displayName, code, id, description);
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}