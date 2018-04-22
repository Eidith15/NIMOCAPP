package com.example.depran.nimoc.function;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class Session {
    public static SharedPreferences preferences;
    public static Editor editor;
    public static String PREF_NAME = "DataMahasiswa";

    public static void createSignInSession(Context context, String username){
        preferences = context.getSharedPreferences(PREF_NAME, 0);
        editor = preferences.edit();
        editor.putString("username", username);
        editor.commit();
    }

    public static void logout(Context context){
        preferences = context.getSharedPreferences(PREF_NAME, 0);
        editor = preferences.edit();
        editor.clear();
        editor.commit();
    }


}

