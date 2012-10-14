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
		addPreferencesFromResource(R.xml.setting_sdcard);

		Preference p;

		LinkedList<String> timeList = new LinkedList<String>();
		for (int i = 0; i < 24; ++i) {
			timeList.add(String.format("%02d:%02d", i, 0));
			timeList.add(String.format("%02d:%02d", i, 30));
		}
		String[] timeAry = timeList.toArray(new String[0]);

		ListPreference time_p = (ListPreference) findPreference("preference_setting_sdcard_period_time");
		time_p.setEntries(timeAry);
		time_p.setEntryValues(timeAry);

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

		String preference_setting_sdcard_period_value = sp.getString("preference_setting_sdcard_period", "day");
		boolean preference_setting_sdcard_enable_value = sp.getBoolean("preference_setting_sdcard_enable", false);
		String preference_setting_sdcard_period_time_value = sp.getString("preference_setting_sdcard_period_time", "04:30");
		String preference_setting_sdcard_range_value = sp.getString("preference_setting_sdcard_range", "7");
		String preference_setting_sdcard_min_copy_value = sp.getString("preference_setting_sdcard_min_copy", "3");

		Preference p;
		boolean b;

		p = findPreference("preference_setting_sdcard_period");
		if (preference_setting_sdcard_period_value.equals("hour")) {
			p.setSummary(R.string.preference_setting_xxx_period_hourly_title);
		} else if (preference_setting_sdcard_period_value.equals("day")) {
			p.setSummary(R.string.preference_setting_xxx_period_daily_title);
		} else {
			p.setSummary("");
		}

		p = findPreference("preference_setting_sdcard_period_time");
		b = true;
		b = b && preference_setting_sdcard_enable_value;
		b = b && preference_setting_sdcard_period_value.equals("day");
		p.setEnabled(b);
		p.setSummary(preference_setting_sdcard_period_time_value);

		p = findPreference("preference_setting_sdcard_range");
		p.setSummary(getString(R.string.preference_setting_xxx_range_summary, preference_setting_sdcard_range_value));

		p = findPreference("preference_setting_sdcard_min_copy");
		p.setSummary(preference_setting_sdcard_min_copy_value);
	}

}
