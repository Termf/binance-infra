package com.binance.infra.sample.mybatis.enums;

/**
 * @author lucas.w
 * @version 1.0.0
 * @since 2021-08-19
 */
public enum BookType {

    Computer("计算机类"), Financial("金融类");

    private String typeName;

    BookType(String typeName){
        this.typeName = typeName;
    }

    public String toString(){
        return typeName;
    }

    public String getValue(){
        return name();
    }

}
