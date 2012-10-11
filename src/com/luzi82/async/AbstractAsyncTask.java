package com.luzi82.async;

public abstract class AbstractAsyncTask<Result> {

	AbstractAsyncCore mACore;

	protected AbstractAsyncTask(AbstractAsyncCore aACore) {
		mACore = aACore;
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
		mACore.start(mStartTurnCallback);
	}

	final private AbstractAsyncCore.Callback mStartTurnCallback = new AbstractAsyncCore.Callback() {
		@Override
		public void acFire() {
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
		mACore.start(new AbstractAsyncCore.Callback() {
			@Override
			public void acFire() {
				mCallback.atFinish(aResult);
			}
		});
	}

}
