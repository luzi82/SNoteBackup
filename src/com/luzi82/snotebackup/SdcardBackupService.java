package com.luzi82.snotebackup;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class SdcardBackupService extends Service {

	Executor mExecutor = Executors.newCachedThreadPool();

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public synchronized int onStartCommand(Intent intent, int flags, int startId) {
		return START_STICKY;
	}

}
