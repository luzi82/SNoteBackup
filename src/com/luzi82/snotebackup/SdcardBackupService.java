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

		File from = new File("/mnt/extSdCard/luzi82/tmp");
		File to = new File("/mnt/extSdCard/luzi82/tmp2");
		Copy copy = new Copy(mExecutor, from, to);
		copy.setCallback(new Callback<Boolean>() {
			@Override
			public void atFinish(Boolean aResult) {
				T_T.v("copy atFinish " + aResult);
				stopSelf();
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

}
