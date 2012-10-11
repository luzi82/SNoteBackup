package com.luzi82.snotebackup;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class SdCardBackupManager {
	
	private static final int BUFFER_SIZE = 512*1024;

	public static void clone(File aFrom, File aTo) throws IOException {
		if (!aFrom.exists()) {
			throw new FileNotFoundException();
		}
		if (aTo.exists()) {
			throw new IOException("File already exist");
		}
		if (aFrom.isDirectory()) {
			aTo.mkdirs();
			File[] list = aFrom.listFiles();
			for (File from : list) {
				File to = new File(aTo, from.getName());
				clone(from, to);
			}
		}else if(aFrom.isFile()){
			// TODO
		}
	}

}
