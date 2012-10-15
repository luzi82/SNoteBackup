package com.luzi82.snotebackup;

import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.content.SharedPreferences;

public class Pref {

	static final String PREF_KEY = "com.luzi82.snotebackup";

	enum Period {
		HOUR1(R.string.preference_setting_xxx_period_hour1_title), //
		HOUR2(R.string.preference_setting_xxx_period_hour2_title), //
		HOUR3(R.string.preference_setting_xxx_period_hour3_title), //
		HOUR6(R.string.preference_setting_xxx_period_hour6_title), //
		HOUR12(R.string.preference_setting_xxx_period_hour12_title), //
		DAY(R.string.preference_setting_xxx_period_day_title), //
		;
		public int res;

		Period(int _res) {
			res = _res;
		}
	}

	boolean preference_setting_sdcard_enable;
	Period preference_setting_sdcard_schedule_backup_period;
	long preference_setting_sdcard_next_backup_time;
	int preference_setting_sdcard_range;
	int preference_setting_sdcard_min_copy;

	static Pref getPref(Context aContext) {
		SharedPreferences sp = aContext.getSharedPreferences(PREF_KEY, Context.MODE_MULTI_PROCESS);
		Pref ret = new Pref();

		boolean preference_setting_sdcard_enable_default = Boolean.parseBoolean(aContext.getString(R.string.preference_setting_sdcard_enable_default));
		try {
			ret.preference_setting_sdcard_enable = sp.getBoolean("preference_setting_sdcard_enable", preference_setting_sdcard_enable_default);
		} catch (Exception e) {
			ret.preference_setting_sdcard_enable = preference_setting_sdcard_enable_default;
		}

		String preference_setting_sdcard_schedule_backup_period_default = aContext.getString(R.string.preference_setting_sdcard_schedule_backup_period_default);
		try {
			ret.preference_setting_sdcard_schedule_backup_period = Enum.valueOf(Period.class, sp.getString("preference_setting_sdcard_schedule_backup_period", preference_setting_sdcard_schedule_backup_period_default));
		} catch (Exception e) {
			ret.preference_setting_sdcard_schedule_backup_period = Enum.valueOf(Period.class, preference_setting_sdcard_schedule_backup_period_default);
		}

		String preference_setting_sdcard_next_backup_time_default = Long.toString(preference_setting_sdcard_next_backup_time_default(aContext));
		try {
			ret.preference_setting_sdcard_next_backup_time = Long.parseLong(sp.getString("preference_setting_sdcard_next_backup_time", preference_setting_sdcard_next_backup_time_default));
		} catch (Exception e) {
			ret.preference_setting_sdcard_next_backup_time = Long.parseLong(preference_setting_sdcard_next_backup_time_default);
		}

		String preference_setting_sdcard_range_default = aContext.getString(R.string.preference_setting_sdcard_range_default);
		try {
			ret.preference_setting_sdcard_range = Integer.parseInt(sp.getString("preference_setting_sdcard_range", preference_setting_sdcard_range_default));
		} catch (Exception e) {
			ret.preference_setting_sdcard_range = Integer.parseInt(preference_setting_sdcard_range_default);
		}

		String preference_setting_sdcard_min_copy_default = aContext.getString(R.string.preference_setting_sdcard_min_copy_default);
		try {
			ret.preference_setting_sdcard_min_copy = Integer.parseInt(sp.getString("preference_setting_sdcard_min_copy", preference_setting_sdcard_min_copy_default));
		} catch (Exception e) {
			ret.preference_setting_sdcard_min_copy = Integer.parseInt(preference_setting_sdcard_min_copy_default);
		}

		return ret;
	}

	static long preference_setting_sdcard_next_backup_time_default(Context aContext) {
		Pattern p = Pattern.compile("(\\d{2})\\:(\\d{2})");

		String def = aContext.getString(R.string.preference_setting_sdcard_next_backup_time_default);
		T_T.v(def);
		Matcher m = p.matcher(def);
		m.matches();

		int hr = Integer.parseInt(m.group(1));
		int mn = Integer.parseInt(m.group(2));

		long ret = 0; // hr
		ret += hr;
		ret *= 60; // min
		ret += mn;
		ret *= 60; // sec
		ret *= 1000; // msec
		ret = unOffset(ret);

		return ret;
	}

	public static long offset(long aMsec) {
		long z = TimeZone.getDefault().getRawOffset();
		long day = ((long) 24) * 60 * 60 * 1000;

		long ret = 0; // msec
		ret += aMsec;
		ret += z;

		ret += day * 2;
		ret %= day;

		return ret;
	}

	public static long unOffset(long aMsec) {
		long z = TimeZone.getDefault().getRawOffset();
		long day = ((long) 24) * 60 * 60 * 1000;

		long ret = 0; // msec
		ret += aMsec;
		ret -= z;

		ret += day * 2;
		ret %= day;

		return ret;
	}

	private Pref() {
	}

}
