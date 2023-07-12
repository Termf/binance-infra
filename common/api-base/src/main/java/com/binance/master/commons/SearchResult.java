package com.binance.master.commons;

import java.util.List;

/**
 * 查询结果
 *
 * @author Wuqingming
 * @since 2012-3-4
 */
public class SearchResult<T>{

    /**
     * 总记录条数
     */
    private long total;

    /**
     * 查询结果集
     */
    private List<T> rows;

    public SearchResult() {
        super();
    }

    public SearchResult(List<T> rows, long total) {
        super();
        this.rows = rows;
        this.total = total;
    }

    public List<T> getRows() {
        return rows;
    }

    public void setRows(List<T> rows) {
        this.rows = rows;
    }

    public long getTotal() {
        if (total == 0 && rows != null) {
            return rows.size();
        }
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    @Override
    public String toString() {
        if (rows == null) {
            return "[total=" + total + ",data=NULL]";
        }
        return "[total=" + total + ",data=" + rows.toString() + "]";
    }

}