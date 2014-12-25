package com.atguigu.ms_git.util;

import android.content.Context;
import android.content.SharedPreferences;

public class SpUtils {

	public static final String SIM = "sim";
	public static final String IS_PROTECTED = "isProtected";
	public static final String NUMBER = "number";
	public static final String LOST_LOCATION = "lostLocation";
	public static final String SET_UP = "setUp";

	private static SharedPreferences sp;
	private static SpUtils instance = new SpUtils();

	private SpUtils() {
	}

	public static SpUtils getInstance(Context context) {
		if (sp == null)
			sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
		return instance;
	}

	public void putString(String name, String value) {
		sp.edit().putString(name, value).commit();
	}

	public void putBoolean(String name, boolean value) {
		sp.edit().putBoolean(name, value).commit();
	}

	public void putInt(String name, int value) {
		sp.edit().putInt(name, value).commit();
	}

	public String getString(String name, String defaultValue) {
		return sp.getString(name, defaultValue);
	}

	public boolean getBoolean(String name, boolean defaultValue) {
		return sp.getBoolean(name, defaultValue);
	}

	public int getInt(String name, int defaultValue) {
		return sp.getInt(name, defaultValue);
	}

	public void remove(String key) {
		sp.edit().remove(key).commit();
	}
}
