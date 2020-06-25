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

public class AlarmAertActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String action = getIntent().getAction();

        //알람 종류에 따라 레이아웃 다르게 띄우기
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
                finish();
            }
        });
    }
}
