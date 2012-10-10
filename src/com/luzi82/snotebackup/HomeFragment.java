package com.luzi82.snotebackup;

import android.os.Bundle;
import android.preference.PreferenceFragment;

public class HomeFragment extends PreferenceFragment {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		addPreferencesFromResource(R.xml.home);
	}

}
