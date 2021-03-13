package com.rutershok.phrases.notifications;

import static android.content.Context.ALARM_SERVICE;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.rutershok.phrases.MainActivity;
import com.rutershok.phrases.R;
import com.rutershok.phrases.model.Storage;
import com.rutershok.phrases.utils.Key;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Calendar;

public class Notification {

    public static void initialize(Context context) {
        //Set time now
        Calendar calendarMorning = Calendar.getInstance();
        calendarMorning.set(Calendar.HOUR_OF_DAY, Key.TIME_MORNING);
        calendarMorning.set(Calendar.MINUTE, 0);
        calendarMorning.set(Calendar.SECOND, 0);
        calendarMorning.set(Calendar.MILLISECOND, 0);

        Calendar calendarNight = Calendar.getInstance();
        calendarNight.set(Calendar.HOUR_OF_DAY, Key.TIME_NIGHT);
        calendarNight.set(Calendar.MINUTE, 0);
        calendarNight.set(Calendar.SECOND, 0);
        calendarNight.set(Calendar.MILLISECOND, 0);

        // Create two different PendingIntents, they MUST have different requestCodes
        Intent intentMorning = new Intent(context, MorningAlarmReceiver.class);
        Intent intentNight = new Intent(context, NightAlarmReceiver.class);

        PendingIntent pendingIntentMorning = PendingIntent.getBroadcast(context, 100, intentMorning, 0);
        PendingIntent pendingIntentNight = PendingIntent.getBroadcast(context, 200, intentNight, 0);

        // Start both alarms, set to repeat once every day
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        if (alarmManager != null) {
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendarMorning.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntentMorning);
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendarNight.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntentNight);
        }
    }

    public static void send(final Context context, String categoryKey, final String title) {
        try {
            String url = "http://phrases.netsons.org/api/get_phrase.php"
                    + "?language=" + Storage.getLanguage(context)
                    + "&category=" + URLEncoder.encode(categoryKey, "UTF-8");
            Volley.newRequestQueue(context).add(new StringRequest(Request.Method.GET, url,
                    response -> {
                        try {
                            create(context, title, new JSONObject(response)
                                    .getJSONArray(Key.PHRASES)
                                    .getJSONObject(0)
                                    .getString(Key.PHRASE));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }, null));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    //This method generate notification
    private static void create(Context context, String messageTitle, String messageBody) {
        // Notification channel ( >= API 26 )
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            ((NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE)).createNotificationChannel(new NotificationChannel("PHRASES_CHANNEL", context.getString(R.string.app_name), NotificationManager.IMPORTANCE_HIGH));
        }

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, "PHRASES_CHANNEL")
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher))
                .setSmallIcon(R.drawable.ic_notification_icon)
                .setContentTitle(messageTitle)
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(messageBody))
                .setContentIntent(PendingIntent.getActivity(context, 0, new Intent(context, MainActivity.class)/*.putExtra("fromNotification", true)*/, PendingIntent.FLAG_UPDATE_CURRENT))
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (notificationManager != null) {
            notificationManager.notify(0, notificationBuilder.build());
        }
    }
}
