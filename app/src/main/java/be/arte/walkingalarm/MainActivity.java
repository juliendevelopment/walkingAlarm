package be.arte.walkingalarm;

import java.util.Random;

import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
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
	TimePicker timeText;
	@BindView(R.id.main_schedule_alarm)
	Button scheduleAlarm;
	@BindView(R.id.main_on_off_switch)
	Switch enableSwitch;


	private CreateAlarmViewModel createAlarmViewModel;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		ButterKnife.bind(this);


		this.createAlarmViewModel = new ViewModelProvider(this).get(CreateAlarmViewModel.class);

		scheduleAlarm.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				scheduleAlarm();
			}
		});

	}


	private void scheduleAlarm() {
		Alarm alarm = new Alarm(
				new Random().nextInt(Integer.MAX_VALUE),
				TimePickerUtil.getTimePickerHour(timeText),
				TimePickerUtil.getTimePickerMinute(timeText),
				System.currentTimeMillis(),
				enableSwitch.isChecked()
		);

		// TODO
		//createAlarmViewModel.insert(alarm);
		//alarm.schedule(getApplicationContext());

		Toast.makeText(getApplicationContext(), alarm.toString(), Toast.LENGTH_LONG).show();
	}
}
