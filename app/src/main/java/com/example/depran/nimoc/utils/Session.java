package com.example.depran.nimoc.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

public class Session {
	public static SharedPreferences pref;
	public static SharedPreferences prefIntro;
	public static Editor editor;
	public static Editor editorIntro;
	public static Boolean introBoolean;
	public static String PREF_NAME = "Nimoc";
	public static String PREF_NAME_INTRO = "Intro SliderNimoc";

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

	public static void createAwalSession(Context context, boolean b){
        prefIntro = context.getSharedPreferences(PREF_NAME_INTRO, 0);
        editorIntro = prefIntro.edit();
        introBoolean = b;
        editorIntro.putBoolean("awalan", introBoolean);
        Log.e("awalan : ", String.valueOf(introBoolean));
        editorIntro.commit();
    }

	public static void logout(Context context) {
		pref = context.getSharedPreferences(PREF_NAME, 0);
		editor = pref.edit();
		editor.clear();
		editor.commit();
	}
}