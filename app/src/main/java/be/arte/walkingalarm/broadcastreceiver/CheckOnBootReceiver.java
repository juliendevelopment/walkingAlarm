package be.arte.walkingalarm.broadcastreceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import androidx.lifecycle.ViewModelProvider;
import be.arte.walkingalarm.createalarm.CreateAlarmViewModel;
import be.arte.walkingalarm.data.Alarm;
import be.arte.walkingalarm.data.AlarmDao;
import be.arte.walkingalarm.data.AlarmDatabase;

public class CheckOnBootReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		AlarmDatabase db = AlarmDatabase.getDatabase(context);
		AlarmDao alarmDao = db.alarmDao();
		Alarm theAlarm = alarmDao.getTheAlarm();
		if (theAlarm.isEnable()) {
			theAlarm.schedulePreAlarm(context);
		}
	}
}
