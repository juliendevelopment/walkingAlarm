package be.arte.walkingalarm.broadcastreceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import be.arte.walkingalarm.RingActivity;

public class ScreenOffReveiver extends BroadcastReceiver {

	private RingActivity ringActivity;

	public ScreenOffReveiver(RingActivity ringActivity) {
		this.ringActivity = ringActivity;
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		Log.d("ScreenOffReveiver", "onReceive");
		Intent intent1 = ringActivity.getIntent();
		context.startActivity(intent1);
	}
}
