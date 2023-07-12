package com.binance.master.old.models.charge;

import java.util.Date;

public class OldBlockLogAbandon {
    private Integer id;

    private String coin;

    private Long height;

    private String blockHash;

    private String preBlockHash;

    private Date time;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCoin() {
        return coin;
    }

    public void setCoin(String coin) {
        this.coin = coin == null ? null : coin.trim();
    }

    public Long getHeight() {
        return height;
    }

    public void setHeight(Long height) {
        this.height = height;
    }

    public String getBlockHash() {
        return blockHash;
    }

    public void setBlockHash(String blockHash) {
        this.blockHash = blockHash == null ? null : blockHash.trim();
    }

    public String getPreBlockHash() {
        return preBlockHash;
    }

    public void setPreBlockHash(String preBlockHash) {
        this.preBlockHash = preBlockHash == null ? null : preBlockHash.trim();
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }
}