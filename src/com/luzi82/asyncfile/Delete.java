package com.luzi82.asyncfile;

import java.io.File;
import java.util.concurrent.Executor;

import com.luzi82.async.AbstractAsyncTask;
import com.luzi82.snotebackup.T_T;

public class Delete extends AbstractAsyncTask<Boolean> {

	final File mTarget;

	public Delete(Executor aExecutor, File aTarget) {
		super(aExecutor);
		mTarget = aTarget;
	}

	@Override
	protected boolean tick() {
		T_T.v(mTarget.getAbsolutePath());
		if (!mTarget.exists()) {
			sendMsg(true);
			return false;
		}
		if (mTarget.isDirectory()) {
			DeleteDir d = new DeleteDir(mExecutor, mTarget);
			d.setCallback(new Callback<Boolean>() {
				@Override
				public void receiveMsg(Boolean aResult) {
					boolean good = aResult;
					boolean t = mTarget.delete();
					sendMsg(good && t);
				}
			});
			d.start();
			return false;
		} else if (mTarget.isFile()) {
			boolean good = mTarget.delete();
			sendMsg(good);
			return false;
		}
		throw new IllegalStateException();
	}

}
