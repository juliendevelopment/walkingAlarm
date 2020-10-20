package be.arte.walkingalarm.service;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import be.arte.walkingalarm.App;
import be.arte.walkingalarm.R;
import be.arte.walkingalarm.RingActivity;

public class AlarmService extends Service {

    @Override
    public void onCreate() {
        super.onCreate();
		Log.i("AlarmService","onCreate()" );

		Intent alarmIntent = new Intent(this, RingActivity.class);
		alarmIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(alarmIntent);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
		Log.i("AlarmService","onStartCommand()" );

		Intent alarmIntent = new Intent(this, RingActivity.class);
		alarmIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(alarmIntent);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, alarmIntent, 0);

        String alarmTitle = String.format("%s Alarm", "the");

        Notification notification = new NotificationCompat.Builder(this, App.CHANNEL_ID)
                .setContentTitle(alarmTitle)
                .setContentText("Ring Ring .. Ring Ring")
                .setSmallIcon(R.drawable.ic_alarm_black_24dp)
                .setContentIntent(pendingIntent)
                .build();

        startForeground(1, notification);

        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
