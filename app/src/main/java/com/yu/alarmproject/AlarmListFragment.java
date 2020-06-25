package com.yu.alarmproject;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Switch;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class AlarmListFragment extends Fragment {
    private RecyclerView recyclerView;
    private AlarmAdapter adapter;
    private AlarmManager alarmManager; //TODO:개별알람 on/off
    private Context context;
    private ArrayList<SchedAlarm> items;
    //listener; TODO: 한 아이템 클릭하면 수정 -> AddAlarmActiity로 이동


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        this.context = context;
    }

    @Override
    public void onDetach() {
        super.onDetach();

        if(context != null){
            context = null;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_show_alarm_list, container, false);
        initUI(rootView);

        loadAlarmList(); //db에서 리스트가져오기, dapater 초기화

        alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);

        adapter.setOnItemClickListener(new AlarmAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(getActivity(),SubmitActivity.class);
                intent.putExtra("SchedAlarm",adapter.getItem(position));
                startActivity(intent);
            }
        });

        adapter.setOnButtonClickListenr(new AlarmAdapter.OnButtonClickListener() {
            @Override
            public void ondeleteClick(View view, int position) {
                //TODO: 삭제처리
                //삭제하시겠습니까 팝업창
                //알람 취소 및 삭제
                deleteAlarmData(adapter.getItem(position));
                loadAlarmList();
                recyclerView.setAdapter(adapter);
            }
        });

        Button addButton = rootView.findViewById(R.id.addButton);
        addButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SubmitActivity.class);
                startActivity(intent);
            }
        });

        return rootView;
    }


    private void deleteAlarmData(SchedAlarm item){

        //db에서 알람데이터 삭제
        String sql="";
        if (item != null) {
            sql = "delete from " + Database.TABLE_ALARM + " where " + " _id = " + item.getId();

            Database database = Database.getInstance(context);
            database.execSQL(sql);
        }
        cancelAlarm(item.getId(), item.getViewType()); //알람 삭제
    }

    public void cancelAlarm(int _id, int viewType){ //삭제는 한일정에 매핑된 알람 한꺼번에!
        //item alarm 삭제, _id로 구분됨!
        Intent intent; //TODO

        switch(viewType){
            case Constants.INTEGRATED_CONTENT:
                intent = new Intent(context, AlarmReceiver.class);
                intent.setAction(AlarmReceiver.READY_ALARM_ALERT_ACTION);
                alarmManager.cancel(getPendingIntent(intent,_id)); //준비시작 알람 삭제

                intent = new Intent(context, AlarmReceiver.class);
                intent.setAction(AlarmReceiver.GOOUT_ALARM_ALERT_ACTION);
                alarmManager.cancel(getPendingIntent(intent,_id));//출발 알람 삭제
                break;
            case Constants.ONLY_READY_CONTENT:
                intent = new Intent(context, AlarmReceiver.class);
                intent.setAction(AlarmReceiver.READY_ALARM_ALERT_ACTION);
                alarmManager.cancel(getPendingIntent(intent,_id));//준비시작 알람 삭제
                break;
            case Constants.ONLY_GOOUT_CONTENT:
                intent = new Intent(context, AlarmReceiver.class);
                intent.setAction(AlarmReceiver.GOOUT_ALARM_ALERT_ACTION);
                alarmManager.cancel(getPendingIntent(intent,_id));//출발 알람 삭제
                break;
            case Constants.ONLY_SCHED_CONTENT:
                intent = new Intent(context, AlarmReceiver.class);
                intent.setAction(AlarmReceiver.SCHED_ALARM_ALERT_ACTION);
                alarmManager.cancel(getPendingIntent(intent,_id));//일정 알람삭제
                break;
        }
    }

    private PendingIntent getPendingIntent(Intent intent, int _id){
        return PendingIntent.getBroadcast(context, _id, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private int loadAlarmList(){

        String sql = "select _id, LABEL, SCHEDTIME, READY_ALARMTIME, GOOUT_ALARMTIME, SCHED_ALARMTIME,"
                + " REDAY_ENABLED, GOOUT_ENABLED, SCHED_ENABLED, VIEWTYPE,"
                + " READY_H, READY_M, MOVE_H, MOVE_M from "
                + Database.TABLE_ALARM;

        int recordCnt = -1;
        Database database = Database.getInstance(context);
        if( database != null){
            Cursor outCursor = database.rawQuery(sql);
            recordCnt = outCursor.getCount();

            ArrayList<SchedAlarm> items = new ArrayList<SchedAlarm>();
            for(int i=0; i<recordCnt; i++){
                outCursor.moveToNext();

                int _id = outCursor.getInt(0);
                String label = outCursor.getString(1);

                Calendar schedTime = Calendar.getInstance();
                Calendar readyAlarmTime = Calendar.getInstance();
                Calendar goOutAlarmTime = Calendar.getInstance();
                try{
                    Date date = Constants.dateFormat.parse(outCursor.getString(2));
                    schedTime.setTime(date);

                    date = Constants.dateFormat.parse(outCursor.getString(3));
                    schedTime.setTime(date);

                    date = Constants.dateFormat.parse(outCursor.getString(4));
                    schedTime.setTime(date);
                }catch(ParseException e){
                    e.printStackTrace();
                }

                boolean readyEnabled = outCursor.getInt(5)==0 ? false:true;
                boolean goOutEnabled = outCursor.getInt(6)==0 ? false:true;
                boolean schedEnabled = outCursor.getInt(7)==0 ? false:true;

                int viewType = outCursor.getInt(8);
                int ready_h = outCursor.getInt(9);
                int ready_m = outCursor.getInt(10);
                int move_h = outCursor.getInt(11);
                int move_m = outCursor.getInt(12);

                switch(viewType){
                    case Constants.INTEGRATED_CONTENT:
                        items.add(new SchedAlarm(_id,label,schedTime,readyAlarmTime,goOutAlarmTime,readyEnabled,goOutEnabled,viewType,ready_h,ready_m,move_h,move_m));
                        break;
                    case Constants.ONLY_READY_CONTENT:
                        items.add(new SchedAlarm(_id,label,schedTime,readyAlarmTime,readyEnabled,viewType,ready_h,ready_m));
                        break;
                    case Constants.ONLY_GOOUT_CONTENT:
                        items.add(new SchedAlarm(_id,label,schedTime,goOutAlarmTime,goOutEnabled,viewType,move_h,move_m));
                        break;
                    case Constants.ONLY_SCHED_CONTENT:
                        items.add(new SchedAlarm(_id,label,schedTime,schedEnabled,viewType));
                        break;
                }

                //TODO: items에 추가해서 adpater에 set

                outCursor.close();

                adapter.setItems(items);
                adapter.notifyDataSetChanged();
            }
        }
        return recordCnt;
    }

    private void initUI(ViewGroup rootView){
        recyclerView = rootView.findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        items = new ArrayList<>();
        //TODO : db에서 일정-알람 목록 받아와서 itmes에 schedAlarm 객체초기화 후 add
        Calendar test = Calendar.getInstance();
        test.set(Calendar.HOUR_OF_DAY, 6);
        test.set(Calendar.MINUTE, 30);
        test.set(Calendar.SECOND, 0);
        test.set(Calendar.MILLISECOND, 0); //6시 30분

        Calendar Rtest = Calendar.getInstance();
        test.set(Calendar.HOUR_OF_DAY, 5);
        test.set(Calendar.MINUTE, 0);
        test.set(Calendar.SECOND, 0);
        test.set(Calendar.MILLISECOND, 0); //5시

        Calendar Gtest = Calendar.getInstance();
        test.set(Calendar.HOUR_OF_DAY, 5);
        test.set(Calendar.MINUTE, 30);
        test.set(Calendar.SECOND, 0);
        test.set(Calendar.MILLISECOND, 0); //5시 30분

        items.add(new SchedAlarm(1, "준비o출발o",test,Rtest,Gtest, true,true,Constants.INTEGRATED_CONTENT,0,30,0,30));//샘플
        items.add(new SchedAlarm(2,"준비o출발x",test,Rtest,true, Constants.ONLY_READY_CONTENT,1,0));//샘플
        items.add(new SchedAlarm(3,"준비x출발o",test,Gtest,true,Constants.ONLY_GOOUT_CONTENT,0,30));//샘플
        items.add(new SchedAlarm(4,"준비x출발x",test,true,Constants.ONLY_SCHED_CONTENT));//샘플

        adapter = new AlarmAdapter(items);
        recyclerView.setAdapter(adapter); //리싸이클러뷰에 adapter 등록
    }

}
