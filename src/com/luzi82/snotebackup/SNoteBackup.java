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
		StatFs statfs = sdcardStatFs();
		if (statfs == null)
			return false;
		if (statfs.getAvailableBlocks() == 0)
			return false;

		return true;
	}

	static public StatFs sdcardStatFs() {
		if (!sdcardDir().exists())
			return null;
		try {
			return new StatFs(SDCARD_PATH);
		} catch (Exception e) {
			return null;
		}
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

	static public String toByteSize(long aSize) {
		if (aSize < 1000)
			return String.format("%dB", (int) aSize);
		// TODO will have bug when aSize >= 1000 YB
		String[] mp = { "kB", "MB", "GB", "TB", "PB", "EB", "ZB", "YB" };
		int mpi = 0;
		while (aSize >= 1000000) {
			aSize /= 1000;
			++mpi;
		}
		int u = (int) (aSize / 1000);
		String uu = Integer.toString(u);
		int l = (int) (aSize % 1000);
		String ll = String.format("%03d", l);
		ll = ll.substring(0, 3 - uu.length());
		return String.format("%s.%s%s", uu, ll, mp[mpi]);
	}

}
