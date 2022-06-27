package be.arte.walkingalarm.broadcastreceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import androidx.activity.ComponentActivity;
import be.arte.walkingalarm.WalkingRingActivity;

public class ScreenOffReveiver extends BroadcastReceiver {

	private ComponentActivity ringActivity;

	public ScreenOffReveiver(ComponentActivity ringActivity) {
		this.ringActivity = ringActivity;
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		Log.d("ScreenOffReveiver", "onReceive");
		Intent intent1 = ringActivity.getIntent();
		context.startActivity(intent1);
	}
}
