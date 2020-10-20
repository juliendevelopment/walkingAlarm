package be.arte.walkingalarm.broadcastreceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;
import be.arte.walkingalarm.service.AlarmService;

public class AlarmBroadcastReceiver extends BroadcastReceiver {
    public static final String MONDAY = "MONDAY";
    public static final String TUESDAY = "TUESDAY";
    public static final String WEDNESDAY = "WEDNESDAY";
    public static final String THURSDAY = "THURSDAY";
    public static final String FRIDAY = "FRIDAY";
    public static final String SATURDAY = "SATURDAY";
    public static final String SUNDAY = "SUNDAY";
    public static final String RECURRING = "RECURRING";
    public static final String TITLE = "TITLE";

    @Override
    public void onReceive(Context context, Intent intent) {
		Log.i("TAG", "onReceive receiver");
		Toast.makeText(context, "onReceive receiver", Toast.LENGTH_LONG).show();
            String toastText = String.format("Alarm Received");
            Toast.makeText(context, toastText, Toast.LENGTH_SHORT).show();
            startAlarmService(context, intent);
    }

	private void startAlarmService(Context context, Intent intent) {

		Intent intentService = new Intent(context, AlarmService.class);
		intentService.putExtra(TITLE, intent.getStringExtra(TITLE));
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
			context.startForegroundService(intentService);
		} else {
			context.startService(intentService);
		}
	}
}
