package com.aware.plugin.esm.flexible.definition;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.ArrayList;
import java.util.List;

@Root(name = "Schedule", strict = false)
public class Schedule {

    @Element(name="id")
    private String id;

    @ElementList(entry="hour", required = false, inline = true)
    private List<Integer> hour = new ArrayList<>();

    @ElementList(entry="minute", required = false, inline = true)
    private List<Integer> minute = new ArrayList<>();

    @ElementList(entry="weekday", required = false, inline = true)
    private List<String> weekday = new ArrayList<>();

    @ElementList(entry="month", required = false, inline = true)
    private List<String> month = new ArrayList<>();

    @Element(name = "random", required = false)
    private boolean random;

    @Element(name = "amount", required = false)
    private int amount;

    @Element(name = "interval", required = false)
    private int interval;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Integer> getHour() {
        return hour;
    }

    public void setHour(List<Integer> hour) {
        this.hour = hour;
    }

    public List<Integer> getMinute() {
        return minute;
    }

    public void setMinute(List<Integer> minute) {
        this.minute = minute;
    }

    public List<String> getWeekday() {
        return weekday;
    }

    public void setWeekday(List<String> weekday) {
        this.weekday = weekday;
    }

    public List<String> getMonth() {
        return month;
    }

    public void setMonth(List<String> month) {
        this.month = month;
    }

    public boolean isRandom() {
        return random;
    }

    public void setRandom(boolean random) {
        this.random = random;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getInterval() {
        return interval;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }

}
