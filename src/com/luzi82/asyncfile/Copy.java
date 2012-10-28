package com.luzi82.asyncfile;

import java.io.File;
import java.util.concurrent.Executor;

import com.luzi82.async.AbstractAsyncTask;

public class Copy extends AbstractAsyncTask<Boolean> {

	final File mFrom;
	final File mTo;

	public Copy(Executor aExecutor, File aFrom, File aTo) {
		super(aExecutor);
		mFrom = aFrom;
		mTo = aTo;
	}

	@Override
	protected boolean tick() {
		boolean good = true;
		good = good && mFrom.exists();
		good = good && (!mTo.exists());
		if (!good) {
			sendMsg(false);
			return false;
		}
		if (mFrom.isDirectory()) {
			mTo.mkdirs();
			CopyDir c = new CopyDir(mExecutor, mFrom, mTo);
			c.setCallback(new Callback<Boolean>() {
				@Override
				public void receiveMsg(Boolean aResult) {
					sendMsg(aResult);
				}
			});
			c.start();
			return false;
		} else if (mFrom.isFile()) {
			CopyFile c = new CopyFile(mExecutor, mFrom, mTo);
			c.setCallback(new Callback<Boolean>() {
				@Override
				public void receiveMsg(Boolean aResult) {
					sendMsg(aResult);
				}
			});
			c.start();
			return false;
		}
		sendMsg(false);
		return false;
	}

}
