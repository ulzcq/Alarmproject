package com.yu.alarmproject;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class StatisticsFragment extends Fragment {

    private Context context;
    private LineChart chart;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if(context != null){
            context =null;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.fragment_show_statistics, container, false);

        TextView title = ; //차트 제목 가장 최근 yyyy년 MM월 설정. 버튼 바뀔때마다 바뀌게
        Button leftButton = ;
        Button rightButton = ;

        loadData(); //년,월 넣기 : 가장 최근날짜
        //x축: 요일별? y축: 지각 횟수
        //월별로 보여준다.
        initUI(rootView);


        return rootView;

    }

    private void initUI(ViewGroup rootView){
        chart = rootView.findViewById(R.id.chart);
    }

    public void loadData(Calendar start){
        Calendar end = (Calendar)start.clone();
        end.add(Calendar.MONTH,1);//start의 월+1

        Database database = Database.getInstance(context);
        String sql = "select strftime('%Y/%m %w', SCHEDTIME)" +
                ", LATENESS" +
                "from " + Database.TABLE_LATENESS + " " +
                "where SCHEDTIME >= '" + start +"'" +
                " and SCHEDTIME < '" + end +"'" +
                "group by strftime('%Y/%m %w', LATENESS)";

        Cursor cursor = database.rawQuery(sql);
        int recordCnt = cursor.getCount();

        HashMap<String,Boolean> data = new HashMap<String,Boolean>();
        for(int i=0; i< recordCnt; i++) {
            cursor.moveToNext();

            String weekDay = cursor.getString(0);
            boolean lateness = cursor.getInt(1)==0?false:true;

            data.put(weekDay, lateness);
        }

        setData();
    }

    private void setData(ArrayList<String> key, ArrayList<Boolean> value){
        //TODO: 차트 설정
    }
}
