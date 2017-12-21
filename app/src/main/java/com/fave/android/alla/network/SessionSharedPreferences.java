package com.fave.android.alla.network;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashSet;

/**
 * Created by seungyeop on 2017-12-06.
 */

public class SessionSharedPreferences {

    public static final String KEY_COOKIE = "com.dalgonakit.key.cookie";

    private static SessionSharedPreferences dsp = null;

    public static SessionSharedPreferences getInstanceOf(Context c){
        if(dsp==null){
            dsp = new SessionSharedPreferences(c);
        }
        return dsp;
    }

    private Context context;
    private SharedPreferences pref;

    public SessionSharedPreferences(Context c) {
        context = c;
        final String PREF_NAME = c.getPackageName();
        pref = context.getSharedPreferences(PREF_NAME, Activity.MODE_PRIVATE);
    }

    public void putHashSet(String key, HashSet<String> set){
        SharedPreferences.Editor editor = pref.edit();
        editor.putStringSet(key, set);
        editor.commit();
    }

    public HashSet<String> getHashSet(String key, HashSet<String> dftValue){
        try {
            return (HashSet<String>)pref.getStringSet(key, dftValue);
        } catch (Exception e) {
            e.printStackTrace();
            return dftValue;
        }
    }
}
