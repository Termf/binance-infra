package com.binance.master.old.models.trade;

import java.util.Date;

public class HolidayItem {
    private int id;
    private Date holidayStart;
    private Date holidayEnd;
    private String description;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getHolidayStart() {
        return holidayStart;
    }

    public void setHolidayStart(Date holidayStart) {
        this.holidayStart = holidayStart;
    }

    public Date getHolidayEnd() {
        return holidayEnd;
    }

    public void setHolidayEnd(Date holidayEnd) {
        this.holidayEnd = holidayEnd;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
