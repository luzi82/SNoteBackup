package com.luzi82.asyncfile;

import java.io.File;
import java.util.concurrent.Executor;

import com.luzi82.async.AbstractAsyncTask;

public class CopyDir extends AbstractAsyncTask<Boolean> {

	final File mFrom;
	final File mTo;

	int mTotal = 0;
	int mDone = 0;
	boolean mGood = true;

	public CopyDir(Executor aExecutor, File aFrom, File aTo) {
		super(aExecutor);
		mFrom = aFrom;
		mTo = aTo;
	}

	@Override
	protected boolean tick() {
		boolean good = true;
		good = good && (mFrom.exists());
		good = good && (mFrom.isDirectory());
		good = good && (mTo.exists());
		good = good && (mTo.isDirectory());
		if (!good) {
			sendMsg(false);
			return false;
		}
		File[] fileList = mFrom.listFiles();
		mTotal = fileList.length;
		if (mTotal == 0) {
			sendMsg(true);
			return false;
		}

		Callback<Boolean> cb = new Callback<Boolean>() {
			@Override
			public void receiveMsg(Boolean aResult) {
				synchronized (CopyDir.this) {
					mGood = mGood && aResult;
					++mDone;
					if (mDone == mTotal) {
						sendMsg(mGood);
					}
				}
			}
		};

		for (File file : fileList) {
			File to = new File(mTo, file.getName());
			Copy c = new Copy(mExecutor, file, to);
			c.setCallback(cb);
			c.start();
		}
		return false;
	}

}
