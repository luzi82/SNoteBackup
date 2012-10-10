package com.luzi82.snotebackup;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;

public class AboutFragment extends PreferenceFragment {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		addPreferencesFromResource(R.xml.about);

		// app ver
		Preference appVerPreference = findPreference("preference_info_about_version");
		PackageManager pm = getActivity().getPackageManager();
		String pn = getActivity().getPackageName();
		String appVerString = "unknown";
		try {
			PackageInfo pi = pm.getPackageInfo(pn, 0);
			appVerString = String.format("%1$s (%2$d)", pi.versionName, pi.versionCode);
		} catch (NameNotFoundException e1) {
			e1.printStackTrace();
		}
		appVerPreference.setSummary(appVerString);

		// email intent
		Preference emailPreference = findPreference("preference_info_about_email");
		Intent emailIntent = new Intent(Intent.ACTION_SEND);
		emailIntent.setType("message/rfc882");
		emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[] { getResources().getString(R.string.preference_info_about_email_address) });
		emailIntent.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.app_name));
		emailPreference.setIntent(Intent.createChooser(emailIntent, emailPreference.getTitle()));
	}

}
