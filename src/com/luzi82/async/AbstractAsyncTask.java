package com.luzi82.async;

import java.util.concurrent.Executor;

public abstract class AbstractAsyncTask<Result> {

	protected final Executor mExecutor;

	protected AbstractAsyncTask(Executor aACore) {
		mExecutor = aACore;
	}

	static abstract public class Callback<Result> {
		public abstract void atFinish(Result aResult);
	}

	Callback<Result> mCallback = new Callback<Result>() {
		@Override
		public void atFinish(Result aResult) {
		}
	};

	public void setCallback(Callback<Result> aCallback) {
		mCallback = aCallback;
	}

	// ///////

	public void start() {
		startTurn();
	}

	private void startTurn() {
		mExecutor.execute(mStartTurnCallback);
	}

	final private Runnable mStartTurnCallback = new Runnable() {
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

	protected void done(final Result aResult) {
		mExecutor.execute(new Runnable() {
			@Override
			public void run() {
				mCallback.atFinish(aResult);
			}
		});
	}

}
