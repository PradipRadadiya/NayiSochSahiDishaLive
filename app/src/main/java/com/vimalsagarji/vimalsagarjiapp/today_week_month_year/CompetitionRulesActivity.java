package com.vimalsagarji.vimalsagarjiapp.today_week_month_year;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.vimalsagarji.vimalsagarjiapp.R;
import com.vimalsagarji.vimalsagarjiapp.RegisterActivity;
import com.vimalsagarji.vimalsagarjiapp.activity.mainactivity.SearchActivity;
import com.vimalsagarji.vimalsagarjiapp.common.CommonMethod;
import com.vimalsagarji.vimalsagarjiapp.common.CommonUrl;
import com.vimalsagarji.vimalsagarjiapp.common.Sharedpreferance;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import ch.boye.httpclientandroidlib.NameValuePair;
import ch.boye.httpclientandroidlib.message.BasicNameValuePair;

public class CompetitionRulesActivity extends AppCompatActivity {

    private String cid, rules, minute;
    Sharedpreferance sharedpreferance;
    private String status;
    private Button button_start;
    private String starttime;
    private String endtime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_competition_rules);
        sharedpreferance = new Sharedpreferance(CompetitionRulesActivity.this);
        Intent intent = getIntent();
        cid = intent.getStringExtra("cid");
        rules = intent.getStringExtra("rules");
        minute = intent.getStringExtra("minute");
        starttime = intent.getStringExtra("starttime");
        endtime = intent.getStringExtra("endtime");
        bindID();
        toolbarClick();

        new UserCheckExam().execute();

    }

    private void bindID() {
        TextView txt_rules = (TextView) findViewById(R.id.txt_rules);
        txt_rules.setText(CommonMethod.decodeEmoji(rules));
        button_start = (Button) findViewById(R.id.button_start);
        button_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!sharedpreferance.getId().equalsIgnoreCase("")) {


                    //                    if (status.equalsIgnoreCase("1")) {

                    button_start.setEnabled(false);
                    Intent intent = new Intent(v.getContext(), CompetitionQuestionActivity.class);
                    intent.putExtra("cid", cid);
                    intent.putExtra("minute", minute);
                    intent.putExtra("starttime", starttime);
                    intent.putExtra("endtime", endtime);

                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
//                    }else{
//                        Toast.makeText(CompetitionRulesActivity.this, "You have already getting competition.", Toast.LENGTH_SHORT).show();
//                    }
//

                } else {
                    showSnackbar(v);
                }

            }
        });

    }


    //Method to show the snackbar
    private void showSnackbar(View v) {
        //Creating snackbar
        Snackbar snackbar = Snackbar.make(v, R.string.notregister, Snackbar.LENGTH_LONG);

        //Adding action to snackbar
        snackbar.setAction("Register", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Displaying another snackbar when user click the action for first snackbar
//                Snackbar s = Snackbar.make(v, "Register", Snackbar.LENGTH_LONG);
//                s.show();
                Intent intent = new Intent(CompetitionRulesActivity.this, RegisterActivity.class);
                startActivity(intent);
                finishAffinity();
            }
        });

        //Customizing colors
        snackbar.setActionTextColor(Color.WHITE);
        View view = snackbar.getView();
        view.setBackground(getResources().getDrawable(R.drawable.back_gradiant));
        TextView textView = (TextView) view.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(Color.WHITE);

        //Displaying snackbar
        snackbar.show();
    }

    private void toolbarClick() {
        ImageView imgarrorback;
        TextView txt_title;
        ImageView img_search;

        txt_title = (TextView) findViewById(R.id.txt_title);
        imgarrorback = (ImageView) findViewById(R.id.imgarrorback);
        img_search = (ImageView) findViewById(R.id.img_search);
        txt_title.setText("Competition");
        img_search.setVisibility(View.VISIBLE);
        imgarrorback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        img_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CompetitionRulesActivity.this, SearchActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }

        });

    }

    @SuppressLint("StaticFieldLeak")
    private class UserCheckExam extends AsyncTask<String, Void, String> {
        String responseJson = "";
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(CompetitionRulesActivity.this);
            progressDialog.setMessage("Please wait..");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }


        @Override
        protected String doInBackground(String... strings) {

            ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();
            nameValuePairs.add(new BasicNameValuePair("cid", cid));
            nameValuePairs.add(new BasicNameValuePair("uid", sharedpreferance.getId()));

            responseJson = CommonMethod.postStringResponse(CommonUrl.Main_url + "competition/checkcompetitionviewed", nameValuePairs, CompetitionRulesActivity.this);
            return responseJson;

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("response", "----------------------" + s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                if (jsonObject.getString("status").equalsIgnoreCase("success")) {

                    progressDialog.dismiss();

//                    status = jsonObject.getString("status");
                  /*  AlertDialog.Builder builder = new AlertDialog.Builder(CompetitionRulesActivity.this);
                    builder.setMessage("Thank you! \nYou have already getting this competition!")
                            .setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    //do things
                                    dialog.dismiss();
                                    button_start.setEnabled(false);
                                }
                            });

                    AlertDialog alert = builder.create();
                    alert.show();*/


                    final Dialog dialog = new Dialog(CompetitionRulesActivity.this);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.alert_dialog);

                    Button btn_ok = (Button) dialog.findViewById(R.id.btn_ok);
                    btn_ok.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                            button_start.setEnabled(false);
                            finish();
                        }
                    });

                    TextView txt_title = (TextView) dialog.findViewById(R.id.txt_title);
                    TextView txt_description = (TextView) dialog.findViewById(R.id.txt_description);
                    TextView txt_date = (TextView) dialog.findViewById(R.id.txt_date);
                    TextView txt_time = (TextView) dialog.findViewById(R.id.txt_time);


                    dialog.show();
                    dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                    dialog.getWindow().setGravity(Gravity.CENTER);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));



                } else {


                    progressDialog.dismiss();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }
}
