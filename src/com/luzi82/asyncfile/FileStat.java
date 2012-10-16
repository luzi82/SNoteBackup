package com.luzi82.asyncfile;

import java.io.File;
import java.util.concurrent.Executor;

import com.luzi82.async.AbstractAsyncTask;
import com.luzi82.snotebackup.T_T;

public class FileStat extends AbstractAsyncTask<FileStat.Result> {

	public static class Result {
		public long mSize = 0;
	}

	final File mTarget;

	Result mResult = new Result();
	int mCount = 0;
	int mDone = 0;

	public FileStat(Executor aExecutor, File aTarget) {
		super(aExecutor);
		mTarget = aTarget;
	}

	@Override
	protected boolean tick() {
//		T_T.v(mTarget.getAbsolutePath());
		if (!mTarget.exists()) {
			done(null);
			return false;
		}
		if (mTarget.isDirectory()) {
			File[] fileList = mTarget.listFiles();
			mCount = fileList.length;
			if (mCount == 0) {
				done(mResult);
				return false;
			}
			for (File file : fileList) {
				FileStat fs = new FileStat(mExecutor, file);
				fs.setCallback(new Callback<FileStat.Result>() {
					@Override
					public void atFinish(FileStat.Result aResult) {
						if (aResult != null) {
							mResult.mSize += aResult.mSize;
						}
						++mDone;
						if (mDone == mCount) {
							done(mResult);
							return;
						}
					}
				});
				fs.start();
			}
			return false;
		} else if (mTarget.isFile()) {
			mResult.mSize += mTarget.length();
			done(mResult);
			return false;
		}
		throw new IllegalStateException();
	}

}
