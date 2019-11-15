package com.vimalsagarji.vimalsagarjiapp.activity.mainactivity;

import android.annotation.SuppressLint;
import android.app.MediaRouteButton;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.vimalsagarji.vimalsagarjiapp.R;
import com.vimalsagarji.vimalsagarjiapp.common.CommonMethod;
import com.vimalsagarji.vimalsagarjiapp.common.CommonUrl;
import com.vimalsagarji.vimalsagarjiapp.common.Sharedpreferance;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.BreakIterator;
import java.util.ArrayList;

import ch.boye.httpclientandroidlib.NameValuePair;
import ch.boye.httpclientandroidlib.message.BasicNameValuePair;

public class ProfileActivity extends AppCompatActivity {
    private Sharedpreferance sharedpreferance;
    private EditText edit_fnm;
    private EditText edit_email;
    private EditText edit_mobile;
    private EditText edit_location;
    private TextView txt_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        bindToolbar();
        bindId();

    }

    private void bindId() {
        sharedpreferance = new Sharedpreferance(ProfileActivity.this);
        edit_fnm = findViewById(R.id.edit_fnm);
        edit_email = findViewById(R.id.edit_email);
        edit_mobile = findViewById(R.id.edit_mobile);
        edit_location = findViewById(R.id.edit_location);
        txt_name = findViewById(R.id.txt_name);
        Button btn_update = findViewById(R.id.btn_update);

        txt_name.setText(sharedpreferance.getFirst_name());
        edit_fnm.setText(sharedpreferance.getFirst_name());
        edit_email.setText(sharedpreferance.getEmail());
        edit_mobile.setText(sharedpreferance.getMobile());
        edit_location.setText(sharedpreferance.getLast_name());

        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (TextUtils.isEmpty(edit_fnm.getText().toString())) {
                    edit_fnm.setError("Please enter name.");
                    edit_fnm.requestFocus();
                } else if (TextUtils.isEmpty(edit_email.getText().toString())) {
                    edit_email.setError("Please enter email.");
                    edit_email.requestFocus();
                } else if (TextUtils.isEmpty(edit_mobile.getText().toString())) {
                    edit_mobile.setError("Please enter mobile.");
                    edit_mobile.requestFocus();
                } else if (TextUtils.isEmpty(edit_location.getText().toString())) {
                    edit_location.setError("Please enter location.");
                    edit_location.requestFocus();
                } else {
                    new updateProfile().execute(sharedpreferance.getId(), CommonMethod.capitalizeString(edit_fnm.getText().toString()), edit_email.getText().toString(), CommonMethod.capitalizeString(edit_location.getText().toString()));
                }

            }
        });

    }


    @SuppressLint("StaticFieldLeak")
    private class updateProfile extends AsyncTask<String, Void, String> {

        ProgressDialog progressDialog;
        String responseJson = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog=new ProgressDialog(ProfileActivity.this);
            progressDialog.setMessage("Loading..");
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();
            nameValuePairs.add(new BasicNameValuePair("uid", strings[0]));
            nameValuePairs.add(new BasicNameValuePair("name", strings[1]));
            nameValuePairs.add(new BasicNameValuePair("email", strings[2]));
            nameValuePairs.add(new BasicNameValuePair("address", strings[3]));
            responseJson = CommonMethod.postStringResponse(CommonUrl.Main_url + "userregistration/editprofile", nameValuePairs, ProfileActivity.this);

            return responseJson;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("response", "------------------" + s);
            progressDialog.dismiss();
            try {
                JSONObject jsonObject=new JSONObject(s);
                if (jsonObject.getString("status").equalsIgnoreCase("success")){
                    Toast.makeText(ProfileActivity.this, "Profile updated successfully.", Toast.LENGTH_SHORT).show();
                    sharedpreferance.saveFirst_name(edit_fnm.getText().toString());
                    sharedpreferance.saveEmail(edit_email.getText().toString());
                    sharedpreferance.saveLast_name(edit_location.getText().toString());
                    txt_name.setText(sharedpreferance.getFirst_name());

                }else{
                    Toast.makeText(ProfileActivity.this, "Profile not updated.", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

    }


    private void bindToolbar() {
        TextView txt_title = findViewById(R.id.txt_title);
        ImageView img_search = findViewById(R.id.img_search);
        ImageView imgarrorback = findViewById(R.id.imgarrorback);
        txt_title.setText("My Profile");
        img_search.setVisibility(View.GONE);
        imgarrorback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });

    }
}
