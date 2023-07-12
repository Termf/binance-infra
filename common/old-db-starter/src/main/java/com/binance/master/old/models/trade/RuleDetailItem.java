package com.binance.master.old.models.trade;

import com.google.common.base.MoreObjects;

public class RuleDetailItem {
    private Long id;
    private Long ruleId;
    private Integer startDay;
    private Integer startHour;
    private Integer startMinute;
    private Integer endDay;
    private Integer endHour;
    private Integer endMinute;

    public String toString() {
        return MoreObjects.toStringHelper(this.getClass()).add("id", id).add("ruleId", ruleId).add("startDay", startDay)
                .add("startHour", startHour).add("startMinute", startMinute).add("endDay", endDay)
                .add("endHour", endHour).add("endMinute", endMinute).toString();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getRuleId() {
        return ruleId;
    }

    public void setRuleId(Long ruleId) {
        this.ruleId = ruleId;
    }

    public Integer getStartDay() {
        return startDay;
    }

    public void setStartDay(Integer startDay) {
        this.startDay = startDay;
    }

    public Integer getStartHour() {
        return startHour;
    }

    public void setStartHour(Integer startHour) {
        this.startHour = startHour;
    }

    public Integer getStartMinute() {
        return startMinute;
    }

    public void setStartMinute(Integer startMinute) {
        this.startMinute = startMinute;
    }

    public Integer getEndDay() {
        return endDay;
    }

    public void setEndDay(Integer endDay) {
        this.endDay = endDay;
    }

    public Integer getEndHour() {
        return endHour;
    }

    public void setEndHour(Integer endHour) {
        this.endHour = endHour;
    }

    public Integer getEndMinute() {
        return endMinute;
    }

    public void setEndMinute(Integer endMinute) {
        this.endMinute = endMinute;
    }

}
