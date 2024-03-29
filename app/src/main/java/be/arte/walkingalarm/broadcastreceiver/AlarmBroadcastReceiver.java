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
import be.arte.walkingalarm.R;
import be.arte.walkingalarm.RingActivity;

public class AlarmBroadcastReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
		Log.i("TAG", "onReceive receiver");
		Toast.makeText(context, "onReceive receiver", Toast.LENGTH_LONG).show();
            String toastText = String.format("Alarm Received");
            Toast.makeText(context, toastText, Toast.LENGTH_SHORT).show();

		Intent alarmIntent = new Intent(context, RingActivity.class);
		alarmIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(alarmIntent);

		addNotification(context, alarmIntent);
	}

	private void addNotification(Context context, Intent alarmIntent) {
		PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, alarmIntent, 0);

		String alarmTitle = String.format("%s Alarm", "the");

		Notification notification = new NotificationCompat.Builder(context, App.CHANNEL_ID)
				.setContentTitle(alarmTitle)
				.setContentText("Ring Ring .. Ring Ring")
				.setSmallIcon(R.drawable.ic_alarm_black_24dp)
				.setContentIntent(pendingIntent)
				.build();

		NotificationManager mNotificationManager =
				(NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		mNotificationManager.notify(1, notification);
	}
}
