package com.luzi82.snotebackup;

import java.util.LinkedList;

import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.PreferenceFragment;

public class SdcardSetttingFragment extends PreferenceFragment {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.setting_sdcard);

		LinkedList<String> timeList = new LinkedList<String>();
		for (int i = 0; i < 24; ++i) {
			timeList.add(String.format("%02d:%02d", i, 0));
			timeList.add(String.format("%02d:%02d", i, 30));
		}
		String[] timeAry = timeList.toArray(new String[0]);

		ListPreference time_p = (ListPreference) findPreference("preference_setting_sdcard_period_time");
		time_p.setEntries(timeAry);
		time_p.setEntryValues(timeAry);
	}

}
