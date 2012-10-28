package com.luzi82.async;

import java.util.concurrent.Executor;

public abstract class AbstractAsyncTask<Msg> {

	protected final Executor mExecutor;

	protected AbstractAsyncTask(Executor aExecutor) {
		mExecutor = aExecutor;
	}

	static abstract public class Callback<Msg> {
		public abstract void receiveMsg(Msg aMsg);
	}

	Callback<Msg> mCallback = new Callback<Msg>() {
		@Override
		public void receiveMsg(Msg aMsg) {
		}
	};

	public void setCallback(Callback<Msg> aCallback) {
		mCallback = aCallback;
	}

	// ///////

	public void start() {
		startTurn();
	}

	protected void startTurn() {
		mExecutor.execute(mTurnRunnable);
	}

	final private Runnable mTurnRunnable = new Runnable() {
		@Override
		public void run() {
			turn();
		}
	};

	private void turn() {
		boolean t = tick();
		if (t)
			startTurn();
	}

	protected abstract boolean tick();

	// /////////

	protected void sendMsg(Msg aMsg) {
		sendMsg(aMsg, false);
	}

	protected void sendMsg(final Msg aMsg, final boolean aTick) {
		mExecutor.execute(new Runnable() {
			@Override
			public void run() {
				mCallback.receiveMsg(aMsg);
				if (aTick) {
					startTurn();
				}
			}
		});
	}

}
