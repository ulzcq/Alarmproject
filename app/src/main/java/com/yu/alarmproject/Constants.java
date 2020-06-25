package com.yu.alarmproject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class Constants {
    //상수값을 모아놓은 클래스

    public static final String DATABASE_NAME ="alarm.db";

    //추가,수정,삭제 모드 상수
    public static final int MODE_INSERT = 1;
    public static final int MODE_MODIFY = 2;

    //AlarmAdapter클래스 - viewType에 대한 Code Value
    public static final int INTEGRATED_CONTENT = 0;
    public static final int ONLY_READY_CONTENT = 1;
    public static final int ONLY_GOOUT_CONTENT = 2;
    public static final int ONLY_SCHED_CONTENT = 3;

    public static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd a HH:mm");
    public static SimpleDateFormat timeFormat = new SimpleDateFormat("a HH:mm");
    public static SimpleDateFormat calTitleFormat = new SimpleDateFormat("yyyy년 MM월");

}
