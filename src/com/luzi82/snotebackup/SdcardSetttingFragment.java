package com.luzi82.snotebackup;

import java.util.LinkedList;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceFragment;

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

		entryList = new LinkedList<String>();
		entryValueList = new LinkedList<String>();
		for (int hr = 0; hr < 24; ++hr) {
			for (int j = 0; j < 2; ++j) {
				int mn = j * 30;
				long t = 0; // hour
				t += hr;
				t *= 60; // min
				t += mn;
				t *= 60; // sec
				t *= 1000; // msec
				t = Pref.unOffset(t);

				entryList.add(String.format("%02d:%02d", hr, mn));
				entryValueList.add(Long.toString(t));
			}
		}
		ListPreference time_p = (ListPreference) findPreference("preference_setting_sdcard_period_time");
		time_p.setEntries(entryList.toArray(new String[0]));
		time_p.setEntryValues(entryValueList.toArray(new String[0]));

		entryList = new LinkedList<String>();
		entryValueList = new LinkedList<String>();
		for (Pref.Period pp : Pref.Period.values()) {
			entryList.add(getString(pp.res));
			entryValueList.add(pp.name());
		}
		lp = (ListPreference) findPreference("preference_setting_sdcard_period");
		lp.setEntries(entryList.toArray(new String[0]));
		lp.setEntryValues(entryValueList.toArray(new String[0]));

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
		SharedPreferences sp = getPreferenceManager().getSharedPreferences();

		Pref pref = Pref.getPref(getActivity());

		// String preference_setting_sdcard_period_value =
		// sp.getString("preference_setting_sdcard_period", "day");
		// boolean preference_setting_sdcard_enable_value =
		// sp.getBoolean("preference_setting_sdcard_enable", false);
		// String preference_setting_sdcard_period_time_value =
		// sp.getString("preference_setting_sdcard_period_time", "04:30");
		// String preference_setting_sdcard_range_value =
		// sp.getString("preference_setting_sdcard_range", "7");
		// String preference_setting_sdcard_min_copy_value =
		// sp.getString("preference_setting_sdcard_min_copy", "3");

		Preference p;
		boolean b;
		long l;
		int hr, mn;

		p = findPreference("preference_setting_sdcard_period");
		p.setSummary(pref.preference_setting_sdcard_period.res);

		p = findPreference("preference_setting_sdcard_period_time");
		b = true;
		b = b && pref.preference_setting_sdcard_enable;
		b = b && (pref.preference_setting_sdcard_period == Pref.Period.DAY);
		p.setEnabled(b);
		l = pref.preference_setting_sdcard_period_time;
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
