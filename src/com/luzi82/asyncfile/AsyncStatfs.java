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
			sendMsg(null);
			return false;
		}
		try {
			sendMsg(new StatFs(mTarget.getAbsolutePath()));
		} catch (Exception e) {
			sendMsg(null);
		}
		return false;
	}

}
