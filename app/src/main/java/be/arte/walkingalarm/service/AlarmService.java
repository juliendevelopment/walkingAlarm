package be.arte.walkingalarm.service;


import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.Vibrator;
import android.util.Log;
import android.view.WindowManager;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import be.arte.walkingalarm.App;
import be.arte.walkingalarm.R;
import be.arte.walkingalarm.RingActivity;
import be.arte.walkingalarm.broadcastreceiver.AlarmBroadcastReceiver;

public class AlarmService extends Service {
    //private MediaPlayer mediaPlayer; //TODO disable media player
    private Vibrator vibrator;

	private PowerManager.WakeLock wakeLock = null;

	private static final String LOCK_NAME = AlarmService.class.getName()
											+ ".Lock";
	private static volatile PowerManager.WakeLock lockStatic = null; // notice static

	synchronized private static PowerManager.WakeLock getLock(Context context) {
		if (lockStatic == null) {
			PowerManager mgr = (PowerManager) context
					.getSystemService(Context.POWER_SERVICE);
			lockStatic = mgr.newWakeLock(PowerManager.FULL_WAKE_LOCK,
										 LOCK_NAME);
			lockStatic.setReferenceCounted(true);
		}
		return (lockStatic);
	}

    @Override
    public void onCreate() {
        super.onCreate();
		Log.i("AlarmService","onCreate()" );
        //mediaPlayer = MediaPlayer.create(this, R.raw.alarm);
        //mediaPlayer.setLooping(true);

		//PowerManager pm = (PowerManager)getSystemService(Context.POWER_SERVICE);
		//wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,this.getPackageName());
		//wakeLock.acquire();

		vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
		Log.i("AlarmService","onStartCommand()" );
        Intent notificationIntent = new Intent(this, RingActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

        String alarmTitle = String.format("%s Alarm", intent.getStringExtra(AlarmBroadcastReceiver.TITLE));

        Notification notification = new NotificationCompat.Builder(this, App.CHANNEL_ID)
                .setContentTitle(alarmTitle)
                .setContentText("Ring Ring .. Ring Ring")
                .setSmallIcon(R.drawable.ic_alarm_black_24dp)
                .setContentIntent(pendingIntent)
                .build();

        //mediaPlayer.start();

        startForeground(1, notification);


		int timeout = 15 * 60 * 1000; // 15 min
		getLock(getApplicationContext()).acquire(timeout);


		long[] pattern = { 0, 500, 1000 }; //TODO add better pattern
		vibrator.vibrate(pattern, 0);

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
		Log.i("AlarmService","onDestroy()" );
        super.onDestroy();
        //wakeLock.release();
		getLock(getApplicationContext()).release();
        //mediaPlayer.stop();
        vibrator.cancel();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
