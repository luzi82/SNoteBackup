package com.luzi82.snotebackup;

import java.util.LinkedList;

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

public class SdcardSetttingFragment extends PreferenceFragment {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getPreferenceManager().setSharedPreferencesName(Pref.PREF_KEY);
		addPreferencesFromResource(R.xml.setting_sdcard);

		Preference p;
		LinkedList<String> entryList;
		LinkedList<String> entryValueList;
		ListPreference lp;
		boolean b;

		StatFs statfs = SNoteBackup.sdcardStatFs();

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

		p = findPreference("preference_setting_sdcard_available_space");
		b = (statfs != null) && (statfs.getAvailableBlocks() != 0);
		p.setShouldDisableView(!b);
		if (b) {
			long sizeL = 1;
			sizeL *= statfs.getAvailableBlocks();
			sizeL *= statfs.getBlockSize();
			p.setSummary(SNoteBackup.toByteSize(sizeL));
		}

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
		b = b && pref.preference_setting_sdcard_enable;
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
	}

}
