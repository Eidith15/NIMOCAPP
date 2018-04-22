package com.example.depran.nimoc.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class Session {
	public static SharedPreferences pref;
	public static Editor editor;
	public static String PREF_NAME = "Nimoc";

	public static void createLoginSession(Context context, String... params) {
		pref = context.getSharedPreferences(PREF_NAME, 0);
		editor = pref.edit();

		editor.putString("id_u", params[0]);
		editor.putString("username", params[1]);
		editor.putString("password", params[2]);
//		editor.putString("name", params[3]);
//		editor.putString("no_telp", params[4]);
//		editor.putString("photo", params[5]);

		editor.commit();
	}

	public static void logout(Context context) {
		pref = context.getSharedPreferences(PREF_NAME, 0);
		editor = pref.edit();
		editor.clear();
		editor.commit();
	}
}