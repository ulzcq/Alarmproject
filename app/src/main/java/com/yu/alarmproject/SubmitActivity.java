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

import com.google.android.material.tabs.TabLayout;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class SubmitActivity extends AppCompatActivity {

    //public static final String READY_ALARM_ALERT_ACTION = "com.yu.alarmproject.READY_ALARM_ALERT";
    //public static final String GOOUT_ALARM_ALERT_ACTION = "com.yu.alarmproject.GOOUT_ALARM_ALERT";

    private Context context;
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

    SchedAlarm item; //수정시 보여줄 데이터

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

        applyItem();

        Button cancelButton = findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: 취소 버튼 클릭시 timpePicker 초기화하고 ShowAlarmList로 돌아간다
                finish();
            }
        });

        Button saveButton = (Button)findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mode == Constants.MODE_INSERT){
                    addAlarm(); //알람 추가
                    finish();
                }else if(mode == Constants.MODE_MODIFY){
                    modifyAlarm(); //알람 수정
                    finish();
                }
            }
        });
    }

    private void applyItem(){
        if(item != null){
            mode = Constants.MODE_MODIFY;
            //item 데이터로 뷰들 값 설정하기

            String schedTime = item.getSchedTime();//"2018-09-06 오전 11:11";

            try {
                Date date = Constants.DateFormat.parse(schedTime); //String -> Date
                Calendar cal = Calendar.getInstance();
                cal.setTime(date); //Date -> Calendar
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    schedTimePicker.setHour(cal.HOUR_OF_DAY);
                    schedTimePicker.setMinute(cal.MINUTE);
                } else {
                    schedTimePicker.setCurrentHour(cal.HOUR_OF_DAY);
                    schedTimePicker.setCurrentMinute(cal.MINUTE);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }

            ready_hPicker.setValue(item.getReady_h());
            ready_mPicker.setValue(item.getReady_m());

            move_hPicker.setValue(item.getMove_h());
            move_mPicker.setValue(item.getMove_m());

        } else{
            mode = Constants.MODE_INSERT;
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

        Intent intent;
        Calendar calendar;

        switch(viewType){
            case Constants.INTEGRATED_CONTENT:
                intent = new Intent(getApplicationContext(), ReadyAlarmReceiver.class);
                intent.setAction(ReadyAlarmReceiver.READY_ALARM_ALERT_ACTION);
                calendar = getCalendar(readyTime[0],readyTime[1]); //날짜설정
                alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), getPendingIntent(intent,_id));//준비시작 알람 생성

                intent = new Intent(getApplicationContext(), GoOutAlarmReceiver.class);
                intent.setAction(GoOutAlarmReceiver.GOOUT_ALARM_ALERT_ACTION);
                calendar = getCalendar(goOutTime[0],goOutTime[1]);
                alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), getPendingIntent(intent,_id));//출발 알람 생성
                break;
            case Constants.ONLY_READY_CONTENT:
                intent = new Intent(getApplicationContext(), ReadyAlarmReceiver.class);
                intent.setAction(ReadyAlarmReceiver.READY_ALARM_ALERT_ACTION);
                calendar = getCalendar(readyTime[0],readyTime[1]);
                alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), getPendingIntent(intent,_id));//준비시작 알람 생성
                break;
            case Constants.ONLY_GOOUT_CONTENT:
                intent = new Intent(getApplicationContext(), GoOutAlarmReceiver.class);
                intent.setAction(GoOutAlarmReceiver.GOOUT_ALARM_ALERT_ACTION);
                calendar = getCalendar(goOutTime[0],goOutTime[1]);
                alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), getPendingIntent(intent,_id));//출발 알람 생성
                break;
            case Constants.ONLY_SCHED_CONTENT:
                intent = new Intent(getApplicationContext(), SchedAlarmReceiver.class);
                intent.setAction(SchedAlarmReceiver.SCHED_ALARM_ALERT_ACTION);
                calendar = getCalendar(schedTime_h,schedTime_m);
                alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), getPendingIntent(intent,_id));//일정 알람생성
                break;
        }

        //4가지 상황에 따라 view분리해서 생성
    }

    private PendingIntent getPendingIntent(Intent intent,int _id){
        return PendingIntent.getBroadcast(this, _id, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private void addAlarm(){
        int _id;
        int viewType = getViewType();

        String schedTime = Constants.DateFormat.format(getCalendar(schedTime_h,schedTime_m).getTimeInMillis());
        String readyAlarmTime = Constants.DateFormat.format(getCalendar(readyTime[0],readyTime[1]).getTimeInMillis());
        String goOutAlarmTime = Constants.DateFormat.format(getCalendar(goOutTime[0],goOutTime[1]).getTimeInMillis());

        String sql="";
        switch(viewType){ //db에 추가
            case Constants.INTEGRATED_CONTENT:
                sql = "insert into " + Database.TABLE_ALARM +
                        "(LABEL, SCHEDTIME, R_ALARMTIME, G_ALARMTIME, R_ENABLED, G_ENABLED, VIEWTYPE, READY_H, READY_M, MOVE_H, MOVE_M) values(" +
                        "'"+ schedLabel.getText() +"',"+
                        "'"+ schedTime +"',"+
                        "'"+ readyAlarmTime +"',"+
                        "'"+ goOutAlarmTime +"',"+
                        "'"+ 1 +"',"+
                        "'"+ 1 +"',"+
                        "'"+ viewType +"',"+
                        "'"+ ready_h +"',"+
                        "'"+ ready_m +"',"+
                        "'"+ move_h +"',"+
                        "'"+ move_m +"',"+
                        ")";
                break;
            case Constants.ONLY_READY_CONTENT:
                sql = "insert into " + Database.TABLE_ALARM +
                        "(LABEL, SCHEDTIME, READY_ALARMTIME, READY_ENABLED, VIEWTYPE, READY_H, READY_M, MOVE_H, MOVE_M) values(" +
                        "'"+ schedLabel.getText() +"',"+
                        "'"+ schedTime +"',"+
                        "'"+ readyAlarmTime +"',"+
                        "'"+ 1 +"',"+
                        "'"+ viewType +"',"+
                        "'"+ ready_h +"',"+
                        "'"+ ready_m +"',"+
                        "'"+ move_h +"',"+
                        "'"+ move_m +"',"+
                        ")";
                break;
            case Constants.ONLY_GOOUT_CONTENT:
                sql = "insert into " + Database.TABLE_ALARM +
                        "(LABEL, SCHEDTIME, GOOUT_ALARMTIME, GOOUT_ENABLED, VIEWTYPE, READY_H, READY_M, MOVE_H, MOVE_M) values(" +
                        "'"+ schedLabel.getText() +"',"+
                        "'"+ schedTime +"',"+
                        "'"+ goOutAlarmTime +"',"+
                        "'"+ 1 +"',"+
                        "'"+ viewType +"',"+
                        "'"+ ready_h +"',"+
                        "'"+ ready_m +"',"+
                        "'"+ move_h +"',"+
                        "'"+ move_m +"',"+
                        ")";
                break;
            case Constants.ONLY_SCHED_CONTENT:
                sql = "insert into " + Database.TABLE_ALARM +
                        "(LABEL, SCHEDTIME, SCHED_ENABLED, VIEWTYPE, READY_H, READY_M, MOVE_H, MOVE_M) values(" +
                        "'"+ schedLabel.getText() +"',"+
                        "'"+ schedTime +"',"+
                        "'"+ 1 +"',"+
                        "'"+ viewType +"',"+
                        "'"+ ready_h +"',"+
                        "'"+ ready_m +"',"+
                        "'"+ move_h +"',"+
                        "'"+ move_m +"',"+
                        ")";
                break;
            default:
                Log.d("SubmitActivity","viewType error.");
                break;
        }

        Database database = Database.getInstance(context);
        database.execSQL(sql);
        //TODO: id값 다시 조회
        setAlarm(_id,viewType);

        Toast.makeText(getApplicationContext(), "알람이 설정되었습니다.", Toast.LENGTH_SHORT).show();
        finish();
    }

    private void modifyAlarm(){
        //update 문 실행. sql 문이 실행될 때 where 조건이 들어가는 키는 _id 값이다.
        //item을 가져와서 알람삭제후 db 수정하고 다시 생성
        int _id = item.getId();
        cancelAlarm(_id); //알람 삭제
        int viewType = getViewType(); //새로입력된값의 뷰타입 받아오기

        String schedTime = Constants.DateFormat.format(getCalendar(schedTime_h,schedTime_m).getTimeInMillis());
        String readyAlarmTime = Constants.DateFormat.format(getCalendar(readyTime[0],readyTime[1]).getTimeInMillis());
        String goOutAlarmTime = Constants.DateFormat.format(getCalendar(goOutTime[0],goOutTime[1]).getTimeInMillis());

        String sql ="";
        if(item!=null){
            switch(viewType){ // item의 id에 해당되는 쿼리 변경
                case Constants.INTEGRATED_CONTENT:
                    sql = "update " + Database.TABLE_ALARM +
                            "set "+
                            " LABEL = '" + schedLabel.getText() + "'" +
                            " ,SCHED_TIME = '" + schedTime + "'" +
                            " ,READY_ALARMTIME = '" + readyAlarmTime + "'" +
                            " ,GOOUT_ALARMTIME = '" + goOutAlarmTime + "'" +
                            " ,READY_ENABLED = '" + 1 +
                            " ,GOOUT_ENABLED = '" + 1 +
                            " ,SCHED_ENABLED = '" + 0 +
                            " ,VIEWTYPE" + viewType +
                            " where "+
                            " _id =" + item.getId(); break;
                case Constants.ONLY_READY_CONTENT:
                    sql = "update " + Database.TABLE_ALARM +
                            "set "+
                            " LABEL = '" + schedLabel.getText() + "'" +
                            " ,SCHED_TIME = '" + schedTime + "'" +
                            " ,READY_ALARMTIME = '" + readyAlarmTime + "'" +
                            " ,READY_ENABLED = '" + 1 +
                            " ,GOOUT_ENABLED = '" + 0 +
                            " ,SCHED_ENABLED = '" + 0 +
                            " ,VIEWTYPE" + viewType +
                            " where "+
                            " _id =" + item.getId(); break;
                case Constants.ONLY_GOOUT_CONTENT:
                    sql = "update " + Database.TABLE_ALARM +
                            "set "+
                            " LABEL= '" + schedLabel.getText() + "'" +
                            " ,SCHED_TIME= '" + schedTime + "'" +
                            " ,GOOUT_ALARMTIME= '" + goOutAlarmTime + "'" +
                            " ,READY_ENABLED= '" + 0 +
                            " ,GOOUT_ENABLED= '" + 1 +
                            " ,SCHED_ENABLED= '" + 1 +
                            " ,VIEWTYPE" + viewType +
                            " where "+
                            " _id =" + item.getId(); break;
                case Constants.ONLY_SCHED_CONTENT:
                    sql = "update " + Database.TABLE_ALARM +
                            "set "+
                            " LABEL= '" + schedLabel.getText() + "'" +
                            " ,SCHED_TIME= '" + schedTime + "'" +
                            " ,READY_ENABLED= '" + 0 +
                            " ,GOOUT_ENABLED= '" + 0 +
                            " ,SCHED_ENABLED= '" + 1 +
                            " ,VIEWTYPE" + viewType + //뷰타입 변경 젤중요,,
                            " where "+
                            " _id =" + item.getId(); break;
                default:
                    Log.d("SubmitActivity","viewType error.");
                    break;
            }

            Database database = Database.getInstance(context);
            database.execSQL(sql);
            setAlarm(_id,viewType);
        }

    }

    public void cancelAlarm(int _id){ //삭제는 한일정에 매핑된 알람 한꺼번에!
        //item alarm 삭제
        Intent intent = new Intent(context, .class); //TODO
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,_id,intent,0);
        alarmManager.cancel(pendingIntent);
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
