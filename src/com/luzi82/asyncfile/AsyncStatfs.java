package com.luzi82.asyncfile;

import java.io.File;
import java.util.concurrent.Executor;

import android.os.StatFs;

import com.luzi82.async.AbstractAsyncTask;

public class AsyncStatfs extends AbstractAsyncTask<StatFs> {

	final File mTarget;

	public AsyncStatfs(Executor aExecutor, File aTarget) {
		super(aExecutor);
		mTarget = aTarget;
	}

	@Override
	protected boolean tick() {
		boolean good = true;
		good = good && mTarget.exists();
		if (!good) {
			done(null);
			return false;
		}
		try {
			done(new StatFs(mTarget.getAbsolutePath()));
		} catch (Exception e) {
			done(null);
		}
		return false;
	}

}
