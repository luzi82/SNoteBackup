package com.luzi82.snotebackup;

import android.util.Log;

public class T_T {

	// config

	public static final String TAG = "SNoteBackup";

	// common stuff

	// public static void v(String msg){
	// Log.v(TAG, msg);
	// }
	//
	// public static void v(String msg,Throwable tr){
	// Log.v(TAG, msg, tr);
	// }

	public static void vf() {
		StackTraceElement[] stev = Thread.currentThread().getStackTrace();
		StackTraceElement ste = stev[3];
		String cn = ste.getClassName();
		int firstDot = cn.lastIndexOf(".");
		cn = cn.substring(firstDot + 1);
		Log.v(cn, ste.getMethodName() + " " + ste.getLineNumber());
	}

	public static void v(String aMsg) {
		StackTraceElement[] stev = Thread.currentThread().getStackTrace();
		StackTraceElement ste = stev[3];
		String cn = ste.getClassName();
		int firstDot = cn.lastIndexOf(".");
		cn = cn.substring(firstDot + 1);
		Log.v(cn, ste.getMethodName() + " " + ste.getLineNumber() + " " + aMsg);
	}

	public static void v(String aMsg, Throwable aTr) {
		StackTraceElement[] stev = Thread.currentThread().getStackTrace();
		StackTraceElement ste = stev[3];
		String cn = ste.getClassName();
		int firstDot = cn.lastIndexOf(".");
		cn = cn.substring(firstDot + 1);
		Log.v(cn, ste.getMethodName() + " " + ste.getLineNumber() + " " + aMsg, aTr);
	}

}
