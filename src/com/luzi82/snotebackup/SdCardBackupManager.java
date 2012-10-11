package com.luzi82.snotebackup;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class SdCardBackupManager {

	private static final int BUFFER_SIZE = 512 * 1024;

	public static void clone(File aFrom, File aTo) throws IOException {
		if (!aFrom.exists())
			throw new FileNotFoundException();
		if (aTo.exists())
			throw new IOException("File already exist");
		if (aFrom.isDirectory()) {
			aTo.mkdirs();
			cloneDir(aFrom, aTo);
		} else if (aFrom.isFile()) {
			byte[] buf = new byte[BUFFER_SIZE];
			InputStream is = new FileInputStream(aFrom);
			is = new BufferedInputStream(is);
			OutputStream os = new FileOutputStream(aTo);
			os = new BufferedOutputStream(os);
			while (true) {
				int len = is.read(buf);
				if (len < 0)
					break;
				os.write(buf, 0, len);
			}
			is.close();
			os.flush();
			os.close();
		}
	}

	public static void cloneDir(File aFrom, File aTo) throws IOException {
		if (!aFrom.exists())
			throw new FileNotFoundException();
		if (!aTo.exists())
			throw new FileNotFoundException();
		if (!aFrom.isDirectory())
			throw new IOException("Not directory");
		if (!aTo.isDirectory())
			throw new IOException("Not directory");
		File[] list = aFrom.listFiles();
		for (File from : list) {
			File to = new File(aTo, from.getName());
			clone(from, to);
		}
	}

	public static boolean rm(File aFile) {
		boolean t;
		if (!aFile.exists())
			return true;
		if (aFile.isDirectory()) {
			t = rmDir(aFile);
			if (!t)
				return false;
		}
		t = aFile.delete();
		if (!t)
			return false;
		return true;
	}

	private static boolean rmDir(File aFile) {
		boolean t;
		if (!aFile.exists())
			return false;
		if (!aFile.isDirectory())
			return false;
		File[] list = aFile.listFiles();
		for (File f : list) {
			t = rm(f);
			if (!t)
				return false;
		}
		return true;
	}

}
