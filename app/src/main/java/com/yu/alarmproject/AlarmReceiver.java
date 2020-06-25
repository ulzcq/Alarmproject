package com.yu.alarmproject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AlarmReceiver extends BroadcastReceiver {
    public static final String READY_ALARM_ALERT_ACTION = "com.yu.alarmproject.READY_ALARM_ALERT";
    public static final String GOOUT_ALARM_ALERT_ACTION = "com.yu.alarmproject.GOOUT_ALARM_ALERT";
    public static final String SCHED_ALARM_ALERT_ACTION = "com.yu.alarmproject.SCHED_ALARM_ALERT";

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent Alarmintent = new Intent(context, AlarmAlertActivity.class);
        Alarmintent.setAction(intent.getAction());
        context.startActivity(Alarmintent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
    }
}
