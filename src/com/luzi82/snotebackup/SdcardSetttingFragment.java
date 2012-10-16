package com.luzi82.snotebackup;

import java.io.File;
import java.util.LinkedList;
import java.util.concurrent.Executor;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.os.StatFs;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceFragment;
import android.widget.Toast;

import com.luzi82.async.AbstractAsyncTask.Callback;
import com.luzi82.asyncfile.AsyncStatfs;
import com.luzi82.asyncfile.DirFileCount;
import com.luzi82.asyncfile.FileStat;
import com.luzi82.asyncfile.FileStat.Result;

public class SdcardSetttingFragment extends PreferenceFragment {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getPreferenceManager().setSharedPreferencesName(Pref.PREF_KEY);
		addPreferencesFromResource(R.xml.setting_sdcard);

		Executor executor = ((MainActivity) getActivity()).getExecutor();

		Preference p;
		LinkedList<String> entryList;
		LinkedList<String> entryValueList;
		ListPreference lp;

		// StatFs statfs = SNoteBackup.sdcardStatFs();

		entryList = new LinkedList<String>();
		entryValueList = new LinkedList<String>();
		for (Pref.Period pp : Pref.Period.values()) {
			entryList.add(getString(pp.res));
			entryValueList.add(pp.name());
		}
		lp = (ListPreference) findPreference("preference_setting_sdcard_schedule_backup_period");
		lp.setEntries(entryList.toArray(new String[0]));
		lp.setEntryValues(entryValueList.toArray(new String[0]));

		p = findPreference("preference_backup_now");
		p.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			@Override
			public boolean onPreferenceClick(Preference preference) {
				Intent i = new Intent(getActivity(), MainService.class);
				getActivity().startService(i);
				return true;
			}
		});

		p = findPreference("preference_setting_sdcard_range");
		p.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
			@Override
			public boolean onPreferenceChange(Preference preference, Object newValue) {
				try {
					String v = (String) newValue;
					Integer.parseInt(v);
				} catch (Exception e) {
					Toast.makeText(getActivity(), R.string.warning_input_number, Toast.LENGTH_SHORT).show();
					return false;
				}
				return true;
			}
		});

		p = findPreference("preference_setting_sdcard_min_copy");
		p.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
			@Override
			public boolean onPreferenceChange(Preference preference, Object newValue) {
				try {
					String v = (String) newValue;
					Integer.parseInt(v);
				} catch (Exception e) {
					Toast.makeText(getActivity(), R.string.warning_input_number, Toast.LENGTH_SHORT).show();
					return false;
				}
				return true;
			}
		});

		DirFileCount dfc = new DirFileCount(executor, SNoteBackup.sdcardAppDir(getActivity()));
		dfc.setCallback(new Callback<Integer>() {
			@Override
			public void atFinish(Integer aResult) {
				if (aResult < 0)
					aResult = 0;
				Preference p = findPreference("preference_setting_sdcard_backup_count");
				p.setSummary(aResult.toString());
			}
		});
		dfc.start();

		AsyncStatfs as = new AsyncStatfs(executor, SNoteBackup.sdcardDir());
		as.setCallback(new Callback<StatFs>() {
			@Override
			public void atFinish(StatFs aResult) {
				Preference p = findPreference("preference_setting_sdcard_available_space");
				boolean b = (aResult != null) && (aResult.getAvailableBlocks() != 0);
				p.setShouldDisableView(!b);
				if (b) {
					long sizeL = 1;
					sizeL *= aResult.getAvailableBlocks();
					sizeL *= aResult.getBlockSize();
					p.setSummary(SNoteBackup.toByteSize(sizeL));
				}

				p = findPreference("preference_setting_sdcard_backup_count");
				p.setShouldDisableView(!b);

				p = findPreference("preference_setting_sdcard_backup_size");
				p.setShouldDisableView(!b);
			}
		});
		as.start();

		FileStat fs = new FileStat(executor, SNoteBackup.sdcardAppDir(getActivity()));
		fs.setCallback(new Callback<FileStat.Result>() {
			@Override
			public void atFinish(Result aResult) {
				if (aResult == null)
					return;
				Preference p = findPreference("preference_setting_sdcard_backup_size");
				p.setSummary(SNoteBackup.toByteSize(aResult.mSize));
			}
		});
		fs.start();

		fs = new FileStat(executor, new File(SNoteBackup.SNOTE_PATH));
		fs.setCallback(new Callback<FileStat.Result>() {
			@Override
			public void atFinish(Result aResult) {
				if (aResult == null)
					return;
				Preference p = findPreference("preference_setting_sdcard_snote_size");
				p.setSummary(SNoteBackup.toByteSize(aResult.mSize));
			}
		});
		fs.start();
	}

	@Override
	public void onResume() {
		super.onResume();

		SharedPreferences sp = getPreferenceManager().getSharedPreferences();
		sp.registerOnSharedPreferenceChangeListener(changeListener);

		update();
	}

	@Override
	public void onPause() {
		super.onPause();

		SharedPreferences sp = getPreferenceManager().getSharedPreferences();
		sp.unregisterOnSharedPreferenceChangeListener(changeListener);
	}

	SharedPreferences.OnSharedPreferenceChangeListener changeListener = new OnSharedPreferenceChangeListener() {
		@Override
		public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
			update();
		}
	};

	private void update() {
		Pref pref = Pref.getPref(getActivity());

		Preference p;
		boolean b;
		long l;
		int hr, mn;

		p = findPreference("preference_setting_sdcard_schedule_backup_period");
		p.setSummary(pref.preference_setting_sdcard_schedule_backup_period.res);

		p = findPreference("preference_setting_sdcard_next_backup_time");
		b = true;
		b = b && pref.preference_setting_sdcard_backup_enable;
		b = b && (pref.preference_setting_sdcard_schedule_backup_period == Pref.Period.DAY);
		p.setEnabled(b);
		l = pref.preference_setting_sdcard_next_backup_time;
		l = Pref.offset(l); // msec
		l /= 1000; // sec
		l /= 60; // min
		mn = (int) (l % 60);
		l /= 60; // hour
		hr = (int) l;
		p.setSummary(String.format("%02d:%02d", hr, mn));

		p = findPreference("preference_setting_sdcard_range");
		p.setSummary(getString(R.string.preference_setting_xxx_range_summary, pref.preference_setting_sdcard_range));

		p = findPreference("preference_setting_sdcard_min_copy");
		p.setSummary(Integer.toString(pref.preference_setting_sdcard_min_copy));

		b = true;
		b = b && pref.preference_setting_sdcard_backup_enable;
		b = b && pref.preference_setting_sdcard_cleanup_enable;
		p = findPreference("preference_setting_sdcard_range");
		p.setEnabled(b);
		p = findPreference("preference_setting_sdcard_min_copy");
		p.setEnabled(b);
	}

}
