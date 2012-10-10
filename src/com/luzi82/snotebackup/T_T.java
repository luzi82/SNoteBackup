package com.luzi82.snotebackup;

import android.util.Log;

public class T_T {
	
	// config
	
	public static final String TAG = "SNoteBackup";

	// common stuff
	
	public static void v(String msg){
		Log.v(TAG, msg);
	}
	
	public static void v(String msg,Throwable tr){
		Log.v(TAG, msg, tr);
	}
	
}
