package be.arte.walkingalarm.service;

import java.util.Calendar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;
import be.arte.walkingalarm.broadcastreceiver.AlarmBroadcastReceiver;
import be.arte.walkingalarm.createalarm.DayUtil;
import be.arte.walkingalarm.data.Alarm;

public class AlarmService {


	public void schedule(Context context, Alarm alarm) {
		AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		Intent intent = new Intent(context, AlarmBroadcastReceiver.class);
		PendingIntent alarmPendingIntent = PendingIntent.getBroadcast(context, alarm.getAlarmId(), intent, 0);

		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(System.currentTimeMillis());
		calendar.set(Calendar.HOUR_OF_DAY, alarm.getHour());
		calendar.set(Calendar.MINUTE, alarm.getMinute());
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);

		// if alarm time has already passed, increment day by 1
		if (calendar.getTimeInMillis() <= System.currentTimeMillis()) {
			calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) + 1);
		}


		String toastText = null;
		try {
			toastText = String.format("Alarm scheduled for %s at %02d:%02d", DayUtil.toDay(calendar.get(Calendar.DAY_OF_WEEK)), alarm.getHour(), alarm.getMinute(), alarm.getAlarmId());
		} catch (Exception e) {
			e.printStackTrace();
		}


		alarmManager.setExact(
				AlarmManager.RTC_WAKEUP,
				calendar.getTimeInMillis(),
				alarmPendingIntent
		);

		Log.i("Alarm", "schedule");
		Toast.makeText(context, toastText, Toast.LENGTH_LONG).show();
	}

	public void cancelAlarm(Context context, Alarm alarm) {
		AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		Intent intent = new Intent(context, AlarmBroadcastReceiver.class);
		PendingIntent alarmPendingIntent = PendingIntent.getBroadcast(context, alarm.getAlarmId(), intent, 0);
		alarmManager.cancel(alarmPendingIntent);

		String toastText = String.format("Alarm cancelled for %02d:%02d with id %d", alarm.getHour(), alarm.getMinute(), alarm.getAlarmId());
		Toast.makeText(context, toastText, Toast.LENGTH_SHORT).show();
		Log.i("Alarm", toastText);
	}
}
