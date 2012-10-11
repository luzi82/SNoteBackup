package com.luzi82.snotebackup;

import java.util.List;

import android.os.Bundle;
import android.preference.PreferenceActivity;

public class MainActivity extends PreferenceActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

    @Override
    public void onBuildHeaders(List<Header> target) {
        loadHeadersFromResource(R.xml.main, target);
    }

}
