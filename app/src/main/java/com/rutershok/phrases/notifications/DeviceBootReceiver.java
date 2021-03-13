package com.rutershok.phrases.notifications;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.rutershok.phrases.utils.Key;

import java.util.Calendar;

public class DeviceBootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction() != null &&
                intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

            calendar.set(Calendar.HOUR_OF_DAY, Key.TIME_MORNING);
            manager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, PendingIntent.getBroadcast(context, 0, new Intent(context, MorningAlarmReceiver.class), 0));

            calendar.set(Calendar.HOUR_OF_DAY, Key.TIME_NIGHT);
            manager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, PendingIntent.getBroadcast(context, 0, new Intent(context, MorningAlarmReceiver.class), 0));
        }
    }
}
