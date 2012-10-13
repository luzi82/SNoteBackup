package com.luzi82.snotebackup;

import java.io.File;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceFragment;

import com.luzi82.async.AbstractAsyncTask.Callback;
import com.luzi82.asyncfile.Copy;
import com.luzi82.asyncfile.Delete;

public class HomeFragment extends PreferenceFragment {

	Executor exe = Executors.newCachedThreadPool();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		addPreferencesFromResource(R.xml.home);

		Preference p;

//		p = findPreference("test_copy");
//		p.setOnPreferenceClickListener(new OnPreferenceClickListener() {
//			@Override
//			public boolean onPreferenceClick(Preference preference) {
//				copy();
//				return true;
//			}
//		});
//
//		p = findPreference("test_delete");
//		p.setOnPreferenceClickListener(new OnPreferenceClickListener() {
//			@Override
//			public boolean onPreferenceClick(Preference preference) {
//				delete();
//				return true;
//			}
//		});

		p = findPreference("preference_backup_now");
		p.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			@Override
			public boolean onPreferenceClick(Preference preference) {
				Intent i = new Intent(getActivity(), SdcardBackupService.class);
				getActivity().startService(i);
				return true;
			}
		});
	}

//	void copy() {
//		File from = new File("/mnt/extSdCard/luzi82/tmp");
//		File to = new File("/mnt/extSdCard/luzi82/tmp2");
//		Copy c = new Copy(exe, from, to);
//		c.setCallback(new Callback<Boolean>() {
//			@Override
//			public void atFinish(Boolean aResult) {
//				T_T.v("copy atFinish " + aResult);
//			}
//		});
//		c.start();
//	}
//
//	void delete() {
//		T_T.vf();
//		File tar = new File("/mnt/extSdCard/luzi82/tmp2");
//		Delete d = new Delete(exe, tar);
//		d.setCallback(new Callback<Boolean>() {
//			@Override
//			public void atFinish(Boolean aResult) {
//				T_T.v("delete atFinish " + aResult);
//			}
//		});
//		d.start();
//	}

}
