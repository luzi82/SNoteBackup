package com.luzi82.asyncfile;

import java.io.File;
import java.util.concurrent.Executor;

import com.luzi82.async.AbstractAsyncTask;

public class Delete extends AbstractAsyncTask<Boolean> {

	final File mTarget;

	public Delete(Executor aExecutor, File aTarget) {
		super(aExecutor);
		mTarget = aTarget;
	}

	@Override
	protected boolean tick() {
		if (!mTarget.exists()) {
			done(true);
			return false;
		}
		if (mTarget.isDirectory()) {
			DeleteDir d = new DeleteDir(mExecutor, mTarget);
			d.setCallback(new Callback<Boolean>() {
				@Override
				public void atFinish(Boolean aResult) {
					boolean good = aResult;
					boolean t = mTarget.delete();
					done(good && t);
				}
			});
			return false;
		} else if (mTarget.isFile()) {
			boolean good = mTarget.delete();
			done(good);
			return false;
		}
		throw new IllegalStateException();
	}

}
