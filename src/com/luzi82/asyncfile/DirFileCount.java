package com.luzi82.asyncfile;

import java.io.File;
import java.util.concurrent.Executor;

import com.luzi82.async.AbstractAsyncTask;

public class DirFileCount extends AbstractAsyncTask<Integer> {

	final File mTarget;

	public DirFileCount(Executor aExecutor, File aTarget) {
		super(aExecutor);
		mTarget = aTarget;
	}

	@Override
	protected boolean tick() {
		boolean good = true;
		good = good && mTarget.exists();
		good = good && mTarget.isDirectory();
		if (!good) {
			sendMsg(-1);
			return false;
		}
		sendMsg(mTarget.list().length);
		return false;
	}

}
