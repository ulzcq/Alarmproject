package com.yu.alarmproject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class ReadyAlarmReceiver extends BroadcastReceiver {
    public static final String READY_ALARM_ALERT_ACTION = "com.yu.alarmproject.READY_ALARM_ALERT";

    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals(READY_ALARM_ALERT_ACTION)) {
            Intent service_intent = new Intent(context, ReadyAlarmService.class);
            context.startService(service_intent);
        }
    }
}
