package com.yu.alarmproject;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class SchedAlarmActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sched_alarm);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);


        //TODO: db에서 해당알람 label 조회
        TextView label = findViewById(R.id.label);

//        String sql = "select _id, LABEL, SCHEDTIME, READY_ALARMTIME, GOOUT_ALARMTIME, ENABLED from "
//                + Database.TABLE_ALARM
//                + " order by CREATE_DATE desc";
//
//        int recordCnt = -1;
//        Database database = Database.getInstance(context);
//        if( database != null){
//            Cursor outCursor = database.rawQuery(sql);
//            recordCnt = outCursor.getCount();
//
//            ArrayList<SchedAlarm> items = new ArrayList<SchedAlarm>();
//            for(int i=0; i<recordCnt; i++){
//                outCursor.moveToNext();
//
//                label.setText(outCursor.getString(1));
//
//
//                outCursor.close();
//            }


        //알람해제 버튼 누르면 알람꺼진다
        Button button = findViewById(R.id.alarmReleaseButton);
        button.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
