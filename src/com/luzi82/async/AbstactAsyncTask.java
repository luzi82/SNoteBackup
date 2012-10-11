package com.luzi82.async;

public abstract class AbstactAsyncTask<Result> {

	AbstractAsyncCore mACore;

	protected AbstactAsyncTask(AbstractAsyncCore aACore) {
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
		public void acFinish() {
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

	Result mResult;

	protected void done(Result aResult) {
		mResult = aResult;
		mACore.start(mDoneCallback);
	}

	final private AbstractAsyncCore.Callback mDoneCallback = new AbstractAsyncCore.Callback() {
		@Override
		public void acFinish() {
			mCallback.atFinish(mResult);
		}
	};

}
