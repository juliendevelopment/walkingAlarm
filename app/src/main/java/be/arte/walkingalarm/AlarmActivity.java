package be.arte.walkingalarm;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import androidx.activity.ComponentActivity;
import androidx.lifecycle.ViewModelProvider;
import be.arte.walkingalarm.createalarm.CreateAlarmViewModel;
import be.arte.walkingalarm.data.Alarm;
import butterknife.BindView;
import butterknife.ButterKnife;

public class AlarmActivity extends ComponentActivity {

	@BindView(R.id.alarm_time_display)
	TextView time;

	@BindView(R.id.alarm_steps)
	TextView stepsComponent;
	@BindView(R.id.alarm_schedule_alarm)
	Button scheduleAlarm;
	@BindView(R.id.alarm_on_off_switch)
	Switch enableSwitch;

	private Alarm theAlarm;
	private CreateAlarmViewModel createAlarmViewModel;

	private int hour;
	private int minute;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_alarm);

		ButterKnife.bind(this);

		this.createAlarmViewModel = new ViewModelProvider(this).get(CreateAlarmViewModel.class);
		theAlarm = createAlarmViewModel.getTheAlarm();

		if (theAlarm == null) {
			theAlarm = new Alarm(
					1,
					6,
					0,
					false,
					20,
					System.currentTimeMillis()
			);
			createAlarmViewModel.insert(theAlarm);
		}

		enableSwitch.setChecked(theAlarm.isEnable());
		hour = theAlarm.getHour();
		minute = theAlarm.getMinute();
		time.setText(hour+" : "+minute);

		stepsComponent.setText(theAlarm.getSteps() + "");

		time.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				TimePickerDialog mTimePicker;
				mTimePicker = new TimePickerDialog(AlarmActivity.this, new TimePickerDialog.OnTimeSetListener() {
					@Override
					public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
						hour = selectedHour;
						minute = selectedMinute;
						time.setText(hour+" : "+minute);
					}
				}, hour, minute, true);//Yes 24 hour time
				mTimePicker.setTitle("Select Time");
				mTimePicker.show();
			}
		});

		scheduleAlarm.setOnClickListener(v -> {
			updateAlarm();
			if (theAlarm.isEnable()) {
				theAlarm.schedulePreAlarm(getApplicationContext());
			}else{
				theAlarm.cancelAlarm(getApplicationContext());
			}
		});

	}

	private void updateAlarm() {
		theAlarm.setHour(hour);
		theAlarm.setMinute(minute);
		theAlarm.setEnable(enableSwitch.isChecked());
		theAlarm.setSteps(Integer.parseInt(stepsComponent.getText().toString()));
		createAlarmViewModel.update(theAlarm);
	}
}
