package com.binance.master.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Created by Fei.Huang on 2018/8/27.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class IpBlackListMsg {

    Type type;
    List<String> ips;

    public enum Type {
        ADD,
        DELETE
    }

}