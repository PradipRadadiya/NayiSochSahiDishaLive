package com.vimalsagarji.vimalsagarjiapp.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import java.util.HashMap;

/**
 * Created by Ashok on 2/5/2016.
 */
@SuppressWarnings("ALL")
public class SessionManager {
    private final SharedPreferences pref;
    // Editor for Shared preferences
    private final Editor editor;
    // Context
    private final Context _context;

    // Shared pref mode
    private final int PRIVATE_MODE = 0;
    private static final String PREF_NAME = "AndroidHivePref";
    // All Shared Preferences Keys
    private static final String IS_LOGIN = "IsLoggedIn";
    // User name (make variable public to access from outside)
    public static final String KEY_STATUS = "status";

    public static final String KEY_ID = "id";
    // Email address (make variable public to access from outside)

    // Constructor
    @SuppressLint("CommitPrefEdits")
    public SessionManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> user = new HashMap<String, String>();
        // user name
        user.put(KEY_STATUS, pref.getString(KEY_STATUS, null));
        // user email id
        user.put(KEY_ID, pref.getString(KEY_ID, null));
        return user;

    }

    // Get Login State
    private boolean isLoggedIn() {
        return pref.getBoolean(IS_LOGIN, false);
    }
}