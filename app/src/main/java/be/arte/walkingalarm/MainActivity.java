package be.arte.walkingalarm;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TimePicker;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import be.arte.walkingalarm.createalarm.CreateAlarmViewModel;
import be.arte.walkingalarm.createalarm.TimePickerUtil;
import be.arte.walkingalarm.data.Alarm;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

	@BindView(R.id.main_timePicker)
	TimePicker timePicker;
	@BindView(R.id.main_schedule_alarm)
	Button scheduleAlarm;
	@BindView(R.id.main_on_off_switch)
	Switch enableSwitch;

	Alarm theAlarm;
	private CreateAlarmViewModel createAlarmViewModel;

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
		timePicker.setHour(theAlarm.getHour());
		timePicker.setMinute(theAlarm.getMinute());

		scheduleAlarm.setOnClickListener(v -> {
			updateAlarm();
			if (theAlarm.isEnable()) {
				theAlarm.schedule(getApplicationContext());
			}
		});

	}

	private void updateAlarm() {
		theAlarm.setHour(TimePickerUtil.getTimePickerHour(timePicker));
		theAlarm.setMinute(TimePickerUtil.getTimePickerMinute(timePicker));
		theAlarm.setEnable(enableSwitch.isChecked());
		createAlarmViewModel.update(theAlarm);
	}
}
