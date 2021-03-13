package com.rutershok.phrases.notifications;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.rutershok.phrases.model.Storage;
import com.rutershok.phrases.R;
import com.rutershok.phrases.utils.Key;

import java.util.Calendar;

public class NightAlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (Storage.getNotificationsEnabled(context) && Calendar.getInstance().get(Calendar.HOUR_OF_DAY) == Key.TIME_NIGHT) {
            Notification.send(context, context.getString(R.string.key_goodnight), context.getString(R.string.cat_goodnight));
        }
    }
}
