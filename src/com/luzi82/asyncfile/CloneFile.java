package com.luzi82.asyncfile;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.Executor;

import com.luzi82.async.AbstractAsyncTask;

public class CloneFile extends AbstractAsyncTask<Boolean> {

	final File mFrom;
	final File mTo;

	int state = 0;

	InputStream is;
	OutputStream os;

	private static final int BUF_SIZE = 128 * 1024;
	private static final int LOOP = 8;

	protected CloneFile(Executor aExecutor, File aFrom, File aTo) {
		super(aExecutor);
		mFrom = aFrom;
		mTo = aTo;
	}

	@Override
	protected boolean tick() {
		try {
			int statev = 0;
			if (state == (statev++)) {
				boolean good = true;
				good = good && mFrom.exists();
				good = good && mFrom.isFile();
				good = good && (!mTo.exists());
				if (!good) {
					done(false);
					return false;
				}

				is = new FileInputStream(mFrom);
				is = new BufferedInputStream(is);
				os = new FileOutputStream(mTo);
				os = new BufferedOutputStream(os);

				++state;
				return true;
			} else if (state == (statev++)) {
				boolean done = false;
				byte[] buf = new byte[BUF_SIZE];
				for (int i = 0; i < LOOP; ++i) {
					int len = is.read(buf);
					if (len < 0) {
						done = true;
						break;
					}
					os.write(buf, 0, len);
				}
				if (done) {
					is.close();
					++state;
				}
				return true;
			} else if (state == (statev++)) {
				os.flush();
				os.close();
				done(true);
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (is != null) {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if (os != null) {
			try {
				os.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		done(false);
		return false;
	}

}
