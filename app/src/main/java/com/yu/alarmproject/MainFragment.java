package com.yu.alarmproject;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Switch;
import android.widget.TextView;

import java.text.SimpleDateFormat;


public class MainFragment extends Fragment {
    private TextView beforeSched;
    private CheckBox checkBox;
    private Lateness item;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_today_alarm, container, false);

        //TODO: db table에서 이전 일정 가져와서 출력
        //TODO: 체크박스로 이전 일정에 매핑되는 지각여부 입력 받아서 db table에 저장

        applyItem();
        saveLateness();
        CheckBox checkBox = rootView.findViewById(R.id.checkBox);
        //체크박스가 체크/언체크 되면 -> 킵해둔 item의 id로 db변경하면 된다.
        modifyLateness();
    }

    private void saveLateness(){

    }

    private void applyItem(){
        Database database = Database.getInstance(getContext());
        //db에서 제일 최근 일정 가져와서 뷰에 set

        //없으면 없다고 출력
    }
    private void modifyLateness(int id){
        //lateness db 수정
    }
}
