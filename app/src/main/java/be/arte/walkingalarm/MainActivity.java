package be.arte.walkingalarm;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import androidx.activity.ComponentActivity;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import be.arte.walkingalarm.createalarm.CreateAlarmViewModel;
import be.arte.walkingalarm.createalarm.TimePickerUtil;
import be.arte.walkingalarm.data.Alarm;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends ComponentActivity {

	@BindView(R.id.main_alarm)
	Button alarm;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		ButterKnife.bind(this);

		//TODO 
		//https://developer.android.com/training/wearables/overlays/screens

		alarm.setOnClickListener(v -> {
			Intent intent = new Intent(this, AlarmActivity.class);
			startActivity(intent);
		});

	}
}
