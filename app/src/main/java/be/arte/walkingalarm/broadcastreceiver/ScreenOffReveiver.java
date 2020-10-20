package be.arte.walkingalarm.broadcastreceiver;

import android.app.ActivityManager;
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

		////Intent i = new Intent(context, RingActivity.class);
		////context.startActivity(i);
		//
		//for (ActivityManager.AppTask appTask : ((ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE)).getAppTasks()) {
		//	appTask.getTaskInfo().baseIntent.
		//	if (appTask.getClass().equals(RingActivity.class)) {
		//		appTask.finishAndRemoveTask();
		//	}
		//}
		//((ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE)).moveTaskToFront(ringActivity, 0);
	}
}
