package com.yu.alarmproject;

import android.net.Uri;

public class SchedAlarm {
    private int id;
    private String label; //일정 제목

    private String schedTime; //yyyy-MM-dd HH:mm:ss 일정 년,월,일,시
    private String readyAlarmTime; //준비시작 알람시각
    private String goOutAlarmTime; //출발 알람-시각

    private boolean readyEnabled; //on/off
    private boolean goOutEnabled;
    private boolean schedEnabled;

    private int viewType;
    private int ready_h; //준비시간
    private int ready_m;
    private int move_h; //이동시간
    private int move_m;
    //boolean vibrate; // 진동 여부
    //int daysOfWeek; //요일
    //Uri alert; //벨소리

    public SchedAlarm(int id, String label, String schedTime, String readyAlarmTime, String goOutAlarmTime,
                      boolean readyAlarmEnabled, boolean goOutAlarmEnabled, int viewType, int h1, int m1, int h2, int m2) {
        this.id = id;
        this.label = label;
        this.schedTime = schedTime;
        this.readyAlarmTime = readyAlarmTime;
        this.goOutAlarmTime = goOutAlarmTime;
        this.readyEnabled = readyAlarmEnabled;
        this.goOutEnabled = goOutAlarmEnabled;
        this.viewType = viewType;

        this.ready_h = h1;
        this.ready_m = m1;
        this.move_h = h2;
        this.move_m = m2;
    }

    public SchedAlarm(int id, String label, String schedTime, String AlarmTime, boolean enabled, int viewType, int h, int m) {
                this.id = id;
                this.label = label;
                this.schedTime = schedTime;
                this.viewType = viewType;

                if(viewType ==Constants.ONLY_READY_CONTENT){
                    this.readyAlarmTime = AlarmTime;
                    this.readyEnabled = enabled;
                    this.ready_h= h;
                    this.ready_m= m;
                }
                else if(viewType ==Constants.ONLY_GOOUT_CONTENT){
                    this.goOutAlarmTime = AlarmTime;
                    this.goOutEnabled = enabled;
                    this.move_h = h;
                    this.move_m = m;
        }

    }

    public SchedAlarm(int id, String label, String schedTime, boolean enabled, int viewType) {
        this.id = id;
        this.label = label;
        this.schedTime = schedTime;
        this.schedEnabled = enabled;
        this.viewType = viewType; //3
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getSchedTime() {
        return schedTime;
    }

    public void setSchedTime(String schedTime) {
        this.schedTime = schedTime;
    }

    public String getReadyAlarmTime() {
        return readyAlarmTime;
    }

    public void setReadyAlarmTime(String readyAlarmTime) {
        this.readyAlarmTime = readyAlarmTime;
    }

    public String getGoOutAlarmTime() {
        return goOutAlarmTime;
    }

    public void setGoOutAlarmTime(String goOutAlarmTime) {
        this.goOutAlarmTime = goOutAlarmTime;
    }

    public boolean isReadyEnabled() {
        return readyEnabled;
    }

    public void setReadyEnabled(boolean readyEnabled) {
        this.readyEnabled = readyEnabled;
    }

    public boolean isGoOutEnabled() {
        return goOutEnabled;
    }

    public void setGoOutEnabled(boolean goOutEnabled) {
        this.goOutEnabled = goOutEnabled;
    }

    public boolean isSchedEnabled() {
        return schedEnabled;
    }

    public void setSchedEnabled(boolean schedEnabled) {
        this.schedEnabled = schedEnabled;
    }

    public int getViewType() {
        return viewType;
    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
    }

    public int getReady_h() {
        return ready_h;
    }

    public void setReady_h(int ready_h) {
        this.ready_h = ready_h;
    }

    public int getReady_m() {
        return ready_m;
    }

    public void setReady_m(int ready_m) {
        this.ready_m = ready_m;
    }

    public int getMove_h() {
        return move_h;
    }

    public void setMove_h(int move_h) {
        this.move_h = move_h;
    }

    public int getMove_m() {
        return move_m;
    }

    public void setMove_m(int move_m) {
        this.move_m = move_m;
    }
}
