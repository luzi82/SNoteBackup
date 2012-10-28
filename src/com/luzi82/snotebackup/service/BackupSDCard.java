package com.luzi82.snotebackup.service;

import java.io.File;
import java.util.concurrent.Executor;

import android.content.Context;

import com.luzi82.async.AbstractAsyncTask;
import com.luzi82.asyncfile.Copy;
import com.luzi82.snotebackup.SNoteBackup;
import com.luzi82.snotebackup.T_T;

public class BackupSDCard extends AbstractAsyncTask<BackupSDCard.State> {

	public enum State{
		INIT,CHECK,BACKUP,SUCCESS,FAIL
	}
	
	private State mState=State.INIT;
	final private Context mContext;

	public BackupSDCard(Executor aExecutor,Context aContext) {
		super(aExecutor);
		mContext=aContext;
	}

	@Override
	protected boolean tick() {
		T_T.vf();
		if(mState==State.INIT){
			mState = State.CHECK;
			sendMsg(mState, true);
			return false;
		}else if(mState==State.CHECK){
			if (!SNoteBackup.sdcardExist()) {
				mState = State.FAIL;
				return false;
			}
			
			mState = State.BACKUP;
			sendMsg(mState, true);
			return false;
		}else if(mState==State.BACKUP){
			long time = System.currentTimeMillis();
			File sdcardDir = SNoteBackup.sdcardAppDir(mContext);
			sdcardDir.mkdirs();

			File from = new File(SNoteBackup.SNOTE_PATH);
			File to = new File(sdcardDir, Long.toString(time));
			Copy copy = new Copy(mExecutor, from, to);
			copy.setCallback(new Callback<Boolean>() {
				@Override
				public void receiveMsg(Boolean aResult) {
					mState = aResult?State.SUCCESS:State.FAIL;
					sendMsg(mState);
				}
			});
			copy.start();
			return false;
		}
		return false;
	}

}
