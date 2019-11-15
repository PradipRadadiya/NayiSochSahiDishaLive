package com.vimalsagarji.vimalsagarjiapp.activity.splash;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.vimalsagarji.vimalsagarjiapp.ActivityHomeMain;
import com.vimalsagarji.vimalsagarjiapp.R;
import com.vimalsagarji.vimalsagarjiapp.RegisterActivity;
import com.vimalsagarji.vimalsagarjiapp.common.CommonMethod;
import com.vimalsagarji.vimalsagarjiapp.common.CommonUrl;
import com.vimalsagarji.vimalsagarjiapp.common.Sharedpreferance;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import ch.boye.httpclientandroidlib.NameValuePair;
import ch.boye.httpclientandroidlib.message.BasicNameValuePair;
import io.fabric.sdk.android.Fabric;

@SuppressWarnings("ALL")
public class ThirdSpalshScreenActivity extends AppCompatActivity {
    Intent intent;
    private Sharedpreferance sharedpreferance;
    private ProgressBar progress;
    private TextView txt_content,txt_timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.content_third_spalsh_screen);
//        Fabric.with(this, new Crashlytics());
        sharedpreferance = new Sharedpreferance(ThirdSpalshScreenActivity.this);
        progress = (ProgressBar) findViewById(R.id.progress);
        txt_content = findViewById(R.id.txt_content);
        txt_timer = findViewById(R.id.txt_timer);
        new ContentGet().execute();

        Log.e("strinf decode","-----------"+CommonMethod.decodeEmoji("A%29+%E0%A4%B8%E0%A4%BE%E0%A4%A2%E0%A5%82+%E0%A4%AD%E0%A4%BE%E0%A4%88"));

    }

    private void askforPermission() {

        Intent intent = new Intent(ThirdSpalshScreenActivity.this, RegisterActivity.class);
        startActivity(intent);
        finish();




        /*askCompactPermissions(new String[]{ PermissionUtils.Manifest_RECEIVE_SMS,PermissionUtils.Manifest_READ_SMS,PermissionUtils.Manifest_SEND_SMS}, new PermissionChecker.PermissionResult() {
            @Override
            public void permissionGranted() {
                Log.e("permission","---------------Granted");
                Intent intent = new Intent(ThirdSpalshScreenActivity.this, RegisterActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void permissionDenied() {
                Log.e("permission","---------------Not Granted");
                askforPermission();
            }

            @Override
            public void permissionForeverDenied() {
                Log.e("permission","---------------Not Granted");
                askforPermission();
            }

        });*/

    }

    private void askforLogin() {
        Intent intent = new Intent(ThirdSpalshScreenActivity.this, ActivityHomeMain.class);
        startActivity(intent);
        finish();

    }

    private final CountDownTimer timer = new CountDownTimer(16000, 1000) {
        @Override
        public void onTick(long millisUntilFinished) {
            Log.e("time","----------"+millisUntilFinished / 1000);
            txt_timer.setText(String.valueOf(millisUntilFinished / 1000));

        }

        @Override
        public void onFinish() {

            txt_timer.setText("");
            if (sharedpreferance.getId().equalsIgnoreCase("")) {
                askforPermission();
            }

            else {
                if (CommonMethod.isInternetConnected(ThirdSpalshScreenActivity.this)) {
                    new AllreadyRegisterUser().execute(sharedpreferance.getEmail(), sharedpreferance.getMobile(), sharedpreferance.getToken());
                } else {
                    Toast.makeText(ThirdSpalshScreenActivity.this, R.string.internet, Toast.LENGTH_LONG).show();

                }

            }

        }
    };

    private class AllreadyRegisterUser extends AsyncTask<String, Void, String> {
        String responseJSON = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... params) {
            try {

                ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();
                nameValuePairs.add(new BasicNameValuePair("EmailID", params[0]));
                nameValuePairs.add(new BasicNameValuePair("Phone", params[1]));
                nameValuePairs.add(new BasicNameValuePair("DeviceID", params[2]));
                responseJSON = CommonMethod.postStringResponse(CommonUrl.Main_url+"aluser/checkuser", nameValuePairs, ThirdSpalshScreenActivity.this);

            } catch (Exception e) {
                e.printStackTrace();
            }
            return responseJSON;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("reponse", "-----------------" + s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                if (jsonObject.getString("status").equalsIgnoreCase("success")) {
                    progress.setVisibility(View.GONE);
//                    dialog.dismiss();
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    Log.e("status", "-----------------success");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);
                        Log.e("array", "-----------------success");
                        String uid = object.getString("ID");
                        String email = object.getString("EmailID");
                        String mobile = object.getString("Phone");
                        String Address = object.getString("Address");

                        String Name = object.getString("Name");
                        String Phone = object.getString("DeviceToken");

                        sharedpreferance.saveId(uid);
                        sharedpreferance.saveEmail(email);
                        sharedpreferance.saveMobile(mobile);
                        sharedpreferance.saveLast_name(Address);
                        sharedpreferance.saveFirst_name(Name);

                        Log.e("email", "---------------" + sharedpreferance.getEmail());
                        Log.e("uid", "---------------" + sharedpreferance.getId());

                        askforLogin();


                    }

                } else {
                    progress.setVisibility(View.GONE);
                    Intent intent = new Intent(ThirdSpalshScreenActivity.this, RegisterActivity.class);
                    startActivity(intent);
                    finish();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

    }


    private class ContentGet extends AsyncTask<String, Void, String> {
        String responseJSON = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... params) {
            try {

                responseJSON = CommonMethod.getStringResponse(CommonUrl.Main_url+"quote/viewQuote");
            } catch (Exception e) {
                e.printStackTrace();
            }
            return responseJSON;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            timer.start();
            Log.e("reponse", "-----------------" + s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                if (jsonObject.getString("status").equalsIgnoreCase("success")) {

                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    Log.e("status", "-----------------success");

                    JSONObject object = jsonArray.getJSONObject(0);

                    Log.e("quote","-------------"+object.getString("title"));

                    txt_content.setText(CommonMethod.decodeEmoji(object.getString("title")));

                    Log.e("array", "-----------------success");


                    progress.setVisibility(View.GONE);

                } else {
                    progress.setVisibility(View.GONE);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

    }

}
