package be.arte.walkingalarm.broadcastreceiver;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;
import androidx.core.app.NotificationCompat;
import be.arte.walkingalarm.App;
import be.arte.walkingalarm.PreRingActivity;
import be.arte.walkingalarm.R;
import be.arte.walkingalarm.WalkingRingActivity;
import be.arte.walkingalarm.data.Alarm;
import be.arte.walkingalarm.data.AlarmDao;
import be.arte.walkingalarm.data.AlarmDatabase;

public class PreAlarmBroadcastReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
		Log.i("TAG", "onReceive receiver");

		AlarmDatabase db = AlarmDatabase.getDatabase(context);
		AlarmDao alarmDao = db.alarmDao();
		Alarm theAlarm = alarmDao.getTheAlarm();
		if (theAlarm.isEnable()) {
			theAlarm.schedule(context);
		}

		Toast.makeText(context, "onReceive receiver", Toast.LENGTH_LONG).show();
		String toastText = String.format("pre Alarm Received");
		Toast.makeText(context, toastText, Toast.LENGTH_SHORT).show();

		Intent alarmIntent = new Intent(context, PreRingActivity.class);
		alarmIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(alarmIntent);

		addNotification(context, alarmIntent);
	}

	private void addNotification(Context context, Intent alarmIntent) {
		PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, alarmIntent, 0);

		String alarmTitle = String.format("%s Alarm", "the");

		Notification notification = new NotificationCompat.Builder(context, App.CHANNEL_ID_2)
				.setContentTitle(alarmTitle)
				.setContentText("pre alarm")
				.setSmallIcon(R.drawable.ic_alarm_black_24dp)
				.setContentIntent(pendingIntent)
				.build();

		NotificationManager mNotificationManager =
				(NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		mNotificationManager.notify(1, notification);
	}
}
