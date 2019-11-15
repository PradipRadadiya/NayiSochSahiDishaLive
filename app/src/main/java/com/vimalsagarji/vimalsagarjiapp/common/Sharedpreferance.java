package com.vimalsagarji.vimalsagarjiapp.common;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * Created by prade on 17-Oct-16.
 */

@SuppressWarnings("ALL")
public class Sharedpreferance {
    private final SharedPreferences sharedPreferences;
    private final Editor editor;
    private static final String SHARED = "DEMO";

    private static final String Id = "Id";
    private static final String First_name = "First_name";
    private static final String Last_name = "Last_name";
    private static final String Email = "Email";
    private static final String User_name = "User_name";
    private static final String Status = "Status";
    private static final String Profile_image = "Profile_image";
    private static final String Mobile = "Mobile";
    private static final String PushNotification = "PushNotification";
    private static final String token = "Token";

    @SuppressLint("CommitPrefEdits")
    public Sharedpreferance(Context mContext) {
        sharedPreferences = mContext.getSharedPreferences(SHARED, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public String getId() {
        return sharedPreferences.getString(Id, "");
    }

    public void saveId(String id) {
        editor.putString(Id, id);
        editor.commit();
    }

    @SuppressWarnings("unused")
    public String getFirst_name() {
        return sharedPreferences.getString(First_name, "");
    }

    @SuppressWarnings("unused")
    public void saveFirst_name(String fnm) {
        editor.putString(First_name, fnm);
        editor.commit();
    }

    @SuppressWarnings("unused")
    public String getEmail() {
        return sharedPreferences.getString(Email, "");
    }

    public void saveEmail(String em) {
        editor.putString(Email, em);
        editor.commit();
    }

    @SuppressWarnings("unused")
    public String getLast_name() {
        return sharedPreferences.getString(Last_name, "");
    }

    @SuppressWarnings("unused")
    public void saveLast_name(String lnm) {
        editor.putString(Last_name, lnm);
        editor.commit();
    }

    @SuppressWarnings("unused")
    public String getUser_name() {
        return sharedPreferences.getString(User_name, "");
    }

    @SuppressWarnings("unused")
    public void saveUser_name(String unm) {
        editor.putString(User_name, unm);
        editor.commit();
    }

    public String getStatus() {
        return sharedPreferences.getString(Status, "");
    }

    @SuppressWarnings("unused")
    public void saveStatus(String st) {
        editor.putString(Status, st);
        editor.commit();
    }

    @SuppressWarnings("unused")
    public String getProfile_image() {
        return sharedPreferences.getString(Profile_image, "");
    }

    @SuppressWarnings("unused")
    public void saveProfile_image(String img) {
        editor.putString(Profile_image, img);
        editor.commit();
    }

    @SuppressWarnings("unused")
    public String getMobile() {
        return sharedPreferences.getString(Mobile, "");
    }

    @SuppressWarnings("unused")
    public void saveMobile(String mobile) {
        editor.putString(Mobile, mobile);
        editor.commit();
    }

    //Get and Save Mobile
    public String getPushNotification() {
        return sharedPreferences.getString(PushNotification, "");
    }

    public void savePushNotification(String notification) {
        editor.putString(PushNotification, notification);
        editor.commit();
    }

    //Get and Save Token
    public String getToken() {
        return sharedPreferences.getString(token, "");
    }

    public void saveToken(String tokens) {
        editor.putString(token, tokens);
        editor.commit();
    }

}
