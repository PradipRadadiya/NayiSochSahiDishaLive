package com.vimalsagarji.vimalsagarjiapp;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.Settings;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
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
import com.vimalsagarji.vimalsagarjiapp.common.CommonMethod;
import com.vimalsagarji.vimalsagarjiapp.common.CommonUrl;
import com.vimalsagarji.vimalsagarjiapp.common.Sharedpreferance;
import com.vimalsagarji.vimalsagarjiapp.fcm.Config;
import com.vimalsagarji.vimalsagarjiapp.fcm.NotificationUtils;
import com.vimalsagarji.vimalsagarjiapp.utils.ValidationUtils;
import com.ybs.countrypicker.CountryPicker;
import com.ybs.countrypicker.CountryPickerListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import ch.boye.httpclientandroidlib.NameValuePair;
import ch.boye.httpclientandroidlib.message.BasicNameValuePair;

@SuppressWarnings("ALL")
public class RegisterActivity extends AppCompatActivity {

    private EditText etName, etlastName, etEmail, etMobile, etAddress;
    private static final String TAG = RegisterActivity.class.getSimpleName();
    private Sharedpreferance sharedpreferance;
    private String strDeviceid;
    private String strDevicetoken;
    TextView txt_alreready;
    Dialog dialog;
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    ProgressDialog progressDialog;
    String randno;
    EditText edit_otp;
    CountDownTimer countDownTimer;          // built in android class CountDownTimer
    long totalTimeCountInMilliseconds;      // total count down time in milliseconds
    long timeBlinkInMilliseconds;
    ImageView img_close;
    private EditText edit_select_country;
    String countryCode;

    @SuppressLint("HardwareIds")
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_user);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        totalTimeCountInMilliseconds = 180 * 1000;      // time count for 3 minutes = 180 seconds
        timeBlinkInMilliseconds = 30 * 1000;


        if (CommonMethod.isInternetConnected(RegisterActivity.this)) {
            mRegistrationBroadcastReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {

                    try {
                        // checking for type intent filter
                        if (intent.getAction().equals(Config.REGISTRATION_COMPLETE)) {
                            // gcm successfully registered
                            // now subscribe to `global` topic to receive app wide notifications
                            FirebaseMessaging.getInstance().subscribeToTopic(Config.TOPIC_GLOBAL);
                            if (CommonMethod.isInternetConnected(RegisterActivity.this)) {
                                displayFirebaseRegId();
                            }

                        } else if (intent.getAction().equals(Config.PUSH_NOTIFICATION)) {
                            // new push notification is received

                            String message = intent.getStringExtra("message");

                            Toast.makeText(getApplicationContext(), "Push notification: " + message, Toast.LENGTH_SHORT).show();

                            Log.e("message", "---------------" + message);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };
        } else {
            Toast.makeText(RegisterActivity.this, R.string.internet, Toast.LENGTH_SHORT).show();
        }

        if (CommonMethod.isInternetConnected(RegisterActivity.this)) {
            displayFirebaseRegId();
        }
//        strDeviceToken = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID); //get android device id
        sharedpreferance = new Sharedpreferance(RegisterActivity.this);


        strDeviceid = Settings.Secure.getString(RegisterActivity.this.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        Log.e(TAG, "Device id:" + strDeviceid);
        Log.e(TAG, "Device token:" + strDevicetoken);

        img_close = (ImageView) findViewById(R.id.img_close);
        edit_select_country = (EditText) findViewById(R.id.edit_select_country);
        etName = (EditText) findViewById(R.id.etName);
        etlastName = (EditText) findViewById(R.id.etlastName);
        etEmail = (EditText) findViewById(R.id.etEmail);
        etMobile = (EditText) findViewById(R.id.etMobile);
        etAddress = (EditText) findViewById(R.id.etAddress);

        txt_alreready = (TextView) findViewById(R.id.txt_alreready);

        Button btnRegister = (Button) findViewById(R.id.btnRegister);
        TextView textview_skip = (TextView) findViewById(R.id.textview_skip);

        textview_skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharedpreferance.saveId("");
                sharedpreferance.saveEmail("");
                sharedpreferance.saveMobile("");
                sharedpreferance.savePushNotification("pushon");
                sharedpreferance.saveToken(strDevicetoken);
                moveAhead();
            }
        });

        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharedpreferance.saveId("");
                sharedpreferance.saveEmail("");
                sharedpreferance.saveMobile("");
                sharedpreferance.savePushNotification("pushon");
                sharedpreferance.saveToken(strDevicetoken);
                moveAhead();
            }
        });
        txt_alreready.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, AlreadyRegisterUser.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CommonMethod.isInternetConnected(RegisterActivity.this)) {
                    submitProfile();
                } else {
                    Toast.makeText(RegisterActivity.this, "Internet not connected.", Toast.LENGTH_SHORT).show();
                }
            }
        });


        edit_select_country.setCursorVisible(false);
        edit_select_country.setFocusableInTouchMode(false);
        edit_select_country.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final CountryPicker picker = CountryPicker.newInstance("Select Country");  // dialog title
                picker.setListener(new CountryPickerListener() {
                    @Override
                    public void onSelectCountry(String name, String code, String dialCode, int flagDrawableResID) {
                        // Implement your code here
                        Log.e("country name", "-------------" + name);
                        Log.e("country code", "-------------" + code);
                        Log.e("country dialCode", "-------------" + dialCode);
                        edit_select_country.setText(name + " (" + dialCode + ")");
                        countryCode = dialCode;
                        picker.dismiss();
                    }
                });
                picker.show(getSupportFragmentManager(), "COUNTRY_PICKER");
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
                Log.e("Firebase Reg Id is not received yet!", "");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void submitProfile() {
        String strName = etName.getText().toString();
        String strlName = etlastName.getText().toString();
        String strEmail = etEmail.getText().toString();
        String strMobile = etMobile.getText().toString();
        String strAddress = etAddress.getText().toString();

        if (TextUtils.isEmpty(edit_select_country.getText().toString())) {
            Toast.makeText(this, "Please select country.", Toast.LENGTH_LONG).show();
        } else if (TextUtils.isEmpty(strName)) {
            etName.setError("Please enter your name.");
            etName.requestFocus();
        } else if (TextUtils.isEmpty(strEmail) || !ValidationUtils.checkEmail(strEmail)) {
            etEmail.setText(strName);
            etEmail.setError("Please enter valid email.");
            etEmail.requestFocus();
        } else if (TextUtils.isEmpty(strMobile)) {
            etMobile.setError("Please enter mobile number.");
            etMobile.requestFocus();
        } else if (etMobile.getText().toString().trim().length() < 10) {
            etMobile.setError("Please enter 10 digit number.");
            etMobile.requestFocus();
        } else if (TextUtils.isEmpty(strAddress)) {
            etAddress.setError("Please enter your location.");
            etAddress.requestFocus();
        } else {
            if (CommonMethod.isInternetConnected(RegisterActivity.this)) {
                new GenrateOTP().execute();
            } else {
                Toast.makeText(RegisterActivity.this, R.string.internet, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void moveAhead() {
        Intent intentSuceess = new Intent(RegisterActivity.this, ActivityHomeMain.class);
        startActivity(intentSuceess);
        finishAffinity();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    private class PostRegisterData extends AsyncTask<String, Void, String> {
        String responseJSON = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
           /* loadingProgressDialog = KProgressHUD.create(RegisterActivity.this)
                    .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                    .setLabel("Please Wait")
                    .setCancellable(false);
            loadingProgressDialog.show();*/
            progressDialog = new ProgressDialog(RegisterActivity.this);
            progressDialog.setMessage("Please wait..");
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                ArrayList<ch.boye.httpclientandroidlib.NameValuePair> nameValuePairs = new ArrayList<>();
                nameValuePairs.add(new ch.boye.httpclientandroidlib.message.BasicNameValuePair("Name", params[0]));
                nameValuePairs.add(new ch.boye.httpclientandroidlib.message.BasicNameValuePair("DeviceID", params[1]));
                nameValuePairs.add(new ch.boye.httpclientandroidlib.message.BasicNameValuePair("EmailID", params[2]));
                nameValuePairs.add(new ch.boye.httpclientandroidlib.message.BasicNameValuePair("Address", params[3]));
                nameValuePairs.add(new ch.boye.httpclientandroidlib.message.BasicNameValuePair("Phone", params[4]));
                nameValuePairs.add(new ch.boye.httpclientandroidlib.message.BasicNameValuePair("DeviceToken", params[5]));
                responseJSON = CommonMethod.postStringResponse(CommonUrl.Main_url + "userregistration/signinsignup", nameValuePairs, RegisterActivity.this);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return responseJSON;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("response", "-----------------------------" + s);
            try {
                JSONObject jsonObject = new JSONObject(s);

                if (jsonObject.getString("status").equalsIgnoreCase("success")) {
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    JSONObject object = jsonArray.getJSONObject(0);

                    String uid = object.getString("ID");
                    String email = object.getString("EmailID");
                    String mobile = object.getString("Phone");
                    String token = object.getString("DeviceToken");

                    String Name = object.getString("Name");
                    String Phone = object.getString("DeviceToken");
                    String Address = object.getString("Address");


                    Log.e("token", "------------" + token);
                    Log.e("email", "------------" + email);

                    sharedpreferance.saveId(uid);
                    sharedpreferance.saveEmail(email);
                    sharedpreferance.saveMobile(mobile);
                    sharedpreferance.savePushNotification("pushon");
                    sharedpreferance.saveToken(token);
                    sharedpreferance.saveFirst_name(Name);
                    sharedpreferance.saveLast_name(Address);


                    Toast.makeText(RegisterActivity.this, "Congratulations! You are successfully registered!", Toast.LENGTH_SHORT).show();
                    moveAhead();
                } else {
                    Toast.makeText(RegisterActivity.this, "User not registerd please try again.", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            progressDialog.dismiss();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
//        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, new IntentFilter("otp"));
        // register GCM registration complete receiver


        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.REGISTRATION_COMPLETE));

        // register new push message receiver
        // by doing this, the activity will be notified each time a new message arrives
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.PUSH_NOTIFICATION));

        // clear the notification area when the app is opened
        NotificationUtils.clearNotifications(getApplicationContext());
    }

    @Override
    protected void onPause() {
//        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }

    private class GenrateOTP extends AsyncTask<String, Void, String> {
        String responseString = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(RegisterActivity.this);
            progressDialog.setMessage("Please wait..");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            int max = 999999;
            int min = 100000;
            randno = String.valueOf(Math.round(Math.random() * (max - min + 1) + min));
//            int mobile=Integer.parseInt(etMobile.getText().toString());

            Log.e("randno","--------------"+randno);
            ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();
            nameValuePairs.add(new BasicNameValuePair("authkey", "210431AROU1gUWMy5ad5aa00"));
            nameValuePairs.add(new BasicNameValuePair("mobile", countryCode + etMobile.getText().toString()));
            nameValuePairs.add(new BasicNameValuePair("message", "Your%20OTP%20is%20" + randno));
            nameValuePairs.add(new BasicNameValuePair("sender", "NAYISO"));
            nameValuePairs.add(new BasicNameValuePair("otp", randno));
            nameValuePairs.add(new BasicNameValuePair("otp_expiry", "1440"));
            nameValuePairs.add(new BasicNameValuePair("otp_length", "6"));
            responseString = CommonMethod.postStringResponse("https://control.msg91.com/api/sendotp.php", nameValuePairs, RegisterActivity.this);
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

                    dialog = new Dialog(RegisterActivity.this);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.dialog_getotp);
                    dialog.setCancelable(false);
                    dialog.show();

                    edit_otp = (EditText) dialog.findViewById(R.id.edit_otp);
                    Button btn_submit = (Button) dialog.findViewById(R.id.button_submit);
                    TextView resendotp = (TextView) dialog.findViewById(R.id.txtresendotp);
                    ImageView img_close = (ImageView) dialog.findViewById(R.id.img_close);
                    final TextView text_timer = (TextView) dialog.findViewById(R.id.text_timer);

                    img_close.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });

                    countDownTimer = new CountDownTimer(totalTimeCountInMilliseconds, 500) {
                        // 500 means, onTick function will be called at every 500 milliseconds

                        @Override
                        public void onTick(long leftTimeInMilliseconds) {
                            long seconds = leftTimeInMilliseconds / 1000;
                            text_timer.setText(String.format("%02d", seconds / 60) + ":" + String.format("%02d", seconds % 60));
                            // format the textview to show the easily readable format
                        }

                        @Override
                        public void onFinish() {
                            // this function will be called when the timecount is finished
                            text_timer.setText("Time up!");
                            text_timer.setVisibility(View.VISIBLE);
                        }

                    }.start();


                    resendotp.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                            new GenrateOTP().execute();
                        }
                    });

                    btn_submit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (CommonMethod.isInternetConnected(RegisterActivity.this)) {
                                new VeryfyOTP().execute();

                            } else {
                                Toast.makeText(RegisterActivity.this, R.string.internet, Toast.LENGTH_SHORT).show();
                            }

                        }
                    });

                    WindowManager.LayoutParams attrs = getWindow().getAttributes();
//                    attrs.flags &= ~WindowManager.LayoutParams.FLAG_FULLSCREEN;
                    getWindow().setAttributes(attrs);
                    dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


                } else {
                    progressDialog.dismiss();
                    Toast.makeText(RegisterActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

                }


            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (progressDialog != null) {
                progressDialog.dismiss();
            }

        }


    }

    private class VeryfyOTP extends AsyncTask<String, Void, String> {
        String responseString = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(RegisterActivity.this);
            progressDialog.setMessage("Please wait..");
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();
            nameValuePairs.add(new BasicNameValuePair("authkey", "210431AROU1gUWMy5ad5aa00"));
            nameValuePairs.add(new BasicNameValuePair("mobile", countryCode + etMobile.getText().toString()));
            nameValuePairs.add(new BasicNameValuePair("otp", edit_otp.getText().toString()));
            responseString = CommonMethod.postStringResponse("https://control.msg91.com/api/verifyRequestOTP.php", nameValuePairs, RegisterActivity.this);
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
//                    Toast.makeText(RegisterActivity.this, "Verify OTP", Toast.LENGTH_SHORT).show();
//                    Toast.makeText(RegisterActivity.this, "" + jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    if (CommonMethod.isInternetConnected(RegisterActivity.this)) {
                        new PostRegisterData().execute(CommonMethod.capitalizeString(etName.getText().toString()), strDeviceid, etEmail.getText().toString(), etAddress.getText().toString(), etMobile.getText().toString(), strDevicetoken);
                    }

                } else {
                    progressDialog.dismiss();
                    Toast.makeText(RegisterActivity.this, "" + jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
/*
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equalsIgnoreCase("otp")) {
                final String message = intent.getStringExtra("message");
                Log.e("received message", "-------------------" + message);
                if (message.length() == 6) {
                    edit_otp.setText(message);
                    new VeryfyOTP().execute();
                } else {
                    edit_otp.setText(message);
                }
                //Do whatever you want with the code here
            }
        }

    };*/


    public static void setCapitalizeTextWatcher(final EditText editText) {
        final TextWatcher textWatcher = new TextWatcher() {

            int mStart = 0;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mStart = start + count;
            }

            @Override
            public void afterTextChanged(Editable s) {
                String input = s.toString();
                String capitalizedText;
                if (input.length() < 1)
                    capitalizedText = input;
                else
                    capitalizedText = input.substring(0, 1).toUpperCase() + input.substring(1);
                if (!capitalizedText.equals(editText.getText().toString())) {
                    editText.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {

                        }

                        @Override
                        public void afterTextChanged(Editable s) {
                            editText.setSelection(mStart);
                            editText.removeTextChangedListener(this);
                        }
                    });
                    editText.setText(capitalizedText);
                }
            }
        };

        editText.addTextChangedListener(textWatcher);
    }

}
