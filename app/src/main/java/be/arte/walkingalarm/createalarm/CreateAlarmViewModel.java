package be.arte.walkingalarm.createalarm;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import be.arte.walkingalarm.data.Alarm;
import be.arte.walkingalarm.data.AlarmRepository;

public class CreateAlarmViewModel extends AndroidViewModel {
    private AlarmRepository alarmRepository;

    public CreateAlarmViewModel(@NonNull Application application) {
        super(application);

        alarmRepository = new AlarmRepository(application);
    }

    public void insert(Alarm alarm) {
        alarmRepository.insert(alarm);
    }

    public Alarm getTheAlarm() {
		return alarmRepository.getTheAlarm();
	}

	public void update(Alarm alarm) {
		alarmRepository.update(alarm);
	}




}
