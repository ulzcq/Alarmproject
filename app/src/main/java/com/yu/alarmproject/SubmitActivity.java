package com.yu.alarmproject;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class SubmitActivity extends AppCompatActivity {

    private AlarmManager alarmManager;

    private int mode = Constants.MODE_INSERT; //알람 새로 생성하는지 수정하는지 구분자 값

    private EditText schedLabel;
    private TimePicker schedTimePicker;
    private NumberPicker ready_hPicker;
    private NumberPicker ready_mPicker;
    private NumberPicker move_hPicker;
    private NumberPicker move_mPicker; //view 객체

    private int schedTime_h;
    private int schedTime_m;
    private int ready_h;
    private int ready_m;
    private int move_h;
    private int move_m; //getViewType, setAlarm에서 필요

    private int readyTime[];
    private int goOutTime[]; //계산한 알람시각 결과

    private SchedAlarm item; //수정시 보여줄 데이터

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_alarm);

        alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        //notifManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);

        //반복 여부
        //사운드
        schedLabel = findViewById(R.id.schedLabel);//일정 제목
        schedTimePicker = findViewById(R.id.schedTimePicker); // 일정시각
        schedTimePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                schedTime_h = hourOfDay;
                schedTime_m = minute;
            }
        });

        ready_hPicker = findViewById(R.id.ready_hPicker);
        ready_mPicker = findViewById(R.id.ready_mPicker);
        move_hPicker = findViewById(R.id.move_hPicker);
        move_mPicker = findViewById(R.id.move_mPicker);

        Intent intent = getIntent(); //AlarmList에서 전달받은 intent 참조
        setItem((SchedAlarm)intent.getSerializableExtra("SchedAlarm"));

        applyItem(); //수정 이라면 뷰 설정

        Button cancelButton = findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: 취소 버튼 클릭시 timpePicker 초기화하고 ShowAlarmList로 돌아간다
                finish();
            }
        });

        Button saveButton = (Button)findViewById(R.id.saveButton); //저장버튼 클릭 시
        saveButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mode == Constants.MODE_INSERT){
                    addAlarmData(); //알람 추가
                    finish();
                }else if(mode == Constants.MODE_MODIFY){
                    modifyAlarm(); //알람 수정
                    finish();
                }
            }
        });
    }

    public void setItem(SchedAlarm item){
        this.item = item;
    }

    private void applyItem(){
        if(item != null){ //item이 있다면
            mode = Constants.MODE_MODIFY; //수정모드로 설정
            //item 데이터로 뷰들 값 설정하기

            Calendar schedTime = item.getSchedTime(); //schedTimePciker 로드
                try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    schedTimePicker.setHour(schedTime.HOUR_OF_DAY);
                    schedTimePicker.setMinute(schedTime.MINUTE);
                } else {
                    schedTimePicker.setCurrentHour(schedTime.HOUR_OF_DAY);
                    schedTimePicker.setCurrentMinute(schedTime.MINUTE);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            ready_hPicker.setValue(item.getReady_h()); // ready Picker 로드
            ready_mPicker.setValue(item.getReady_m());

            move_hPicker.setValue(item.getMove_h()); // move Picker 로드
            move_mPicker.setValue(item.getMove_m());

        } else{
            mode = Constants.MODE_INSERT; //추가모드로 설정
        }
    }

    private int getViewType(){
        ready_h = ready_hPicker.getValue();
        ready_m = ready_mPicker.getValue();
        move_h= move_hPicker.getValue();
        move_m= move_mPicker.getValue(); //picker에서 값가져오기

        if(ready_h==0  && ready_m==0){
            if(move_h==0 && move_m==0) {
                //준비알람x 출발알람 x
                return Constants.ONLY_SCHED_CONTENT;
            }else{ //준비알람x 출발알람 o
                return Constants.ONLY_GOOUT_CONTENT;
            }
        }else{
            if(move_h==0 && move_m==0){
                //준비알람o 출발알람x
                return Constants.ONLY_READY_CONTENT;
            }else{
                //준비알람o 출발알람o
                return Constants.INTEGRATED_CONTENT;
            }
        }
    }

    private void setAlarm(int _id, int viewType){

        goOutTime = getAlarmTime(schedTime_h,schedTime_m,move_h,move_m);
        readyTime = getAlarmTime(goOutTime[0],goOutTime[1],ready_h,ready_m); //알람시각계산

        Intent intent = new Intent(getApplicationContext(), AlarmReceiver.class);
        Calendar calendar;

        switch(viewType){
            case Constants.INTEGRATED_CONTENT:
                intent.setAction(AlarmReceiver.READY_ALARM_ALERT_ACTION);
                calendar = getCalendar(readyTime[0],readyTime[1]); //날짜설정
                alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), getPendingIntent(intent,_id));//준비시작 알람 생성

                intent.setAction(AlarmReceiver.GOOUT_ALARM_ALERT_ACTION);
                calendar = getCalendar(goOutTime[0],goOutTime[1]);
                alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), getPendingIntent(intent,_id));//출발 알람 생성
                break;
            case Constants.ONLY_READY_CONTENT:
                intent.setAction(AlarmReceiver.READY_ALARM_ALERT_ACTION);
                calendar = getCalendar(readyTime[0],readyTime[1]);
                alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), getPendingIntent(intent,_id));//준비시작 알람 생성
                break;
            case Constants.ONLY_GOOUT_CONTENT:
                intent.setAction(AlarmReceiver.GOOUT_ALARM_ALERT_ACTION);
                calendar = getCalendar(goOutTime[0],goOutTime[1]);
                alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), getPendingIntent(intent,_id));//출발 알람 생성
                break;
            case Constants.ONLY_SCHED_CONTENT:
                intent.setAction(AlarmReceiver.SCHED_ALARM_ALERT_ACTION);
                calendar = getCalendar(schedTime_h,schedTime_m);
                alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), getPendingIntent(intent,_id));//일정 알람생성
                break;
            default:
                Log.d("SubmitActivity","setAlarm() viewType error");
        }

        //4가지 상황에 따라 view분리해서 생성
    }

    private PendingIntent getPendingIntent(Intent intent,int _id){
        return PendingIntent.getBroadcast(this, _id, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private void addAlarmData(){
        int _id;
        int viewType = getViewType();

        String schedTime = Constants.dateFormat.format(getCalendar(schedTime_h,schedTime_m).getTimeInMillis());
        String readyAlarmTime = Constants.dateFormat.format(getCalendar(readyTime[0],readyTime[1]).getTimeInMillis());
        String goOutAlarmTime = Constants.dateFormat.format(getCalendar(goOutTime[0],goOutTime[1]).getTimeInMillis());

        String sql="";
        int enabled[] = {0, 0, 0};
        if(item!=null){
            switch(viewType){
                case Constants.INTEGRATED_CONTENT:
                    enabled[0]=1; enabled[1]=1; enabled[2]=0;
                    break;
                case Constants.ONLY_READY_CONTENT:
                    enabled[0]=1; enabled[1]=0; enabled[2]=0;
                    break;
                case Constants.ONLY_GOOUT_CONTENT:
                    enabled[0]=0; enabled[1]=1; enabled[2]=1;
                    break;
                case Constants.ONLY_SCHED_CONTENT:
                    enabled[0]=0; enabled[1]=0; enabled[2]=1;
                    break;
                default:
                    Log.d("SubmitActivity","viewType error.");
                    break;
            }
            sql = "insert into " + Database.TABLE_ALARM +
                    "(LABEL, SCHEDTIME, R_ALARMTIME, G_ALARMTIME, R_ENABLED, G_ENABLED, S_ENABLED, VIEWTYPE, READY_H, READY_M, MOVE_H, MOVE_M) values(" +
                    "'"+ schedLabel.getText() +"',"+
                    "'"+ schedTime +"',"+
                    "'"+ readyAlarmTime +"',"+
                    "'"+ goOutAlarmTime +"',"+
                    "'"+ enabled[0] +"',"+
                    "'"+ enabled[1] +"',"+
                    "'"+ enabled[2] +"',"+
                    "'"+ viewType +"',"+
                    "'"+ ready_h +"',"+
                    "'"+ ready_m +"',"+
                    "'"+ move_h +"',"+
                    "'"+ move_m +"',"+
                    ")";
        }

        Database database = Database.getInstance(getApplicationContext());
        database.execSQL(sql);
        //db에서 id값 설정해야하기때문에 db먼저 저장한거임,
        //TODO: db에서 설정된 id값 조회해서 알람 설정해야함!!
        setAlarm(_id,viewType);

        //lateness 테이블에도 insert

        Toast.makeText(getApplicationContext(), "알람이 설정되었습니다.", Toast.LENGTH_SHORT).show();
        finish();
    }

    private void modifyAlarm(){
        //update 문 실행. sql 문이 실행될 때 where 조건이 들어가는 키는 _id 값이다.
        //item을 가져와서 알람삭제후 db 수정하고 다시 생성
        int _id = item.getId();
        cancelAlarm(_id,item.getViewType()); //알람매니저-시스템 알람 삭제
        int viewType = getViewType(); //새로입력된값의 뷰타입 받아오기

        String schedTime = Constants.dateFormat.format(getCalendar(schedTime_h,schedTime_m).getTimeInMillis());
        String readyAlarmTime = Constants.dateFormat.format(getCalendar(readyTime[0],readyTime[1]).getTimeInMillis());
        String goOutAlarmTime = Constants.dateFormat.format(getCalendar(goOutTime[0],goOutTime[1]).getTimeInMillis());

        String sql ="";
        int enabled[] = {0, 0, 0};
        if(item!=null){
            switch(viewType){ // item의 id에 해당되는 쿼리 변경
                case Constants.INTEGRATED_CONTENT:
                    enabled[0]=1; enabled[1]=1; enabled[2]=0;
                    break;
                case Constants.ONLY_READY_CONTENT:
                    enabled[0]=1; enabled[1]=0; enabled[2]=0;
                    break;
                case Constants.ONLY_GOOUT_CONTENT:
                    enabled[0]=0; enabled[1]=1; enabled[2]=1;
                    break;
                case Constants.ONLY_SCHED_CONTENT:
                    enabled[0]=0; enabled[1]=0; enabled[2]=1;
                    break;
                default:
                    Log.d("SubmitActivity","viewType error.");
                    break;
            }

            sql = "update " + Database.TABLE_ALARM +
                    "set "+
                    " LABEL = '" + schedLabel.getText() + "'" +
                    " ,SCHED_TIME = '" + schedTime + "'" +
                    " ,READY_ALARMTIME = '" + readyAlarmTime + "'" +
                    " ,GOOUT_ALARMTIME = '" + goOutAlarmTime + "'" +
                    " ,READY_ENABLED = '" + enabled[0] +
                    " ,GOOUT_ENABLED = '" + enabled[1] +
                    " ,SCHED_ENABLED = '" + enabled[2] +
                    " ,VIEWTYPE" + viewType +
                    " ,READY_H" + ready_h +
                    " ,READY_M" + ready_m +
                    " ,MOVE_H" + move_h +
                    " ,MOVE_M" + move_m +
                    " where "+
                    " _id =" + item.getId();

            Database database = Database.getInstance(getApplicationContext());
            database.execSQL(sql);
            setAlarm(_id,viewType);
        }

    }

    private void cancelAlarm(int _id, int viewType){ //삭제는 한일정에 매핑된 알람 한꺼번에!
        //item alarm 삭제, _id로 구분됨!!
        Intent intent; //TODO

        switch(viewType){
            case Constants.INTEGRATED_CONTENT:
                intent = new Intent(getApplicationContext(), AlarmReceiver.class);
                intent.setAction(AlarmReceiver.READY_ALARM_ALERT_ACTION);
                alarmManager.cancel(getPendingIntent(intent,_id)); //준비시작 알람 삭제

                intent = new Intent(getApplicationContext(), AlarmReceiver.class);
                intent.setAction(AlarmReceiver.GOOUT_ALARM_ALERT_ACTION);
                alarmManager.cancel(getPendingIntent(intent,_id));//출발 알람 삭제
                break;
            case Constants.ONLY_READY_CONTENT:
                intent = new Intent(getApplicationContext(), AlarmReceiver.class);
                intent.setAction(AlarmReceiver.READY_ALARM_ALERT_ACTION);
                alarmManager.cancel(getPendingIntent(intent,_id));//준비시작 알람 삭제
                break;
            case Constants.ONLY_GOOUT_CONTENT:
                intent = new Intent(getApplicationContext(), AlarmReceiver.class);
                intent.setAction(AlarmReceiver.GOOUT_ALARM_ALERT_ACTION);
                alarmManager.cancel(getPendingIntent(intent,_id));//출발 알람 삭제
                break;
            case Constants.ONLY_SCHED_CONTENT:
                intent = new Intent(getApplicationContext(), AlarmReceiver.class);
                intent.setAction(AlarmReceiver.SCHED_ALARM_ALERT_ACTION);
                alarmManager.cancel(getPendingIntent(intent,_id));//일정 알람삭제
                break;
        }
    }

    public int[] getAlarmTime(int origin_h, int origin_m, int h, int m){
        int time[] = new int[2];

        if(origin_h > 12) h = h-12;//오후. 13시~23시
        else if(origin_h == 0) h = 12;//자정. 0시 => 오전 12시
        //origin_h <= : 오전1시~11시, 오후 12시 => 값변경 필요x

        if(origin_m < m){ //
            time[1] = 60 - m - origin_m;
            time[0] = origin_h - h -1;
        } else{
            time[1] = origin_m - m; //11-1 = 10
            time[0] = origin_h - h; //3-0 = 3 => 3시 10분아냐?
        }
        return time;
    }


    private Calendar getCalendar(int hour, int minute){
        GregorianCalendar calendar = (GregorianCalendar) GregorianCalendar.getInstance();
        int currentHour = calendar.get(GregorianCalendar.HOUR_OF_DAY);
        int currentMinute = calendar.get(GregorianCalendar.MINUTE);

        //현재시간과 비교해: 시가 작거나. 시는 같고, 분은 작으면 날짜 +1
        if (currentHour > hour || ( currentHour == hour && currentMinute > minute )) //TODO: 복붙한거 좀 다르게 바꾸기
            calendar.add(GregorianCalendar.DAY_OF_YEAR, 1);

        calendar.set(GregorianCalendar.HOUR_OF_DAY, hour);
        calendar.set(GregorianCalendar.MINUTE, minute);
        calendar.set(GregorianCalendar.SECOND, 0);
        calendar.set(GregorianCalendar.MILLISECOND, 0);

        Log.d("SubmitActivity","triggerTimeMillis() return:"+calendar.getTimeInMillis());
        return calendar;

    }

}
