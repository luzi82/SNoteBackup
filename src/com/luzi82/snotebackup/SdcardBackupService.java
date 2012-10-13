package com.luzi82.snotebackup;

import java.io.File;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.luzi82.async.AbstractAsyncTask.Callback;
import com.luzi82.asyncfile.Copy;

public class SdcardBackupService extends Service {

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

		long time = System.currentTimeMillis();
		File sdcardDir = SNoteBackup.sdcardAppDir(this);
		sdcardDir.mkdirs();

		File from = new File(SNoteBackup.SNOTE_PATH);
		File to = new File(sdcardDir, Long.toString(time));
		Copy copy = new Copy(mExecutor, from, to);
		copy.setCallback(new Callback<Boolean>() {
			@Override
			public void atFinish(Boolean aResult) {
				synchronized (SdcardBackupService.this) {
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
		stopSelf();
		busy = false;
	}

}
