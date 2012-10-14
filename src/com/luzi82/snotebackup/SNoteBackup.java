package com.luzi82.snotebackup;

import java.io.File;

import android.content.Context;
import android.os.StatFs;

public class SNoteBackup {
	
	enum MenuItem {
		HOME, SDCARD, BACKUP, ABOUT,
	}

	static public String SNOTE_PATH = "/mnt/sdcard/S Note";
	static public String SDCARD_PATH = "/mnt/extSdCard";

	static public boolean sdcardExist() {
		if (!sdcardDir().exists()) {
			return false;
		}

		StatFs statfs = new StatFs(SDCARD_PATH);
		if (statfs.getAvailableBlocks() == 0) {
			return false;
		}

		return true;
	}

	static public File sdcardDir() {
		// return Environment.getExternalStorageDirectory();
		return new File(SDCARD_PATH);
	}

	static public File sdcardAppDir(Context aContext) {
		String pn = aContext.getPackageName();

		File f = new File(sdcardDir(), "data");
		f = new File(f, pn);

		return f;
	}

}
