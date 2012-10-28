package com.luzi82.snotebackup;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.luzi82.async.AbstractAsyncTask;
import com.luzi82.async.AsyncTaskQueue;
import com.luzi82.snotebackup.service.BackupSDCard;
import com.luzi82.snotebackup.service.BackupSDCard.State;

public class MainService extends Service {

	Executor mExecutor = Executors.newCachedThreadPool();

	AsyncTaskQueue mTaskQueue = new AsyncTaskQueue(mExecutor);

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {

		mTaskQueue.push(new BackupSdCardTask(startId));

		return START_STICKY;
	}

	@Override
	public void onDestroy() {
		T_T.vf();
		super.onDestroy();
	}

	// private synchronized void done() {
	// stopForeground(true);
	// stopSelf();
	// }

	private class BackupSdCardTask extends AbstractAsyncTask<Void> {

		final int mStartId;
		private BackupSDCard mAsync;

		protected BackupSdCardTask(int aStartId) {
			super(MainService.this.mExecutor);
			mStartId = aStartId;
		}

		@Override
		protected boolean tick() {
			T_T.vf();
			mAsync = new BackupSDCard(mExecutor, MainService.this);
			mAsync.setCallback(new Callback<BackupSDCard.State>() {
				@Override
				public void receiveMsg(State aMsg) {
					T_T.vf();
					if (aMsg == BackupSDCard.State.CHECK) {
						T_T.v("show notification");
						Notification.Builder nb = new Notification.Builder(MainService.this);
						nb = nb.setContentTitle(getString(R.string.app_name));
						nb = nb.setContentText(getString(R.string.notification_sdcard));
						nb = nb.setOngoing(true);
						nb = nb.setSmallIcon(R.drawable.ic_launcher);
						Notification notification = nb.getNotification();
						startForeground(mStartId, notification);
					} else if (aMsg == BackupSDCard.State.SUCCESS) {
						stopForeground(true);
						sendMsg(null);
					} else if (aMsg == BackupSDCard.State.FAIL) {
						stopForeground(true);
						sendMsg(null);
					}
				}
			});
			mAsync.start();
			return false;
		}

	}

}
