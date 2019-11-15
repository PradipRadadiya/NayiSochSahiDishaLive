package com.vimalsagarji.vimalsagarjiapp;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessaging;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.vimalsagarji.vimalsagarjiapp.common.CommonMethod;
import com.vimalsagarji.vimalsagarjiapp.common.CommonUrl;
import com.vimalsagarji.vimalsagarjiapp.common.Sharedpreferance;
import com.vimalsagarji.vimalsagarjiapp.fcm.Config;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import ch.boye.httpclientandroidlib.NameValuePair;
import ch.boye.httpclientandroidlib.message.BasicNameValuePair;



public class AlreadyRegisterUser extends AppCompatActivity {
    KProgressHUD loadingProgressDialog;
    Sharedpreferance sharedpreferance;
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private String strDevicetoken;
    ProgressDialog progressDialog;
    String randno;
    EditText edit_otp;
    EditText edit_mobile;
    EditText edit_email;
    Dialog dialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_already_registr_user);

        ImageView imgarrorback= (ImageView) findViewById(R.id.imgarrorback);
        ImageView img_search= (ImageView) findViewById(R.id.img_search);
        ImageView imgHome= (ImageView) findViewById(R.id.imgHome);
        TextView txt_title= (TextView) findViewById(R.id.txt_title);
        txt_title.setText("Registered User");
        imgHome.setVisibility(View.GONE);
        img_search.setVisibility(View.GONE);
        imgarrorback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        sharedpreferance = new Sharedpreferance(AlreadyRegisterUser.this);
        if (CommonMethod.isInternetConnected(AlreadyRegisterUser.this)) {
            mRegistrationBroadcastReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {

                    // checking for type intent filter
                    if (intent.getAction().equals(Config.REGISTRATION_COMPLETE)) {
                        // gcm successfully registered
                        // now subscribe to `global` topic to receive app wide notifications
                        FirebaseMessaging.getInstance().subscribeToTopic(Config.TOPIC_GLOBAL);
                        if (CommonMethod.isInternetConnected(AlreadyRegisterUser.this)) {
                            try {
                                displayFirebaseRegId();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                    } else if (intent.getAction().equals(Config.PUSH_NOTIFICATION)) {
                        // new push notification is received

                        String message = intent.getStringExtra("message");

                        Toast.makeText(getApplicationContext(), "Push notification: " + message, Toast.LENGTH_SHORT).show();

                        Log.e("message", "---------------" + message);
                    }
                }
            };
        } else {
            Toast.makeText(AlreadyRegisterUser.this, R.string.internet, Toast.LENGTH_SHORT).show();
        }

        if (CommonMethod.isInternetConnected(AlreadyRegisterUser.this)) {
            try {
                displayFirebaseRegId();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
//        strDeviceToken = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID); //get android device id
        sharedpreferance = new Sharedpreferance(AlreadyRegisterUser.this);


        Log.e("Device token:", "--------------" + strDevicetoken);


        edit_email = (EditText) findViewById(R.id.emailid);
        edit_mobile = (EditText) findViewById(R.id.mobileno);
        Button btn_submit = (Button) findViewById(R.id.btn_submit);
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 if (TextUtils.isEmpty(edit_mobile.getText().toString())) {
                    edit_mobile.setError("Please enter mobile no.");
                    edit_mobile.requestFocus();
                } else {
                    if (CommonMethod.isInternetConnected(AlreadyRegisterUser.this)) {

                        Log.e("new token","---------------------"+strDevicetoken);
                        new AllreadyRegisterUser().execute(edit_mobile.getText().toString(),strDevicetoken);
//                        new GenrateOTP().execute();
                    } else {
                        Toast.makeText(AlreadyRegisterUser.this, R.string.internet, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void displayFirebaseRegId() {
        try {
            SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
            String regId = pref.getString("regId", null);

            Log.e("Firebase token id: ", regId);

            if (!TextUtils.isEmpty(regId)) {
    //            txtRegId.setText("Firebase Reg Id: " + regId);
                Log.e("Firebase token id: ", regId);
                strDevicetoken = regId;
            } else {
                Log.e("Firebase Reg Id ", "is not received yet!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private class AllreadyRegisterUser extends AsyncTask<String, Void, String> {
        String responseJSON = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loadingProgressDialog = KProgressHUD.create(AlreadyRegisterUser.this)
                    .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                    .setLabel("Please Wait")
                    .setCancellable(false);
            loadingProgressDialog.show();

        }

        @Override
        protected String doInBackground(String... params) {
            try {

                ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();
//                nameValuePairs.add(new BasicNameValuePair("EmailID", params[0]));
                nameValuePairs.add(new BasicNameValuePair("Phone", params[0]));
                nameValuePairs.add(new BasicNameValuePair("DeviceID", params[1]));
                responseJSON = CommonMethod.postStringResponse(CommonUrl.Main_url+"aluser/checkuser", nameValuePairs, AlreadyRegisterUser.this);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return responseJSON;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("reponse", "-----------------" + s);
            loadingProgressDialog.dismiss();
            try {
                JSONObject jsonObject = new JSONObject(s);
                if (jsonObject.getString("status").equalsIgnoreCase("success")) {
//                    dialog.dismiss();
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    Log.e("status", "-----------------success");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);
                        Log.e("array", "-----------------success");
                        String uid = object.getString("ID");
                        String email = object.getString("EmailID");
                        String mobile = object.getString("Phone");

                        sharedpreferance.saveId(uid);
                        sharedpreferance.saveEmail(email);
                        sharedpreferance.saveMobile(mobile);
                        sharedpreferance.saveToken(strDevicetoken);
                        sharedpreferance.savePushNotification("pushon");

                        Log.e("email", "---------------" + sharedpreferance.getEmail());
                        Log.e("uid", "---------------" + sharedpreferance.getId());

                    }
                    new GenrateOTP().execute();

                } else {
                    Toast.makeText(AlreadyRegisterUser.this, "" + jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
//                Window window = dialog.getWindow();
//                window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(AlreadyRegisterUser.this, RegisterActivity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    @SuppressLint("StaticFieldLeak")
    private class GenrateOTP extends AsyncTask<String, Void, String> {
        String responseString = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(AlreadyRegisterUser.this);
            progressDialog.setMessage("Please wait..");
        }

        @Override
        protected String doInBackground(String... params) {
            int max = 999999;
            int min = 100000;
            randno = String.valueOf(Math.round(Math.random() * (max - min + 1) + min));
//            int mobile=Integer.parseInt(etMobile.getText().toString());
            responseString = CommonMethod.getStringResponse("https://control.msg91.com/api/sendotp.php?authkey=210431AROU1gUWMy5ad5aa00&mobile=+91" + edit_mobile.getText().toString() + "&message=Your%20otp%20is%20" + randno + "&sender=NAYISO&otp=" + randno + "&otp_expiry=5&otp_length=6");
//            responseString=CommonMethod.getStringResponse("https://control.msg91.com/api/sendotp.php?authkey=170539A1PovTWJpc0s5996ef0e&mobile=919725800283&message=Your%20otp%20is%20" + String.valueOf(randno) + "&sender=Nayi Soch&otp=" + String.valueOf(randno)+"&otp_length=6");
            return responseString;

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("response", "-----------------" + s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                if (jsonObject.getString("type").equalsIgnoreCase("success")) {
                    progressDialog.dismiss();

                    dialog = new Dialog(AlreadyRegisterUser.this);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.dialog_getotp);
                    dialog.show();

                    edit_otp = (EditText) dialog.findViewById(R.id.edit_otp);
                    Button btn_submit = (Button) dialog.findViewById(R.id.button_submit);
                    ImageView img_close = (ImageView) dialog.findViewById(R.id.img_close);


                    img_close.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });

                    btn_submit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (CommonMethod.isInternetConnected(AlreadyRegisterUser.this)) {
                                new VeryfyOTP().execute();
                            } else {
                                Toast.makeText(AlreadyRegisterUser.this, R.string.internet, Toast.LENGTH_SHORT).show();
                            }

                        }
                    });

                    WindowManager.LayoutParams attrs = getWindow().getAttributes();
                    attrs.flags &= ~WindowManager.LayoutParams.FLAG_FULLSCREEN;
                    getWindow().setAttributes(attrs);
                    dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


                } else {
                    progressDialog.dismiss();
                    Toast.makeText(AlreadyRegisterUser.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

                }


            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (progressDialog != null) {
                progressDialog.dismiss();
            }

        }

    }

    @SuppressLint("StaticFieldLeak")
    private class VeryfyOTP extends AsyncTask<String, Void, String> {
        String responseString = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(AlreadyRegisterUser.this);
            progressDialog.setMessage("Please wait..");
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            responseString = CommonMethod.getStringResponse("https://control.msg91.com/api/verifyRequestOTP.php?authkey=210431AROU1gUWMy5ad5aa00&mobile=91" + edit_mobile.getText().toString() + "&otp=" + edit_otp.getText().toString());
            return responseString;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("response", "-----------------" + s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                if (jsonObject.getString("type").equalsIgnoreCase("success")) {
                    progressDialog.dismiss();
                    dialog.dismiss();
                    Toast.makeText(AlreadyRegisterUser.this, "" + jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    if (CommonMethod.isInternetConnected(AlreadyRegisterUser.this)) {
                        Intent intent = new Intent(AlreadyRegisterUser.this, ActivityHomeMain.class);
                        startActivity(intent);
                        finish();
                        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
//                        new AllreadyRegisterUser().execute(edit_email.getText().toString(), edit_mobile.getText().toString(), strDevicetoken);
                    }
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(AlreadyRegisterUser.this, "" + jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (progressDialog != null) {
                progressDialog.dismiss();
            }

        }


    }


}
