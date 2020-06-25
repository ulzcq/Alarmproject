package com.yu.alarmproject;

import java.util.Calendar;

public class Lateness {
    private int id;
    private String label;
    private Calendar schedTime;
    private boolean lateness;

    public Lateness(int id, String label, Calendar schedTime, boolean lateness) {
        this.id = id;
        this.label = label;
        this.schedTime = schedTime;
        this.lateness = lateness;
    }

    public boolean isLateness() {
        return lateness;
    }

    public void setLateness(boolean lateness) {
        this.lateness = lateness;
    }
}
