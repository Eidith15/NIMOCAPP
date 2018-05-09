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
        editor.putString("email", params[3]);
        editor.putString("no_hp", params[4]);
        editor.putString("jenis_kelamin", params[5]);
        editor.commit();
    }

    public static void updateAccountSession(Context context, String... params) {
        pref = context.getSharedPreferences(PREF_NAME, 0);
        editor = pref.edit();

        editor.putString("username", params[0]);
        editor.putString("email", params[1]);
        editor.putString("no_hp", params[2]);
        editor.putString("jenis_kelamin", params[3]);
        editor.commit();
    }

    public static void updatePasswordSession(Context context, String... params) {
        pref = context.getSharedPreferences(PREF_NAME, 0);
        editor = pref.edit();

        editor.putString("password", params[0]);

        editor.commit();
    }

    public static void createBukuSession(Context context, String... params) {
        pref = context.getSharedPreferences(PREF_NAME, 0);
        editor = pref.edit();
        editor.putString("id_buku", params[0]);
        editor.putString("nama_buku", params[1]);
        editor.commit();
    }

    public static void createDivisiSession(Context context, String... params) {
        pref = context.getSharedPreferences(PREF_NAME, 0);
        editor = pref.edit();
        editor.putString("id_divisi", params[0]);
        editor.commit();
    }

    public static void createAwalSession(Context context, boolean b) {
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