package com.yu.alarmproject;

import android.net.Uri;

import java.io.Serializable;
import java.util.Calendar;

public class SchedAlarm implements Serializable { //직렬화해야함. SchedAlarm -> SubmitActivity 수정시
    private static final long serialVersionUID = 1L; //직렬화된 클래스의 버전
    //객체를 전달하는 측과 해당 객체를 수신하는 측에서 사용하는 클래스 파일이 동일한지 체크하는 용도

    private int id;
    private String label; //일정 제목

    private Calendar schedTime; //yyyy-MM-dd HH:mm:ss 일정 년,월,일,시
    private Calendar readyAlarmTime; //준비시작 알람시각
    private Calendar goOutAlarmTime; //출발 알람-시각

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

    public SchedAlarm(int id, String label, Calendar schedTime, Calendar readyAlarmTime, Calendar goOutAlarmTime,
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

    public SchedAlarm(int id, String label, Calendar schedTime, Calendar AlarmTime, boolean enabled, int viewType, int h, int m) {
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

    public SchedAlarm(int id, String label, Calendar schedTime, boolean enabled, int viewType) {
        this.id = id;
        this.label = label;
        this.schedTime = schedTime;
        this.schedEnabled = enabled;
        this.viewType = viewType; //3
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    } //뭐지..? 지울까

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

    public Calendar getSchedTime() {
        return schedTime;
    }

    public Calendar getReadyAlarmTime() {
        return readyAlarmTime;
    }

    public Calendar getGoOutAlarmTime() {
        return goOutAlarmTime;
    }

    public boolean isReadyEnabled() { //알람울릴때 이값이 true여야 울리게
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

    public int getReady_h() {
        return ready_h;
    }

    public int getReady_m() {
        return ready_m;
    }

    public int getMove_h() {
        return move_h;
    }

    public int getMove_m() {
        return move_m;
    }

}
