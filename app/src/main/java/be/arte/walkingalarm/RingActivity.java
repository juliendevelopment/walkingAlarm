package be.arte.walkingalarm;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.nfc.NfcManager;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.PowerManager;
import android.os.Vibrator;
import android.support.wearable.activity.WearableActivity;
import android.util.Base64;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import be.arte.walkingalarm.broadcastreceiver.ScreenOffReveiver;
import butterknife.BindView;
import butterknife.ButterKnife;

public class RingActivity extends WearableActivity implements SensorEventListener{
	public static final String NFC_TAG_ID = "julien";
	@BindView(R.id.activity_ring_dismiss)
	Button dismiss;
	//@BindView(R.id.activity_ring_clock) ImageView clock;
	@BindView(R.id.activity_ring_step)
	TextView stepsDisplay;

	private SensorManager sensorManager;
	private Sensor sensor;

	private NfcAdapter nfcAdapter;

	private int currentStep;
	private final int MAX_STEP = 50;

	private boolean isDismissed = false;

	private Vibrator vibrator;


	PowerManager.WakeLock wakeLock;

	ScreenOffReveiver screenOffReveiver;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		Log.d("RingActivity", "onCreate()");

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ring);

		sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		sensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);
		sensorManager.registerListener(this, sensor, Sensor.TYPE_STEP_COUNTER);
		
		vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

		NfcManager nfcManager = (NfcManager) getSystemService(Context.NFC_SERVICE);
		nfcAdapter = nfcManager.getDefaultAdapter();

		ButterKnife.bind(this);

		dismiss.setOnClickListener(v -> {
			Toast.makeText(getApplicationContext(), "walk lazy !!! ", Toast.LENGTH_LONG).show();

			//FIXME TEST
			//Toast.makeText(getApplicationContext(), "should be disable test only ", Toast.LENGTH_LONG).show();
			//dismissAlarm();
		});


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
			setAmbientEnabled();
			long[] pattern = { 0, 500, 1000 }; //TODO add better pattern
			vibrator.vibrate(pattern, 0);
		}
	}

	@Override
	protected void onPause(){
		Log.d("RingActivity", "onPause()");
		super.onPause();

		//if(!isDismissed){
		//	Log.d("RingActivity", "Preventing leaving");
		//	startActivity(this.getIntent());
		//}
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

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		Log.d("RingActivity", "onNewIntent()");

		if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(intent.getAction())) {
			Log.d("RingActivity", "Action = NfcAdapter");
			Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
			String tagUid = Base64.encodeToString(tag.getId(), 0);
			Toast.makeText(getApplicationContext(), tagUid, Toast.LENGTH_LONG).show();

			Parcelable[] ndefMessageArray = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
			if (ndefMessageArray != null) {
				NdefMessage ndefMessage = (NdefMessage) ndefMessageArray[0];
				if (ndefMessage != null) {
					String msg = new String(ndefMessage.getRecords()[0].getPayload());
					if (msg.equals(NFC_TAG_ID)) {
						dismissAlarm();
					}
				}
			}
		}
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


}
