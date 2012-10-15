package com.luzi82.snotebackup;

import java.io.File;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.luzi82.async.AbstractAsyncTask.Callback;
import com.luzi82.asyncfile.Copy;

public class MainService extends Service {

	Executor mExecutor = Executors.newCachedThreadPool();
	boolean busy = false;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public synchronized int onStartCommand(Intent intent, int flags, int startId) {
		if (busy)
			return START_STICKY;

		busy = true;

		if (!SNoteBackup.sdcardExist()) {
			mExecutor.execute(mDoneRunnable);
			return START_STICKY;
		}

		// show notification
		T_T.v("show notification");
		Notification.Builder nb = new Notification.Builder(this);
		// nb = nb.setTicker("ticker");
		nb = nb.setContentTitle(getString(R.string.app_name));
		nb = nb.setContentText(getString(R.string.notification_sdcard));
		nb = nb.setOngoing(true);
		nb = nb.setSmallIcon(R.drawable.ic_launcher);
		// nb = nb.setLargeIcon(Bitmap.createBitmap(32, 32,
		// Bitmap.Config.ARGB_8888));
		Notification notification = nb.getNotification();
		startForeground(R.string.app_name, notification);

		// fire backup
		long time = System.currentTimeMillis();
		File sdcardDir = SNoteBackup.sdcardAppDir(this);
		sdcardDir.mkdirs();

		File from = new File(SNoteBackup.SNOTE_PATH);
		File to = new File(sdcardDir, Long.toString(time));
		Copy copy = new Copy(mExecutor, from, to);
		copy.setCallback(new Callback<Boolean>() {
			@Override
			public void atFinish(Boolean aResult) {
				synchronized (MainService.this) {
					T_T.v("copy atFinish " + aResult);
					done();
				}
			}
		});
		copy.start();

		return START_STICKY;
	}

	@Override
	public void onDestroy() {
		T_T.vf();
		super.onDestroy();
	}

	final private Runnable mDoneRunnable = new Runnable() {
		@Override
		public void run() {
			done();
		}
	};

	private synchronized void done() {
		stopForeground(true);
		stopSelf();
		busy = false;
	}

}
