package be.arte.walkingalarm;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import be.arte.walkingalarm.service.AlarmService;
import butterknife.BindView;
import butterknife.ButterKnife;

public class RingActivity extends AppCompatActivity implements SensorEventListener {
	@BindView(R.id.activity_ring_dismiss)
	Button dismiss;
	//@BindView(R.id.activity_ring_clock) ImageView clock;
	@BindView(R.id.activity_ring_step)
	TextView stepsDisplay;

	private SensorManager sensorManager;
	private Sensor sensor;

	private int currentStep;
	private final int MAX_STEP = 20;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ring_activity_2);

		sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		sensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);

		ButterKnife.bind(this);

		dismiss.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				Toast.makeText(getApplicationContext(), "walk lazy !!! ", Toast.LENGTH_LONG).show();
				//dismissAlarm();
			}
		});

		animateClock();
	}

	private void dismissAlarm() {
		Intent intentService = new Intent(getApplicationContext(), AlarmService.class);
		getApplicationContext().stopService(intentService);
		finish();
	}

	private void animateClock() {
		//ObjectAnimator rotateAnimation = ObjectAnimator.ofFloat(clock, "rotation", 0f, 20f, 0f, -20f, 0f);
		//rotateAnimation.setRepeatCount(ValueAnimator.INFINITE);
		//rotateAnimation.setDuration(800);
		//rotateAnimation.start();
	}

	@Override
	protected void onResume() {
		super.onResume();
		sensorManager.registerListener(this, sensor, Sensor.TYPE_STEP_COUNTER);
	}

	@Override
	protected void onPause() {
		super.onPause();
		sensorManager.unregisterListener(this);
	}

	@Override
	public void onSensorChanged(SensorEvent event) {

		float[] values = event.values;
		int value = -1;

		if (values.length > 0) {
			value = (int) values[0];
			currentStep += value;
		}
		// For test only. Only allowed value is 1.0 i.e. for step taken
		stepsDisplay.setText(currentStep + " / " + MAX_STEP);

		if (currentStep > MAX_STEP) {
			dismissAlarm();
		}
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {

	}
}
