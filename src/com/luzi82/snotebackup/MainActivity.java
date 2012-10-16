package com.luzi82.snotebackup;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import android.os.Bundle;
import android.preference.PreferenceActivity;

public class MainActivity extends PreferenceActivity {

	private Executor mExecutor = Executors.newCachedThreadPool();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onBuildHeaders(List<Header> target) {
		loadHeadersFromResource(R.xml.main, target);
	}

	public Executor getExecutor() {
		return mExecutor;
	}

}
