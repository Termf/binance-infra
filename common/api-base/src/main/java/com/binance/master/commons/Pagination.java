package com.binance.master.commons;

import org.springframework.util.StringUtils;

import java.io.Serializable;

/**
 * User 分页字段
 *
 * @author liwei
 * @date 2018-05-11
 */
public class Pagination implements Serializable {

    private static final long serialVersionUID = 7829709854461635402L;
    private Integer page;

    private Integer rows;

    private String order;

    private String sort;

    public Integer getPage() {
        if (page == null) {
            page = 1;
        }
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getRows() {
        if (rows == null) {
            rows = 10;
        }
        return rows;
    }

    public Integer getStart() {
        if (page == null || page <= 0) {
            page = 1;
        }
        if (rows == null) {
            rows = 10;
        }

        return (page - 1) * rows;
    }

    public void setRows(Integer rows) {
        this.rows = rows;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public boolean haveOrderStatement(){
        return !StringUtils.isEmpty(order) && !StringUtils.isEmpty(sort);
    }

    public String getOrderStatement(){
        return sort + " " + order;
    }
}
