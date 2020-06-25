package com.yu.alarmproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import java.text.SimpleDateFormat;

public class AlarmAlertActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String action = getIntent().getAction();

        //TODO: 데이터받아와서 enabeld 보고 알람 울릴건지 결정
        //알람 종류에 따라 레이아웃 다르게 띄우기

        if(isEnabled(getIntent().get)) //id 값전달
            //알람 on이라면

        if(action.equals(AlarmReceiver.READY_ALARM_ALERT_ACTION)){
            setContentView(R.layout.activity_ready_alarm);
        }
        else if(action.equals(AlarmReceiver.GOOUT_ALARM_ALERT_ACTION)){
            setContentView(R.layout.activity_go_out_alarm);
        }
        else if(action.equals(AlarmReceiver.SCHED_ALARM_ALERT_ACTION)){
            setContentView(R.layout.activity_sched_alarm);
        }
        else{
            Log.e("AlarmActivity","invalid intent action error");
        }

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);

        Button button = findViewById(R.id.alarmReleaseButton);
        button.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO:알람 enabled -> db 수정하고 알람도 cancel시키기
                finish();
            }
        });
    }

    public boolean isEnabled(int id){
        //데이터베이스 조회

    }

    public void snnozeAlarm(int id){
        //알람 off
        //db enabled 값 0으로 set
    }
}
