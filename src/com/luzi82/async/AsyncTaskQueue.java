package com.luzi82.async;

import java.util.LinkedList;
import java.util.concurrent.Executor;

import com.luzi82.snotebackup.T_T;

public class AsyncTaskQueue extends AbstractAsyncTask<Void> {

	LinkedList<AbstractAsyncTask<Void>> mQueue = new LinkedList<AbstractAsyncTask<Void>>();
	boolean mRunning = false;

	public AsyncTaskQueue(Executor aExecutor) {
		super(aExecutor);
	}

	public synchronized void push(AbstractAsyncTask<Void> aTask) {
		mQueue.addLast(aTask);
		if (!mRunning) {
			startTurn();
		}
		mRunning = true;
	}

	@Override
	protected synchronized boolean tick() {
		T_T.vf();
		if (!mQueue.isEmpty()) {
			AbstractAsyncTask<Void> task = mQueue.removeFirst();
			task.setCallback(new Callback<Void>() {
				@Override
				public void receiveMsg(Void aMsg) {
					startTurn();
				}
			});
			task.start();
		} else {
			mRunning = false;
			sendMsg(null);
		}
		return false;
	}

}
