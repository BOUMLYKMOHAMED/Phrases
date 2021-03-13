package com.rutershok.phrases.notifications;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.rutershok.phrases.model.Storage;
import com.rutershok.phrases.R;
import com.rutershok.phrases.utils.Key;

import java.util.Calendar;

public class MorningAlarmReceiver extends BroadcastReceiver {

    @SuppressLint("UnsafeProtectedBroadcastReceiver")
    @Override
    public void onReceive(Context context, Intent intent) {
        if (Storage.getNotificationsEnabled(context) &&
                Calendar.getInstance().get(Calendar.HOUR_OF_DAY) == Key.TIME_MORNING) {
            Notification.send(context, context.getString(R.string.key_good_morning), context.getString(R.string.cat_good_morning));
        }
    }
}