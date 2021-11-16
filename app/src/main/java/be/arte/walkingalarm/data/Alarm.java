package be.arte.walkingalarm.data;

import java.util.Calendar;

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
import be.arte.walkingalarm.createalarm.DayUtil;

@Entity(tableName = "alarm_table")
public class Alarm {
    @PrimaryKey
    @NonNull
    private int alarmId;

    private int hour, minute;
    private boolean enable;

    private  int numberOfStep;

    private long created;

    public Alarm(int alarmId, int hour, int minute, long created, boolean enable) {
        this.alarmId = alarmId;
        this.hour = hour;
        this.minute = minute;
        this.enable = enable;
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

	public int getNumberOfStep() {
		return numberOfStep;
	}

	public void setNumberOfStep(int numberOfStep) {
		this.numberOfStep = numberOfStep;
	}

	public boolean isEnable() {
        return enable;
    }

    public long getCreated() {
        return created;
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
}
