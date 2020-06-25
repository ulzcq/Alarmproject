package com.yu.alarmproject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.view.Window;
import android.widget.Toast;

public class GoOutAlarmReceiver extends BroadcastReceiver {
    public static final String GOOUT_ALARM_ALERT_ACTION = "com.yu.alarmproject.GOOUT_ALARM_ALERT";

    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals(GOOUT_ALARM_ALERT_ACTION)) {
            Intent service_intent = new Intent(context, GoOutAlarmService.class);
            context.startService(service_intent);
        }
    }
}
