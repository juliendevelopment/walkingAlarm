package be.arte.walkingalarm.broadcastreceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import be.arte.walkingalarm.data.Alarm;
import be.arte.walkingalarm.data.AlarmDao;
import be.arte.walkingalarm.data.AlarmDatabase;
import be.arte.walkingalarm.service.AlarmService;

public class CheckOnBootReceiver extends BroadcastReceiver {

	private AlarmService alarmService = new AlarmService();

	@Override
	public void onReceive(Context context, Intent intent) {
		AlarmDatabase db = AlarmDatabase.getDatabase(context);
		AlarmDao alarmDao = db.alarmDao();
		Alarm theAlarm = alarmDao.getTheAlarm();
		if (theAlarm.isEnable()) {
			alarmService.schedule(context, theAlarm);
		}
	}
}
