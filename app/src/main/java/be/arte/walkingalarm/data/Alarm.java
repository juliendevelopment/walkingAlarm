package be.arte.walkingalarm.data;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.EnumSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import be.arte.walkingalarm.broadcastreceiver.AlarmBroadcastReceiver;
import be.arte.walkingalarm.broadcastreceiver.PreAlarmBroadcastReceiver;
import be.arte.walkingalarm.createalarm.DayUtil;

@Entity(tableName = "alarm_table")
public class Alarm {
	private static final int PRE_ALARM_MINUTE_DELTA = 10;
	@PrimaryKey
    @NonNull
    private int alarmId;

    private int hour, minute;
    private boolean enable;

    private int steps;

    private long created;

	public Alarm(int alarmId, int hour, int minute, boolean enable, int steps, long created) {
		this.alarmId = alarmId;
		this.hour = hour;
		this.minute = minute;
		this.enable = enable;
		this.steps = steps;
		this.created = created;
	}

	public void setHour(int hour) {
        this.hour = hour;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public int getAlarmId() {
        return alarmId;
    }

    public int getHour() {
        return hour;
    }

    public int getMinute() {
        return minute;
    }

    public boolean isEnable() {
        return enable;
    }

    public long getCreated() {
        return created;
    }

	public int getSteps() {
		return steps;
	}

	public void setSteps(int steps) {
		this.steps = steps;
	}



	public void schedulePreAlarm(Context context) {
		Intent intent = new Intent(context, PreAlarmBroadcastReceiver.class);
		schedulteAlarm(context, intent, createCalandarPreAlarm());

		Log.i("PreAlarm", "schedule");
	}
	public void schedule(Context context) {
        Intent intent = new Intent(context, AlarmBroadcastReceiver.class);
		Calendar calandarAlarm = createCalandarAlarm();
		schedulteAlarm(context, intent, calandarAlarm);

		try {
			String toastText = String.format("Alarm scheduled for %s at %02d:%02d", DayUtil.toDay(calandarAlarm.get(Calendar.DAY_OF_WEEK)), hour, minute, alarmId);
			toastText += "\n";
			toastText += computeDiff(calandarAlarm.getTime(), new Date());
        	Toast.makeText(context, toastText, Toast.LENGTH_LONG).show();
		} catch (Exception e) {
			e.printStackTrace();
		}

        Log.i("Alarm", "schedule");
    }

	@NonNull
	private void schedulteAlarm(Context context, Intent intent, Calendar calandarAlarm) {
		PendingIntent alarmPendingIntent = PendingIntent.getBroadcast(context, alarmId, intent, 0);

		AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		Log.i("alarmManager", "date="+calandarAlarm.getTime());
		alarmManager.setExact(
				AlarmManager.RTC_WAKEUP,
				calandarAlarm.getTimeInMillis(),
				alarmPendingIntent
		);
	}

	@NonNull
	private Calendar createCalandarAlarm() {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(System.currentTimeMillis());
		calendar.set(Calendar.HOUR_OF_DAY, hour);
		calendar.set(Calendar.MINUTE, minute);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);

		// if alarm time has already passed, increment day by 1
		if (calendar.getTimeInMillis() <= System.currentTimeMillis()) {
			calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) + 1);
		}
		return calendar;
	}

	@NonNull
	private Calendar createCalandarPreAlarm() {
		Calendar calendar = createCalandarAlarm();
		calendar.set(Calendar.MINUTE, minute - PRE_ALARM_MINUTE_DELTA);
		return calendar;
	}

	public void cancelAlarm(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmBroadcastReceiver.class);
        PendingIntent alarmPendingIntent = PendingIntent.getBroadcast(context, alarmId, intent, 0);
        alarmManager.cancel(alarmPendingIntent);

        String toastText = String.format("Alarm cancelled for %02d:%02d with id %d", hour, minute, alarmId);
        Toast.makeText(context, toastText, Toast.LENGTH_SHORT).show();
        Log.i("Alarm", toastText);
    }

    @Override
    public String toString() {
        return "Alarm{" +
               "alarmId=" + alarmId +
               ", hour=" + hour +
               ", minute=" + minute +
               ", enable=" + enable +
               ", created=" + created +
               '}';
    }

	public static String computeDiff(Date date1, Date date2) {

		long diffInMillies = date2.getTime() - date1.getTime();

		//create the list
		List<TimeUnit> units = new ArrayList<TimeUnit>(EnumSet.allOf(TimeUnit.class));
		Collections.reverse(units);

		//create the result map of TimeUnit and difference
		Map<TimeUnit,Long> result = new LinkedHashMap<TimeUnit,Long>();
		long milliesRest = diffInMillies;

		for ( TimeUnit unit : units ) {

			//calculate difference in millisecond
			long diff = unit.convert(milliesRest, TimeUnit.MILLISECONDS);
			long diffInMilliesForUnit = unit.toMillis(diff);
			milliesRest = milliesRest - diffInMilliesForUnit;

			//put the result in the map
			result.put(unit,diff);
		}

		Long days = result.get(TimeUnit.DAYS);
		Long hours = result.get(TimeUnit.HOURS);
		Long minutes = result.get(TimeUnit.MINUTES);
		Long seconds = result.get(TimeUnit.SECONDS);

		return String.format("%d days %d:%d %ds", days, hours, minutes, seconds);
	}
}
