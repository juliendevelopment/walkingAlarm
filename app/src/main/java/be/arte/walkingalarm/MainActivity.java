package be.arte.walkingalarm;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import be.arte.walkingalarm.createalarm.CreateAlarmViewModel;
import be.arte.walkingalarm.createalarm.TimePickerUtil;
import be.arte.walkingalarm.data.Alarm;
import be.arte.walkingalarm.service.AlarmService;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

	@BindView(R.id.main_time_display)
	TextView time;
	@BindView(R.id.main_schedule_alarm)
	Button scheduleAlarm;
	@BindView(R.id.main_on_off_switch)
	Switch enableSwitch;

	Alarm theAlarm;
	private CreateAlarmViewModel createAlarmViewModel;

	private int hour;
	private int minute;

	private AlarmService alarmService = new AlarmService();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		ButterKnife.bind(this);

		this.createAlarmViewModel = new ViewModelProvider(this).get(CreateAlarmViewModel.class);
		theAlarm = createAlarmViewModel.getTheAlarm();

		if (theAlarm == null) {
			theAlarm = new Alarm(
					1,
					6,
					0,
					System.currentTimeMillis(),
					false
			);
			createAlarmViewModel.insert(theAlarm);
		}

		enableSwitch.setChecked(theAlarm.isEnable());
		hour = theAlarm.getHour();
		minute = theAlarm.getMinute();
		time.setText(hour+" : "+minute);

		time.setOnClickListener(v -> {
			TimePickerDialog mTimePicker;
			mTimePicker = new TimePickerDialog(MainActivity.this, (timePicker, selectedHour, selectedMinute) -> {
				hour = selectedHour;
				minute = selectedMinute;
				time.setText(hour+" : "+minute);
			}, hour, minute, true);//Yes 24 hour time
			mTimePicker.setTitle("Select Time");
			mTimePicker.show();
		});

		scheduleAlarm.setOnClickListener(v -> {
			updateAlarm();
			if (theAlarm.isEnable()) {
				alarmService.schedule(getApplicationContext(), theAlarm);
			}else{
				alarmService.cancelAlarm(getApplicationContext(), theAlarm);
			}
		});

	}

	private void updateAlarm() {
		theAlarm.setHour(hour);
		theAlarm.setMinute(minute);
		theAlarm.setEnable(enableSwitch.isChecked());
		createAlarmViewModel.update(theAlarm);
	}
}
