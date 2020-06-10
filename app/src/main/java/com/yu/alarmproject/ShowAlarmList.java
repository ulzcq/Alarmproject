package com.yu.alarmproject;

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

import java.util.ArrayList;

public class ShowAlarmList extends Fragment {
    private RecyclerView recyclerView;
    private AlarmAdapter adapter;
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

        loadAlarmList();

        //알람삭제버튼 클릭 : 해당 item db조회 - 알람리퀘스트코드로 구별해 삭제
        Button deleteButton = rootView.findViewById(R.id.deleteButton);
        deleteButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), DeleteActivity.class);
                startActivity(intent);
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

    public int loadAlarmList(){

        String sql = "select _id, LABEL, SCHEDTIME, READY_ALARMTIME, GOOUT_ALARMTIME, REDAY_ENABLED, GOOUT_ENABLED, SCHED_ENABLED, VIEWTYPE from "
                + Database.TABLE_ALARM
                + " order by CREATE_DATE desc";

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
                String schedTime = outCursor.getString(2);
                String readyAlarmTime = outCursor.getString(3);
                String goOutAlarmTime = outCursor.getString(4);
                boolean readyEnabled = outCursor.getInt(5)==0 ? false:true;
                boolean goOutEnabled = outCursor.getInt(6)==0 ? false:true;
                boolean schedEnabled = outCursor.getInt(7)==0 ? false:true;
                int viewType = outCursor.getInt(8);

                if(label != null && label.length() >10){
                    try{
                        //TODO:글자수 10 이상이면 어떠케처리..? 이부분 해 말어?
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }
                switch(viewType){
                    case Constants.INTEGRATED_CONTENT:
                        items.add(new SchedAlarm(_id,label,schedTime,readyAlarmTime,goOutAlarmTime,readyEnabled,goOutEnabled,viewType));
                        break;
                    case Constants.ONLY_READY_CONTENT:
                        items.add(new SchedAlarm(_id,label,schedTime,readyAlarmTime,readyEnabled,viewType));
                        break;
                    case Constants.ONLY_GOOUT_CONTENT:
                        items.add(new SchedAlarm(_id,label,schedTime,goOutAlarmTime,goOutEnabled,viewType));
                        break;
                    case Constants.ONLY_SCHED_CONTENT:
                        items.add(new SchedAlarm(_id,label,schedTime,schedEnabled,viewType));
                        break;
                }

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
        items.add(new SchedAlarm(1, "준비o출발o","06:00","05:00","5:30",
                true,true,Constants.INTEGRATED_CONTENT,0,30,0,30));//샘플
        items.add(new SchedAlarm(2,"준비o출발x","06:00","05:00",true, Constants.ONLY_READY_CONTENT,1,0));//샘플
        items.add(new SchedAlarm(3,"준비x출발o","06:00","5:30",true,Constants.ONLY_GOOUT_CONTENT,0,30));//샘플
        items.add(new SchedAlarm(4,"준비x출발x","06:00",true,Constants.ONLY_SCHED_CONTENT));//샘플
        recyclerView.setAdapter(new AlarmAdapter(items)); //adapter 등록
    }

}
