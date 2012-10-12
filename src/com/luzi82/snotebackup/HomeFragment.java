package com.luzi82.snotebackup;

import java.io.File;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceFragment;

import com.luzi82.async.AbstractAsyncTask.Callback;
import com.luzi82.asyncfile.Clone;
import com.luzi82.asyncfile.Delete;

public class HomeFragment extends PreferenceFragment {

	Executor exe = Executors.newCachedThreadPool();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		addPreferencesFromResource(R.xml.home);

		Preference c = findPreference("test_copy");
		c.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			@Override
			public boolean onPreferenceClick(Preference preference) {
				copy();
				return true;
			}
		});

		Preference d = findPreference("test_delete");
		d.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			@Override
			public boolean onPreferenceClick(Preference preference) {
				delete();
				return true;
			}
		});
	}

	void copy() {
		File from = new File("/mnt/extSdCard/luzi82/tmp");
		File to = new File("/mnt/extSdCard/luzi82/tmp2");
		Clone c = new Clone(exe, from, to);
		c.setCallback(new Callback<Boolean>() {
			@Override
			public void atFinish(Boolean aResult) {
				T_T.v("copy atFinish " + aResult);
			}
		});
		c.start();
	}

	void delete() {
		T_T.vf();
		File tar = new File("/mnt/extSdCard/luzi82/tmp2");
		Delete d = new Delete(exe, tar);
		d.setCallback(new Callback<Boolean>() {
			@Override
			public void atFinish(Boolean aResult) {
				T_T.v("delete atFinish " + aResult);
			}
		});
		d.start();
	}

}
