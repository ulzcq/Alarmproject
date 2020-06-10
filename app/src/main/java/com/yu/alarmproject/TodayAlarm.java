package com.yu.alarmproject;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;


public class TodayAlarm extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_today_alarm, container, false);

        //TODO: db table에서 이전 일정 가져와서 출력
        //TODO: 체크박스로 이전 일정에 매핑되는 지각여부 입력 받아서 db table에 저장

        return rootView;
    }
}
