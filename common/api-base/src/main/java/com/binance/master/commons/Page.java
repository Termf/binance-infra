package com.binance.master.commons;

public class Page extends ToString {
    /**
     * 
     */
    private static final long serialVersionUID = 2373772276976425915L;
    public static final int NO_ROW_OFFSET = 0;
    public static final int NO_ROW_LIMIT = Integer.MAX_VALUE;
    private int offset = NO_ROW_OFFSET;
    private int limit = NO_ROW_LIMIT;

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        if (offset <= 0) {
            offset = 0;
        }
        this.offset = offset;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        if(limit <= 0) {
            this.limit = Integer.MAX_VALUE;
        }
        this.limit = limit;
    }

}
