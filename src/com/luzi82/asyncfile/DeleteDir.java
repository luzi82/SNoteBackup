package com.luzi82.asyncfile;

import java.io.File;
import java.util.concurrent.Executor;

import com.luzi82.async.AbstractAsyncTask;
import com.luzi82.snotebackup.T_T;

public class DeleteDir extends AbstractAsyncTask<Boolean> {

	final File mTarget;

	int mTotal = 0;
	int mDone = 0;
	boolean mGood = true;

	public DeleteDir(Executor aExecutor, File aTarget) {
		super(aExecutor);
		mTarget = aTarget;
	}

	@Override
	protected boolean tick() {
		T_T.v(mTarget.getAbsolutePath());
		boolean good = true;
		good = good && mTarget.exists();
		good = good && mTarget.isDirectory();
		if (!good) {
			sendMsg(false);
			return false;
		}

		File[] fileList = mTarget.listFiles();
		mTotal = fileList.length;
		if (mTotal == 0) {
			sendMsg(true);
			return false;
		}

		Callback<Boolean> cb = new Callback<Boolean>() {
			@Override
			public void receiveMsg(Boolean aResult) {
				synchronized (DeleteDir.this) {
					mGood = mGood && aResult;
					++mDone;
					if (mDone == mTotal) {
						sendMsg(mGood);
					}
				}
			}
		};

		for (File file : fileList) {
			Delete d = new Delete(mExecutor, file);
			d.setCallback(cb);
			d.start();
		}

		return false;
	}

}
