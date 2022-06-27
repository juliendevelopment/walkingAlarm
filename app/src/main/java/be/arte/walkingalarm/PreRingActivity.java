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
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.ComponentActivity;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import be.arte.walkingalarm.broadcastreceiver.ScreenOffReveiver;
import be.arte.walkingalarm.createalarm.CreateAlarmViewModel;
import butterknife.BindView;
import butterknife.ButterKnife;

public class PreRingActivity extends ComponentActivity {
	@BindView(R.id.activity_pre_ring_dismiss)
	Button dismiss;

	private boolean isDismissed = false;

	private Vibrator vibrator;


	PowerManager.WakeLock wakeLock;

	ScreenOffReveiver screenOffReveiver;

	private CreateAlarmViewModel createAlarmViewModel;


	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		Log.d("PreRingActivity", "onCreate()");

		super.onCreate(savedInstanceState);
		setContentView(R.layout.pre_activity_ring);

		ButterKnife.bind(this);

		vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);


		dismiss.setOnClickListener(v -> {
			dismissAlarm();
		});


		this.createAlarmViewModel = new ViewModelProvider(this).get(CreateAlarmViewModel.class);

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

	}


	private void dismissAlarm() {
		Log.d("RingActivity", "dismissAlarm()");
		isDismissed = true;
		vibrator.cancel();
		unregisterReceiver(screenOffReveiver);
		wakeLock.release();
		finish();
	}

}
