package be.arte.walkingalarm;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.Vibrator;
import android.support.wearable.activity.WearableActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.ComponentActivity;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import be.arte.walkingalarm.broadcastreceiver.ScreenOffReveiver;
import be.arte.walkingalarm.createalarm.CreateAlarmViewModel;
import butterknife.BindView;
import butterknife.ButterKnife;

public class RingActivity extends ComponentActivity implements SensorEventListener{
	@BindView(R.id.activity_ring_dismiss)
	Button dismiss;
	//@BindView(R.id.activity_ring_clock) ImageView clock;
	@BindView(R.id.activity_ring_step)
	TextView stepsDisplay;

	private SensorManager sensorManager;
	private Sensor sensor;

	private int currentStep;
	private int steps;

	private boolean isDismissed = false;

	private Vibrator vibrator;


	PowerManager.WakeLock wakeLock;

	ScreenOffReveiver screenOffReveiver;

	private CreateAlarmViewModel createAlarmViewModel;


	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		Log.d("RingActivity", "onCreate()");

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ring);

		ButterKnife.bind(this);

		stepsDisplay.setText("move !!!!");

		sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		sensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);
		sensorManager.registerListener(this, sensor, Sensor.TYPE_STEP_COUNTER);

		vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);


		dismiss.setOnClickListener(v -> {
			Toast.makeText(getApplicationContext(), "walk lazy !!! ", Toast.LENGTH_LONG).show();
			//dismissAlarm();
		});


		this.createAlarmViewModel = new ViewModelProvider(this).get(CreateAlarmViewModel.class);
		steps = createAlarmViewModel.getTheAlarm().getSteps();

		PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
		wakeLock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK, this.getClass().getCanonicalName());
		wakeLock.acquire();

		screenOffReveiver = new ScreenOffReveiver(this);
		registerReceiver(screenOffReveiver, new IntentFilter(Intent.ACTION_SCREEN_OFF));
	}

	@Override
	protected void onResume() {
		Log.d("RingActivity", "onResume()");
		super.onResume();

		if(!isDismissed){
			Log.d("RingActivity", "onResume");
			long[] pattern = { 0, 500, 1000 }; //TODO add better pattern
			vibrator.vibrate(pattern, 0);
		}
	}

	@Override
	protected void onUserLeaveHint() {
		Log.d("RingActivity", "onUserLeaveHint()");

		//if(!isDismissed){
		//	Log.d("RingActivity", "Preventing leaving");
		//	startActivity(this.getIntent());
		//}
	}

	@Override
	protected void onPause(){
		super.onPause();
		Log.d("RingActivity", "onPause()");

		//if(!isDismissed){
		//	Log.d("RingActivity", "Preventing leaving");
		//	startActivity(this.getIntent());
		//}
	}

	@Override
	protected void onStop(){
		super.onStop();
		Log.d("RingActivity", "onStop()");

		//if(!isDismissed){
		//	Log.d("RingActivity", "Preventing leaving");
		//	startActivity(this.getIntent());
		//}
	}

	@Override
	protected void onDestroy() {
		Log.d("RingActivity", "onDestroy()");
		super.onDestroy();

		//if(!isDismissed){
		//	Log.d("RingActivity", "Preventing leaving");
		//	startActivity(this.getIntent());
		//}
	}


	private void dismissAlarm() {
		Log.d("RingActivity", "dismissAlarm()");
		isDismissed = true;
		vibrator.cancel();
		unregisterReceiver(screenOffReveiver);
		sensorManager.unregisterListener(this);
		wakeLock.release();
		finish();
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
		stepsDisplay.setText(currentStep + " / " + steps);

		if (currentStep > steps) {
			dismissAlarm();
		}
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {

	}
}
